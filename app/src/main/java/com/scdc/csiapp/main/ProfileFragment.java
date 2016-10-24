package com.scdc.csiapp.main;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiProfile;
import com.scdc.csiapp.apimodel.ApiStatus;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;

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
    String officialID;
    TextView tMemberID, tAccessType, txtChangePassword;
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

        edtPosition = (EditText) view.findViewById(R.id.edtPosition);
        edtFirstName = (EditText) view.findViewById(R.id.edtFirstName);
        edtLastName = (EditText) view.findViewById(R.id.edtLastName);
        edtEmail = (EditText) view.findViewById(R.id.edtEmail);
        editTextPhone = (EditText) view.findViewById(R.id.editTextPhone);
        edtUsername.addTextChangedListener(new ProfileTextWatcher(edtUsername));
//        edtPassword.addTextChangedListener(new ProfileTextWatcher(edtPassword));
        edtFirstName.addTextChangedListener(new ProfileTextWatcher(edtFirstName));
        edtLastName.addTextChangedListener(new ProfileTextWatcher(edtLastName));
        edtEmail.addTextChangedListener(new ProfileTextWatcher(edtEmail));
        edtPosition.addTextChangedListener(new ProfileTextWatcher(edtPosition));
        editTextPhone.addTextChangedListener(new ProfileTextWatcher(editTextPhone));
        tAccessType = (TextView) view.findViewById(R.id.txtStatus);

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
            tAccessType.setText(WelcomeActivity.profile.getTbOfficial().AccessType);
        }
        if (WelcomeActivity.profile.getTbUsers() != null) {
            edtUsername.setText(WelcomeActivity.profile.getTbUsers().id_users);
            edtPassword.setText(WelcomeActivity.profile.getTbUsers().pass);
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
            } else if (editable == edtPassword.getEditableText()) {
//                WelcomeActivity.profile.getTbUsers().setPass(md5(edtPassword.getText().toString()));
//                Log.i(TAG, "edtPassword " + WelcomeActivity.profile.getTbUsers().pass);
            } else if (editable == edtPosition.getEditableText()) {
                WelcomeActivity.profile.getTbOfficial().setSubPossition(edtPosition.getText().toString());
                Log.i(TAG, "SubPossition " + WelcomeActivity.profile.getTbOfficial().SubPossition);
            }
        }
    }



    private class ProfileOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view == txtChangePassword) {
                MainActivity.setFragment(changePassFragment, 1);
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

    class EditProfile extends AsyncTask<ApiProfile, Void, ApiStatus> {

        @Override
        protected ApiStatus doInBackground(ApiProfile... apiProfiles) {
            return WelcomeActivity.api.editProfile(apiProfiles[0]);
        }

        @Override
        protected void onPostExecute(ApiStatus apiStatus) {
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
                                            + " " + WelcomeActivity.profile.getTbOfficial().id_users.toString()
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

            } else {
                if (snackbar == null || !snackbar.isShown()) {
                    snackbar = Snackbar.make(rootLayout, apiStatus.getData().getReason().toString()
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
