package com.example.smit.teseclient.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.smit.teseclient.Fragments.Coluns;
import com.example.smit.teseclient.Fragments.List;
import com.example.smit.teseclient.Fragments.Music;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[];
    int NumbOfTabs;


    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                List list = new List();
                return list;
            case 1:
                Music music = new Music();
                return music;
            case 2:
                Coluns coluns = new Coluns();
                return coluns;
        }

        return null;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return  Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}