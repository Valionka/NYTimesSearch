package com.codepath.nytimessearch.activities;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.frgament.DatePickerFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FilterActivity extends AppCompatActivity  implements OnItemSelectedListener, OnDateSetListener {

    String sortOrder = null;
    ArrayList<String> desk;

    CheckBox checkArts;
    CheckBox checkFashion;
    CheckBox checkSports;

    Date date;
    static Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        ArrayList<String> fq = getIntent().getStringArrayListExtra("fq");
        setupCheckboxes(fq);

        if(getIntent().getStringExtra("date") != null) {
           // String passedDate = getIntent().getStringExtra("date");
            EditText dateView = (EditText) findViewById(R.id.date);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            try {
                Date passedDate = df.parse(getIntent().getStringExtra("date"));
                SimpleDateFormat df1 = new SimpleDateFormat("MM/dd/yy");
                dateView.setText(df1.format(passedDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        String sort = getIntent().getStringExtra("sort");
        Spinner spinner = (Spinner) findViewById(R.id.spSorter);
        spinner.setOnItemSelectedListener(this);
        if(sort != null) {
            spinner.setSelection(getIndex(spinner, sort));
        }


      /*  CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean checked) {
                // compoundButton is the checkbox
                // boolean is whether or not checkbox is checked
                // Check which checkbox was clicked

                switch(view.getId()) {
                    case R.id.checkbox_arts:
                        if (checked) {
                            desk.add("Arts");
                        } else {
                            desk.remove("Arts");
                        }
                        break;
                    case R.id.checkbox_fashion:
                        if (checked) {
                            desk.add("Fashion & Style");
                        } else {
                            desk.remove("Fashion & Style");
                        }
                        break;
                    case R.id.checkbox_sports:
                        if (checked) {
                            desk.add("Sports");
                        } else {
                            desk.remove("Sports");
                        }
                        break;
                }
            }
        }; */


    }

    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        if(c != null){
            newFragment.setCalender(c);
        }
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        date = c.getTime();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

        EditText dateView = (EditText)findViewById(R.id.date);

        dateView.setText(df.format(date));
        //dateView.setText(date.getMonth() + "/" + date.getDay() + "/" + date.getYear().substring(1));
    }

    // get sinner position for mystring
    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * Handle the save event
     * @param v
     */
    public void onSaveItem(View v){


        Intent data = new Intent();
        String fqFilter;

        desk = new ArrayList<>();
        if(checkArts.isChecked()) {
            desk.add("Arts");
        }
        if (checkFashion.isChecked()) {
            desk.add("Fashion & Style");
        }

        if(checkSports.isChecked()) {
            desk.add("Sports");
        }

        /*if(desk.size() > 0){
            fqFilter = "news_desk:(\"" + TextUtils.join("\" \"", desk) + "\")";
            data.putExtra("fq", fqFilter);
        }*/

        data.putStringArrayListExtra("fq", desk);
        data.putExtra("sort", sortOrder);
        if(date != null) {
            DateFormat df = new SimpleDateFormat("yyyyMMdd");
            data.putExtra("date", df.format(date));
        }
        setResult(RESULT_OK, data);
        finish();
    }

    /**
     * Handle the cancel event
     * @param v
     */
    public void onCancel(View v){
        finish();
    }

    //public void setupCheckboxes(CompoundButton.OnCheckedChangeListener checkListener) {
    public void setupCheckboxes(ArrayList<String> fq) {
         checkArts = (CheckBox) findViewById(R.id.checkbox_arts);
         checkFashion = (CheckBox) findViewById(R.id.checkbox_fashion);
         checkSports = (CheckBox) findViewById(R.id.checkbox_sports);

        if(fq != null) {
            for (int i = 0; i < fq.size(); i++) {
                String filter = fq.get(i);
                switch (filter) {
                    case "Arts":
                        checkArts.setChecked(true);
                        break;
                    case "Fashion & Style":
                        checkFashion.setChecked(true);
                        break;
                    case "Sports":
                        checkSports.setChecked(true);
                }

            }
        }
        /*checkArts.setOnCheckedChangeListener(checkListener);
        checkFashion.setOnCheckedChangeListener(checkListener);
        checkSports.setOnCheckedChangeListener(checkListener);*/
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sortOrder = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
