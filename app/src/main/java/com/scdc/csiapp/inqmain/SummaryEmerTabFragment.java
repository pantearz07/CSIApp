package com.scdc.csiapp.inqmain;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.ConnectServer;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.main.AcceptListFragment;
import com.scdc.csiapp.main.GetDateTime;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class SummaryEmerTabFragment extends Fragment {
    FloatingActionButton fabBtn;
    CoordinatorLayout rootLayout;
    // connect sqlite
    SQLiteDatabase mDb;
    SQLiteDBHelper mDbHelper;
    private Context mContext;
    private PreferenceData mManager;
    ConnectionDetector cd;
    Boolean networkConnectivity = false;
    long isConnectingToInternet = 0;
    GetDateTime getDateTime;
    String officialID, reportID;
    ArrayAdapter<String> adapterCaseType, adapterSubCaseType;
    EditText edtReportNo;
    FragmentManager mFragmentManager;
    AcceptListFragment acceptListFragment;
    Spinner spnCaseType, spnSubCaseType;
    TextView edtYear, edtStatus, edtInvestDateTime, edtUpdateDateTime,
            txtButtonCount1, txtButtonCount2, txtButtonCount3, txtButtonCount4, txtButtonCount5;
    String[] updateDT, selectedCaseType, selectedSubCaseType;
    String message = "";
    String[][] mTypeAgencyArray, mTypeCenterArray = null;
    String[] mTypeAgencyArray2, mTypeCenterArray2, mAgencyID, mCenterID = null;
    ArrayAdapter<String> adapterSCDCcenter, adapterSCDCagency;
    private String selectedAgency, SelectedAgencyID, selectedCenter, SelectedCenterID = null;
    private View mViewBtnSaveServer, mViewBtnFullReport, mViewBtnTransReport, layoutButton1, layoutButton2, layoutButton3, layoutButton4, layoutButton5;
    Spinner spnSCDCcenterType, spnSCDCagencyType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewSummaryCSI = inflater.inflate(R.layout.summarycsi_tab_layout, null);
// Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        final Context context = viewSummaryCSI.getContext();
        mFragmentManager = getActivity().getSupportFragmentManager();
        acceptListFragment = new AcceptListFragment();
        rootLayout = (CoordinatorLayout) viewSummaryCSI.findViewById(R.id.rootLayout);
        mDbHelper = new SQLiteDBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();
        mManager = new PreferenceData(getActivity());
        getDateTime = new GetDateTime();
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        reportID = mManager.getPreferenceData(mManager.PREF_REPORTID);
        cd = new ConnectionDetector(getActivity());
        networkConnectivity = cd.isNetworkAvailable();
        isConnectingToInternet = cd.isConnectingToInternet();
        updateDT = getDateTime.getDateTimeNow();
        Log.i("reportID", reportID);
        Log.i("updateDataDateTime", updateDT[0] + " " + updateDT[1]);

        edtYear = (TextView) viewSummaryCSI.findViewById(R.id.edtYear);
        edtYear.setVisibility(View.GONE);
        edtReportNo = (EditText) viewSummaryCSI.findViewById(R.id.edtReportNo);
        edtReportNo.setVisibility(View.GONE);
        spnCaseType = (Spinner) viewSummaryCSI.findViewById(R.id.spnCaseType);
        spnSubCaseType = (Spinner) viewSummaryCSI.findViewById(R.id.spnSubCaseType);
        edtStatus = (TextView) viewSummaryCSI.findViewById(R.id.edtStatus);
        edtInvestDateTime = (TextView) viewSummaryCSI.findViewById(R.id.edtInvestDateTime);
        edtUpdateDateTime = (TextView) viewSummaryCSI.findViewById(R.id.edtUpdateDateTime);
        TextView txtInvestDateTimeTitle = (TextView) viewSummaryCSI.findViewById(R.id.txtInvestDateTimeTitle);
        txtInvestDateTimeTitle.setText("วันเวลาแจ้งเหตุ:");
        final String[] CaseType = getResources().getStringArray(R.array.casetype);
        adapterCaseType = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, CaseType);
        spnCaseType.setAdapter(adapterCaseType);
        selectedCaseType = new String[1];
        spnCaseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCaseType[0] = CaseType[position];
            }

            public void onNothingSelected(AdapterView<?> parent) {
                selectedCaseType[0] = CaseType[0];
            }
        });

        final String[] SubCaseType = getResources().getStringArray(R.array.subcasetypeproperties);
        adapterSubCaseType = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, SubCaseType);
        spnSubCaseType.setAdapter(adapterSubCaseType);
        selectedSubCaseType = new String[1];
        spnSubCaseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSubCaseType[0] = SubCaseType[position];
            }

            public void onNothingSelected(AdapterView<?> parent) {
                selectedSubCaseType[0] = SubCaseType[0];
            }
        });
        //View linearLayoutDocfiles= viewSummaryCSI.findViewById(R.id.linearLayoutDocfiles);
        //linearLayoutDocfiles.setVisibility(View.GONE);
        mViewBtnSaveServer = viewSummaryCSI.findViewById(R.id.layoutButtonServer);
        TextView txtButtonServer1 = (TextView) viewSummaryCSI.findViewById(R.id.txtButtonServer1);
        txtButtonServer1.setText("แจ้งเหตุ");
        mViewBtnTransReport = viewSummaryCSI.findViewById(R.id.layoutButtonTransReport);
        mViewBtnTransReport.setVisibility(View.GONE);

