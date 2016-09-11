package me.nereo.multi_image_selector.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import me.nereo.multi_image_selector.R;

/**
 * Created by liweihui on 16/7/24.
 */
public class SelectImageAdapter extends RecyclerView.Adapter<SelectImageAdapter.ViewHolder> {
    private ArrayList<String> images = new ArrayList<>();
    private Context context;

    public SelectImageAdapter(Context context, ArrayList<String> images) {
        this.context = context;
        this.images = images;
    }


    @Override
    public int getItemCount() {
        switch (images.size()) {
            case 0:
                return 1;
            default:
                return 2;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item_image,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.imageView = (ImageView) view
                .findViewById(R.id.image_view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        switch (images.size()){
            case 0:
                holder.imageView.setImageResource(R.drawable.xuk);
                break;
            case 1:
                if (position == 0) {
                    Glide.with(context).load(images.get(position)).into(holder.imageView);
                    holder.imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((MultiImageSelectorActivity)context).update(images.get(position));
                        }
                    });
                }else {
                    holder.imageView.setImageResource(R.drawable.xuk);
                    holder.imageView.setOnClickListener(null);
                }
                break;
            default:
                Glide.with(context).load(images.get(position)).into(holder.imageView);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MultiImageSelectorActivity)context).update(images.get(position));
                    }
                });
                break;
        }

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
