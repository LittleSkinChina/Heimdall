package cafe.honoka.heimdall.core.query;

public abstract class BaseQueryResponse {
    public final Status status;
    public final Boolean outlook;

    public BaseQueryResponse(Status status, Boolean outlook) {
        this.status = status;
        this.outlook = outlook;
    }
}
