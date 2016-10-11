package com.kuiyuan.aogou.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kuiyuan.aogou.R;
import com.kuiyuan.aogou.adapter.MainAdapter;
import com.kuiyuan.aogou.entity.Classify;
import com.kuiyuan.aogou.entity.Goods;
import com.kuiyuan.aogou.util.Constant;
import com.kuiyuan.aogou.view.RecycleViewDivider;

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
    private int lastVisibleItem, page;
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

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
//        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimaryDark);
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

    public void refresh(String id) {
        if (this.id == id)
            return;
        page=0;
        this.id = id;
        get();
    }

    private void get() {
        BmobQuery<Goods> query = new BmobQuery<>();
//        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.addQueryKeys("name,content,image,price");
        if (!TextUtils.isEmpty(id)) {
            Classify classify = new Classify();
            classify.setObjectId(id);
            query.addWhereEqualTo("type", new BmobPointer(classify));
        }
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
                        adapter.addAll(object);
                    }
                    page++;
                } else {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
