package com.kui.gou.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kui.gou.R;
import com.kui.gou.activity.AoApplication;
import com.kui.gou.adapter.WeChatAdapter;
import com.kui.gou.util.Constant;
import com.kui.gou.view.RecycleViewDivider;
import com.mob.mobapi.API;
import com.mob.mobapi.APICallback;
import com.mob.mobapi.MobAPI;
import com.mob.mobapi.apis.WxArticle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class WeChatFragment extends Fragment implements APICallback {
    private RecyclerView recyclerView;
    private WeChatAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean hasMore;
    private WxArticle api;
    private int lastVisibleItem, page;
    private String id = "1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_recycler, container, false);
        Bundle args = getArguments();
        id = args.getString("index");
        api = (WxArticle) MobAPI.getAPI(WxArticle.NAME);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity()));
        adapter = new WeChatAdapter(getActivity(), recyclerView, null);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                get();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (hasMore && newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == adapter.getItemCount()) {
                    swipeRefreshLayout.setRefreshing(true);
                    page++;
                    get();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
            }
        });
        swipeRefreshLayout.setRefreshing(true);
        get();
        return view;
    }

    private void get() {
        api.searchArticleList(id, page + 1, Constant.COUNT, this);
    }

    @Override
    public void onSuccess(API api, int i, Map<String, Object> result) {
        swipeRefreshLayout.setRefreshing(false);
        if (result.get("retCode").equals("200")) {
            result = (Map<String, Object>) result.get("result");
            int total = (int) result.get("total");
            int totalPage = total / Constant.COUNT;
            if (total % Constant.COUNT > 0) {
                totalPage += 1;
            }
            if (page + 1 < totalPage) {
                hasMore = true;
            } else {
                hasMore = false;
            }
            ArrayList<HashMap<String, Object>> resultList = (ArrayList<HashMap<String, Object>>) result.get("list");
            adapter.addAll(resultList);
        } else {
            AoApplication.showToast(R.string.no_network);
        }

    }

    @Override
    public void onError(API api, int i, Throwable throwable) {
        swipeRefreshLayout.setRefreshing(false);
        AoApplication.showToast(R.string.no_network);
    }
}
