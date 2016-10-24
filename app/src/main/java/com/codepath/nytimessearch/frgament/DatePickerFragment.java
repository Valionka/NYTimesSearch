package com.codepath.nytimessearch.frgament;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * Created by vmiha on 10/23/16.
 */

public class DatePickerFragment extends DialogFragment {

    public DatePickerFragment(){}

    public Calendar getCalender() {
        return calendar;
    }

    public void setCalender(Calendar cal) {
        this.calendar = cal;
    }

    Calendar calendar;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Calendar c;
        // Use the current time as the default values for the picker
        if (calendar == null) {
            c = Calendar.getInstance();
        } else {
            c = calendar;
        }
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        // Activity needs to implement this interface
        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getActivity();

        // Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }
}
