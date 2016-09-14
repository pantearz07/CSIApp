package com.scdc.csiapp.csidatatabs;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.main.GetDateTime;


public class SumCaseTabFragment extends Fragment {

    CoordinatorLayout rootLayout;
    // connect sqlite
    SQLiteDatabase mDb,mReadDb;
    SQLiteDBHelper mDbHelper;
    private Context mContext;
    private PreferenceData mManager;
    ConnectionDetector cd;
    Boolean networkConnectivity = false;
    long isConnectingToInternet = 0;
    GetDateTime getDateTime;
    String officialID, reportID;
    ArrayAdapter<String> adapterCaseType, adapterSubCaseType;

    TextView edtCaseType, edtSubCaseType, edtCDCcenter, edtCDCagency,edtInvestigator;
    TextView  edtStatus, edtInvestDateTime, edtUpdateDateTime;
    String[] updateDT ;
    String message = "";

    Spinner sSCDCcenterType, sSCDCagencyType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewSummaryCSI = inflater.inflate(R.layout.sumcase_tab_layout, null);
// Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        final Context context = viewSummaryCSI.getContext();

        rootLayout = (CoordinatorLayout) viewSummaryCSI.findViewById(R.id.rootLayout);
        mDbHelper = new SQLiteDBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();
        mReadDb = mDbHelper.getReadableDatabase();
        mManager = new PreferenceData(getActivity());
        getDateTime = new GetDateTime();
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        reportID = mManager.getPreferenceData(mManager.PREF_REPORTID);
        cd = new ConnectionDetector(getActivity());
        networkConnectivity = cd.isNetworkAvailable();
        isConnectingToInternet = cd.isConnectingToInternet();
        updateDT = getDateTime.updateDataDateTime();
        Log.i("reportID", reportID);
        Log.i("updateDataDateTime", updateDT[0] + " " + updateDT[1]);


        edtCaseType = (TextView) viewSummaryCSI.findViewById(R.id.edtCaseType);
        edtSubCaseType = (TextView) viewSummaryCSI.findViewById(R.id.edtSubCaseType);
        edtStatus = (TextView) viewSummaryCSI.findViewById(R.id.edtStatus);
        edtInvestDateTime = (TextView) viewSummaryCSI.findViewById(R.id.edtInvestDateTime);
        edtUpdateDateTime = (TextView) viewSummaryCSI.findViewById(R.id.edtUpdateDateTime);
        edtCDCcenter = (TextView) viewSummaryCSI.findViewById(R.id.edtCDCcenter);
        edtCDCagency = (TextView) viewSummaryCSI.findViewById(R.id.edtCDCagency);
        edtInvestigator= (TextView) viewSummaryCSI.findViewById(R.id.edtInvestigator);
        return viewSummaryCSI;
    }

    class showData extends AsyncTask<String, Void, String[]> {
        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar

        }

        @Override
        protected String[] doInBackground(String... params) {
            String[] arrData = {""};
            arrData = mDbHelper.SelectDataCaseScene(params[0]);


            return arrData;
        }

        protected void onPostExecute(String[] arrData) {
            if (arrData != null) {
                if (arrData[30].equals("investigating")) {
                    edtStatus.setText("กำลังดำเนินการตรวจ");

                }else if (arrData[30].equals("waiting")) {
                    edtStatus.setText("ยังไม่ส่งแจ้งเหตุ");

                }else if (arrData[30].equals("receiving")) {
                    edtStatus.setText("แจ้งเหตุแล้ว");

                }else if (arrData[30].equals("investigated")) {
                    edtStatus.setText("ตรวจเสร็จแล้ว");

                }
                if (arrData[21] != null) {
                    edtInvestDateTime.setText(arrData[21] + " เวลาประมาณ " + arrData[20]);
                } else {
                    edtInvestDateTime.setText("-");
                }
                if (arrData[7] != null) {
                    edtUpdateDateTime.setText(arrData[7] + " เวลาประมาณ " + arrData[8]);
                } else {
                    edtInvestDateTime.setText("-");
                }
                if (arrData[0] != null) {
                    edtCaseType.setText(arrData[0]);

                }
                if (arrData[26] != null) {
                    edtSubCaseType.setText(arrData[26]);

                }
                if (arrData[10] != null) {
                    String sToSCDCagencyID = arrData[10];
                    String[] arrData1 = null;
                    arrData1 = mDbHelper.SelectSCDCagencyName(sToSCDCagencyID);
                    if (arrData1 != null) {
                        edtCDCagency.setText(arrData1[3]);
                    }else{
                        edtCDCagency.setText("");
                    }
                }
                if (arrData[11] != null) {
                    //sToSCDCcenter
                    String sToSCDCcenterID = arrData[11];
                    String[] arrData1 = null;
                    arrData1 = mDbHelper.SelectSCDCcenterName(sToSCDCcenterID);

                    if (arrData1 != null) {
                        edtCDCcenter.setText(arrData1[2]);
                    }else{
                        edtCDCcenter.setText("");
                    }
                }

                if (arrData[27] != null) {
                    //sToSCDCcenter
                     String sInvestigatorID = arrData[27];

                    String[] arrData1 = null;
                    arrData1 = mDbHelper.SelectDataOfficial(sInvestigatorID);

                    if (arrData1 != null) {
                        edtInvestigator.setText( arrData1[4] + " " + arrData1[6] + " " + arrData1[7] + " (" + arrData1[8]
                                + ") \nโทร. (" + arrData1[2] + ")-" + arrData1[1]);
                    }else{
                        edtInvestigator.setText("");
                    }


                }
            }
        }
    }


    public void onStart() {
        super.onStart();
        Log.i("Check", "onStartSummary");
        new showData().execute(reportID);

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Log.i("onPause", "onPause sum");

    }

}