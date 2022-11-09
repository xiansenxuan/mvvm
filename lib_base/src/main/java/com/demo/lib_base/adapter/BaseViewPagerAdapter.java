package com.demo.lib_base.adapter;

import android.annotation.SuppressLint;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * BaseViewPagerAdapter
 */

public class BaseViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;
    private List<String> mTitleList;
    private String[] mTitles;

    @SuppressLint("WrongConstant")
    public BaseViewPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragments, String[] mTitles) {
        super(fragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = fragments;
        this.mTitles = mTitles;
    }

    @SuppressLint("WrongConstant")
    public BaseViewPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragments) {
        super(fragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = fragments;
    }

    @SuppressLint("WrongConstant")
    public BaseViewPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragments, List<String> mTitleList) {
        super(fragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = fragments;
        this.mTitleList = mTitleList;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(mTitles!=null && mTitles.length>0){
            return mTitles[position];
        }else if(mTitleList!=null && mTitleList.size()>0){
            return mTitleList.get(position);
        }else {
            return "无标题";
        }
    }

    @Override
    public int getCount() {
        return fragments!=null?fragments.size():0;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    public void setPagerTitle(String[] mTitles){
        this.mTitles=mTitles;
        notifyDataSetChanged();
    }
}
