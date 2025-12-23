package cafe.honoka.heimdall.core.logger;

import org.apache.logging.log4j.Logger;

public class Log4jLogger implements PlatformLogger{
    public final Logger logger;

    public Log4jLogger(Logger logger) {
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
