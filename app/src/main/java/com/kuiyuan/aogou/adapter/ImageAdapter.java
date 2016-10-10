package com.kuiyuan.aogou.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kuiyuan.aogou.activity.AoApplication;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by liweihui on 2016/10/10.
 */

public class ImageAdapter extends PagerAdapter {
    private List<BmobFile> images;

    public void setImages(List<BmobFile> images) {
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView photoView = new ImageView(container.getContext(), null);

        String imageUrl = images.get(position).getUrl();
        Glide.with(AoApplication.getInstance()).load(imageUrl).into(photoView);

        container.addView(photoView, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);

        return photoView;
    }
}
