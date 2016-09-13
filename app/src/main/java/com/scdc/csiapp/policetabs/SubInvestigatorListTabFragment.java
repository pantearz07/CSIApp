package com.scdc.csiapp.policetabs;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.scdc.csiapp.main.GetDateTime;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pantearz07 on 22/9/2558.
 */
public class SubInvestigatorListTabFragment extends Fragment {
    CoordinatorLayout rootLayoutIns;
    FloatingActionButton fabBtnIns;
    RVAdapter adapter;
    private List<Person> persons;
    private RecyclerView rv;
    // connect sqlite
    SQLiteDatabase mDb;
    SQLiteDBHelper mDbHelper;
    Cursor mCursor;
    ConnectionDetector cd;
    Boolean networkConnectivity = false;
    private PreferenceData mManager;
    private TextView txtUpdateDate;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private int mSecond;
    private String CurrentDate_ID;
    GetDateTime mDateTime;
    String officialID;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View viewinspector = inflater.inflate(R.layout.investigator_list_tab_layout, null);
        Context context = getContext();
        rootLayoutIns = (CoordinatorLayout) viewinspector.findViewById(R.id.rootLayoutInv);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.policelist);
        cd = new ConnectionDetector(getActivity());
        networkConnectivity = cd.networkConnectivity();
        mManager = new PreferenceData(getActivity());
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);

        mDbHelper = new SQLiteDBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();

        mDateTime = new GetDateTime();

        txtUpdateDate = (TextView) viewinspector
                .findViewById(R.id.txtUpdateDate);
        String updatedate = mManager.getPreferenceData(
                mManager.ARG_UPDATE_DATA_INVESTIGATOR);
        if (updatedate.length() != 0) {
            txtUpdateDate.setText(updatedate);
        } else {
            txtUpdateDate.setText("อัพเดทข้อมูลล่าสุดเมื่อ : ");
        }
        persons = new ArrayList<>();
        rv = (RecyclerView) viewinspector.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        adapter = new RVAdapter(persons);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(onItemClickListener);
        refreshData();



        //fab
        fabBtnIns = (FloatingActionButton) viewinspector.findViewById(R.id.fabBtnInv);
        fabBtnIns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkConnectivity) {

                     new SelectAllInvestigator2ToSQLite().execute();

                    Toast.makeText(getActivity(),
                            "กำลังดาวน์โหลดข้อมูล", Toast.LENGTH_SHORT).show();

                } else {

                    Snackbar.make(v, "กรุณาเชื่อมต่ออินเทอร์เน็ต", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });


        return viewinspector;
    }

    RVAdapter.OnItemClickListener onItemClickListener = new RVAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(View view, int position) {
            Person ps = persons.get(position);
            //Toast.makeText(getActivity(), "Clicked " + ps.tel, Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getContext());
            builder.setMessage("โทรออก " + ps.tel);
            builder.setPositiveButton("โทรออก", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getActivity(),
                            "โทรออกแล้ว", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();

        }
    };
    private void refreshData() {
        initializeData();
        adapter = new RVAdapter(persons);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(onItemClickListener);
        String updateDate = updateDateTime();
        txtUpdateDate.setText(updateDate);

        Toast.makeText(getActivity(),
                "ดาวน์โหลดข้อมูลเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();

    }

    private void initializeData() {
         mDb = mDbHelper.getReadableDatabase();
        mCursor = mDb.rawQuery("SELECT * FROM "
                + SQLiteDBHelper.TABLE_official + " WHERE "
                + SQLiteDBHelper.COL_AccessType + " = 'investigator2'", null);

        Log.i("log_show", "Show data All investigator2 " + mCursor.getCount());
        persons.clear();
        mCursor.moveToFirst();
        while (!mCursor.isAfterLast()) {
            persons.add(new Person(mCursor.getString(mCursor
                    .getColumnIndex(SQLiteDBHelper.COL_Rank)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_FirstName))
                            + " " + mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_LastName)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_Position)),
                    R.drawable.avatar,
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_AreaCodeTel)) + "-" +
                            mCursor.getString(mCursor
                                    .getColumnIndex(SQLiteDBHelper.COL_PhoneNumber)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_AgencyName)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_CenterName))));

            mCursor.moveToNext();
        }
        mCursor.close();
        adapter.notifyDataSetChanged();
    }
    class SelectAllInvestigator2ToSQLite extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
        }

        protected String doInBackground(String... params) {
            //
            List<NameValuePair> params1 = new ArrayList<NameValuePair>();
            params1.add(new BasicNameValuePair("sOfficialID", officialID));

            String resultServer = ConnectServer.getJsonPostGet(params1,"getSceneOfficial");

            Log.i("Recieve investigator2", resultServer);
            return resultServer;
        }

        protected void onPostExecute(String resultServer) {
            String sOfficialID = "";
            String sRank = "";
            String sFirstName = "";
            String sLastName = "";
            String sPosition = "";
            String sSubPossition = "";
            String sStationName = "";
            String sAgencyName = "";
            String sCenterName = "";
            String sAreaCodeTel = "";
            String sPhoneNumber = "";
            String sOfficialEmail = "";
            String sOfficialDisplayPic = "";
            String sLastLogin = "";
            String sAccessType = "";
            try {
                final JSONArray data = new JSONArray(resultServer);
                for (int i = 0; i < data.length(); i++) {
                    JSONObject c = data.getJSONObject(i);

                    sOfficialID = c.getString("OfficialID");
                    sRank = c.getString("Rank");
                    sFirstName = c.getString("FirstName");
                    sLastName = c.getString("LastName");
                    sPosition = c.getString("Position");
                    sSubPossition = c.getString("SubPossition");
                    sStationName = c.getString("StationName");
                    sAgencyName = c.getString("AgencyName");
                    sCenterName = c.getString("CenterName");
                    sAreaCodeTel = c.getString("AreaCodeTel");
                    sPhoneNumber = c.getString("PhoneNumber");
                    sOfficialEmail = c.getString("OfficialEmail");
                    sOfficialDisplayPic = c.getString("OfficialDisplayPic");
                    sLastLogin = c.getString("LastLogin");
                    sAccessType = c.getString("AccessType");
                    long saveStatus = mDbHelper.SaveAllInvestigator(sOfficialID,
                            sRank, sFirstName, sLastName, sPosition, sSubPossition,
                            sStationName, sAgencyName, sCenterName, sAreaCodeTel, sPhoneNumber,
                            sOfficialEmail, sOfficialDisplayPic, sLastLogin, sAccessType);

                    if (saveStatus == -1) {
                        Log.i("Recieve investigator2", "Error!!");
                    }else if(saveStatus == -2) {

                        Log.i("Recieve investigator2", "had");

                    }else {

                        Log.i("Recieve investigator2" + i, sOfficialID + " " + sRank
                                + " " + sPosition + " " + sFirstName + " " + sLastName + " " + sAccessType);
                    }
                }
            } catch (JSONException e) {
                // TODO: handle exception
                e.printStackTrace();
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
            refreshData();
        }
    }
    public String updateDateTime() {
        //update date
        String sDate = "";
        String sTime = "";
        String UpdateDataDate[] = mDateTime.updateDataDateTime();
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
