package com.scdc.csiapp.main;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
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
import java.util.Date;
import java.util.List;

/**
 * Created by Pantearz07 on 23/9/2558.
 */
public class ReceivingCaseListFragment2 extends Fragment {
    CoordinatorLayout rootLayoutDraft;

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
    SwipeRefreshLayout swipeContainer;
    private static NotificationManager mNotificationManager;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View viewdraft = inflater.inflate(R.layout.receivigcase_layout2, null);
        // Context context = getContext();
        Log.i("log_show receiving", "onCreateView!! ");
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
                mManager.ARG_UPDATE_DATA_RECEIVINGCASE);
        if (updatedate.length() != 0) {
            txtUpdateDate.setText(updatedate);
        } else {
            txtUpdateDate.setText("อัพเดทข้อมูลล่าสุดเมื่อ : ");
        }
        receivigCaseLists = new ArrayList<>();
        rvReceive = (RecyclerView) viewdraft.findViewById(R.id.rvDraft);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvReceive.setLayoutManager(llm);
        rvReceive.setHasFixedSize(true);




        if (networkConnectivity) {
            Log.i("networkConnectivity", "connect!! ");

            new getNoti().execute();


            //showNotification();
        } else {
            Log.i("networkConnectivity", "no connect!! ");
        }
        // Lookup the swipe container view

        swipeContainer = (SwipeRefreshLayout) viewdraft.findViewById(R.id.swipeContainer);

        // Setup refresh listener which triggers new data loading

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {


                // Your code to refresh the list here.

                // Make sure you call swipeContainer.setRefreshing(false)

                // once the network request has completed successfully.

                if (networkConnectivity) {
                    Log.i("log_show receiving", "Refreshing!! ");

                    swipeContainer.setRefreshing(true);
                    initializeData();

                    // Snackbar.make(viewdraft, "ดาวน์โหลดข้อมูลเรียบร้อยแล้ว", Snackbar.LENGTH_LONG)
                    //       .setAction("Action", null).show();
                } else {
                    swipeContainer.setRefreshing(false);
                    Log.i("log_show receiving", "fail network");

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
                Log.i("log_show receiving", "Runnable");

                if (networkConnectivity) {
                    Log.i("log_show receiving", "Refreshing!! ");

                    swipeContainer.setRefreshing(true);
                    initializeData();

                    // Snackbar.make(viewdraft, "ดาวน์โหลดข้อมูลเรียบร้อยแล้ว", Snackbar.LENGTH_LONG)
                    //       .setAction("Action", null).show();
                } else {
                    swipeContainer.setRefreshing(false);
                    Log.i("log_show receiving", "fail network");

                    Snackbar.make(viewdraft, "กรุณาเชื่อมต่ออินเทอร์เน็ต", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        receivigCaseListAdapter = new ReceivingCaseAdapter(receivigCaseLists);
        rvReceive.setAdapter(receivigCaseListAdapter);


        return viewdraft;
    }

    private void acceptCase(String caseReportID) {
        new sendNoti().execute(caseReportID);
        new saveReceivingReportFromServer().execute(caseReportID);
        Toast.makeText(getActivity(), "เลือกรับตรวจคดี " + caseReportID, Toast.LENGTH_SHORT).show();

    }
    private class sendNoti extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> params1 = new ArrayList<NameValuePair>();
            params1.add(new BasicNameValuePair("CaseReportID", params[0]));

            String resultServer = ConnectServer.getJsonPostGet(params1, "sendNotification");
            Log.i("sendNotification", params[0] + resultServer);
            String success = "";

            try {
                JSONObject c = new JSONObject(resultServer.replace("\uFEFF", ""));
                success = c.getString("success");
                return success;
            } catch (JSONException e) {
                // TODO: handle exception
                e.printStackTrace();
                Log.e("log_tag", "Error parsing data " + e.toString());

                return null;
            }


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == "1") {
                Log.e("sendNoti", "noti success");

            } else {
                Log.e("sendNoti", "noti failure");
            }

        }
    }
    private class getNoti extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            List<NameValuePair> params1 = new ArrayList<NameValuePair>();
            params1.add(new BasicNameValuePair("sOfficialID", officialID));

            String resultServer = ConnectServer.getJsonPostGet(params1, "getReceivingReport");
            Log.i("getReceivingReport", officialID + resultServer);
           if(resultServer=="error"){
               String status = "error";
               return status;
           }else {
               try {
                   final JSONArray data = new JSONArray(resultServer.replace("\uFEFF", ""));
                   if (data.length() > 0) {
                       return String.valueOf(data.length());
                   }else{
                       return String.valueOf("0");
                   }
               } catch (JSONException e) {
                   // TODO: handle exception
                   e.printStackTrace();
                   Log.e("log_tag", "Error parsing data " + e.toString());
                   String status = "error";
                   return status;
               }
           }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s == "error") {
                Toast.makeText(getActivity(), "กรุณาเปลี่ยนค่า IP ใหม่",
                        Toast.LENGTH_LONG).show(); // When Finish Show Content
               // switchPageToSettingIP();
            } else if(s == "0") {

            }else{
                showNotification(s);
            }
        }


    }

    public void showNotification(String number) {
        Intent intent = new Intent(getActivity(), MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            // API 16 onwards

            //Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Notification.Builder builder = new Notification.Builder(context);
            builder.setAutoCancel(false)
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentTitle("คุณมีงานค้างตรวจ")
                    .setContentText(number + " คดี")
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.logo_csi)
                    .setWhen((new Date()).getTime())
                    .setSound(defaultSoundUri)
                    .setDefaults(Notification.DEFAULT_ALL);
            Notification notification = builder.build();
            notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
            mNotificationManager =
                    (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1000, notification);

        }else {

        Notification notification =
                new NotificationCompat.Builder(getActivity()) // this is context
                        .setPriority(android.support.v4.app.NotificationCompat.PRIORITY_MAX)
                        .setSmallIcon(R.drawable.logo_csi)
                        .setContentTitle("คุณมีงานค้างตรวจ")
                        .setContentText(number + " คดี")
                        .setAutoCancel(true)
                        .setWhen((new Date()).getTime())
                        .setSound(defaultSoundUri)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentIntent(pendingIntent)
                        .build();
        NotificationManager notificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1001, notification);
        }
    }

    private void initializeData() {
        Log.i("log_show receiving", "Loading data ");

        Toast.makeText(getActivity(), "กำลังดาวน์โหลดข้อมูล", Toast.LENGTH_SHORT).show();

        new getReceivingReportFromServer().execute();


    }

    private void showdata() {
        // receivigCaseListAdapter.notifyDataSetChanged();
        if (swipeContainer.isRefreshing()) {
            swipeContainer.setRefreshing(false);
        }
        receivigCaseListAdapter = new ReceivingCaseAdapter(receivigCaseLists);
        rvReceive.setAdapter(receivigCaseListAdapter);
        String updateDate = updateDateTime();
        txtUpdateDate.setText(updateDate);

        receivigCaseListAdapter.setOnItemClickListener(onItemClickListener);


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

                   // FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    //fragmentTransaction.replace(R.id.containerView, fCSIDataTabFragment).addToBackStack(null).commit();
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



        }
    };


    class getReceivingReportFromServer extends AsyncTask<String, Void, String> {
        // private final ProgressDialog dialog = new ProgressDialog(
        //         getActivity());

        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
            // this.dialog.setMessage("Loading...");
            // this.dialog.setCancelable(false);
            // this.dialog.show();
        }


        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> params1 = new ArrayList<NameValuePair>();
            params1.add(new BasicNameValuePair("sOfficialID", officialID));

            String resultServer = ConnectServer.getJsonPostGet(params1, "getReceivingReport");
            Log.i("getReceivingReport", officialID + resultServer);
            if(resultServer=="error"){
                Log.i("getReceivingReport", officialID + resultServer);
                String status = "2";
                return status;
            }else {
                String caseReportID = "";
                String policeStation = "";
                String typeCase = "";
                String dateReceiving = "";
                String timeReceiving = "";
                //  String sInquiryOfficialID = "";
                String sRank = "";
                String sFirstName = "";
                String sLastName = "";
                String sAreaCodeTel = "";
                String sPhoneNumber = "";
                String LocaleName = "";
                String HouseNo = "";
                String VillageNo = "";
                String VillageName = "";
                String LaneName = "";
                String RoadName = "";
                String District = "";
                String Amphur = "";
                String Province = "";
                try {
                    final JSONArray data = new JSONArray(resultServer.replace("\uFEFF", ""));
                    if (data.length() > 0) {
                        receivigCaseLists.clear();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject c = data.getJSONObject(i);

                            caseReportID = c.getString("CaseReportID");
                            policeStation = c.getString("PoliceStation");
                            typeCase = c.getString("SubCaseTypeName");
                            dateReceiving = c.getString("ReceivingCaseDate");
                            timeReceiving = c.getString("ReceivingCaseTime");
                            LocaleName = c.getString("LocaleName");
                            HouseNo = c.getString("HouseNo");
                            VillageNo = c.getString("VillageNo");
                            VillageName = c.getString("VillageName");
                            LaneName = c.getString("LaneName");
                            RoadName = c.getString("RoadName");
                            District = c.getString("District");
                            Amphur = c.getString("Amphur");
                            Province = c.getString("Province");

                            sRank = c.getString("Rank");
                            sFirstName = c.getString("FirstName");
                            sLastName = c.getString("LastName");
                            sAreaCodeTel = c.getString("AreaCodeTel");
                            sPhoneNumber = c.getString("PhoneNumber");
                            receivigCaseLists.add(new ReceivingCaseList(
                                    caseReportID, typeCase, LocaleName,
                                    HouseNo,
                                    VillageNo,
                                    VillageName,
                                    LaneName,
                                    RoadName,
                                    District,
                                    Amphur,
                                    Province, policeStation, getDateTime.changeDateFormatToCalendar(dateReceiving), timeReceiving,
                                    sRank, sFirstName, sLastName, sAreaCodeTel, sPhoneNumber));
                            Log.i("log_receiving", String.valueOf(i) + caseReportID + " " + sFirstName);

                        }

                        String status = "1";
                        return status;
                    } else {
                        String status = "0";
                        return status;
                    }
                } catch (JSONException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    Log.e("log_tag", "Error parsing data " + e.toString());
                    String status = "2";
                    return status;
                }
            }


        }

        protected void onPostExecute(String status) {
            if (status == "1") {
                receivigCaseListAdapter.notifyDataSetChanged();
                showdata();
            } if (status == "2") {
                    Toast.makeText(getActivity(), "กรุณาเปลี่ยนค่า IP ใหม่",
                            Toast.LENGTH_LONG).show(); // When Finish Show Content
                   // switchPageToSettingIP();
                }
            else {

                if (swipeContainer.isRefreshing()) {
                    swipeContainer.setRefreshing(false);
                }

            }
          /*  String caseReportID = "";
            String policeStation = "";
            String typeCase = "";
            String dateReceiving = "";
            String timeReceiving = "";
             String sRank = "";
            String sFirstName = "";
            String sLastName = "";

            try {
                final JSONArray data = new JSONArray(resultServer);
                for (int i = 0; i < data.length(); i++) {
                    JSONObject c = data.getJSONObject(i);
                    caseReportID = c.getString("CaseReportID");
                    policeStation = c.getString("PoliceStation");
                    typeCase = c.getString("SubCaseTypeName");
                    dateReceiving = c.getString("ReceivingCaseDate");
                    timeReceiving = c.getString("ReceivingCaseTime");
                      sRank = c.getString("Rank");
                    sFirstName = c.getString("FirstName");
                    sLastName = c.getString("LastName");
                    //ReceivingCaseList item = new ReceivingCaseList();

                    receivigCaseLists.add(new ReceivingCaseList(caseReportID, sRank, sFirstName,
                            sLastName, policeStation, typeCase, dateReceiving, timeReceiving
                    ));
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

          }*/
        }
    }
    protected void switchPageToFragment() {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerView, fCSIDataTabFragment).addToBackStack(null).commit();

    }
