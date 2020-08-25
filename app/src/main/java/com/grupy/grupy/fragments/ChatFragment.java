package com.grupy.grupy.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.grupy.grupy.R;
import com.grupy.grupy.adapters.ChatsAdapter;

/**
 * A simple {@Link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    ChatsAdapter mAdapter;
    RecyclerView mRecycleView;
    View mView;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_chat, container, false);
        return  mView;
    }
}
