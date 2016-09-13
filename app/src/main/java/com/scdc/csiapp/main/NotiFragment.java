package com.scdc.csiapp.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scdc.csiapp.R;

/**
 * Created by Pantearz07 on 22/9/2558.
 */
public class NotiFragment extends Fragment {

    CoordinatorLayout rootLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View x =  inflater.inflate(R.layout.noti_layout,null);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.notification);

        rootLayout = (CoordinatorLayout) x.findViewById(R.id.rootLayout);

        return x;
    }

}
