package de.berrnd.Time_Recording_Auto_Export_Tasker_Plugin;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

public class DatePickerPreference extends DialogPreference implements DatePicker.OnDateChangedListener {

    private static final String VALIDATION_EXPRESSION = "[0-9]*[0-9]*[0-9]*[0-9]-[0-1]*[0-9]-[0-3]*[0-9]";
    private String defaultValue;

    public DatePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize();
    }

    public DatePickerPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.initialize();
    }

    private void initialize() {
        this.setPersistent(true);
        this.defaultValue = DateHelper.toIsoDateString(new Date());
    }

    @Override
    protected View onCreateDialogView() {
        int y = this.getYear();
        int m = this.getMonth();
        int d = this.getDay();

        DatePicker picker = new DatePicker(getContext());
        if (y == -1) {
            Date today = new Date();
            picker.init(
                    DateHelper.getDatePart(today, Calendar.YEAR),
                    DateHelper.getDatePart(today, Calendar.MONTH),
                    DateHelper.getDatePart(today, Calendar.DAY_OF_MONTH),
                    this);
        }
        else
            picker.init(y, m, d, this);

        return picker;
    }

    @Override
    public void setDefaultValue(Object defaultValue) {
        super.setDefaultValue(defaultValue);

        if (!(this.defaultValue instanceof String))
            return;

        if (!(this.defaultValue).matches(VALIDATION_EXPRESSION))
            return;

        this.defaultValue = (String)defaultValue;
        this.setSummary(this.defaultValue);
    }

    private int getYear() {
        String date = this.getPersistedString(this.defaultValue);

        if (date == null || !date.matches(VALIDATION_EXPRESSION)) {
            return -1;
        }

        return Integer.valueOf(date.split("-")[0]);
    }

    private int getMonth() {
        String date = this.getPersistedString(this.defaultValue);

        if (date == null || !date.matches(VALIDATION_EXPRESSION))
            return -1;

        return Integer.valueOf(date.split("-")[1]);
    }

    private int getDay() {
        String date = this.getPersistedString(this.defaultValue);

        if (date == null || !date.matches(VALIDATION_EXPRESSION))
            return -1;

        return Integer.valueOf(date.split("-")[2]);
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.persistString(year + "-"
                + ("0" + monthOfYear).substring(("0" + monthOfYear).length() - 2, ("0" + monthOfYear).length()) + "-"
                + ("0" + dayOfMonth).substring(("0" + dayOfMonth).length() - 2, ("0" + dayOfMonth).length()));
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            String result = this.getPersistedString(this.defaultValue);

            SharedPreferences.Editor editor = this.getEditor();
            editor.putString(this.getKey(), result);

            this.callChangeListener(result);
        }
    }

    @Override
    public void setSummary(CharSequence summary) {
        String currentValue = this.getPersistedString(this.defaultValue);

        if (currentValue == null)
            super.setSummary("");
        else
            super.setSummary(DateHelper.toSystemLocaleString(DateHelper.fromIsoDate(currentValue)));
    }

}
