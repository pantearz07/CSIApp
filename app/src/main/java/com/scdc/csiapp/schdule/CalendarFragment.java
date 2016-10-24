package com.scdc.csiapp.schdule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiListScheduleInvestigates;
import com.scdc.csiapp.apimodel.ApiScheduleGroup;
import com.scdc.csiapp.apimodel.ApiScheduleInvestigates;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.main.WelcomeActivity;
import com.scdc.csiapp.schdule.decorators.EventDecorator;
import com.scdc.csiapp.schdule.decorators.HighlightWeekendsDecorator;
import com.scdc.csiapp.schdule.decorators.MySelectorDecorator;
import com.scdc.csiapp.schdule.decorators.OneDayDecorator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by Pantearz07 on 22/10/2559.
 */

public class CalendarFragment extends Fragment implements OnDateSelectedListener {
    private static final String TAG = "DEBUG-CalendarFragment";
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    CoordinatorLayout coordinatorLayout;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    List<ApiScheduleInvestigates> apiScheduleInvestigatesList;
    TextView textView;
    MaterialCalendarView widget;
    DBHelper mDbHelper;
    ConnectionDetector cd;
    Context context;
    ListView listViewInvestigator;
//    List<ApiScheduleInvestigates> apiScheduleInvestigates_bydate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.calendar_layout, null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.schedule);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.rootLayout);
        context = view.getContext();
        mDbHelper = new DBHelper(getActivity());
        cd = new ConnectionDetector(context);
        apiScheduleInvestigatesList = new ArrayList<>();
        widget = (MaterialCalendarView) view.findViewById(R.id.calendarView);
        textView = (TextView) view.findViewById(R.id.textView);
        listViewInvestigator = (ListView) view
                .findViewById(R.id.listViewInvestigator);
        listViewInvestigator.setOnTouchListener(new ListviewSetOnTouchListener());

        widget.setOnDateChangedListener(this);
        widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        Calendar instance = Calendar.getInstance();
        widget.setSelectedDate(instance.getTime());

        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance1.get(Calendar.YEAR), Calendar.JANUARY, 1);

        Calendar instance2 = Calendar.getInstance();
        instance2.set(instance2.get(Calendar.YEAR), Calendar.DECEMBER, 31);

        widget.state().edit()
                .setMinimumDate(instance1.getTime())
                .setMaximumDate(instance2.getTime())
                .commit();

        widget.addDecorators(
                new MySelectorDecorator(getActivity()),
                new HighlightWeekendsDecorator(),
                oneDayDecorator
        );

        if (cd.isNetworkAvailable()) {
            new ConnectlistScheduleInvestigates().execute();
        } else {
            selectApiScheduleInvestigatesSQLite();
        }
        textView.setText("รายชื่อผุ้ตรวจประจำวันที่ " + getSelectedDatesString());
        getSelectedDates();
        new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());
