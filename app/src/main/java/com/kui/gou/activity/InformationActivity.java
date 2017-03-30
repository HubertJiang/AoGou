package com.kui.gou.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kui.gou.R;
import com.kui.gou.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by Administrator on 2015/3/6.
 */
public class InformationActivity extends BaseActivity implements View.OnClickListener {
    private TextView addressTextView, genderTextView, nicknameTextView, genderText;
    private String avatar, cropImagePath;
    private ImageView avatarImageView;
    private String imagePath;
    private User user;
    private final int REQUEST_ADDRESS = 1, REQUEST_IMAGE = 2, REQUEST_NAME = 3;
    private int index = -1;
    private String[] genderArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        initialize();

        nicknameTextView.setText(user.getNickname());
        genderTextView.setText(user.getEmail());
        addressTextView.setText(user.getAddress());
        genderText.setText(user.getGender());
        for (int i = 0; i < genderArray.length; i++) {
            if (genderArray[i].equals(user.getGender())) {
                index = i;
            }
        }
        if (user.getAvatar() != null)
            Glide.with(AoApplication.getInstance()).load(user.getAvatar().getUrl()).into(avatarImageView);
        findViewById(R.id.address).setOnClickListener(this);
        findViewById(R.id.avatar).setOnClickListener(this);
        findViewById(R.id.nickname).setOnClickListener(this);
        findViewById(R.id.gender).setOnClickListener(this);
        findViewById(R.id.exit).setOnClickListener(this);
    }

    private void initialize() {
        getSupportActionBar().setTitle(R.string.information);
        addressTextView = (TextView) findViewById(R.id.addressTextView);
        genderTextView = (TextView) findViewById(R.id.gender_text);
        nicknameTextView = (TextView) findViewById(R.id.nicknameTextView);
        genderText = (TextView) findViewById(R.id.gender_text);
        avatarImageView = (ImageView) findViewById(R.id.avatarImageView);
        user = BmobUser.getCurrentUser(User.class);
        genderArray = getResources().getStringArray(R.array.gender);
    }

    @Override
    public void onClick(View v) {
        final Intent intent;
        switch (v.getId()) {
            case R.id.address:
                intent = new Intent(this, ModifyAddressActivity.class);
                intent.putExtra("address", user.getAddress());
                startActivityForResult(intent, REQUEST_ADDRESS);
                break;
            case R.id.nickname:
                intent = new Intent(this, ModifyNicknameActivity.class);
                intent.putExtra("nickname", user.getNickname());
                startActivityForResult(intent, REQUEST_NAME);
                break;
            case R.id.avatar:
                intent = new Intent(this, MultiImageSelectorActivity.class);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, 0);
                startActivityForResult(intent, REQUEST_IMAGE);
                break;
            case R.id.gender:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.gender);
                builder.setSingleChoiceItems(R.array.gender, index, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        index = which;
                        modify(genderArray[which]);
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
            case R.id.exit:
                BmobUser.logOut();
                finish();
                break;
        }
    }

    private void modify(String gender) {
        genderText.setText(gender);
        User newUser = new User();
        newUser.setGender(gender);
        BmobUser bmobUser = BmobUser.getCurrentUser();
        newUser.update(bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {

                } else {
                    try {
                        JSONObject json = new JSONObject(e.getMessage());
                        AoApplication.showToast(json.getString("detail"));
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE:
                    ArrayList<String> images = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    final BmobFile bmobFile = new BmobFile(new File(images.get(0)));
                    bmobFile.uploadblock(new UploadFileListener() {

                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                final User newUser = new User();
                                newUser.setAvatar(bmobFile);
                                BmobUser bmobUser = BmobUser.getCurrentUser();
                                newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            Glide.with(AoApplication.getInstance()).load(newUser.getAvatar().getUrl()).into(avatarImageView);
                                        } else {
                                            try {
                                                JSONObject json = new JSONObject(e.getMessage());
                                                AoApplication.showToast(json.getString("detail"));
                                            } catch (JSONException ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                    }
                                });
                            } else {
                                try {
                                    JSONObject json = new JSONObject(e.getMessage());
                                    AoApplication.showToast(json.getString("detail"));
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }

                        }

                        @Override
                        public void onProgress(Integer value) {
                            // 返回的上传进度（百分比）
                        }
                    });
                    break;
                case REQUEST_ADDRESS:
                    addressTextView.setText(data.getStringExtra("address"));
                    break;
                case REQUEST_NAME:
                    nicknameTextView.setText(data.getStringExtra("nickname"));
                    break;
            }
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


//    /**
//     * Callback received when a permissions request has been completed.
//     */
//    @TargetApi(Build.VERSION_CODES.M)
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        if (requestCode == REQUEST_READ_CONTACTS && grantResults.length == 1) {
//            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
//                if (!shouldShowRequestPermissionRationale(CAMERA)) {
//                    Intent intent = new Intent();
//                    intent.setAction("android.intent.action.MAIN");
//                    intent.setClassName("com.android.settings", "com.android.settings.ManageApplications");
//                    startActivity(intent);
//                }
//            } else {
//                Intent intent = new Intent(this, MultiImageSelectorActivity.class);
//                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
//                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 2);
//                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, 0);
//                startActivityForResult(intent, REQUEST_IMAGE);
//            }
//        }
//    }
}
