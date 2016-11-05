package com.scdc.csiapp.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
    String officialID, sDisplayPicpath;
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
    public static final int REQUEST_CAMERA_OUTSIDE = 0;
    public static final int REQUEST_GALLERY = 1;
    private static String strSDCardPathName_temp = Environment.getExternalStorageDirectory() + "/CSIFiles" + "/temp/";
    private static String strSDCardPathName_temps = Environment.getExternalStorageDirectory() + "/CSIFiles" + "/temp/temps/";

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
        if (WelcomeActivity.profile.getTbUsers() != null) {
            edtUsername.setText(WelcomeActivity.profile.getTbUsers().id_users);
            edtPassword.setText(WelcomeActivity.profile.getTbUsers().pass);
        }
        profile_image = (ImageView) view.findViewById(R.id.profile_image);
        change_display = (TextView) view.findViewById(R.id.change_display);
        change_display.setOnClickListener(new ProfileOnClickListener());
        if (WelcomeActivity.profile.getTbUsers().getPicture() == null || WelcomeActivity.profile.getTbUsers().getPicture().equals("")) {

//            Picasso.with(getActivity())
//                    .load(R.drawable.avatar)
//                    .resize(76, 76)
//                    .centerCrop()
//                    .into(profile_image);
        } else {

            File avatarfile = new File(strSDCardPathName_temp + WelcomeActivity.profile.getTbUsers().getPicture());
            if (avatarfile.exists()) {
                Picasso.with(getActivity())
                        .load(new File(strSDCardPathName_temp + WelcomeActivity.profile.getTbUsers().getPicture()))
                        .resize(50, 50)
                        .centerCrop()
                        .into(profile_image);
            } else {

//                Picasso.with(getActivity())
//                        .load(R.drawable.avatar)
//                        .resize(76, 76)
//                        .centerCrop()
//                        .into(profile_image);
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
                WelcomeActivity.profile.getTbOfficial().setId_users(edtUsername.getText().toString());
                WelcomeActivity.profile.getTbUsers().setId_users(edtUsername.getText().toString());
                Log.i(TAG, "id_users " + WelcomeActivity.profile.getTbOfficial().id_users);
            }
//            else if (editable == edtPosition.getEditableText()) {
//                WelcomeActivity.profile.getTbOfficial().setSubPossition(edtPosition.getText().toString());
//                Log.i(TAG, "SubPossition " + WelcomeActivity.profile.getTbOfficial().SubPossition);
//            }
        }
    }


    private class ProfileOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view == txtChangePassword) {
                MainActivity.setFragment(changePassFragment, 1);
            }
            if (view == change_display) {

                File folder = new File(strSDCardPathName_temp);
                try {
                    // Create folder
                    if (!folder.exists()) {
                        folder.mkdir();
                        Log.i(TAG, "mkdir" + strSDCardPathName_temp);
                    } else {
                        Log.i(TAG, "folder.exists" + strSDCardPathName_temp);

                    }
                } catch (Exception ex) {
                    Log.i(TAG, "mkdir" + ex.getMessage());
                }
                File folder_temps = new File(strSDCardPathName_temps);
                try {
                    // Create folder
                    if (!folder_temps.exists()) {
                        folder_temps.mkdir();
                        Log.i(TAG, "mkdir" + strSDCardPathName_temps);
                    } else {
                        Log.i(TAG, "folder.exists" + strSDCardPathName_temps);

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
                if (cd.isNetworkAvailable()) {
                    if (WelcomeActivity.profile.getTbOfficial() != null) {
                        //save to server
                        EditProfile editProfile = new EditProfile();
                        editProfile.execute(WelcomeActivity.profile);

                    }
                } else {

                    if (WelcomeActivity.profile.getTbOfficial() != null) {
                        boolean isSuccess = dbHelper.updateProfile(WelcomeActivity.profile);
                        if (isSuccess) {
                            boolean isSuccess2 = mManager.registerUser(WelcomeActivity.profile.getTbUsers(), WelcomeActivity.profile.getTbOfficial());
                            if (isSuccess2) {
                                if (snackbar == null || !snackbar.isShown()) {
                                    snackbar = Snackbar.make(rootLayout, getString(R.string.offline_mode)
                                                    + "/n" + getString(R.string.save_complete)
                                                    + "/n" + WelcomeActivity.profile.getTbOfficial().id_users.toString()
                                            , Snackbar.LENGTH_INDEFINITE)
                                            .setAction(getString(R.string.ok), new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                }
                                            });
                                    snackbar.show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "บันทึก pref ไม่สำเร็จ", Toast.LENGTH_LONG).show();
                                if (snackbar == null || !snackbar.isShown()) {
                                    snackbar = Snackbar.make(rootLayout, getString(R.string.save_error)
                                                    + "และบันทึก pref ไม่สำเร็จ/n" + WelcomeActivity.profile.getTbOfficial().id_users.toString()
                                            , Snackbar.LENGTH_INDEFINITE)
                                            .setAction(getString(R.string.ok), new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {


                                                }
                                            });
                                    snackbar.show();
                                }
                            }

                        } else {
                            if (snackbar == null || !snackbar.isShown()) {
                                snackbar = Snackbar.make(rootLayout, getString(R.string.save_error)
                                                + " " + WelcomeActivity.profile.getTbOfficial().id_users.toString()
                                        , Snackbar.LENGTH_INDEFINITE)
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

            Log.d(TAG, apiStatus.getStatus());
            if (apiStatus.getStatus().equalsIgnoreCase("success")) {
                Log.d(TAG, apiStatus.getData().getReason());
                boolean isSuccess = dbHelper.updateProfile(WelcomeActivity.profile);
                if (isSuccess) {

                    boolean isSuccess2 = mManager.registerUser(WelcomeActivity.profile.getTbUsers(), WelcomeActivity.profile.getTbOfficial());
                    if (isSuccess2) {
                        if (snackbar == null || !snackbar.isShown()) {
                            snackbar = Snackbar.make(rootLayout, getString(R.string.save_complete)
                                            + "\n" + apiStatus.getData().getResult().toString()
                                    , Snackbar.LENGTH_INDEFINITE)
                                    .setAction(getString(R.string.ok), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                        }
                                    });
                            snackbar.show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "บันทึก pref ไม่สำเร็จ", Toast.LENGTH_LONG).show();
                        if (snackbar == null || !snackbar.isShown()) {
                            snackbar = Snackbar.make(rootLayout, getString(R.string.save_error)
                                            + "และบันทึก pref ไม่สำเร็จ/n" + WelcomeActivity.profile.getTbOfficial().id_users.toString()
                                    , Snackbar.LENGTH_INDEFINITE)
                                    .setAction(getString(R.string.ok), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {


                                        }
                                    });
                            snackbar.show();
                        }
                    }

                } else {
                    if (snackbar == null || !snackbar.isShown()) {
                        snackbar = Snackbar.make(rootLayout, getString(R.string.save_error)
                                , Snackbar.LENGTH_INDEFINITE)
                                .setAction(getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


                                    }
                                });
                        snackbar.show();
                    }
                }

            } else {
                if (snackbar == null || !snackbar.isShown()) {
                    snackbar = Snackbar.make(rootLayout, apiStatus.getData().getReason().toString()
                                    + "\n " + apiStatus.getData().getResult().toString()
                            , Snackbar.LENGTH_INDEFINITE)
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
                        File sd = Environment.getExternalStorageDirectory();
                        File data1 = Environment.getExternalStorageDirectory();
                        if (sd.canWrite()) {
                            String sourceImagePath = "/CSIFiles/temp/temps/" + sDisplayPicpath;
                            String destinationImagePath = "/CSIFiles/temp/" + sDisplayPicpath;
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
                                    boolean isSuccess = dbHelper.updateProfile(WelcomeActivity.profile);
                                    if (isSuccess) {
                                        Log.i(TAG, "OfficialDisplayPic :" + String.valueOf(WelcomeActivity.profile.getTbOfficial().OfficialDisplayPic));
                                        Log.i(TAG, "PHOTO saved to Gallery!" + strSDCardPathName_temp + sDisplayPicpath);
                                        Picasso.with(getActivity())
                                                .load(new File(strSDCardPathName_temp + WelcomeActivity.profile.getTbUsers().getPicture()))
                                                .resize(76, 76)
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
                    File sd = Environment.getExternalStorageDirectory();
                    File datadest = Environment.getExternalStorageDirectory();
                    if (sd.canWrite()) {
//                        String sourceImagePath = "/path/to/source/file.jpg";
                        String destinationImagePath = "/CSIFiles/temp/" + sDisplayPicpath;
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
                                source.delete();
                                Log.i(TAG, "source.delete ");
                                WelcomeActivity.profile.getTbOfficial().setOfficialDisplayPic(sDisplayPicpath);
                                WelcomeActivity.profile.getTbUsers().setPicture(sDisplayPicpath);
                                boolean isSuccess = dbHelper.updateProfile(WelcomeActivity.profile);
                                if (isSuccess) {
                                    Log.i(TAG, "OfficialDisplayPic :" + String.valueOf(WelcomeActivity.profile.getTbOfficial().OfficialDisplayPic));
                                    Log.i(TAG, "PHOTO saved to Gallery!" + strSDCardPathName_temp + sDisplayPicpath);
                                    Picasso.with(getActivity())
                                            .load(new File(strSDCardPathName_temp + WelcomeActivity.profile.getTbUsers().getPicture()))
                                            .resize(76, 76)
                                            .centerCrop()
                                            .into(profile_image);
                                }
                            }
                            src.close();
                            dst.close();
                            Log.i(TAG, "save new Photo from gallery " + destinationImagePath);
                        } else {
                            Log.i(TAG, "Photo from gallery error ");
                            if (snackbar == null || !snackbar.isShown()) {
                                snackbar = Snackbar.make(rootLayout, "ไม่สามารถใช้รูปนี้ได้"
                                        , Snackbar.LENGTH_INDEFINITE)
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


                    newfile = new File(strSDCardPathName_temps + sDisplayPicpath);
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
                    getIntent.setType("image/jpeg");

                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/jpeg");

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
