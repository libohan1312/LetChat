package com.ltc.letchat.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/7/18.
 */
public class MainFragementPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> views;
    List<String> tabs;

    public MainFragementPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        views = fragments;
        tabs = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return views.get(position);
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position);
    }


    //    @Override
//    public int getCount() {
//        return views.size();
//    }
//
//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return view == object;
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView(views.get(position));
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        return null;
//    }
//
//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        container.addView(views.get(position));
//        return views.get(position);
//    }
//
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return tabs.get(position);
//    }

}