//    protected void switchPageToSettingIP() {
//        Intent gotoIPSettingActivity = new Intent(getActivity(), IPSettingActivity.class);
//
//        startActivity(gotoIPSettingActivity);
//        getActivity().finish();
//    }
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
                    if (saveScheduleInvestigators(params[0]) == 1) {
                        Log.i("ScheduleInvestigators", "OK");
                        return "ok";
                    }else {
                        Log.i("ScheduleInvestigators", "error");
                        return "error";
                    }
                }else{
                    Log.i("saveCaseReport", "error");
                    return "error";
                }

            } else {
                Log.i("saveSuffer", "error");
                return "error";
            }

        }

        protected void onPostExecute(String resultServer) {
            if(resultServer == "0k") {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    switchPageToFragment();
                }
            }else {
                if (dialog.isShowing()) {
                    dialog.dismiss();

                }
            }

        }
    }

    public long saveCaseReport(String sCaseReportID) {
        List<NameValuePair> params1 = new ArrayList<NameValuePair>();
        params1.add(new BasicNameValuePair("sOfficialID", officialID));
        params1.add(new BasicNameValuePair("sCaseReportID", sCaseReportID));

        String resultServer = ConnectServer.getJsonPostGet(params1, "getUpdateReceivingReport");

        Log.i("ReceivingReport", resultServer);
        String sInvestigatorOfficialID, sInquiryOfficialID,
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
                sLastDateUpdateData, sLastTimeUpdateData, sRecievingStatus, sEmergencyDate, sEmergencyTime = "";
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
                    sRecievingStatus = c.getString("ReceivingStatus");
                    sEmergencyDate = c.getString("EmergencyDate");
                    sEmergencyTime = c.getString("EmergencyTime");
                    Log.i("log_receivings", sCaseReportID+" "+sToSCDCagency+" "+sToSCDCcenter);

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
                    } else {

                        Log.i("Recieving", "ok");

                        long SaveReceivingCase = mDbHelper.SaveReceivingCase(sCaseReportID, sInquiryOfficialID, sRecievingStatus, sEmergencyDate, sEmergencyTime);
                        if (SaveReceivingCase == -1) {
                            Log.i("SaveReceivingCase", "Error!! ");
                            return -1;
                        } else {
                            Log.i("SaveReceivingCase", "receiving ok" + sInquiryOfficialID);
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

                }
                return 1;
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
            final JSONArray data = new JSONArray(resultServer.replace("\uFEFF", ""));
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                sScheduleID = c.getString("ScheduleID");


                Log.i("ScheduleCase", String.valueOf(i) + sCaseReportID);

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


        return 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("log_show receiving", " onResume ");
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
        Log.i("log_show receiving", " onDestroyView ");

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