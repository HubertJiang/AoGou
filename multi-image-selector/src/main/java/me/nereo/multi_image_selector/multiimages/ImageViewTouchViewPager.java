package me.nereo.multi_image_selector.multiimages;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ImageViewTouchViewPager extends ViewPager {

    public static final String VIEW_PAGER_OBJECT_TAG = "image#";
    private static final String TAG = "ImageViewTouchViewPager";
    private int previousPosition;

    private OnPageSelectedListener onPageSelectedListener;

    public ImageViewTouchViewPager(Context context) {
        super(context);
        init();
    }

    public ImageViewTouchViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public void setOnPageSelectedListener(OnPageSelectedListener listener) {
        onPageSelectedListener = listener;
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof ImageViewTouch) {
            return ((ImageViewTouch) v).canScroll(dx);
        } else {
            return super.canScroll(v, checkV, dx, x, y);
        }
    }

    private void init() {
        previousPosition = getCurrentItem();

        addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (onPageSelectedListener != null) {
                    onPageSelectedListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == SCROLL_STATE_SETTLING
                        && previousPosition != getCurrentItem()) {
                    try {
                        ImageViewTouch imageViewTouch = (ImageViewTouch) findViewWithTag(VIEW_PAGER_OBJECT_TAG
                                + getCurrentItem());
                        if (imageViewTouch != null) {
                            imageViewTouch.zoomTo(1, 1);
                        }

                        previousPosition = getCurrentItem();
                    } catch (ClassCastException ex) {
                        Log.e(TAG,
                                "This view pager should have only ImageViewTouch as a children.",
                                ex);
                    }
                }
            }
        });
    }

    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
        super.onLayout(arg0, arg1, arg2, arg3, arg4);
    }

    public interface OnPageSelectedListener {

        public void onPageSelected(int position);

    }
}
