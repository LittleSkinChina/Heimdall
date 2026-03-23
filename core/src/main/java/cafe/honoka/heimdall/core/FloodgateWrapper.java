package cafe.honoka.heimdall.core;

import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.geysermc.floodgate.util.LinkedPlayer;

import java.util.UUID;

public class FloodgateWrapper {
    private final FloodgateApi api;

    protected FloodgateWrapper() {
        this.api = FloodgateApi.getInstance();
    }

    protected boolean isBedrockPlayer(UUID uuid) {
        return this.api.isFloodgatePlayer(uuid);
    }

    protected BedrockPlayer getBedrockPlayer(UUID id) {
        FloodgatePlayer player = this.api.getPlayer(id);
        String xuid = player.getXuid();
        LinkedPlayer linkedPlayer = player.getLinkedPlayer();
        UUID linked = linkedPlayer == null ? null : linkedPlayer.getJavaUniqueId();
        return new BedrockPlayer(xuid, linked);
    }
}
