package cafe.honoka.heimdall.core.logger;

import java.util.List;
import java.util.function.Consumer;

public interface PlatformLogger {
    void info(String message);
    void warning(String message);
    void error(String message);
    void error(String message, Throwable throwable);
    void debug(String message);

    default void multiline(List<String> messages, Consumer<String> action) {
        messages.forEach(action);
    }

    default void info(List<String> messages) {
        this.multiline(messages, this::info);
    }

    default void warning(List<String> messages) {
        this.multiline(messages, this::warning);
    }

    default void error(List<String> messages) {
        this.multiline(messages, this::error);
    }

    default void debug(List<String> messages) {
        this.multiline(messages, this::debug);
    }
}