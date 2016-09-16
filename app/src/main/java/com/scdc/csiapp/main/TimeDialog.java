package com.scdc.csiapp.main;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Pantearz07 on 29/10/2558.
 */
@SuppressLint("ValidFragment")
public class TimeDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    TextView txttime;
    @SuppressLint("ValidFragment")
    public TimeDialog(View view){
        txttime=(TextView)view;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String time = String.valueOf(txttime.getText());
        Log.i("Old time", time);
        if(time.length()==0) {

            final Calendar c = Calendar.getInstance();

            int mHour = c.get(Calendar.HOUR_OF_DAY);
            int mMinute = c.get(Calendar.MINUTE);
            Log.i("Cur time ", String.valueOf(mHour) + ":" + String.valueOf(mMinute));

            return new TimePickerDialog(getActivity(), this, mHour, mMinute,
                    true);
        }else{//09:55
            int mHour = Integer.parseInt(time.substring(0,2));
            int mMinute = Integer.parseInt(time.substring(3, 5));
            Log.i("New time ", String.valueOf(mHour) + ":" + String.valueOf(mMinute));

            return new TimePickerDialog(getActivity(), this, mHour, mMinute,
                    true);
        }

    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        txttime.setText(new StringBuilder()
                .append(String.format("%02d", hourOfDay)).append(":")
                .append(String.format("%02d", minute)));
    }
}
