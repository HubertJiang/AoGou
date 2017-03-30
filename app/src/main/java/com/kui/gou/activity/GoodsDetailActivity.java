package com.kui.gou.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.kui.gou.R;
import com.kui.gou.adapter.ImageAdapter;
import com.kui.gou.entity.Goods;
import com.kui.gou.entity.User;
import com.kui.gou.util.Constant;
import com.kui.gou.view.CirclePageIndicator;
import com.sobot.chat.SobotApi;
import com.sobot.chat.api.model.ConsultingContent;
import com.sobot.chat.api.model.Information;

import java.util.ArrayList;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener {
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

        content = (TextView) findViewById(R.id.content);
        likesTextView = (TextView) findViewById(R.id.likes_text_view);
        serviceTextView = (TextView) findViewById(R.id.service_text_view);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
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
                Information info = new Information();
                info.setAppkey(Constant.SERVICE_KEY);
                info.setColor("#3F51B5");
                info.setUname(BmobUser.getCurrentUser(User.class).getNickname());
                info.setPhone(BmobUser.getCurrentUser(User.class).getMobilePhoneNumber());
                info.setFace(BmobUser.getCurrentUser(User.class).getAvatar() == null ? null : BmobUser.getCurrentUser(User.class).getAvatar().getUrl());
                //咨询内容
                ConsultingContent consultingContent = new ConsultingContent();
                //咨询内容标题，必填
                consultingContent.setSobotGoodsTitle(goods.name);
                //咨询内容图片，选填 但必须是图片地址
                consultingContent.setSobotGoodsImgUrl(goods.image.getFileUrl());
                //描述，选填
                consultingContent.setSobotGoodsDescribe(goods.content);
                //标签，选填
                consultingContent.setSobotGoodsLable(goods.price + "");
                consultingContent.setSobotGoodsFromUrl(goods.getObjectId());
                info.setConsultingContent(consultingContent);
                SobotApi.startSobotChat(this, info);
                break;
        }
    }
}