//        new ApiSimulator2().executeOnExecutor(Executors.newSingleThreadExecutor());
        return view;
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        oneDayDecorator.setDate(date.getDate());
        widget.invalidateDecorators();
        textView.setText("รายชื่อผุ้ตรวจประจำวันที่ " + getSelectedDatesString());
        getSelectedDates();
    }

    /**
     * Simulate an API call to show how to add decorators
     */

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();
            int size = apiScheduleInvestigatesList.size();
            for (int i = 0; i < size; i++) {
                CalendarDay day = new CalendarDay();
                String ScheduleDate = apiScheduleInvestigatesList.get(i).getTbScheduleInvestigates().ScheduleDate;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                int sizeScheduleGroup = 0;
                sizeScheduleGroup = apiScheduleInvestigatesList.get(i).getApiScheduleGroup().size();
                Log.d(TAG, "sizeScheduleGroup" + String.valueOf(sizeScheduleGroup));
                for (int j = 0; j < sizeScheduleGroup; j++) {
                    String y = apiScheduleInvestigatesList.get(i).getApiScheduleGroup().get(j).getTbScheduleGroup().ScheduleGroupID;
                    Log.d(TAG, "ScheduleGroupID :" + y);
                    int sizeScheduleInvInGroup = 0;
                    sizeScheduleInvInGroup = apiScheduleInvestigatesList.get(i).getApiScheduleGroup().get(j).getApiScheduleInvInGroup().size();
                    Log.d(TAG, "sizeScheduleInvInGroup" + String.valueOf(sizeScheduleInvInGroup));
                    for (int k = 0; k < sizeScheduleInvInGroup; k++) {
                        String InvOfficialID = apiScheduleInvestigatesList.get(i).getApiScheduleGroup().get(j).getApiScheduleInvInGroup().get(k).getTbScheduleInvInGroup().InvOfficialID;
                        String AccessType = apiScheduleInvestigatesList.get(i).getApiScheduleGroup().get(j).getApiScheduleInvInGroup().get(k).getTbOfficial().AccessType;
                        if (AccessType.equals("investigator")) {
                            Log.d(TAG, "on AccessType : " + apiScheduleInvestigatesList.get(i).getApiScheduleGroup().get(j).getApiScheduleInvInGroup().get(k).getTbOfficial().AccessType);
                            if (WelcomeActivity.profile.getTbOfficial().OfficialID.equals(apiScheduleInvestigatesList.get(i).getApiScheduleGroup().get(j).getApiScheduleInvInGroup().get(k).getTbScheduleInvInGroup().InvOfficialID)) {
                                Log.d(TAG, "on InvOfficialID : " + apiScheduleInvestigatesList.get(i).getApiScheduleGroup().get(j).getApiScheduleInvInGroup().get(k).getTbScheduleInvInGroup().InvOfficialID);
                                try {
                                    calendar.setTime(sdf.parse(ScheduleDate));
                                    calendar.add(Calendar.DATE, 0);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                day = CalendarDay.from(calendar);
                                dates.add(day);
                                Log.d(TAG, "day. " + day.toString());
                            } else {
                                Log.d(TAG, "not InvOfficialID : " + apiScheduleInvestigatesList.get(i).getApiScheduleGroup().get(j).getApiScheduleInvInGroup().get(k).getTbScheduleInvInGroup().InvOfficialID);
                            }
                        }
                    }
                }


                Log.d(TAG, "Calendar.DATE " + String.valueOf(Calendar.DATE));
                Log.d(TAG, "day.getDay " + String.valueOf(day.getYear()) + "-" + String.valueOf(day.getMonth()) + "-" + String.valueOf(day.getDay()));

            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (getActivity().isFinishing()) {
                return;
            }

            widget.addDecorator(new EventDecorator(Color.RED, calendarDays));
        }
    }

    private String getSelectedDatesString() {
        CalendarDay date = widget.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }
        return FORMATTER.format(date.getDate());
    }

    private void getSelectedDates() {
        CalendarDay date = widget.getSelectedDate();
        if (date == null) {
//            return "No Selection";
        } else {
            List<ApiScheduleGroup> apiScheduleGroups = new ArrayList<>();
            String daySelected = String.valueOf(date.getYear()) + "-" + String.format("%02d", date.getMonth() + 1) + "-" + String.format("%02d", date.getDay());
            Log.d(TAG, "getSelectedDate " + daySelected);
            int size = apiScheduleInvestigatesList.size();
            Log.d(TAG, "apiScheduleInvestigatesList " + String.valueOf(size));
            for (int i = 0; i < size; i++) {
                String ScheduleDate = apiScheduleInvestigatesList.get(i).getTbScheduleInvestigates().ScheduleDate;
                if (ScheduleDate.equals(daySelected)) {
                    Log.d(TAG, "have ScheduleDate " + daySelected + "/n");

                    int sizeScheduleGroup = 0;
                    sizeScheduleGroup = apiScheduleInvestigatesList.get(i).getApiScheduleGroup().size();
                    Log.d(TAG, "have sizeScheduleGroup " + sizeScheduleGroup);
                    apiScheduleGroups = new ArrayList<>(sizeScheduleGroup);
                    for(int j=0; j < sizeScheduleGroup; j++) {
                        apiScheduleGroups.add(apiScheduleInvestigatesList.get(i).getApiScheduleGroup().get(j));
                        Log.d(TAG, "have getApiScheduleGroup " + apiScheduleInvestigatesList.get(i).getApiScheduleGroup().get(j).getTbScheduleGroup().ScheduleGroupID);

                    }
                }
            }

            if (apiScheduleGroups != null) {
                Log.d(TAG, String.valueOf(apiScheduleGroups.size()));
                showListInvestigators(apiScheduleGroups);
                listViewInvestigator.setVisibility(View.VISIBLE);
            }
        }
        new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());
