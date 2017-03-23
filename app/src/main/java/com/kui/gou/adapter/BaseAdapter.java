package com.kui.gou.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.kui.gou.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<T> data;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
//    private OnScrollListener onScrollListener;

    public BaseAdapter(Context context, RecyclerView recyclerView, List<T> data) {
        this.context = context;
        this.data = data == null ? new ArrayList<T>() : new ArrayList<>(data);
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();

            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
//                            if (onScrollListener != null) {
//                                onScrollListener.onScrolled();
//                            }
                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }

//    public void setOnScrollListener(OnScrollListener onScrollListener) {
//        this.onScrollListener = onScrollListener;
//    }
//
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setData(List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return data;
    }

    public void addAll(List<T> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void addNull() {
        this.data.add(null);
        notifyItemInserted(data.size() - 1);
    }

    public void deleteNull() {
        this.data.remove(data.size() - 1);
        notifyItemRemoved(data.size());
    }

    public void setLoaded() {
        loading = false;
    }

    public Context getContext() {
        return context;
    }

    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position) == null ? 0 : 1;
    }

    @Override
    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position);
}
