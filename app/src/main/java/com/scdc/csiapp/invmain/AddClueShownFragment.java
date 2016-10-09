package com.scdc.csiapp.invmain;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.tablemodel.TbClueShown;
import com.scdc.csiapp.tablemodel.TbResultScene;

/**
 * Created by Pantearz07 on 6/10/2559.
 */

public class AddClueShownFragment extends Fragment {
    FloatingActionButton fabBtnDetails;
    private CoordinatorLayout rootLayout;
    private static final String TAG = "DEBUG-AddClueShownFragment";
    private GridView horizontal_gridView_ClueShown, horizontal_gridView_ClueShown_video;
    private TextView txtPhoto, txtVideo;
    GetDateTime getDateTime;
    DBHelper dbHelper;
    ResultTabFragment resultTabFragment;
    String sRSID, mode, typeid;
    int position = 0;
    TbResultScene tbResultScene;
    TbClueShown tbClueShown;
    AutoCompleteTextView editClueShownPositionDetail;
    Button btn_clear_txt;

    public AddClueShownFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.add_clueshown, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("เพิ่มร่องรอยที่ปรากฏ");

        //call the main activity set tile method
        Bundle args = getArguments();
        sRSID = args.getString(ResultTabFragment.Bundle_ID);
        mode = args.getString(ResultTabFragment.Bundle_mode);
        typeid = args.getString(ResultTabFragment.Bundle_RSType);
        Log.i(TAG, "sRSID " + sRSID + " typeid " + typeid);
        Log.i(TAG, "CaseReportID " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID);
        Log.i(TAG, typeid + " tbClueShownsesList num1:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbClueShowns().size()));


        dbHelper = new DBHelper(getActivity());
        tbResultScene = new TbResultScene();
        tbClueShown = new TbClueShown();
        resultTabFragment = new ResultTabFragment();
        getDateTime = new GetDateTime();

        rootLayout = (CoordinatorLayout) view.findViewById(R.id.rootLayout);
        fabBtnDetails = (FloatingActionButton) view.findViewById(R.id.fabBtnDetails);


        editClueShownPositionDetail = (AutoCompleteTextView) view.findViewById(R.id.editClueShownPositionDetail);
        String[] mGateClueArray;
        mGateClueArray = getResources().getStringArray(
                R.array.gate_clue);
        ArrayAdapter<String> adapterGateClue = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_dropdown_item_1line,
                mGateClueArray);
        editClueShownPositionDetail.setThreshold(1);
        editClueShownPositionDetail.setAdapter(adapterGateClue);
        btn_clear_txt = (Button) view.findViewById(R.id.btn_clear_txt);
        editClueShownPositionDetail.addTextChangedListener(new RSTextWatcher(editClueShownPositionDetail));
        if (mode == "edit") {
            position = args.getInt(ResultTabFragment.Bundle_Index, -1);
            Log.i(TAG, "position " + position);
            tbClueShown = (TbClueShown) args.getSerializable(ResultTabFragment.Bundle_TB);
            editClueShownPositionDetail.setText(tbClueShown.getRSDetail());
        }
        horizontal_gridView_ClueShown= (GridView) view.findViewById(R.id.horizontal_gridView_ClueShown);
        horizontal_gridView_ClueShown_video = (GridView) view.findViewById(R.id.horizontal_gridView_ClueShown_video);
        txtPhoto = (TextView) view.findViewById(R.id.txtPhoto);
        txtVideo = (TextView) view.findViewById(R.id.txtVideo);
        txtVideo.setVisibility(View.GONE);

        fabBtnDetails.setOnClickListener(new InsideOnClickListener());

        return view;
    }

    private class InsideOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v == fabBtnDetails) {
                final String dateTimeCurrent[] = getDateTime.getDateTimeCurrent();

                CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];

                tbClueShown.RSID = sRSID;
                tbClueShown.RSTypeID = typeid;
                tbClueShown.CaseReportID = CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID;
                ResultTabFragment.tbClueShowns.add(tbClueShown);
                CSIDataTabFragment.apiCaseScene.setTbClueShowns(ResultTabFragment.tbClueShowns);
                Log.i(TAG, typeid + " tbClueShowns num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbClueShowns().size()));
                boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
                if (isSuccess) {
                    if (mode == "edit") {
                        CSIDataTabFragment.apiCaseScene.getTbClueShowns().remove(position);
                        Log.i(TAG, typeid + " tbClueShowns remove num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbClueShowns().size()));

                    } else {
                        Log.i(TAG, typeid + " tbClueShowns num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbClueShowns().size()));

                    }
                    getActivity().onBackPressed();
                }

            }
            if (v == btn_clear_txt) {
                editClueShownPositionDetail.setText("");
            }
        }
    }

    private class RSTextWatcher implements android.text.TextWatcher {
        private EditText mEditText;

        public RSTextWatcher(EditText editText) {
            mEditText = editText;
        }


        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (mEditText == editClueShownPositionDetail) {
                btn_clear_txt.setVisibility(View.VISIBLE);
                btn_clear_txt.setOnClickListener(new InsideOnClickListener());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s == editClueShownPositionDetail.getEditableText()) {
                tbClueShown.RSDetail = editClueShownPositionDetail.getText().toString();
                Log.i(TAG, "editClueShownPositionDetail " + tbClueShown.RSDetail);
            }
        }
    }
}