/*
        layoutButton1 = viewSummaryCSI.findViewById(R.id.layoutButton1);
        layoutButton2 = viewSummaryCSI.findViewById(R.id.layoutButton2);
        layoutButton3 = viewSummaryCSI.findViewById(R.id.layoutButton3);
        layoutButton4 = viewSummaryCSI.findViewById(R.id.layoutButton4);
        layoutButton5 = viewSummaryCSI.findViewById(R.id.layoutButton5);*/
        mViewBtnSaveServer.setOnClickListener(new SummaryOnClickListener());
        mViewBtnTransReport.setOnClickListener(new SummaryOnClickListener());
   /*     layoutButton1.setOnClickListener(new SummaryOnClickListener());
        layoutButton2.setOnClickListener(new SummaryOnClickListener());
        layoutButton3.setOnClickListener(new SummaryOnClickListener());
        layoutButton4.setOnClickListener(new SummaryOnClickListener());
        layoutButton5.setOnClickListener(new SummaryOnClickListener());
*/
        fabBtn = (FloatingActionButton) viewSummaryCSI.findViewById(R.id.fabBtnSum);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new saveData().execute(reportID,
                        String.valueOf(edtReportNo.getText()),
                        selectedCaseType[0],
                        selectedSubCaseType[0],
                        updateDT[0],
                        updateDT[1]);


            }
        });
        new showData().execute(reportID);
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
                Log.i("edtStatus", arrData[30]);

                if (arrData[30].equals("investigating")) {
                    edtStatus.setText("กำลังดำเนินการตรวจ");

                } else if (arrData[30].equals("waiting")) {
                    edtStatus.setText("ยังไม่ส่งแจ้งเหตุ");

                } else if (arrData[30].equals("receiving")) {
                    edtStatus.setText("แจ้งเหตุแล้ว รอจ่ายงาน");
                    mViewBtnSaveServer.setVisibility(View.GONE);

                } else if (arrData[30].equals("investigated")) {
                    edtStatus.setText("ตรวจเสร็จแล้ว");

                }
                if (arrData[21] != null) {

                    edtInvestDateTime.setText(getDateTime.changeDateFormatToCalendar(arrData[21]) + " เวลาประมาณ " + arrData[20]);

                } else {
                    edtInvestDateTime.setText("-");
                }
                if (arrData[7] != null) {
                    edtUpdateDateTime.setText(getDateTime.changeDateFormatToCalendar(arrData[7]) + " เวลาประมาณ " + arrData[8]);
                } else {
                    edtInvestDateTime.setText("-");
                }
                if (arrData[0] != null) {
                    for (int i = 0; i < adapterCaseType.getCount(); i++) {
                        if (arrData[0].trim().equals(adapterCaseType.getItem(i).toString())) {
                            spnCaseType.setSelection(i);
                            break;
                        }
                    }
                }
                if (arrData[26] != null) {
                    for (int i = 0; i < adapterSubCaseType.getCount(); i++) {
                        if (arrData[26].trim().equals(adapterSubCaseType.getItem(i).toString())) {
                            spnSubCaseType.setSelection(i);
                            break;
                        }
                    }
                }

            }
        }
    }

    class saveData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
        }

        @Override
        protected String doInBackground(String... params) {
            String arrData = "";
            long updateCaseScene = mDbHelper.updateCaseSceneSum(params[0], params[1], params[2],
                    params[3], params[4], params[5]);

            if (updateCaseScene <= 0) {
                Log.i("update sum", "error");
                arrData = "error";
            } else {
                Log.i("update sum", "save");
                arrData = "save";
            }

            return arrData;
        }

        protected void onPostExecute(String arrData) {
            if (arrData == "save") {
                message = "บันทึกข้อมูลเรียบร้อยแล้ว";

            } else {
                message = "เกิดข้อผิดพลาด";

            }
            Log.i("save sum", message);
            //Snackbar.make(fabBtn.getRootView(), message, Snackbar.LENGTH_LONG)
            //        .setAction("Action", null).show();
           /* Toast.makeText(getActivity(),
                 message,
                   Toast.LENGTH_SHORT).show();*/

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

        new saveData().execute(reportID,
                String.valueOf(edtReportNo.getText()),
                selectedCaseType[0],
                selectedSubCaseType[0],
                updateDT[0],
                updateDT[1]);


    }

    public void uploadDataToServer() {
        Log.i("agency center", SelectedAgencyID + " " + SelectedCenterID);
        new saveEmergency().execute(reportID, SelectedCenterID, SelectedAgencyID);
        new saveDataToServer().execute(reportID);
        new saveSuffererToServer().execute(reportID);
        new saveReceivingCaseToServer().execute(reportID);

        //new saveOtherOffInCaseToServer().execute(reportID);

    }

    private class SummaryOnClickListener implements View.OnClickListener {
        public void onClick(View v) {
            if (v == mViewBtnSaveServer) {

                //new saveFullReport().execute(reportID);
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();

                View view1 = inflater.inflate(R.layout.emergency_send_case, null);
                dialog.setView(view1);

                spnSCDCcenterType = (Spinner) view1.findViewById(R.id.spnSCDCcenterType);
                spnSCDCagencyType = (Spinner) view1.findViewById(R.id.spnSCDCagencyType);
                mTypeCenterArray = mDbHelper
                        .SelectAllSCDCCenter();

                if (mTypeCenterArray != null) {
                    mCenterID = new String[mTypeCenterArray.length];
                    mTypeCenterArray2 = new String[mTypeCenterArray.length];
                    for (int i = 0; i < mTypeCenterArray.length; i++) {
                        mTypeCenterArray2[i] = mTypeCenterArray[i][2];
                        mCenterID[i] = mTypeCenterArray[i][1];
                        Log.i("SelectAllSCDCCenter",
                                mTypeCenterArray2[i].toString());
                    }
                    adapterSCDCcenter = new ArrayAdapter<String>(
                            getActivity(),
                            android.R.layout.simple_dropdown_item_1line,
                            mTypeCenterArray2);
                    //set the view for the Drop down list
                    adapterSCDCcenter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spnSCDCcenterType.setAdapter(adapterSCDCcenter);
                    spnSCDCcenterType.setOnItemSelectedListener(new EmerOnItemSelectedListener());
                    String selectedCenterName = String
                            .valueOf(spnSCDCcenterType
                                    .getSelectedItem());
                    if (mTypeCenterArray2 == null) {
                        Log.i("selectedCenterName ", "null");

                    } else {
                        for (int i = 0; i < mTypeCenterArray2.length; i++) {

                            if (selectedCenterName == mTypeCenterArray2[i]) {
                                SelectedCenterID = mTypeCenterArray[i][1];
                                Log.i("selectedCenterName", SelectedCenterID);
                            }
                        }
                        // new SaveInspectorReceivingCase().execute(reportID, SelectedInspectorID, "accept", "");
                        Log.i("selectedCenterName ", "not null");
                    }


                    Log.i("selectedCenterName ", SelectedCenterID);

                } else {
                    Log.i("SelectAllSCDCCenter", "null");
                }

                mTypeAgencyArray = mDbHelper
                        .SelectAllSCDCAgency();

                if (mTypeAgencyArray != null) {
                    mAgencyID = new String[mTypeAgencyArray.length];
                    mTypeAgencyArray2 = new String[mTypeAgencyArray.length];
                    for (int i = 0; i < mTypeAgencyArray.length; i++) {
                        mTypeAgencyArray2[i] = mTypeAgencyArray[i][3];
                        mAgencyID[i] = mTypeAgencyArray[i][0];
                        Log.i("SelectAllSCDCAgency",
                                mTypeAgencyArray2[i].toString());
                    }
                    adapterSCDCagency = new ArrayAdapter<String>(
                            getActivity(),
                            android.R.layout.simple_dropdown_item_1line,
                            mTypeAgencyArray2);
                    //set the view for the Drop down list
                    adapterSCDCagency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spnSCDCagencyType.setAdapter(adapterSCDCagency);
                    spnSCDCagencyType.setOnItemSelectedListener(new EmerOnItemSelectedListener());


                    String SelectedSCDCagencyName = String
                            .valueOf(spnSCDCagencyType
                                    .getSelectedItem());
                    if (mTypeAgencyArray2 == null) {
                        Log.i("SelectedAgency ", "null");

                    } else {
                        for (int i = 0; i < mTypeAgencyArray2.length; i++) {

                            if (SelectedSCDCagencyName == mTypeAgencyArray2[i]) {
                                SelectedAgencyID = mTypeAgencyArray[i][0];
                                Log.i("SelectedAgency", SelectedAgencyID);
                            }
                        }
                        // new SaveInspectorReceivingCase().execute(reportID, SelectedInspectorID, "accept", "");
                        Log.i("SelectedAgency ", "not null");
                    }


                    Log.i("SelectedAgency ", SelectedAgencyID);
                } else {
                    Log.i("SelectAllSCDCAgency", "null");
                }
                dialog.setMessage("แจ้งเหตุ");
                dialog.setPositiveButton("ส่ง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        uploadDataToServer();
                        new showData().execute(reportID);
                        Toast.makeText(getActivity(),
                                "อัพโหลดข้อมูล แจ้งเหตุ เรียบร้อย", Toast.LENGTH_SHORT).show();

                        //FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                        //fragmentTransaction.replace(R.id.containerView, acceptListFragment).addToBackStack(null).commit();
                    }
                });
                dialog.setCancelable(true);
                dialog.setNeutralButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


                dialog.create();
                dialog.show();

            }
           /* if (v == mViewBtnTransReport) {
                Log.i("mViewBtnTransReport", "mViewBtnTransReport");
               new DownloadFileAsync().execute(reportID);
               // String fromURL = "http://10.199.120.145/CSIReport/csi_files/Documents/";
                //String root = "";
               // root = Environment.getExternalStorageDirectory().toString();
              //  String path="root"+"/CSIFiles/Documents/";

            }

            if (v == layoutButton1) {
                Log.i("layoutButton1", "layoutButton1");

            }
            if (v == layoutButton2) {
                Log.i("layoutButto2", "layoutButton2");

            }
            if (v == layoutButton3) {
                Log.i("layoutButton3", "layoutButton3");

            }
            if (v == layoutButton4) {
                Log.i("layoutButton4", "layoutButton4");

            }
            if (v == layoutButton5) {
                Log.i("layoutButton5", "layoutButton5");

            }*/
        }
    }

    public class EmerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            String selectedItem = parent.getItemAtPosition(pos).toString();

            //check which spinner triggered the listener
            switch (parent.getId()) {

                case R.id.spnSCDCagencyType:
                    //make sure the animal was already selected during the onCreate

                    String SelectedSCDCagencyName = String
                            .valueOf(spnSCDCagencyType
                                    .getSelectedItem());
                    if (mTypeAgencyArray2 == null) {
                        Log.i("SelectedAgency ", "null");

                    } else {
                        for (int i = 0; i < mTypeAgencyArray2.length; i++) {

                            if (SelectedSCDCagencyName == mTypeAgencyArray2[i]) {
                                SelectedAgencyID = mTypeAgencyArray[i][0];
                                Log.i("SelectedAgency", SelectedAgencyID);
                            }
                        }
                        // new SaveInspectorReceivingCase().execute(reportID, SelectedInspectorID, "accept", "");
                        Log.i("SelectedAgency ", "not null");
                    }


                    Log.i("SelectedAgency ", SelectedAgencyID);
                    break;
                case R.id.spnSCDCcenterType:
                    //make sure the animal was already selected during the onCreate
                    String selectedCenterName = String
                            .valueOf(spnSCDCcenterType
                                    .getSelectedItem());
                    if (mTypeCenterArray2 == null) {
                        Log.i("selectedCenterName ", "null");

                    } else {
                        for (int i = 0; i < mTypeCenterArray2.length; i++) {

                            if (selectedCenterName == mTypeCenterArray2[i]) {
                                SelectedCenterID = mTypeCenterArray[i][1];
                                Log.i("selectedCenterName", SelectedCenterID);
                            }
                        }
                        // new SaveInspectorReceivingCase().execute(reportID, SelectedInspectorID, "accept", "");
                        Log.i("selectedCenterName ", "not null");
                    }


                    Log.i("selectedCenterName ", SelectedCenterID);

                    break;
            }


        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }

    class DownloadFileAsync extends AsyncTask<String, Void, String> {

        private final ProgressDialog dialog = new ProgressDialog(
                getActivity());

        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
            this.dialog.setMessage("Loading...");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            if (downloadDocFile(params[0])) {

                Log.i("downloadDocFile", "OK");
            } else {
                Log.i("downloadDocFile", "ERROR");
            }


            return null;
        }

        protected void onPostExecute(String arrData) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    class saveDataToServer extends AsyncTask<String, Void, String> {

        private final ProgressDialog dialog = new ProgressDialog(
                getActivity());

        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
            this.dialog.setMessage("Loading...");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            if (saveCaseScene(params[0])) {

                Log.i("saveCaseScene ToServer", "OK");
            }


            return null;
        }

        protected void onPostExecute(String arrData) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    class saveSuffererToServer extends AsyncTask<String, Void, String> {

        private final ProgressDialog dialog = new ProgressDialog(
                getActivity());

        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
            this.dialog.setMessage("Loading...");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            if (saveSufferer(params[0])) {
                Log.i("saveSufferer ToServer", "OK");
            }


            return null;
        }

        protected void onPostExecute(String arrData) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }


    class saveReceivingCaseToServer extends AsyncTask<String, Void, String> {

        private final ProgressDialog dialog = new ProgressDialog(
                getActivity());

        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
            this.dialog.setMessage("Loading...");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            if (saveReceivingCase(params[0])) {

                Log.i("saveReceiving ToServer", "OK");
            }


            return null;
        }

        protected void onPostExecute(String arrData) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    class saveOtherOffInCaseToServer extends AsyncTask<String, Void, String> {

        private final ProgressDialog dialog = new ProgressDialog(
                getActivity());

        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
            this.dialog.setMessage("Loading...");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            if (saveOtherOfficialInCase(params[0])) {

                Log.i("saveOtherOffInCase", "OK");
            }


            return null;
        }

        protected void onPostExecute(String arrData) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    public boolean saveCaseScene(String sReportID) {
        String arrData[] = mDbHelper.SelectDataCaseScene(sReportID);
        if (arrData != null) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("sCaseReportID", sReportID));
            params.add(new BasicNameValuePair("sReportNo", arrData[28]));
            params.add(new BasicNameValuePair("sInvestigatorOfficialID", ""));
            params.add(new BasicNameValuePair("sYear", arrData[29]));

            params.add(new BasicNameValuePair("sCaseTypeName", arrData[0]));
            params.add(new BasicNameValuePair("sSubCaseTypeName", arrData[26]));
            params.add(new BasicNameValuePair("sNotifiedBy", arrData[24]));
            params.add(new BasicNameValuePair("sCaseTel", arrData[31]));
            params.add(new BasicNameValuePair("sReportStatus", "receiving"));

            params.add(new BasicNameValuePair("sPoliceStation", arrData[22]));
            params.add(new BasicNameValuePair("sPoliceProvince", arrData[23]));
            params.add(new BasicNameValuePair("sReceivingCaseDate", arrData[21]));
            params.add(new BasicNameValuePair("sReceivingCaseTime", arrData[20]));
            params.add(new BasicNameValuePair("sHappenCaseDate", arrData[19]));
            params.add(new BasicNameValuePair("sHappenCaseTime", arrData[18]));
            params.add(new BasicNameValuePair("sKnowCaseDate", arrData[17]));
            params.add(new BasicNameValuePair("sKnowCaseTime", arrData[16]));

            params.add(new BasicNameValuePair("sSceneInvestDate", arrData[5]));
            params.add(new BasicNameValuePair("sSceneInvestTime", arrData[4]));
            params.add(new BasicNameValuePair("sCompleteSceneDate", arrData[15]));
            params.add(new BasicNameValuePair("sCompleteSceneTime", arrData[14]));
            params.add(new BasicNameValuePair("sLastDateUpdateData", arrData[7]));
            params.add(new BasicNameValuePair("sLastTimeUpdateData", arrData[8]));
            params.add(new BasicNameValuePair("sLocaleName", arrData[6]));
            params.add(new BasicNameValuePair("sHouseNo", arrData[32]));
            params.add(new BasicNameValuePair("sVillageNo", arrData[33]));
            params.add(new BasicNameValuePair("sVillageName", arrData[34]));
            params.add(new BasicNameValuePair("sLaneName", arrData[35]));
            params.add(new BasicNameValuePair("sRoadName", arrData[36]));
            params.add(new BasicNameValuePair("sDistrict", arrData[37]));
            params.add(new BasicNameValuePair("sAmphur", arrData[13]));
            params.add(new BasicNameValuePair("sProvince", arrData[38]));
            params.add(new BasicNameValuePair("sPostalCode", arrData[12]));
            params.add(new BasicNameValuePair("sLongitude", arrData[40]));
            params.add(new BasicNameValuePair("sLatitude", arrData[39]));

            params.add(new BasicNameValuePair("sVehicleCode", arrData[1]));
            params.add(new BasicNameValuePair("sFeatureInsideDetail", arrData[2]));
            params.add(new BasicNameValuePair("sConfineSufferer", arrData[3]));
            params.add(new BasicNameValuePair("sCriminalUsedWeapon", arrData[9]));
            params.add(new BasicNameValuePair("sMaleCriminalNum", arrData[45]));
            params.add(new BasicNameValuePair("sFemaleCriminalNum", arrData[46]));

            params.add(new BasicNameValuePair("sCircumstanceOfCaseDetail", arrData[41]));
            params.add(new BasicNameValuePair("sPersonInvolvedDetail", arrData[42]));
            params.add(new BasicNameValuePair("sFullEvidencePerformed", arrData[43]));
            params.add(new BasicNameValuePair("sAnnotation", arrData[44]));
            params.add(new BasicNameValuePair("sToSCDCagency", arrData[10]));//sToSCDCagency
            params.add(new BasicNameValuePair("sToSCDCcenter", arrData[11]));//SelectedCenterID

            String resultServer = ConnectServer.getJsonPostGet(params,
                    "saveDataCaseScene");
            /*** Default Value ***/
            String strStatusID = "0";
            String strError = "Unknow Status!";
            JSONObject c;

            try {
                c = new JSONObject(resultServer.replace("\uFEFF", ""));
                strStatusID = c.getString("StatusID");
                strError = c.getString("Error");
            } catch (JSONException e) {
                // TODO: handle exception
                e.printStackTrace();
            }

            // Prepare Save Data
            if (strStatusID.equals("0")) {
                Log.i("saveCaseScene ToServer", strError);

            } else {
                Log.i("saveCaseScene ToServer", strError + " " + arrData[26]);

            }
        }
        return true;
    }

    public boolean updateToFullReport(String sReportID) {
        long status = mDbHelper.updateReportStatus(sReportID, "investigated");
        if (status > 0) {
            Log.i("updateReportStatus", "OK");
        } else {

            Log.i("updateReportStatus", "error");
        }
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sCaseReportID", sReportID));
        params.add(new BasicNameValuePair("sReportStatus", "investigated"));

        String resultServer = ConnectServer.getJsonPostGet(params,
                "updateToFullReport");
        /*** Default Value ***/
        String strStatusID = "0";
        String strError = "Unknow Status!";
        JSONObject c;

        try {
            c = new JSONObject(resultServer.replace("\uFEFF", ""));
            strStatusID = c.getString("StatusID");
            strError = c.getString("Error");
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        // Prepare Save Data
        if (strStatusID.equals("0")) {
            Log.i("updateToFullReport", strError);

        } else {
            Log.i("updateToFullReport", strError + " " + sReportID);

        }

        return true;
    }

    public boolean saveSufferer(String sReportID) {
        String arrData[][] = mDbHelper.SelectDataSufferer(sReportID);
        //Log.i("data length",String.valueOf(arrData.length));

        if (arrData != null) {
            for (int i = 0; i < arrData.length; i++) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("sCaseReportID", arrData[i][0]));
                params.add(new BasicNameValuePair("sSuffererID", arrData[i][3]));
                params.add(new BasicNameValuePair("sSSN", arrData[i][4]));
                params.add(new BasicNameValuePair("sSuffererPrename", arrData[i][2]));
                params.add(new BasicNameValuePair("sSuffererFirstName", arrData[i][5]));
                params.add(new BasicNameValuePair("sSuffererLastName", arrData[i][6]));
                params.add(new BasicNameValuePair("sSuffererAge", arrData[i][7]));
                params.add(new BasicNameValuePair("sSuffererTelMobile", arrData[i][1]));
                params.add(new BasicNameValuePair("sSuffererTelephone", arrData[i][8]));
                params.add(new BasicNameValuePair("sSuffererStatus", arrData[i][9]));

                String resultServer = ConnectServer.getJsonPostGet(params,
                        "saveSufferer");
                /*** Default Value ***/
                String strStatusID = "0";
                String strError = "Unknow Status!";
                JSONObject c;

                try {
                    c = new JSONObject(resultServer.replace("\uFEFF", ""));
                    strStatusID = c.getString("StatusID");
                    strError = c.getString("Error");
                } catch (JSONException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

                // Prepare Save Data
                if (strStatusID.equals("0")) {
                    Log.i("saveSuffererToServer", strError);

                } else {
                    Log.i("saveSuffererToServer", strError);

                }
            }
        } else {
            Log.i("saveSuffererToServer", "no have");

        }
        return true;
    }

    public boolean saveReceivingCase(String sReportID) {
        // TODO Auto-generated method stub
        final String dateTimeCurrent[] = getDateTime.getDateTimeNow();

        final String sEmergencyDate = dateTimeCurrent[0];
        final String sEmergencyTime = dateTimeCurrent[1];

        String arrDataInqID[] = mDbHelper.searchInquiryOfficialID(sReportID);
        if (arrDataInqID != null) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("sCaseReportID", sReportID));
            params.add(new BasicNameValuePair("sInquiryOfficialID",
                    arrDataInqID[1]));
            params.add(new BasicNameValuePair("sReceivingStatus",
                    arrDataInqID[2]));
            params.add(new BasicNameValuePair("sEmergencyDate",
                    sEmergencyDate));
            params.add(new BasicNameValuePair("sEmergencyTime",
                    sEmergencyTime));
            String resultServer = ConnectServer.getJsonPostGet(params,
                    "saveReceivingCase");
            /*** Default Value ***/
            String strStatusID = "0";
            String strError = "Unknow Status!";
            JSONObject c;

            try {
                c = new JSONObject(resultServer.replace("\uFEFF", ""));
                strStatusID = c.getString("StatusID");
                strError = c.getString("Error");
            } catch (JSONException e) {
                // TODO: handle exception
                e.printStackTrace();
            }

            // Prepare Save Data
            if (strStatusID.equals("0")) {
                Log.i("saveInquiry ToServer", strError);

            } else {
                Log.i("saveInquiry ToServer", strError + " "
                        + arrDataInqID[0]);

            }

        } else {
            Log.i("saveInquiry ToServer", "no have");

        }
        return true;
    }

    public boolean saveOtherOfficialInCase(String sReportID) {
        // TODO Auto-generated method stub

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sCaseReportID", sReportID));
        String resultServer = ConnectServer.getJsonPostGet(params,
                "deleteOtherOfficialInCase");
        /*** Default Value ***/
        String strStatusID = "0";
        String strError = "Unknow Status!";
        JSONObject c;

        try {
            c = new JSONObject(resultServer.replace("\uFEFF", ""));
            strStatusID = c.getString("StatusID");
            strError = c.getString("Error");
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        // Prepare Save Data
        if (strStatusID.equals("0")) {
            Log.i("deleteOtherOfficial", strError);

        } else {
            Log.i("deleteOtherOfficial", strError);

            String arrDataOtherOff[][];
            arrDataOtherOff = mDbHelper.SelectOtherOfficialInCase(sReportID);
            //Log.i("data length",String.valueOf(arrDataInspector.length));
            if (arrDataOtherOff != null) {
                for (int i = 0; i < arrDataOtherOff.length; i++) {
                    List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                    params2.add(new BasicNameValuePair("sCaseReportID",
                            arrDataOtherOff[i][0].toString()));
                    params2.add(new BasicNameValuePair("sScheduleID",
                            arrDataOtherOff[i][1].toString()));
                    String resultServer2 = ConnectServer.getJsonPostGet(
                            params2, "saveOtherOffInCase");
                    /*** Default Value ***/
                    String strStatusID2 = "0";
                    String strError2 = "Unknow Status!";
                    JSONObject c2;

                    try {
                        c2 = new JSONObject(resultServer2.replace("\uFEFF", ""));
                        strStatusID2 = c2.getString("StatusID");
                        strError2 = c2.getString("Error");
                    } catch (JSONException e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }

                    // Prepare Save Data
                    if (strStatusID2.equals("0")) {
                        Log.i("saveOtherOfficialInCase", strError2);

                    } else {
                        Log.i("saveOtherOfficialInCase", strError2 + " "
                                + arrDataOtherOff[i][0].toString());

                    }
                }
            } else {
                Log.i("saveOtherOfficialInCase", "no have");

            }
        }
        return true;
    }

    public boolean downloadDocFile(String sReportID) {
        // TODO Auto-generated method stub
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sCaseReportID", sReportID));
        String resultServer = ConnectServer.getJsonPostGet(params,
                "downloadDocFile");
        /*** Default Value ***/
        String strStatusID = "0";
        String strError = "Unknow Status!";
        JSONObject c;

        try {
            c = new JSONObject(resultServer.replace("\uFEFF", ""));
            strStatusID = c.getString("StatusID");
            strError = c.getString("Error");
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        // Prepare Save Data
        if (strStatusID.equals("0")) {
            Log.i("downloadDocFile", strError);
            return false;
        } else {
            Log.i("downloadDocFile", strError);
            new DownloadFile().execute(strError);

            return true;
        }
    }

    class DownloadFile extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar

        }

        @Override
        protected String doInBackground(String... params) {
            int count;
            String root = "";
            File myDir;
            String fileoutput = "";
            try {
                root = Environment.getExternalStorageDirectory().toString();
                myDir = new File(root + "/CSIFiles/Documents/");
                myDir.mkdirs();
                ConnectServer.updateIP();
                String filepath = ConnectServer.urlWebIP + "csi_files/Documents/" + officialID + "/" + params[0] + ".doc";
                URL url = new URL(filepath);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();
                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

                InputStream input = new BufferedInputStream(url.openStream());

                // Get File Name from URL
                fileoutput = Environment.getExternalStorageDirectory() + "/CSIFiles/Documents/" + params[0] + ".doc";
                Log.i("fileoutput", filepath + ":" + fileoutput);
                OutputStream output = new FileOutputStream(fileoutput);
                //OutputStream output = new FileOutputStream("/sdcard/Download/"+fileName+".doc");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    //publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return fileoutput;
        }

        protected void onPostExecute(String arrData) {
            Toast.makeText(getActivity(),
                    "ดาวน์โหลดเรียบร้อยแล้ว",
                    Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(),
                    "ไฟล์อยู่ใน " + arrData,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private class saveEmergency extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            long updateEmergency = mDbHelper.updateEmergency(params[0], params[1], params[2]);
            if (updateEmergency > 0) {
                Log.i("saveEmergency", "save ok");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}