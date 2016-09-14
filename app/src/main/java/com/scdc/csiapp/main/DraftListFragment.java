package com.scdc.csiapp.main;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class DraftListFragment extends Fragment {
    CoordinatorLayout rootLayoutDraft;
    RecyclerView rvDraft;
    CardView cvDraft;
    private List<CSIDataList> csiDataLists;
    private CSIDataListAdapter csiDataListAdapter;
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
    SwipeRefreshLayout swipeContainer;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View viewdraft = inflater.inflate(R.layout.draft_layout, null);
        Context context = getContext();
        Log.i("log_show draft", "onCreateView!! ");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.draftcsidata);
        rootLayoutDraft = (CoordinatorLayout) viewdraft.findViewById(R.id.rootLayoutDraft);
        mDbHelper = new SQLiteDBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();
        mManager = new PreferenceData(getActivity());
        context = viewdraft.getContext();
        mFragmentManager = getActivity().getSupportFragmentManager();
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        Log.i("officialID", officialID);
        cd = new ConnectionDetector(getActivity());
        networkConnectivity = cd.isNetworkAvailable();
        isConnectingToInternet = cd.isConnectingToInternet();
        getDateTime = new GetDateTime();
        txtUpdateDate = (TextView) viewdraft
                .findViewById(R.id.txtUpdateDate);
        String updatedate = mManager.getPreferenceData(
                mManager.ARG_UPDATE_DATA_DRAFTCASE);
        if (updatedate.length() != 0) {
            txtUpdateDate.setText(updatedate);
        } else {
            txtUpdateDate.setText("อัพเดทข้อมูลล่าสุดเมื่อ : ");
        }

        fCSIDataTabFragment = new CSIDataTabFragment();


        csiDataLists = new ArrayList<>();
        rvDraft = (RecyclerView) viewdraft.findViewById(R.id.rvDraft);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rvDraft.setLayoutManager(llm);
        rvDraft.setHasFixedSize(true);
        swipeContainer = (SwipeRefreshLayout) viewdraft.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {
                if (networkConnectivity) {
                    Log.i("log_show draft", "Refreshing!! ");

                    swipeContainer.setRefreshing(true);
                    loadFromServer();

                } else {
                    swipeContainer.setRefreshing(true);
                    initializeData();

                    Log.i("log_show draft", "fail network");
                    Snackbar.make(viewdraft, "กรุณาเชื่อมต่ออินเทอร์เน็ต", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }

        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                Log.i("log_show draft", "Runnable");

                if (networkConnectivity) {
                    Log.i("log_show draft", "Refreshing!! ");

                    swipeContainer.setRefreshing(true);
                    loadFromServer();

                } else {
                    swipeContainer.setRefreshing(true);
                    initializeData();

                    Log.i("log_show draft", "fail network");
                    Snackbar.make(viewdraft, "กรุณาเชื่อมต่ออินเทอร์เน็ต", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
        csiDataListAdapter = new CSIDataListAdapter(csiDataLists);
        rvDraft.setAdapter(csiDataListAdapter);
        csiDataListAdapter.setOnItemClickListener(onItemClickListener);

        return viewdraft;
    }

    CSIDataListAdapter.OnItemClickListener onItemClickListener = new CSIDataListAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(View view, int position) {
            final CSIDataList csidata = csiDataLists.get(position);
            final String caserepTD = csidata.caseReportID;

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());
            builder.setMessage("ดูข้อมูลการตรวจนี้ " + caserepTD);

            builder.setPositiveButton("ดู", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getActivity(), "Clicked " + caserepTD, Toast.LENGTH_SHORT).show();

                    mManager.setPreferenceData(mManager.PREF_REPORTID, caserepTD);

                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView, fCSIDataTabFragment).addToBackStack(null).commit();
                }
            });
            builder.setNeutralButton("ลบ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mDbHelper.DeleteReport1(caserepTD,
                            mDbHelper.TABLE_CASESCENE.toString());
                    mDbHelper.DeleteReport1(caserepTD,
                            mDbHelper.TABLE_FEATUREOUT.toString());
                    mDbHelper.DeleteReport1(caserepTD,
                            mDbHelper.TABLE_FEATUREIN.toString());
                    mDbHelper.DeleteReport1(caserepTD,
                            mDbHelper.TABLE_receivingcase.toString());
                    mDbHelper.DeleteReport1(caserepTD,
                            mDbHelper.TABLE_otherofficialinscene.toString());
                    mDbHelper.DeleteReport1(caserepTD,
                            mDbHelper.TABLE_SUFFERER.toString());
                    mDbHelper.DeleteReport1(caserepTD,
                            mDbHelper.TABLE_PROPERTYLOSS.toString());
                    mDbHelper.DeleteReport1(caserepTD,
                            mDbHelper.TABLE_FindEevidence.toString());
                    mDbHelper.DeleteReport1(caserepTD,
                            mDbHelper.TABLE_resultscene.toString());
                    mDbHelper.DeleteReport1(caserepTD,
                            mDbHelper.TABLE_multimediafile.toString());
                    Toast.makeText(getActivity(), "ลบ " + caserepTD, Toast.LENGTH_SHORT).show();
                    Log.i("log_show draft 1", " delete " + caserepTD);

                    initializeData();
                    // swipeContainer.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) getActivity());
                }
            });

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

    private void loadFromServer() {
        Log.i("log_show draft", "Loading data From Server");

        Toast.makeText(getActivity(), "กำลังดาวน์โหลดข้อมูล", Toast.LENGTH_SHORT).show();

        new getDraftReportFromServer().execute();
//getDraftReport.php

    }

    private void showdata() {
        if (swipeContainer.isRefreshing()) {
            swipeContainer.setRefreshing(false);
        }
        csiDataListAdapter = new CSIDataListAdapter(csiDataLists);
        rvDraft.setAdapter(csiDataListAdapter);
        String updateDate = updateDateTime();
        txtUpdateDate.setText(updateDate);

        csiDataListAdapter.setOnItemClickListener(onItemClickListener);
    }

    private void initializeData() {


        csiDataLists.clear();
        String[][] investigatingCase = null;
        investigatingCase = mDbHelper.investigatingCase(officialID);
        if (investigatingCase == null) {
            Log.i("log_show draft", " investigatingCase null ");
        } else {

            for (int i = 0; i < investigatingCase.length; i++) {
                csiDataLists.add(new CSIDataList(investigatingCase[i][25], investigatingCase[i][26],
                        investigatingCase[i][6], investigatingCase[i][32],
                        investigatingCase[i][33], investigatingCase[i][34],
                        investigatingCase[i][35], investigatingCase[i][36],
                        investigatingCase[i][37], investigatingCase[i][13], investigatingCase[i][38],
                        investigatingCase[i][22], getDateTime.changeDateFormatToCalendar(investigatingCase[i][21]),
                        investigatingCase[i][20], investigatingCase[i][47]));
            }

        }
        csiDataListAdapter.notifyDataSetChanged();
        showdata();
        /*
        mCursor.moveToFirst();
        while (!mCursor.isAfterLast()) {
            csiDataLists.add(new CSIDataList(mCursor.getString(mCursor
                    .getColumnIndex(SQLiteDBHelper.COL_CaseReportID)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_SubCaseTypeName)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_LocaleName)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_HouseNo)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_VillageNo)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_VillageName)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_LaneName)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_RoadName)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_District)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_Amphur)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_Province)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_PoliceStation)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_ReceivingCaseDate)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_ReceivingCaseTime)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_InquiryOfficialID))
            ));

            mCursor.moveToNext();
        }
        mCursor.close(); mDb.close();

        */

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("log_show draft", " onResume ");
        //csiDataListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("log_show draft", " onDestroyView ");

    }

    @Override
    public void onStart() {
        super.onStart();
        new getReceivingReport().execute(officialID);

    }

    class getReceivingReport extends AsyncTask<String, Void, String[][]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String[][] doInBackground(String... params) {
            String[][] arrData = null;
            arrData = mDbHelper.SelectCaseSceneReport(params[0]);


            return arrData;


        }

        @Override
        protected void onPostExecute(String s[][]) {

            if (s != null) {
                for (int i = 0; i < s.length; i++) {
                    Log.i("getReceivingReport", s[i][25] + " " + s[i][28] + " " +
                            s[i][29] + " " + s[i][30] + " " + s[i][20] + " " + s[i][27] + s[i][24]);
                }


            } else {
                Log.i("getReceivingReport", "Null!! getReceivingReport ");
            }
        }
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

    class getDraftReportFromServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String status = "";
            List<NameValuePair> params1 = new ArrayList<NameValuePair>();
            params1.add(new BasicNameValuePair("sOfficialID", officialID));

            String resultServer = ConnectServer.getJsonPostGet(params1, "getDraftReport");
            Log.i("getDraftReport", officialID + resultServer);
            if(resultServer=="error"){
                Log.i("getDraftReport", officialID + resultServer);
            }
            String sCaseReportID, sInvestigatorOfficialID, sInquiryOfficialID,
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
                    sLastDateUpdateData, sLastTimeUpdateData, sReceivingStatus, sEmergencyDate, sEmergencyTime = "";
            try {
                final JSONArray data = new JSONArray(resultServer.replace("\uFEFF", ""));
                if (data.length() > 0) {


                    for (int i = 0; i < data.length(); i++) {

                        JSONObject c = data.getJSONObject(i);
                        sCaseReportID = c.getString("CaseReportID");
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
                        sReceivingStatus = c.getString("ReceivingStatus");
                        sEmergencyDate = c.getString("EmergencyDate");
                        sEmergencyTime = c.getString("EmergencyTime");
                        Log.i("log_show draft", String.valueOf(i) + sCaseReportID);

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
                            Log.i("log_show draft", "SaveReceivingCase Error!! ");
                            //status = "0";
                        } else if (saveStatus == -2) {
                            Log.i("log_show draft", "had SaveReceivingCase  ");
                        } else {

                            Log.i("log_show draft", String.valueOf(i) + " ok");

                            long SaveReceivingCase = mDbHelper.SaveReceivingCase(sCaseReportID, sInquiryOfficialID, sReceivingStatus, sEmergencyDate, sEmergencyTime);
                            if (SaveReceivingCase == -1) {
                                Log.i("log_show draft", "SaveReceivingCase  Error!! ");
                                // status = "0";
                            } else {
                                Log.i("log_show draft", String.valueOf(i) + " SaveReceivingCase ok" + sInquiryOfficialID);
                                if (saveSufferer(sCaseReportID) == 1) {
                                    Log.i("log_show draft", String.valueOf(i) + " saveSuffer OK");
                                    if (saveScheduleInvestigators(sCaseReportID) == 1) {
                                        Log.i("log_show draft", String.valueOf(i) + " saveSchedule OK");
                                        //   status = "1";
                                    } else {
                                        Log.i("log_show draft", "saveSchedule error");

                                        // status = "0";
                                    }
                                } else {
                                    Log.i("log_show draft", "saveSuffer error");

                                    //  status = "0";
                                }
                            }

                        }
                    }
                    status = "1";
                } else {
                    status = "0";
                }

            } catch (JSONException e) {
                // TODO: handle exception
                e.printStackTrace();
                Log.e("log_tag", "Error parsing data " + e.toString());
                status = "0";
            }


            return status;
        }

        @Override
        protected void onPostExecute(String status) {
            if (status == "1") {
                initializeData();
                Log.i("log_show draft", "initializeData 1");

            } else {
                initializeData();
                Log.i("log_show draft", "initializeData 0");

            }
        }
    }

    public long saveSufferer(String sCaseReportID) {
        List<NameValuePair> params1 = new ArrayList<NameValuePair>();
        params1.add(new BasicNameValuePair("sCaseReportID", sCaseReportID));

        String resultServer = ConnectServer.getJsonPostGet(params1, "getSufferer");

        Log.i("saveSufferer", resultServer);
        String sSuffererID, sSSN,
                sSuffererPrename, sSuffererFirstName,
                sSuffererLastName, sSuffererAge,
                sSuffererTelephone, sSuffererTelMobile,
                sSuffererStatus = "";

        try {
            final JSONArray data = new JSONArray(resultServer.replace("\uFEFF", ""));
            if (data.length() > 0) {
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


                    Log.i("saveDataSufferer", "receiving " + String.valueOf(i) + sCaseReportID+sSuffererID);

                    long saveStatus = mDbHelper.saveDataSufferer(sCaseReportID, sSuffererID, sSSN,
                            sSuffererPrename, sSuffererFirstName,
                            sSuffererLastName, sSuffererAge,
                            sSuffererTelephone, sSuffererTelMobile,
                            sSuffererStatus);

                    if (saveStatus == -1) {
                        Log.i("saveDataSufferer", "Error!! ");

                    } else {

                        Log.i("saveDataSufferer", "ok");

                    }

                }return 1;
            } else {
                return -1;
            }
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e("log_tag", "Error parsing data " + e.toString());
            return -1;
        }

    }

    public long saveScheduleInvestigators(String sCaseReportID) {
        List<NameValuePair> params1 = new ArrayList<NameValuePair>();
        params1.add(new BasicNameValuePair("sCaseReportID", sCaseReportID));

        String resultServer = ConnectServer.getJsonPostGet(params1, "getInvestigatorsInscene");

        Log.i("getInvestigatorsInscene", resultServer);
        String sInvOfficialID = "";
        String sCRID = "";
        try {
            final JSONArray data = new JSONArray(resultServer.replace("\uFEFF", ""));
            Log.i("getInvestigatorsInscene", String.valueOf(data.length()));
            if (data.length() > 0) {
                for (int i = 0; i < data.length(); i++) {
                    JSONObject c = data.getJSONObject(i);
                    sInvOfficialID = c.getString("InvOfficialID");
                    sCRID = c.getString("CaseReportID");

                    Log.i("getInvestigatorsInscene", String.valueOf(i) + sCRID + " " + sInvOfficialID);

                    long saveStatus = mDbHelper.saveInvestigatorsInscene(sCRID, sInvOfficialID);

                    if (saveStatus == -1) {
                        Log.i("getInvestigatorsInscene", "Error!! ");

                    } else {
                        Log.i("getInvestigatorsInscene" + i, sCRID + " " + sInvOfficialID);

                        Log.i("getInvestigatorsInscene", "ok receiving");

                    }

                } return 1;
            }else {
                return -1;
            }
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e("log_tag", "Error parsing data " + e.toString());
            return -1;
        }


    }
    public long saveSchedule(String sCaseReportID) {
        List<NameValuePair> params1 = new ArrayList<NameValuePair>();
        params1.add(new BasicNameValuePair("sCaseReportID", sCaseReportID));

        String resultServer = ConnectServer.getJsonPostGet(params1, "getScheduleCase");

        Log.i("getScheduleCase", resultServer);
        String sScheduleID = "";

        try {

            JSONObject c = new JSONObject(resultServer.replace("\uFEFF", ""));
            sScheduleID = c.getString("ScheduleID");

            if (sScheduleID.equals("")) {
                return 0;
            } else {

                Log.i("ScheduleCase", sCaseReportID+" "+sScheduleID);

                long saveStatus = mDbHelper.saveScheduleINCase(sCaseReportID, sScheduleID);

                if (saveStatus == -1) {
                    Log.i("saveScheduleCase", "Error!! ");
                    return -1;
                } else {

                    Log.i("saveScheduleCase", "ok receiving");
                    return 1;
                }
            }
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e("log_tag", "Error parsing data " + e.toString());
            return -1;
        }


    }

}