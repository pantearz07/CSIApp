package com.scdc.csiapp.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pantearz07 on 23/9/2558.
 */
public class ReceivingCaseListFragment extends Fragment {
    CoordinatorLayout rootLayoutDraft;
    FloatingActionButton fabBtnDraft;
    RecyclerView rvReceive;
    CardView cvReceive;
    List<ReceivingCaseList> receivigCaseLists;
    private ReceivingCaseAdapter receivigCaseListAdapter;
    Context context;
    // connect sqlite
    SQLiteDatabase mDb;
    SQLiteDBHelper mDbHelper;
    FragmentManager mFragmentManager;

    private PreferenceData mManager;
    ConnectionDetector cd;
    Boolean networkConnectivity = false;
    long isConnectingToInternet = 0;
    Cursor mCursor;
    String officialID;
    CSIDataTabFragment fCSIDataTabFragment;
    GetDateTime getDateTime;
    private String CurrentDate_ID;
    TextView txtUpdateDate;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View viewdraft = inflater.inflate(R.layout.receivigcase_layout, null);
        // Context context = getContext();
        Log.i("log_show draft", "onCreateView!! ");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.receivingcase);
        rootLayoutDraft = (CoordinatorLayout) viewdraft.findViewById(R.id.rootLayoutDraft);
        mDbHelper = new SQLiteDBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();
        mManager = new PreferenceData(getActivity());
        context = viewdraft.getContext();
        mFragmentManager = getActivity().getSupportFragmentManager();
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        cd = new ConnectionDetector(getActivity());
        networkConnectivity = cd.networkConnectivity();
        isConnectingToInternet = cd.isConnectingToInternet();
        getDateTime = new GetDateTime();

        fCSIDataTabFragment = new CSIDataTabFragment();
        txtUpdateDate = (TextView) viewdraft
                .findViewById(R.id.txtUpdateDate);
        String updatedate = mManager.getPreferenceData(
                mManager.ARG_UPDATE_DATA_INVESTIGATOR);
        if (updatedate.length() != 0) {
            txtUpdateDate.setText(updatedate);
        } else {
            txtUpdateDate.setText("อัพเดทข้อมูลล่าสุดเมื่อ : ");
        }
        receivigCaseLists = new ArrayList<>();
        //cvDraft =(CardView) viewdraft.findViewById((R.))
        rvReceive = (RecyclerView) viewdraft.findViewById(R.id.rvDraft);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvReceive.setLayoutManager(llm);
        rvReceive.setHasFixedSize(true);
        // new getReceivingReportFromServer().execute();
        initializeData();
        //receivigCaseListAdapter.setOnItemClickListener(onItemClickListener);
        //csiDataListAdapter.setTouchListener(onItemTouchListener);

        fabBtnDraft = (FloatingActionButton) viewdraft.findViewById(R.id.fabBtnDraft);
        fabBtnDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                if (networkConnectivity) {
                    initializeData();
                    Snackbar.make(v, "ดาวน์โหลดข้อมูลเรียบร้อยแล้ว", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {

                    Snackbar.make(v, "กรุณาเชื่อมต่ออินเทอร์เน็ต", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }*/

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();

                View view1 = inflater.inflate(R.layout.dialog_add_case, null);
                dialog.setView(view1);

                final EditText edtReportNo = (EditText) view1.findViewById(R.id.edtReportNo);
                final Spinner spnCaseType = (Spinner) view1.findViewById(R.id.spnCaseType);
                final String[] CaseType = getResources().getStringArray(R.array.casetype);
                ArrayAdapter<String> adapterCaseType = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, CaseType);
                spnCaseType.setAdapter(adapterCaseType);
                final String[] selectedCaseType = new String[1];
                spnCaseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedCaseType[0] = CaseType[position];
                        Log.i("selectedCaseType", "Select : " + CaseType[position] + " " + selectedCaseType[0]);


                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                final Spinner spnSubCaseType = (Spinner) view1.findViewById(R.id.spnSubCaseType);
                //final String mSubCaseTypeArray[][] = mDbHelper.SelectSubCaseType();
                final String[] SubCaseType = getResources().getStringArray(R.array.subcasetypeproperties);
                ArrayAdapter<String> adapterSubCaseType = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, SubCaseType);
                spnSubCaseType.setAdapter(adapterSubCaseType);
                final String[] selectedSubCaseType = new String[1];
                spnSubCaseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedSubCaseType[0] = SubCaseType[position];
                        Log.i("spnSubCaseType", "Select : " + SubCaseType[position] + " " + selectedSubCaseType[0]);

                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                final TextView edtYear = (TextView) view1.findViewById(R.id.edtYear);
                dialog.setTitle("เพิ่มข้อมูลการตรวจสถานที่เกิดเหตุ");
                dialog.setIcon(R.drawable.ic_noti);
                dialog.setCancelable(true);
// Current Date
                final String dateTimeCurrent[] = getDateTime.getDateTimeCurrent();
                final String reportYear = dateTimeCurrent[0];
                final String saveDataTime = dateTimeCurrent[2] + dateTimeCurrent[1] + dateTimeCurrent[0]+"_"+dateTimeCurrent[3] + dateTimeCurrent[4] + dateTimeCurrent[5];
//RC_07042016_034747
                edtYear.setText(" / " + reportYear);
                dialog.setPositiveButton("ถัดไป", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String reportNo = null;
                        if (edtReportNo.getText().toString().equals("")) {
                            reportNo = "";
                        } else {
                            reportNo = edtReportNo.getText().toString();
                        }

                        String reportID = "RC_" + saveDataTime;

                        // save new Report
                        long saveStatus1 = mDbHelper.saveReportID(
                                reportID, reportNo, officialID, reportYear, selectedCaseType[0], selectedSubCaseType[0], "investigating");
                        Log.i("save report", selectedCaseType[0] + " " + selectedSubCaseType[0]);
                        if (saveStatus1 <= 0) {
                            Log.i("save report", "Error!! ");
                        } else {
                            Log.i("save report", reportID + " " + reportNo
                                    + " " + officialID + " " + reportYear);
                        }
                        //save preference reportID
                        mManager.setPreferenceData(mManager.PREF_REPORTID, reportID);


                        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.containerView, fCSIDataTabFragment).addToBackStack(null).commit();


                    }

                });

                dialog.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
                dialog.create();
                dialog.show();
            }
        });

        return viewdraft;
    }

    ReceivingCaseAdapter.OnItemClickListener onItemClickListener = new ReceivingCaseAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(View view, int position) {
            final ReceivingCaseList receivigCasedata = receivigCaseLists.get(position);
            final String caseReportID = receivigCasedata.caseReportID;
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());
           builder.setPositiveButton("รับคดี", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                   // Toast.makeText(getActivity(), "เลือกรับคดี " + caseReportID, Toast.LENGTH_SHORT).show();
                    acceptCase(caseReportID);
                     mManager.setPreferenceData(mManager.PREF_REPORTID, caseReportID);

                      FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView, fCSIDataTabFragment).addToBackStack(null).commit();
                }
            });
            /* builder.setNeutralButton("รับคดี", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //Log.i("log_show draft 1", " delete " + caserepTD);

                    acceptCase(caseReportID);
                    mManager.setPreferenceData(mManager.PREF_REPORTID, caseReportID);
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView, fCSIDataTabFragment).addToBackStack(null).commit();

                }
            });
            */
            builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create();
            builder.show();

            //Snackbar.make(view, "Clicked " + csidata.caseReportID, Snackbar.LENGTH_LONG)
            //       .setAction("Action", null).show();


        }
    };

    private void acceptCase(String caseReportID) {
        new saveReceivingReportFromServer().execute(caseReportID);
        Toast.makeText(getActivity(), "เลือกรับตรวจคดี " + caseReportID, Toast.LENGTH_SHORT).show();

    }


    private void initializeData() {
        new getReceivingReportFromServer().execute();
       //showdata();
        Toast.makeText(getActivity(), "กำลังดาวน์โหลดข้อมูล" , Toast.LENGTH_SHORT).show();

    }

    private void showdata() {
        receivigCaseListAdapter = new ReceivingCaseAdapter(receivigCaseLists);
        rvReceive.setAdapter(receivigCaseListAdapter);
        String updateDate = updateDateTime();
        txtUpdateDate.setText(updateDate);

        receivigCaseListAdapter.setOnItemClickListener(onItemClickListener);


    }

    class getReceivingReportFromServer extends AsyncTask<String, Void, String> {
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
            List<NameValuePair> params1 = new ArrayList<NameValuePair>();
            params1.add(new BasicNameValuePair("sOfficialID", officialID));

            String resultServer = ConnectServer.getJsonPostGet(params1, "getReceivingReport");

            Log.i("getReceivingReport", officialID + resultServer);
            return resultServer;
        }

        protected void onPostExecute(String resultServer) {
            String caseReportID = "";
            String policeStation = "";
            String typeCase = "";
            String dateReceiving = "";
            String timeReceiving = "";
            //  String sInquiryOfficialID = "";
            String sRank = "";
            String sFirstName = "";
            String sLastName = "";
 /*Initialize array if null*/
            // if (null == receivigCaseLists)
            // receivigCaseLists = new ArrayList<>();

            try {
                final JSONArray data = new JSONArray(resultServer);
                for (int i = 0; i < data.length(); i++) {
                    JSONObject c = data.getJSONObject(i);
                    caseReportID = c.getString("CaseReportID");
                    policeStation = c.getString("PoliceStation");
                    typeCase = c.getString("SubCaseTypeName");
                    dateReceiving = c.getString("ReceivingCaseDate");
                    timeReceiving = c.getString("ReceivingCaseTime");
                    // sInquiryOfficialID = c.getString("InquiryOfficialID");
                    sRank = c.getString("Rank");
                    sFirstName = c.getString("FirstName");
                    sLastName = c.getString("LastName");
                    //ReceivingCaseList item = new ReceivingCaseList();
/*
                    receivigCaseLists.add(new ReceivingCaseList(caseReportID, sRank, sFirstName,
                            sLastName, policeStation, typeCase, dateReceiving, timeReceiving
                    ));*/
                    Log.i("log_receiving", String.valueOf(i) + caseReportID);


                }

            } catch (JSONException e) {
                // TODO: handle exception
                e.printStackTrace();
                Log.e("log_tag", "Error parsing data " + e.toString());

            }
            if (dialog.isShowing()) {
                dialog.dismiss();
                showdata();

            }
        }
    }

    class saveReceivingReportFromServer extends AsyncTask<String, Void, String> {
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
            if (saveCaseReport(params[0]) == 1) {
                Log.i("saveCaseReport", "OK");

                 if (saveSufferer(params[0]) == 1) {
                    Log.i("saveSuffer", "OK");
                     if (saveSchedule(params[0]) == 1) {
                         Log.i("saveSuffer", "OK");

                     }
                }
            }else {
                Log.i("saveSuffer", "error");
            }
            return null;
        }

        protected void onPostExecute(String resultServer) {

            if (dialog.isShowing()) {
                dialog.dismiss();


            }


        }
    }

    public long saveCaseReport(String sCaseReportID) {
        List<NameValuePair> params1 = new ArrayList<NameValuePair>();
        params1.add(new BasicNameValuePair("sOfficialID", officialID));
        params1.add(new BasicNameValuePair("sCaseReportID", sCaseReportID));

        String resultServer = ConnectServer.getJsonPostGet(params1, "getUpdateReceivingReport");

        Log.i("ReceivingReport", resultServer);
        String sInvestigatorOfficialID,sInquiryOfficialID,
                sYear, sReportStatus,
                sCaseType, sSubCaseType, sNotifiedBy, sCaseTel,
                sPoliceStation, sPoliceProvince, sLocaleName,
                sHouseNo, sVillageNo, sVillageName,
                sLaneName, sRoadName, sDistrict,
                sAmphur, sProvince, sPostalCode,
                sLatitude, sLongitude,
                sReceivingCaseDate, sReceivingCaseTime,
                sHappenCaseDate, sHappenCaseTime,
                sKnowCaseDate, sKnowCaseTime,
                sCircumstanceOfCaseDetail,
                sToSCDCagency, sToSCDCcenter,
                sLastDateUpdateData, sLastTimeUpdateData,sRecievingStatus, sEmergencyDate,sEmergencyTime = "";
        String strStatusID = "0";
        String strError = "Unknow Status!";

        try {
            final JSONArray data = new JSONArray(resultServer);
            for (int i = 0; i < data.length(); i++) {

                JSONObject c = data.getJSONObject(i);
                strStatusID = c.getString("StatusID");
                strError = c.getString("Error");
                if (strStatusID.equals("0")) {
                    Log.i("saveCaseScene ToServer", strError);

                } else {
                    Log.i("saveCaseScene ToServer", strError);

                    sInquiryOfficialID = c.getString("InquiryOfficialID");
                    sInvestigatorOfficialID = c.getString("InvestigatorOfficialID");
                    sYear = c.getString("Year");
                    sReportStatus = c.getString("ReportStatus");
                    sCaseType = c.getString("CaseTypeName");
                    sSubCaseType = c.getString("SubCaseTypeName");
                    sNotifiedBy = c.getString("NotifiedBy");
                    sCaseTel = c.getString("CaseTel");
                    sPoliceStation = c.getString("PoliceStation");
                    sPoliceProvince = c.getString("PoliceProvince");
                    sLocaleName = c.getString("LocaleName");
                    sHouseNo = c.getString("HouseNo");
                    sVillageNo = c.getString("VillageNo");
                    sVillageName = c.getString("VillageName");
                    sLaneName = c.getString("LaneName");
                    sRoadName = c.getString("RoadName");
                    sDistrict = c.getString("District");
                    sAmphur = c.getString("Amphur");
                    sProvince = c.getString("Province");
                    sPostalCode = c.getString("PostalCode");
                    sLatitude = c.getString("Latitude");
                    sLongitude = c.getString("Longitude");
                    sReceivingCaseDate = c.getString("ReceivingCaseDate");
                    sReceivingCaseTime = c.getString("ReceivingCaseTime");
                    sHappenCaseDate = c.getString("HappenCaseDate");
                    sHappenCaseTime = c.getString("HappenCaseTime");
                    sKnowCaseDate = c.getString("KnowCaseDate");
                    sKnowCaseTime = c.getString("KnowCaseTime");
                    sCircumstanceOfCaseDetail = c.getString("CircumstanceOfCaseDetail");
                    sToSCDCagency = c.getString("ToSCDCagency");
                    sToSCDCcenter = c.getString("ToSCDCcenter");
                    sLastDateUpdateData = c.getString("LastDateUpdateData");
                    sLastTimeUpdateData = c.getString("LastTimeUpdateData");
                    sRecievingStatus= c.getString("RecievingStatus");
                    sEmergencyDate = c.getString("EmergencyDate");
                    sEmergencyTime= c.getString("EmergencyTime");

                    Log.i("log_receivings", sCaseReportID);

                    long saveStatus = mDbHelper.SaveCaseReport(sCaseReportID, sInvestigatorOfficialID,
                            sYear, sReportStatus, sCaseType,
                            sSubCaseType, sNotifiedBy, sCaseTel,
                            sPoliceStation, sPoliceProvince, sLocaleName,
                            sHouseNo, sVillageNo, sVillageName,
                            sLaneName, sRoadName, sDistrict,
                            sAmphur, sProvince, sPostalCode,
                            sLatitude, sLongitude,
                            sReceivingCaseDate, sReceivingCaseTime,
                            sHappenCaseDate, sHappenCaseTime,
                            sKnowCaseDate, sKnowCaseTime,
                            sCircumstanceOfCaseDetail, sToSCDCagency, sToSCDCcenter,
                            sLastDateUpdateData, sLastTimeUpdateData);

                    if (saveStatus == -1) {
                        Log.i("Recieving", "Error!! ");
                        return -1;
                    } else{

                        Log.i("Recieving", "ok");

                        long SaveReceivingCase = mDbHelper.SaveReceivingCase(sCaseReportID, sInquiryOfficialID,sRecievingStatus,sEmergencyDate,sEmergencyTime);
                        if (SaveReceivingCase == -1) {
                            Log.i("SaveReceivingCase", "Error!! ");
                            return -1;
                        } else {
                            Log.i("SaveReceivingCase",  "ok"+sInquiryOfficialID);
                            return 1;
                        }

                    }
                }
            }
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e("log_tag", "Error parsing data " + e.toString());
            return -1;
        }


        return 0;
    }

    public long saveSufferer(String sCaseReportID) {
        List<NameValuePair> params1 = new ArrayList<NameValuePair>();
        params1.add(new BasicNameValuePair("sCaseReportID", sCaseReportID));

        String resultServer = ConnectServer.getJsonPostGet(params1, "getSufferer");

        Log.i("saveSufferer", resultServer);
        String sSuffererID,  sSSN,
                  sSuffererPrename,   sSuffererFirstName,
                  sSuffererLastName,   sSuffererAge,
                  sSuffererTelephone,   sSuffererTelMobile,
                  sSuffererStatus ="";

        try {
            final JSONArray data = new JSONArray(resultServer);
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                sSuffererID = c.getString("SuffererID");
                sSSN = c.getString("SSN");
                sSuffererPrename = c.getString("SuffererPrename");
                sSuffererFirstName = c.getString("SuffererFirstName");
                sSuffererLastName = c.getString("SuffererLastName");
                sSuffererAge = c.getString("SuffererAge");
                sSuffererTelephone = c.getString("SuffererTelephone");
                sSuffererTelMobile = c.getString("SuffererTelMobile");
                sSuffererStatus = c.getString("SuffererStatus");



                Log.i("saveDataSufferer", String.valueOf(i) + sCaseReportID);

                long saveStatus = mDbHelper.saveDataSufferer(sCaseReportID, sSuffererID, sSSN,
                        sSuffererPrename, sSuffererFirstName,
                        sSuffererLastName, sSuffererAge,
                        sSuffererTelephone, sSuffererTelMobile,
                        sSuffererStatus);

                if (saveStatus == -1) {
                    Log.i("saveDataSufferer", "Error!! ");
                    return -1;
                } else{

                    Log.i("saveDataSufferer", "ok");
                    return 1;
                }

            }
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e("log_tag", "Error parsing data " + e.toString());
            return -1;
        }


        return 0;
    }
    public long saveSchedule(String sCaseReportID) {
        List<NameValuePair> params1 = new ArrayList<NameValuePair>();
        params1.add(new BasicNameValuePair("sCaseReportID", sCaseReportID));

        String resultServer = ConnectServer.getJsonPostGet(params1, "getScheduleCase");

        Log.i("getScheduleCase", resultServer);
        String sScheduleID ="";

        try {
            final JSONArray data = new JSONArray(resultServer);
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                sScheduleID = c.getString("ScheduleID");



                Log.i("ScheduleCase", String.valueOf(i) + sCaseReportID);

                long saveStatus = mDbHelper.saveScheduleCase(sCaseReportID, sScheduleID);

                if (saveStatus == -1) {
                    Log.i("saveScheduleCase", "Error!! ");
                    return -1;
                } else{

                    Log.i("saveScheduleCase", "ok");
                    return 1;
                }

            }
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e("log_tag", "Error parsing data " + e.toString());
            return -1;
        }


        return 0;
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.i("log_show draft", " onResume ");
        //csiDataListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("log_show receiving", " onStart ");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("log_show draft", " onDestroyView ");

    }

    public String updateDateTime() {
        //update date
        String sDate = "";
        String sTime = "";
        String UpdateDataDate[] = getDateTime.updateDataDateTime();
        if (UpdateDataDate != null) {
            sDate = UpdateDataDate[0];
            sTime = UpdateDataDate[1];
        }
        CurrentDate_ID = "อัพเดทข้อมูลล่าสุดเมื่อ : " + sDate + " เวลา " + sTime + " น.";
        mManager.setPreferenceData(mManager.ARG_UPDATE_DATA_INSPECTOR,
                CurrentDate_ID);

        return CurrentDate_ID;
    }
}