package cafe.honoka.heimdall.core.exceptions;

public class UnknownResponseException extends ExpectableException {
    public UnknownResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
