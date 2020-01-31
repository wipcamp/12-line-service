package linebot.linelogin.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
//import io.jsonwebtoken.*;
import linebot.linelogin.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Call;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 * <p>LINE v2 API Access</p>
 */
@Component
public class LineAPIService {

    private static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";
    private static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";

    @Value("${linecorp.platform.channel.channelId}")
    private String channelId;
    @Value("${linecorp.platform.channel.channelSecret}")
    private String channelSecret;
    @Value("${linecorp.platform.channel.callbackUrl}")
    private String callbackUrl;

    @Value("${line.secret}")
    private String SECRET_KEY;

    private String gameChannelId = "1653703435";
    private String gameChannelSecret = "1de29b41b4411ff9832c4bba82fe3234";
    private String gameCallbackUrl = "";

    public AccessToken accessToken(String code) {
        return getClient(t -> t.accessToken(
                GRANT_TYPE_AUTHORIZATION_CODE,
                gameChannelId,
                gameChannelSecret,
                "https://12-itim.freezer.wip.camp/login",
                code));
    }

    public AccessToken gameAccessToken(String code) {
        return getClient(t -> t.accessToken(
                GRANT_TYPE_AUTHORIZATION_CODE,
                gameChannelId,
                gameChannelSecret,
                "https://12-gamecamp.freezer.wip.camp/login",
                code));
    }

    public AccessToken refreshToken(final AccessToken accessToken) {
        return getClient(t -> t.refreshToken(
                GRANT_TYPE_REFRESH_TOKEN,
                accessToken.refresh_token,
                channelId,
                channelSecret));
    }

    public Verify verify(final AccessToken accessToken) {
        return getClient(t -> t.verify(
                accessToken.access_token));
    }

    public void revoke(final AccessToken accessToken) {
        getClient(t -> t.revoke(
                accessToken.access_token,
                channelId,
                channelSecret));
    }

    public IdToken idToken(String id_token) {
        try {
            DecodedJWT jwt = JWT.decode(id_token);
            return new IdToken(
                    jwt.getClaim("iss").asString(),
                    jwt.getClaim("sub").asString(),
                    jwt.getClaim("aud").asString(),
                    jwt.getClaim("ext").asLong(),
                    jwt.getClaim("iat").asLong(),
                    jwt.getClaim("nonce").asString(),
                    jwt.getClaim("name").asString(),
                    jwt.getClaim("picture").asString(),
                    jwt.getClaim("email").asString());
        } catch (JWTDecodeException e) {
            throw new RuntimeException(e);
        }
    }

    public UserProfile getProfile(String access_token) {
        return getClient(t -> t.getProfile());
    }

    public String getLineLoginUrl(String state, String nonce, List<String> scopes) {
        final String encodedCallbackUrl;
        final String scope = String.join("%20", scopes);

        try {
            encodedCallbackUrl = URLEncoder.encode(callbackUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return "https://access.line.me/oauth2/v2.1/authorize?response_type=code"
                + "&client_id=" + channelId
                + "&redirect_uri=" + encodedCallbackUrl
                + "&state=" + state
                + "&scope=" + scope
                + "&nonce=" + nonce;
    }

    public String gameLineLoginUrl(String state, String nonce, List<String> scopes) {
        final String encodedCallbackUrl;
        final String scope = String.join("%20", scopes);

        try {
            encodedCallbackUrl = URLEncoder.encode(callbackUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return "https://access.line.me/oauth2/v2.1/authorize?response_type=code"
                + "&client_id=1653724802"
                + "&redirect_uri=http://line.service.freezer.wip.camp/authForGame"
                + "&state=" + state
                + "&scope=" + scope
                + "&nonce=" + nonce;
    }

    public boolean verifyIdToken(String id_token, String nonce) {
        try {
            JWT.require(
                    Algorithm.HMAC256(channelSecret))
                    .withIssuer("https://access.line.me")
                    .withAudience(channelId)
                    .withClaim("nonce", nonce)
                    .acceptLeeway(60) // add 60 seconds leeway to handle clock skew between client and server sides.
                    .build()
                    .verify(id_token);
            return true;
        } catch (UnsupportedEncodingException e) {
            //UTF-8 encoding not supported
            return false;
        } catch (JWTVerificationException e) {
            //Invalid signature/claims
            return false;
        }
    }

    public String createToken(LineResponse lineResponse) {
        System.out.println(SECRET_KEY);
        String token = Jwts.builder()
                .claim("scope", lineResponse.scope)
                .claim("access_token", lineResponse.access_token)
                .claim("token_type", lineResponse.token_type)
                .claim("expires_in", lineResponse.expires_in)
                .claim("id_token", lineResponse.id_token)
                .claim("userId", lineResponse.userId)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).setHeaderParam("token_type", lineResponse.token_type)
                .compact();
        return token;
    }

    public LineResponse decodeToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return new LineResponse(
                    jwt.getClaim("scope").asString(),
                    jwt.getClaim("access_token").asString(),
                    jwt.getClaim("token_type").asString(),
                    jwt.getClaim("expires_in").asInt(),
                    jwt.getClaim("id_token").asString(),
                    jwt.getClaim("userId").asString());
        } catch (JWTDecodeException e) {
            throw new RuntimeException(e);
        }
    }

    private <R> R getClient(final Function<LineAPI, Call<R>> function) {
        return Client.getClient("https://api.line.me/", LineAPI.class, function);
    }

    public String getGenerateCode() {
        byte[] array = new byte[32]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = Base64.getUrlEncoder().withoutPadding().encodeToString(array);
        System.out.println(generatedString);
        return generatedString;
    }
}
