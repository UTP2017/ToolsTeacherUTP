package pe.edu.utp.toolsteacherutp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pe.edu.utp.toolsteacherutp.Models.AccessToken;
import pe.edu.utp.toolsteacherutp.Models.User;

/**
 * Created by elbuenpixel on 14/03/17.
 */

public class MyAplication extends Application {
    private AccessToken accessToken;
    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
    public AccessToken getCurrentAccesToken (){
        if ( accessToken == null ){
            Gson gson = new GsonBuilder().create();
            SharedPreferences prefs = getApplicationContext().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
            AccessToken access_token = gson.fromJson(prefs.getString( "oauth.currentToken", "{}") , AccessToken.class);
            setAccessToken( access_token );
        }
        return getAccessToken();
    }
}
