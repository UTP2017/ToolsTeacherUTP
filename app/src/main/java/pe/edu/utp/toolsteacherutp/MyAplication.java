package pe.edu.utp.toolsteacherutp;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orm.SugarApp;

import pe.edu.utp.toolsteacherutp.Models.AccessToken;
import pe.edu.utp.toolsteacherutp.Models.Horario;
import pe.edu.utp.toolsteacherutp.Models.Seccion;
import pe.edu.utp.toolsteacherutp.Models.User;

/**
 * Created by elbuenpixel on 14/03/17.
 */

public class MyAplication extends SugarApp {
    private AccessToken accessToken;
    private User currentUser;

    public User getCurrentUser() {
        if ( currentUser == null ){
            SharedPreferences prefs = getApplicationContext().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
            Long userID = prefs.getLong( "user.id", 0 );
            if ( userID != 0  ){
                currentUser = User.findById( User.class, userID );
                Log.e( "MyAplication", currentUser.toString() );
            }
        }
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
            String _access_token;
            SharedPreferences prefs = getApplicationContext().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
            _access_token = prefs.getString( "oauth.access_token", "" );
            if ( !_access_token.equals("") ){
                AccessToken accessToken = new AccessToken();
                AccessToken currentToken;
                currentToken = accessToken.findByAccessToken( _access_token );
                Long tsLong = System.currentTimeMillis()/1000;
                if(currentToken.getExpires_in() >= tsLong){
                    setAccessToken( currentToken );
                }
                else {
                    User.deleteAll(User.class);
                    AccessToken.deleteAll(AccessToken.class);
                    Seccion.deleteAll(Seccion.class);
                    Horario.deleteAll(Horario.class);

                    prefs.edit().putBoolean("oauth.loggedin", false).apply();
                    prefs.edit().putString("oauth.access_token", null).apply();
                    prefs.edit().putString("oauth.refresh_token", "").apply();
                    prefs.edit().putString("oauth.token_type", "").apply();
                    prefs.edit().putString("oauth.scope", "").apply();
                    prefs.edit().putLong("oauth.expires_in", 0 ).apply();

                    prefs.edit().putLong("user.id", 0).apply();
                    prefs.edit().putString("user.name", "").apply();
                    prefs.edit().putString("user.rol", "").apply();
                    prefs.edit().putString("user.correo", "").apply();
                    prefs.edit().putString("user.avatar", "").apply();
                    this.accessToken = null;
                }
            }
        }
        return getAccessToken();
    }

    public void signOut() {
        String _access_token;
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        _access_token = prefs.getString( "oauth.access_token", "" );
        if ( !_access_token.equals("") ){
            User.deleteAll(User.class);
            AccessToken.deleteAll(AccessToken.class);
            Seccion.deleteAll(Seccion.class);
            Horario.deleteAll(Horario.class);

            prefs.edit().putBoolean("oauth.loggedin", false).apply();
            prefs.edit().putString("oauth.access_token", null).apply();
            prefs.edit().putString("oauth.refresh_token", "").apply();
            prefs.edit().putString("oauth.token_type", "").apply();
            prefs.edit().putString("oauth.scope", "").apply();
            prefs.edit().putLong("oauth.expires_in", 0 ).apply();

            prefs.edit().putLong("user.id", 0).apply();
            prefs.edit().putString("user.name", "").apply();
            prefs.edit().putString("user.rol", "").apply();
            prefs.edit().putString("user.correo", "").apply();
            prefs.edit().putString("user.avatar", "").apply();
            this.accessToken = null;
        }
    }

}
