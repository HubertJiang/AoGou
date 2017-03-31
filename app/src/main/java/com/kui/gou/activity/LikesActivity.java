package com.kui.gou.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kui.gou.R;
import com.kui.gou.adapter.MainAdapter;
import com.kui.gou.entity.Goods;
import com.kui.gou.listener.OnLoadMoreListener;
import com.kui.gou.util.Constant;
import com.kui.gou.view.RecycleViewDivider;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class LikesActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private MainAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int  page;
    private boolean hasMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);
        getSupportActionBar().setTitle(R.string.my_likes);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecycleViewDivider(this));
        adapter = new MainAdapter(this, recyclerView, null);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                get();
            }
        });

        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (hasMore) {
                    adapter.addNull();
                    get();
                }
            }
        });
        swipeRefreshLayout.setRefreshing(true);
        get();
    }

    private void get() {
        BmobQuery<Goods> query = new BmobQuery<>();
//        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.addWhereRelatedTo("likes", new BmobPointer(BmobUser.getCurrentUser()));
//查询playerName叫“比目”的数据
//返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(Constant.COUNT);
        query.setSkip(Constant.COUNT * page);
//执行查询方法
        query.findObjects(new FindListener<Goods>() {
            @Override
            public void done(List<Goods> object, BmobException e) {
                swipeRefreshLayout.setRefreshing(false);
                if (e == null) {
                    if (object.size() < Constant.COUNT) {
                        hasMore = false;
                    } else {
                        hasMore = true;
                    }
                    if (page == 0) {
                        adapter.setData(object);
                    } else {
                        adapter.deleteNull();
                        adapter.addAll(object);
                    }
                    adapter.setLoaded();
                    page++;
                } else {
                    AoApplication.showToast(R.string.no_network);
                }
            }
        });
    }
}
