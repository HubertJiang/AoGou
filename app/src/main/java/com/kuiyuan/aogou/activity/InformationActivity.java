package com.kuiyuan.aogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kuiyuan.aogou.R;
import com.kuiyuan.aogou.entity.User;

import cn.bmob.v3.BmobUser;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by Administrator on 2015/3/6.
 */
public class InformationActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView addressTextView, signatureTextView, nicknameTextView;
    private String avatar, cropImagePath;
    private ImageView avatarImageView;
    private String imagePath;
    private User user;
    private static final int REQUEST_IMAGE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        initialize();

//        addressTextView.setText(Application.getUser().provincename + Application.getUser().cityname);
//        signatureTextView.setText(Application.getUser().shortdesc);
        nicknameTextView.setText(user.getNickname());
//        Glide.with(AoApplication.getInstance()).load(user.getAvatar().getUrl()).into(avatarImageView);
        findViewById(R.id.signature).setOnClickListener(this);
        findViewById(R.id.address).setOnClickListener(this);
        findViewById(R.id.avatar).setOnClickListener(this);
        findViewById(R.id.nickname).setOnClickListener(this);
    }

    private void initialize() {


        addressTextView = (TextView) findViewById(R.id.addressTextView);
        signatureTextView = (TextView) findViewById(R.id.signatureTextView);
        nicknameTextView = (TextView) findViewById(R.id.nicknameTextView);
        avatarImageView = (ImageView) findViewById(R.id.avatarImageView);

        user = BmobUser.getCurrentUser(User.class);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
//            case R.id.address:
//                intent = new Intent(this, ModifyAreaActivity.class);
//                startActivityForResult(intent, 1);
//                break;
//            case R.id.signature:
//                intent = new Intent(this, ModifySignatureActivity.class);
//                intent.putExtra("signature", signatureTextView.getText().toString());
//                startActivityForResult(intent, 2);
//                break;
            case R.id.nickname:
                intent = new Intent(this, ModifyNicknameActivity.class);
                intent.putExtra("nickname", nicknameTextView.getText().toString());
                startActivityForResult(intent, 3);
                break;
            case R.id.avatar:
                intent = new Intent(this, MultiImageSelectorActivity.class);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 2);


                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, 0);
                startActivityForResult(intent, REQUEST_IMAGE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
            }
        }
    }


//    private void modify() {
//        Api api = new Api();
//        Parameter parameter = new Parameter();
//        parameter.headimg = avatar;
//        parameter.userid = Application.getUid();
//        api.name = "user.profile.detail.post";
//        api.params = parameter;
//        VolleyInstance.connect(Request.Method.POST, VolleyInstance.getRequest(1, api), listener, errorListener);
//    }


}
