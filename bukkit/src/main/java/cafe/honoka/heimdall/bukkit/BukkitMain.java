package cafe.honoka.heimdall.bukkit;

import cafe.honoka.heimdall.core.Core;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class BukkitMain extends JavaPlugin {

    private Core core;

    @Override
    public void onEnable() {
        Metrics metrics = new Metrics(this, 28321);
        this.core = new Core(new BukkitAdapter(this));
    }

    public void onDisable() {
        if (this.core != null) {
            this.core.shutdown();
        }
    }
}
