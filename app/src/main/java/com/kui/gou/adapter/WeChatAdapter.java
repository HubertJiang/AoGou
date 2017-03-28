package com.kui.gou.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kui.gou.R;
import com.kui.gou.ViewHolder.LoadingViewHolder;
import com.kui.gou.ViewHolder.WeChatViewHolder;
import com.kui.gou.activity.WebActivity;
import com.kui.gou.entity.WeChat;

import java.util.List;

/**
 * Created by jiangkuiyuan on 16/9/4.
 */
public class WeChatAdapter extends BaseAdapter<WeChat> {
    public WeChatAdapter(Context context, RecyclerView recyclerView, List<WeChat> data) {
        super(context, recyclerView, data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        } else {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_we_chat, parent, false);
            return new WeChatViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadingViewHolder) {
            ((LoadingViewHolder) holder).loading.setVisibility(View.VISIBLE);
        } else if (holder instanceof WeChatViewHolder) {
            WeChatViewHolder weChatViewHolder = (WeChatViewHolder) holder;
            final WeChat data = getItem(position);
            weChatViewHolder.time.setText(data.pubTime);
            weChatViewHolder.title.setText(data.title);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), WebActivity.class);
                    intent.putExtra("url", data.sourceUrl);
                    intent.putExtra("title", data.title);
                    getContext().startActivity(intent);
                }
            });
        }
    }
}
