package de.berrnd.Time_Recording_Auto_Export_Tasker_Plugin;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExportReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        SharedPreferences settings = context.getSharedPreferences(Constants.SHARED_SETTINGS_COMMON, Context.MODE_PRIVATE);
        String lastExport  = settings.getString(Constants.SETTING_LAST_EXPORT, "2014-01-01");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        //Intent data: DateFrom;DateTo;ExportType;ExportFormat;FilePath

        String data = intent.getDataString();
        String[] dataParts = data.split(";");
        String dateFrom = dataParts[0];
        String dateTo = dataParts[1];
        String exportType = dataParts[2];
        String exportFormat = dataParts[3];
        String filePath = dataParts[4];

        if (dateFrom.equalsIgnoreCase("auto")) {
            Date lastExportDate;
            try {
                lastExportDate = dateFormat.parse(lastExport);
                dateFrom = dateFormat.format(DateHelper.addDays(lastExportDate, 1));
            }
            catch (ParseException ex) { }
        }

        if (dateTo.equalsIgnoreCase("auto"))
            dateTo = dateFormat.format(new Date());

        this.doExport(context, dateFrom, dateTo, exportType, exportFormat, filePath);

        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constants.SETTING_LAST_EXPORT, dateTo);
        editor.commit();
    }

    private void doExport(Context context, String dateFrom, String dateTo, String exportType, String exportFormat, final String destinationFilePath) {
        //DATE_FROM yyyy-mm-dd
        //DATE_TO yyyy-mm-dd
        //EXPORT_TYPE [e1-e6]
        //EXPORT_FORMAT [csv, html, xml]

        Intent intent = new Intent("com.dynamicg.timerecording.DATA_EXPORT");
        intent.putExtra("com.dynamicg.timerecording.DATE_FROM", dateFrom);
        intent.putExtra("com.dynamicg.timerecording.DATE_TO", dateTo);
        intent.putExtra("com.dynamicg.timerecording.EXPORT_TYPE", exportType);
        intent.putExtra("com.dynamicg.timerecording.EXPORT_FORMAT", exportFormat);

        final String exportFilePath = "com.dynamicg.timerecording.FILE";

        BroadcastReceiver resultReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent resultIntent) {
                Bundle bundle = this.getResultExtras(true);
                String filePath = bundle.getString(exportFilePath);
                File file = new File(filePath);

                try {
                    copyFile(file, new File(destinationFilePath));
                }
                catch (IOException ex) {
                    //TODO
                }
            }
        };

        context.sendOrderedBroadcast(intent, null, resultReceiver
                , null, Activity.RESULT_OK, null, null);
    }

    private void copyFile(File sourceFile, File destinationFile) throws IOException {
        FileInputStream inStream = new FileInputStream(sourceFile);
        FileOutputStream outStream = new FileOutputStream(destinationFile);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }
}
