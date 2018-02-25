package de.adesso.brainysnake.gamelogic.player;

import de.adesso.brainysnake.gamelogic.player.executors.KeyExecutorService;
import de.adesso.brainysnake.playercommon.PlayerUpdate;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class PlayerUpdateGetExecutorService extends KeyExecutorService<PlayerUpdate, UUID, PlayerHandler> {

    public PlayerUpdateGetExecutorService(List<PlayerHandler> instancesToCall, int maxProcessingTimeMs) {
        super(instancesToCall, maxProcessingTimeMs);
    }

    @Override
    public Supplier<PlayerUpdate> functionCall(PlayerHandler caller) {
        return caller::requestPlayerUpdate;
    }

    @Override
    public UUID getKey(PlayerHandler caller) {
        return caller.getPlayerIdentifier();
    }

    @Override
    public void onCancel(PlayerHandler instance, UUID uuid) {

    }
}
