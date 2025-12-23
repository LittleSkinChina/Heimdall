package cafe.honoka.heimdall.core.exceptions;

import java.io.IOException;

public class ExpectableException extends IOException {
    public ExpectableException(String message) {
        super(message);
    }

    public ExpectableException(String message, Throwable cause) {
        super(message, cause);
    }
}
