package pe.edu.utp.toolsteacherutp.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
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

import pe.edu.utp.toolsteacherutp.Adapters.CourseAdapter;
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

public class CoursesFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private User currentUser;
    private AccessToken currentToken;
    private SwipeRefreshLayout swipeContainer;
    private List<Seccion> sectionsList;

    private RecyclerView coursesRecyclerView;
    private CourseAdapter coursesAdapter;
    private LinearLayoutManager coursesLayoutManager;
    private static final String TAG = "CoursesFragment";

    public CoursesFragment() {
        // Required empty public constructor
    }

    public static CoursesFragment newInstance(String param1, String param2) {
        CoursesFragment fragment = new CoursesFragment();
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

        View view = inflater.inflate(R.layout.fragment_courses, container, false);
        coursesRecyclerView = (RecyclerView) view.findViewById(R.id.coursesRecyclerView);
        currentToken = ((MyAplication) getActivity().getApplication() ).getCurrentAccesToken();
        currentUser = (( MyAplication ) getActivity().getApplication() ).getCurrentUser();
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reLoadSecciones( currentToken, true );
            }
        });

        coursesRecyclerView.setHasFixedSize(true);
        coursesLayoutManager = new LinearLayoutManager( getActivity().getApplicationContext() ) ;
        coursesRecyclerView.setLayoutManager( coursesLayoutManager );

        loadSecciones( currentToken, false );
        return view;
    }

    private void reLoadSecciones(final AccessToken accessToken, final boolean refresh) {
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
                        loadSecciones( accessToken, refresh );
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

    private void loadSecciones(AccessToken currentToken, boolean refresh) {
        if ( currentUser.getSeccionesList().size() > 0 )  {
            sectionsList = new ArrayList<>();
            setupSecciones( currentUser, refresh );
        }
        swipeContainer.setRefreshing(false);
    }

    private void setupSecciones(User currentUser, boolean refresh) {
        sectionsList.clear();
        sectionsList = currentUser.getSeccionesList();
        if ( refresh ){
            coursesAdapter.updateData( sectionsList );
        }
        else {
            coursesAdapter = new CourseAdapter(sectionsList, getActivity().getApplicationContext());
            coursesRecyclerView.setAdapter(coursesAdapter);
        }
    }

}
