package com.kuiyuan.aogou.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kuiyuan.aogou.R;
import com.kuiyuan.aogou.entity.Goods;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class GoodsDetailActivity extends AppCompatActivity {
    private String id;
    private TextView content;
    private ImageView imageView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        id=getIntent().getStringExtra("id");
        Toolbar  toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        content= (TextView) findViewById(R.id.content);
        imageView= (ImageView) findViewById(R.id.image);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setProgressViewOffset(true, 0, 500);
        swipeRefreshLayout.setRefreshing(true);
        BmobQuery<Goods> query = new BmobQuery<>();
        query.getObject(id, new QueryListener<Goods>() {

            @Override
            public void done(Goods object, BmobException e) {
//                swipeRefreshLayout.setRefreshing(false);
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
}
