package de.adesso.brainysnake.Gamelogic.Player;

import de.adesso.brainysnake.Gamelogic.Player.executors.KeyExecutorService;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class PlayerStatePushExecutorService extends KeyExecutorService<Boolean, UUID, PlayerHandler> {

    public PlayerStatePushExecutorService(List<PlayerHandler> playerHandlers, int maxProcessingTimeMs) {
        super(playerHandlers, maxProcessingTimeMs);
    }

    @Override
    public Supplier<Boolean> functionCall(PlayerHandler playerHandler) {
        return playerHandler::sendPlayerState;
    }

    @Override
    public UUID getKey(PlayerHandler caller) {
        return caller.getPlayerIdentifier();
    }

    @Override
    public void onCancel(PlayerHandler instance, UUID uuid) {

    }
}
