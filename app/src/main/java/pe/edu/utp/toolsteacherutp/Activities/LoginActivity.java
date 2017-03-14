package pe.edu.utp.toolsteacherutp.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;


import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

import pe.edu.utp.toolsteacherutp.BuildConfig;
import pe.edu.utp.toolsteacherutp.Interfaces.APIClient;
import pe.edu.utp.toolsteacherutp.Models.User;
import pe.edu.utp.toolsteacherutp.MyAplication;
import pe.edu.utp.toolsteacherutp.R;
import pe.edu.utp.toolsteacherutp.Models.AccessToken;
import pe.edu.utp.toolsteacherutp.Services.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    private static final String TAG = "LoginActivity";


    public static final String API_OAUTH_CLIENTID = "W3N0vZJiyKWQNb3mUSNrcDVPybWLXz";
    public static final String API_OAUTH_CLIENTSECRET = "JTCwU8vEr4pnUcyYztQnU31PSz0PlY";

    private AccessToken currentToken;
    private ProgressDialog progress = null;
    private User currentUser;


    // UI references.
    private AutoCompleteTextView mUserNameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        if ( prefs.getBoolean( "oauth.loggedin", false ) ){
            startActivity( new Intent( getApplicationContext(), MainActivity.class ) );
            finish();
        }

        mUserNameView = (AutoCompleteTextView) findViewById(R.id.username);

        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        progress = new ProgressDialog(this);
        progress.setMessage("Iniciando Sesión");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        currentToken = (( MyAplication ) getApplication() ).getCurrentAccesToken();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (progress.isShowing() ) {
            return;
        }
        Log.e( TAG,  "attemptLogin" );
        // Reset errors.
        mUserNameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String userName = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_password_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(userName)) {
            mUserNameView.setError(getString(R.string.error_user_name_required));
            focusView = mUserNameView;
            cancel = true;
        }
        /*
        else if (!isEmailValid(email)) {
            mUserNameView.setError(getString(R.string.error_invalid_email));
            focusView = mUserNameView;
            cancel = true;
        }
        */
        if (cancel) {
            focusView.requestFocus();
        } else {
            //showProgress(true);
            progress.show();
            if( currentToken.getAccessToken() != null ){
                SharedPreferences prefs = getApplicationContext().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
                prefs.edit().putBoolean("oauth.loggedin", true).apply();
                Log.e( TAG, "loadMe" );
                Log.e( TAG, currentToken.getExpiry() + "" );
                loadMe( currentToken );
            }
            else {
                signIn( userName, password );
            }
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void loadMe(final AccessToken accessToken ){
        APIClient loginService = ServiceGenerator.createService(APIClient.class, accessToken , getApplicationContext(), true );
        Call<User> callUserMe = loginService.getUserMe();
        callUserMe.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    int statusCode = response.code();
                    if( statusCode == 200) {
                        currentUser = response.body();
                        ((MyAplication) getApplication() ).setCurrentUser( currentUser );

                        String token = FirebaseInstanceId.getInstance().getToken();
                        if ( token != null ){
                            APIClient tokenDevice = ServiceGenerator.createService(APIClient.class, accessToken , getApplicationContext(), false );
                            Call<Void> callUserMe = tokenDevice.registerDeviceNotification( token, "Android", currentUser.getCorreo() );
                            callUserMe.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    //FirebaseMessaging.getInstance().subscribeToTopic("news");
                                    startActivity( new Intent( getApplicationContext(), MainActivity.class ));
                                    finish();
                                }
                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Log.e( TAG, "onFailure " + t.getMessage() );
                                }
                            } );
                        }
                    }
                    else {
                        progress.cancel();
                        mPasswordView.setError(getString(R.string.error_incorrect_password));
                        mPasswordView.requestFocus();
                    }
                } catch ( NullPointerException e){
                    Log.e( TAG, "NullPointerException " + e.getMessage() );
                    progress.cancel();
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e( TAG, "onFailure " + t.getMessage() );
                progress.cancel();
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        } );
    }


    private void signIn(  String userName, String password ){
        APIClient loginService = ServiceGenerator.createService(APIClient.class, API_OAUTH_CLIENTID, API_OAUTH_CLIENTSECRET );
        Call<AccessToken> call = loginService.getNewAccessToken( userName, password,"password" );
        call.enqueue(new Callback<AccessToken >() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                try {
                    Log.e( TAG, "onResponse " + response.body().getAccessToken() );
                    int statusCode = response.code();
                    if(statusCode == 200) {
                        currentToken = response.body();
                        SharedPreferences prefs = getApplicationContext().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
                        prefs.edit().putBoolean("oauth.loggedin", true).apply();
                        prefs.edit().putString("oauth.access_token", currentToken.getAccessToken()).apply();
                        prefs.edit().putString("oauth.refresh_token", currentToken.getRefreshToken()).apply();
                        prefs.edit().putString("oauth.token_type", currentToken.getTokenType()).apply();
                        prefs.edit().putString("oauth.scope", currentToken.getScope()).apply();
                        prefs.edit().putString("oauth.currentToken", currentToken.toString()).apply();
                        prefs.edit().putInt("oauth.expires_in", currentToken.getExpiry()).apply();

                        loadMe( currentToken );
                    }
                    else {
                        progress.cancel();
                        mPasswordView.setError(getString(R.string.error_incorrect_password));
                        mPasswordView.requestFocus();
                    }

                } catch ( NullPointerException e){
                    progress.cancel();
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                    Log.e( TAG, "NullPointerException " + e.getMessage() );
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                progress.cancel();
                Log.e( TAG, "onFailure " + t.getMessage() );
            }
        } );
    }
}

