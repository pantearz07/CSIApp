package com.scdc.csiapp.invmain;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiMultimedia;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.main.MainActivity;
import com.scdc.csiapp.tablemodel.TbMultimediaFile;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class DiagramTabFragment extends Fragment {
    private static final String TAG = "DEBUG-DiagramTabFragment";
    DBHelper dbHelper;
    SQLiteDatabase mDb;
    FragmentManager mFragmentManager;
    GridView gViewPic;
    Uri uri;
    private PreferenceData mManager;
    String officialID, caseReportID;
    FloatingActionButton fabBtn;
    CoordinatorLayout rootLayout;
    String[] Cmd = {"View", "Delete"};
    String arrDataPic[][];
    DrawingDiagramFragment drawingDiagramFragment = new DrawingDiagramFragment();
    GetDateTime getDateTime;

    public static List<TbMultimediaFile> tbMultimediaFileList = null;
    public static List<ApiMultimedia> apiMultimediaList = null;
    String sDiagramID;
    public static String Bundle_ID = "dataid";
    public static String Bundle_TB = "datatb";
    public static String Bundle_mode = "mode";
    public static String Bundle_MediaDescription = "mode";
    public static List<TbMultimediaFile> tbDiagramFileList = null;
    Context mContext;
    ConnectionDetector cd;
    private static String strSDCardPathName_Pic = "/CSIFiles/";
    String defaultIP = "180.183.251.32/mcsi";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mManager = new PreferenceData(getActivity());
        caseReportID = CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID;
        mFragmentManager = getActivity().getSupportFragmentManager();
        dbHelper = new DBHelper(getActivity());
        getDateTime = new GetDateTime();
        View viewDiagramTab = inflater.inflate(R.layout.diagram_tab_layout, container, false);
        cd = new ConnectionDetector(getActivity());
        gViewPic = (GridView) viewDiagramTab.findViewById(R.id.gridViewShowMedia);
        rootLayout = (CoordinatorLayout) viewDiagramTab.findViewById(R.id.rootLayout);
        SharedPreferences sp = getActivity().getSharedPreferences(PreferenceData.PREF_IP, mContext.MODE_PRIVATE);
        defaultIP = sp.getString(PreferenceData.KEY_IP, defaultIP);

        if (CSIDataTabFragment.apiCaseScene.getApiMultimedia() == null) {
            apiMultimediaList = new ArrayList<>();
            Log.i(TAG, "apiMultimediaList null");
        } else {
            apiMultimediaList = CSIDataTabFragment.apiCaseScene.getApiMultimedia();
            Log.i(TAG, "apiMultimediaList not null");
            Log.i(TAG, "apiMultimediaList num1:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getApiMultimedia().size()));
        }
        showAllPic();

        fabBtn = (FloatingActionButton) viewDiagramTab.findViewById(R.id.fabBtn);
        fabBtn.setOnClickListener(new DiagramOnClickListener());
        if (CSIDataTabFragment.mode == "view") {
            CoordinatorLayout.LayoutParams p = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
            p.setAnchorId(View.NO_ID);
            p.width = 0;
            p.height = 0;
            fabBtn.setLayoutParams(p);
            fabBtn.hide();
        }

        return viewDiagramTab;
    }

    public void showAllPic() {
        // TODO Auto-generated method stub
        tbDiagramFileList = new ArrayList<>();

        if (CSIDataTabFragment.mode.equals("view") && CSIDataTabFragment.apiCaseScene.getMode().equals("online")) {
            Log.i(TAG, "view online tbDiagramFileList num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getApiMultimedia().size()));
            if (cd.isNetworkAvailable()) {
                for (int i = 0; i < CSIDataTabFragment.apiCaseScene.getApiMultimedia().size(); i++) {
                    if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().CaseReportID.equals(CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID)) {
                        if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FileType.equals("diagram")) {
                            tbDiagramFileList.add(CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile());
                        }
                    }
                }
                Log.i(TAG, "tbDiagramFileList " + String.valueOf(tbDiagramFileList.size()));
            } else {
                tbDiagramFileList = dbHelper.selectedMediafiles(caseReportID, "diagram");
            }
        } else {
            tbDiagramFileList = dbHelper.selectedMediafiles(caseReportID, "diagram");
            Log.i(TAG, "tbDiagramFileList offline " + String.valueOf(tbDiagramFileList.size()));
        }
        if (tbDiagramFileList != null) {
            Log.i(TAG, "gViewPic SelectDataMultimediaFile " + String.valueOf(tbDiagramFileList.size()));
            gViewPic.setVisibility(View.VISIBLE);
            gViewPic.setAdapter(new DiagramAdapter(getActivity()));
            registerForContextMenu(gViewPic);
            // OnClick
            gViewPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    Toast.makeText(
                            getActivity(),
                            "Your selected : " + tbDiagramFileList.get(position).FilePath.toString(),
                            Toast.LENGTH_SHORT).show();
//                    showViewPic(tbDiagramFileList.get(position).FilePath.toString());
                    Intent intent = new Intent(getActivity(), FullScreenPhoto.class);
                    Bundle extras = new Bundle();
                    extras.putString("photopath", tbDiagramFileList.get(position).FilePath.toString());
                    extras.putString("fileid", tbDiagramFileList.get(position).FileID.toString());
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });
        } else {
            gViewPic.setVisibility(View.GONE);
            Log.i(TAG, "gViewPic SelectDataMultimediaFile " + "Null!! ");

        }
    }

    public void showViewPic(String sPicPath) {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(getActivity(), R.style.FullHeightDialog);
        dialog.setContentView(R.layout.view_pic_dialog);
        String root = Environment.getExternalStorageDirectory().toString();
        String strPath = root + "/CSIFiles/Pictures/" + sPicPath;

        // Image Resource
        ImageView imageView = (ImageView) dialog
                .findViewById(R.id.imgPhoto);

        Bitmap bmpSelectedImage = BitmapFactory.decodeFile(strPath);
        int width = bmpSelectedImage.getWidth();
        int height = bmpSelectedImage.getHeight();
        //Matrix matrix = new Matrix();
        //matrix.postRotate(90);
        //Bitmap resizedbitmap = Bitmap.createBitmap(bmpSelectedImage, 0, 0,
        //      width, height, matrix, true);

        Bitmap resizedbitmap = Bitmap.createScaledBitmap(bmpSelectedImage, width, height, true);
        imageView.setImageBitmap(resizedbitmap);
        dialog.show();
    }

    protected ViewGroup findViewById(int layoutMediaDialog) {
        // TODO Auto-generated method stub
        return null;
    }

    private class DiagramOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view == fabBtn) {

                final AlertDialog.Builder addDialog = new AlertDialog.Builder(getActivity());
                final LayoutInflater inflaterDialog = (LayoutInflater) getActivity().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);

                View Viewlayout = inflaterDialog.inflate(R.layout.add_media_dialog,
                        (ViewGroup) findViewById(R.id.layout_media_dialog));
                addDialog.setIcon(R.drawable.ic_drawing);
                addDialog.setTitle("วาดภาพแผนผังสถานที่เกิดเหตุ");
                addDialog.setView(Viewlayout);

                String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();
                sDiagramID = "DIA_" + CurrentDate_ID[2] + CurrentDate_ID[1] + CurrentDate_ID[0] + "_" + CurrentDate_ID[3] + CurrentDate_ID[4] + CurrentDate_ID[5];
                final TextView editMediaName = (TextView) Viewlayout
                        .findViewById(R.id.editMediaName);
                editMediaName.setText(sDiagramID);

                final EditText editMediaDescription = (EditText) Viewlayout
                        .findViewById(R.id.editMediaDescription);
                editMediaDescription.setHint("คำอธิบายภาพ");

                // Button OK
                addDialog.setPositiveButton(getString(R.string.save),
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Bundle i = new Bundle();
                                i.putString(Bundle_ID, sDiagramID);
                                i.putString(Bundle_mode, "new");
                                i.putString(Bundle_MediaDescription, editMediaDescription.getText().toString());

                                drawingDiagramFragment.setArguments(i);
                                MainActivity.setFragment(drawingDiagramFragment, 1);
                            }
                        })
                        // Button Cancel
                        .setNegativeButton(getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                addDialog.create();
                addDialog.show();
            }
        }
    }

    public class DiagramAdapter extends BaseAdapter {
        private Context context;

        public DiagramAdapter(Context c) {
            // TODO Auto-generated method stub
            context = c;

        }

        public int getCount() {
            // TODO Auto-generated method stub
            return tbDiagramFileList.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.show_photos, null);
            }

            TextView textView = (TextView) convertView
                    .findViewById(R.id.txtDescPhoto);
            String strPath = strSDCardPathName_Pic + tbDiagramFileList.get(position).FilePath.toString();
            Log.i(TAG, strPath);

            textView.setText(tbDiagramFileList.get(position).FilePath.toString() + "\n"
                    + tbDiagramFileList.get(position).FileDescription.toString());
            textView.setVisibility(View.GONE);
            // Image Resource
            ImageView imageView = (ImageView) convertView
                    .findViewById(R.id.imgPhoto);

            final File curfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), strPath);
            final String filepath = "http://" + defaultIP + "/assets/csifiles/"
                    + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID + "/pictures/"
                    + tbDiagramFileList.get(position).FilePath.toString();
            if (CSIDataTabFragment.mode.equals("view") && CSIDataTabFragment.apiCaseScene.getMode().equals("online")) {
                if (cd.isNetworkAvailable()) {
                    Picasso.with(getActivity())
                            .load(filepath)
                            .resize(100, 100)
                            .centerCrop()
                            .placeholder(R.drawable.ic_imagefile)
                            .error(R.drawable.ic_imagefile)
                            .into(imageView);
                } else {

                    if (curfile.exists()) {
                        Picasso.with(getActivity())
                                .load(curfile)
                                .resize(100, 100)
                                .centerCrop()
                                .placeholder(R.drawable.ic_imagefile)
                                .error(R.drawable.ic_imagefile)
                                .into(imageView);
                    } else {
                        imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_imagefile));
                    }
                }
            } else {

                if (curfile.exists()) {
                    Picasso.with(getActivity())
                            .load(curfile)
                            .resize(100, 100)
                            .placeholder(R.drawable.ic_imagefile)
                            .error(R.drawable.ic_imagefile)
                            .centerCrop()
                            .into(imageView);
                } else {
                    if (cd.isNetworkAvailable()) {
                        Picasso.with(getActivity())
                                .load(filepath)
                                .resize(100, 100)
                                .placeholder(R.drawable.ic_imagefile)
                                .error(R.drawable.ic_imagefile)
                                .centerCrop()
                                .into(imageView);
                    } else {
                        imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_imagefile));
                    }
                }

            }

            return convertView;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showAllPic();
    }
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
        showAllPic();
    }
}