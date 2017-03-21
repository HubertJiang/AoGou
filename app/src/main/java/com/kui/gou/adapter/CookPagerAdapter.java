package com.kui.gou.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.kui.gou.entity.CookInfo;
import com.kui.gou.fragment.CookFragment;

import java.util.List;

/**
 * Created by liweihui on 2017/3/8.
 */

public class CookPagerAdapter extends FragmentStatePagerAdapter {
    public CookPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private List<CookInfo> data;

    public void setData(List<CookInfo> data) {
        this.data = data;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new CookFragment();
        Bundle args = new Bundle();
        args.putString("index", data.get(i).categoryInfo.ctgId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return data.get(position).categoryInfo.name;
    }
}
