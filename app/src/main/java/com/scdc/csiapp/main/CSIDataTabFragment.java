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
import com.scdc.csiapp.csidatatabs.DetailsTabFragment;
import com.scdc.csiapp.csidatatabs.DiagramTabFragment;
import com.scdc.csiapp.csidatatabs.PhotoTabFragment;
import com.scdc.csiapp.csidatatabs.ReceiveDataTabFragment;
import com.scdc.csiapp.csidatatabs.ResultTabFragment;
import com.scdc.csiapp.csidatatabs.SummaryCSITabFragment;
import com.scdc.csiapp.csidatatabs.VideoTabFragment;
import com.scdc.csiapp.csidatatabs.VoiceTabFragment;


public class CSIDataTabFragment extends Fragment {

   // CoordinatorLayout rootLayoutCSI;
    public static TabLayout tabLayoutCSI;
    public static ViewPager viewpagerCSI;
    public static int int_items = 8 ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate csi_data_tab_layout and setup Views.
         */
        View x =  inflater.inflate(R.layout.csi_data_tab_layout,null);
        //rootLayoutCSI = (CoordinatorLayout) x.findViewById(R.id.rootLayoutCSI);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.form_csi);
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
                case 0 : return new SummaryCSITabFragment();
                case 1 : return new ReceiveDataTabFragment();
                case 2 : return new DetailsTabFragment();
                case 3 : return new ResultTabFragment();
                case 4 : return new DiagramTabFragment();
                case 5 : return new PhotoTabFragment();
                case 6 : return new VideoTabFragment();
                case 7 : return new VoiceTabFragment();
                //case 8 : return new NoteTabFragment();
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
                    return "รับแจ้ง";
                case 2 :
                    return "สภาพที่เกิดเหตุ";
                case 3 :
                    return "ผลตรวจ";
                case 4 :
                    return "แผนผัง";
                case 5 :
                    return "ภาพ";
                case 6 :
                    return "วิดีโอ";
                case 7 :
                    return "เสียง";
               // case 8 :
                 //   return "บันทึกย่อ";
            }
            return null;
        }
    }


}