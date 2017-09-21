package com.kui.gou.activity;

/**
 * Created by carmack on 2014/9/28.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.kui.gou.R;

import me.nereo.multi_image_selector.multiimages.ImageViewTouch;
import me.nereo.multi_image_selector.multiimages.ImageViewTouchViewPager;

public class ImageViewActivity extends Activity {
    private ImageViewTouchViewPager imageViewPager;
    private ImageAdapter adapter;
//    private ArrayList<BmobFile> images;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        imageViewPager = (ImageViewTouchViewPager) findViewById(R.id.pager);
//        images = (ArrayList<BmobFile>) getIntent().getSerializableExtra("images");
        index = getIntent().getIntExtra("index", 0);
        adapter = new ImageAdapter();
        imageViewPager.setAdapter(adapter);

        imageViewPager.setCurrentItem(index);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    class ImageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageViewTouch photoView = new ImageViewTouch(container.getContext(), null);

//            String imageUrl = images.get(position).getUrl();
//            Glide.with(AoApplication.getInstance()).load(imageUrl).into(photoView);
//            photoView.setTag(ImageViewTouchViewPager.VIEW_PAGER_OBJECT_TAG + position);
//            photoView.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
//            photoView.setSingleTapListener(new ImageViewTouch.OnImageViewTouchSingleTapListener() {
//
//                @Override
//                public void onSingleTapConfirmed() {
//                    finish();
//                }
//            });
//            container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            return photoView;
        }
    }
}

