package com.kui.gou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kui.gou.R;
import com.kui.gou.activity.LikesActivity;
import com.kui.gou.activity.SignInActivity;
import com.sobot.chat.SobotApi;

import cn.bmob.v3.BmobUser;


public class SettingFragment extends Fragment implements View.OnClickListener {

    private Intent intent;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        view.findViewById(R.id.likes).setOnClickListener(this);
        view.findViewById(R.id.exit).setOnClickListener(this);
        view.findViewById(R.id.message).setOnClickListener(this);
        view.findViewById(R.id.user).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.count_text)).setText(SobotApi.getUnreadMsg(getActivity()) + "");
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.likes:
                startActivity(new Intent(getActivity(), LikesActivity.class));
                break;
            case R.id.exit:
                BmobUser.logOut();
                getActivity().finish();
                break;
            case R.id.message:

                break;
            case R.id.user:
                if (BmobUser.getCurrentUser() == null) {
                    intent = new Intent(getActivity(), SignInActivity.class);
                } else {

                }
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
