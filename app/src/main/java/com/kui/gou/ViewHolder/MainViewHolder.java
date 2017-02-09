package com.kui.gou.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kui.gou.R;

/**
 * Created by jiangkuiyuan on 16/9/4.
 */
public class MainViewHolder extends RecyclerView.ViewHolder {
    public TextView name,content,money;
    public ImageView imageView;
    public MainViewHolder(View itemView) {
        super(itemView);
        name= (TextView) itemView.findViewById(R.id.name);
        content= (TextView) itemView.findViewById(R.id.content);
        money= (TextView) itemView.findViewById(R.id.money);
        imageView= (ImageView) itemView.findViewById(R.id.imageView);
    }
}
