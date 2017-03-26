package pe.edu.utp.toolsteacherutp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import pe.edu.utp.toolsteacherutp.Models.User;
import pe.edu.utp.toolsteacherutp.MyAplication;
import pe.edu.utp.toolsteacherutp.R;
import pe.edu.utp.toolsteacherutp.Transforms.CircleTransform;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ImageView pictureImageView;
    private TextView totalSectionsView;
    private TextView nameTextView;
    private TextView emailTextView;
    private User currentUser;


    public ProfileFragment() {
        // Required empty public constructor
    }
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        currentUser = ((MyAplication) getActivity().getApplication() ).getCurrentUser();

        pictureImageView = (ImageView) view.findViewById(R.id.pictureImageView);
        nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        emailTextView = (TextView) view.findViewById(R.id.emailTextView);
        totalSectionsView = (TextView) view.findViewById(R.id.totalSectionsView);

        nameTextView.setText( currentUser.getName() );
        emailTextView.setText( currentUser.getCorreo() );
        totalSectionsView.setText( "Cursos: " + currentUser.getSeccionesList().size() );

        Picasso.with( getActivity().getApplicationContext() ).load( currentUser.getAvatar() ).transform( new CircleTransform() ).into(pictureImageView);
        return view;
    }

}
