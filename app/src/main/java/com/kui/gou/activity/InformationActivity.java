package com.kui.gou.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import static android.Manifest.permission.CAMERA;

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
    private static final int REQUEST_READ_CONTACTS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        initialize();

//        addressTextView.setText(Application.getUser().provincename + Application.getUser().cityname);
//        signatureTextView.setText(Application.getUser().shortdesc);
        nicknameTextView.setText(user.getNickname());
        Glide.with(AoApplication.getInstance()).load(user.getAvatar().getUrl()).into(avatarImageView);
        findViewById(R.id.signature).setOnClickListener(this);
        findViewById(R.id.address).setOnClickListener(this);
        findViewById(R.id.avatar).setOnClickListener(this);
        findViewById(R.id.nickname).setOnClickListener(this);
    }

    private void initialize() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.information);
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
                if (mayRequestContacts()) {
                    intent = new Intent(this, MultiImageSelectorActivity.class);
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 2);


                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, 0);
                    startActivityForResult(intent, REQUEST_IMAGE);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
             ArrayList<String> images = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                final BmobFile bmobFile = new BmobFile(new File(images.get(0)));
                bmobFile.uploadblock(new UploadFileListener() {

                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            final User newUser = new User();
                            newUser.setAvatar(bmobFile);
                            BmobUser bmobUser = BmobUser.getCurrentUser();
                            newUser.update(bmobUser.getObjectId(),new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        Glide.with(AoApplication.getInstance()).load(newUser.getAvatar().getUrl()).into(avatarImageView);
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
                        }else{
                            try {
                                JSONObject json=new JSONObject(e.getMessage());
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
    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        requestPermissions(new String[]{CAMERA}, REQUEST_READ_CONTACTS);

        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS &&grantResults.length == 1) {
            if ( grantResults[0] == PackageManager.PERMISSION_DENIED) {
                if (!shouldShowRequestPermissionRationale(CAMERA)) {
                    Intent intent =  new Intent();
                    intent.setAction("android.intent.action.MAIN");
                    intent.setClassName("com.android.settings", "com.android.settings.ManageApplications");
                    startActivity(intent);
                }
            } else {
                Intent   intent = new Intent(this, MultiImageSelectorActivity.class);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 2);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, 0);
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        }
    }
}
