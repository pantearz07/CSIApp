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
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.main.GetDateTime;


public class DiagramTabFragment extends Fragment {
    SQLiteDBHelper mDbHelper;
    SQLiteDatabase mDb;
    FragmentManager mFragmentManager;
    GridView gViewPic;
    Uri uri;
    private PreferenceData mManager;
    String officialID, reportID;
    FloatingActionButton fabBtn;
    CoordinatorLayout rootLayout;
    String[] Cmd = { "View", "Delete" };
    String arrDataPic[][];
    DrawingDiagramFragment drawingDiagramFragment = new DrawingDiagramFragment();
    GetDateTime getDateTime;
    String[] updateDT,datetime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mManager = new PreferenceData(getActivity());
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        reportID = mManager.getPreferenceData(mManager.PREF_REPORTID);
        mFragmentManager = getActivity().getSupportFragmentManager();
        mDbHelper = new SQLiteDBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();
        getDateTime = new GetDateTime();
        updateDT=getDateTime.getDateTimeNow();
        datetime =getDateTime.getDateTimeCurrent();
        View viewDiagramTab = inflater.inflate(R.layout.diagram_tab_layout, container, false);
        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        gViewPic = (GridView) viewDiagramTab.findViewById(R.id.gridViewShowMedia);
        rootLayout = (CoordinatorLayout) viewDiagramTab.findViewById(R.id.rootLayout);
        showAllPic();

        fabBtn = (FloatingActionButton) viewDiagramTab.findViewById(R.id.fabBtn);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showMediaDialog();


            }
        });

        return viewDiagramTab;
    }
    public void showAllPic() {
        // TODO Auto-generated method stub
        arrDataPic = mDbHelper.SelectDataMultimediaFile(reportID, "diagram");
        if(arrDataPic!=null){
            gViewPic.setVisibility(View.VISIBLE);
            gViewPic.setAdapter(new DiagramPicAdapter(getActivity(), arrDataPic));
            registerForContextMenu(gViewPic);
            // OnClick
            gViewPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
				/*Intent PhotoViewActivity = new Intent(getActivity(),PhotoViewActivity.class);
				PhotoViewActivity.putExtra("PhotoPath", arrDataPhoto[position][2].toString());
				startActivity(PhotoViewActivity);*/
                    Toast.makeText(
                            getActivity(),
                            "Your selected : " + arrDataPic[position][3].toString(),
                            Toast.LENGTH_SHORT).show();
                    showViewPic(arrDataPic[position][3].toString());
                }
            });
        } else {
            gViewPic.setVisibility(View.GONE);
            Log.i("Recieve", "Null!! ");

        }
    }
    public void showViewPic(String sPicPath) {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(getActivity(), R.style.FullHeightDialog);
        dialog.setContentView(R.layout.view_pic_dialog);
        String root = Environment.getExternalStorageDirectory().toString();
        String strPath = root + "/CSIFiles/Pictures/"+ sPicPath;

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

    public void showMediaDialog() {
        // TODO Auto-generated method stub
        final AlertDialog.Builder addDialog = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflaterDialog = (LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        View Viewlayout = inflaterDialog.inflate(R.layout.add_media_dialog,
                (ViewGroup) findViewById(R.id.layout_media_dialog));
        addDialog.setIcon(android.R.drawable.btn_star_big_on);
        addDialog.setTitle("วาดภาพแผนผังสถานที่เกิดเหตุ");
        addDialog.setView(Viewlayout);
       String  timeStamp =updateDT[0]+" "+updateDT[1];

        String CurrentDate_ID = "DIA_" + datetime[2]+""+datetime[1]+""+datetime[0]+"_"+ datetime[3]+""+datetime[4]+""+datetime[5];

        String sDiagramID = "DIA_" + CurrentDate_ID;
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

                        String sMediaName, sMediaDescription;
                        sMediaName = editMediaName.getText().toString();
                        if (editMediaDescription.getText().toString()
                                .equals("")) {
                            sMediaDescription = "";
                        } else {
                            sMediaDescription = editMediaDescription.getText()
                                    .toString();
                        }
                        Log.i("show", "drawing " + sMediaName + " "
                                + sMediaDescription);

                        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.containerView, drawingDiagramFragment.newInstance(reportID, sMediaName, sMediaDescription)).addToBackStack(null).commit();

                        /*FragmentTransaction mfragmentManager = getActivity()
                                .getSupportFragmentManager().beginTransaction();
                        mfragmentManager
                                .replace(
                                        R.id.containerView,
                                        drawingDiagramFragment.newInstance(reportID, sMediaName, sMediaDescription)).addToBackStack(null)
                                .commit();*/
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
    public class DiagramPicAdapter extends BaseAdapter {
        private Context context;
        private String[][] lis;

        public DiagramPicAdapter(Context c, String[][] li) {
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
            String root = Environment.getExternalStorageDirectory().toString();

            String strPath = root + "/CSIFiles/Pictures/"+ lis[position][3].toString();
            textView.setText(lis[position][3].toString()+"\n"
                    +lis[position][4].toString() );
            textView.setVisibility(View.GONE);
            // Image Resource
            ImageView imageView = (ImageView) convertView
                    .findViewById(R.id.imgPhoto);

            Bitmap bmpSelectedImage = BitmapFactory.decodeFile(strPath);
            int width1 = bmpSelectedImage.getWidth();
            int height1 = bmpSelectedImage.getHeight();
            Log.i("size", width1 + " "
                    + height1);
            int width = width1/4;
            int height = height1/4;
            Bitmap resizedbitmap = Bitmap.createScaledBitmap(bmpSelectedImage, width, height, true);
            imageView.setImageBitmap(resizedbitmap);

            return convertView;

        }
    }
    protected ViewGroup findViewById(int layoutMediaDialog) {
        // TODO Auto-generated method stub
        return null;
    }
}