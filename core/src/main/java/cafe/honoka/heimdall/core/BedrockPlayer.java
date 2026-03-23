package cafe.honoka.heimdall.core;

import java.util.UUID;

public class BedrockPlayer {
    public final String xuid;
    public final UUID linked;

    protected BedrockPlayer(String xuid, UUID linked) {
        this.xuid = xuid;
        this.linked = linked;
    }
}
