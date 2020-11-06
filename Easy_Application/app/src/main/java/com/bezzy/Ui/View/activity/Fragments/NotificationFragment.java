package com.bezzy.Ui.View.activity.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bezzy.Ui.View.model.Friendsnoti_item;
import com.bezzy_application.R;

import java.util.ArrayList;


public class NotificationFragment extends Fragment {
    ArrayList<Friendsnoti_item> dataholder;
    RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_notification, container, false);
        recyclerView=view.findViewById(R.id.noti_listf);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        dataholder=new ArrayList<>();
        return view;

    }
}