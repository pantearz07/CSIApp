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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.tablemodel.TbGatewayCriminal;
import com.scdc.csiapp.tablemodel.TbResultScene;

/**
 * Created by Pantearz07 on 6/10/2559.
 */

public class AddGatewayFragment extends Fragment {
    FloatingActionButton fabBtnDetails;
    private CoordinatorLayout rootLayout;
    private static final String TAG = "DEBUG-AddGatewayFragment";
    private GridView horizontal_gridView_GC, horizontal_gridView_GC_video;
    private TextView txtPhoto, txtVideo;
    GetDateTime getDateTime;
    DBHelper dbHelper;
    ResultTabFragment resultTabFragment;
    String sRSID, mode, typeid;
    int position = 0;
    TbResultScene tbResultScene;
    TbGatewayCriminal tbGatewayCriminal;
    AutoCompleteTextView editGatewayCriminalDetails;
    Button btn_clear_txt;

    public AddGatewayFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.add_gatewaycriminal, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("เพิ่มทางเข้าออกของคนร้าย");

        //call the main activity set tile method
        Bundle args = getArguments();
        sRSID = args.getString(ResultTabFragment.Bundle_ID);
        mode = args.getString(ResultTabFragment.Bundle_mode);
        typeid = args.getString(ResultTabFragment.Bundle_RSType);
        Log.i(TAG, "sRSID " + sRSID + " typeid " + typeid);
        Log.i(TAG, "CaseReportID " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID);
        Log.i(TAG, typeid + " GatewayCriminalsList num1:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbGatewayCriminals().size()));


        dbHelper = new DBHelper(getActivity());
        tbResultScene = new TbResultScene();
        tbGatewayCriminal = new TbGatewayCriminal();
        resultTabFragment = new ResultTabFragment();
        getDateTime = new GetDateTime();

        rootLayout = (CoordinatorLayout) view.findViewById(R.id.rootLayout);
        fabBtnDetails = (FloatingActionButton) view.findViewById(R.id.fabBtnDetails);


        editGatewayCriminalDetails = (AutoCompleteTextView) view.findViewById(R.id.editGatewayCriminalDetails);
//        String[] mGateClueArray;
//        mGateClueArray = getResources().getStringArray(
//                R.array.gate_clue);
//        ArrayAdapter<String> adapterGateClue = new ArrayAdapter<String>(
//                getActivity(),
//                android.R.layout.simple_dropdown_item_1line,
//                mGateClueArray);
//        editGatewayCriminalDetails.setThreshold(1);
//        editGatewayCriminalDetails.setAdapter(adapterGateClue);
        btn_clear_txt = (Button) view.findViewById(R.id.btn_clear_txt);
        editGatewayCriminalDetails.addTextChangedListener(new RSTextWatcher(editGatewayCriminalDetails));
        if (mode == "edit") {
            position = args.getInt(ResultTabFragment.Bundle_Index, -1);
            Log.i(TAG, "position " + position);
            tbGatewayCriminal = (TbGatewayCriminal) args.getSerializable(ResultTabFragment.Bundle_TB);
            editGatewayCriminalDetails.setText(tbGatewayCriminal.getRSDetail());
        }
        horizontal_gridView_GC = (GridView) view.findViewById(R.id.horizontal_gridView_GC);
        horizontal_gridView_GC_video = (GridView) view.findViewById(R.id.horizontal_gridView_GC_video);
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

                tbGatewayCriminal.RSID = sRSID;
                tbGatewayCriminal.RSTypeID = typeid;
                tbGatewayCriminal.CaseReportID = CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID;
                ResultTabFragment.tbGatewayCriminals.add(tbGatewayCriminal);
                //add list
                CSIDataTabFragment.apiCaseScene.setTbGatewayCriminals(ResultTabFragment.tbGatewayCriminals);
                Log.i(TAG, typeid + " tbGatewayCriminals num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbGatewayCriminals().size()));
                boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
                if (isSuccess) {
                    if (mode == "edit") {
                        CSIDataTabFragment.apiCaseScene.getTbGatewayCriminals().remove(position);
                        Log.i(TAG, typeid + " tbGatewayCriminals remove num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbGatewayCriminals().size()));

                    } else {
                        Log.i(TAG, typeid + " tbGatewayCriminals num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbGatewayCriminals().size()));

                    }
                    getActivity().onBackPressed();
                }

            }
            if (v == btn_clear_txt) {
                editGatewayCriminalDetails.setText("");
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
            if (mEditText == editGatewayCriminalDetails) {
                btn_clear_txt.setVisibility(View.VISIBLE);
                btn_clear_txt.setOnClickListener(new InsideOnClickListener());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s == editGatewayCriminalDetails.getEditableText()) {
                tbGatewayCriminal.RSDetail = editGatewayCriminalDetails.getText().toString();
                Log.i(TAG, "editGatewayCriminalDetails " + tbGatewayCriminal.RSDetail);
            }
        }
    }
}

