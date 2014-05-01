package de.berrnd.Time_Recording_Auto_Export_Tasker_Plugin;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.util.Date;

public class MainActivityPreferencesFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.addPreferencesFromResource(R.xml.main_activity);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference.getKey().equals("request_feature")) {
            MailHelper.ComposeTextMail(
                    this.getResources().getString(R.string.developer_mail),
                    this.getResources().getString(R.string.app_name) + ": Feature Request",
                    String.format("\n\n---\n%s", this.getAppAndSystemInfosText()),
                    this.getActivity());
        }
        else if (preference.getKey().equals("report_error")) {
            MailHelper.ComposeTextMail(
                    this.getResources().getString(R.string.developer_mail),
                    this.getResources().getString(R.string.app_name) + ": Error Report",
                    String.format("\n\n---\n%s", this.getAppAndSystemInfosText()),
                    this.getActivity());
        }
        else if (preference.getKey().equals("launch_tasker")) {
            this.startActivity(this.getActivity().getPackageManager().getLaunchIntentForPackage("net.dinglisch.android.taskerm"));
        }
        else if (preference.getKey().equals("clear_last_export_date")) {
            SharedPreferences settings = this.getActivity().getSharedPreferences(Constants.SHARED_SETTINGS_COMMON, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(Constants.SETTING_LAST_EXPORT, DateHelper.toIsoDateString(new Date()));
            editor.commit();

            Toast.makeText(this.getActivity(), this.getResources().getString(R.string.clear_last_export_date_cleared), Toast.LENGTH_LONG).show();
        }

        return true;
    }

    private String getAppAndSystemInfosText() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        StringBuilder sb = new StringBuilder();
        sb.append("Device/App info:\n");
        try {
            sb.append(String.format("App Version: %s\n", this.getActivity().getPackageManager().getPackageInfo(this.getActivity().getPackageName(), 0).versionName));
        }
        catch (PackageManager.NameNotFoundException ex)
        { }
        sb.append(String.format("Model: %s (%s)\n", Build.MODEL, Build.MANUFACTURER));
        sb.append(String.format("Brand: %s\n", Build.BRAND));
        sb.append(String.format("Android Version: %s (SDK %d)\n", Build.VERSION.RELEASE, Build.VERSION.SDK_INT));
        sb.append(String.format("Screen Resolution: %dx%d\n", displayMetrics.heightPixels, displayMetrics.widthPixels));

        return sb.toString();
    }

}