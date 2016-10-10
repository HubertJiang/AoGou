package com.kuiyuan.aogou.adapter;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kuiyuan.aogou.activity.AoApplication;
import com.kuiyuan.aogou.activity.ImageViewActivity;

import java.util.ArrayList;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by liweihui on 2016/10/10.
 */

public class ImageAdapter extends PagerAdapter {
    private ArrayList<BmobFile> images;

    public void setImages(ArrayList<BmobFile> images) {
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
    public Object instantiateItem(final ViewGroup container, final int position) {
        ImageView photoView = new ImageView(container.getContext(), null);

        String imageUrl = images.get(position).getUrl();
        Glide.with(AoApplication.getInstance()).load(imageUrl).into(photoView);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(container.getContext(), ImageViewActivity.class);
                intent.putExtra("images", images);
                intent.putExtra("index", position);
                container.getContext().startActivity(intent);
            }
        });
        container.addView(photoView, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);

        return photoView;
    }
}
