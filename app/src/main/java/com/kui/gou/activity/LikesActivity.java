package com.kui.gou.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kui.gou.R;
import com.kui.gou.adapter.MainAdapter;
import com.kui.gou.entity.Goods;
import com.kui.gou.listener.OnLoadMoreListener;
import com.kui.gou.util.RetrofitFactory;
import com.kui.gou.view.RecycleViewDivider;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LikesActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private MainAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int page;
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
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        for (String s : AoApplication.collection) {
            jsonArray.add(s);
        }
        jsonObject.add("inq", jsonArray);
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.add("id", jsonObject);
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.add("where", jsonObject1);
        RetrofitFactory.getInstance().getGoods(jsonObject2.toString()).enqueue(new Callback<List<Goods>>() {
            @Override
            public void onResponse(Call<List<Goods>> call, Response<List<Goods>> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()) {
                    adapter.setData(response.body());
                } else {
                    AoApplication.showToast(R.string.no_network);
                }
            }

            @Override
            public void onFailure(Call<List<Goods>> call, Throwable throwable) {
                swipeRefreshLayout.setRefreshing(false);
                adapter.setLoaded();
                AoApplication.showToast(R.string.no_network);
            }
        });
    }
}
