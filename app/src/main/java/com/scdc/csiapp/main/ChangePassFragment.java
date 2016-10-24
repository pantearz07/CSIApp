package com.scdc.csiapp.main;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiProfile;
import com.scdc.csiapp.apimodel.ApiStatus;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;

import java.security.MessageDigest;

/**
 * Created by Pantearz07 on 21/10/2559.
 */

public class ChangePassFragment extends Fragment {

    private static final String TAG = "DEBUG-ChangePassFragment";
    CoordinatorLayout rootLayout;
    private PreferenceData mManager;
    ConnectionDetector cd;
    DBHelper dbHelper;
    GetDateTime getDateTime;
    Snackbar snackbar;
    Context mContext;
    Button btnSaveNewPassword;
    EditText edtPasswordOld, edtPasswordNew, edtPasswordNewConfirm;
    String sPasswordOld, txtPasswordOld, txtPasswordNew, txtPasswordNewConfirm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.changepass_fragment, null);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.change_pass);
        rootLayout = (CoordinatorLayout) view.findViewById(R.id.rootLayout);
        mContext = getContext();
        mManager = new PreferenceData(getActivity());
        cd = new ConnectionDetector(getActivity());
        dbHelper = new DBHelper(getActivity());
        getDateTime = new GetDateTime();

        btnSaveNewPassword = (Button) view.findViewById(R.id.btnSaveNewPassword);
        edtPasswordOld = (EditText) view.findViewById(R.id.edtPasswordOld);
        edtPasswordNew = (EditText) view.findViewById(R.id.edtPasswordNew);
        edtPasswordNewConfirm = (EditText) view.findViewById(R.id.edtPasswordNewConfirm);
        edtPasswordOld.addTextChangedListener(new ProfileTextWatcher(edtPasswordOld));
        edtPasswordNew.addTextChangedListener(new ProfileTextWatcher(edtPasswordNew));
        edtPasswordNewConfirm.addTextChangedListener(new ProfileTextWatcher(edtPasswordNewConfirm));
        if (WelcomeActivity.profile.getTbUsers() != null) {
            sPasswordOld = WelcomeActivity.profile.getTbUsers().pass;
            Log.i(TAG, "sPasswordOld " + sPasswordOld);
        }
        String txt_pass = mManager.getPreferenceData(dbHelper.COL_pass);
        Log.d(TAG, "Not Pass " + txt_pass);
        btnSaveNewPassword.setOnClickListener(new ConfirmOnClickListener());
        btnSaveNewPassword.setEnabled(true);
        return view;
    }

    public static final String md5(final String toEncrypt) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(toEncrypt.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02X", bytes[i]));
            }
            return sb.toString().toLowerCase();
        } catch (Exception exc) {
            return ""; // Impossibru!
        }
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
            if (editable == edtPasswordOld.getEditableText()) {

                txtPasswordOld = md5(editable.toString());
                Log.i(TAG, "txtPasswordOld " + txtPasswordOld);
                Log.i(TAG, "sPasswordOld " + sPasswordOld);
                if (sPasswordOld.equals(txtPasswordOld)) {
                    btnSaveNewPassword.setEnabled(true);

                } else {
                    btnSaveNewPassword.setEnabled(false);

                    Toast.makeText(getActivity(), "รหัสผ่านเก่าไม่ถูกต้อง", Toast.LENGTH_LONG).show();
                }

            } else if (editable == edtPasswordNew.getEditableText()) {

                txtPasswordNew = md5(editable.toString());
                Log.i(TAG, "txtPasswordNew " + txtPasswordNew);

            } else if (editable == edtPasswordNewConfirm.getEditableText()) {
                txtPasswordNewConfirm = md5(editable.toString());

                Log.i(TAG, "txtPasswordNewConfirm " + txtPasswordNewConfirm);

            }
        }
    }

    private class ConfirmOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view == btnSaveNewPassword) {
                if (sPasswordOld.equals(txtPasswordOld)) {
                    if (txtPasswordNew.equals(txtPasswordNewConfirm)) {
                        WelcomeActivity.profile.getTbUsers().setPass(txtPasswordNew);

                        if (cd.isNetworkAvailable()) {
                            if (WelcomeActivity.profile.getTbUsers() != null) {
                                //save to server
                                EditProfile editProfile = new EditProfile();
                                editProfile.execute(WelcomeActivity.profile);

                            }
                        } else {
                            Toast.makeText(getActivity(),
                                    getString(R.string.offline_mode),
                                    Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(getActivity(), "ยืนยันรหัสผ่านเใหม่ ไม่ตรงกัน", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "txtPasswordNewConfirm " + txtPasswordNewConfirm);
                        Log.i(TAG, "txtPasswordNew " + txtPasswordNew);
                    }
                }else {
                    Toast.makeText(getActivity(), "รหัสผ่านเก่าไม่ถูกต้อง", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "sPasswordOld " + sPasswordOld);
                        Log.i(TAG, "txtPasswordOld " + txtPasswordOld);
                    }
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

            if (apiStatus.getStatus().equalsIgnoreCase("success")) {
                Log.d(TAG, apiStatus.getData().getReason());
                boolean isSuccess = dbHelper.updateProfile(WelcomeActivity.profile);
                if (isSuccess) {

                    boolean isSuccess2 = mManager.registerUser(WelcomeActivity.profile.getTbUsers(), WelcomeActivity.profile.getTbOfficial());
                    if (isSuccess2) {
                        Toast.makeText(getActivity(),
                                getString(R.string.save_complete)
                                        + " " + WelcomeActivity.profile.getTbOfficial().id_users.toString(),
                                Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getActivity(), getString(R.string.save_error)
                                + "และบันทึก pref ไม่สำเร็จ/n" + WelcomeActivity.profile.getTbOfficial().id_users.toString(), Toast.LENGTH_LONG).show();

                    }

                } else {
                    Toast.makeText(getActivity(),
                            getString(R.string.save_error)
                                    + " " + WelcomeActivity.profile.getTbOfficial().id_users.toString(),
                            Toast.LENGTH_SHORT).show();

                }

            } else {
                Toast.makeText(getActivity(),
                        apiStatus.getData().getReason().toString()
                                + " " + WelcomeActivity.profile.getTbOfficial().id_users.toString(),
                        Toast.LENGTH_SHORT).show();


            }
        }
    }
}
