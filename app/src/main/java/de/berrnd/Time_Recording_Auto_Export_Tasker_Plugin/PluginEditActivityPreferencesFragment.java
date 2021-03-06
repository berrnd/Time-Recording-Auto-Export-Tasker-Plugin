package de.berrnd.Time_Recording_Auto_Export_Tasker_Plugin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import java.util.Map;

public class PluginEditActivityPreferencesFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.addPreferencesFromResource(R.xml.plugin_edit_activity);
        this.getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

        Map<String, ?> keys = this.getPreferenceScreen().getSharedPreferences().getAll();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            this.onSharedPreferenceChanged(this.getPreferenceScreen().getSharedPreferences(), entry.getKey());
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //Display the current setting in summary-text for these settings
        if (key.equals(Constants.PLUGIN_SETTINGS_EXPORT_START_DATE)
                || key.equals(Constants.PLUGIN_SETTINGS_EXPORT_END_DATE)
                || key.equals(Constants.PLUGIN_SETTINGS_EXPORT_FORMAT)
                || key.equals(Constants.PLUGIN_SETTINGS_EXPORT_TYPE)
                || key.equals(Constants.PLUGIN_SETTINGS_EXPORT_DESTINATION_FILE_PATH)) {
            Preference connectionPref = this.findPreference(key);
            connectionPref.setSummary(sharedPreferences.getString(key, ""));
        }

        //Multiple dependent checkbox/datepicker enabled/disabled handling
        if (key.equals(Constants.PLUGIN_SETTINGS_EXPORT_START_DATE_AUTO)
                || key.equals(Constants.PLUGIN_SETTINGS_EXPORT_START_DATE_TODAY)) {
            boolean dependentCheckboxStartDateAuto = sharedPreferences.getBoolean(Constants.PLUGIN_SETTINGS_EXPORT_START_DATE_AUTO, false);
            boolean dependentCheckboxStartDateToday = sharedPreferences.getBoolean(Constants.PLUGIN_SETTINGS_EXPORT_START_DATE_TODAY, false);
            boolean datePickerStartDateEnabled = !dependentCheckboxStartDateAuto && !dependentCheckboxStartDateToday;
            this.findPreference(Constants.PLUGIN_SETTINGS_EXPORT_START_DATE).setEnabled(datePickerStartDateEnabled);

            if (key.equals(Constants.PLUGIN_SETTINGS_EXPORT_START_DATE_AUTO) && dependentCheckboxStartDateAuto) {
                CheckBoxPreference pref = (CheckBoxPreference) this.findPreference(Constants.PLUGIN_SETTINGS_EXPORT_START_DATE_TODAY);
                pref.setChecked(false);
            }

            if (key.equals(Constants.PLUGIN_SETTINGS_EXPORT_START_DATE_TODAY) && dependentCheckboxStartDateToday) {
                CheckBoxPreference pref = (CheckBoxPreference) this.findPreference(Constants.PLUGIN_SETTINGS_EXPORT_START_DATE_AUTO);
                pref.setChecked(false);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}