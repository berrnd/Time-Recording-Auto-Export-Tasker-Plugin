package de.berrnd.Time_Recording_Auto_Export_Tasker_Plugin;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new MainActivityPreferencesFragment())
                .commit();
    }

}
