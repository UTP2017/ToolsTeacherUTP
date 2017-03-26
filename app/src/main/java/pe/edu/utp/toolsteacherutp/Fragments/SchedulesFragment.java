package pe.edu.utp.toolsteacherutp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.iid.FirebaseInstanceId;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pe.edu.utp.toolsteacherutp.Activities.MainActivity;
import pe.edu.utp.toolsteacherutp.Adapters.DateHorariosAdapter;
import pe.edu.utp.toolsteacherutp.BuildConfig;
import pe.edu.utp.toolsteacherutp.Interfaces.APIClient;
import pe.edu.utp.toolsteacherutp.Models.AccessToken;
import pe.edu.utp.toolsteacherutp.Models.DateHorarios;
import pe.edu.utp.toolsteacherutp.Models.Horario;
import pe.edu.utp.toolsteacherutp.Models.Seccion;
import pe.edu.utp.toolsteacherutp.Models.User;
import pe.edu.utp.toolsteacherutp.MyAplication;
import pe.edu.utp.toolsteacherutp.R;
import pe.edu.utp.toolsteacherutp.Services.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SchedulesFragment extends Fragment {
    private static final String TAG = "SchedulesFragment";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Date now;
    private RecyclerView dateHorariosRecyclerView;
    private LinearLayoutManager mDateHorariosLayoutManager;
    private AccessToken currentToken;
    private User currentUser;
    private List<DateHorarios> dateHorarios;
    private DateHorariosAdapter dateHorariosAdapter;
    private SwipeRefreshLayout swipeContainer;

    public SchedulesFragment() {
        // Required empty public constructor
    }

    public static SchedulesFragment newInstance(String param1, String param2) {
        SchedulesFragment fragment = new SchedulesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedules, container, false);
        dateHorariosRecyclerView = (RecyclerView) view.findViewById(R.id.dateHorariosRecyclerView);
        currentToken = ((MyAplication) getActivity().getApplication() ).getCurrentAccesToken();
        currentUser = (( MyAplication ) getActivity().getApplication() ).getCurrentUser();
        now = new Date();
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reLoadSecciones( currentToken, now, true );
                //swipeContainer.setRefreshing(false);
            }
        });

        dateHorariosRecyclerView.setHasFixedSize(true);
        mDateHorariosLayoutManager = new LinearLayoutManager( getActivity().getApplicationContext() ) ;
        dateHorariosRecyclerView.setLayoutManager( mDateHorariosLayoutManager );

        if (  currentUser == null  )
            loadMe( currentToken, now, false );
        else
            loadSecciones( currentToken, now, false );
        return view;
    }

    public void setupHorarios ( Date now, User currentUser, boolean refresh ){
        dateHorarios.clear();
        for (Seccion seccion : currentUser.getSeccionesList()) {
            try {
                DateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                Date dateInicio = format.parse(seccion.getInicio());
                Date dateFin = format.parse(seccion.getFin());
                if (!(dateInicio.compareTo(now) * now.compareTo(dateFin) >= 0)) {
                    continue;
                }
                if (seccion.getHorariosList().size() > 0) {
                    for (Horario horario : seccion.getHorariosList()) {
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
            dateHorariosAdapter = new DateHorariosAdapter(dateHorarios, getActivity().getApplicationContext());
            dateHorariosRecyclerView.setAdapter(dateHorariosAdapter);
        }
    }

    public void reLoadSecciones(final AccessToken accessToken, final Date now, final boolean refresh ){
        APIClient loginService = ServiceGenerator.createService(APIClient.class, accessToken , getActivity().getApplicationContext(), true );
        Call<User> callUserMe = loginService.getUserMe();
        callUserMe.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    int statusCode = response.code();
                    Log.e( TAG, "loadMe " + statusCode );
                    if( statusCode == 200) {
                        currentUser = response.body();
                        ((MyAplication) getActivity().getApplication() ).setCurrentUser( currentUser );
                        String token = FirebaseInstanceId.getInstance().getToken();
                        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
                        prefs.edit().putLong("user.id", currentUser.getId()).apply();
                        prefs.edit().putString("user.name", currentUser.getName()).apply();
                        prefs.edit().putString("user.rol", currentUser.getRol()).apply();
                        prefs.edit().putString("user.correo", currentUser.getCorreo()).apply();
                        prefs.edit().putString("user.avatar", currentUser.getAvatar()).apply();
                        Seccion.deleteAll(Seccion.class);
                        Horario.deleteAll(Horario.class);
                        currentUser.save();
                        loadSecciones( accessToken, now, refresh );
                    }
                    else {
                        swipeContainer.setRefreshing(false);
                    }
                } catch ( NullPointerException e){
                    Log.e( TAG, "NullPointerException " + e.getMessage() );
                    swipeContainer.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e( TAG, "onFailure " + t.getMessage() );
                swipeContainer.setRefreshing(false);
            }
        } );
    }
    public void loadSecciones(final AccessToken accessToken, final Date now, final boolean refresh ){
        if ( currentUser.getSeccionesList().size() > 0 )  {
            dateHorarios = new ArrayList<>();
            setupHorarios( now, currentUser, refresh );
        }
        swipeContainer.setRefreshing(false);
    }
    public void loadMe(final AccessToken accessToken, final Date now, final boolean refresh ){
        APIClient loginService = ServiceGenerator.createService(APIClient.class, accessToken , getActivity().getApplicationContext(), true );
        Call<User> callUserMe = loginService.getUserMe();
        callUserMe.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    currentUser = response.body();
                    APIClient tokenDevice = ServiceGenerator.createService(APIClient.class, accessToken , getActivity().getApplicationContext(), false );
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
                    if ( currentUser.getSeccionesList().size() > 0 )  {
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

}
