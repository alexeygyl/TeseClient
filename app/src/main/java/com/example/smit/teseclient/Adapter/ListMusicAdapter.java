package com.example.smit.teseclient.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.smit.teseclient.Units.MusicUnits;
import com.example.smit.teseclient.R;

import java.util.List;

public class ListMusicAdapter extends ArrayAdapter<MusicUnits>  {
    private final Context context;
    private List<MusicUnits> musicUnitses;

    public ListMusicAdapter(Context context, List<MusicUnits> musicUnitses) {
        super(context, R.layout.list_music,musicUnitses);
        this.context = context;
        this.musicUnitses =  musicUnitses;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.list_music, parent, false);

        }
        ProgressBar music_list_pg = (ProgressBar)convertView.findViewById(R.id.music_list_pg);
        music_list_pg.setMax(musicUnitses.get(position).MDuration);
        music_list_pg.setProgress(0);
        TextView textName = (TextView) convertView.findViewById(R.id.MusicName);
        TextView textAuthor = (TextView) convertView.findViewById(R.id.MusicAuthor);
        TextView textTime = (TextView) convertView.findViewById(R.id.MusicTime);
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);


        textName.setText(musicUnitses.get(position).Mtitle);
        textName.setTextColor(Color.parseColor("#ABABAB"));
        textName.setMaxWidth(320);
        textAuthor.setText(musicUnitses.get(position).MAuthor);
        textAuthor.setTextColor(Color.rgb(200,200,200));
        textTime.setText(musicUnitses.get(position).Mtime);

        String s = musicUnitses.get(position).MAuthor;
        return convertView;
    }
}
