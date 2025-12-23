package cafe.honoka.heimdall.bukkit;

import cafe.honoka.heimdall.core.LCommands;
import cafe.honoka.heimdall.core.Core;
import cafe.honoka.heimdall.core.PlatformAdapter;
import cafe.honoka.heimdall.core.Result;
import cafe.honoka.heimdall.core.logger.JulLogger;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.adventure.bukkit.platform.LiteAdventurePlatformExtension;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.platform.bukkit.BukkitComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

public class BukkitAdapter extends PlatformAdapter {

    private final Plugin plugin;
    private final BukkitAudiences audiences;
    private final LegacyComponentSerializer serializer;
    private final Server server;
    private LiteCommands<CommandSender> lcmds;

    public BukkitAdapter(
        final Plugin plugin
    ) {
        super(
            plugin.getServer().getName(),
            plugin.getServer().getVersion(),
            new JulLogger(plugin.getLogger()),
            plugin.getDataFolder().toPath()
        );
        this.plugin = plugin;
        this.audiences = BukkitAudiences.create(plugin);
        this.serializer = BukkitComponentSerializer.legacy();
        this.server = plugin.getServer();
    }

    @Override
    public boolean checkOnlineMode() {
        return this.server.getOnlineMode();
    }

    @Override
    public void stopServer() {
        this.server.shutdown();
    }

    @Override
    public void onServerStop() {
        this.lcmds.unregister();
        this.audiences.close();
    }

    @Override
    public void registerCommands(Core core) {
        this.lcmds = LiteBukkitFactory
            .builder(this.plugin)
            .commands(new LCommands(core))
            .extension(
                new LiteAdventurePlatformExtension<>(this.audiences),
                config -> config.colorizeArgument(true).serializer(this.serializer)
            )
            .build();
    }

    @Override
    public void registerLoginEventListener(
        Function<UUID, CompletableFuture<Result>> check,
        Supplier<Component> getExKickMsg
    ) {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.HIGHEST)
            public void onPreLogin(AsyncPlayerPreLoginEvent event) {
                if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
                    return;
                }

                UUID id = event.getUniqueId();
                try {
                    Result result = check.apply(id).get();
                    if (!result.good) {
                        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, serializer.serialize(result.message));
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, serializer.serialize(getExKickMsg.get()));
                }
            }
        }, this.plugin);
    }
}
