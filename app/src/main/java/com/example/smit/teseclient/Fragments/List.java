package com.example.smit.teseclient.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.smit.teseclient.Adapter.ListMusicAdapter;
import com.example.smit.teseclient.General;
import com.example.smit.teseclient.MainActivity;
import com.example.smit.teseclient.R;
import com.example.smit.teseclient.Units.MusicUnits;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class List extends Fragment {

    public static java.util.List<MusicUnits> musicUnits = new ArrayList<MusicUnits>();
    public  static ListMusicAdapter adapter;
    ListView listmusic;
    Integer lastMusPos = 0;
    ImageView itemSelected;
    public static ProgressBar musicProgress;
    public static TextView musicProgressTime;
    ImageView icon;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_list,container,false);
        init(v);
        adapter = new ListMusicAdapter(getContext(), musicUnits);
        listmusic.setAdapter(adapter);
        //listmusic.setDivider(null);
        Log.d("TAG","List" + MainActivity.pager.getCurrentItem());

        listmusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                General.startPlay(musicUnits.get(i).Mid);
                icon = (ImageView) view.findViewById(R.id.musicListPlay);
                if(itemSelected == null){
                    lastMusPos = i;
                    musicProgress = (ProgressBar)view.findViewById(R.id.music_list_pg);
                    musicProgressTime = (TextView) view.findViewById(R.id.MusicTime);
                    icon.setBackgroundResource(R.drawable.ic_pause_circle_outline_white_24dp);
                    itemSelected = icon;
                }
                else {
                    musicProgress.setProgress(0);
                    musicProgress = (ProgressBar)view.findViewById(R.id.music_list_pg);
                    musicProgressTime.setText(musicUnits.get(lastMusPos).Mtime);
                    musicProgressTime = (TextView) view.findViewById(R.id.MusicTime);
                    icon.setBackgroundResource(R.drawable.ic_pause_circle_outline_white_24dp);
                    itemSelected.setBackgroundResource(R.drawable.ic_play_circle_outline_white_24dp);
                    itemSelected = icon;
                }

            }
        });

        return v;
    }

    protected void init(View v){
        listmusic = (ListView)v.findViewById(R.id.listViewMusic);

    }

    public  static void updateList(){
        adapter.notifyDataSetChanged();
    }

    public  static void setSeekBar(int position){
        try{
            musicProgress.setProgress(position);
            musicProgressTime.setText(General.getStrTime(position));
        }catch (NullPointerException e){

        }


    }



}