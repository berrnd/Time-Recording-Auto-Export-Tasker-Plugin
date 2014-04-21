package de.berrnd.Time_Recording_Auto_Export_Tasker_Plugin;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class MainActivityPreferencesFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.addPreferencesFromResource(R.xml.main_activity);
    }

}