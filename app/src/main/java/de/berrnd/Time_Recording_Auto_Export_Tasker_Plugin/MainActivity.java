package de.berrnd.Time_Recording_Auto_Export_Tasker_Plugin;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import java.util.Date;

public class MainActivity extends Activity {

    private SharedPreferences ActivityPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager.setDefaultValues(this, R.xml.main_activity, true);
        this.ActivityPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences appSettings = this.getSharedPreferences(Constants.SHARED_SETTINGS_COMMON, Context.MODE_PRIVATE);
        String exportStartDate = appSettings.getString(Constants.SETTING_LAST_EXPORT, "");

        SharedPreferences.Editor editor = this.ActivityPreferences.edit();
        editor.putString(Constants.SETTING_LAST_EXPORT, exportStartDate);
        editor.commit();

        this.getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new MainActivityPreferencesFragment())
                .commit();
    }

    @Override
    public void finish() {
        final String exportStartDate = this.ActivityPreferences.getString(Constants.SETTING_LAST_EXPORT, "");

        SharedPreferences appSettings = this.getSharedPreferences(Constants.SHARED_SETTINGS_COMMON, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = appSettings.edit();
        editor.putString(Constants.SETTING_LAST_EXPORT, exportStartDate);
        editor.commit();

        this.setResult(RESULT_OK);
        super.finish();
    }

}
