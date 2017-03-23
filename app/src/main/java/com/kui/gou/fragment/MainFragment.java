package com.kui.gou.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kui.gou.R;
import com.kui.gou.activity.AoApplication;
import com.kui.gou.adapter.MainAdapter;
import com.kui.gou.entity.Classify;
import com.kui.gou.entity.Goods;
import com.kui.gou.listener.OnLoadMoreListener;
import com.kui.gou.util.Constant;
import com.kui.gou.view.RecycleViewDivider;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by jiangkuiyuan on 16/9/4.
 */
public class MainFragment extends Fragment {
    private RecyclerView recyclerView;
    private MainAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int  page;
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
                get(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
            }
        });


//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView,
//                                             int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (hasMore && newState == RecyclerView.SCROLL_STATE_IDLE
//                        && lastVisibleItem + 1 == adapter.getItemCount()) {
//                    swipeRefreshLayout.setRefreshing(true);
//                    get(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
//            }
//        });

        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (hasMore) {
                    adapter.addNull();
                    get(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
                }
            }
        });
        swipeRefreshLayout.setRefreshing(true);
        get(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        return view;
    }

    public void refresh(String id) {
        if (this.id == id)
            return;
        swipeRefreshLayout.setRefreshing(true);
        page = 0;
        this.id = id;
        get(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
    }

    private void get(BmobQuery.CachePolicy cachePolicy) {
        BmobQuery<Goods> query = new BmobQuery<>();
        query.setCachePolicy(cachePolicy);
        query.addQueryKeys("name,content,image,price");
        if (!TextUtils.isEmpty(id)) {
            Classify classify = new Classify();
            classify.setObjectId(id);
            query.addWhereEqualTo("type", new BmobPointer(classify));
        }
        query.setLimit(Constant.COUNT);
        query.setSkip(Constant.COUNT * page);
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
                    adapter.setLoaded();
                    AoApplication.showToast(e.toString());
                }
            }
        });
    }
}
