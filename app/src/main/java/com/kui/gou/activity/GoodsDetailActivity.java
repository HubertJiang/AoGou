package com.kui.gou.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.kui.gou.R;
import com.kui.gou.adapter.ImageAdapter;
import com.kui.gou.entity.Goods;
import com.kui.gou.entity.User;
import com.kui.gou.view.CirclePageIndicator;

import java.util.ArrayList;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import io.rong.imkit.RongIM;

public class GoodsDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private String id;
    private TextView content, likesTextView, serviceTextView;
    private ViewPager viewPager;
    private CirclePageIndicator circlePageIndicator;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Goods goods;
    private ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        id = getIntent().getStringExtra("id");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        content = (TextView) findViewById(R.id.content);
        likesTextView = (TextView) findViewById(R.id.likes_text_view);
        serviceTextView = (TextView) findViewById(R.id.service_text_view);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setProgressViewOffset(true, 0, 500);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);

        adapter = new ImageAdapter();
        likesTextView.setOnClickListener(this);
        serviceTextView.setOnClickListener(this);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        BmobQuery<Goods> query = new BmobQuery<>();
        query.getObject(id, new QueryListener<Goods>() {

            @Override
            public void done(Goods object, BmobException e) {
                goods = object;
                swipeRefreshLayout.setRefreshing(false);
//                swipeRefreshLayout.setEnabled(false);
                if (e == null) {
                    getSupportActionBar().setTitle(object.name);
                    content.setText(object.content);
                    ArrayList<BmobFile> images = new ArrayList<>();
                    images.add(object.image);
                    if (object.image1 != null) {
                        images.add(object.image1);
                    }
                    if (object.image2 != null) {
                        images.add(object.image2);
                    }
                    if (object.image3 != null) {
                        images.add(object.image3);
                    }
                    adapter.setImages(images);
                    viewPager.setAdapter(adapter);
                    circlePageIndicator.setViewPager(viewPager);
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.likes_text_view:
                BmobRelation relation = new BmobRelation();
                relation.add(goods);
                User newUser = new User();
                newUser.setLikes(relation);
                newUser.update(BmobUser.getCurrentUser().getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.i("bmob", "多对多关联添加成功");
                        } else {
                            Log.i("bmob", "失败：" + e.getMessage());
                        }
                    }
                });
                break;
            case R.id.service_text_view:
                if (RongIM.getInstance() != null)
                    RongIM.getInstance().startPrivateChat(this, "TLW7444p", getString(R.string.service));
                break;
        }
    }
}
