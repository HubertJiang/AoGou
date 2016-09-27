package com.kuiyuan.aogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kuiyuan.aogou.R;
import com.kuiyuan.aogou.entity.Classify;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liweihui on 2016/9/27.
 */

public class ClassifyAdapter extends ArrayAdapter<Classify> {
    private List<Classify> objects;
    private Context context;

    public ClassifyAdapter(Context context, int resource) {
        super(context, resource, new ArrayList<Classify>());
        this.context = context;
    }

    public void setObjects(List<Classify> objects) {
        clear();
        addAll(objects);
        this.objects=objects;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.item_classify, parent, false);
        TextView label = (TextView) row.findViewById(R.id.text_view);
        label.setText(objects.get(position).name);

        if (position == 0) {//Special style for dropdown header
            label.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }

        return row;
    }
}
