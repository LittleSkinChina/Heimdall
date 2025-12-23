package cafe.honoka.heimdall.velocity;

import cafe.honoka.heimdall.core.LCommands;
import cafe.honoka.heimdall.core.Core;
import cafe.honoka.heimdall.core.PlatformAdapter;
import cafe.honoka.heimdall.core.Result;
import cafe.honoka.heimdall.core.logger.Slf4jLogger;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.AwaitingEventExecutor;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.adventure.LiteAdventureExtension;
import dev.rollczi.litecommands.velocity.LiteVelocityFactory;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

public class VelocityAdapter extends PlatformAdapter {

    private final VelocityMain plugin;
    private final ProxyServer server;
    private LiteCommands<CommandSource> lcmds;

    @Inject
    public VelocityAdapter(
        final VelocityMain plugin,
        final ProxyServer server,
        final Logger logger,
        final @DataDirectory Path dataDirectory
    ) {
        super(
            server.getVersion().getName(),
            server.getVersion().getVersion(),
            new Slf4jLogger(logger),
            dataDirectory
        );
        this.plugin = plugin;
        this.server = server;
    }

    @Override
    public boolean checkOnlineMode() {
        return this.server.getConfiguration().isOnlineMode();
    }

    @Override
    public void stopServer() {
        this.server.shutdown();
    }

    @Override
    public void onServerStop() {
        this.lcmds.unregister();
    }

    @Override
    public void registerCommands(Core core) {
        this.lcmds = LiteVelocityFactory
            .builder(this.server)
            .commands(new LCommands(core))
            .extension(
                new LiteAdventureExtension<>(),
                config -> config
                    .miniMessage(true)
                    .legacyColor(true)
                    .colorizeArgument(true)
            )
            .build();
    }

    @Override
    public void registerLoginEventListener(
        Function<UUID, CompletableFuture<Result>> check,
        Supplier<Component> getExKickMsg
    ) {
        this.server.getEventManager().register(
            this.plugin,
            LoginEvent.class,
            PostOrder.EARLY,
            (AwaitingEventExecutor<LoginEvent>) event -> EventTask.withContinuation((continuation) -> {
                if (!event.getResult().isAllowed()) {
                    continuation.resume();
                    return;
                }
                UUID id = event.getPlayer().getUniqueId();
                check.apply(id).whenComplete((result, ex) -> {
                    if (ex != null) {
                        this.logger.error(ex.getMessage(), ex);
                        event.setResult(ResultedEvent.ComponentResult.denied(getExKickMsg.get()));
                    } else if (!result.good) {
                        event.setResult(ResultedEvent.ComponentResult.denied(result.message));
                    }
                    continuation.resume();
                });
            })
        );
    }
}
