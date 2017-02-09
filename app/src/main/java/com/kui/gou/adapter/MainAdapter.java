package com.kui.gou.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.kui.gou.R;
import com.kui.gou.ViewHolder.MainViewHolder;
import com.kui.gou.activity.GoodsDetailActivity;
import com.kui.gou.entity.Goods;
import com.kui.gou.util.FormatUtil;

import java.util.List;

/**
 * Created by jiangkuiyuan on 16/9/4.
 */
public class MainAdapter extends BaseAdapter<Goods> {
    public MainAdapter(Context context, RecyclerView recyclerView, List<Goods> data) {
        super(context, recyclerView, data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_main, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MainViewHolder) {
            final Goods goods = getItem(position);
            ((MainViewHolder) holder).name.setText(goods.name);
            ((MainViewHolder) holder).content.setText(goods.content);
            ((MainViewHolder) holder).money.setText(FormatUtil.moneyFormat(goods.price));
            if(goods.image!=null)
            Glide.with(getContext()).load(goods.image.getUrl()).into(((MainViewHolder)holder).imageView);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getContext(), GoodsDetailActivity.class);
                    intent.putExtra("id",goods.getObjectId());
                    getContext().startActivity(intent);
                }
            });
        }
    }
}
