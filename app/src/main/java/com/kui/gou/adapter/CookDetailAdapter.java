package com.kui.gou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.kui.gou.R;
import com.kui.gou.ViewHolder.CookTopViewHolder;
import com.kui.gou.ViewHolder.CookViewHolder;
import com.kui.gou.activity.AoApplication;
import com.kui.gou.entity.Cook;
import com.kui.gou.entity.CookStep;

/**
 * Created by jiangkuiyuan on 16/9/4.
 */
public class CookDetailAdapter extends BaseAdapter<CookStep> {
    private Cook cook;

    public CookDetailAdapter(Context context, RecyclerView recyclerView, Cook cook) {
        super(context, recyclerView, null);
        this.cook = cook;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_cook_top, parent, false);
            return new CookTopViewHolder(view);
        } else {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_cook_step, parent, false);
            return new CookViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CookTopViewHolder) {
            CookTopViewHolder weChatViewHolder = (CookTopViewHolder) holder;
            weChatViewHolder.text.setText(cook.sumary);
            weChatViewHolder.content.setText(cook.ingredients);
            weChatViewHolder.imageView.setImageResource(android.R.color.transparent);
            if (!TextUtils.isEmpty(cook.img))
                Glide.with(AoApplication.getInstance()).load(cook.img).placeholder(android.R.color.transparent).into(weChatViewHolder.imageView);

        } else {
            CookViewHolder weChatViewHolder = (CookViewHolder) holder;
            CookStep cookStep=getItem(position-1);
            weChatViewHolder.text.setText(cookStep.step);
            if (!TextUtils.isEmpty(cookStep.img))
                Glide.with(AoApplication.getInstance()).load(cookStep.img).placeholder(android.R.color.transparent).into(weChatViewHolder.imageView);

        }
    }
}
