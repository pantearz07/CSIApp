package com.scdc.csiapp.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;

import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Pantearz07 on 13/11/2558.
 */
public class MapActivity extends FragmentActivity {

    private static final LatLng DAVAO = new LatLng(7.0722, 125.6131);
    private GoogleMap mMap;
    GMapV2Direction md;
    LatLng fromPosition, toPosition, coordinates, addPosition;
    double latTo, lngTo, coLat, coLng, latAdd, lngAdd;
    LocationManager lm;
    SQLiteDatabase mDb;
    SQLiteDBHelper mDbHelper;
    private String sReportID, sLocaleName, sHouseNo, sVillageNo, sVillageName, sLane, sRoad, sDistrict,
            sAmphur, sProvince,sLatitude,sLongitude;
    private EditText etLocation;
    private TextView tvLocation,txt_location;
    private Button btn_find, btn_findcurrent, btn_savelocale,btn_findpath;
    ArrayAdapter<String> adapter;
    // flag for Internet connection status
    ConnectionDetector cd;
    Boolean networkConnectivity = false;

    private PreferenceData mManager;
    String officialID, reportID;
    private ViewGroup viewByIdAddLocationDialog;
    protected static final int DIALOG_AddLocation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_map);

        mDbHelper = new SQLiteDBHelper(this);
        mDb = mDbHelper.getWritableDatabase();
        mManager = new PreferenceData(this);
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        reportID = mManager.getPreferenceData(mManager.PREF_REPORTID);
        cd = new ConnectionDetector(this);
        networkConnectivity = cd.isNetworkAvailable();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        sReportID = getIntent().getExtras().getString("reportid");
        sLocaleName = getIntent().getExtras().getString("LocaleName");
        sHouseNo = getIntent().getExtras().getString("houseNo");
        sVillageNo = getIntent().getExtras().getString("villageNo");
        sVillageName = getIntent().getExtras().getString("villageName");
        sLane = getIntent().getExtras().getString("lane");
        sRoad = getIntent().getExtras().getString("road");
        sDistrict = getIntent().getExtras().getString("District");
        sAmphur = getIntent().getExtras().getString("Amphur");
        sProvince = getIntent().getExtras().getString("province");

        sLatitude = getIntent().getExtras().getString("Latitude");
        sLongitude = getIntent().getExtras().getString("Longitude");
        txt_location = (TextView) findViewById(R.id.txt_location);
        txt_location.setText("Lat: "+sLatitude+"\nLong: "+sLongitude);
        Log.i("Latlog", sLatitude + " " + sLongitude);
        tvLocation = (TextView) findViewById(R.id.sh_currentlatlng);
        etLocation = (EditText) findViewById(R.id.et_location);

        if (sHouseNo.length() != 0) {
            sHouseNo = "บ้านเลขที่ "
                    + sHouseNo + " ";
        }
        if (sVillageNo.length() != 0) {
            sVillageNo = "หมู่ที่ " + sVillageNo + " ";
        }
        if (sVillageName.length() != 0) {
            sVillageName = "หมู่บ้าน"
                    + sVillageName + " ";
        }
        if (sLane.length() != 0) {
            sLane = "ซอย" + sLane + " ";
        }
        if (sRoad.length() != 0) {
            sRoad = "ถนน" + sRoad + " ";
        }
        if (sDistrict.length() != 0) {
            sDistrict = "ตำบล"
                    + sDistrict
                    + " ";
        }
        if (sAmphur.length() != 0) {
            sAmphur = "อำเภอ"
                    + sAmphur + " ";
        }
        if (sProvince.length() != 0) {
            sProvince = "จังหวัด"
                    + sProvince;
        }

        String strAddress = sVillageName + sLane + sRoad + sDistrict
                + sAmphur + sProvince;
        etLocation.setText(strAddress);
        Toast.makeText(this,
                strAddress,
                Toast.LENGTH_LONG).show();

        btn_find = (Button) findViewById(R.id.btn_find);
        btn_savelocale = (Button) findViewById(R.id.btn_savelocale);
        btn_findpath = (Button) findViewById(R.id.btn_findpath);
        // Button find current
        btn_findcurrent = (Button) findViewById(R.id.btn_findcurrent);
        btn_find.setOnClickListener(new MapOnClickListener());
        btn_savelocale.setOnClickListener(new MapOnClickListener());
        btn_findcurrent.setOnClickListener(new MapOnClickListener());
        btn_findpath.setOnClickListener(new MapOnClickListener());
        md = new GMapV2Direction(this);
        mMap = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapView)).getMap();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            public void onMapClick(LatLng arg0) {
                final LatLng coordinate = arg0;
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        MapActivity.this);
                builder.setTitle("Select Marker").setItems(
                        new String[] { "From", "To" },
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int position) {
                                if (position == 0)
                                    fromPosition = coordinate;

                                else if (position == 1)
                                    toPosition = coordinate;
                                coordinates = toPosition;
                                refreshMarker();
                            }
                        });
                builder.show();
            }
        });

        /*mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapView)).getMap();

        Marker davao = map.addMarker(new MarkerOptions().position(DAVAO).title("Davao City").snippet("Ateneo de Davao University"));

        // zoom in the camera to Davao city
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(DAVAO, 15));

        // animate the zoom process
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        */
    }

    public class MapOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (v == btn_savelocale) {

                Log.i("btn_savelocale", "btn_savelocale");
                boolean isGPS = lm
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);
                Log.i("btn_savelocale", String.valueOf(isGPS));
                if (isGPS == false) {
                    Toast.makeText(MapActivity.this,
                            "กรุณาทำการเชื่อมต่อสัญญาาณ GPS ก่อน",
                            Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(MapActivity.this,
                            "เชื่อมต่อสัญญาณ GPS เเล้ว", Toast.LENGTH_SHORT)
                            .show();
                    viewByIdAddLocationDialog = (ViewGroup) v.findViewById(R.id.layout_clueshown_dialog);

                    createdDialog(DIALOG_AddLocation).show();
                }
            }
            if (v == btn_find) {
                Log.i("btn_find", "btn_find");
                if (networkConnectivity) {
                    Log.d("internet status", "connected to wifi");
                    boolean isGPS = lm
                            .isProviderEnabled(LocationManager.GPS_PROVIDER);
                    if (isGPS == false) {
                        Toast.makeText(MapActivity.this,
                                "กรุณาทำการเชื่อมต่อสัญญาาณ GPS ก่อน",
                                Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(MapActivity.this,
                                "เชื่อมต่อสัญญาณ GPS เเล้ว", Toast.LENGTH_SHORT)
                                .show();
                        String location = etLocation.getText().toString();

                        if (location != null && !location.equals("")) {
                            new GeocoderTask().execute(location);
                        }
                    }
                } else {
                    Log.d("internet status", "no Internet Access");

                    Toast.makeText(MapActivity.this,
                            "กรุณาเชื่อมต่ออินเทอร์เน็ต",
                            Toast.LENGTH_SHORT).show();


                }

            }
            if (v == btn_findcurrent) {
                Log.i("btn_findcurrent", "btn_findcurrent");

                boolean isGPS = lm
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (isGPS == false) {
                    Toast.makeText(MapActivity.this,
                            "กรุณาทำการเชื่อมต่อสัญญาณ GPS ก่อน",
                            Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MapActivity.this,
                            "เชื่อมต่อสัญญาณ GPS เเล้ว", Toast.LENGTH_SHORT)
                            .show();
                    setUpMap();

                }
            }
            if(v == btn_findpath){
                Log.i("btn_findpath", "btn_findpath");
                if (networkConnectivity) {
                    Log.d("internet status", "connected to wifi");
                    boolean isGPS = lm
                            .isProviderEnabled(LocationManager.GPS_PROVIDER);
                    if (isGPS == false) {
                        Toast.makeText(MapActivity.this,
                                "กรุณาทำการเชื่อมต่อสัญญาาณ GPS ก่อน",
                                Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(MapActivity.this,
                                "เชื่อมต่อสัญญาณ GPS เเล้ว", Toast.LENGTH_SHORT)
                                .show();

                        latTo = Double.parseDouble(sLatitude);
                        lngTo = Double.parseDouble(sLongitude);
                        toPosition = new LatLng(latTo, lngTo);
                        coordinates = toPosition;
                        refreshMarker();
                    }
                } else {
                    Log.d("internet status", "no Internet Access");

                    Toast.makeText(MapActivity.this,
                            "กรุณาเชื่อมต่ออินเทอร์เน็ต",
                            Toast.LENGTH_SHORT).show();


                }
            }
        }
    }

    protected Dialog createdDialog(int id) {
        Dialog dialog = null;
        final AlertDialog.Builder builderDialog1, builderDialog2;
        final LayoutInflater inflaterDialog;
        View Viewlayout;

        builderDialog1 = new AlertDialog.Builder(this);
        builderDialog2 = new AlertDialog.Builder(this);
        inflaterDialog = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (id) {
            case DIALOG_AddLocation:
                Viewlayout = inflaterDialog
                        .inflate(
                                R.layout.add_clueshown_dialog, viewByIdAddLocationDialog);
                builderDialog1
                        .setTitle("ระบุพิกัดสถานที่เกิดเหตุ");
                builderDialog1.setView(Viewlayout);
                final EditText editLocaleName = (EditText) Viewlayout
                        .findViewById(R.id.editClueShownPosition);
                editLocaleName.setHint("ชื่อสถานที่เกิดเหตุ");
                editLocaleName.setText(sLocaleName);

// Button OK
                builderDialog1.setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();

                                if (editLocaleName.getText().toString()
                                        .equals("")) {
                                    builderDialog2
                                            .setIcon(android.R.drawable.btn_star_big_on);
                                    builderDialog2
                                            .setTitle("กรุณากรอกชื่อที่อยู่สถานที่เกิดเหตุ!");
                                    builderDialog2.setPositiveButton("OK", null);
                                    builderDialog2.show();

                                } else {

                                    String sLocaleName;
                                    sLocaleName = "";
                                    if (editLocaleName.getText().toString()
                                            .length() != 0) {
                                        sLocaleName = editLocaleName
                                                .getText().toString();
                                    }
                                    Toast.makeText(MapActivity.this,
                                            sLocaleName, Toast.LENGTH_SHORT)
                                            .show();
                                    saveAddressScene(sLocaleName);
                                }
                            }

                        })

                        // Button Cancel
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                dialog = builderDialog1.create();
                break;
            default:
                dialog = null;
        }
        return dialog;
    }

    private void saveAddressScene(String sLocaleName) {
        // TODO Auto-generated method stub
        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();

        String provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location myLocation = locationManager.getLastKnownLocation(provider);

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        latAdd = myLocation.getLatitude();
        lngAdd = myLocation.getLongitude();

        addPosition = new LatLng(latAdd, lngAdd);
        Log.i("latAdd", String.valueOf(latAdd));
        Log.i("lngAdd", String.valueOf(lngAdd));
        mMap.addMarker(new MarkerOptions()
                .position(addPosition)
                .title(sLocaleName)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        saveLocale(sReportID, sLocaleName, latAdd, lngAdd);
    }

    private void saveLocale(String sReportID, String sLocaleName,
                            double slatAdd, double slngAdd) {
        // TODO Auto-generated method stub
        //String arrData[] = mDbHelper.SelectDataLocaleOfCase(sReportID);
        //if (arrData != null) {
        ///String sLocaleID = arrData[0];
        //Log.i("sLocaleID", sLocaleID);
        long saveStatus = mDbHelper.updateDataLocale(sReportID,
                sLocaleName, slatAdd, slngAdd);
        if (saveStatus <= 0) {
            Log.i("Recieve", "Error!! ");
        } else {
            Log.i("updateData Locale", sReportID + " " + sLocaleName + " "
                    + slatAdd + " " + slngAdd);
            Toast.makeText(MapActivity.this, "บันทึกเรียบร้อยแล้ว",
                    Toast.LENGTH_LONG).show();
            this.onBackPressed();
        }

        //} else {
        //	Log.i("Recieve", "Null!! ");
        //}
    }

    // AsyncTask GeoCoding Web Service
    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

        @Override
        protected List<Address> doInBackground(String... locationName) {

            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;

            try {

                addresses = geocoder.getFromLocationName(locationName[0], 8);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();

            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {

            if (addresses == null || addresses.size() == 0) {
                Toast.makeText(getBaseContext(), "No Location found",
                        Toast.LENGTH_SHORT).show();
            }

            mMap.clear();

            ArrayList<HashMap<String, String>> myArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

            for (int i = 0; i < addresses.size(); i++) {

                Address address = (Address) addresses.get(i);

                // LatLng toPosition1 = new LatLng(address.getLatitude(),
                // address.getLongitude());
                double latTo1, lngTo1;
                latTo1 = address.getLatitude();
                lngTo1 = address.getLongitude();
                String addressText = String.format(
                        "%s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address
                                .getAddressLine(0) : "", address
                                .getCountryName());

                Log.i("addressText", addressText);
                Log.i("latto1", String.valueOf(latTo1));
                Log.i("lngto1", String.valueOf(lngTo1));

                map = new HashMap<String, String>();
                map.put("AddressText", addressText);
                map.put("LatTo", String.valueOf(latTo1));
                map.put("LngTo", String.valueOf(lngTo1));
                myArrList.add(map);

            }
            Log.i("myArrList size", String.valueOf(myArrList.size()));
            AlertUsingAdapter(myArrList);
        }
    }

    private void AlertUsingAdapter(
            final ArrayList<HashMap<String, String>> myArrList) {

        Log.i("myArrList size", String.valueOf(myArrList.size()));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("เลือกพิกัดปลายทาง");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);
        for (int i = 0; i < myArrList.size(); i++) {
            arrayAdapter.add(myArrList.get(i).get("AddressText"));

        }
        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(
                        MapActivity.this,
                        "เลือก "
                                + String.valueOf(myArrList.get(which).get(
                                "AddressText"))
                                + "\nLat: "
                                + String.valueOf(myArrList.get(which).get(
                                "LatTo"))
                                + "\nLng: "
                                + String.valueOf(myArrList.get(which).get(
                                "LngTo")), Toast.LENGTH_LONG).show();

                latTo = Double.parseDouble(myArrList.get(which).get("LatTo"));
                lngTo = Double.parseDouble(myArrList.get(which).get("LngTo"));
                toPosition = new LatLng(latTo, lngTo);
                // coLat = (latFrom + latTo) / 2;
                // coLng = (lngFrom + lngTo) / 2;
                coordinates = toPosition;
                // Log.i("lat co", String.valueOf(coLat));
                // Log.i("lng co", String.valueOf(coLng));

                refreshMarker();
            }
        });
        builder.setPositiveButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void refreshMarker() {
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(fromPosition)
                .title("Start")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.addMarker(new MarkerOptions()
                .position(toPosition)
                .title("End")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        Log.i("refreshMarker", "refreshMarker");
        Document doc = md.getDocument(fromPosition, toPosition,
                GMapV2Direction.MODE_DRIVING);
        ArrayList<LatLng> directionPoint = md.getDirection(doc);
        md.getDurationValue(doc);
        md.getStartAddress(doc);
        md.getEndAddress(doc);
        md.getCopyRights(doc);

        PolylineOptions rectLine = new PolylineOptions().width(3).color(
                Color.RED);

        for (int i = 0; i < directionPoint.size(); i++) {
            rectLine.add(directionPoint.get(i));
        }
        mMap.addPolyline(rectLine);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 14));

    }

    private void setUpMap() {
        // TODO Auto-generated method stub
        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();

        String provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        double latFrom = myLocation.getLatitude();
        double lngFrom = myLocation.getLongitude();
        fromPosition = new LatLng(latFrom, lngFrom);
        mMap.addMarker(new MarkerOptions()
                .position(fromPosition)
                .title("Current")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        // Setting latitude and longitude in the TextView tv_location
        tvLocation.setText("Lat: " + String.valueOf(latFrom) + "\nLong: " + String.valueOf(lngFrom));

        Log.i("latFrom", String.valueOf(latFrom));
        Log.i("lngFrom", String.valueOf(lngFrom));
        btn_find.setEnabled(true);
    }

    }
