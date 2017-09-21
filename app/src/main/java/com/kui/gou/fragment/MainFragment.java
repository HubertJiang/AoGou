package com.kui.gou.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kui.gou.R;
import com.kui.gou.activity.AoApplication;
import com.kui.gou.adapter.MainAdapter;
import com.kui.gou.entity.Goods;
import com.kui.gou.listener.OnLoadMoreListener;
import com.kui.gou.util.Constant;
import com.kui.gou.util.RetrofitFactory;
import com.kui.gou.view.RecycleViewDivider;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by jiangkuiyuan on 16/9/4.
 */
public class MainFragment extends Fragment {
    private RecyclerView recyclerView;
    private MainAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int page;
    private boolean hasMore;
    private String id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity()));
        adapter = new MainAdapter(getActivity(), recyclerView, null);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
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
//                    get(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
                }
            }
        });
        swipeRefreshLayout.setRefreshing(true);
        get();
        return view;
    }

    public void refresh(String id) {
        if (this.id == id)
            return;
        swipeRefreshLayout.setRefreshing(true);
        page = 0;
        this.id = id;
//        get(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
    }



    private void get() {
        RetrofitFactory.getInstance().getGoods().enqueue(new Callback<List<Goods>>() {
            @Override
            public void onResponse(Call<List<Goods>> call, Response<List<Goods>> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.body().size() < Constant.COUNT) {
                    hasMore = false;
                } else {
                    hasMore = true;
                }
                if (page == 0) {
                    adapter.setData(response.body());
                } else {
                    adapter.deleteNull();
                    adapter.addAll(response.body());
                }
                adapter.setLoaded();
                page++;
            }

            @Override
            public void onFailure(Call<List<Goods>> call, Throwable throwable) {
                adapter.setLoaded();
                AoApplication.showToast(R.string.no_network);
            }
        });

    }
}
