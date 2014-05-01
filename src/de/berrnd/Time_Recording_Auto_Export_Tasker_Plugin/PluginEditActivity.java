package de.berrnd.Time_Recording_Auto_Export_Tasker_Plugin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class PluginEditActivity extends PreferenceActivity {

    private SharedPreferences PluginPreferences;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager.setDefaultValues(this, R.xml.plugin_edit_activity, true);
        this.PluginPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        final Bundle pluginBundle = getIntent().getBundleExtra("com.twofortyfouram.locale.intent.extra.BUNDLE");
        if (pluginBundle != null) {
            final String exportStartDate = pluginBundle.getString(Constants.BUNDLE_EXTRA_EXPORT_START_DATE);
            final String exportEndDate = pluginBundle.getString(Constants.BUNDLE_EXTRA_EXPORT_END_DATE);
            final boolean exportStartDateAuto = pluginBundle.getBoolean(Constants.BUNDLE_EXTRA_EXPORT_START_DATE_AUTO);
            final boolean exportEndDateAuto = pluginBundle.getBoolean(Constants.BUNDLE_EXTRA_EXPORT_END_DATE_AUTO);
            final String exportFormat = pluginBundle.getString(Constants.BUNDLE_EXTRA_EXPORT_FORMAT);
            final String exportType = pluginBundle.getString(Constants.BUNDLE_EXTRA_EXPORT_TYPE);
            final String destinationFilePath = pluginBundle.getString(Constants.BUNDLE_EXTRA_EXPORT_DESTINATION_FILE_PATH);

            SharedPreferences.Editor editor = this.PluginPreferences.edit();
            editor.putString(Constants.PLUGIN_SETTINGS_EXPORT_START_DATE, exportStartDate);
            editor.putBoolean(Constants.PLUGIN_SETTINGS_EXPORT_START_DATE_AUTO, exportStartDateAuto);
            editor.putString(Constants.PLUGIN_SETTINGS_EXPORT_END_DATE, exportEndDate);
            editor.putBoolean(Constants.PLUGIN_SETTINGS_EXPORT_END_DATE_AUTO, exportEndDateAuto);
            editor.putString(Constants.PLUGIN_SETTINGS_EXPORT_FORMAT, exportFormat);
            editor.putString(Constants.PLUGIN_SETTINGS_EXPORT_TYPE, exportType);
            editor.putString(Constants.PLUGIN_SETTINGS_EXPORT_DESTINATION_FILE_PATH, destinationFilePath);
            editor.commit();
        }

        this.getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PluginEditActivityPreferencesFragment())
                .commit();
    }

    @Override
    public void finish()
    {
        final String exportStartDate = this.PluginPreferences.getString(Constants.PLUGIN_SETTINGS_EXPORT_START_DATE, "");
        final String exportEndDate = this.PluginPreferences.getString(Constants.PLUGIN_SETTINGS_EXPORT_END_DATE, "");
        final boolean exportStartDateAuto = this.PluginPreferences.getBoolean(Constants.PLUGIN_SETTINGS_EXPORT_START_DATE_AUTO, true);
        final boolean exportEndDateAuto = this.PluginPreferences.getBoolean(Constants.PLUGIN_SETTINGS_EXPORT_END_DATE_AUTO, true);
        final String exportFormat = this.PluginPreferences.getString(Constants.PLUGIN_SETTINGS_EXPORT_FORMAT, "");
        final String exportType = this.PluginPreferences.getString(Constants.PLUGIN_SETTINGS_EXPORT_TYPE, "");
        final String destinationFilePath = this.PluginPreferences.getString(Constants.PLUGIN_SETTINGS_EXPORT_DESTINATION_FILE_PATH, "");

        final String blurb = String.format("%s %s %s", exportFormat, getResources().getString(R.string.export_to), destinationFilePath);

        final Intent resultIntent = new Intent();
        final Bundle resultBundle = new Bundle();

        resultBundle.putString(Constants.BUNDLE_EXTRA_EXPORT_START_DATE, exportStartDate);
        resultBundle.putString(Constants.BUNDLE_EXTRA_EXPORT_END_DATE, exportEndDate);
        resultBundle.putBoolean(Constants.BUNDLE_EXTRA_EXPORT_START_DATE_AUTO, exportStartDateAuto);
        resultBundle.putBoolean(Constants.BUNDLE_EXTRA_EXPORT_END_DATE_AUTO, exportEndDateAuto);
        resultBundle.putString(Constants.BUNDLE_EXTRA_EXPORT_FORMAT, exportFormat);
        resultBundle.putString(Constants.BUNDLE_EXTRA_EXPORT_TYPE, exportType);
        resultBundle.putString(Constants.BUNDLE_EXTRA_EXPORT_DESTINATION_FILE_PATH, destinationFilePath);

        resultIntent.putExtra("com.twofortyfouram.locale.intent.extra.BUNDLE", resultBundle);
        resultIntent.putExtra("com.twofortyfouram.locale.intent.extra.BLURB", blurb);

        this.setResult(RESULT_OK, resultIntent);
        super.finish();
    }
}