package com.scdc.csiapp.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scdc.csiapp.R;
import com.scdc.csiapp.csidatatabs.CaseDetailTabFragment;
import com.scdc.csiapp.csidatatabs.SumCaseTabFragment;

public class AcceptDoneTabFragment extends Fragment {

   // CoordinatorLayout rootLayoutCSI;
    public static TabLayout tabLayoutCSI;
    public static ViewPager viewpagerCSI;
    public static int int_items = 2 ;

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
                case 0 : return new SumCaseTabFragment();
                case 1 : return new CaseDetailTabFragment();

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