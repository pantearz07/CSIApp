package com.scdc.csiapp.invmain;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.main.MainActivity;
import com.scdc.csiapp.tablemodel.TbMultimediaFile;

import java.util.ArrayList;
import java.util.List;


public class DiagramTabFragment extends Fragment {
    private static final String TAG = "DEBUG-DiagramTabFragment";
    SQLiteDBHelper mDbHelper;
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
    String[] updateDT, datetime;
    public static List<TbMultimediaFile> tbMultimediaFileList = null;
    String sDiagramID;
    public static String Bundle_ID = "dataid";
    public static String Bundle_TB = "datatb";
    public static String Bundle_mode = "mode";
    public static String Bundle_MediaDescription = "mode";
    public static List<TbMultimediaFile> tbDiagramFileList = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mManager = new PreferenceData(getActivity());
//        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        caseReportID = CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID;
        mFragmentManager = getActivity().getSupportFragmentManager();
        mDbHelper = new SQLiteDBHelper(getActivity());
        dbHelper = new DBHelper(getActivity());
//        mDb = mDbHelper.getWritableDatabase();
        getDateTime = new GetDateTime();
        updateDT = getDateTime.getDateTimeNow();
        datetime = getDateTime.getDateTimeCurrent();
        View viewDiagramTab = inflater.inflate(R.layout.diagram_tab_layout, container, false);

        gViewPic = (GridView) viewDiagramTab.findViewById(R.id.gridViewShowMedia);
        rootLayout = (CoordinatorLayout) viewDiagramTab.findViewById(R.id.rootLayout);

        if (CSIDataTabFragment.apiCaseScene.getTbMultimediaFiles() == null) {
            tbMultimediaFileList = new ArrayList<>();
            Log.i(TAG, "getTbMultimediaFiles null");
        } else {
            tbMultimediaFileList = CSIDataTabFragment.apiCaseScene.getTbMultimediaFiles();
            Log.i(TAG, "getTbMultimediaFiles not null");
            Log.i(TAG, "tbMultimediaFileList num1:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbMultimediaFiles().size()));
        }
        showAllPic();

        fabBtn = (FloatingActionButton) viewDiagramTab.findViewById(R.id.fabBtn);
        fabBtn.setOnClickListener(new DiagramOnClickListener());


        return viewDiagramTab;
    }

    public void showAllPic() {
        // TODO Auto-generated method stub
        tbDiagramFileList = new ArrayList<>();
        tbDiagramFileList = dbHelper.selectedMediafiles(caseReportID, "diagram");
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
                    showViewPic(tbDiagramFileList.get(position).FilePath.toString());
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
                addDialog.setIcon(R.drawable.paint);
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
                addDialog.setPositiveButton("Save",
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
                        .setNegativeButton("Cancel",
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
            String root = Environment.getExternalStorageDirectory().toString();
            String strPath = root + "/CSIFiles/Pictures/" + tbDiagramFileList.get(position).FilePath.toString();
            Log.i(TAG, strPath);

            textView.setText(tbDiagramFileList.get(position).FilePath.toString() + "\n"
                    + tbDiagramFileList.get(position).FileDescription.toString());
            textView.setVisibility(View.GONE);
            // Image Resource
            ImageView imageView = (ImageView) convertView
                    .findViewById(R.id.imgPhoto);

            Bitmap bmpSelectedImage = BitmapFactory.decodeFile(strPath);
            int width1 = bmpSelectedImage.getWidth();
            int height1 = bmpSelectedImage.getHeight();
            Log.i(TAG, "size " + width1 + " "
                    + height1);
            int width = width1 / 4;
            int height = height1 / 4;
            Bitmap resizedbitmap = Bitmap.createScaledBitmap(bmpSelectedImage, width, height, true);
            imageView.setImageBitmap(resizedbitmap);

            return convertView;

        }
    }
}