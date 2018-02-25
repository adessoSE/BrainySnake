package de.adesso.brainysnake.gamelogic.player.executors;

import de.adesso.brainysnake.gamelogic.utils.time.Timekeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * KeyExecutorService handles all thread call in the game
 *
 * @param <V>   Expected competition result
 * @param <KEY> identifies the competing instance
 * @param <IN>  instance to compute
 */
public abstract class KeyExecutorService<V, KEY, IN> {

    public static final Logger LOGGER = LoggerFactory.getLogger(KeyExecutorService.class);

    private final BlockingQueue<Future<KeyResult>> completionQueue;

    private final Map<KEY, IN> executableInstances;
    private final Map<KEY, ExecutorService> executorMap;
    private final Map<KEY, FutureTask> futureTaskMap;
    /**
     * To prevent any future mismatch, we will iterate on this number
     */
    private final int numberOfExecutions;
    private Timekeeper timekeeper;

    public KeyExecutorService(List<IN> instancesToCall, int maxProcessingTimeMs) {
        this.executableInstances = new HashMap<>();
        this.executorMap = new HashMap<>();
        this.futureTaskMap = new HashMap<>();

        this.completionQueue = new LinkedBlockingQueue<Future<KeyResult>>();
        ;


        this.numberOfExecutions = instancesToCall.size();

        this.timekeeper = Timekeeper.start(maxProcessingTimeMs);

        instancesToCall.forEach(inst -> {
            executableInstances.put(getKey(inst), inst);
            executorMap.put(getKey(inst), Executors.newFixedThreadPool(1));
        });
    }

    public Map<IN, Optional<V>> process() {
        // stop all running tasks
        this.stopAll();
        // remove all processed futures
        this.completionQueue.clear();

        // submit all new tasks
        this.submitAll();
        //this.executeAll();

        // compute the results
        return this.getResults();
    }

    void stopAll() {
        this.futureTaskMap.forEach((key, futureTask) -> {
            if (futureTask.isDone()) {
                this.onSuccess(this.executableInstances.get(key), key);
            } else if (futureTask.isCancelled()) {
                // if the task was already canceled
                this.onCancel(this.executableInstances.get(key), key);
            } else {

                // force interrupt
                try {
                    // this will cause an exception
                    futureTask.cancel(true);
                } catch (Exception e) {
                    LOGGER.error("Function call on {} was forced to interrupt", key, e);
                } finally {
                    ThreadPoolExecutor executor = (ThreadPoolExecutor) this.executorMap.get(key);
                    executor.purge();
                    this.onCancel(this.executableInstances.get(key), key);
                }
            }
        });
        this.futureTaskMap.clear();
    }

    void submitAll() {
        this.callAll(this::submit);
    }

    void executeAll() {
        this.callAll(this::execute);
    }

    private void callAll(BiFunction<KeyCallable, ExecutorService, FutureTask<KeyResult>> caller) {
        this.executableInstances.forEach((key, IN) -> {
            // create a new identifiable function call
            KeyCallable keyCallable = new KeyCallable(key, functionCall(IN));
            // get the executor
            ExecutorService executorService = this.executorMap.get(key);
            // perform the call
            FutureTask<KeyResult> futureTask = caller.apply(keyCallable, executorService);
            // memorize the running task
            this.futureTaskMap.put(key, futureTask);
        });
    }

    Map<IN, Optional<V>> getResults() {
        Map<IN, Optional<V>> instanceResultMap = new HashMap<>();

        this.getAllResults().forEach((key, v) -> {
            instanceResultMap.put(executableInstances.get(key), v);
        });
        return instanceResultMap;
    }

    /**
     * Returns all results form the current execution
     * incomplete Tasks will get an empty Optional as result
     */
    Map<KEY, Optional<V>> getAllResults() {
        Map<KEY, Optional<V>> results = new HashMap<>();
        Map<KEY, Optional<V>> completedResults = this.getCompletedResults();

        // Puts an empty result for each result that were not completed
        this.executableInstances.keySet().forEach(key -> {
            results.put(key, completedResults.containsKey(key) ? completedResults.get(key) : Optional.empty());
        });

        return results;
    }

