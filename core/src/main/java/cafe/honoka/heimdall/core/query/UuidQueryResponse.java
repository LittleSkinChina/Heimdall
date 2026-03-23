package cafe.honoka.heimdall.core.query;

public class UuidQueryResponse extends BaseQueryResponse {
    public final String uuid;

    public UuidQueryResponse(String uuid, Status status, Boolean outlook) {
        super(status, outlook);
        this.uuid = uuid;
    }
}
