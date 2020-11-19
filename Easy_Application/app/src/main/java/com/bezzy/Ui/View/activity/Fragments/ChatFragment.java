package com.bezzy.Ui.View.activity.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bezzy.Ui.View.model.Chatlist_item;
import com.bezzy.Ui.View.model.Notification_item;
import com.bezzy_application.R;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {
    RecyclerView chatlist;
    ArrayList<Chatlist_item> dataholder;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        chatlist=view.findViewById(R.id.chatlist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        chatlist.setLayoutManager(linearLayoutManager);

        dataholder=new ArrayList<>();
        return view;
    }
}