package com.bezzy.Ui.View.activity.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bezzy_application.R;

import kr.pe.burt.android.lib.androidgradientimageview.AndroidGradientImageView;


public class ImageshowFragment extends Fragment {




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_imageshow, container, false);

        return view;
    }
}