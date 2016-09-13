package com.scdc.csiapp.csidatatabs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.R;

/**
 * Created by Pantearz07 on 30/10/2558.
 */
public class AddSufferer  extends AppCompatActivity {
    private Toolbar toolbar;
    private PreferenceData mManager;
    String officialID, reportID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_sufferer);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);                   // Setting toolbar as the ActionBar with setSupportActionBar() call
        mManager = new PreferenceData(this);
        reportID = mManager.getPreferenceData(mManager.PREF_REPORTID);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_search) {
            Toast.makeText(this,
                    "รหัสรายงาน1 : " + reportID,
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_user) {
            Toast.makeText(this,
                    "รหัสรายงาน2 : " + reportID,
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


