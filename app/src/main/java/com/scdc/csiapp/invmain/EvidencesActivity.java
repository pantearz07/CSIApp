package com.scdc.csiapp.invmain;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.main.GetDateTime;

import java.io.File;
import java.io.IOException;

/**
 * Created by Pantearz07 on 21/3/2559.
 */
public class EvidencesActivity extends AppCompatActivity {
    String officialID, reportID, sEvidencesID, type;
    SQLiteDBHelper mDbHelper;
    SQLiteDatabase mDb;
    private PreferenceData mManager;
    GetDateTime getDateTime;
    String[] updateDT,datetime;
    FloatingActionButton fabBtnDetails;
    CoordinatorLayout rootLayout;
    private GridView horizontal_gridView_EV_photo, horizontal_gridView_EV_video;
    private TextView txtPhoto, txtVideo;
    public static final int REQUEST_CAMERA_EV = 888;
    private String mCurrentPhotoPath;
    Uri uri;
    static String strSDCardPathName = Environment.getExternalStorageDirectory() + "/CSIFiles" + "/";
    String sPhotoID;
    String arrDataPhoto[][];
    String arrDataEV[];
    Button btn_clear_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_evidences);
        sEvidencesID = getIntent().getExtras().getString("sEvidencesID");
        type = getIntent().getExtras().getString("type");
        Log.i("sEvidencesID", sEvidencesID + type);
        mDbHelper = new SQLiteDBHelper(this);
        mDb = mDbHelper.getWritableDatabase();
        mManager = new PreferenceData(this);
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        reportID = mManager.getPreferenceData(mManager.PREF_REPORTID);

        getDateTime = new GetDateTime();
        updateDT = getDateTime.getDateTimeNow();
        datetime = getDateTime.getDateTimeCurrent();
        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
        fabBtnDetails = (FloatingActionButton) findViewById(R.id.fabBtnDetails);
        horizontal_gridView_EV_photo = (GridView) findViewById(R.id.horizontal_gridView_EV);
        horizontal_gridView_EV_video= (GridView) findViewById(R.id.horizontal_gridView_EV_video);

        txtPhoto = (TextView) findViewById(R.id.txtPhoto);
        txtVideo = (TextView) findViewById(R.id.txtVideo);
        txtVideo.setVisibility(View.GONE);
        showAllPhoto();
        final Spinner spnEvidenceType = (Spinner) findViewById(R.id.spinnerEvidenceType);
        final EditText editEvidenceType = (EditText) findViewById(R.id.editEvidenceType);
        editEvidenceType.setVisibility(View.GONE);

        spnEvidenceType
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    public void onItemSelected(
                            AdapterView<?> adapterView, View view,
                            int i, long l) {
                        // TODO Auto-generated method stub

                        if (String.valueOf(
                                spnEvidenceType.getSelectedItem()).equals(
                                "อื่นๆ")) {
                            editEvidenceType
                                    .setVisibility(View.VISIBLE);
                        }

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                        Toast.makeText(EvidencesActivity.this,
                                String.valueOf("กรุณาเลือก"),
                                Toast.LENGTH_SHORT).show();

                    }

                });
        final EditText editEvidenceNumber = (EditText)findViewById(R.id.editEvidenceNumber);
        final EditText editFindEvidenceZone = (EditText) findViewById(R.id.editFindEvidenceZone);
        final EditText editMarking = (EditText) findViewById(R.id.editMarking);
        final EditText editParceling = (EditText) findViewById(R.id.editParceling);
        final EditText editEvidencePerformed = (EditText)  findViewById(R.id.editEvidencePerformed);
        final String[] type_evidence = getResources().getStringArray(R.array.type_evidence);
        ArrayAdapter<String> adapterType_evidence = new ArrayAdapter<String>(EvidencesActivity.this,
                android.R.layout.simple_dropdown_item_1line, type_evidence);
        spnEvidenceType.setAdapter(adapterType_evidence);
        arrDataEV= mDbHelper.SelectDataEachEvidence(sEvidencesID);
        if (arrDataEV != null) {
            if (arrDataEV[2] != null) {
                for (int i = 0; i < adapterType_evidence.getCount(); i++) {
                    if (arrDataEV[2].trim().equals(adapterType_evidence.getItem(i).toString())) {
                        spnEvidenceType.setSelection(i);
                        break;
                    }else if(arrDataEV[2].trim().equals(
                            "อื่นๆ")) {
                        editEvidenceType
                                .setVisibility(View.VISIBLE);
                        editEvidenceType.setText(arrDataEV[2]);
                    }
                }
            }
            if (arrDataEV[3] != null) {
                editEvidenceNumber.setText(arrDataEV[3]);
            }
            if (arrDataEV[4] != null) {
                editFindEvidenceZone.setText(arrDataEV[4]);
            }
            if (arrDataEV[5] != null) {
                editMarking.setText(arrDataEV[5]);
            }
            if (arrDataEV[6] != null) {
                editParceling.setText(arrDataEV[6]);
            }
            if (arrDataEV[7] != null) {
                editEvidencePerformed.setText(arrDataEV[7]);
            }
        }

        fabBtnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sEvidenceTypeName, sEvidenceNumber, sFindEvidenceZone, sEvidencePerformed, sMarking, sParceling;
                sEvidenceTypeName = "";
                if (String.valueOf(spnEvidenceType
                        .getSelectedItem()) != "อื่นๆ") {
                    sEvidenceTypeName = String
                            .valueOf(spnEvidenceType
                                    .getSelectedItem());
                }else if (editEvidenceType.getText().toString()
                        .length() != 0) {
                    sEvidenceTypeName = editEvidenceType
                            .getText().toString();
                }
                sEvidenceNumber = "";
                if (editEvidenceNumber.getText().toString()
                        .length() != 0) {
                    sEvidenceNumber = editEvidenceNumber
                            .getText().toString();
                }
                sFindEvidenceZone = "";
                if (editFindEvidenceZone.getText().toString()
                        .length() != 0) {
                    sFindEvidenceZone = editFindEvidenceZone
                            .getText().toString();
                }
                sMarking = "";
                if (editMarking.getText().toString().length() != 0) {
                    sMarking = editMarking.getText().toString();
                }
                sParceling = "";
                if (editParceling.getText().toString().length() != 0) {
                    sParceling = editParceling.getText()
                            .toString();
                }
                sEvidencePerformed = "";
                if (editEvidencePerformed.getText().toString()
                        .length() != 0) {
                    sEvidencePerformed = editEvidencePerformed
                            .getText().toString();
                }


                if (type.equals("new")) {
                    saveDataFindEvidence(sEvidenceTypeName, sEvidenceNumber,
                            sFindEvidenceZone, sMarking,
                            sParceling, sEvidencePerformed);
                } else if (type.equals("update")) {
                    updateDataFindEvidence(sEvidenceTypeName, sEvidenceNumber,
                            sFindEvidenceZone, sMarking,
                            sParceling, sEvidencePerformed);
                }
                Toast.makeText(EvidencesActivity.this,
                        "เพิ่มข้อมูลเรียบร้อยเเล้ว",
                        Toast.LENGTH_LONG).show();


            }
        });

        ImageButton btnTakePhotoEV = (ImageButton) findViewById(R.id.btnTakePhotoEV);

        btnTakePhotoEV.setOnClickListener(new View.OnClickListener()

        {

            @Override
            public void onClick(View v) {
                String timeStamp = "";
                File newfile;
                createFolder("Pictures");

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                timeStamp =updateDT[0]+" "+updateDT[1];

                sPhotoID =  "IMG_" + datetime[2]+""+datetime[1]+""+datetime[0]+"_"+ datetime[3]+""+datetime[4]+""+datetime[5];
                String sPhotoPath = sPhotoID + ".jpg";
                newfile = new File(strSDCardPathName, "Pictures/" + sPhotoPath);
                if (newfile.exists())
                    newfile.delete();
                try {
                    newfile.createNewFile();
                    mCurrentPhotoPath = newfile.getAbsolutePath();
                } catch (IOException e) {
                }
                if (newfile != null) {
                    uri = Uri.fromFile(newfile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(Intent.createChooser(cameraIntent
                            , "Take a picture with"), REQUEST_CAMERA_EV);

                    String sPhotoDescription = "";
                    new saveDataMedia().execute(reportID, sPhotoID, sPhotoPath, sPhotoDescription, timeStamp, "photo");

                    Log.i("show", "PHOTO saved to Gallery!" + strSDCardPathName + "Pictures/" + " : " + sPhotoPath);
                }

            }
        });
        ImageButton btnVideoEV = (ImageButton) findViewById(R.id.btnVideoEV);
        btnVideoEV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
    }
    public void saveDataFindEvidence(String sEvidenceTypeName, String sEvidenceNumber,
                                     String sFindEvidenceZone, String sMarking, String sParceling,
                                     String sEvidencePerformed) {
        // TODO Auto-generated method stub


        long saveStatus = mDbHelper.saveDataFindEvidence(reportID, sEvidencesID,
                sEvidenceTypeName, sEvidenceNumber, sFindEvidenceZone, sMarking, sParceling,sEvidencePerformed);
        if (saveStatus <= 0) {
            Log.i("Recieve", "Error!! ");
        } else {


            Log.i("saveData Evidences", reportID + " " + sEvidencesID );
            this.finish();
        }

    }
    public void updateDataFindEvidence(String sEvidenceTypeName, String sEvidenceNumber,
                                       String sFindEvidenceZone, String sMarking, String sParceling,
                                       String sEvidencePerformed) {
        // TODO Auto-generated method stub

        long saveStatus = mDbHelper.updateDataSelectedFindEvidence( sEvidencesID,
                sEvidenceTypeName, sEvidenceNumber, sFindEvidenceZone, sMarking, sParceling,sEvidencePerformed);
        if (saveStatus <= 0) {
            Log.i("Recieve", "Error!! ");
        } else {


            Log.i("update Evidences", sEvidencesID + " " + sEvidenceTypeName );
            this.finish();
        }

    }
    public static void createFolder(String pathType) {
        File folder = new File(Environment.getExternalStorageDirectory() + "/CSIFiles/" + pathType + "/");
        try {
            // Create folder
            if (!folder.exists()) {
                folder.mkdir();
            }
        } catch (Exception ex) {
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Result media", String.valueOf(requestCode) + " " + String.valueOf(resultCode));


        if (requestCode == REQUEST_CAMERA_EV) {
            if (resultCode == RESULT_OK) {
                try {

                    Log.i("REQUEST_Photo", "Photo save");
                    showAllPhoto();
                    //showAllVideo();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (resultCode == RESULT_CANCELED) {
                //data.getData();
                Log.i("REQUEST_Photo", "media recording cancelled." + sPhotoID);
                File photosfile = new File(mCurrentPhotoPath);
                if (photosfile.exists()) {
                    photosfile.delete();
                    long saveStatus = mDbHelper.DeleteMediaFile(reportID, sPhotoID);
                    if (saveStatus <= 0) {
                        Log.i("deletephoto", "Cannot delete!! ");

                    } else {
                        long saveStatus2 = mDbHelper.DeletePhotoOfEvidences(sEvidencesID, sPhotoID);
                        if (saveStatus2 <= 0) {
                            Log.i("deletephoto", "Cannot delete!! ");

                        } else {
                            Log.i("deletephoto", "ok");
                            showAllPhoto();
                        }
                    }
                }
            } else {
                Log.i("REQUEST_Photo", "Failed to record media");
            }
        }
    }

    class saveDataMedia extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
        }

        @Override
        protected String doInBackground(String... params) {
            String arrData = "";


            long saveStatus = mDbHelper.saveDataMultimediaifile(params[0], params[1], params[5], params[2],
                    params[3], params[4]);
            if (saveStatus <= 0) {
                arrData = "error";
                Log.i("saveData ClueShown " + params[5], "Error!! ");
            } else {

                long saveStatus2 = mDbHelper.saveDataPhotoOfEvidence(sEvidencesID, params[1]);
                if (saveStatus2 <= 0) {
                    arrData = "error";
                    Log.i("saveData Evidences " + params[5], "Error!! ");
                } else {
                    arrData = "save";
                    Log.i("saveData Evidences " + sEvidencesID, params[1]);
                }
            }

            return arrData;
        }

        protected void onPostExecute(String arrData) {
            if (arrData == "save") {
                Log.i("saveData", "save");
                //showAllPhoto();
                //showAllVideo();

            } else {
                Log.i("saveData", "error");

            }
        }
    }

    public void showAllPhoto() {
        // TODO Auto-generated method stub
        int photolength = 0;
        arrDataPhoto = mDbHelper.SelectDataPhotoOfEvidences(reportID, sEvidencesID, "photo");

        if (arrDataPhoto != null) {
            Log.i("arrDataPhoto_Evidences", String.valueOf(arrDataPhoto.length));
            txtPhoto.setText("รูปภาพ  (" + String.valueOf(arrDataPhoto.length) + ")");
            photolength = arrDataPhoto.length;
            //int size=list.size();
            // Calculated single Item Layout Width for each grid element ....
            int width = 100;

            DisplayMetrics dm = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(dm);
            float density = dm.density;

            int totalWidth = (int) (width * photolength * density);
            int singleItemWidth = (int) (width * density);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    totalWidth, LinearLayout.LayoutParams.MATCH_PARENT);

            horizontal_gridView_EV_photo.setLayoutParams(params);
            // horizontal_gridView_Outside.setHorizontalSpacing(2);
            horizontal_gridView_EV_photo.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            horizontal_gridView_EV_photo.setNumColumns(photolength);

            horizontal_gridView_EV_photo.setVisibility(View.VISIBLE);
            horizontal_gridView_EV_photo.setAdapter(new PhotoAdapter(this, arrDataPhoto));
            registerForContextMenu(horizontal_gridView_EV_photo);
            // OnClick
            horizontal_gridView_EV_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    showViewPic(arrDataPhoto[position][3].toString());
                }
            });
        } else {
            horizontal_gridView_EV_photo.setVisibility(View.GONE);
            Log.i("Recieve_EV", "Null!! ");

        }
    }

    public void showViewPic(String sPicPath) {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(this,
                R.style.FullHeightDialog);
        dialog.setContentView(R.layout.view_pic_dialog);
        String root = Environment.getExternalStorageDirectory().toString();
        String strPath = root + "/CSIFiles/Pictures/" + sPicPath;

        // Image Resource
        ImageView imageView = (ImageView) dialog.findViewById(R.id.imgPhoto);

        Bitmap bmpSelectedImage = BitmapFactory.decodeFile(strPath);
        if(bmpSelectedImage!=null) {
            int width = bmpSelectedImage.getWidth();
            int height = bmpSelectedImage.getHeight();
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap resizedBitmap = Bitmap.createBitmap(bmpSelectedImage, 0, 0,
                    width, height, matrix, true);
            imageView.setImageBitmap(resizedBitmap);
        }
        dialog.show();
    }

    public class PhotoAdapter extends BaseAdapter {
        private Context context;
        private String[][] lis;

        public PhotoAdapter(Context c, String[][] li) {
            // TODO Auto-generated method stub
            context = c;
            lis = li;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return lis.length;
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
            textView.setVisibility(View.GONE);
            String root = Environment.getExternalStorageDirectory().toString();

            String strPath = root + "/CSIFiles/Pictures/"
                    + lis[position][3].toString();

            Log.i("list photo", "/CSIFiles/Pictures/" + lis[position][3].toString());

            // Image Resource
            ImageView imageView = (ImageView) convertView
                    .findViewById(R.id.imgPhoto);

            /*textView.setText(lis[position][3].toString() + "\n"
                    + lis[position][4].toString());*/
            Bitmap bmpSelectedImage = BitmapFactory.decodeFile(strPath);

            int width1 = bmpSelectedImage.getWidth();
            int height1 = bmpSelectedImage.getHeight();
            Log.i("size", width1 + " " + height1);
            int width = width1 / 13;
            int height = height1 / 13;
            Log.i("resize", width + " " + height);
            Bitmap resizedbitmap = Bitmap.createScaledBitmap(bmpSelectedImage,
                    width, height, true);
            imageView.setImageBitmap(resizedbitmap);


            // imageView.setImageBitmap(bmpSelectedImage);

            return convertView;

        }
    }

}
