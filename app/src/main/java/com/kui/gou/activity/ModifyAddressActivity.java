package com.kui.gou.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.kui.gou.R;


public class ModifyAddressActivity extends BaseActivity {
    private EditText editText;
    private String address;
    private MenuItem saveItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_address);
        address = getIntent().getStringExtra("address");
        editText = (EditText) findViewById(R.id.editText);
        if (!TextUtils.isEmpty(address)) {
            editText.setText(address);
            editText.setSelection(address.length());
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
//        User newUser = new User();
//        newUser.setAddress(address);
//        BmobUser bmobUser = BmobUser.getCurrentUser();
//        newUser.update(bmobUser.getObjectId(), new UpdateListener() {
//            @Override
//            public void done(BmobException e) {
//                saveItem.setEnabled(true);
//                if (e == null) {
//                    Intent intent = new Intent();
//                    intent.putExtra("address", address);
//                    setResult(RESULT_OK, intent);
//                    finish();
//                } else {
//                    try {
//                        JSONObject json = new JSONObject(e.getMessage());
//                        AoApplication.showToast(json.getString("detail"));
//                    } catch (JSONException ex) {
//                        ex.printStackTrace();
//                    }
//                }
//            }
//        });
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
                address = editText.getText().toString();
                if (TextUtils.isEmpty(address)) {
                } else {
                    saveItem.setEnabled(false);
                    modify();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
