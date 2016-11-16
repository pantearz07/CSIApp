package com.scdc.csiapp.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiProfile;
import com.scdc.csiapp.apimodel.ApiStatus;
import com.scdc.csiapp.apimodel.ApiStatusResult;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import static android.support.design.widget.Snackbar.LENGTH_INDEFINITE;

/**
 * Created by Pantearz07 on 23/9/2558.
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = "DEBUG-ProfileFragment";
    // connect sqlite
    DBHelper dbHelper;
    private Context mContext;
    private PreferenceData mManager;
    ConnectionDetector cd;
    private CoordinatorLayout rootLayout;
    String officialID, sDisplayPicpath, Username_old, Username_new;
    TextView tMemberID, tAccessType, txtChangePassword, txtCenter, change_display;
    EditText edtUsername, edtPassword, edtFirstName, edtLastName, edtEmail,
            editTextPhone, edtPosition;
    Spinner spinnerRankInspector, spinnerPositionInspector;
    Button btnUpdateMember;
    GetDateTime getDateTime;
    boolean oldRank, oldPosition = false;
    String[] Rank, Position;
    Snackbar snackbar;
    FloatingActionButton fabBtn;
    String[] mRankArray2, mPositionArray2;
    String[][] mRankArray, mPositionArray;
    ChangePassFragment changePassFragment;
    ImageView profile_image;
    Uri uri;
    File newfile;

    private String mCurrentPhotoPath;
    public static final int REQUEST_CAMERA_OUTSIDE = 77;
    public static final int REQUEST_GALLERY = 777;
    private static String strSDCardPathName_temp = "/CSIFiles/temp/";
    private static String strSDCardPathName_temps = "/CSIFiles/temp/temps/";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile_layout, null);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.profile);
        rootLayout = (CoordinatorLayout) view.findViewById(R.id.rootLayout);
        changePassFragment = new ChangePassFragment();
        mContext = getContext();
        mManager = new PreferenceData(getActivity());
        cd = new ConnectionDetector(getActivity());
        dbHelper = new DBHelper(getActivity());
        getDateTime = new GetDateTime();
// txtMemberID,txtMemberID,txtUsername,txtPassword,txtName,txtEmail,txtTel
        Username_old = mManager.getPreferenceData(dbHelper.COL_id_users);
        txtChangePassword = (TextView) view.findViewById(R.id.txtChangePassword);
        tMemberID = (TextView) view.findViewById(R.id.txtMemberID);
        edtUsername = (EditText) view.findViewById(R.id.edtUsername);
        edtPassword = (EditText) view.findViewById(R.id.edtPassword);
        edtPassword.setVisibility(View.GONE);
        spinnerRankInspector = (Spinner) view
                .findViewById(R.id.spinnerRankInspector);
        mRankArray = dbHelper.SelectAllRank();
        if (mRankArray != null) {
            mRankArray2 = new String[mRankArray.length];
            for (int i = 0; i < mRankArray.length; i++) {
                mRankArray2[i] = mRankArray[i][2];
                Log.i(TAG + " show mRankArray2", mRankArray2[i].toString());
            }
            ArrayAdapter<String> adapterRank = new ArrayAdapter<String>(
                    getActivity(), android.R.layout.simple_dropdown_item_1line,
                    mRankArray2);
            spinnerRankInspector.setAdapter(adapterRank);
        } else {
            Log.i(TAG + " show mRankArray", "null");
        }
        if (WelcomeActivity.profile.getTbOfficial().Rank == null || WelcomeActivity.profile.getTbOfficial().Rank.equals("") || WelcomeActivity.profile.getTbOfficial().Rank.equals("null")) {
            spinnerRankInspector.setSelection(0);
        } else {
            for (int i = 0; i < mRankArray2.length; i++) {
                if (WelcomeActivity.profile.getTbOfficial().Rank.trim().equals(mRankArray2[i])) {
                    spinnerRankInspector.setSelection(i);
                    oldRank = true;
                    break;
                }
            }
        }
        spinnerRankInspector.setOnItemSelectedListener(new ProOnItemSelectedListener());
        spinnerRankInspector.setOnTouchListener(new ProOnItemSelectedListener());

        spinnerPositionInspector = (Spinner) view
                .findViewById(R.id.spinnerPositionInspector);
        if (WelcomeActivity.profile.getTbOfficial().AccessType.equals("investigator")) {
            mPositionArray = dbHelper.SelectAllinvposition();
        } else if (WelcomeActivity.profile.getTbOfficial().AccessType.equals("inquiryofficial")) {
            mPositionArray = dbHelper.SelectAllinqposition();
        }
        if (mPositionArray != null) {
            mPositionArray2 = new String[mPositionArray.length];
            for (int i = 0; i < mPositionArray.length; i++) {
                mPositionArray2[i] = mPositionArray[i][2];
                Log.i(TAG + " show mPositionArray2", mPositionArray2[i].toString());
            }
            ArrayAdapter<String> adapterPosition = new ArrayAdapter<String>(
                    getActivity(), android.R.layout.simple_dropdown_item_1line,
                    mPositionArray2);
            spinnerPositionInspector.setAdapter(adapterPosition);
        } else {
            Log.i(TAG + " show mRankArray", "null");
        }
        if (WelcomeActivity.profile.getTbOfficial().Position == null || WelcomeActivity.profile.getTbOfficial().Position.equals("") || WelcomeActivity.profile.getTbOfficial().Position.equals("null")) {
            spinnerPositionInspector.setSelection(0);
        } else {
            for (int i = 0; i < mPositionArray2.length; i++) {
                if (WelcomeActivity.profile.getTbOfficial().Position.trim().equals(mPositionArray2[i])) {
                    spinnerPositionInspector.setSelection(i);
                    oldPosition = true;
                    break;
                }
            }
        }

        spinnerPositionInspector.setOnItemSelectedListener(new ProOnItemSelectedListener());
        spinnerPositionInspector.setOnTouchListener(new ProOnItemSelectedListener());

//        edtPosition = (EditText) view.findViewById(R.id.edtPosition);
        edtFirstName = (EditText) view.findViewById(R.id.edtFirstName);
        edtLastName = (EditText) view.findViewById(R.id.edtLastName);
        edtEmail = (EditText) view.findViewById(R.id.edtEmail);
        editTextPhone = (EditText) view.findViewById(R.id.editTextPhone);
        edtUsername.addTextChangedListener(new ProfileTextWatcher(edtUsername));
        edtFirstName.addTextChangedListener(new ProfileTextWatcher(edtFirstName));
        edtLastName.addTextChangedListener(new ProfileTextWatcher(edtLastName));
        edtEmail.addTextChangedListener(new ProfileTextWatcher(edtEmail));
//        edtPosition.addTextChangedListener(new ProfileTextWatcher(edtPosition));
        editTextPhone.addTextChangedListener(new ProfileTextWatcher(editTextPhone));
        tAccessType = (TextView) view.findViewById(R.id.txtStatus);
        txtCenter = (TextView) view.findViewById(R.id.txtCenter);
        fabBtn = (FloatingActionButton) view.findViewById(R.id.fabBtn);
        fabBtn.setOnClickListener(new ProfileOnClickListener());
        txtChangePassword.setOnClickListener(new ProfileOnClickListener());

        if (WelcomeActivity.profile.getTbOfficial() != null) {
            officialID = WelcomeActivity.profile.getTbOfficial().OfficialID;
            tMemberID.setText(WelcomeActivity.profile.getTbOfficial().OfficialID);

            edtUsername.setText(WelcomeActivity.profile.getTbOfficial().id_users);
            edtFirstName.setText(WelcomeActivity.profile.getTbOfficial().FirstName);
            edtLastName.setText(WelcomeActivity.profile.getTbOfficial().LastName);
            edtEmail.setText(WelcomeActivity.profile.getTbOfficial().OfficialEmail);
            editTextPhone.setText(WelcomeActivity.profile.getTbOfficial().PhoneNumber);
            Log.i(TAG, WelcomeActivity.profile.getTbOfficial().AccessType);
            if (WelcomeActivity.profile.getTbOfficial().AccessType.equals("investigator")) {
                tAccessType.setText("ผู้ตรวจสถานที่เกิดเหตุ");
                if (WelcomeActivity.profile.getTbOfficial().SCDCAgencyCode == null || WelcomeActivity.profile.getTbOfficial().SCDCAgencyCode.equals("")) {

                    txtCenter.setText("");
                } else {
                    String SCDCAgencyName = dbHelper.getSCDCAgencyName(WelcomeActivity.profile.getTbOfficial().SCDCAgencyCode);
                    txtCenter.setText(SCDCAgencyName);
                }
            }
            if (WelcomeActivity.profile.getTbOfficial().AccessType.equals("inquiryofficial")) {
                tAccessType.setText("พนักงานสอบสวน");
                if (WelcomeActivity.profile.getTbOfficial().PoliceStationID == null || WelcomeActivity.profile.getTbOfficial().PoliceStationID.equals("")) {

                    txtCenter.setText("");
                } else {
                    String PoliceStionName = dbHelper.getPoliceStionName(WelcomeActivity.profile.getTbOfficial().PoliceStationID);
                    txtCenter.setText("สภ." + PoliceStionName);
                }
            }

        }
        if (cd.isNetworkAvailable()) {
            edtUsername.setEnabled(true);
        } else {
            edtUsername.setEnabled(false);
        }
        if (WelcomeActivity.profile.getTbUsers() != null) {

            edtUsername.setText(WelcomeActivity.profile.getTbUsers().id_users);
            edtPassword.setText(WelcomeActivity.profile.getTbUsers().pass);
        }
        profile_image = (ImageView) view.findViewById(R.id.profile_image);
        change_display = (TextView) view.findViewById(R.id.change_display);
        change_display.setOnClickListener(new ProfileOnClickListener());
        if (WelcomeActivity.profile.getTbUsers().getPicture() == null || WelcomeActivity.profile.getTbUsers().getPicture().equals("")) {

        } else {

            File avatarfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                    strSDCardPathName_temp + WelcomeActivity.profile.getTbUsers().getPicture());
            if (avatarfile.exists()) {
                Picasso.with(getActivity())
                        .load(avatarfile)
                        .resize(100, 100)
                        .centerCrop()
                        .into(profile_image);
            }
        }

        return view;
    }


    private class ProfileTextWatcher implements TextWatcher {
        private EditText mEditText;

        public ProfileTextWatcher(EditText editText) {
            mEditText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable == editTextPhone.getEditableText()) {
                WelcomeActivity.profile.getTbOfficial().setPhoneNumber(editTextPhone.getText().toString());
                Log.i(TAG, "PhoneNumber " + WelcomeActivity.profile.getTbOfficial().PhoneNumber);
            } else if (editable == edtEmail.getEditableText()) {
                WelcomeActivity.profile.getTbOfficial().setOfficialEmail(edtEmail.getText().toString());
                Log.i(TAG, "OfficialEmail " + WelcomeActivity.profile.getTbOfficial().OfficialEmail);
            } else if (editable == edtFirstName.getEditableText()) {
                WelcomeActivity.profile.getTbOfficial().setFirstName(edtFirstName.getText().toString());
                WelcomeActivity.profile.getTbUsers().setName(edtFirstName.getText().toString());
                Log.i(TAG, "FirstName " + WelcomeActivity.profile.getTbOfficial().FirstName);
            } else if (editable == edtLastName.getEditableText()) {
                WelcomeActivity.profile.getTbOfficial().setLastName(edtLastName.getText().toString());
                WelcomeActivity.profile.getTbUsers().setSurname(edtLastName.getText().toString());
                Log.i(TAG, "LastName " + WelcomeActivity.profile.getTbOfficial().LastName);
            } else if (editable == edtUsername.getEditableText()) {
                Username_new = edtUsername.getText().toString();
                Log.i(TAG, "id_users " + edtUsername.getText().toString());
                if (cd.isNetworkAvailable()) {
                    CheckUsername checkUsername = new CheckUsername();
                    checkUsername.execute(edtUsername.getText().toString());
                }
            }
//            else if (editable == edtPosition.getEditableText()) {
//                WelcomeActivity.profile.getTbOfficial().setSubPossition(edtPosition.getText().toString());
//                Log.i(TAG, "SubPossition " + WelcomeActivity.profile.getTbOfficial().SubPossition);
//            }
        }
    }

    public void hiddenKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private class ProfileOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view == txtChangePassword) {
                hiddenKeyboard();
                if (cd.isNetworkAvailable()) {
                    if (WelcomeActivity.profile.getTbOfficial().AccessType.equals("investigator")) {
                        MainActivity.setFragment(changePassFragment, 1);
                    } else {
                        InqMainActivity.setFragment(changePassFragment, 1);
                    }
                } else {
                    if (snackbar == null || !snackbar.isShown()) {
                        snackbar = Snackbar.make(rootLayout, getString(R.string.network_unavailable)
                                , LENGTH_INDEFINITE)
                                .setAction(getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


                                    }
                                });
                        snackbar.show();
                    }
                }
            }
            if (view == change_display) {
                hiddenKeyboard();
                File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), strSDCardPathName_temp);
                try {
                    // Create folder
                    if (!folder.exists()) {
                        folder.mkdir();
                        Log.i(TAG, "mkdir folder temp" + folder.getPath());
                    } else {
                        Log.i(TAG, "folder.exists" + folder.getPath());

                    }
                } catch (Exception ex) {
                    Log.i(TAG, "mkdir" + ex.getMessage());
                }
                File folder_temps = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), strSDCardPathName_temps);
                try {
                    // Create folder
                    if (!folder_temps.exists()) {
                        folder_temps.mkdir();
                        Log.i(TAG, "mkdir folder_temps" + folder_temps.getPath());
                    } else {
                        Log.i(TAG, "folder.exists" + folder_temps.getPath());

                    }
                } catch (Exception ex) {
                    Log.i(TAG, "mkdir" + ex.getMessage());
                }
                sDisplayPicpath = "img_" + WelcomeActivity.profile.getTbOfficial().OfficialID + ".jpg";

                String title = "เลือกรูปภาพโปรไฟล์";
                CharSequence[] itemlist = {"ถ่ายรูป",
                        "เลือกจากอัลบั้มภาพ"
//                        , "เปิดจากไฟล์"
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setIcon(R.drawable.icon_app);
                builder.setTitle(title);
                builder.setItems(itemlist, new DialogInterfaceOnClickListener());
                AlertDialog alert = builder.create();
                alert.setCancelable(true);
                alert.show();
            }
            if (view == fabBtn) {
                hiddenKeyboard();
                if (cd.isNetworkAvailable()) {
                    if (WelcomeActivity.profile != null) {
                        //save to server
                        EditProfile editProfile = new EditProfile();
                        editProfile.execute(WelcomeActivity.profile);

                    }
                } else {
                    if (snackbar == null || !snackbar.isShown()) {
                        snackbar = Snackbar.make(rootLayout, getString(R.string.network_unavailable)
                                , LENGTH_INDEFINITE)
                                .setAction(getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


                                    }
                                });
                        snackbar.show();
                    }
                }

            }
        }
    }

    private class ProOnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener, View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (view == spinnerRankInspector) {
                oldRank = false;
            }
            if (view == spinnerPositionInspector) {
                oldPosition = false;
            }
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            switch (adapterView.getId()) {

                case R.id.spinnerRankInspector:
                    if (oldRank == false) {
                        WelcomeActivity.profile.getTbOfficial().setRank(String.valueOf(mRankArray2[i]));
                        WelcomeActivity.profile.getTbUsers().setTitle(String.valueOf(mRankArray2[i]));
                        Log.i(TAG, "Rank " + WelcomeActivity.profile.getTbOfficial().Rank);
                    }
                    break;
                case R.id.spinnerPositionInspector:
                    if (oldPosition == false) {
                        WelcomeActivity.profile.getTbOfficial().setPosition(String.valueOf(mPositionArray2[i]));
                        WelcomeActivity.profile.getTbUsers().setPosition(String.valueOf(mPositionArray2[i]));
                        Log.i(TAG, "Position " + WelcomeActivity.profile.getTbOfficial().Position);
                    }
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            switch (adapterView.getId()) {

                case R.id.spinnerRankInspector:
                    WelcomeActivity.profile.getTbOfficial().setRank(String.valueOf(mRankArray2[0]));
                    WelcomeActivity.profile.getTbUsers().setTitle(String.valueOf(mRankArray2[0]));
                    Log.i(TAG, "Rank " + WelcomeActivity.profile.getTbOfficial().Rank);
                    break;
                case R.id.spinnerPositionInspector:
                    WelcomeActivity.profile.getTbOfficial().setPosition(String.valueOf(mPositionArray2[0]));
                    WelcomeActivity.profile.getTbUsers().setPosition(String.valueOf(mPositionArray2[0]));
                    Log.i(TAG, "Position " + WelcomeActivity.profile.getTbOfficial().Position);
                    break;
            }
        }
    }

    class EditProfile extends AsyncTask<ApiProfile, Void, ApiStatusResult> {

        @Override
        protected ApiStatusResult doInBackground(ApiProfile... apiProfiles) {
            return WelcomeActivity.api.editProfile(apiProfiles[0]);
        }

        @Override
        protected void onPostExecute(ApiStatusResult apiStatus) {
            super.onPostExecute(apiStatus);

//            Log.d(TAG, apiStatus.getStatus());
            if (apiStatus.getStatus().equalsIgnoreCase("success")) {
                Log.d(TAG, apiStatus.getData().getReason());
                boolean isSuccess = dbHelper.updateProfile(WelcomeActivity.profile, Username_old);
                if (isSuccess) {

                    boolean isSuccess2 = mManager.registerUser(WelcomeActivity.profile.getTbUsers(), WelcomeActivity.profile.getTbOfficial());
                    if (isSuccess2) {
                        if (snackbar == null || !snackbar.isShown()) {

                            SnackBarAlert snackBarAlert = new SnackBarAlert(snackbar, rootLayout, LENGTH_INDEFINITE, getString(R.string.save_complete)
                                    + " " + apiStatus.getData().getResult().toString());
                            snackBarAlert.createSnacbar();
                        }

                    } else {
                        Toast.makeText(getActivity(), "บันทึก pref ไม่สำเร็จ", Toast.LENGTH_LONG).show();
                        if (snackbar == null || !snackbar.isShown()) {

                            SnackBarAlert snackBarAlert = new SnackBarAlert(snackbar, rootLayout, LENGTH_INDEFINITE,
                                    getString(R.string.save_error) + "และบันทึก pref ไม่สำเร็จ"
                                            + " " + apiStatus.getData().getResult().toString());
                            snackBarAlert.createSnacbar();
                        }
                    }

                } else {
                    if (snackbar == null || !snackbar.isShown()) {

                        SnackBarAlert snackBarAlert = new SnackBarAlert(snackbar, rootLayout, LENGTH_INDEFINITE,
                                getString(R.string.save_error));
                        snackBarAlert.createSnacbar();
                    }
                }

            } else {
                Toast.makeText(getActivity(),
                        getString(R.string.error_data) + " " + getString(R.string.network_error),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    class CheckUsername extends AsyncTask<String, Void, ApiStatus> {

        @Override
        protected ApiStatus doInBackground(String... strings) {
            return WelcomeActivity.api.checkUsername(strings[0]);
        }

        @Override
        protected void onPostExecute(ApiStatus apiStatus) {
            super.onPostExecute(apiStatus);

//            Log.d(TAG, apiStatus.getStatus());
            if (apiStatus.getStatus().equalsIgnoreCase("success")) {
                Log.d(TAG, apiStatus.getData().getReason());

                if (apiStatus.getData().getReason() != null) {
                    String status = apiStatus.getData().getReason();
                    if (status.equals("0")) { //"ชื่อ Username เดิม";
                        edtUsername.setError("ชื่อ Username เดิม");
//                        Toast.makeText(getActivity(),
//                                "ชื่อ Username เดิม",
//                                Toast.LENGTH_SHORT).show();
                        WelcomeActivity.profile.getTbOfficial().setId_users(Username_old);
                        WelcomeActivity.profile.getTbUsers().setId_users(Username_old);
                    }
                    if (status.equals("1")) { //"สามารถใช้ Username นี้ได้";
                        edtUsername.setError("สามารถใช้ Username นี้ได้");
//                        Toast.makeText(getActivity(),
//                                "สามารถใช้ Username นี้ได้",
//                                Toast.LENGTH_SHORT).show();
                        WelcomeActivity.profile.getTbOfficial().setId_users(Username_new);
                        WelcomeActivity.profile.getTbUsers().setId_users(Username_new);
                    }
                    if (status.equals("2")) { //"มีผู้ใช้ Username นี้เเล้ว";
                        edtUsername.setError("มีผู้ใช้ Username นี้เเล้ว");
//                        Toast.makeText(getActivity(),
//                                "มีผู้ใช้ Username นี้เเล้ว",
//                                Toast.LENGTH_SHORT).show();
                        WelcomeActivity.profile.getTbOfficial().setId_users(Username_old);
                        WelcomeActivity.profile.getTbUsers().setId_users(Username_old);
                    }
                } else {

                    Toast.makeText(getActivity(),
                            "เเกิดข้อผิดพลาด",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(),
                        getString(R.string.error_data) + " " + getString(R.string.network_error),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private Object mActivityResultSubscriber = new Object() {
        @Subscribe
        public void onActivityResultReceived(ActivityResultEvent event) {
            int requestCode = event.getRequestCode();
            int resultCode = event.getResultCode();
            Intent data = event.getData();
            onActivityResult(requestCode, resultCode, data);
        }
    };

    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart profilefragment ");
        ActivityResultBus.getInstance().register(mActivityResultSubscriber);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop profilefragment");

        ActivityResultBus.getInstance().unregister(mActivityResultSubscriber);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume profilefragment");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Result media", String.valueOf(requestCode) + " " + String.valueOf(resultCode));


        if (requestCode == REQUEST_CAMERA_OUTSIDE) {
            if (resultCode == getActivity().RESULT_OK) {
                try {
                    Log.i(TAG, "Photo from camera 1" + sDisplayPicpath);
//                    String path = uri.getPath();
//                    Log.i(TAG, "path Photo from camera " + path);
                    try {
                        File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        File data1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        if (sd.canWrite()) {
                            String sourceImagePath = strSDCardPathName_temp + sDisplayPicpath;
                            String destinationImagePath = strSDCardPathName_temps + sDisplayPicpath;
                            File source = new File(data1, sourceImagePath);
                            File destination = new File(sd, destinationImagePath);
                            if (source.exists()) {
                                Log.i(TAG, "source" + sourceImagePath);
                                FileChannel src = new FileInputStream(source).getChannel();
                                FileChannel dst = new FileOutputStream(destination).getChannel();
//                                if (destination.exists())
//                                    destination.delete();
                                try {
                                    dst.transferFrom(src, 0, src.size());
                                    Log.i(TAG, "transferFrom ");
                                } catch (IOException e) {
                                    Log.i(TAG, "transferFrom " + e.getMessage());
                                }
                                if (destination != null) {
                                    source.delete();
                                    WelcomeActivity.profile.getTbOfficial().setOfficialDisplayPic(sDisplayPicpath);
                                    WelcomeActivity.profile.getTbUsers().setPicture(sDisplayPicpath);
                                    boolean isSuccess = dbHelper.updateDisplayProfile(WelcomeActivity.profile);
                                    if (isSuccess) {
                                        Log.i(TAG, "OfficialDisplayPic :" + String.valueOf(WelcomeActivity.profile.getTbOfficial().OfficialDisplayPic));
                                        Log.i(TAG, "PHOTO saved to Gallery!" + strSDCardPathName_temp + sDisplayPicpath);
                                        Picasso.with(getActivity())
                                                .load(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                                                        , strSDCardPathName_temp + WelcomeActivity.profile.getTbUsers().getPicture()))
                                                .resize(100, 100)
                                                .centerCrop()
                                                .into(profile_image);
                                    }
                                }
                                src.close();
                                dst.close();
                                Log.i(TAG, "save new Photo from camera " + destinationImagePath);
//                                if (cd.isNetworkAvailable()) {
//                                    if (WelcomeActivity.profile.getTbOfficial() != null) {
//                                        //save to server
//                                        EditProfile editProfile = new EditProfile();
//                                        editProfile.execute(WelcomeActivity.profile);
//
//                                    }
//                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.i(TAG, "Photo from camera " + e.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                //data.getData();
                String path = uri.getPath();
                File source = new File(path);
                if (source.exists())
                    source.delete();
                Log.i(TAG, "media recording cancelled." + sDisplayPicpath);
            } else {
                Log.i(TAG, "Failed to record media");
            }
        }
        if (requestCode == REQUEST_GALLERY && resultCode == getActivity().RESULT_OK && null != data) {

//            InputStream inputStream = context.getContentResolver().openInputStream(data.getData());
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
            try {
                Uri selectedImage = data.getData();
                Log.i(TAG, "path Photo from gallery " + selectedImage.getPath());

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                Log.i(TAG, "Photo from gallery " + picturePath);

                try {
                    File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    File datadest = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

                    if (sd.canWrite()) {
//                        String sourceImagePath = "/path/to/source/file.jpg";
                        String destinationImagePath = strSDCardPathName_temp + sDisplayPicpath;
                        File source = new File(picturePath);

                        File destination = new File(datadest, destinationImagePath);
                        if (source.exists()) {
                            Log.i(TAG, "source ");
                            FileChannel src = new FileInputStream(source).getChannel();
                            FileChannel dst = new FileOutputStream(destination).getChannel();

                            try {
                                dst.transferFrom(src, 0, src.size());
                                Log.i(TAG, "transferFrom ");
                            } catch (IOException e) {
                                Log.i(TAG, "transferFrom " + e.getMessage());
                            }
                            if (destination.exists()) {
//                                source.delete();
                                Log.i(TAG, "source.delete ");
                                WelcomeActivity.profile.getTbOfficial().setOfficialDisplayPic(sDisplayPicpath);
                                WelcomeActivity.profile.getTbUsers().setPicture(sDisplayPicpath);
                                boolean isSuccess = dbHelper.updateDisplayProfile(WelcomeActivity.profile);
                                if (isSuccess) {
                                    Log.i(TAG, "OfficialDisplayPic :" + String.valueOf(WelcomeActivity.profile.getTbOfficial().OfficialDisplayPic));
                                    Log.i(TAG, "PHOTO saved to Gallery!" + strSDCardPathName_temp + sDisplayPicpath);
//                                    Picasso.with(getActivity())
//                                            .load(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
//                                                    strSDCardPathName_temp + WelcomeActivity.profile.getTbUsers().getPicture()))
//                                            .resize(100, 100)
//                                            .centerCrop()
//                                            .into(profile_image);
                                    try {

                                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver()
                                                .openInputStream(selectedImage));
                                        profile_image.setImageBitmap(bitmap);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            src.close();
                            dst.close();
                            Log.i(TAG, "save new Photo from gallery " + destinationImagePath);
                        } else {
                            Log.i(TAG, "Photo from gallery error ");
                            if (snackbar == null || !snackbar.isShown()) {
                                snackbar = Snackbar.make(rootLayout, "ไม่สามารถใช้รูปนี้ได้"
                                        , LENGTH_INDEFINITE)
                                        .setAction(getString(R.string.ok), new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                            }
                                        });
                                snackbar.show();
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.i(TAG, "Photo from gallery error " + e.getMessage());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return;
        }
    }

    private class DialogInterfaceOnClickListener implements DialogInterface.OnClickListener {


        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            switch (which) {
                case 0:// Take Photo
                    // Do Take Photo task here

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


                    newfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), strSDCardPathName_temp + sDisplayPicpath);
                    if (newfile.exists())
                        newfile.delete();
                    try {
                        newfile.createNewFile();
                        mCurrentPhotoPath = newfile.getAbsolutePath();
                        Log.i(TAG, "mCurrentPhotoPath " + mCurrentPhotoPath);
                    } catch (IOException e) {
                    }
                    if (newfile != null) {
                        uri = Uri.fromFile(newfile);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        getActivity().startActivityForResult(Intent.createChooser(cameraIntent
                                , "เลือกแอปพลิเคชันถ่ายรูป"), REQUEST_CAMERA_OUTSIDE);
                    }
                    break;
                case 1:// Choose Existing Photo
                    // Do Pick Photo task here

                    Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    getIntent.setType("image/*");

                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*");

                    Intent chooserIntent = Intent.createChooser(getIntent, "เลือกรูปภาพ");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
                    getActivity().startActivityForResult(chooserIntent, REQUEST_GALLERY);

                    break;
//                case 2:// Choose Existing File
//                    // Do Pick file here
//                    break;
                default:
                    break;
            }
        }
    }
}
