package com.kui.gou.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kui.gou.R;
import com.kui.gou.ViewHolder.WeChatViewHolder;
import com.kui.gou.activity.WebActivity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jiangkuiyuan on 16/9/4.
 */
public class WeChatAdapter extends BaseAdapter<HashMap<String, Object>> {
    public WeChatAdapter(Context context, RecyclerView recyclerView, List<HashMap<String, Object>> data) {
        super(context, recyclerView, data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_we_chat, parent, false);
        return new WeChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof WeChatViewHolder) {
            WeChatViewHolder weChatViewHolder= (WeChatViewHolder) holder;
            final HashMap<String, Object> data = getItem(position);
            weChatViewHolder.time.setText(String.valueOf(data.get("pubTime")));
            weChatViewHolder.title.setText(String.valueOf(data.get("title")));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getContext(), WebActivity.class);
                    intent.putExtra("url",String.valueOf(data.get("sourceUrl")));
                    intent.putExtra("title",String.valueOf(data.get("title")));
                    getContext().startActivity(intent);
                }
            });
        }
    }
}
