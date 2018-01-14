package com.example.smit.teseclient.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smit.teseclient.MainActivity;
import com.example.smit.teseclient.R;

public class Coluns extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_coluns,container,false);
        Log.d("TAG","Coluns" + MainActivity.pager.getCurrentItem());
        return v;
    }
}