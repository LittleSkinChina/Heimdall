package cafe.honoka.heimdall.bungee;

import cafe.honoka.heimdall.core.Core;
import net.md_5.bungee.api.plugin.Plugin;
import org.bstats.bungeecord.Metrics;

public final class BungeeMain extends Plugin {

    private Core core;

    @Override
    public void onEnable() {
        Metrics metrics = new Metrics(this, 28322);
        this.core = new Core(new BungeeAdapter(this));
    }

    public void onDisable() {
        if (this.core != null) {
            this.core.shutdown();
        }
    }
}