    /**
     * Get all completed results monitored by the {@link Timekeeper}
     *
     * @return all completed tasks
     */
    Map<KEY, Optional<V>> getCompletedResults() {
        Map<KEY, Optional<V>> resultMap = new HashMap<KEY, Optional<V>>();

        // start time
        this.timekeeper.reset();

        for (int i = 0; i < this.numberOfExecutions; i++) {
            try {
                // Retrieves and removes the head of this queue, waiting up to the
                // specified wait time if necessary for an element to become available.
                Future<KeyResult> future = this.completionQueue.poll(this.timekeeper.getRemainingTime(), this.timekeeper.getTimeUnit());

                if (future == null) {
                    continue;
                }

                // could cause an ExecutionException {@link #submit(Object, Object)}
                KeyResult<KEY, V> keyResult = future.get();
                // Create an Optional that is empty if the result is null
                Optional<V> resultValue = Optional.ofNullable(keyResult.getResultValue());
                // Store result
                resultMap.put(keyResult.getIdentifier(), resultValue);
            } catch (CancellationException e) {
                LOGGER.error("A running task was canceled", e);
            } catch (InterruptedException e) {
                // This should never happen
                LOGGER.error("Polling future from Completion Queue was interrupted", e);
            } catch (ExecutionException e) {
                // Every task should handle its exceptions!
                LOGGER.error("Exception caused by executed task", e);
            } catch (Exception e) {
                // Every task should handle its exceptions!
                LOGGER.error("ExecutionException caused by executed task", e);
            }

        }
        return resultMap;
    }

    /**
     * Executes the Callable with the executor, the result will be stored in the completionQueue
     * <p>
     * If the callable task generates some Throwable,
     * this will cause the UncaughtExceptionHandler for the Thread running the Task
     * The default UncaughtExceptionHandler will print the Throwables stack trace to System.err,
     * if no custom UncaughtExceptionHandler is installed
     *
     * @param callable task to execute within a thread
     * @param executor ExecutorService to execute the task (callable)
     * @return A cancellable asynchronous computation
     */
    private FutureTask<KeyResult> execute(KeyCallable callable, ExecutorService executor) {
        return this.start(callable, executor::execute);
    }

    /**
     * Executes the Callable with the executor, the result will be stored in the completionQueue
     * <p>
     * If the callable task generates some Throwable,
     * the Trowable will be bind to the Future.
     * Calling get() on the Future, will throw the ExecutionException
     * withe the original Trowable as its cause
     *
     * @param callable task to execute within a thread
     * @param executor ExecutorService to execute the task (callable)
     * @return A cancellable asynchronous computation
     */
    private FutureTask<KeyResult> submit(KeyCallable callable, ExecutorService executor) {
        return this.start(callable, executor::submit);
    }

    /**
     * Executes the Callable with the executor, the result will be stored in the completionQueue
     *
     * @param callable task to run
     * @param executor runnable to run the callable
     * @return A cancellable asynchronous computation
     */
    private FutureTask<KeyResult> start(KeyCallable callable, Consumer<Runnable> executor) {
        FutureTask<KeyResult> futureTask = new FutureTask(callable);
        QueueingFuture queueingFuture = new QueueingFuture(futureTask);
        executor.accept(queueingFuture);
        return futureTask;
    }

    public abstract Supplier<V> functionCall(IN caller);

    public abstract KEY getKey(IN caller);

    /**
     * tells whether {@link #functionCall(Object)} was canceled
     *
     * @param instance instance on which the als call was performed
     * @param key      identifies the caller instance
     */
    public abstract void onCancel(IN instance, KEY key);

    /**
     * Maintenance method
     * Tells whether {@link #functionCall(Object)} was successful
     *
     * @param instance instance on which the als call was performed
     * @param key      identifies the caller instance
     */
    public void onSuccess(IN instance, KEY key) {
        // normally we don't have to do anything of everything is fine
    }

    /**
     * FutureTask extension to enqueue upon completion
     */
    private class QueueingFuture extends FutureTask<Void> {
        private final Future<KeyResult> task;

        QueueingFuture(RunnableFuture<KeyResult> task) {
            super(task, null);
            this.task = task;
        }

        // if the task finishes add the result to completion Queue
        protected void done() {
            completionQueue.add(task);
        }
    }

    public class KeyCallable implements Callable<KeyResult<KEY, V>> {

        private Supplier<V> execCall;
        private KEY key;

        public KeyCallable(KEY key, Supplier<V> execCall) {
            this.execCall = execCall;
            this.key = key;
        }

        @Override
        public KeyResult<KEY, V> call() throws Exception {
            V processedResult = this.execCall.get();
            return new KeyResult<KEY, V>(this.key, processedResult);
        }
    }

    public class KeyResult<KEY, V> {

        private KEY identifier;
        private V resultValue;

        public KeyResult(KEY identifier, V resultValue) {
            this.identifier = identifier;
            this.resultValue = resultValue;
        }

        public KEY getIdentifier() {
            return identifier;
        }

        public V getResultValue() {
            return resultValue;
        }
    }
}
