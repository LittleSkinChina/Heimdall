package cafe.honoka.heimdall.bungee.listeners;

import cafe.honoka.heimdall.core.Result;
import cafe.honoka.heimdall.core.logger.JulLogger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

public class LoginEventListener implements Listener {

    private final Plugin plugin;
    private final BungeeComponentSerializer serializer;
    private final JulLogger logger;
    private final Function<UUID, CompletableFuture<Result>> check;
    private final Supplier<Component> getExKickMsg;

    public LoginEventListener(
        Plugin plugin,
        BungeeComponentSerializer serializer,
        JulLogger logger,
        Function<UUID, CompletableFuture<Result>> check,
        Supplier<Component> getExKickMsg
    ) {
        this.plugin = plugin;
        this.serializer = serializer;
        this.logger = logger;
        this.check = check;
        this.getExKickMsg = getExKickMsg;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(LoginEvent event) {
        if (!event.isCancelled()) {
            event.registerIntent(this.plugin);
            UUID id = event.getConnection().getUniqueId();
            this.check.apply(id).whenComplete((result, ex) -> {
                if (ex != null) {
                    this.logger.error(ex.getMessage(), ex);
                    event.setCancelled(true);
                    event.setCancelReason(this.serializer.serialize(this.getExKickMsg.get()));
                } else if (!result.good) {
                    event.setCancelled(true);
                    event.setCancelReason(this.serializer.serialize(result.message));
                }
                event.completeIntent(this.plugin);
            });
        }
    }
}
