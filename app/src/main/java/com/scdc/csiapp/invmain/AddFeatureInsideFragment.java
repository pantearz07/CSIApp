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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.main.MainActivity;
import com.scdc.csiapp.tablemodel.TbSceneFeatureInSide;

/**
 * Created by Pantearz07 on 6/10/2559.
 */

public class AddFeatureInsideFragment extends Fragment {
    FloatingActionButton fabBtnDetails;
    private CoordinatorLayout rootLayout;
    private static final String TAG = "DEBUG-AddFeatureInsideFragment";
    EditText editFeatureInsideFloor, editFeatureInsideCave, editFeatureInsideClassBack, editFeatureInsideClassLeft, editFeatureInsideClassCenter,
            editFeatureInsideClassRight, editFeatureInsideClassFront;
    private GridView horizontal_gridView_Inside, horizontal_gridView_Inside_video;
    private TextView txtPhoto, txtVideo;
    GetDateTime getDateTime;
    TbSceneFeatureInSide tbSceneFeatureInSide;
    DBHelper dbHelper;
    DetailsTabFragment detailsTabFragment;
    String sFeatureInsideID,mode;
    public AddFeatureInsideFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.add_feature_inside, container, false);
        //call the main activity set tile method

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("เพิ่มลักษณะภายใน");
        Log.i(TAG, "CaseReportID " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID);
        Bundle args = getArguments();
        sFeatureInsideID = args.getString(DetailsTabFragment.Bundle_InsideID);
        mode = args.getString(DetailsTabFragment.Bundle_Inside_mode);
        Log.i(TAG, "sFeatureInsideID " + sFeatureInsideID);
        dbHelper = new DBHelper(getActivity());
        tbSceneFeatureInSide = new TbSceneFeatureInSide();
        detailsTabFragment = new DetailsTabFragment();
        getDateTime = new GetDateTime();
        Log.i(TAG, "tbSceneFeatureInSideList num1:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbSceneFeatureInSide().size()));

        rootLayout = (CoordinatorLayout) view.findViewById(R.id.rootLayout);
        fabBtnDetails = (FloatingActionButton) view.findViewById(R.id.fabBtnDetails);

        editFeatureInsideFloor = (EditText) view.findViewById(R.id.editFeatureInsideFloor);
        editFeatureInsideCave = (EditText) view.findViewById(R.id.editFeatureInsideCave);
        editFeatureInsideClassBack = (EditText) view.findViewById(R.id.editFeatureInsideClassBack);
        editFeatureInsideClassLeft = (EditText) view.findViewById(R.id.editFeatureInsideClassLeft);
        editFeatureInsideClassCenter = (EditText) view.findViewById(R.id.editFeatureInsideClassCenter);
        editFeatureInsideClassRight = (EditText) view.findViewById(R.id.editFeatureInsideClassRight);
        editFeatureInsideClassFront = (EditText) view.findViewById(R.id.editFeatureInsideClassFront);

        editFeatureInsideFloor.addTextChangedListener(new InsideTextWatcher(editFeatureInsideFloor));
        editFeatureInsideCave.addTextChangedListener(new InsideTextWatcher(editFeatureInsideCave));
        editFeatureInsideClassBack.addTextChangedListener(new InsideTextWatcher(editFeatureInsideClassBack));
        editFeatureInsideClassCenter.addTextChangedListener(new InsideTextWatcher(editFeatureInsideClassCenter));
        editFeatureInsideClassRight.addTextChangedListener(new InsideTextWatcher(editFeatureInsideClassRight));
        editFeatureInsideClassFront.addTextChangedListener(new InsideTextWatcher(editFeatureInsideClassFront));
        editFeatureInsideClassLeft.addTextChangedListener(new InsideTextWatcher(editFeatureInsideClassLeft));

        if(mode == "edit"){
            tbSceneFeatureInSide = (TbSceneFeatureInSide) args.getSerializable(DetailsTabFragment.Bundle_InsideTB);
            editFeatureInsideFloor.setText(tbSceneFeatureInSide.getFloorNo());
            editFeatureInsideCave.setText(tbSceneFeatureInSide.getCaveNo());
            editFeatureInsideClassBack.setText(tbSceneFeatureInSide.getBackInside());
            editFeatureInsideClassCenter.setText(tbSceneFeatureInSide.getCenterInside());
            editFeatureInsideClassRight.setText(tbSceneFeatureInSide.getRightInside());
            editFeatureInsideClassFront.setText(tbSceneFeatureInSide.getFrontInside());
            editFeatureInsideClassLeft.setText(tbSceneFeatureInSide.getLeftInside());
        }
        horizontal_gridView_Inside = (GridView) view.findViewById(R.id.horizontal_gridView_Inside);
        horizontal_gridView_Inside_video = (GridView) view.findViewById(R.id.horizontal_gridView_Inside_video);
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
                tbSceneFeatureInSide.FeatureInsideID = sFeatureInsideID;
                tbSceneFeatureInSide.CaseReportID = CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID;
                DetailsTabFragment.tbSceneFeatureInSideList.add(tbSceneFeatureInSide);
                CSIDataTabFragment.apiCaseScene.setTbSceneFeatureInSide(DetailsTabFragment.tbSceneFeatureInSideList);
                Log.i(TAG, "tbSceneFeatureInSideList num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbSceneFeatureInSide().size()));
                boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
                if (isSuccess) {
                    MainActivity.setFragment(detailsTabFragment, 0);
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
            if (s == editFeatureInsideFloor.getEditableText()) {
                tbSceneFeatureInSide.FloorNo = editFeatureInsideFloor.getText().toString();
                Log.i(TAG, "FloorNo " + tbSceneFeatureInSide.FloorNo);
            } else if (s == editFeatureInsideCave.getEditableText()) {
                tbSceneFeatureInSide.CaveNo = editFeatureInsideCave.getText().toString();
                Log.i(TAG, "CaveNo " + tbSceneFeatureInSide.CaveNo);
            } else if (s == editFeatureInsideClassBack.getEditableText()) {
                tbSceneFeatureInSide.BackInside = editFeatureInsideClassBack.getText().toString();
                Log.i(TAG, "BackInside " + tbSceneFeatureInSide.BackInside);
            } else if (s == editFeatureInsideClassLeft.getEditableText()) {
                tbSceneFeatureInSide.LeftInside = editFeatureInsideClassLeft.getText().toString();
                Log.i(TAG, "LeftInside " + tbSceneFeatureInSide.LeftInside);
            } else if (s == editFeatureInsideClassCenter.getEditableText()) {
                tbSceneFeatureInSide.CenterInside = editFeatureInsideClassCenter.getText().toString();
                Log.i(TAG, "CenterInside " + tbSceneFeatureInSide.CenterInside);
            } else if (s == editFeatureInsideClassRight.getEditableText()) {
                tbSceneFeatureInSide.RightInside = editFeatureInsideClassRight.getText().toString();
                Log.i(TAG, "RightInside " + tbSceneFeatureInSide.RightInside);
            } else if (s == editFeatureInsideClassFront.getEditableText()) {
                tbSceneFeatureInSide.FrontInside = editFeatureInsideClassFront.getText().toString();
                Log.i(TAG, "FrontInside " + tbSceneFeatureInSide.FrontInside);
            }
        }
    }

}