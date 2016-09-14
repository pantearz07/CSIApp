package com.scdc.csiapp.main;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
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
public class AcceptListFragment extends Fragment {
    CoordinatorLayout rootLayoutDraft;
    FloatingActionButton fabBtnDraft;
    RecyclerView rvDraft;
    CardView cvDraft;
    private List<CSIDataList> csiDataLists;
    private CSIDataListAdapter csiDataListAdapter;
    Context context;
    // connect sqlite
    SQLiteDatabase mDb,mRead;
    SQLiteDBHelper mDbHelper;
    FragmentManager mFragmentManager;

    private PreferenceData mManager;
    ConnectionDetector cd;
    Boolean networkConnectivity = false;
    long isConnectingToInternet = 0;
    Cursor mCursor;
    String officialID;
    EmergencyTabFragment emergencyTabFragment;
    AcceptDoneTabFragment acceptDoneTabFragment;
    GetDateTime getDateTime;
    private String CurrentDate_ID;
    TextView txtUpdateDate;
    SwipeRefreshLayout swipeContainer;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View viewdraft = inflater.inflate(R.layout.emergency_layout, null);
        Context context = getContext();
        Log.i("log_show draft", "onCreateView!! ");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.accept);
        rootLayoutDraft = (CoordinatorLayout) viewdraft.findViewById(R.id.rootLayoutDraft);
        mDbHelper = new SQLiteDBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();
        mRead = mDbHelper.getReadableDatabase();
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

        acceptDoneTabFragment = new AcceptDoneTabFragment();
        emergencyTabFragment = new EmergencyTabFragment();

        fabBtnDraft = (FloatingActionButton) viewdraft.findViewById(R.id.fabBtnDraft);
        fabBtnDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();

                View view1 = inflater.inflate(R.layout.dialog_add_case, null);
                dialog.setView(view1);

                final EditText edtReportNo = (EditText) view1.findViewById(R.id.edtReportNo);
                edtReportNo.setVisibility(View.GONE);
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
                edtYear.setVisibility(View.GONE);
                dialog.setTitle("เพิ่มข้อมูลการตรวจสถานที่เกิดเหตุ");
                dialog.setIcon(R.drawable.ic_noti);
                dialog.setCancelable(true);
// Current Date
                final String dateTimeCurrent[] = getDateTime.getDateTimeCurrent();

                final String saveDataTime = dateTimeCurrent[2] + dateTimeCurrent[1] + dateTimeCurrent[0] + "_" + dateTimeCurrent[3] + dateTimeCurrent[4] + dateTimeCurrent[5];
//RC_07042016_034747
                 final String sEmergencyDate = dateTimeCurrent[2] + "/" + dateTimeCurrent[1] + "/" + dateTimeCurrent[0];
                final String sEmergencyTime =dateTimeCurrent[3]+ ":" + dateTimeCurrent[4];
                dialog.setPositiveButton("ถัดไป", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String reportNo = "";

                        String reportID = "RC_" + saveDataTime;

                        // save new Report
                        long saveStatus1 = mDbHelper.saveReportID(
                                reportID, reportNo, officialID, "", selectedCaseType[0], selectedSubCaseType[0], "waiting");
                        Log.i("save report", selectedCaseType[0] + " " + selectedSubCaseType[0]);
                        if (saveStatus1 <= 0) {
                            Log.i("save report", "Error!! ");
                        } else {
                            Log.i("save report", reportID + " " + selectedSubCaseType[0]
                                    + " " + officialID);
                            long SaveReceivingCase = mDbHelper.SaveReceivingCase(reportID, officialID, "receiving", sEmergencyDate,sEmergencyTime);
                            if (SaveReceivingCase == -1) {
                                Log.i("log_show draft", "SaveReceivingCase  Error!! ");
                                // status = "0";
                            } else {
                                Log.i("log_show draft", " SaveReceivingCase ok" + officialID);

                            }
                        }
                        //save preference reportID
                        mManager.setPreferenceData(mManager.PREF_REPORTID, reportID);


                         FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                         fragmentTransaction.replace(R.id.containerView, emergencyTabFragment).addToBackStack(null).commit();


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
                    //initializeData();
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
            builder.setMessage("ดูข้อมูลการตรวจนี้ " +caserepTD);

            builder.setPositiveButton("ดู", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getActivity(), "Clicked " + caserepTD, Toast.LENGTH_SHORT).show();

                    mManager.setPreferenceData(mManager.PREF_REPORTID, caserepTD);

                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView, acceptDoneTabFragment).addToBackStack(null).commit();
                }
            });
            builder.setNeutralButton("ลบ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mDbHelper.DeleteReport1(caserepTD,
                            mDbHelper.TABLE_CASESCENE.toString());

                    mDbHelper.DeleteReport1(caserepTD,
                            mDbHelper.TABLE_receivingcase.toString());
                    mDbHelper.DeleteReport1(caserepTD,
                            mDbHelper.TABLE_otherofficialinscene.toString());
                    mDbHelper.DeleteReport1(caserepTD,
                            mDbHelper.TABLE_SUFFERER.toString());

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
        //Log.i("log_show draft", "Show data All investigating" + mCursor.getCount());
        String[][] acceptCase = null;
        acceptCase = mDbHelper.acceptCase(officialID);
        if(acceptCase == null){
            Log.i("log_show acceptlist", " acceptCase null ");
        }else {

            for(int i = 0; i < acceptCase.length; i++ ){
                csiDataLists.add(new CSIDataList(acceptCase[i][25],acceptCase[i][26],
                        acceptCase[i][6],acceptCase[i][32],
                        acceptCase[i][33],acceptCase[i][34],
                        acceptCase[i][35],acceptCase[i][36],
                        acceptCase[i][37],acceptCase[i][13],acceptCase[i][38],
                        acceptCase[i][22],getDateTime.changeDateFormatToCalendar(acceptCase[i][21]),
                        acceptCase[i][20],acceptCase[i][27]));
            }

        }
        csiDataListAdapter.notifyDataSetChanged();
        showdata();
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

            String resultServer = ConnectServer.getJsonPostGet(params1, "getAcceptCase");
            Log.i("getDraftReport", officialID + resultServer);
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
                    sLastDateUpdateData, sLastTimeUpdateData,sReceivingStatus,  sEmergencyDate,sEmergencyTime = "";
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
                        sReceivingStatus= c.getString("ReceivingStatus");
                        sEmergencyDate = c.getString("EmergencyDate");
                        sEmergencyTime= c.getString("EmergencyTime");
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

                            long SaveReceivingCase = mDbHelper.SaveReceivingCase(sCaseReportID, sInquiryOfficialID,sReceivingStatus,sEmergencyDate,sEmergencyTime);
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
            final JSONArray data = new JSONArray(resultServer);
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


                    Log.i("saveDataSufferer", "receiving " + String.valueOf(i) + sCaseReportID);

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

                } return 1;
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
            final JSONArray data = new JSONArray(resultServer);
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                sScheduleID = c.getString("ScheduleID");


                Log.i("ScheduleCase", String.valueOf(i) + sCaseReportID +" "+sScheduleID);

                long saveStatus = mDbHelper.saveScheduleIDCase(sCaseReportID, sScheduleID);

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


        return 0;
    }


}