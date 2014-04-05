package de.berrnd.Time_Recording_Auto_Export_Tasker_Plugin;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SpinnerHelper {
    public static void SetSpinnerSelectionByValue(Spinner spinner, String value) {
        ArrayAdapter arrayAdapter = (ArrayAdapter)spinner.getAdapter();
        int spinnerPosition = arrayAdapter.getPosition(value);
        spinner.setSelection(spinnerPosition);
    }
}
