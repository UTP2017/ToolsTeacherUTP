package pe.edu.utp.toolsteacherutp.Interfaces;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pe.edu.utp.toolsteacherutp.Models.Media;
import pe.edu.utp.toolsteacherutp.Models.AccessToken;
import pe.edu.utp.toolsteacherutp.Models.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by elbuenpixel on 10/03/17.
 */

public interface APIClient {

    @FormUrlEncoded
    @POST("/oauth/token")
    Call<AccessToken> getNewAccessToken(
            @Field("username") String username,
            @Field("password") String password,
            @Field("grant_type") String grant_type
    );


    @FormUrlEncoded
    @POST("/pnfw/register")
    Call<Void> registerDeviceNotification(
            @Field("token") String token,
            @Field("os") String os,
            @Field("email") String email
    );

    @GET("media")
    Call<Media> uploadMedia();

    @Multipart
    @POST("media")
    Call<Media> upload(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file
    );


    @GET("users/me")
    Call<User> getUserMe();

    @FormUrlEncoded
    @POST("/oauth/token")
    Call<AccessToken> getNewAccessToken(
            @Field("code") String code,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("redirect_uri") String redirectUri,
            @Field("grant_type") String grantType);
    @FormUrlEncoded
    @POST("/oauth/token")
    Call<AccessToken> getRefreshAccessToken(
            @Field("refresh_token") String refreshToken,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("redirect_uri") String redirectUri,
            @Field("grant_type") String grantType);
}
