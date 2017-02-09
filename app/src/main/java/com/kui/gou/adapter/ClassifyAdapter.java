package com.kui.gou.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kui.gou.R;
import com.kui.gou.entity.Classify;

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

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.title_spinner, parent, false);
        TextView label = (TextView) view.findViewById(R.id.text_view_title);
        label.setText(objects.get(position).name);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_classify, parent, false);
        TextView label = (TextView) view.findViewById(R.id.text_view);
        label.setText(objects.get(position).name);
//        if (gradeSpinner.getSelectedItemPosition() == position) {
//            view.setBackgroundColor(getResources().getColor(
//                    R.color.spinner_green));
//        } else {
//            view.setBackgroundColor(getResources().getColor(
//                    R.color.spinner_light_green));
//        }
        return view;
    }

}
