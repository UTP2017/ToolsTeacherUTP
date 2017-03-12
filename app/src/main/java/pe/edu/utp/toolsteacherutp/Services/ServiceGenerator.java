package pe.edu.utp.toolsteacherutp.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import pe.edu.utp.toolsteacherutp.BuildConfig;
import pe.edu.utp.toolsteacherutp.Rest.AccessToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * Created by elbuenpixel on 10/03/17.
 */

public class ServiceGenerator {
    public static final String API_BASE_URL = "http://162.243.185.203/wp-json/wp/v2/";
    public static final String API_OAUTH_BASE_URL = "http://162.243.185.203";
    public static final String API_OAUTH_REDIRECT = BuildConfig.APPLICATION_ID + "://oauth";

    private static OkHttpClient.Builder httpClient;

    private static Retrofit.Builder builder;

    private static Context mContext;
    private static AccessToken mToken;

    public static <S> S createService(Class<S> serviceClass, final String username, final String password) {
        httpClient = new OkHttpClient.Builder();
        builder = new Retrofit.Builder()
                .baseUrl(API_OAUTH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Content-type", "application/x-www-form-urlencoded")
                            .header("Authorization", Credentials.basic(username, password) )
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createService(Class<S> serviceClass) {
        httpClient = new OkHttpClient.Builder();
        builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createService(Class<S> serviceClass, AccessToken accessToken, final String path, Context c) {
        httpClient = new OkHttpClient.Builder();
        builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        if(accessToken != null) {
            mContext = c;
            mToken = accessToken;
            final AccessToken token = accessToken;
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Content-Disposition", "attachment; filename=\"" + path + "\"")
                            .header("Authorization",
                                    token.getTokenType() + " " + token.getAccessToken())
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createService(Class<S> serviceClass, AccessToken accessToken, Context c, final boolean json) {
        httpClient = new OkHttpClient.Builder();
        builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        if(accessToken != null) {
            mContext = c;
            mToken = accessToken;
            final AccessToken token = accessToken;
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder requestBuilder;
                    if ( json ){
                        requestBuilder = original.newBuilder()
                                .header("Accept", "application/json")
                                //.header("Content-type", "application/json")
                                //.header("Content-type", "application/x-www-form-urlencoded")
                                .header("Authorization",
                                        token.getTokenType() + " " + token.getAccessToken())
                                .method(original.method(), original.body());
                    }
                    else {
                        requestBuilder = original.newBuilder()
                                //.header("Accept", "application/json")
                                //.header("Content-type", "application/json")
                                //.header("Content-type", "application/x-www-form-urlencoded")
                                .header("Authorization",
                                        token.getTokenType() + " " + token.getAccessToken())
                                .method(original.method(), original.body());
                    }

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
            /*
            httpClient.authenticator(new Authenticator() {
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    if(responseCount(response) >= 2) {
                        // If both the original call and the call with refreshed token failed,
                        // it will probably keep failing, so don't try again.
                        return null;
                    }

                    // We need a new client, since we don't want to make another call
                    // using our client with access token

                    APIClient tokenClient = createService(APIClient.class);
                    Call<AccessToken> call = tokenClient.getRefreshAccessToken(mToken.getRefreshToken(),
                            mToken.getClientID(), mToken.getClientSecret(), API_OAUTH_REDIRECT,
                            "refresh_token");
                    try {
                        retrofit2.Response<AccessToken> tokenResponse = call.execute();
                        if(tokenResponse.code() == 200) {
                            AccessToken newToken = tokenResponse.body();
                            mToken = newToken;
                            SharedPreferences prefs = mContext.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
                            prefs.edit().putBoolean("oauth.loggedin", true).apply();
                            prefs.edit().putString("oauth.accesstoken", newToken.getAccessToken()).apply();
                            prefs.edit().putString("oauth.refreshtoken", newToken.getRefreshToken()).apply();
                            prefs.edit().putString("oauth.tokentype", newToken.getTokenType()).apply();

                            return response.request().newBuilder()
                                    .header("Authorization", newToken.getTokenType() + " " + newToken.getAccessToken())
                                    .build();
                        } else {
                            return null;
                        }
                    } catch(IOException e) {
                        return null;
                    }
                }
            });
            */
        }

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

    private static int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }

}
