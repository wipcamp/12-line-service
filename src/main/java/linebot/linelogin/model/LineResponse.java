package linebot.linelogin.model;

public class LineResponse {

    public final String scope;
    public final String access_token;
    public final String token_type;
    public final int expires_in;
    public final String id_token;
    public final String userId;

    public LineResponse(String scope, String access_token, String token_type, int expires_in, String id_token, String userId) {
        this.scope = scope;
        this.access_token = access_token;
        this.token_type = token_type;
        this.expires_in = expires_in;
        this.id_token = id_token;
        this.userId = userId;
    }
}
