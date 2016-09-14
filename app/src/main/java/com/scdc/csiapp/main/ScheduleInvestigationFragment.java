package com.scdc.csiapp.main;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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
public class ScheduleInvestigationFragment extends Fragment {
    CoordinatorLayout rootLayoutDraft;
    FloatingActionButton fabBtnDraft;
    RecyclerView rvSchedule;
    List<ScheduleList> scheduleLists;
    private ScheduleAdapter scheduleListAdapter;
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
    GetDateTime getDateTime;
    private String CurrentDate_ID;
    TextView txtUpdateDate;
    SwipeRefreshLayout swipeContainer;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View viewdraft = inflater.inflate(R.layout.schedule_layout, null);
        // Context context = getContext();
        Log.i("log_show receiving", "onCreateView!! ");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.schedule);
        rootLayoutDraft = (CoordinatorLayout) viewdraft.findViewById(R.id.rootLayoutDraft);
        mDbHelper = new SQLiteDBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();
        mManager = new PreferenceData(getActivity());
        context = viewdraft.getContext();
        mFragmentManager = getActivity().getSupportFragmentManager();
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        cd = new ConnectionDetector(getActivity());
        networkConnectivity = cd.isNetworkAvailable();
        isConnectingToInternet = cd.isConnectingToInternet();
        getDateTime = new GetDateTime();
        txtUpdateDate = (TextView) viewdraft
                .findViewById(R.id.txtUpdateDate);
        String updatedate = mManager.getPreferenceData(
                mManager.ARG_UPDATE_DATA_RECEIVINGCASE);
        if (updatedate.length() != 0) {
            txtUpdateDate.setText(updatedate);
        } else {
            txtUpdateDate.setText("อัพเดทข้อมูลล่าสุดเมื่อ : ");
        }
        scheduleLists = new ArrayList<>();
        rvSchedule = (RecyclerView) viewdraft.findViewById(R.id.rvDraft);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvSchedule.setLayoutManager(llm);
        rvSchedule.setHasFixedSize(true);

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
                    new SelectAllScheduleSQLite().execute();


                    //  refreshData();
                    Toast.makeText(getActivity(),
                            "กำลังดาวน์โหลดข้อมูล", Toast.LENGTH_SHORT).show();
                } else {
                    swipeContainer.setRefreshing(false);
                    Log.i("log_show receiving", "fail network");
                    refreshData();
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
                    new SelectAllScheduleSQLite().execute();
                } else {
                    swipeContainer.setRefreshing(false);
                    Log.i("log_show receiving", "fail network");
                    refreshData();
                    Snackbar.make(viewdraft, "กรุณาเชื่อมต่ออินเทอร์เน็ต", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        scheduleListAdapter = new ScheduleAdapter(scheduleLists);
        rvSchedule.setAdapter(scheduleListAdapter);
        refreshData();
        fabBtnDraft = (FloatingActionButton) viewdraft.findViewById(R.id.fabBtnDraft);
        fabBtnDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (networkConnectivity) {
                    new SelectAllScheduleSQLite().execute();


                    //  refreshData();
                    //  Toast.makeText(getActivity(),
                    //        "กำลังดาวน์โหลดข้อมูล", Toast.LENGTH_SHORT).show();

                } else {

                    Snackbar.make(v, "กรุณาเชื่อมต่ออินเทอร์เน็ต", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });

        return viewdraft;
    }

    private void refreshData() {
        initializeData();
        scheduleListAdapter = new ScheduleAdapter(scheduleLists);
        rvSchedule.setAdapter(scheduleListAdapter);
        // scheduleListAdapter.setOnItemClickListener(onItemClickListener);
        String updateDate = updateDateTime();
        txtUpdateDate.setText(updateDate);

        Toast.makeText(getActivity(),
                "ดาวน์โหลดข้อมูลเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();

    }

    public boolean SaveScheduleInvestigateToSQLite() {
        // TODO Auto-generated method stub
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sTableName", "scheduleinvestigate"));

        try {
            final JSONArray data = new JSONArray(
                    ConnectServer
                            .getJsonPostGet(params, "getData"));

            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                String sScheduleID = c.getString("ScheduleID");
                String sSchedule_Date = c.getString("Schedule_Date");
                String sSchedule_Month = c.getString("Schedule_Month");
                String sSCDCCenterID = c.getString("SCDCCenterID");


                long saveStatus = mDbHelper.SaveScheduleInvestigate(sScheduleID, sSchedule_Date, sSchedule_Month, sSCDCCenterID);
                if (saveStatus <= 0) {
                    Log.i("Recieve", "Error!! ");
                }
                Log.i("scheduleinvestigate" + i, sScheduleID + " " + sSchedule_Date);
            }
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return true;
    }

    public boolean SaveScheduleOfOfficialToSQLite() {
        // TODO Auto-generated method stub
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sTableName", "scheduleofofficial"));

        try {
            final JSONArray data = new JSONArray(
                    ConnectServer
                            .getJsonPostGet(params, "getData"));

            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                String sScheduleID = c.getString("ScheduleID");
                String sOfficialID = c.getString("OfficialID");


                long saveStatus = mDbHelper.SaveScheduleofofficial(sScheduleID, sOfficialID);
                if (saveStatus <= 0) {
                    Log.i("Recieve", "Error!! ");
                }
                Log.i("scheduleofofficial" + i, sScheduleID + " " + sOfficialID);
            }
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return true;
    }

    private void initializeData() {
        mDb = mDbHelper.getReadableDatabase();
        mCursor = mDb.rawQuery("SELECT scheduleinvestigate.ScheduleID," +
                "scheduleinvestigate.Schedule_Date,scheduleinvestigate.Schedule_Month," +
                "scheduleinvestigate.SCDCCenterID  FROM "
                + SQLiteDBHelper.TABLE_scheduleofofficial + ","
                + SQLiteDBHelper.TABLE_scheduleinvestigate
                + " WHERE scheduleinvestigate.ScheduleID = scheduleofofficial.ScheduleID AND " +
                "scheduleofofficial.OfficialID = '" + officialID + "' " +
                "ORDER BY scheduleinvestigate.Schedule_Date ASC", null);
//SELECT * FROM `scheduleofofficial`,scheduleinvestigate
// WHERE scheduleinvestigate.ScheduleID = scheduleofofficial.ScheduleID AND
// scheduleofofficial.OfficialID = 'INV_SCDCC04_2'
        Log.i("log_show", "Show data Schedule" + mCursor.getCount());
        scheduleLists.clear();
        mCursor.moveToFirst();
        while (!mCursor.isAfterLast()) {
            scheduleLists.add(new ScheduleList(
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_ScheduleID)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_Schedule_Date)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_Schedule_Month)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_SCDCCenterID))
            ));

            mCursor.moveToNext();
        }
        mCursor.close();
        scheduleListAdapter.notifyDataSetChanged();
    }
/*
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
        scheduleListAdapter = new ScheduleAdapter(scheduleLists);
        rvSchedule.setAdapter(scheduleListAdapter);
         String updateDate = updateDateTime();
        txtUpdateDate.setText(updateDate);


    }*/


    @Override
    public void onResume() {
        super.onResume();
        Log.i("log_show receiving", " onResume ");
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

    class SelectAllScheduleSQLite extends AsyncTask<String, Void, String> {
        /*private final ProgressDialog dialog = new ProgressDialog(
                 getActivity());

        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
            this.dialog.setMessage("Loading...");
             this.dialog.setCancelable(false);
             this.dialog.show();
        }*/

        @Override
        protected String doInBackground(String... params) {
            SaveScheduleInvestigateToSQLite();
            SaveScheduleOfOfficialToSQLite();
            String status = "1";
            return status;
        }

        @Override
        protected void onPostExecute(String status) {
            if (status == "1") {
                scheduleListAdapter.notifyDataSetChanged();
                refreshData();
                if (swipeContainer.isRefreshing()) {
                    swipeContainer.setRefreshing(false);
                }
            } else {
                if (swipeContainer.isRefreshing()) {
                    swipeContainer.setRefreshing(false);
                }

            }

        }
    }
}