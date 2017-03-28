package com.kui.gou.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.kui.gou.entity.WeChat;
import com.kui.gou.fragment.WeChatFragment;

import java.util.List;

/**
 * Created by liweihui on 2017/3/8.
 */

public class CollectionPagerAdapter extends FragmentStatePagerAdapter {
    public CollectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private List<WeChat> data;

    public void setData(List<WeChat> data) {
        this.data = data;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new WeChatFragment();
        Bundle args = new Bundle();
        args.putString("index", data.get(i).cid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return data.get(position).name;
    }
}
