package com.scdc.csiapp.inqmain;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scdc.csiapp.R;

public class EmergencyTabFragment extends Fragment {
    //หน้าโชว์ข้อมูลคดีทั้งหมด โดยเเบ่งเป็นเเท็บ 2 แท็บ
    //SummaryEmerTabFragment
    //EmergencyDetailTabFragment

    // CoordinatorLayout rootLayoutCSI;
    public static TabLayout tabLayoutCSI;
    public static ViewPager viewpagerCSI;
    public static int int_items = 2 ;
    public static String NoticeCaseID;
    private static final String TAG = "DEBUG-EmergencyTabFragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate csi_data_tab_layout and setup Views.
         */
        View x =  inflater.inflate(R.layout.emer_data_tab_layout,null);
        //rootLayoutCSI = (CoordinatorLayout) x.findViewById(R.id.rootLayoutCSI);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.inq_appname);
        tabLayoutCSI = (TabLayout) x.findViewById(R.id.tabLayoutCSI);
        viewpagerCSI = (ViewPager) x.findViewById(R.id.viewpagerCSI);

        /**
         *Set an Apater for the View Pager
         */
        viewpagerCSI.setAdapter(new MyAdapter(getChildFragmentManager()));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayoutCSI.post(new Runnable() {
            @Override
            public void run() {
                tabLayoutCSI.setupWithViewPager(viewpagerCSI);
            }
        });
        Bundle bundle = getArguments();
        NoticeCaseID = bundle.getString("NoticeCaseID", "");
        Log.i(TAG, " NoticeCaseID "+NoticeCaseID);
        return x;

    }

    class MyAdapter extends FragmentPagerAdapter{

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position)
        {

            switch (position){
                case 0 : return new SummaryEmerTabFragment();
                case 1 : return new EmergencyDetailTabFragment();

            }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "สรุป";
                case 1 :
                    return "แจ้งเหตุ";

            }
            return null;
        }
    }


}