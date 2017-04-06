package com.kui.gou.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.kui.gou.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


public class ModifyPasswordActivity extends BaseActivity implements TextWatcher {
    private EditText editText, newEditText, reEditText;
    private String pass, newPass, rePass;
    private MenuItem saveItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        getSupportActionBar().setTitle(R.string.modify_password);
        editText = (EditText) findViewById(R.id.password_text);
        newEditText = (EditText) findViewById(R.id.new_password_text);
        reEditText = (EditText) findViewById(R.id.re_password_text);

        editText.addTextChangedListener(this);
        newEditText.addTextChangedListener(this);
        reEditText.addTextChangedListener(this);

    }

    private void modify() {
        BmobUser.updateCurrentUserPassword("旧密码", "新密码", new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
//                    toast("密码修改成功，可以用新密码进行登录啦");
                } else {
//                    toast("失败:" + e.getMessage());
                }
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
                pass = editText.getText().toString();
                newPass = newEditText.getText().toString();
                rePass = reEditText.getText().toString();

                saveItem.setEnabled(false);
                modify();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        pass = editText.getText().toString();
        newPass = newEditText.getText().toString();
        rePass = reEditText.getText().toString();
        if(TextUtils.isEmpty(pass)||TextUtils.isEmpty(newPass)||TextUtils.isEmpty(rePass)){
            saveItem.setEnabled(false);
        }else {
            saveItem.setEnabled(true);
        }
    }
}
