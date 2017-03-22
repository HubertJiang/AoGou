package com.kui.gou.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kui.gou.R;
import com.kui.gou.adapter.CookDetailAdapter;
import com.kui.gou.entity.Cook;
import com.kui.gou.entity.CookStep;
import com.kui.gou.view.RecycleViewDivider;

import java.util.List;

public class CookDetailActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private CookDetailAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Cook cook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_recycler);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecycleViewDivider(this));
        cook = (Cook) getIntent().getSerializableExtra("cook");
        getSupportActionBar().setTitle(cook.title);
        Gson gson=new Gson();
        List<CookStep> data=gson.fromJson(cook.method, new TypeToken<List<CookStep>>() {
        }.getType
                ());
        adapter = new CookDetailAdapter(this, recyclerView, cook);
        adapter.setData(data);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        swipeRefreshLayout.setEnabled(false);
    }


}
