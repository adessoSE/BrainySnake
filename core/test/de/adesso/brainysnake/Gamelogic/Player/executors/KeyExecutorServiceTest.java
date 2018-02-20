package de.adesso.brainysnake.Gamelogic.Player.executors;

import de.adesso.brainysnake.Gamelogic.Player.SlowPlayer;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.function.Supplier;

import static org.junit.Assert.*;

public class KeyExecutorServiceTest {
    public static final int MAX_PROCESSING_TIME = 100;

    @Test
    public void processSingleTurn() {
        List<TestInstance> testInstances = new ArrayList<>();

        testInstances.add(new TestInstance());
        testInstances.add(new TestInstance());

        TestKeyExecutorService testKeyExecutorService = new TestKeyExecutorService(testInstances, MAX_PROCESSING_TIME);
        testKeyExecutorService.process();
    }

    @Test
    public void processMultiTurn() {
        List<TestInstance> testInstances = new ArrayList<>();

        testInstances.add(new TestInstance());
        testInstances.add(new TestInstance());

        TestKeyExecutorService testKeyExecutorService = new TestKeyExecutorService(testInstances, MAX_PROCESSING_TIME);

        for(int i = 0; i < 5; i++) {
            testKeyExecutorService.process();
        }
    }

    @Test
    public void processMultiTurnSlow() {
        List<TestInstance> testInstances = new ArrayList<>();


        testInstances.add(new NormalTestInstance());
        testInstances.add(new NormalTestInstance());
        testInstances.add(new SlowTestInstance());


        TestKeyExecutorService testKeyExecutorService = new TestKeyExecutorService(testInstances, MAX_PROCESSING_TIME);

        for(int i = 0; i < 5; i++) {
            System.out.println("new round");
            testKeyExecutorService.process();
        }
    }

    @Test
    public void processMultiTurnMultiSlow() {
        List<TestInstance> testInstances = new ArrayList<>();


        testInstances.add(new NormalTestInstance());
        testInstances.add(new NormalTestInstance());
        testInstances.add(new SlowTestInstance());
        testInstances.add(new SlowTestInstance());



        TestKeyExecutorService testKeyExecutorService = new TestKeyExecutorService(testInstances, MAX_PROCESSING_TIME);

        for(int i = 0; i < 5; i++) {
            System.out.println("new round");
            testKeyExecutorService.process();
        }
    }


    @Test
    public void processMultiTurnMultiToSlow() throws InterruptedException {
        List<TestInstance> testInstances = new ArrayList<>();


        testInstances.add(new NormalTestInstance());
        testInstances.add(new NormalTestInstance());
        testInstances.add(new ToSlowTestInstance());
        testInstances.add(new SlowTestInstance());



        TestKeyExecutorService testKeyExecutorService = new TestKeyExecutorService(testInstances, MAX_PROCESSING_TIME);

        for(int i = 0; i < 4; i++) {
            Thread.sleep(10);
            System.out.println("new round");
            Map<TestInstance, Optional<TestResult>> process = testKeyExecutorService.process();
            assertAll(process);
                Thread.sleep(10);
            System.out.println("");
        }
    }

    @Test
    public void processMultiTurnMultiToSlow2() throws InterruptedException {
        List<TestInstance> testInstances = new ArrayList<>();

        testInstances.add(new ToSlowTestInstance());
        testInstances.add(new NormalTestInstance());
        testInstances.add(new NormalTestInstance());
        testInstances.add(new SlowTestInstance());



        TestKeyExecutorService testKeyExecutorService = new TestKeyExecutorService(testInstances, MAX_PROCESSING_TIME);

        for(int i = 0; i < 4; i++) {
            Thread.sleep(10);
            System.out.println("new round");
            Map<TestInstance, Optional<TestResult>> process = testKeyExecutorService.process();
            assertAll(process);
            Thread.sleep(10);
            System.out.println("");
        }
    }

    @Test
    public void processMultiTurnMultiToSlow3() throws InterruptedException {
        List<TestInstance> testInstances = new ArrayList<>();

        ToSlowTestInstance toSlow1 = new ToSlowTestInstance();
        NormalTestInstance normalTestInstance1 = new NormalTestInstance();
        NormalTestInstance normalTestInstance2 = new NormalTestInstance();
        ToSlowTestInstance toSlow2 = new ToSlowTestInstance();
        SlowTestInstance slow = new SlowTestInstance();

        testInstances.add(toSlow1);
        testInstances.add(normalTestInstance1);
        testInstances.add(normalTestInstance2);
        testInstances.add(toSlow2);
        testInstances.add(slow);

        TestKeyExecutorService testKeyExecutorService = new TestKeyExecutorService(testInstances, MAX_PROCESSING_TIME);

        for(int i = 0; i < 4; i++) {
            Thread.sleep(10);
            System.out.println("new round");
            Map<TestInstance, Optional<TestResult>> process = testKeyExecutorService.process();
            assertAll(process);

            Thread.sleep(10);
            System.out.println("");
        }
    }

    public void assertAll( Map<TestInstance, Optional<TestResult>> process) {
        process.forEach((instance, optional) -> {
            assertSingle(instance, optional);
        });
    }

    public void assertSingle(TestInstance instance, Optional<TestResult> optional) {

        if(instance instanceof ToSlowTestInstance) {
            assertFalse(optional.isPresent());
        } else {
            assertTrue(optional.isPresent());
        }
    }

    public void assertSingle(NormalTestInstance normalTestInstance, Optional<TestResult> optional) {
        assertTrue(optional.isPresent());
    }

    class TestKeyExecutorService extends KeyExecutorService<TestResult, UUID, TestInstance> {

        public TestKeyExecutorService(List<TestInstance> instancesToCall, int maxProcessingTimeMs) {
            super(instancesToCall, maxProcessingTimeMs);
        }

        @Override
        public Supplier<TestResult> functionCall(TestInstance caller) {
            return caller::someAction;
        }

        @Override
        public UUID getKey(TestInstance caller) {
            return caller.getUuid();
        }

        @Override
        public void onCancel(TestInstance instance, UUID uuid) {
            System.out.println("canceled " + uuid);
        }

        @Override
        public void onSuccess(TestInstance instance, UUID uuid) {
            System.out.println("success " + uuid);
        }
    }

    class NormalTestInstance extends TestInstance {
        @Override
        public TestResult someAction() {
            System.out.println(getUuid() + ": I am a normal process");
            return super.someAction();
        }
    }

    class SlowTestInstance extends TestInstance {
        @Override
        public TestResult someAction() {
            System.out.println(getUuid() + ": I am a slow process");
            try {
                Thread.sleep(51);
            } catch (InterruptedException e) {
                System.out.println(getUuid() + ": I was Interrupted");
                e.printStackTrace();
            }
            return super.someAction();
        }
    }

    class ToSlowTestInstance extends TestInstance {
        @Override
        public TestResult someAction() {
            System.out.println(getUuid() + ": I am a to slow process");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                System.out.println(getUuid() + ": I was Interrupted, because I am to slow");
                //e.printStackTrace();
            }
            return super.someAction();
        }
    }

    class TestInstance {

        private final UUID uuid;

        public TestInstance() {
            this.uuid = UUID.randomUUID();
        }

        public UUID getUuid() {
            return uuid;
        }

        public TestResult someAction() {
            TestResult  testResult = new TestResult();
            testResult.setData((int) (Math.random() * 100));
            return testResult;
        }
    }

    public class TestResult {
        int data = 0;

        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }
    }
}