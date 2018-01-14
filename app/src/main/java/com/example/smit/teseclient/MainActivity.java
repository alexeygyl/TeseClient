package com.example.smit.teseclient;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.smit.teseclient.Adapter.ViewPagerAdapter;
import com.example.smit.teseclient.Fragments.List;
import com.example.smit.teseclient.Google.SlidingTabLayout;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public static Handler handler = new Handler();
    public static ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    RelativeLayout mainLayout;
    Drawable DmainView;
    CharSequence Titles[]={"List","Music","Coluns"};
    int Numboftabs =3 ;
    TimerTask timerTask;
    public static Timer timer;

    static Integer PLAY =1;
    static Integer PAUSE = 2;
    static  Integer STOP =0;
    static Integer status=STOP;
    public  static int lastMusPos = 0;

    ProgressBar mVolumeProgress;
    TextView volumeTV;
    public static int volume = 30;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //mVolumeProgress = (ProgressBar)findViewById(R.id.mVolumeProgress);
        volumeTV = (TextView)findViewById(R.id.Volume);
        mainLayout = (RelativeLayout)findViewById(R.id.Main);
        try {
            DmainView  = Drawable.createFromStream(getAssets().open("icons/mainbg2.jpg"), null);
            mainLayout.setBackground(DmainView);
        } catch (IOException e) {
            e.printStackTrace();
        }
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("onPageScrolled","" + pager.getCurrentItem());
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("onPageSelected","" + pager.getCurrentItem());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("onPageChanged","" + pager.getCurrentItem());
            }
        });
        pager.setAdapter(adapter);


        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabScrollColor);
            }
        });
        Log.d( "------------>", getSupportFragmentManager().getClass().getSimpleName());
        tabs.setViewPager(pager);
        General.runUDPserver("192.168.1.203",(short) 9999);


        //General.getMusicList();
        startMusicListUpdateSheduler();
    }

    private void startMusicListUpdateSheduler(){
        Log.d("startMusicListUpdate","Start");
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if(General.serverAddr !=null){
                    Log.d("startMusicListUpdate","Yes");
                    General.getMusicList();
                }
            }
        };
        timer = new Timer();
        timer.schedule(timerTask,2000,2000);

    }

    public static void stopMusicListUpdateSheduler(){
        timer.cancel();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        if (keyCode == 24) {
            Log.d("---->","" + keyCode);
            volumeTV.setCursorVisible(true);
            volumeTV.setVisibility(View.VISIBLE);

            //mVolumeProgress.setVisibility(View.VISIBLE);
            if(volume<100) {
                volume++;
                volumeTV.setText(String.valueOf(volume));
                //mVolumeProgress.setProgress(volume);
                General.changeVolume(volume);
                //isVolumeChanged = true;
            }
            return true;
        }else if(keyCode == 25){
            Log.d("---->","" + keyCode);
            //mVolumeProgress.setVisibility(View.VISIBLE);
            volumeTV.setVisibility(View.VISIBLE);
            if(volume>0) {
                volume--;
                volumeTV.setText(String.valueOf(volume));
                //mVolumeProgress.setProgress(volume);
                General.changeVolume(volume);
                //isVolumeChanged = true;
            }
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 24) {
            volumeTV.setVisibility(View.INVISIBLE);
            //mVolumeProgress.setVisibility(View.INVISIBLE);
            return true;
        }else if(keyCode == 25){
            //mVolumeProgress.setVisibility(View.INVISIBLE);
            volumeTV.setVisibility(View.INVISIBLE);
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }



}
