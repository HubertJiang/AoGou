package com.kui.gou.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kui.gou.R;

/**
 * Created by jiangkuiyuan on 16/9/4.
 */
public class WeChatViewHolder extends RecyclerView.ViewHolder {
    public TextView title, time;

    public WeChatViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        time = (TextView) itemView.findViewById(R.id.time);
    }
}
