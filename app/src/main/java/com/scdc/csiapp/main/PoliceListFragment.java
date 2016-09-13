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
import com.scdc.csiapp.policetabs.InquiryOfficialListTabFragment;
import com.scdc.csiapp.policetabs.InvestigatorListTabFragment;
import com.scdc.csiapp.policetabs.SubInvestigatorListTabFragment;

/**
 * Created by Pantearz07 on 23/9/2558.
 */
public class PoliceListFragment extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate csi_data_tab_layout and setup Views.
         */
        View x = inflater.inflate(R.layout.police_list_layout, null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabsPolice);
        viewPager = (ViewPager) x.findViewById(R.id.viewpagerPolice);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.policelist);
        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return x;

    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new InvestigatorListTabFragment();
                case 1:
                    return new SubInvestigatorListTabFragment();
                case 2:
                    return new InquiryOfficialListTabFragment();

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

            switch (position) {
                case 0:
                    return "ร้อยเวร";
                case 1:
                    return "สิบเวร";
                case 2:
                    return "พนักงานสอบสวน";
            }
            return null;
        }
    }

}