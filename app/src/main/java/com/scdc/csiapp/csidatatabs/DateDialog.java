package com.scdc.csiapp.csidatatabs;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Pantearz07 on 28/10/2558.
 */
@SuppressLint("ValidFragment")
class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    TextView txtdate;
    public DateDialog(View view){
        txtdate=(TextView)view;
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String date = String.valueOf(txtdate.getText());
        Log.i("OldDate ", date);
        if(date.length()==0) {
// Use the current date as the default date in the dialog
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            Log.i("Cur Date ", String.valueOf(year)+"/"+String.valueOf(month)+"/"+String.valueOf(day));
            return new DatePickerDialog(getActivity(), this, year, month, day);

        }else{//05-12-2556
            int year = Integer.parseInt(date.substring(6,10));
            int month = Integer.parseInt(date.substring(3, 5));
            int day = Integer.parseInt(date.substring(0, 2));
            Log.i("NewDate ", String.valueOf(year)+"/"+String.valueOf(month)+"/"+String.valueOf(day));
           return new DatePickerDialog(getActivity(), this, year, month-1, day);

        }

    }


    public void onDateSet(DatePicker view, int year, int month, int day) {
        //show to the selected date in the text box
        //String date=day+"/"+(month+1)+"/"+year;
        txtdate.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(String.format("%02d", day)).append("/")
                .append(String.format("%02d", month + 1)).append("/")
                .append(year));

    }


}
