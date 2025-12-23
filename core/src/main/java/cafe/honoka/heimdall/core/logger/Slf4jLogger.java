package cafe.honoka.heimdall.core.logger;

import org.slf4j.Logger;

public class Slf4jLogger implements PlatformLogger{
    public final Logger logger;

    public Slf4jLogger(org.slf4j.Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String message) {
        this.logger.info(message);
    }

    @Override
    public void warning(String message) {
        this.logger.warn(message);
    }

    @Override
    public void error(String message) {
        this.logger.error(message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        this.logger.error(message, throwable);
    }

    @Override
    public void debug(String message) {
        this.logger.debug(message);
    }
}
