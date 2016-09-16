package com.scdc.csiapp.invmain;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pantearz07 on 29/1/2559.
 */
public class NoteActivity extends AppCompatActivity  {
    SQLiteDBHelper mDbHelper;
    SQLiteDatabase mDb;
    private PreferenceData mManager;
    String officialID, reportID,fileid;
    String sNoteID;
   android.app.FragmentManager mFragmentManager;
    //NoteTabFragment noteTabFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity);

         //ActionBar actionBar = getSupportActionBar();

        // actionBar.setDisplayUseLogoEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFragmentManager = getFragmentManager();
       // noteTabFragment = new NoteTabFragment();
        mDbHelper = new SQLiteDBHelper(this);
        mDb = mDbHelper.getWritableDatabase();
        mManager = new PreferenceData(this);
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        reportID = mManager.getPreferenceData(mManager.PREF_REPORTID);
        fileid = getIntent().getExtras().getString("fileid");
        String arrData[] = mDbHelper.SelectDataMultimediaFile(fileid);

        final EditText txtNote = (EditText)findViewById(R.id.txtNote);
        if(arrData!=null) {
            txtNote.setText(arrData[4]);
        }else{
            txtNote.setText("");
        }

        final Button btnClear = (Button) findViewById(R.id.btnClear);
        final Button btnSave = (Button) findViewById(R.id.btnSave);
        final Button btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtNote.setText("");
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sNoteDetail;
                final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                Log.i("sNoteID", sNoteID);
                if (txtNote.getText().toString().equals("")) {
                    sNoteDetail = "";
                } else {
                    sNoteDetail = txtNote.getText().toString();
                }

                long saveStatus = mDbHelper.updateDataMultimediaifile(sNoteID, sNoteDetail, timeStamp);
                if (saveStatus <= 0) {
                    Log.i("Recieve", "Error!! ");
                } else {
                    Log.i("save note", "ok");
                    Toast.makeText(NoteActivity.this,
                            "บันทึกเรียบร้อยแล้ว" + sNoteID,
                            Toast.LENGTH_SHORT).show();
                    btnUpdate.setVisibility(View.VISIBLE);
                    btnSave.setEnabled(false);
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sNoteDetail;
                final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
               sNoteID = "NOTE_" + timeStamp;
                if (txtNote.getText().toString().equals("")) {
                    sNoteDetail = "";
                } else {
                    sNoteDetail = txtNote.getText().toString();
                }

                long saveStatus = mDbHelper.saveDataMultimediaifile(reportID, sNoteID, "note",sNoteID, sNoteDetail, timeStamp);
                if (saveStatus <= 0) {
                    Log.i("Recieve", "Error!! ");
                } else {
                    Log.i("save note", "ok");
                    Toast.makeText(NoteActivity.this,
                            "บันทึกเรียบร้อยแล้ว" + sNoteID,
                            Toast.LENGTH_SHORT).show();
                    btnUpdate.setVisibility(View.VISIBLE);
                    btnSave.setEnabled(false);
                    onBackPressed();
                }

            }

        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    /**
     * Let's the user tap the activity icon to go 'home'.
     * Requires setHomeButtonEnabled() in onCreate().
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                // ProjectsActivity is my 'home' activity
                //startActivityAfterCleanup(ProjectsActivity.class);
                //this.finish();
                NavUtils.navigateUpFromSameTask(this);
                //this.onBackPressed();
                /*
                FragmentTransaction ftnoti = mFragmentManager.beginTransaction();
                ftnoti.replace(R.id.containerView, noteTabFragment);
                ftnoti.addToBackStack(null);
                ftnoti.commit();*/
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        this.finish();
    }
}
