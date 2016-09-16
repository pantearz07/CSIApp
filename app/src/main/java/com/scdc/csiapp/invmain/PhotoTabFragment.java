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
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.main.ActivityResultBus;
import com.scdc.csiapp.main.ActivityResultEvent;
import com.scdc.csiapp.main.GetDateTime;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.IOException;

/**
 * Created by Pantearz07 on 14/3/2559.
 */
public class PhotoTabFragment  extends Fragment {
    public static final int REQUEST_CAMERA = 111;
    private String mCurrentPhotoPath;
    TextView txtPhotoNum;
    static String strSDCardPathName = Environment.getExternalStorageDirectory() + "/CSIFiles" + "/";
    String sPhotoID;
    SQLiteDBHelper mDbHelper;
    SQLiteDatabase mDb;
    private GridView gViewPhoto;
    Uri uri;
    private PreferenceData mManager;
    String officialID, reportID;
    String arrDataPhoto[][];
    FloatingActionButton fabBtn;
    CoordinatorLayout rootLayout;
    GetDateTime getDateTime;
    String[] updateDT,datetime;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mManager = new PreferenceData(getActivity());
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        reportID = mManager.getPreferenceData(mManager.PREF_REPORTID);
        mDbHelper = new SQLiteDBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();
        View viewPhotosTab = inflater.inflate(R.layout.photo_tab_layout, container, false);
        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        getDateTime = new GetDateTime();
        updateDT = getDateTime.getDateTimeNow();
        datetime= getDateTime.getDateTimeCurrent();
        rootLayout = (CoordinatorLayout) viewPhotosTab.findViewById(R.id.rootLayout);
        gViewPhoto = (GridView) viewPhotosTab.findViewById(R.id.gridViewPhoto);

        txtPhotoNum = (TextView) viewPhotosTab.findViewById(R.id.txtPhotoNum);
        showAllPhoto();


        fabBtn = (FloatingActionButton) viewPhotosTab.findViewById(R.id.fabBtn);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                String timeStamp = "";
                File newfile;
                createFolder("Pictures");

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                timeStamp = updateDT[0]+" "+updateDT[1];
                sPhotoID = "IMG_" + datetime[2]+""+datetime[1]+""+datetime[0]+"_"+ datetime[3]+""+datetime[4]+""+datetime[5];
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
                    getActivity().startActivityForResult(Intent.createChooser(cameraIntent
                            , "Take a picture with"), REQUEST_CAMERA);

                    String sPhotoDescription = "";
                    new saveDataMedia().execute(reportID, sPhotoID, sPhotoPath, sPhotoDescription, timeStamp, "photo");

                    Log.i("show", "PHOTO saved to Gallery!" + strSDCardPathName + "Pictures/" + " : " + sPhotoPath);
                }

            }
        });

        return viewPhotosTab;
    }

    public static void createFolder(String pathType) {
        File folder = new File(Environment.getExternalStorageDirectory() + "/CSIFiles/" + pathType + "/");
        try {
            // Create folder
            if (!folder.exists()) {
                folder.mkdir();
                Log.i("mkdir",Environment.getExternalStorageDirectory() + "/CSIFiles/" + pathType + "/");
            }else {
                Log.i("folder.exists", Environment.getExternalStorageDirectory() + "/CSIFiles/" + pathType + "/");

            }
        } catch (Exception ex) {
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
                Log.i("saveData " + params[5], "Error!! ");
            } else {
                arrData = "save";
                Log.i("saveData " + params[5], params[2]);


            }

            return arrData;
        }
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
 /*           textView.setText(lis[position][3].toString() + "\n"
                    + lis[position][4].toString());
  */
            String root = Environment.getExternalStorageDirectory().toString();
            String strPath = root + "/CSIFiles/Pictures/"
                    + lis[position][3].toString();
            Log.i("strPath ", strPath);
            // Image Resource
            ImageView imageView = (ImageView) convertView
                    .findViewById(R.id.imgPhoto);
            //load image by piccaso
           // String imgPath = "file:///CSIFiles/Pictures/"
           ///         + lis[position][3].toString();
            //file:///1234.jpg
           /* Picasso.with(getActivity())
                    .load("file:///1234.jpg")
                    //new File("CSIFiles/Pictures/"+lis[position][3].toString())
                   .resize(50, 50)
                    //.centerCrop()
                    //.error(R.drawable.user_placeholder_error)
                    .into(imageView);
*/
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Result media", String.valueOf(requestCode) + " " + String.valueOf(resultCode));


        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == getActivity().RESULT_OK) {
                try {

                    Log.i("REQUEST_Photo", "Photo save");
                    showAllPhoto();
                    //showAllVideo();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                //data.getData();
                Log.i("REQUEST_Photo", "media recording cancelled." + sPhotoID);
                File photosfile = new File(mCurrentPhotoPath);
                if (photosfile.exists()) {
                    photosfile.delete();
                    long saveStatus = mDbHelper.DeleteMediaFile(reportID, sPhotoID);
                    if (saveStatus <= 0) {
                        Log.i("deletephoto", "Cannot delete!! ");

                    } else {
                        Log.i("deletephoto", "ok");
                        showAllPhoto();
                    }
                }
            } else {
                Log.i("REQUEST_Photo", "Failed to record media");
            }
        }
    }
    public void showAllPhoto() {
        // TODO Auto-generated method stub
        arrDataPhoto = mDbHelper.SelectDataMultimediaFile(reportID, "photo");
        if (arrDataPhoto != null) {
            Log.i("arrDataPhoto", String.valueOf(arrDataPhoto.length));
            txtPhotoNum.setText(String.valueOf(arrDataPhoto.length));
            gViewPhoto.setVisibility(View.VISIBLE);
            gViewPhoto.setAdapter(new PhotoAdapter(getActivity(), arrDataPhoto));
            registerForContextMenu(gViewPhoto);
            // OnClick
            gViewPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    showViewPic(arrDataPhoto[position][3].toString());
                }
            });


        } else {
            gViewPhoto.setVisibility(View.GONE);
            Log.i("Recieve", "Null!! ");

        }
    }

    public void showViewPic(String sPicPath) {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(getActivity(),
                R.style.FullHeightDialog);
        dialog.setContentView(R.layout.view_pic_dialog);
        String root = Environment.getExternalStorageDirectory().toString();
        String strPath = root + "/CSIFiles/Pictures/" + sPicPath;

        // Image Resource
        ImageView imageView = (ImageView) dialog.findViewById(R.id.imgPhoto);

        Bitmap bmpSelectedImage = BitmapFactory.decodeFile(strPath);
        int width = bmpSelectedImage.getWidth();
        int height = bmpSelectedImage.getHeight();
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap resizedBitmap = Bitmap.createBitmap(bmpSelectedImage, 0, 0,
                width, height, matrix, true);
        imageView.setImageBitmap(resizedBitmap);
        dialog.show();
    }
    @Override
    public void onStart() {
        super.onStart();
        ActivityResultBus.getInstance().register(mActivityResultSubscriber);
    }

    @Override
    public void onStop() {
        super.onStop();
        ActivityResultBus.getInstance().unregister(mActivityResultSubscriber);
    }

    private Object mActivityResultSubscriber = new Object() {
        @Subscribe
        public void onActivityResultReceived(ActivityResultEvent event) {
            int requestCode = event.getRequestCode();
            int resultCode = event.getResultCode();
            Intent data = event.getData();
            onActivityResult(requestCode, resultCode, data);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        Log.i("onResume photo", "resume");

    }
}
