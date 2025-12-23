package cafe.honoka.heimdall.bungee;

import cafe.honoka.heimdall.bungee.listeners.LoginEventListener;
import cafe.honoka.heimdall.core.LCommands;
import cafe.honoka.heimdall.core.Core;
import cafe.honoka.heimdall.core.PlatformAdapter;
import cafe.honoka.heimdall.core.Result;
import cafe.honoka.heimdall.core.logger.JulLogger;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.adventure.bukkit.platform.LiteAdventurePlatformExtension;
import dev.rollczi.litecommands.bungee.LiteBungeeFactory;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

public class BungeeAdapter extends PlatformAdapter {

    private final Plugin plugin;
    private final ProxyServer server;
    private final BungeeAudiences audiences;
    private final BungeeComponentSerializer serializer;
    private LiteCommands<CommandSender> lcmds;

    public BungeeAdapter(final Plugin plugin) {
        super(
            plugin.getProxy().getName(),
            plugin.getProxy().getVersion(),
            new JulLogger(plugin.getLogger()),
            plugin.getDataFolder().toPath()
        );
        this.plugin = plugin;
        this.server = plugin.getProxy();
        this.audiences = BungeeAudiences.create(plugin);
        this.serializer = BungeeComponentSerializer.get();
    }

    @Override
    public boolean checkOnlineMode() {
        return this.server.getConfig().isOnlineMode();
    }

    @Override
    public void stopServer() {
        this.server.stop();
    }

    @Override
    public void onServerStop() {
        this.lcmds.unregister();
        this.audiences.close();
    }

    @Override
    public void registerCommands(Core core) {
        this.lcmds = LiteBungeeFactory
            .builder(this.plugin)
            .commands(new LCommands(core))
            .extension(
                new LiteAdventurePlatformExtension<>(this.audiences),
                config -> config
                    .miniMessage(true)
                    .legacyColor(true)
                    .colorizeArgument(true)
            )
            .build();
    }

    @Override
    public void registerLoginEventListener(Function<UUID, CompletableFuture<Result>> check, Supplier<Component> getExKickMsg) {
        this.server.getPluginManager().registerListener(
            this.plugin,
            new LoginEventListener(
                this.plugin,
                this.serializer,
                (JulLogger) this.logger,
                check,
                getExKickMsg
            )
        );
    }
}
