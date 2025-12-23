package cafe.honoka.heimdall.core.query;

public class QueryResponse {
    public final String uuid;
    public final Status status;
    public final Boolean outlook;

    public QueryResponse(String uuid, Status status, Boolean outlook) {
        this.uuid = uuid;
        this.status = status;
        this.outlook = outlook;
    }
}
