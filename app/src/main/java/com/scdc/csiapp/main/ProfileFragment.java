package com.scdc.csiapp.main;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Pantearz07 on 23/9/2558.
 */
public class ProfileFragment extends Fragment {
    // connect sqlite
    SQLiteDatabase mDb;
    SQLiteDBHelper mDbHelper;
    private Context mContext;
    private PreferenceData mManager;
    ConnectionDetector cd;
    Boolean networkConnectivity = false;
    long isConnectingToInternet = 0;

    String officialID;
    TextView tMemberID, tAccessType;
    EditText tUsername, tPassword, txtFirstName, txtLastName, tEmail, txtAreaCodeTel,
            txtPhoneNumber;
    Spinner spinnerRankInspector, spinnerPositionInspector;
    Button btnUpdateMember;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile_layout, null);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.profile);

        mContext = getContext();
        mManager = new PreferenceData(getActivity());
        cd = new ConnectionDetector(getActivity());
        networkConnectivity = cd.isNetworkAvailable();
        isConnectingToInternet = cd.isConnectingToInternet();

        mDbHelper = new SQLiteDBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();

        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
// txtMemberID,txtMemberID,txtUsername,txtPassword,txtName,txtEmail,txtTel
        tMemberID = (TextView) view.findViewById(R.id.txtMemberID);
        tUsername = (EditText) view.findViewById(R.id.txtUsername);
        tPassword = (EditText) view.findViewById(R.id.txtPassword);
        spinnerRankInspector = (Spinner) view
                .findViewById(R.id.spinnerRankInspector);
        txtFirstName = (EditText) view.findViewById(R.id.txtFirstName);
        txtLastName = (EditText) view.findViewById(R.id.txtLastName);

        spinnerPositionInspector = (Spinner) view
                .findViewById(R.id.spinnerPositionInspector);
        tEmail = (EditText) view.findViewById(R.id.txtEmail);
        txtAreaCodeTel = (EditText) view.findViewById(R.id.editTextPhone1);
        txtAreaCodeTel.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                if (txtAreaCodeTel.getText().toString().length() == 3) {
                    txtPhoneNumber.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        txtPhoneNumber = (EditText) view.findViewById(R.id.editTextPhone2);
        tAccessType = (TextView) view.findViewById(R.id.txtStatus);


        if (isConnectingToInternet == 1) {
            new showOfficialDataTask().execute(officialID);

        } else if (isConnectingToInternet == 2) {
            Log.d("internet status", "data plan");
            new showOfficialDataFromSQLiteTask().execute(officialID);
        } else {
            new showOfficialDataFromSQLiteTask().execute(officialID);
        }
        btnUpdateMember = (Button) view.findViewById(R.id.btnSave);
        btnUpdateMember.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                // check for Internet status
                if (networkConnectivity) {
                    if (updateMember()) {
                        new updateOfficialDataTask().execute(tMemberID.getText().toString(),
                                tUsername.getText().toString(),
                                tPassword.getText().toString(),
                                String.valueOf(spinnerRankInspector.getSelectedItem()),
                                txtFirstName.getText().toString(),
                                txtLastName.getText().toString(),
                                String.valueOf(spinnerPositionInspector.getSelectedItem()),
                                txtAreaCodeTel.getText().toString(),
                                txtPhoneNumber.getText().toString(),
                                tEmail.getText().toString(),
                                tAccessType.getText().toString());
                        Log.i("Recieve", "Save in SQLite success");
                    } else {
                        Toast.makeText(getActivity(),
                                "เเก้ไขข้อมูลใน SQLite ไม่สำเร็จ",
                                Toast.LENGTH_LONG).show();
                    }


                } else {
                    // Internet connection is not present

                    if (updateMember()) {


                        Toast.makeText(getActivity(),
                                "เเก้ไขข้อมูลใน SQLite เรียบร้อยเเล้ว",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

        });

        return view;
    }

    private boolean updateMember() {

        long saveStatus = mDbHelper.updateMember(tMemberID
                        .getText().toString(), tUsername.getText()
                        .toString(), tPassword.getText().toString(),
                String.valueOf(spinnerRankInspector
                        .getSelectedItem()), txtFirstName.getText()
                        .toString(), txtLastName.getText()
                        .toString(), String
                        .valueOf(spinnerPositionInspector
                                .getSelectedItem()), txtAreaCodeTel.getText().toString(),
                txtPhoneNumber.getText().toString(),
                tEmail.getText().toString(), "", tAccessType
                        .getText().toString());

        if (saveStatus <= 0) {
            Log.i("Recieve", "Error!! ");
            mDbHelper.close();
            return false;
        } else {
            mDbHelper.close();
            return true;
        }


    }

    class showOfficialDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
        }


        @Override
        protected String doInBackground(String... params) {
            String paramsOfficialID = params[0];
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("strOfficialID", paramsOfficialID));

            String resultServer = ConnectServer.getJsonPostGet(nameValuePairs, "getOfficialDetail");

            Log.i("Recieve strOfficialID", resultServer);
            return resultServer;
        }

        protected void onPostExecute(String resultServer) {


            String sOfficialID = "";
            String sUsername = "";
            String sPassword = "";
            String sRank = "";
            String sFirstName = "";
            String sLastName = "";
            String sAlias = "";
            String sPosition = "";
            String sSubPossition = "";
            String sStationName = "";
            String sAgencyName = "";
            String sCenterName = "";
            String sAreaCodeTel = "";
            String sPhoneNumber = "";
            String sOfficialEmail = "";
            String sOfficialDisplayPic = "";
            String sIMEI = "";
            String sLastLogin = "";
            String sAccessType = "";
            JSONObject c;
            try {
                c = new JSONObject(resultServer);
                sOfficialID = c.getString("OfficialID");
                sUsername = c.getString("Username");
                sPassword = c.getString("Password");
                sAlias = c.getString("Alias");
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
                sIMEI = c.getString("IMEI");
                sLastLogin = c.getString("LastLogin");
                sAccessType = c.getString("AccessType");
                Log.i("show sFirstName", sFirstName + " " + sLastName);

                if (!sOfficialID.equals("")) {
                    tMemberID.setText(sOfficialID);
                    tUsername.setText(sUsername);
                    tPassword.setText(sPassword);
                    txtFirstName.setText(sFirstName);
                    txtLastName.setText(sLastName);
                    tEmail.setText(sOfficialEmail);
                    txtAreaCodeTel.setText(sAreaCodeTel);
                    txtPhoneNumber.setText(sPhoneNumber);

                    if(sAccessType == "investigator")
                        tAccessType.setText("ผู้ตรวจสถานที่เกิดเหตุ");
                    if(sAccessType == "inquiryofficial")
                        tAccessType.setText("พนักงานสืบสวน");
                    if (sRank.length() != 0) {
                        String[] strRank = getResources().getStringArray(
                                R.array.rank_inspector);
                        spinnerRankInspector.setSelection(Arrays.asList(strRank).indexOf(
                                sRank));
                    }

                    if (sPosition.length() != 0) {
                        String[] strPosition = getResources().getStringArray(
                                R.array.inv_position);
                        spinnerPositionInspector.setSelection(Arrays.asList(strPosition)
                                .indexOf(sPosition));
                    }

                } else {
                    tMemberID.setText("-");
                    tUsername.setText("-");
                    tPassword.setText("-");
                    txtFirstName.setText("-");
                    txtLastName.setText("-");
                    tEmail.setText("-");
                    txtAreaCodeTel.setText("-");
                    txtPhoneNumber.setText("-");
                    tAccessType.setText("-");
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e("log_tag", "Error parsing data " + e.toString());
            }


        }
    }

    class showOfficialDataFromSQLiteTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
        }

        protected String[] doInBackground(String... params) {
            //
            // Show Data
            String[] arrData = {""};
            arrData = mDbHelper.SelectDataOfficial(params[0]);

            Log.i("Recieve", arrData[8]);
            return arrData;
        }

        protected void onPostExecute(String[] arrData) {
            // Dismiss ProgressBar
            //Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
            /*** Default Value ***/
            if (arrData != null) {

                tMemberID.setText(arrData[5]);
                tUsername.setText(arrData[14]);
                tPassword.setText(arrData[15]);
                txtFirstName.setText(arrData[6]);
                txtLastName.setText(arrData[7]);
                txtAreaCodeTel.setText(arrData[2]);
                txtPhoneNumber.setText(arrData[1]);
                if (arrData[4].length() != 0) {
                    String[] sRank = getResources().getStringArray(
                            R.array.rank_inspector);
                    spinnerRankInspector.setSelection(Arrays.asList(sRank).indexOf(
                            arrData[4]));
                }

                if (arrData[8].length() != 0) {
                    String[] sPosition = getResources().getStringArray(
                            R.array.inv_position);
                    spinnerPositionInspector.setSelection(Arrays.asList(sPosition)
                            .indexOf(arrData[8]));
                }
                tEmail.setText(arrData[12]);
                tAccessType.setText(arrData[18]);


            }

        }
    }

    class updateOfficialDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
        }


        @Override
        protected String doInBackground(String... paramsData) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("sOfficialID", paramsData[0]));
            params.add(new BasicNameValuePair("sUsername"
                    , paramsData[1]));
            params.add(new BasicNameValuePair("sPassword"
                    , paramsData[2]));
            params.add(new BasicNameValuePair("sRank", paramsData[3]));
            params.add(new BasicNameValuePair("sFirstName", paramsData[4]));
            params.add(new BasicNameValuePair("sLastName", paramsData[5]));

            params.add(new BasicNameValuePair("sPosition", paramsData[6]));
            params.add(new BasicNameValuePair("sAreaTel", paramsData[7]));
            params.add(new BasicNameValuePair("sTel", paramsData[8]));
            params.add(new BasicNameValuePair("sEmail", paramsData[9]));
            params.add(new BasicNameValuePair("sAvatar", ""));
            params.add(new BasicNameValuePair("sStatus", paramsData[10]));

            String resultServer = ConnectServer
                    .getJsonPostGet(params,
                            "editProfileMember");
            Log.i("editProfileMember", resultServer);
            return resultServer;
        }

        protected void onPostExecute(String resultServer) {

            String strStatusID = "0";
            String strError = "Unknow Status!";
            JSONObject c;

            try {
                c = new JSONObject(resultServer);
                strStatusID = c.getString("StatusID");
                strError = c.getString("Error");
            } catch (JSONException e) {
                // TODO: handle exception
                e.printStackTrace();
            }

            // Prepare Save Data
            if (strStatusID.equals("0")) {
                Toast.makeText(getActivity(),
                        "เเก้ไขข้อมูลไม่สำเร็จ",
                        Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getActivity(),
                        "เเก้ไขข้อมูลเรียบร้อยเเล้ว",
                        Toast.LENGTH_LONG).show();

            }


        }
    }


}
