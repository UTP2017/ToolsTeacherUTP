package pe.edu.utp.toolsteacherutp.Fragments;


import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.RingtonePreference;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.preference.ListPreference;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import pe.edu.utp.toolsteacherutp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListPreference mListPreference;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        /*mListPreference = (ListPreference)  getPreferenceManager().findPreference("preference_key");
        mListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String stringValue = newValue.toString();

                if (preference instanceof ListPreference) {
                    // For list preferences, look up the correct display value in
                    // the preference's 'entries' list.
                    ListPreference listPreference = (ListPreference) preference;
                    int index = listPreference.findIndexOfValue(stringValue);

                    // Set the summary to reflect the new value.
                    preference.setSummary(
                            index >= 0
                                    ? listPreference.getEntries()[index]
                                    : null);

                } else if (preference instanceof RingtonePreference) {
                    // For ringtone preferences, look up the correct display value
                    // using RingtoneManager.
                    if (TextUtils.isEmpty(stringValue)) {
                        // Empty values correspond to 'silent' (no ringtone).
                        preference.setSummary(R.string.pref_ringtone_silent);

                    } else {
                        Ringtone ringtone = RingtoneManager.getRingtone(
                                preference.getContext(), Uri.parse(stringValue));

                        if (ringtone == null) {
                            // Clear the summary if there was a lookup error.
                            preference.setSummary(null);
                        } else {
                            // Set the summary to reflect the new ringtone display
                            // name.
                            String name = ringtone.getTitle(preference.getContext());
                            preference.setSummary(name);
                        }
                    }

                } else {
                    // For all other preferences, set the summary to the value's
                    // simple string representation.
                    preference.setSummary(stringValue);
                }
                return true;
            }
        });*/
        //return inflater.inflate(R.layout.fragment_manage, container, false);
        return inflater.inflate(R.layout.fragment_manage, container, false);
    }

}
