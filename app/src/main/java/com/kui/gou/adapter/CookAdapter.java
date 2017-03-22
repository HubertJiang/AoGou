package com.kui.gou.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.kui.gou.R;
import com.kui.gou.ViewHolder.MainViewHolder;
import com.kui.gou.activity.AoApplication;
import com.kui.gou.activity.CookDetailActivity;
import com.kui.gou.entity.CookItem;

import java.util.List;

/**
 * Created by jiangkuiyuan on 16/9/4.
 */
public class CookAdapter extends BaseAdapter<CookItem> {
    public CookAdapter(Context context, RecyclerView recyclerView, List<CookItem> data) {
        super(context, recyclerView, data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_cook, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MainViewHolder) {
            MainViewHolder weChatViewHolder = (MainViewHolder) holder;
            final CookItem data = getItem(position);
            weChatViewHolder.name.setText(data.name);
            weChatViewHolder.content.setText(data.ctgTitles);
            weChatViewHolder.imageView.setImageResource(android.R.color.transparent);
            if (!TextUtils.isEmpty(data.thumbnail))
                Glide.with(AoApplication.getInstance()).load(data.thumbnail).placeholder(android.R.color.transparent).into(weChatViewHolder.imageView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), CookDetailActivity.class);
                    intent.putExtra("cook", data.recipe);
                    getContext().startActivity(intent);
                }
            });
        }
    }
}
