package de.adesso.brainysnake.Gamelogic.Player;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayerController {

    private List<PlayerHandler> playerHandlerList;
    private ExecutorService executorService;

    public PlayerController(List<PlayerHandler> playerHandlerList) {
        this.playerHandlerList = playerHandlerList;

        // We need a Thread for every player
        executorService = Executors.newFixedThreadPool(this.playerHandlerList.size());
    }

    public void pushPlayerState() {

    }



    public void shutdown() {
        // This will make the executor accept no new threads
        // and finish all existing threads in the queue
        executorService.shutdownNow();

        // Wait until all threads are finish
        //executorService.awaitTermination();
    }



    class PlayerStatePush implements Runnable {

        private PlayerHandler playerHandler;

        public PlayerStatePush(PlayerHandler playerHandler) {
            this.playerHandler = playerHandler;
        }

        @Override
        public void run() {
            playerHandler.sendPlayerState();
        }
    }
}
