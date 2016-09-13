package com.scdc.csiapp.main;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
 * Created by Pantearz07 on 22/9/2558.
 */
public class SettingFragment extends Fragment {

    CoordinatorLayout rootLayoutSetting;
    FloatingActionButton fabBtnSetting;
    private ListView listMenuSetting;

    SQLiteDatabase mDb;
    SQLiteDBHelper mDbHelper;
    private Context mContext;
    private PreferenceData mManager;
    ConnectionDetector cd;
    Boolean networkConnectivity = false;
    long isConnectingToInternet = 0;

    //ProgressDialog progressBar;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    private static final String[] MenuSetting = new String[] {
            "ตั้งค่า IP ใหม่","ข้อมูลทั้งหมด", "ข้อมูลพื้นฐานสำหรับตำรวจ", "ข้อมูลพื้นฐานสำหรับที่อยู่"};
//,"ข้อมูลตารางเวร"

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.setting_layout,null);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.settings);

        rootLayoutSetting = (CoordinatorLayout) view.findViewById(R.id.rootLayoutSetting);
        mDbHelper = new SQLiteDBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();
        mManager = new PreferenceData(getActivity());
        cd = new ConnectionDetector(getActivity());
        networkConnectivity = cd.networkConnectivity();
        isConnectingToInternet = cd.isConnectingToInternet();
        listMenuSetting = (ListView) view.findViewById(R.id.listSetting);
        listMenuSetting.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, MenuSetting));
        listMenuSetting.setTextFilterEnabled(true);
        listMenuSetting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, final int position,
                                    long id) {
                if (networkConnectivity) {

                    Log.i("connect internet", "SyncData");
                    String selected = (String) ((TextView) v).getText();
                    String sPosition = String.valueOf(position);
                    if(sPosition=="0"){

                        Log.i("settingIP", "settingIP");
                        switchPageToSettingIP();
                    }else
                     {

                        Log.i("syncData", "syncData");

                         new DownloadPoliceDataAsync().execute(sPosition);

                    }



                } else {
                    Toast.makeText(getActivity(), "กรุณาเชื่อมต่อินเทอร์เน็ต",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }
    protected Dialog onCreateDialog(int id){
        switch (id){
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setMessage("Downloading......");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                return mProgressDialog;

            default:
                return null;
        }
    }

   class DownloadPoliceDataAsync extends AsyncTask<String, Void, String> {
         private ProgressDialog dialog;

        public DownloadPoliceDataAsync(){
            dialog = new ProgressDialog(getActivity());
        }
        @SuppressWarnings("deprecation")
        protected void onPreExecute() {
            super.onPreExecute();
            //onCreateDialog(DIALOG_DOWNLOAD_PROGRESS);
            dialog.setMessage("กำลังดาวน์โหลดข้อมูล...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... sPosition) {
            // TODO Auto-generated method stub
          //  int position = 0;
           // position = Integer.parseInt(sPosition[0]);
            String Position = "";
            Position =String.valueOf(sPosition[0]);
            Log.i("Position", Position);
           /* if(Position=="0"){

                Log.i("settingIP", "settingIP");
                return String.valueOf(0);
            }else*/
            if(Position=="1"){

                Log.i("saveAllData", "saveAllData");
                saveAllData();
                return String.valueOf(1);

            }else
            if(Position=="2"){

                Log.i("savePoliceData", "savePoliceData");
                savePoliceData();
                return String.valueOf(2);
            }else
            if(Position=="3"){

                Log.i("saveAddressData", "saveAddressData");
                saveAddressData();
                return String.valueOf(3);
            }
/*
            if(position == 3){

                Log.i("saveScheduleData", "saveScheduleData");
                saveScheduleData();
            }*/
            return String.valueOf(Position);
        }

        @SuppressWarnings("deprecation")
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String Position = "";
            Position =String.valueOf(s);
            Log.i("Positions", Position);

         /*    if (Position == "0") {
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
                Log.i("settingIP", "settingIP");
                switchPageToSettingIP();

            }

          if(Position == "0"){

                Log.i("saveAllData", "saveAllData");
                Toast.makeText(getActivity(), "ดาวน์โหลดข้อมูลเรียบร้อยแล้ว",
                        Toast.LENGTH_LONG).show(); // When Finish Show Content
            }
            if(Position == "0"){

                Log.i("savePoliceData", "savePoliceData");
                Toast.makeText(getActivity(), "ดาวน์โหลดข้อมูลเรียบร้อยแล้ว",
                        Toast.LENGTH_LONG).show(); // When Finish Show Content
            }
            else if(Position == "1"  || Position == "2"  || Position == "3" ){
                */
                Log.i("saveData", "saveData");
                Toast.makeText(getActivity(), "ดาวน์โหลดข้อมูลเรียบร้อยแล้ว",
                        Toast.LENGTH_LONG).show(); // When Finish Show Content
                if(dialog.isShowing()){
                    dialog.dismiss();
                }

           // }


        }

    }
    protected void switchPageToSettingIP() {
        Intent gotoIPSettingActivity = new Intent(getActivity(), IPSettingActivity.class);

        startActivity(gotoIPSettingActivity);
        getActivity().finish();
    }
    public void saveAllData() {
        // TODO Auto-generated method stub
        Log.i("saveAllData", "saveAllData");
        savePoliceData();
        saveAddressData();
       // saveScheduleData();
    }

    public void saveScheduleData(){
        SaveScheduleInvestigateToSQLite(); SaveScheduleOfOfficialToSQLite();
    }
    public void savePoliceData() {
        // TODO Auto-generated method stub
        Log.i("savePoliceData", "savePoliceData");

        //save CaseType
        String [] SelectCaseType = mDbHelper.SelectAllCaseType();
        if (SelectCaseType == null) {
            SaveCaseTypeToSQLite();
            Log.i("add CaseType", "ok1");
        }else if (SelectCaseType != null){
            long saveStatus = mDbHelper.DropTableCaseType();
            if (saveStatus <= 0) {
                SaveCaseTypeToSQLite();
                Log.i("DropTableCaseType", "save");
            } else{

                Log.i("DropTableCaseType", "ok2");
            }
        }
        //save SubCaseType
        String  SelectSubCaseType[][] = mDbHelper.SelectSubCaseType();
        if (SelectSubCaseType == null) {
            SaveSubCaseTypeToSQLite();
            Log.i("add SubCaseType", "ok1");
        }else if (SelectSubCaseType != null){
            long saveStatus = mDbHelper.DropTableSubCaseType();
            if (saveStatus <= 0) {
                SaveSubCaseTypeToSQLite();
                Log.i("DropTableSubCaseType", "save");
            } else{

                Log.i("DropTableSubCaseType", "ok2");
            }
        }

        //save PoliceStation
        String SelectPoliceStation[][]  = mDbHelper.SelectPoliceStation();
        if (SelectPoliceStation == null) {
            SavePoliceStationToSQLite();
            Log.i("add PoliceStation", "ok1");
        }else if (SelectPoliceStation != null){
            long saveStatus = mDbHelper.DropTablePoliceStation();

            Log.i("DropTablePoliceStation", String.valueOf(saveStatus));
            if (saveStatus <= 0) {
                SavePoliceStationToSQLite();
                Log.i("DropTablePoliceStation", "save");
            } else{

                Log.i("DropTablePoliceStation", "ok2");
            }
        }

        //save policeagency
        String [] SelectPoliceAgency = mDbHelper.SelectPoliceAgency();
        if (SelectPoliceAgency == null) {
            SavePoliceAgencyToSQLite();
            Log.i("add PoliceAgency", "ok1");
        }else if (SelectPoliceAgency != null){
            long saveStatus = mDbHelper.DropTablePoliceAgency();
            if (saveStatus <= 0) {
                SavePoliceAgencyToSQLite();
                Log.i("DropTablePoliceAgency", "save");
            } else{

                Log.i("DropTablePoliceAgency", "ok2");
            }
        }

        //save PoliceCenter
        String [] SelectPoliceCenter = mDbHelper.SelectPoliceCenter();
        if (SelectPoliceCenter == null) {
            SavePoliceCenterToSQLite();
            Log.i("add PoliceCenter", "ok1");
        }else if (SelectPoliceCenter != null){
            long saveStatus = mDbHelper.DropTablePoliceCenter();
            if (saveStatus <= 0) {
                SavePoliceCenterToSQLite();
                Log.i("DropTablePoliceCenter", "save");
            } else{

                Log.i("DropTablePoliceCenter", "ok2");
            }
        }

        //save SelectPoliceRank
        String [] SelectPoliceRank = mDbHelper.SelectPoliceRank();
        if (SelectPoliceRank == null) {
            SavePoliceRankToSQLite();
            Log.i("add PoliceRank", "ok1");
        }else if (SelectPoliceRank != null){
            long saveStatus = mDbHelper.DropTablePoliceRank();
            if (saveStatus <= 0) {
                SavePoliceRankToSQLite();
                Log.i("DropTablePoliceRank", "save");
            } else{

                Log.i("DropTablePoliceRank", "ok2");
            }
        }
        //save InvPosition
        String [] SelectInvPosition = mDbHelper.SelectInvPosition();
        if (SelectInvPosition == null) {
            SaveInvPositionToSQLite();
            Log.i("add SelectInvPosition", "ok1");
        }else if (SelectInvPosition != null){
            long saveStatus = mDbHelper.DropTableInvPosition();
            if (saveStatus <= 0) {
                SaveInvPositionToSQLite();
                Log.i("DropTableInvPosition", "save");
            } else{

                Log.i("DropTableInvPosition", "ok2");
            }
        }

        //save InqvPosition
        String [] SelectInqPosition = mDbHelper.SelectInqPosition();
        if (SelectInqPosition == null) {
            SaveInqPositionToSQLite();
            Log.i("add SelectInqPosition", "ok1");
        }else if (SelectInqPosition != null){
            long saveStatus = mDbHelper.DropTableInqPosition();
            if (saveStatus <= 0) {
                SaveInqPositionToSQLite();
                Log.i("DropTableInqPosition", "save");
            } else{

                Log.i("DropTableInqPosition", "ok2");
            }
        }
        //save ComPosition
        String [] SelectComPosition = mDbHelper.SelectComPosition();
        if (SelectComPosition == null) {
            SaveComPositionToSQLite();
            Log.i("add SelectComPosition", "ok1");
        }else if (SelectComPosition != null){
            long saveStatus = mDbHelper.DropTableComPosition();
            if (saveStatus <= 0) {
                SaveComPositionToSQLite();
                Log.i("DropTableComPosition", "save");
            } else{

                Log.i("DropTableComPosition", "ok2");
            }
        }
        //save SCDCAgency
        String [] SelectSCDCAgency = mDbHelper.SelectSCDCAgency();
        if (SelectSCDCAgency == null) {
            SaveSCDCAgencyToSQLite();
            Log.i("add SelectSCDCAgency", "ok1");
        }else if (SelectSCDCAgency != null){
            long saveStatus = mDbHelper.DropTableSCDCAgency();
            if (saveStatus <= 0) {
                SaveSCDCAgencyToSQLite();
                Log.i("DropTableSCDCAgency", "save");
            } else{

                Log.i("DropTableSCDCAgency", "ok2");
            }
        }

        //save SCDCCenter
        String [] SelectSCDCCenter = mDbHelper.SelectSCDCCenter();
        if (SelectSCDCCenter == null) {
            SaveSCDCCenterToSQLite();
            Log.i("add SelectSCDCCenter", "ok1");
        }else if (SelectSCDCCenter != null){
            long saveStatus = mDbHelper.DropTableSCDCCenter();
            if (saveStatus <= 0) {
                SaveSCDCCenterToSQLite();
                Log.i("DropTableSCDCCenter", "save");
            } else{

                Log.i("DropTableSCDCCenter", "ok2");
            }
        }
        //save ResultSceneType
        String [] SelectResultSceneType = mDbHelper.SelectResultSceneType();
        if (SelectResultSceneType == null) {
            SaveResultSceneTypeToSQLite();
            Log.i("add ResultSceneType", "ok1");
        }else if (SelectResultSceneType != null){
            long saveStatus = mDbHelper.DropTableResultSceneType();
            if (saveStatus <= 0) {
                SaveResultSceneTypeToSQLite();
                Log.i("DropResultSceneType", "save");
            } else{

                Log.i("DropResultSceneType", "ok2");
            }
        }

    }
    public void saveAddressData() {
        // TODO Auto-generated method stub
        Log.i("saveAddressData", "saveAddressData");

        //save Amphur
        String SelectAmphur[][] = mDbHelper.SelectAllAmphur();
        if (SelectAmphur == null) {
            SaveAmphurToSQLite();
            Log.i("add SelectAmphur", "ok1");
        }else if (SelectAmphur != null){
            long saveStatus = mDbHelper.DropTableAmphur();
            if (saveStatus <= 0) {
                SaveAmphurToSQLite();
                Log.i("DropTableAmphur", "save");
            } else{

                Log.i("DropTableAmphur", "ok2");
            }
        }
        //save District
        String SelectDistrict[][] = mDbHelper.SelectAllDistrict();
        if (SelectDistrict == null) {
            SaveDistrictToSQLite();
            Log.i("add SelectDistrict", "ok1");
        }else if (SelectDistrict != null){
            long saveStatus = mDbHelper.DropTableDistrict();
            if (saveStatus <= 0) {
                SaveDistrictToSQLite();
                Log.i("DropTableDistrict", "save");
            } else{

                Log.i("DropTableDistrict", "ok2");
            }
        }
        //save Province
        String SelectProvince[][] = mDbHelper.SelectAllProvince();
        if (SelectProvince == null) {
            SaveProvinceToSQLite();
            Log.i("add SelectProvince", "ok1");
        }else if (SelectProvince != null){
            long saveStatus = mDbHelper.DropTableProvince();
            if (saveStatus <= 0) {
                SaveProvinceToSQLite();
                Log.i("DropTableProvince", "save");
            } else{

                Log.i("DropTableProvince", "ok2");
            }
        }
        //save Geography
        String SelectGeography[][] = mDbHelper.SelectAllGeography();
        if (SelectGeography == null) {
            SaveGeoGraphyToSQLite();
            Log.i("add SelectGeography", "ok1");
        }else if (SelectGeography != null){
            long saveStatus = mDbHelper.DropTableGeography();
            if (saveStatus <= 0) {
                SaveGeoGraphyToSQLite();
                Log.i("DropTableGeography", "save");
            } else{

                Log.i("DropTableGeography", "ok2");
            }
        }
    }
    public boolean SaveCaseTypeToSQLite() {
        // TODO Auto-generated method stub
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sTableName", "casescenetype"));

        try {
            final JSONArray data = new JSONArray(
                    ConnectServer
                            .getJsonPostGet(params, "getData"));

            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                String sCaseTypeID = c.getString("CaseTypeID");
                String sCaseTypeName = c.getString("CaseTypeName");
                long saveStatus = mDbHelper.saveDataCaseType(sCaseTypeID,
                        sCaseTypeName);
                if (saveStatus <= 0) {
                    Log.i("Recieve", "Error!! ");
                }
                Log.i("Recieve" + i, sCaseTypeID + " " + sCaseTypeName);
            }
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return true;
    }

    //save SubCaseType
    public boolean SaveSubCaseTypeToSQLite() {
        // TODO Auto-generated method stub
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sTableName", "subcasescenetype"));

        try {
            final JSONArray data = new JSONArray(
                    ConnectServer
                            .getJsonPostGet(params, "getData"));

            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                String sSubCaseTypeID = c.getString("SubCaseTypeID");
                String sCaseTypeID = c.getString("CaseTypeID");
                String sSubCaseTypeName = c.getString("SubCaseTypeName");
                long saveStatus = mDbHelper.saveDataSubCaseType(sSubCaseTypeID,
                        sCaseTypeID, sSubCaseTypeName);
                if (saveStatus <= 0) {
                    Log.i("Recieve", "Error!! ");
                }
                Log.i("Recieve" + i, sSubCaseTypeID  + " " +  sCaseTypeID +" " + sSubCaseTypeName);
            }
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return true;
    }

    public boolean SavePoliceStationToSQLite() {
        // TODO Auto-generated method stub
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sTableName", "policestation"));

        try {
            final JSONArray data = new JSONArray(
                    ConnectServer
                            .getJsonPostGet(params, "getData"));

            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                String sPoliceStationID = c.getString("PoliceStationID");
                String sPoliceStationCode = c.getString("PoliceStationCode");
                String sPoliceAgencyCode = c.getString("PoliceAgencyCode");
                String sPoliceStationName = c.getString("PoliceStationName");
                long saveStatus = mDbHelper.SavePoliceStation(sPoliceStationID, sPoliceStationCode, sPoliceAgencyCode, sPoliceStationName);
                if (saveStatus <= 0) {
                    Log.i("Recieve", "Error!! ");
                }
                Log.i("Recieve" + i, sPoliceStationID  + " " +  sPoliceStationCode +" " + sPoliceStationName);
            }
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return true;
    }
    public boolean SavePoliceAgencyToSQLite() {
        // TODO Auto-generated method stub
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sTableName", "policeagency"));

        try {
            final JSONArray data = new JSONArray(
                    ConnectServer
                            .getJsonPostGet(params, "getData"));

            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                String sPoliceAgencyID = c.getString("PoliceAgencyID");
                String sPoliceAgencyCode = c.getString("PoliceAgencyCode");
                String sPoliceCenterID = c.getString("PoliceCenterID");
                String sPoliceAgencyName = c.getString("PoliceAgencyName");
                long saveStatus = mDbHelper.SavePoliceAgency(sPoliceAgencyID, sPoliceAgencyCode, sPoliceCenterID, sPoliceAgencyName);
                if (saveStatus <= 0) {
                    Log.i("Recieve", "Error!! ");
                }
                Log.i("Recieve PoliceAgency" + i, sPoliceAgencyID  + " " +  sPoliceAgencyCode +" " + sPoliceAgencyName);
            }
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return true;
    }
    public boolean SavePoliceCenterToSQLite() {
        // TODO Auto-generated method stub
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sTableName", "policecenter"));

        try {
            final JSONArray data = new JSONArray(
                    ConnectServer
                            .getJsonPostGet(params, "getData"));

            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                String sPoliceCenterID = c.getString("PoliceCenterID");
                String sPoliceName = c.getString("PoliceName");
                long saveStatus = mDbHelper.SavePoliceCenter(sPoliceCenterID, sPoliceName);
                if (saveStatus <= 0) {
                    Log.i("Recieve", "Error!! ");
                }
                Log.i("Recieve PoliceCenter" + i, sPoliceCenterID  + " " +  sPoliceName );
            }
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return true;
    }
    public boolean SavePoliceRankToSQLite() {
        // TODO Auto-generated method stub

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sTableName", "policerank"));

        try {
            final JSONArray data = new JSONArray(
                    ConnectServer
                            .getJsonPostGet(params, "getData"));

            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                String sRankID = c.getString("RankID");
                String sRankName = c.getString("RankName");
                String sRankAbbr = c.getString("RankAbbr");
                long saveStatus = mDbHelper.SavePoliceRank(sRankID, sRankName, sRankAbbr);
                if (saveStatus <= 0) {
                    Log.i("Recieve", "Error!! ");
                }
                Log.i("Recieve SavePoliceRank" + i, sRankID  + " " +  sRankName );
            }
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return true;
    }

    public boolean SaveInvPositionToSQLite() {
        // TODO Auto-generated method stub

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sTableName", "invposition"));

        try {
            final JSONArray data = new JSONArray(
                    ConnectServer
                            .getJsonPostGet(params, "getData"));

            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                String sInvPosID = c.getString("InvPosID");
                String sInvPosName = c.getString("InvPosName");
                String sInvPosAbbr = c.getString("InvPosAbbr");
                long saveStatus = mDbHelper.SaveInvPosition(sInvPosID, sInvPosName, sInvPosAbbr);
                if (saveStatus <= 0) {
                    Log.i("Recieve", "Error!! ");
                }
                Log.i("Recieve SaveInvPosition" + i, sInvPosID  + " " +  sInvPosName );
            }
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return true;
    }

    public boolean SaveInqPositionToSQLite() {
        // TODO Auto-generated method stub

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sTableName", "inqposition"));

        try {
            final JSONArray data = new JSONArray(
                    ConnectServer
                            .getJsonPostGet(params, "getData"));

            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                String sInqPosID = c.getString("InqPosID");
                String sInqPosName = c.getString("InqPosName");
                String sInqPosAbbr = c.getString("InqPosAbbr");
                long saveStatus = mDbHelper.SaveInqPosition(sInqPosID, sInqPosName, sInqPosAbbr);
                if (saveStatus <= 0) {
                    Log.i("Recieve", "Error!! ");
                }
                Log.i("Recieve SaveInqPosition" + i, sInqPosID  + " " +  sInqPosName );
            }
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return true;
    }

    public boolean SaveComPositionToSQLite() {
        // TODO Auto-generated method stub

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sTableName", "composition"));

        try {
            final JSONArray data = new JSONArray(
                    ConnectServer
                            .getJsonPostGet(params, "getData"));

            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                String sComPosID = c.getString("ComPosID");
                String sComPosName = c.getString("ComPosName");
                String sComPosAbbr = c.getString("ComPosAbbr");
                long saveStatus = mDbHelper.SaveComPosition(sComPosID, sComPosName, sComPosAbbr);
                if (saveStatus <= 0) {
                    Log.i("Recieve", "Error!! ");
                }
                Log.i("Recieve SaveComPosition" + i, sComPosID  + " " +  sComPosName );
            }
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return true;
    }
    public boolean SaveSCDCAgencyToSQLite() {
        // TODO Auto-generated method stub

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sTableName", "scdcagency"));

        try {
            final JSONArray data = new JSONArray(
                    ConnectServer
                            .getJsonPostGet(params, "getData"));

            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                String sSCDCAgencyID = c.getString("SCDCAgencyID");
                String sSCDCAgencyCode = c.getString("SCDCAgencyCode");
                String sSCDCCenterID = c.getString("SCDCCenterID");
                String sSCDCAgencyName = c.getString("SCDCAgencyName");
                long saveStatus = mDbHelper.SaveSCDCAgency(sSCDCAgencyID, sSCDCAgencyCode, sSCDCCenterID, sSCDCAgencyName);
                if (saveStatus <= 0) {
                    Log.i("Recieve", "Error!! ");
                }
                Log.i("Recieve scdcagency" + i, sSCDCAgencyID  + " " +  sSCDCAgencyName );
            }
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return true;
    }
    public boolean SaveSCDCCenterToSQLite() {
        // TODO Auto-generated method stub

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sTableName", "scdccenter"));

        try {
            final JSONArray data = new JSONArray(
                    ConnectServer
                            .getJsonPostGet(params, "getData"));

            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                String sSCDCCenterID = c.getString("SCDCCenterID");
                String sSCDCCenterName = c.getString("SCDCCenterName");
                String sSCDCCenterProvince = c.getString("SCDCCenterProvince");

                long saveStatus = mDbHelper.SaveSCDCCenter(sSCDCCenterID, sSCDCCenterName, sSCDCCenterProvince);
                if (saveStatus <= 0) {
                    Log.i("Recieve", "Error!! ");
                }
                Log.i("Recieve SCDCCenter" + i, sSCDCCenterID  + " " +  sSCDCCenterProvince );
            }
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return true;
    }
    public boolean SaveAmphurToSQLite() {
        // TODO Auto-generated method stub

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sTableName", "amphur"));

        try {
            final JSONArray data = new JSONArray(
                    ConnectServer
                            .getJsonPostGet(params, "getData"));

            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                String sAmphur_ID = c.getString("AMPHUR_ID");
                String sAmphur_Code = c.getString("AMPHUR_CODE");
                String sAmphur_Name = c.getString("AMPHUR_NAME");
                String sPostCode = c.getString("POSTCODE");
                String sGEO_ID = c.getString("GEO_ID");
                String sProvince_ID = c.getString("PROVINCE_ID");

                long saveStatus = mDbHelper.SaveAmphur(sAmphur_ID, sAmphur_Code, sAmphur_Name, sPostCode, sGEO_ID, sProvince_ID);
                if (saveStatus <= 0) {
                    Log.i("Recieve", "Error!! ");
                }
                Log.i("Recieve Amphur" + i, sAmphur_ID  + " " +  sAmphur_Name );
            }
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return true;
    }
    public boolean SaveDistrictToSQLite() {
        // TODO Auto-generated method stub

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sTableName", "district"));

        try {
            final JSONArray data = new JSONArray(
                    ConnectServer
                            .getJsonPostGet(params, "getData"));

            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                String sDistrict_ID = c.getString("DISTRICT_ID");
                String sDistrict_Code = c.getString("DISTRICT_CODE");
                String sDistrict_Name = c.getString("DISTRICT_NAME");
                String sAmphur_ID = c.getString("AMPHUR_ID");
                String sProvince_ID = c.getString("PROVINCE_ID");
                String sGEO_ID = c.getString("GEO_ID");

                long saveStatus = mDbHelper.SaveDistrict(sDistrict_ID, sDistrict_Code, sDistrict_Name, sAmphur_ID, sProvince_ID, sGEO_ID);
                if (saveStatus <= 0) {
                    Log.i("Recieve", "Error!! ");
                }
                Log.i("Recieve District" + i, sDistrict_ID  + " " +  sDistrict_Name );
            }
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return true;
    }
    public boolean SaveProvinceToSQLite() {
        // TODO Auto-generated method stub

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sTableName", "province"));

        try {
            final JSONArray data = new JSONArray(
                    ConnectServer
                            .getJsonPostGet(params, "getData"));

            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                String sProvince_ID = c.getString("PROVINCE_ID");
                String sProvince_Code = c.getString("PROVINCE_CODE");
                String sProvince_Name = c.getString("PROVINCE_NAME");
                String sGEO_ID = c.getString("GEO_ID");

                long saveStatus = mDbHelper.SaveProvince(sProvince_ID, sProvince_Code, sProvince_Name, sGEO_ID);
                if (saveStatus <= 0) {
                    Log.i("Recieve", "Error!! ");
                }
                Log.i("Recieve sProvince" + i, sProvince_ID  + " " +  sProvince_Name );
            }
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return true;
    }
    public boolean SaveGeoGraphyToSQLite() {
        // TODO Auto-generated method stub

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sTableName", "geography"));

        try {
            final JSONArray data = new JSONArray(
                    ConnectServer
                            .getJsonPostGet(params, "getData"));

            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                String sGEO_ID = c.getString("GEO_ID");
                String sGeo_Name = c.getString("GEO_NAME");

                long saveStatus = mDbHelper.SaveGeography(sGEO_ID, sGeo_Name);
                if (saveStatus <= 0) {
                    Log.i("Recieve", "Error!! ");
                }
                Log.i("Recieve sGeo" + i, sGEO_ID  + " " +  sGeo_Name );
            }
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return true;
    }
    public boolean SaveResultSceneTypeToSQLite() {
        // TODO Auto-generated method stub

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sTableName", "resultscenetype"));

        try {
            final JSONArray data = new JSONArray(
                    ConnectServer
                            .getJsonPostGet(params, "getData"));

            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                String sRSTypeID = c.getString("RSTypeID");
                String sRSTypeName = c.getString("RSTypeName");

                long saveStatus = mDbHelper.SaveResultSceneType(sRSTypeID, sRSTypeName);
                if (saveStatus <= 0) {
                    Log.i("Recieve", "Error!! ");
                }
                Log.i("SaveResultSceneType" + i, sRSTypeID  + " " +  sRSTypeName );
            }
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return true;
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
                String 	sSCDCCenterID = c.getString("SCDCCenterID");


                long saveStatus = mDbHelper. SaveScheduleInvestigate(sScheduleID, sSchedule_Date, sSchedule_Month, sSCDCCenterID);
                if (saveStatus <= 0) {
                    Log.i("Recieve", "Error!! ");
                }
                Log.i("scheduleinvestigate" + i, sScheduleID  + " " +  sSchedule_Date );
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


                long saveStatus = mDbHelper. SaveScheduleofofficial(sScheduleID, sOfficialID);
                if (saveStatus <= 0) {
                    Log.i("Recieve", "Error!! ");
                }
                Log.i("scheduleofofficial" + i, sScheduleID  + " " +  sOfficialID );
            }
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return true;
    }
}
