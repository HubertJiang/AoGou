package com.kui.gou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kui.gou.R;
import com.kui.gou.activity.AoApplication;
import com.kui.gou.activity.InformationActivity;
import com.kui.gou.activity.LikesActivity;
import com.kui.gou.activity.SignInActivity;
import com.kui.gou.entity.User;
import com.kui.gou.util.Constant;
import com.kui.gou.util.RetrofitFactory;
import com.sobot.chat.SobotApi;
import com.sobot.chat.api.model.Information;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingFragment extends Fragment implements View.OnClickListener {

    private Intent intent;
    private ImageView avatarImage;
    private TextView nicknameText;
    private User user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        view.findViewById(R.id.likes).setOnClickListener(this);

        view.findViewById(R.id.message).setOnClickListener(this);
        view.findViewById(R.id.user).setOnClickListener(this);
        avatarImage = (ImageView) view.findViewById(R.id.avatar_image);
        nicknameText = (TextView) view.findViewById(R.id.nickname);

//        ((TextView) view.findViewById(R.id.count_text)).setText(SobotApi.getUnreadMsg(getActivity()) + "");
        return view;
    }

    private void getUser() {
        RetrofitFactory.getInstance().getUser(AoApplication.getUserId()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    if (user == null) {
                        avatarImage.setImageResource(R.mipmap.ic_avatar);
                        nicknameText.setText(R.string.sign_in_hint);
                    } else {
                        nicknameText.setText(user.nickname);
                        AoApplication.collection=user.collection;
                        if (user.avatar != null) {
                            Glide.with(AoApplication.getInstance()).load(user.avatar.url).into(avatarImage);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                AoApplication.showToast(R.string.no_network);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (user == null) {
            startActivity(new Intent(getActivity(), SignInActivity.class));
        } else {
            switch (v.getId()) {
                case R.id.likes:
                    startActivity(new Intent(getActivity(), LikesActivity.class));
                    break;
                case R.id.message:
                    Information info = new Information();
                    info.setAppkey(Constant.SERVICE_KEY);
                    info.setColor("#3F51B5");
//                    info.setUname(user.getNickname());
//                    info.setPhone(user.getMobilePhoneNumber());
//                    info.setFace(user.getAvatar() == null ? null : user.getAvatar().getUrl());
                    SobotApi.startSobotChat(getActivity(), info);
                    break;
                case R.id.user:
                    Intent intent=new Intent(getActivity(), InformationActivity.class);
                    intent.putExtra("user",user);
                    startActivity(intent);
                    break;
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(AoApplication.getUserId())) {
            user=null;
            avatarImage.setImageResource(R.mipmap.ic_avatar);
            nicknameText.setText(R.string.sign_in_hint);
        } else {
            getUser();
        }
    }
}
