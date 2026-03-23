package cafe.honoka.heimdall.core.query;

public class XuidQueryResponse extends BaseQueryResponse {
    public final String xuid;

    public XuidQueryResponse(String xuid, Status status, Boolean outlook) {
        super(status, outlook);
        this.xuid = xuid;
    }
}
