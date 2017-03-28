package com.kui.gou.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kui.gou.R;
import com.kui.gou.activity.AoApplication;
import com.kui.gou.adapter.CookAdapter;
import com.kui.gou.entity.ApiResponse;
import com.kui.gou.entity.CookItem;
import com.kui.gou.listener.OnLoadMoreListener;
import com.kui.gou.util.Constant;
import com.kui.gou.util.RetrofitFactory;
import com.kui.gou.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CookFragment extends Fragment implements Callback<ApiResponse> {
    private RecyclerView recyclerView;
    private CookAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean hasMore;
    private int  page;
    private String id = "1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_recycler, container, false);
        Bundle args = getArguments();
        id = args.getString("index");
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity()));
        adapter = new CookAdapter(getActivity(), recyclerView, null);
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
                    page++;
                    get();
                }
            }
        });

        swipeRefreshLayout.setRefreshing(true);
        get();
        return view;
    }

    private void get() {
        RetrofitFactory.getInstance().getCook(Constant.API_KEY, id, page + 1, Constant.COUNT).enqueue(this);
    }


    @Override
    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
        swipeRefreshLayout.setRefreshing(false);
        if (response.body().retCode == 200) {
            int total = response.body().result.getAsJsonObject().get("total").getAsInt();
            int totalPage = total / Constant.COUNT;
            if (total % Constant.COUNT > 0) {
                totalPage += 1;
            }
            if (page + 1 < totalPage) {
                hasMore = true;
            } else {
                hasMore = false;
            }
            Gson gson = new Gson();
            ArrayList<CookItem> resultList = gson.fromJson(
                    response.body().result.getAsJsonObject().get("list").getAsJsonArray(),
                    new TypeToken<List<CookItem>>() {
                    }.getType
                            ());
            adapter.addAll(resultList);
        } else {
            AoApplication.showToast(R.string.no_network);
        }
    }

    @Override
    public void onFailure(Call<ApiResponse> call, Throwable t) {
        swipeRefreshLayout.setRefreshing(false);
        AoApplication.showToast(R.string.no_network);
    }
}
