package cafe.honoka.heimdall.core.logger;

import java.util.logging.Logger;

public class JulLogger implements PlatformLogger {
    private final java.util.logging.Logger logger;

    public JulLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String message) {
        this.logger.info(message);
    }

    @Override
    public void warning(String message) {
        this.logger.warning(message);
    }

    @Override
    public void error(String message) {
        this.logger.severe(message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        this.logger.log(java.util.logging.Level.SEVERE, message, throwable);
    }

    @Override
    public void debug(String message) {
        this.logger.fine(message);
    }
}
