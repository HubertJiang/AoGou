package com.kui.gou.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kui.gou.R;

/**
 * Created by jiangkuiyuan on 16/9/4.
 */
public class CookTopViewHolder extends RecyclerView.ViewHolder {
    public TextView text,content;
    public ImageView imageView;
    public CookTopViewHolder(View itemView) {
        super(itemView);
        text= (TextView) itemView.findViewById(R.id.text);
        content= (TextView) itemView.findViewById(R.id.content);
        imageView= (ImageView) itemView.findViewById(R.id.image);
    }
}
