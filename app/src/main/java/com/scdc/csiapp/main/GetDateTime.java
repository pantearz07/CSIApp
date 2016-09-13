package com.scdc.csiapp.main;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by Pantearz07 on 21/10/2558.
 */
public class GetDateTime {
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private int mSecond;
    private String CurrentDateTime;

    public String setDateTime() {
        final Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        int in = mYear;
        final String year = String.valueOf(in);

        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        mSecond = c.get(Calendar.SECOND);
        CurrentDateTime = year + String.format("%02d", mMonth + 1)
                + String.format("%02d", mDay) + String.format("%02d", mHour)
                + String.format("%02d", mMinute)
                + String.format("%02d", mSecond);

        return CurrentDateTime;

    }
    public String setDateTimeBD() {
        final Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        int in = mYear+543;
        final String year = String.valueOf(in);

        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        mSecond = c.get(Calendar.SECOND);
        CurrentDateTime = year + String.format("%02d", mMonth + 1)
                + String.format("%02d", mDay) + String.format("%02d", mHour)
                + String.format("%02d", mMinute)
                + String.format("%02d", mSecond);

        return CurrentDateTime;

    }
    public String[] getDateTimeCurrent() {
        String[] DateTime = new String[6];
        final Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        int in = mYear;
        final String year = String.valueOf(in);

        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        mSecond = c.get(Calendar.SECOND);
        DateTime[0] = year;
        DateTime[1] = String.format("%02d", mMonth + 1);
        DateTime[2] = String.format("%02d", mDay);
        DateTime[3] = String.format("%02d", mHour);
        DateTime[4] = String.format("%02d", mMinute);
        DateTime[5] = String.format("%02d", mSecond);

        return DateTime;

    }
    public String[] getDateTimeBDCurrent() {
        String[] DateTime = new String[6];
        final Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        int in = mYear+543;
        final String year = String.valueOf(in);

        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        mSecond = c.get(Calendar.SECOND);
        DateTime[0] = year;
        DateTime[1] = String.format("%02d", mMonth + 1);
        DateTime[2] = String.format("%02d", mDay);
        DateTime[3] = String.format("%02d", mHour);
        DateTime[4] = String.format("%02d", mMinute);
        DateTime[5] = String.format("%02d", mSecond);

        return DateTime;

    }
    public String[] updateDataDateTime() {
        try {
            String[] updateDataDateTime = null;
            final Calendar c = Calendar.getInstance();

            mYear = c.get(Calendar.YEAR);
            int in = mYear;
            final String year = String.valueOf(in);
            Log.i("year", year);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            mSecond = c.get(Calendar.SECOND);
            updateDataDateTime = new String[2];
            updateDataDateTime[0] = String.format("%02d", mDay) + "/"
                    + String.format("%02d", mMonth + 1) + "/" + year;
            updateDataDateTime[1] = String.format("%02d", mHour) + ":"
                    + String.format("%02d", mMinute);
            return updateDataDateTime;
        } catch (Exception e) {
            return null;
        }
    }
    public String[] getDateTimeNow() {
        try {
            String[] updateDataDateTime = null;
            final Calendar c = Calendar.getInstance();

            mYear = c.get(Calendar.YEAR);
            int in = mYear;
            final String year = String.valueOf(in);
            Log.i("year", year);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            mSecond = c.get(Calendar.SECOND);
            updateDataDateTime = new String[2];
            updateDataDateTime[0] =  year+ "-"
                    + String.format("%02d", mMonth + 1) + "-"
                    + String.format("%02d", mDay);
            updateDataDateTime[1] = String.format("%02d", mHour) + ":"
                    + String.format("%02d", mMinute) + ":"
                    + String.format("%02d", mSecond);
            return updateDataDateTime;
        } catch (Exception e) {
            return null;
        }
    }
    public String[] updateDataDateTimeBD() {
        try {
            String[] updateDataDateTime = null;
            final Calendar c = Calendar.getInstance();

            mYear = c.get(Calendar.YEAR);
            int in = mYear+543;
            final String year = String.valueOf(in);
            Log.i("year", year);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            mSecond = c.get(Calendar.SECOND);
            updateDataDateTime = new String[2];
            updateDataDateTime[0] = String.format("%02d", mDay) + "/"
                    + String.format("%02d", mMonth + 1) + "/" + year;
            updateDataDateTime[1] = String.format("%02d", mHour) + ":"
                    + String.format("%02d", mMinute);
            return updateDataDateTime;
        } catch (Exception e) {
            return null;
        }
    }
    public String changeDateFormatToCalendar(String date) {
        try {

            if (date == null) {
                return null;
            }
            String[] split = date.split("-");
            //String $temp = ('/', date);
            return split[2] + '/' + split[1] + '/' + split[0];


        } catch (Exception e) {
            return null;
        }
    }

    public String changeDateFormatToDB(String date) {
        try {

            if (date == null) {
                return null;
            }
            String[] split = date.split("/");
            //String $temp = ('/', date);
            return split[2] + '-' + split[1] + '-' + split[0];


        } catch (Exception e) {
            return null;
        }
    }
}