package pe.edu.utp.toolsteacherutp.Activities;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pe.edu.utp.toolsteacherutp.Adapters.DateHorariosAdapter;
import pe.edu.utp.toolsteacherutp.BuildConfig;
import pe.edu.utp.toolsteacherutp.Interfaces.APIClient;
import pe.edu.utp.toolsteacherutp.Models.DateHorarios;
import pe.edu.utp.toolsteacherutp.Models.Horario;
import pe.edu.utp.toolsteacherutp.Models.Media;
import pe.edu.utp.toolsteacherutp.Models.Seccion;
import pe.edu.utp.toolsteacherutp.MyAplication;
import pe.edu.utp.toolsteacherutp.R;
import pe.edu.utp.toolsteacherutp.Models.AccessToken;
import pe.edu.utp.toolsteacherutp.Services.ServiceGenerator;
import pe.edu.utp.toolsteacherutp.Models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity  {
    private static final String TAG = "MainActivity";

    static final int REQUEST_TAKE_PHOTO = 1;
    private SwipeRefreshLayout swipeContainer;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");
    private String mCurrentPhotoPath;

    private AccessToken currentToken;
    private User currentUser;

    private ProgressDialog progress;
    private List<DateHorarios> dateHorarios;
    private Date now;
    private RecyclerView dateHorariosRecyclerView;
    private RecyclerView.LayoutManager mDateHorariosLayoutManager;
    private DateHorariosAdapter dateHorariosAdapter ;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "pe.edu.utp.toolsteacherutp.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        currentToken = (( MyAplication ) getApplication() ).getCurrentAccesToken();
        currentUser = (( MyAplication ) getApplication() ).getCurrentUser();
        if ( currentToken .getAccessToken() == null ){
            startActivity( new Intent( getApplicationContext(), LoginActivity.class ) );
            finish();
        }

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        now = new Date();
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadSecciones( currentToken, now, true );
                //swipeContainer.setRefreshing(false);
            }
        });

        dateHorariosRecyclerView = (RecyclerView) findViewById(R.id.dateHorariosRecyclerView);
        dateHorariosRecyclerView.setHasFixedSize(true);
        mDateHorariosLayoutManager = new LinearLayoutManager( getApplicationContext() ) ;
        dateHorariosRecyclerView.setLayoutManager( mDateHorariosLayoutManager );

        if (  currentUser == null  )
            loadMe( currentToken, now, false );
        else
            loadSecciones( currentToken, now, false );

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        progress = new ProgressDialog(this);
        progress.setMessage("Subiendo archivo");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        //CourseController courseController = new CourseController( );
        //courseController.start();
    }

    public void setupHorarios ( Date now, User currentUser, boolean refresh ){
        dateHorarios.clear();
        for (Seccion seccion : currentUser.getSecciones()) {
            try {
                DateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                Date dateInicio = format.parse(seccion.getInicio());
                Date dateFin = format.parse(seccion.getFin());
                if (!(dateInicio.compareTo(now) * now.compareTo(dateFin) >= 0)) {
                    continue;
                }
                if (seccion.getHorarios().size() > 0) {
                    for (Horario horario : seccion.getHorarios()) {
                        Calendar c = Calendar.getInstance();
                        c.setFirstDayOfWeek(Calendar.MONDAY);
                        c.setTime(now);
                        c.set(Calendar.HOUR, 0);
                        c.set(Calendar.MINUTE, 0);
                        c.set(Calendar.SECOND, 0);
                        if (horario.isLunes()) {
                            c.setFirstDayOfWeek(Calendar.MONDAY);
                            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                            addDateHorarios(c, seccion, horario);
                        }
                        if (horario.isMartes()) {
                            c.setFirstDayOfWeek(Calendar.MONDAY);
                            c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                            addDateHorarios(c, seccion, horario);
                        }
                        if (horario.isMiercoles()) {
                            c.setFirstDayOfWeek(Calendar.MONDAY);
                            c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                            addDateHorarios(c, seccion, horario);
                        }
                        if (horario.isJueves()) {
                            c.setFirstDayOfWeek(Calendar.MONDAY);
                            c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                            addDateHorarios(c, seccion, horario);
                        }
                        if (horario.isViernes()) {
                            c.setFirstDayOfWeek(Calendar.MONDAY);
                            c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                            addDateHorarios(c, seccion, horario);
                        }
                        if (horario.isSabado()) {
                            c.setFirstDayOfWeek(Calendar.MONDAY);
                            c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                            addDateHorarios(c, seccion, horario);
                        }
                        if (horario.isDomingo()) {
                            c.setFirstDayOfWeek(Calendar.MONDAY);
                            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                            addDateHorarios(c, seccion, horario);
                        }
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
                swipeContainer.setRefreshing(false);
            }
        }
        Collections.sort(dateHorarios);
        if ( refresh ){
            dateHorariosAdapter.updateData( dateHorarios );
        }
        else {
            dateHorariosAdapter = new DateHorariosAdapter(dateHorarios, getApplicationContext());
            dateHorariosRecyclerView.setAdapter(dateHorariosAdapter);
        }
    }

    public void loadSecciones(final AccessToken accessToken, final Date now, final boolean refresh ){
        if ( currentUser.getSecciones().size() > 0 )  {
            dateHorarios = new ArrayList<>();
            setupHorarios( now, currentUser, refresh );
        }
        swipeContainer.setRefreshing(false);
    }
    public void loadMe(final AccessToken accessToken, final Date now, final boolean refresh ){
        APIClient loginService = ServiceGenerator.createService(APIClient.class, accessToken , getApplicationContext(), true );
        Call<User> callUserMe = loginService.getUserMe();
        callUserMe.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    currentUser = response.body();
                    APIClient tokenDevice = ServiceGenerator.createService(APIClient.class, accessToken , getApplicationContext(), false );
                    String token = FirebaseInstanceId.getInstance().getToken();
                    if ( token != null ){
                        Call<Void> callUserMe = tokenDevice.registerDeviceNotification( token, "Android", currentUser.getCorreo() );
                        callUserMe.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                //FirebaseMessaging.getInstance().subscribeToTopic("news");
                            }
                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e( "token onFailure", t.getMessage() );
                                swipeContainer.setRefreshing(false);
                            }
                        } );

                    }
                    if ( currentUser.getSecciones().size() > 0 )  {
                        dateHorarios = new ArrayList<>();
                        setupHorarios( now, currentUser, refresh );
                    }
                    swipeContainer.setRefreshing(false);
                } catch ( NullPointerException e){
                    Log.e( "NullPointerException", e.getMessage() );
                    swipeContainer.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e( "onFailure", t.getMessage() );
                swipeContainer.setRefreshing(false);
            }
        } );
    }

    public void addDateHorarios ( Calendar c, Seccion seccion, Horario horario){
        /*
        Date now = new Date();
        Calendar c2 = Calendar.getInstance();
        c.setFirstDayOfWeek( Calendar.MONDAY );
        c.setTime(now);
        c.set( Calendar.HOUR, 0 );
        c.set( Calendar.MINUTE, 0 );
        c.set( Calendar.SECOND, 0 );
        */
        int indexDatesHorarios;
        indexDatesHorarios = getIndexDateHorarios( dateHorarios, c.getTime() );
        DateHorarios nDate = new DateHorarios( );
        nDate.setName( seccion.getPost_title() );
        nDate.setDate( c.getTime()  );
        if ( indexDatesHorarios == -1 ){
            List<Horario> horarios = new ArrayList<>();
            horarios.add( horario );
            nDate.setHorarios( horarios );
            dateHorarios.add( nDate );
        }
        else {
            List<Horario> horarios = dateHorarios.get( indexDatesHorarios ).getHorarios();
            horarios.add( horario );
            dateHorarios.get( indexDatesHorarios ).setHorarios( horarios );
        }
    }

    public int getIndexDateHorarios ( List<DateHorarios> dateHorarios, Date date ){
        int index = -1;
        for ( DateHorarios dateHorario : dateHorarios ) {
            if( dateHorario.getDate().toString().equals( date.toString() ) ){
                index = dateHorarios.indexOf( dateHorario );
                break;
            }
        }
        return  index;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            File file = new File(mCurrentPhotoPath);
            APIClient uploadService = ServiceGenerator.createService(APIClient.class,  currentToken, file.getAbsolutePath(), getApplicationContext() );
            RequestBody requestFile =
                    RequestBody.create(
                            MEDIA_TYPE_PNG,
                            file
                    );
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            String descriptionString = "hello, this is description speaking";
            RequestBody description =
                    RequestBody.create(
                            okhttp3.MultipartBody.FORM, descriptionString);

            Call<Media> uploadMedia = uploadService .upload( description, body);
            progress.show();
            uploadMedia.enqueue(new Callback<Media>() {
                @Override
                public void onResponse(Call<Media> call, Response<Media> response) {
                    try {
                        Log.e( "onResponse", response.body().getSource_url() );
                        Log.e( "onResponse", response.code() + "" );
                        progress.hide();
                    } catch ( NullPointerException e){
                        Log.e( "onResponse", e.getMessage() );
                        progress.hide();
                    }
                }

                @Override
                public void onFailure(Call<Media> call, Throwable t) {
                    Log.e( "onFailure", t.getMessage() );
                    progress.hide();
                }
            } );
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
