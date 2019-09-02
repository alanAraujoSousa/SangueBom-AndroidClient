package com.bom.sangue.sanguebom.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bom.sangue.sanguebom.R;

/**
 * Created by alan on 09/11/15.
 */
public class BloodCompatibility extends Fragment {

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.blood_compatibility, container,false);
        return rootView;
    }
}
