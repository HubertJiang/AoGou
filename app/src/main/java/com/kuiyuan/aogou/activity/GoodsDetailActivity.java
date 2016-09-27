package com.kuiyuan.aogou.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kuiyuan.aogou.R;
import com.kuiyuan.aogou.entity.Goods;
import com.kuiyuan.aogou.entity.User;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class GoodsDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private String id;
    private TextView content,likesTextView;
    private ImageView imageView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Goods goods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        id=getIntent().getStringExtra("id");
        Toolbar  toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        content= (TextView) findViewById(R.id.content);
        likesTextView= (TextView) findViewById(R.id.likes_text_view);
        imageView= (ImageView) findViewById(R.id.image);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setProgressViewOffset(true, 0, 500);

        likesTextView.setOnClickListener(this);

        swipeRefreshLayout.setRefreshing(true);
        BmobQuery<Goods> query = new BmobQuery<>();
        query.getObject(id, new QueryListener<Goods>() {

            @Override
            public void done(Goods object, BmobException e) {
                goods=object;
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setEnabled(false);
                if(e==null){
                    getSupportActionBar().setTitle(object.name);
                    content.setText(object.content);
                    Glide.with(GoodsDetailActivity.this).load(object.image.getUrl()).into(imageView);
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
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
        switch (v.getId()){
            case R.id.likes_text_view:
                BmobRelation relation=new BmobRelation();
                relation.add(goods);
                User newUser = new User();
                newUser.setLikes(relation);
                newUser.update(BmobUser.getCurrentUser().getObjectId(),new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Log.i("bmob","多对多关联添加成功");
                        }else{
                            Log.i("bmob","失败："+e.getMessage());
                        }
                    }
                });
                break;
        }
    }
}