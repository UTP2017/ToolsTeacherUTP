package pe.edu.utp.toolsteacherutp.Models;

import com.orm.SugarRecord;

import org.json.JSONObject;

/**
 * Created by elbuenpixel on 10/03/17.
 */

public class AccessToken  extends SugarRecord {
    private String access_token;
    private String token_type;
    private Long expires_in;
    private String refresh_token;
    private String scope;

    public AccessToken() {
    }

    public AccessToken(String access_token, String token_type, Long expires_in, String refresh_token, String scope) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.expires_in = expires_in;
        this.refresh_token = refresh_token;
        this.scope = scope;
    }

    public AccessToken findByAccessToken( String access_token ){
        return AccessToken.find( AccessToken.class, "accessToken = ?", access_token ).get(0);
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

}
