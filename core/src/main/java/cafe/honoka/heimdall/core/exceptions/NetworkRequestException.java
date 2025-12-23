package cafe.honoka.heimdall.core.exceptions;

public class NetworkRequestException extends ExpectableException {
    public NetworkRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
