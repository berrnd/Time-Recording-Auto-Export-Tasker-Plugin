package de.berrnd.Time_Recording_Auto_Export_Tasker_Plugin;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.util.DisplayMetrics;

import java.util.Map;

public class MainActivityPreferencesFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.addPreferencesFromResource(R.xml.main_activity);
        this.getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

        Map<String, ?> keys = this.getPreferenceScreen().getSharedPreferences().getAll();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            this.onSharedPreferenceChanged(this.getPreferenceScreen().getSharedPreferences(), entry.getKey());
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference.getKey().equals("request_feature")) {
            MailHelper.ComposeTextMail(
                    this.getResources().getString(R.string.developer_mail),
                    this.getResources().getString(R.string.app_name) + ": Feature Request",
                    String.format("\n\n---\n%s", this.getAppAndSystemInfosText()),
                    this.getActivity());
        } else if (preference.getKey().equals("report_error")) {
            MailHelper.ComposeTextMail(
                    this.getResources().getString(R.string.developer_mail),
                    this.getResources().getString(R.string.app_name) + ": Error Report",
                    String.format("\n\n---\n%s", this.getAppAndSystemInfosText()),
                    this.getActivity());
        } else if (preference.getKey().equals("launch_tasker")) {
            this.startActivity(this.getActivity().getPackageManager().getLaunchIntentForPackage("net.dinglisch.android.taskerm"));
        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //Display the current setting in summary-text for these settings
        if (key.equals(Constants.SETTING_LAST_EXPORT)) {
            Preference connectionPref = this.findPreference(key);
            connectionPref.setSummary(sharedPreferences.getString(key, ""));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        this.getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private String getAppAndSystemInfosText() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        StringBuilder sb = new StringBuilder();
        sb.append("Device/App info:\n");
        try {
            sb.append(String.format("App Version: %s\n", this.getActivity().getPackageManager().getPackageInfo(this.getActivity().getPackageName(), 0).versionName));
        } catch (PackageManager.NameNotFoundException ex) {
        }
        sb.append(String.format("Model: %s (%s)\n", Build.MODEL, Build.MANUFACTURER));
        sb.append(String.format("Brand: %s\n", Build.BRAND));
        sb.append(String.format("Android Version: %s (SDK %d)\n", Build.VERSION.RELEASE, Build.VERSION.SDK_INT));
        sb.append(String.format("Screen Resolution: %dx%d\n", displayMetrics.heightPixels, displayMetrics.widthPixels));

        return sb.toString();
    }

}