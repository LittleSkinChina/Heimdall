package cafe.honoka.heimdall.core.exceptions;

public class RateLimitExceededException extends ExpectableException {
    public RateLimitExceededException(String message) {
        super(message);
    }
}
