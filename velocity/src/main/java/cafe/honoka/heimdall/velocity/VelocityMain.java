package cafe.honoka.heimdall.velocity;

import cafe.honoka.heimdall.core.Core;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;

import org.bstats.velocity.Metrics;

@Plugin(
        id = "heimdall",
        name = "Heimdall",
        version = "1.0.0"
)
public class VelocityMain {

    private final Metrics.Factory metricsFactory;
    private Core core;
    private Injector injector;

    @Inject
    public VelocityMain(final Metrics.Factory metricsFactory, Injector injector) {
        this.metricsFactory = metricsFactory;
        this.injector = injector;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        Metrics metrics = metricsFactory.make(this, 28323);
        this.core = new Core(this.injector.getInstance(VelocityAdapter.class));
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        if (this.core != null) {
            this.core.shutdown();
        }
    }
}
