package com.kui.gou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kui.gou.R;
import com.kui.gou.activity.CookActivity;
import com.kui.gou.activity.WeChatActivity;


public class FindFragment extends Fragment implements View.OnClickListener {

    public FindFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        view.findViewById(R.id.we_chat).setOnClickListener(this);
        view.findViewById(R.id.cook).setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.we_chat:
                startActivity(new Intent(getActivity(), WeChatActivity.class));
                break;

            case R.id.cook:
                startActivity(new Intent(getActivity(), CookActivity.class));
                break;
        }
    }
}
