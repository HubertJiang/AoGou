package com.kui.gou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.kui.gou.R;
import com.kui.gou.entity.User;
import com.kui.gou.util.RetrofitFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ModifyNicknameActivity extends BaseActivity {
    private EditText editText;
    private String nickname;
    private MenuItem saveItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_nickname);
        nickname = getIntent().getStringExtra("nickname");
        editText = (EditText) findViewById(R.id.editText);
        if (!TextUtils.isEmpty(nickname)) {
            editText.setText(nickname);
            editText.setSelection(editText.getText().length());
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    saveItem.setEnabled(false);
                } else {
                    saveItem.setEnabled(true);
                }
            }
        });

    }

    private void modify() {
        RetrofitFactory.getInstance().modifyNickname(AoApplication.getUserId(), nickname).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                saveItem.setEnabled(true);
                if (response.isSuccessful()) {
                    Intent intent = new Intent();
                    intent.putExtra("nickname", nickname);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    AoApplication.showToast(R.string.no_network);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                saveItem.setEnabled(true);
                AoApplication.showToast(R.string.no_network);
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        saveItem = menu.findItem(R.id.action_save);
        saveItem.setVisible(true);
        saveItem.setEnabled(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                nickname = editText.getText().toString();
                if (TextUtils.isEmpty(nickname)) {
                } else {
                    saveItem.setEnabled(false);
                    modify();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
