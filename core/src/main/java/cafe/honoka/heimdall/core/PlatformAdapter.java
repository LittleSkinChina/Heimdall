package cafe.honoka.heimdall.core;

import cafe.honoka.heimdall.core.logger.PlatformLogger;
import net.kyori.adventure.text.Component;

import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class PlatformAdapter {

    protected final String serverImpl;
    protected final String serverVer;
    protected final PlatformLogger logger;
    protected final Path dataDirectory;

    public PlatformAdapter(
        final String serverImpl,
        final String serverVersion,
        final PlatformLogger logger,
        final Path dataDirectory
    ) {
        this.serverImpl = serverImpl;
        this.serverVer = serverVersion;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    public PlatformLogger getLogger() {
        return this.logger;
    }

    public String getServerImpl() {
        return this.serverImpl;
    }

    public String getServerVer() {
        return this.serverVer;
    }

    public Path getDataDirectory() {
        return this.dataDirectory;
    }

    public void onServerStop() { }

    abstract public boolean checkOnlineMode();

    abstract public void stopServer();

    abstract public void registerCommands(Core core);

    abstract public void registerLoginEventListener(Function<UUID, CompletableFuture<Result>> check, Supplier<Component> getExKickMsg);
}
