package com.kui.gou.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.kui.gou.R;


public class LoadingViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar loading;

    public LoadingViewHolder(View itemView) {
        super(itemView);
        loading = (ProgressBar) itemView.findViewById(R.id.progressBar);

    }
}
