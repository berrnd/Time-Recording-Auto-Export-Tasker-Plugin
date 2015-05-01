package de.berrnd.Time_Recording_Auto_Export_Tasker_Plugin;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PluginReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        SharedPreferences settings = context.getSharedPreferences(Constants.SHARED_SETTINGS_COMMON, Context.MODE_PRIVATE);
        String lastExport = settings.getString(Constants.SETTING_LAST_EXPORT, DateHelper.toIsoDateString(new Date()));

        final Bundle pluginBundle = intent.getBundleExtra("com.twofortyfouram.locale.intent.extra.BUNDLE");
        String exportStartDate = pluginBundle.getString(Constants.BUNDLE_EXTRA_EXPORT_START_DATE);
        String exportEndDate = pluginBundle.getString(Constants.BUNDLE_EXTRA_EXPORT_END_DATE);
        boolean exportStartDateAuto = pluginBundle.getBoolean(Constants.BUNDLE_EXTRA_EXPORT_START_DATE_AUTO);
        boolean exportStartDateToday = pluginBundle.getBoolean(Constants.BUNDLE_EXTRA_EXPORT_START_DATE_TODAY);
        boolean exportEndDateAuto = pluginBundle.getBoolean(Constants.BUNDLE_EXTRA_EXPORT_END_DATE_AUTO);
        String exportFormat = pluginBundle.getString(Constants.BUNDLE_EXTRA_EXPORT_FORMAT);
        String exportType = pluginBundle.getString(Constants.BUNDLE_EXTRA_EXPORT_TYPE);
        String destinationFilePath = pluginBundle.getString(Constants.BUNDLE_EXTRA_EXPORT_DESTINATION_FILE_PATH);

        if (exportStartDateAuto)
            exportStartDate = DateHelper.toIsoDateString(DateHelper.addDays(DateHelper.fromIsoDate(lastExport), 1));

        if (exportStartDateToday)
            exportStartDate = DateHelper.toIsoDateString(new Date());

        if (exportEndDateAuto)
            exportEndDate = DateHelper.toIsoDateString(new Date());

        this.doExport(context, exportStartDate, exportEndDate, exportType, exportFormat, destinationFilePath);
        Toast.makeText(context, String.format("%s %s %s", exportFormat, context.getResources().getString(R.string.exported_to), destinationFilePath), Toast.LENGTH_LONG).show();

        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constants.SETTING_LAST_EXPORT, exportEndDate);
        editor.commit();
    }

    private void doExport(Context context, String dateFrom, String dateTo, String exportType, String exportFormat, final String destinationFilePath) {
        //DATE_FROM yyyy-mm-dd
        //DATE_TO yyyy-mm-dd
        //EXPORT_TYPE [e1-e6]
        //EXPORT_FORMAT [csv, html, xml, xls]

        Intent intent = new Intent("com.dynamicg.timerecording.DATA_EXPORT");
        intent.putExtra("com.dynamicg.timerecording.DATE_FROM", dateFrom);
        intent.putExtra("com.dynamicg.timerecording.DATE_TO", dateTo);
        intent.putExtra("com.dynamicg.timerecording.EXPORT_TYPE", exportType.toLowerCase());
        intent.putExtra("com.dynamicg.timerecording.EXPORT_FORMAT", exportFormat.toLowerCase());

        final String exportFilePath = "com.dynamicg.timerecording.FILE";

        BroadcastReceiver resultReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent resultIntent) {
                Bundle bundle = this.getResultExtras(true);
                String filePath = bundle.getString(exportFilePath);
                File file = new File(filePath);

                try {
                    FileHelper.copyFile(file, new File(destinationFilePath));
                } catch (IOException ex) {
                    //TODO
                }
            }
        };

        context.sendOrderedBroadcast(intent, null, resultReceiver, null, Activity.RESULT_OK, null, null);
    }

}
