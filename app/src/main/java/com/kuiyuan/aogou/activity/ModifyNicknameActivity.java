package com.kuiyuan.aogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.kuiyuan.aogou.R;
import com.kuiyuan.aogou.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


public class ModifyNicknameActivity extends AppCompatActivity {
    private EditText signatureEditText;
    private String nickname;
    private TextView submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_nickname);
        nickname = getIntent().getStringExtra("nickname");
        submit = (TextView) findViewById(R.id.save);

        signatureEditText = (EditText) findViewById(R.id.signatureEditText);
//        ((TextView) findViewById(R.id.titleTextView)).setText(R.string.nickname);
        if (!TextUtils.isEmpty(nickname)) {
            signatureEditText.setText(nickname);
        }

        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                nickname = signatureEditText.getText().toString();
                if (TextUtils.isEmpty(nickname)) {
                } else {
                    submit.setEnabled(false);
                    modify();
                }
            }
        });

    }

    private void modify() {
        User newUser = new User();
        newUser.setNickname(nickname);
        BmobUser bmobUser = BmobUser.getCurrentUser();
        newUser.update(bmobUser.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Intent intent=new Intent();
                    intent.putExtra("nickname", nickname);
                    setResult(RESULT_OK,intent);
                    finish();
                }else{
                    try {
                        JSONObject json=new JSONObject(e.getMessage());
                        AoApplication.showToast(json.getString("detail"));
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

}
