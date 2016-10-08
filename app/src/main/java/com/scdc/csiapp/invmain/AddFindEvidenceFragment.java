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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.main.MainActivity;
import com.scdc.csiapp.tablemodel.TbFindEvidence;

/**
 * Created by Pantearz07 on 6/10/2559.
 */

public class AddFindEvidenceFragment extends Fragment {
    FloatingActionButton fabBtnDetails;
    private CoordinatorLayout rootLayout;
    private static final String TAG = "DEBUG-AddFindEvidenceFragment";
    EditText editEvidenceType, editEvidenceNumber, editFindEvidenceZone, editMarking, editParceling,
            editEvidencePerformed;
    Spinner spnEvidenceType;
    private GridView horizontal_gridView_EV_photo, horizontal_gridView_EV_video;
    private TextView txtPhoto, txtVideo;
    GetDateTime getDateTime;
    TbFindEvidence tbFindEvidence;
    DBHelper dbHelper;
    ResultTabFragment resultTabFragment;
    String sEVID,mode,sSceneInvestID;
    String[] type_evidence;
    boolean oldEvidenceType = false;
    public AddFindEvidenceFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.add_evidences, container, false);
        //call the main activity set tile method

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("เพิ่มวัตถุพยาน");
        Log.i(TAG, "CaseReportID " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID);
        Bundle args = getArguments();
        sEVID = args.getString(ResultTabFragment.Bundle_ID);
        mode = args.getString(ResultTabFragment.Bundle_mode);

        Log.i(TAG, "sEVID " + sEVID );
        dbHelper = new DBHelper(getActivity());
        tbFindEvidence = new TbFindEvidence();
        resultTabFragment = new ResultTabFragment();
        getDateTime = new GetDateTime();
        Log.i(TAG, "tbFindEvidence num1:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbFindEvidences().size()));

        rootLayout = (CoordinatorLayout) view.findViewById(R.id.rootLayout);
        fabBtnDetails = (FloatingActionButton) view.findViewById(R.id.fabBtnDetails);

        editEvidenceType = (EditText) view.findViewById(R.id.editEvidenceType);
        editEvidenceType.setVisibility(View.GONE);
        spnEvidenceType = (Spinner) view.findViewById(R.id.spinnerEvidenceType);
        type_evidence = getResources().getStringArray(R.array.type_evidence);
        ArrayAdapter<String> adapterType_evidence = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, type_evidence);
        spnEvidenceType.setAdapter(adapterType_evidence);
        spnEvidenceType.setOnItemSelectedListener(new EvidenceOnItemSelectedListener());
        spnEvidenceType.setOnTouchListener(new EvidenceOnItemSelectedListener());

        editEvidenceNumber = (EditText) view.findViewById(R.id.editEvidenceNumber);
        editFindEvidenceZone = (EditText) view.findViewById(R.id.editFindEvidenceZone);
        editMarking = (EditText) view.findViewById(R.id.editMarking);
        editParceling = (EditText) view.findViewById(R.id.editParceling);
        editEvidencePerformed = (EditText) view.findViewById(R.id.editEvidencePerformed);

        editEvidenceNumber.addTextChangedListener(new InsideTextWatcher(editEvidenceNumber));
        editFindEvidenceZone.addTextChangedListener(new InsideTextWatcher(editFindEvidenceZone));
        editMarking.addTextChangedListener(new InsideTextWatcher(editMarking));
        editParceling.addTextChangedListener(new InsideTextWatcher(editParceling));
        editEvidencePerformed.addTextChangedListener(new InsideTextWatcher(editEvidencePerformed));
        editEvidenceType.addTextChangedListener(new InsideTextWatcher(editEvidenceType));
        if(mode == "new"){
            sSceneInvestID = args.getString(ResultTabFragment.Bundle_SceneInvestID);
            Log.i(TAG, "sSceneInvestID " + sSceneInvestID );
        }
        if(mode == "edit"){
            tbFindEvidence = (TbFindEvidence) args.getSerializable(ResultTabFragment.Bundle_TB);
            sSceneInvestID = tbFindEvidence.getSceneInvestID();
            Log.i(TAG, "sSceneInvestID " + sSceneInvestID );
            editEvidenceNumber.setText(tbFindEvidence.getEvidenceNumber());
            editFindEvidenceZone.setText(tbFindEvidence.getFindEvidenceZone());
            editMarking.setText(tbFindEvidence.getMarking());
            editParceling.setText(tbFindEvidence.getParceling());
            editEvidencePerformed.setText(tbFindEvidence.getEvidencePerformed());

            if (tbFindEvidence.getEvidenceTypeName() != null) {
                for (int i = 0; i < type_evidence.length; i++) {
                    if (String.valueOf(type_evidence[i]).equals(tbFindEvidence.EvidenceTypeName)) {

                        spnEvidenceType.setSelection(i);
                        oldEvidenceType = true;
                        Log.i(TAG, "getEvidenceTypeName1" + tbFindEvidence.getEvidenceTypeName());
                        editEvidenceType
                                .setVisibility(View.GONE);

                    } else {
                        Log.i(TAG, "getEvidenceTypeName2" + tbFindEvidence.getEvidenceTypeName());
                        spnEvidenceType.setSelection(i);
                        oldEvidenceType = true;
                        editEvidenceType
                                .setVisibility(View.VISIBLE);
                        editEvidenceType.setText(tbFindEvidence.getEvidenceTypeName());
                    }


                }
            }
        }
        horizontal_gridView_EV_photo = (GridView) view.findViewById(R.id.horizontal_gridView_EV);
        horizontal_gridView_EV_video = (GridView) view.findViewById(R.id.horizontal_gridView_EV_video);
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

                tbFindEvidence.FindEvidenceID = sEVID;
                tbFindEvidence.SceneInvestID = sSceneInvestID;
                tbFindEvidence.CaseReportID = CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID;
                ResultTabFragment.tbFindEvidences.add(tbFindEvidence);
                CSIDataTabFragment.apiCaseScene.setTbFindEvidences(ResultTabFragment.tbFindEvidences);
                Log.i(TAG, "tbFindEvidences num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbFindEvidences().size()));
                Log.i(TAG,"getEvidenceTypeName send"+tbFindEvidence.getEvidenceTypeName());
                boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
                if (isSuccess) {
                    MainActivity.setFragment(resultTabFragment, 0);
                }

            }
        }
    }

    private class InsideTextWatcher implements android.text.TextWatcher {
        private EditText mEditText;

        public InsideTextWatcher(EditText editText) {
            mEditText = editText;
        }


        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s == editEvidenceNumber.getEditableText()) {
                tbFindEvidence.EvidenceNumber = editEvidenceNumber.getText().toString();
                Log.i(TAG, "EvidenceNumber " + tbFindEvidence.EvidenceNumber);
            } else if (s == editFindEvidenceZone.getEditableText()) {
                tbFindEvidence.FindEvidenceZone = editFindEvidenceZone.getText().toString();
                Log.i(TAG, "FindEvidenceZone " + tbFindEvidence.FindEvidenceZone);
            } else if (s == editMarking.getEditableText()) {
                tbFindEvidence.Marking = editMarking.getText().toString();
                Log.i(TAG, "Marking " + tbFindEvidence.Marking);
            } else if (s == editParceling.getEditableText()) {
                tbFindEvidence.Parceling = editParceling.getText().toString();
                Log.i(TAG, "Parceling " + tbFindEvidence.Parceling);
            } else if (s == editEvidencePerformed.getEditableText()) {
                tbFindEvidence.EvidencePerformed = editEvidencePerformed.getText().toString();
                Log.i(TAG, "EvidencePerformed " + tbFindEvidence.EvidencePerformed);
            } else if (s == editEvidenceType.getEditableText()) {
                tbFindEvidence.EvidenceTypeName = editEvidenceType.getText().toString();
                Log.i(TAG, "EvidenceTypeName edittext " + tbFindEvidence.EvidenceTypeName);
            }
        }
    }

    private class EvidenceOnItemSelectedListener implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if(view == spnEvidenceType){
                oldEvidenceType = false;
            }
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
            switch (parent.getId()) {
                case R.id.spinnerEvidenceType:
                    if(oldEvidenceType == false){
                        if (String.valueOf(type_evidence[i]).equals("อื่นๆ")) {
                            editEvidenceType.setVisibility(View.VISIBLE);
                        }else {
                            editEvidenceType.setVisibility(View.GONE);
                            tbFindEvidence.EvidenceTypeName = String.valueOf(type_evidence[i]);
                            Log.i(TAG, "EvidenceTypeName " + tbFindEvidence.EvidenceTypeName);
                        }
                    }
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            switch (parent.getId()) {
                case R.id.spinnerEvidenceType:

                        tbFindEvidence.EvidenceTypeName = String.valueOf(type_evidence[0]);
                        Log.i(TAG, "EvidenceTypeName " + tbFindEvidence.EvidenceTypeName);

                    break;
            }
        }
    }
}