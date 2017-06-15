package com.scdc.csiapp.invmain;


import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.channels.FileChannel;
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
    TextView txtPhotoNum;

    GetDateTime getDateTime;
    public static final int REQUEST_DRAW = 22;
    public static final int REQUEST_LOAD_IMAGE = 2;
    public static final int REQUEST_GALLERY = 222;
    String imageEncoded;
    List<String> imagesEncodedList;
    GetPathUri getPathUri;
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
    TextView editMediaName;
    EditText editMediaDescription;
    String edtMediaDescription, timeStamp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mManager = new PreferenceData(getActivity());
        caseReportID = CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID;
        mFragmentManager = getActivity().getSupportFragmentManager();
        dbHelper = new DBHelper(getActivity());
        getDateTime = new GetDateTime();
        getPathUri = new GetPathUri();
        View viewDiagramTab = inflater.inflate(R.layout.diagram_tab_layout, container, false);
        cd = new ConnectionDetector(getActivity());
        gViewPic = (GridView) viewDiagramTab.findViewById(R.id.gridViewShowMedia);
        rootLayout = (CoordinatorLayout) viewDiagramTab.findViewById(R.id.rootLayout);
        SharedPreferences sp = getActivity().getSharedPreferences(PreferenceData.PREF_IP, mContext.MODE_PRIVATE);
        defaultIP = sp.getString(PreferenceData.KEY_IP, defaultIP);
        txtPhotoNum = (TextView) viewDiagramTab.findViewById(R.id.txtPhotoNum);

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
            txtPhotoNum.setText(String.valueOf(tbDiagramFileList.size()));
            gViewPic.setVisibility(View.VISIBLE);
            gViewPic.setAdapter(new DiagramAdapter(getActivity()));
            registerForContextMenu(gViewPic);
            // OnClick
            gViewPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("images", (Serializable) tbDiagramFileList);
                    bundle.putInt("position", position);

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                    newFragment.setTargetFragment(DiagramTabFragment.this, REQUEST_LOAD_IMAGE);
                    newFragment.setArguments(bundle);
                    newFragment.show(ft, "slideshow");
                }
            });
        } else {
            gViewPic.setVisibility(View.GONE);
            Log.i(TAG, "gViewPic SelectDataMultimediaFile " + "Null!! ");

        }
    }


    protected ViewGroup findViewById(int layoutMediaDialog) {
        // TODO Auto-generated method stub
        return null;
    }

    private class DiagramOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view == fabBtn) {
                String title = getString(R.string.importphoto);
                CharSequence[] itemlist = {getString(R.string.drawing),
                        getString(R.string.gallery)
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(R.drawable.ic_camera_add);
                builder.setTitle(title);
                builder.setItems(itemlist, new DialogInterfaceOnClickListener());
                AlertDialog alert = builder.create();
                alert.setCancelable(true);
                alert.show();
            }
        }
    }

    private class DialogInterfaceOnClickListener implements DialogInterface.OnClickListener {


        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            switch (which) {
                case 0:// Take Photo
                    // Do Take Photo task here
                    drawingDiagram();
                    break;
                case 1:// Choose Existing Photo
                    // Do Pick Photo task here
                    pickFromGallery();

                    break;
                default:
                    break;
            }
        }
    }

    public static void createFolder() {
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), strSDCardPathName_Pic);
        try {
            // Create folder
            if (!folder.exists()) {
                folder.mkdir();
                Log.i("mkdir", folder.getAbsolutePath());
            } else {
                Log.i("folder.exists", folder.getAbsolutePath());

            }
        } catch (Exception ex) {
        }

    }

    private void pickFromGallery() {
        createFolder();
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "เลือกรูปภาพ"), REQUEST_GALLERY);
    }

    private void drawingDiagram() {
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
        editMediaName = (TextView) Viewlayout
                .findViewById(R.id.editMediaName);
        editMediaName.setText(sDiagramID);
        editMediaDescription = (EditText) Viewlayout
                .findViewById(R.id.editMediaDescription);
        editMediaDescription.setHint("คำอธิบายภาพ");
        // Button OK
        addDialog.setPositiveButton(getString(R.string.save),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        edtMediaDescription = editMediaDescription.getText().toString();
                        dialog.dismiss();
                        Bundle i = new Bundle();
                        i.putString(Bundle_ID, sDiagramID);
                        i.putString(Bundle_mode, "new");
                        i.putString(Bundle_MediaDescription, edtMediaDescription);
                        DrawingDiagramFragment drawingDiagramFragment = new DrawingDiagramFragment();
                        drawingDiagramFragment.setTargetFragment(DiagramTabFragment.this, REQUEST_DRAW);
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
            if(tbDiagramFileList.get(position).FileDescription == null ||
                    tbDiagramFileList.get(position).FileDescription.length() == 0){
                textView.setVisibility(View.GONE);
            }else {
                textView.setText(tbDiagramFileList.get(position).FileDescription.toString());
            }
//            textView.setVisibility(View.GONE);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        showAllPic();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "Result media " + String.valueOf(requestCode) + " " + String.valueOf(resultCode));
        // รับข้อมูลจากอัลบั้มภาพ และบันทึกภาพลงแอพ
        if (requestCode == REQUEST_GALLERY) {
            if (resultCode == getActivity().RESULT_OK && null != data) {
                try {
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    imagesEncodedList = new ArrayList<String>();
                    if (data.getData() != null) {
                        Uri mImageUri = data.getData();
                        // Get the cursor getFilepath
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            imageEncoded = getPathUri.getPath(getActivity(), mImageUri);
                        } else {
                            imageEncoded = getFilepath(filePathColumn, mImageUri);
                        }
                        Log.i(TAG, "REQUEST_GALLERY " + imageEncoded);
                        saveToMyAlbum(imageEncoded);
                    } else { //ถ้าเลือกหลายรูป
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            if (data.getClipData() != null) {
                                ClipData mClipData = data.getClipData();
                                for (int i = 0; i < mClipData.getItemCount(); i++) {
                                    //Multiple images
                                    ClipData.Item item = mClipData.getItemAt(i);
                                    Uri uri = item.getUri();
                                    // Get the cursor getFilepath
                                    imageEncoded = getPathUri.getPath(getActivity(), uri);
//                                    Log.v(TAG, "REQUEST_GALLERY [" + i + "] " + imageEncoded);

                                    if (imageEncoded != null) {
                                        imagesEncodedList.add(imageEncoded);
                                        saveToMyAlbum(imageEncoded);
                                    }
                                }
                                Log.v(TAG, "REQUEST_GALLERY Selected Images   :" + " imagesEncodedList :" + imagesEncodedList.size());
                            }
                        } else {
                            Toast.makeText(getActivity(), "ไม่สามารถเลือกหลายรูปได้", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }

                    showAllPic();
                    Log.i(TAG, "REQUEST_GALLERY");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // After Cancel code.
                Log.i(TAG, "Cancel REQUEST_GALLERY");
            } else {
                Log.i(TAG, "Failed to REQUEST_GALLERY");
            }

        }
        if (requestCode == REQUEST_LOAD_IMAGE) {
            if (resultCode == getActivity().RESULT_OK) {
                try {
                    showAllPic();
                    Log.i(TAG, "RESULT_OK");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // After Cancel code.
                Log.i(TAG, "Cancel REQUEST_LOAD_IMAGE");
            } else {
                Log.i(TAG, "Failed to REQUEST_LOAD_IMAGE");
            }

        }
    }

    public String getFilepath(String[] filePathColumn, Uri uri) {
        // Get the cursor
        Cursor cursor = getActivity().getContentResolver().query(uri,
                filePathColumn, null, null, null);
        // Move to first row
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        imageEncoded = cursor.getString(columnIndex);

        cursor.close();
        return imageEncoded;
    }

    private void saveToMyAlbum(String imageEncoded) {
        try {
            File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File datadest = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String sPhotoID = "", sImageType = "";
            String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();
            sPhotoID = "DIA_" + CurrentDate_ID[2] + CurrentDate_ID[1] + CurrentDate_ID[0] + "_" + CurrentDate_ID[3] + CurrentDate_ID[4] + CurrentDate_ID[5] + CurrentDate_ID[6];
            timeStamp = CurrentDate_ID[0] + "-" + CurrentDate_ID[1] + "-" + CurrentDate_ID[2] + " " + CurrentDate_ID[3] + ":" + CurrentDate_ID[4] + ":" + CurrentDate_ID[5];
            sImageType = imageEncoded.substring(imageEncoded.lastIndexOf("."));
            Log.i(TAG, "sPhotoID " + sPhotoID + " sImageType " + sImageType);
            if (sd.canWrite()) {
//                        String sourceImagePath = "/path/to/source/file.jpg";
                String destinationImagePath = strSDCardPathName_Pic + sPhotoID + sImageType;
                File source = new File(imageEncoded);

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
//                                source.delete();
                        Log.i(TAG, "source.delete ");
                        saveToListDB(sPhotoID, sImageType);

                    }
                    src.close();
                    dst.close();
                    Log.i(TAG, "save new Photo from gallery " + destinationImagePath);
                } else {
                    Log.i(TAG, "Photo from gallery error ");
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Photo from gallery error " + e.getMessage());
        }
    }

    // save ข้อมูลรูปภาพไว้ใน list table และ sqlite
    private void saveToListDB(String PhotoID, String sImageType) {
        try {
            List<ApiMultimedia> apiMultimediaList = new ArrayList<>();
            ApiMultimedia apiMultimedia = new ApiMultimedia();
            TbMultimediaFile tbMultimediaFile = new TbMultimediaFile();
            tbMultimediaFile.CaseReportID = CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID;
            tbMultimediaFile.FileID = PhotoID;
            tbMultimediaFile.FileDescription = "แผนผัง";
            tbMultimediaFile.FileType = "diagram";
            tbMultimediaFile.FilePath = PhotoID + sImageType;
            tbMultimediaFile.Timestamp = timeStamp;
            apiMultimedia.setTbMultimediaFile(tbMultimediaFile);

            apiMultimediaList.add(apiMultimedia);
            CSIDataTabFragment.apiCaseScene.getApiMultimedia().add(apiMultimedia);
//                    CSIDataTabFragment.apiCaseScene.setApiMultimedia(apiMultimediaList);
            Log.i(TAG, "apiMultimediaList " + String.valueOf(CSIDataTabFragment.apiCaseScene.getApiMultimedia().size()));
            boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
            if (isSuccess) {
                Log.i(TAG, "PHOTO saved to Gallery!  : " + PhotoID + sImageType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}