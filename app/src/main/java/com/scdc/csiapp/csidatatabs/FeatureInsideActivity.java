package com.scdc.csiapp.csidatatabs;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.main.GetDateTime;

import java.io.File;
import java.io.IOException;

/**
 * Created by Pantearz07 on 16/3/2559.
 */
public class FeatureInsideActivity extends AppCompatActivity {
    SQLiteDBHelper mDbHelper;
    SQLiteDatabase mDb;
    private PreferenceData mManager;
    String officialID, reportID, sFeatureInsideID, type;

    FloatingActionButton fabBtnDetails;
    CoordinatorLayout rootLayout;
    private GridView horizontal_gridView_Inside, horizontal_gridView_Inside_video;
    private TextView txtPhoto, txtVideo;
    public static final int REQUEST_CAMERA_INSIDE = 444;
    private String mCurrentPhotoPath;
    Uri uri;
    static String strSDCardPathName = Environment.getExternalStorageDirectory() + "/CSIFiles" + "/";
    String sPhotoID;
    String arrDataPhoto[][];
    String arrDataFeatureInside[];
    GetDateTime getDateTime;
    String[] updateDT,datetime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_feature_inside);
        mDbHelper = new SQLiteDBHelper(this);
        mDb = mDbHelper.getWritableDatabase();
        mManager = new PreferenceData(this);
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        reportID = mManager.getPreferenceData(mManager.PREF_REPORTID);
        sFeatureInsideID = getIntent().getExtras().getString("featureid");
        type = getIntent().getExtras().getString("type");
        Log.i("sFeatureInsideID", sFeatureInsideID+type);
        getDateTime = new GetDateTime();
        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
        fabBtnDetails = (FloatingActionButton) findViewById(R.id.fabBtnDetails);
        horizontal_gridView_Inside = (GridView) findViewById(R.id.horizontal_gridView_Inside);
        horizontal_gridView_Inside_video = (GridView) findViewById(R.id.horizontal_gridView_Inside_video);
        txtPhoto = (TextView) findViewById(R.id.txtPhoto);
        txtVideo = (TextView) findViewById(R.id.txtVideo);
        txtVideo.setVisibility(View.GONE);
        showAllPhoto();
        getDateTime = new GetDateTime();
        updateDT = getDateTime.getDateTimeNow();
        datetime = getDateTime.getDateTimeCurrent();
        final EditText editFeatureInsideFloor = (EditText) findViewById(R.id.editFeatureInsideFloor);
        final TextView editNoti1 = (TextView) findViewById(R.id.editNoti1);
        final TextView editNoti3 = (TextView) findViewById(R.id.editNoti3);
        editFeatureInsideFloor
                .addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s,
                                              int start, int before, int count) {
                        // TODO Auto-generated method stub
                        editNoti1.setVisibility(View.GONE);

                        editNoti3.setVisibility(View.GONE);
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s,
                                                  int start, int count, int after) {
                        // TODO Auto-generated method stub
                        editNoti1.setVisibility(View.VISIBLE);

                        editNoti3.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // TODO Auto-generated method stub
                        if (s.toString().length() != 0) {
                            editNoti1.setVisibility(View.GONE);

                            editNoti3.setVisibility(View.GONE);
                        } else {
                            editNoti1.setVisibility(View.VISIBLE);

                            editNoti3.setVisibility(View.VISIBLE);
                        }
                    }
                });
        final EditText editFeatureInsideCave = (EditText) findViewById(R.id.editFeatureInsideCave);
        final TextView editNoti2 = (TextView) findViewById(R.id.editNoti2);
        final TextView editNoti4 = (TextView) findViewById(R.id.editNoti4);
        editFeatureInsideCave.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                // TODO Auto-generated method stub
                editNoti2.setVisibility(View.GONE);
                editNoti4.setVisibility(View.GONE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub
                editNoti2.setVisibility(View.VISIBLE);
                editNoti4.setVisibility(View.VISIBLE);

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (s.toString().length() != 0) {
                    editNoti2.setVisibility(View.GONE);
                    editNoti4.setVisibility(View.GONE);

                } else {
                    editNoti2.setVisibility(View.VISIBLE);
                    editNoti4.setVisibility(View.VISIBLE);
                }
            }
        });
        final EditText editFeatureInsideClassBack = (EditText) findViewById(R.id.editFeatureInsideClassBack);
        final EditText editFeatureInsideClassLeft = (EditText) findViewById(R.id.editFeatureInsideClassLeft);
        final EditText editFeatureInsideClassCenter = (EditText) findViewById(R.id.editFeatureInsideClassCenter);
        final EditText editFeatureInsideClassRight = (EditText) findViewById(R.id.editFeatureInsideClassRight);
        final EditText editFeatureInsideClassFront = (EditText) findViewById(R.id.editFeatureInsideClassFront);
        arrDataFeatureInside = mDbHelper.SelectDataFeatureInside(sFeatureInsideID);
        if (arrDataFeatureInside != null) {
            editFeatureInsideFloor.setText(arrDataFeatureInside[2]);
            editFeatureInsideCave.setText(arrDataFeatureInside[3]);
            editFeatureInsideClassFront.setText(arrDataFeatureInside[4]);
            editFeatureInsideClassLeft.setText(arrDataFeatureInside[5]);
            editFeatureInsideClassRight.setText(arrDataFeatureInside[6]);
            editFeatureInsideClassBack.setText(arrDataFeatureInside[7]);
            editFeatureInsideClassCenter.setText(arrDataFeatureInside[8]);

        }
        //final Button btnSaveInside = (Button) findViewById(R.id.btnSaveInside);
        fabBtnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editFeatureInsideFloor.getText().toString()
                        .equals("")
                        || editFeatureInsideCave.getText()
                        .toString().equals("")) {


                } else {

									/*	*/
                    String sFloorNo, sCaveNo, sFrontInside, sLeftInside, sRightInside, sBackInside, sCenterInside;

                    sFloorNo = "";
                    sCaveNo = "";
                    sFrontInside = "";
                    sLeftInside = "";
                    sRightInside = "";
                    sBackInside = "";
                    sCenterInside = "";
                    if (editFeatureInsideFloor.getText()
                            .toString().length() != 0) {
                        sFloorNo = editFeatureInsideFloor
                                .getText().toString();
                    }

                    if (editFeatureInsideCave.getText()
                            .toString().length() != 0) {
                        sCaveNo = editFeatureInsideCave
                                .getText().toString();
                    }
                    if (editFeatureInsideClassBack.getText()
                            .toString().length() != 0) {
                        sBackInside = editFeatureInsideClassBack
                                .getText().toString();
                    }
                    if (editFeatureInsideClassLeft.getText()
                            .toString().length() != 0) {
                        sLeftInside = editFeatureInsideClassLeft
                                .getText().toString();
                    }
                    if (editFeatureInsideClassCenter.getText()
                            .toString().length() != 0) {
                        sCenterInside = editFeatureInsideClassCenter
                                .getText().toString();
                    }
                    if (editFeatureInsideClassRight.getText()
                            .toString().length() != 0) {
                        sRightInside = editFeatureInsideClassRight
                                .getText().toString();
                    }
                    if (editFeatureInsideClassFront.getText()
                            .toString().length() != 0) {
                        sFrontInside = editFeatureInsideClassFront
                                .getText().toString();
                    }
                    if (type.equals("new")) {
                        saveDataAddFeatureInside(reportID,
                                sFloorNo, sCaveNo, sFrontInside,
                                sLeftInside, sRightInside,
                                sBackInside, sCenterInside);
                    } else if (type.equals("update")) {
                        updateDataAddFeatureInside(sFloorNo, sCaveNo, sFrontInside,
                                sLeftInside, sRightInside,
                                sBackInside, sCenterInside);
                    }

                }

            }
        });

        Button btnTakePhotoInside = (Button) findViewById(R.id.btnTakePhotoInside);
        btnTakePhotoInside.setOnClickListener(new View.OnClickListener() {

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
                            , "Take a picture with"), REQUEST_CAMERA_INSIDE);

                    String sPhotoDescription = "";
                    new saveDataMedia().execute(reportID, sPhotoID, sPhotoPath, sPhotoDescription, timeStamp, "photo");

                    Log.i("show", "PHOTO saved to Gallery!" + strSDCardPathName + "Pictures/" + " : " + sPhotoPath);
                }

            }
        });
        Button btnVideoInside = (Button) findViewById(R.id.btnVideoInside);
        btnVideoInside.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
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


        if (requestCode == REQUEST_CAMERA_INSIDE) {
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
                        long saveStatus2 = mDbHelper.DeletePhotoOfInside(sFeatureInsideID, sPhotoID);
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
                Log.i("saveData Inside " + params[5], "Error!! ");
            } else {

                long saveStatus2 = mDbHelper.saveDataPhotoOfInside(sFeatureInsideID, params[1]);
                if (saveStatus2 <= 0) {
                    arrData = "error";
                    Log.i("saveData Inside " + params[5], "Error!! ");
                } else {
                    arrData = "save";
                    Log.i("saveData Inside " + sFeatureInsideID, params[1]);
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
        arrDataPhoto = mDbHelper.SelectDataPhotoOfInside(reportID, sFeatureInsideID, "photo");
        // Log.i("arrDataPhoto_Inside",String.valueOf(arrDataPhoto.length));
        if (arrDataPhoto != null) {
            Log.i("arrDataPhoto_Inside", String.valueOf(arrDataPhoto.length));
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

            horizontal_gridView_Inside.setLayoutParams(params);
            // horizontal_gridView_Outside.setHorizontalSpacing(2);
            horizontal_gridView_Inside.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            horizontal_gridView_Inside.setNumColumns(photolength);

            horizontal_gridView_Inside.setVisibility(View.VISIBLE);
            horizontal_gridView_Inside.setAdapter(new PhotoAdapter(this, arrDataPhoto));
            registerForContextMenu(horizontal_gridView_Inside);
            // OnClick
            horizontal_gridView_Inside.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    showViewPic(arrDataPhoto[position][3].toString());
                }
            });
        } else {
            horizontal_gridView_Inside.setVisibility(View.GONE);
            Log.i("Recieve_inside", "Null!! ");

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

    public void saveDataAddFeatureInside(String reportID, String sFloorNo,
                                         String sCaveNo, String sFrontInside, String sLeftInside,
                                         String sRightInside, String sBackInside, String sCenterInside) {
        // TODO Auto-generated method stub
        // String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();
        //String sFeatureInsideID = "IN" + CurrentDate_ID[2] + CurrentDate_ID[1] + CurrentDate_ID[0] + CurrentDate_ID[4] + CurrentDate_ID[5];

        int floorno = 0, caveno = 0;
        if (sFloorNo.length() != 0) {
            floorno = Integer.parseInt(sFloorNo);
        }
        if (sCaveNo.length() != 0) {

            caveno = Integer.parseInt(sCaveNo);
        }
        long saveStatus = mDbHelper.saveDataFeatureInside(reportID,
                sFeatureInsideID, floorno, caveno, sFrontInside, sLeftInside,
                sRightInside, sBackInside, sCenterInside);
        if (saveStatus <= 0) {
            Log.i("saveDataFeatureInside", "Error!! ");
        } else {
            Log.i("saveDataFeatureInside", "OK!! ");
            Log.i("saveData FeatureInside", sFeatureInsideID + " " + sFrontInside
                    + " " + sLeftInside + " " + sRightInside + " " + sBackInside + " " + sCenterInside);
            this.onBackPressed();
        }

    }

    public void updateDataAddFeatureInside(String sFloorNo,
                                         String sCaveNo, String sFrontInside, String sLeftInside,
                                         String sRightInside, String sBackInside, String sCenterInside) {
        // TODO Auto-generated method stub
        // String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();
        //String sFeatureInsideID = "IN" + CurrentDate_ID[2] + CurrentDate_ID[1] + CurrentDate_ID[0] + CurrentDate_ID[4] + CurrentDate_ID[5];

        int floorno = 0, caveno = 0;
        if (sFloorNo.length() != 0) {
            floorno = Integer.parseInt(sFloorNo);
        }
        if (sCaveNo.length() != 0) {

            caveno = Integer.parseInt(sCaveNo);
        }
        long saveStatus = mDbHelper.updateDataFeatureInside(
                sFeatureInsideID, floorno, caveno, sFrontInside, sLeftInside,
                sRightInside, sBackInside, sCenterInside);
        if (saveStatus <= 0) {
            Log.i("update FeatureInside", "Error!! ");
        } else {
            Log.i("update FeatureInside", "OK!! ");

            this.finish();
        }

    }

}
