package de.berrnd.Time_Recording_Auto_Export_Tasker_Plugin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.Calendar;
import java.util.Date;

public class PluginEditActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plugin_edit);

        final Bundle pluginBundle = getIntent().getBundleExtra("com.twofortyfouram.locale.intent.extra.BUNDLE");

        if (pluginBundle != null) {
            final String exportStartDate = pluginBundle.getString(Constants.BUNDLE_EXTRA_EXPORT_START_DATE);
            final String exportEndDate = pluginBundle.getString(Constants.BUNDLE_EXTRA_EXPORT_END_DATE);
            final boolean exportStartDateAuto = pluginBundle.getBoolean(Constants.BUNDLE_EXTRA_EXPORT_START_DATE_AUTO);
            final boolean exportEndDateAuto = pluginBundle.getBoolean(Constants.BUNDLE_EXTRA_EXPORT_END_DATE_AUTO);
            final String exportFormat = pluginBundle.getString(Constants.BUNDLE_EXTRA_EXPORT_FORMAT);
            final String exportType = pluginBundle.getString(Constants.BUNDLE_EXTRA_EXPORT_TYPE);
            final String destinationFilePath = pluginBundle.getString(Constants.BUNDLE_EXTRA_EXPORT_DESTINATION_FILE_PATH);

            Date exportStartDateAsDate = DateHelper.fromIsoDate(exportStartDate);
            Date exportEndDateAsDate = DateHelper.fromIsoDate(exportEndDate);

            ((DatePicker)findViewById(R.id.datePicker_exportStartDate)).updateDate(
                    DateHelper.getDatePart(exportStartDateAsDate, Calendar.YEAR),
                    DateHelper.getDatePart(exportStartDateAsDate, Calendar.MONTH),
                    DateHelper.getDatePart(exportStartDateAsDate, Calendar.DAY_OF_MONTH));
            ((DatePicker)findViewById(R.id.datePicker_exportEndDate)).updateDate(
                    DateHelper.getDatePart(exportEndDateAsDate, Calendar.YEAR),
                    DateHelper.getDatePart(exportEndDateAsDate, Calendar.MONTH),
                    DateHelper.getDatePart(exportEndDateAsDate, Calendar.DAY_OF_MONTH));

            ((CheckBox)findViewById(R.id.checkBox_exportStartDateAuto)).setChecked(exportStartDateAuto);
            ((CheckBox)findViewById(R.id.checkBox_exportEndDateAuto)).setChecked(exportEndDateAuto);

            SpinnerHelper.SetSpinnerSelectionByValue(((Spinner)findViewById(R.id.spinner_exportFormat)), exportFormat);
            SpinnerHelper.SetSpinnerSelectionByValue(((Spinner)findViewById(R.id.spinner_exportType)), exportType);

            ((EditText)findViewById(R.id.editText_destinationFilePath)).setText(destinationFilePath, TextView.BufferType.EDITABLE);
        }

        Button browseButton = (Button)findViewById(R.id.button_browseDestinationFile);
        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.not_yet_implemented), Toast.LENGTH_LONG).show();
            }
        });

        CheckBox exportStartDateAutoCheckBox = ((CheckBox)findViewById(R.id.checkBox_exportStartDateAuto));
        exportStartDateAutoCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DatePicker datePicker = ((DatePicker) findViewById(R.id.datePicker_exportStartDate));
                datePicker.setEnabled(!isChecked);
            }
        });

        CheckBox exportEndDateAutoCheckBox = ((CheckBox)findViewById(R.id.checkBox_exportEndDateAuto));
        exportEndDateAutoCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DatePicker datePicker = ((DatePicker) findViewById(R.id.datePicker_exportEndDate));
                datePicker.setEnabled(!isChecked);
            }
        });
    }

    @Override
    public void finish()
    {
        final String exportStartDate = DateHelper.toIsoDateString(DateHelper.getDateFromDatePicker((DatePicker) findViewById(R.id.datePicker_exportStartDate)));
        final String exportEndDate = DateHelper.toIsoDateString(DateHelper.getDateFromDatePicker((DatePicker) findViewById(R.id.datePicker_exportEndDate)));
        final boolean exportStartDateAuto = ((CheckBox)findViewById(R.id.checkBox_exportStartDateAuto)).isChecked();
        final boolean exportEndDateAuto = ((CheckBox)findViewById(R.id.checkBox_exportEndDateAuto)).isChecked();
        final String exportFormat = ((Spinner)findViewById(R.id.spinner_exportFormat)).getSelectedItem().toString();
        final String exportType = ((Spinner)findViewById(R.id.spinner_exportType)).getSelectedItem().toString();
        final String destinationFilePath = ((EditText)findViewById(R.id.editText_destinationFilePath)).getText().toString();

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

        setResult(RESULT_OK, resultIntent);
        super.finish();
    }
}