//        return FORMATTER.format(date.getDate());
    }

    private class ConnectlistScheduleInvestigates extends AsyncTask<Void, Void, ApiListScheduleInvestigates> {


        @Override
        protected ApiListScheduleInvestigates doInBackground(Void... voids) {
            return WelcomeActivity.api.listScheduleInvestigates();
        }

        @Override
        protected void onPostExecute(ApiListScheduleInvestigates apiListScheduleInvestigates) {
            super.onPostExecute(apiListScheduleInvestigates);

            if (apiListScheduleInvestigates != null) {
                Log.d(TAG, apiListScheduleInvestigates.getStatus());
                Log.d(TAG, String.valueOf(apiListScheduleInvestigates.getData().getResult().size()));
                apiScheduleInvestigatesList = apiListScheduleInvestigates.getData().getResult();

                mDbHelper.syncApiScheduleInvestigates(apiScheduleInvestigatesList);
                new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());
                getSelectedDates();

            } else {
                Log.d(TAG, "apiListScheduleInvestigates null");
                selectApiScheduleInvestigatesSQLite();
            }
        }
    }

    public void selectApiScheduleInvestigatesSQLite() {
        ApiListScheduleInvestigates apiListScheduleInvestigates = mDbHelper.selectApiScheduleInvestigates(WelcomeActivity.profile.getTbOfficial().SCDCAgencyCode);
        if (apiListScheduleInvestigates != null) {
            Log.d(TAG, apiListScheduleInvestigates.getStatus());
            Log.d(TAG, "Update apiListScheduleInvestigates SQLite");
            apiScheduleInvestigatesList = apiListScheduleInvestigates.getData().getResult();
            Log.d(TAG, String.valueOf(apiScheduleInvestigatesList.size()));
        } else {
            Log.d(TAG, "apiListScheduleInvestigates null SQLite");
        }
    }

    public class ListviewSetOnTouchListener implements ListView.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    // Disallow ScrollView to intercept touch events.
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    break;

                case MotionEvent.ACTION_UP:
                    // Allow ScrollView to intercept touch events.
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }

            // Handle ListView touch events.
            v.onTouchEvent(event);
            return true;
        }
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();
            Log.i("inside", String.valueOf(numberOfItems));
            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                Log.i("inside", String.valueOf(item.getMeasuredHeight()));
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.

            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            int totalHeight = totalItemsHeight + totalDividersHeight;
            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            //params.height = (int) (totalItemsHeight-(totalItemsHeight/1.5));
            params.height = totalHeight;
            Log.i("inside totalHeight", String.valueOf(totalHeight));
            //  Log.i("inside getDividerHeight", String.valueOf(totalItemsHeight) + " " + String.valueOf(totalItemsHeight - (totalItemsHeight / 1.5)));
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }

    private void showListInvestigators(List<ApiScheduleGroup> apiScheduleGroups) {
        if (apiScheduleGroups != null) {
            listViewInvestigator.setAdapter(new InvestigatorsAdapter(getActivity(),apiScheduleGroups));
            setListViewHeightBasedOnItems(listViewInvestigator);
            listViewInvestigator.setVisibility(View.VISIBLE);
        } else {
            listViewInvestigator.setVisibility(View.GONE);
        }
    }

    public class InvestigatorsAdapter extends BaseAdapter {
        private Context context;
        private List<ApiScheduleGroup> apiScheduleGroups;

        public InvestigatorsAdapter(Context c, List<ApiScheduleGroup> cc) {
            // TODO Auto-generated method stub
            context = c;
            apiScheduleGroups  = cc;
        }

        @Override
        public int getCount() {
            return apiScheduleGroups.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (view == null) {
                view = inflater.inflate(R.layout.list_sceneinvestigation, null);
            }
            final TextView txtSceneInvest = (TextView) view.findViewById(R.id.txtSceneInvest);
            final TextView txtNo = (TextView) view.findViewById(R.id.txtNo);
            txtNo.setVisibility(View.VISIBLE);
            String sno = "";
            String invinfo = "";

            int sizeScheduleInvInGroup=0;
                String ScheduleGroupID = apiScheduleGroups.get(i).getTbScheduleGroup().ScheduleGroupID;
                sizeScheduleInvInGroup = apiScheduleGroups.get(i).getApiScheduleInvInGroup().size();
                Log.i(TAG, " sizeScheduleInvInGroup " + String.valueOf(sizeScheduleInvInGroup));
                Log.d(TAG, "กลุ่มที่ " + ScheduleGroupID.substring(11)+" "+ScheduleGroupID);
                sno = "กลุ่มที่ " + ScheduleGroupID.substring(11) ;
                txtNo.setText(sno);

                for (int k = 0; k < sizeScheduleInvInGroup; k++) {
                    final String sRank = apiScheduleGroups.get(i).getApiScheduleInvInGroup().get(k).getTbOfficial().getRank();
                    final String sFirstName = apiScheduleGroups.get(i).getApiScheduleInvInGroup().get(k).getTbOfficial().getFirstName();
                    final String sLastName = apiScheduleGroups.get(i).getApiScheduleInvInGroup().get(k).getTbOfficial().getLastName();
                    final String sPosition = apiScheduleGroups.get(i).getApiScheduleInvInGroup().get(k).getTbOfficial().getPosition();

                    invinfo = invinfo + String.valueOf(k + 1) + ") " + sRank + " " + sFirstName + " " + sLastName + " " + sPosition + "\n";
                }
                Log.d(TAG, "รายชื่อ "  +invinfo);

                txtSceneInvest.setText(invinfo);

            return view;
        }
    }
}
