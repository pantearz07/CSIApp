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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.tablemodel.TbPropertyLoss;

/**
 * Created by Pantearz07 on 6/10/2559.
 */

public class AddPropertyLossFragment extends Fragment {
    FloatingActionButton fabBtnDetails;
    private CoordinatorLayout rootLayout;
    private static final String TAG = "DEBUG-AddPropertyLossFragment";
    EditText editPropertyLossName, editPropertyLossAmount, editPropertyLossPosition, editPropertyInsurance;
    AutoCompleteTextView autoPropertyLossUnit;
    private GridView horizontal_gridView_PL_photo, horizontal_gridView_PL_video;
    private TextView txtPhoto, txtVideo;
    GetDateTime getDateTime;
    TbPropertyLoss tbPropertyLoss;
    DBHelper dbHelper;
    ResultTabFragment resultTabFragment;
    String sPLID, mode;
    int position = 0;

    public AddPropertyLossFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.add_propertyloss, container, false);
        //call the main activity set tile method

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("เพิ่มทรัพย์สินที่หายไป");
        Log.i(TAG, "CaseReportID " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID);
        Bundle args = getArguments();
        sPLID = args.getString(ResultTabFragment.Bundle_ID);
        mode = args.getString(ResultTabFragment.Bundle_mode);
        Log.i(TAG, "sPLID " + sPLID);
        dbHelper = new DBHelper(getActivity());
        tbPropertyLoss = new TbPropertyLoss();
        resultTabFragment = new ResultTabFragment();
        getDateTime = new GetDateTime();
        Log.i(TAG, "tbPropertyLossesList num1:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbSceneFeatureInSide().size()));

        rootLayout = (CoordinatorLayout) view.findViewById(R.id.rootLayout);
        fabBtnDetails = (FloatingActionButton) view.findViewById(R.id.fabBtnDetails);

        editPropertyLossName = (EditText) view.findViewById(R.id.editPropertyLossName);
        editPropertyLossAmount = (EditText) view.findViewById(R.id.editPropertyLossAmount);
        autoPropertyLossUnit = (AutoCompleteTextView) view.findViewById(R.id.autoPropertyLossUnit);
        String[] mUnitArray;
        mUnitArray = getResources().getStringArray(
                R.array.property_unit);
        ArrayAdapter<String> adapterUnit = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_dropdown_item_1line, mUnitArray);

        autoPropertyLossUnit.setThreshold(1);
        autoPropertyLossUnit.setAdapter(adapterUnit);
        editPropertyLossPosition = (EditText) view.findViewById(R.id.editPropertyLossPosition);
        editPropertyInsurance = (EditText) view.findViewById(R.id.editPropertyInsurance);

        editPropertyLossName.addTextChangedListener(new PropertyTextWatcher(editPropertyLossName));
        editPropertyLossAmount.addTextChangedListener(new PropertyTextWatcher(editPropertyLossAmount));
        autoPropertyLossUnit.addTextChangedListener(new PropertyTextWatcher(autoPropertyLossUnit));
        editPropertyLossPosition.addTextChangedListener(new PropertyTextWatcher(editPropertyLossPosition));
        editPropertyInsurance.addTextChangedListener(new PropertyTextWatcher(editPropertyInsurance));

        if (mode == "edit") {
            position = args.getInt(ResultTabFragment.Bundle_Index, -1);
            Log.i(TAG, "position " + position);
            tbPropertyLoss = (TbPropertyLoss) args.getSerializable(ResultTabFragment.Bundle_TB);
            editPropertyLossName.setText(tbPropertyLoss.getPropertyLossName());
            editPropertyLossAmount.setText(tbPropertyLoss.getPropertyLossNumber());
            autoPropertyLossUnit.setText(tbPropertyLoss.getPropertyLossUnit());
            editPropertyLossPosition.setText(tbPropertyLoss.getPropertyLossPosition());
            editPropertyInsurance.setText(tbPropertyLoss.getPropInsurance());

        }
        horizontal_gridView_PL_photo = (GridView) view.findViewById(R.id.horizontal_gridView_PL_photo);
        horizontal_gridView_PL_video = (GridView) view.findViewById(R.id.horizontal_gridView_PL_video);
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
//                if(DetailsTabFragment.tbSceneFeatureInSideList == null){
//                    DetailsTabFragment.tbSceneFeatureInSideList = new ArrayList<>();
//                }
                tbPropertyLoss.PropertyLossID = sPLID;
                tbPropertyLoss.CaseReportID = CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID;
                ResultTabFragment.tbPropertyLosses.add(tbPropertyLoss);
                CSIDataTabFragment.apiCaseScene.setTbPropertyLosses(ResultTabFragment.tbPropertyLosses);
                Log.i(TAG, "tbPropertyLosses num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbPropertyLosses().size()));
                boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
                if (isSuccess) {
                    if(mode == "edit"){
                        CSIDataTabFragment.apiCaseScene.getTbPropertyLosses().remove(position);
                        Log.i(TAG, "tbPropertyLosses remove num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbPropertyLosses().size()));

                    }else{
                        Log.i(TAG, "tbPropertyLosses num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbPropertyLosses().size()));

                    }
                    getActivity().onBackPressed();
                }

            }
        }
    }

    private class PropertyTextWatcher implements android.text.TextWatcher {
        private EditText mEditText;

        public PropertyTextWatcher(EditText editText) {
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
            if (s == editPropertyLossName.getEditableText()) {
                tbPropertyLoss.PropertyLossName = editPropertyLossName.getText().toString();
                Log.i(TAG, "PropertyLossName " + tbPropertyLoss.PropertyLossName);
            } else if (s == editPropertyLossAmount.getEditableText()) {
                tbPropertyLoss.PropertyLossNumber = editPropertyLossAmount.getText().toString();
                Log.i(TAG, "PropertyLossNumber " + tbPropertyLoss.PropertyLossNumber);
            } else if (s == autoPropertyLossUnit.getEditableText()) {
                tbPropertyLoss.PropertyLossUnit = autoPropertyLossUnit.getText().toString();
                Log.i(TAG, "PropertyLossUnit " + tbPropertyLoss.PropertyLossUnit);
            } else if (s == editPropertyLossPosition.getEditableText()) {
                tbPropertyLoss.PropertyLossPosition = editPropertyLossPosition.getText().toString();
                Log.i(TAG, "PropertyLossPosition " + tbPropertyLoss.PropertyLossPosition);
            } else if (s == editPropertyInsurance.getEditableText()) {
                tbPropertyLoss.PropInsurance = editPropertyInsurance.getText().toString();
                Log.i(TAG, "PropInsurance " + tbPropertyLoss.PropInsurance);
            }
        }
    }

}