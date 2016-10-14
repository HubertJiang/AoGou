package com.kuiyuan.aogou.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kuiyuan.aogou.R;
import com.kuiyuan.aogou.adapter.ClassifyAdapter;
import com.kuiyuan.aogou.entity.Classify;
import com.kuiyuan.aogou.entity.User;
import com.kuiyuan.aogou.fragment.MainFragment;
import com.kuiyuan.aogou.fragment.SettingFragment;
import com.kuiyuan.aogou.util.NetUtil;
import com.kuiyuan.aogou.util.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private int currentItem = R.id.goods;
    private ClassifyAdapter adapter;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinner = (Spinner) findViewById(R.id.spinner);

        adapter = new ClassifyAdapter(this, R.layout.title_spinner);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.item_classify);

        spinner.setAdapter(adapter); // set the adapter to provide layout of rows and content
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((MainFragment) getFragmentManager().findFragmentByTag("goods")).refresh(adapter.getItem(position).getObjectId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }); // set the listener, to perform actions based on item selection

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getFragmentManager().beginTransaction().replace(R.id.frame_content, new MainFragment(), "goods").commit();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        spinner.setVisibility(View.VISIBLE);
        navigationView.setCheckedItem(R.id.goods);

        navigationView.setCheckedItem(0);
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.name)).setText(BmobUser.getCurrentUser().getUsername());
        if(BmobUser.getCurrentUser(User.class).getAvatar()!=null){
            Glide.with(this).load(BmobUser.getCurrentUser(User.class).getAvatar().getUrl()).into((ImageView) navigationView.getHeaderView(0).findViewById(R.id.image_view));
        }

        (navigationView.getHeaderView(0).findViewById(R.id.top_view)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, InformationActivity.class));
            }
        });

        get();
        getToken();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (currentItem != id) {
            if (id == R.id.goods) {
                getFragmentManager().beginTransaction().replace(R.id.frame_content, new MainFragment(), "goods").commit();
                getSupportActionBar().setTitle(R.string.goods);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                spinner.setVisibility(View.VISIBLE);
                currentItem = id;
            } else if (id == R.id.nav_gallery) {

            } else if (id == R.id.nav_slideshow) {

            } else if (id == R.id.setting) {
                getFragmentManager().beginTransaction().replace(R.id.frame_content, new SettingFragment()).commit();
                getSupportActionBar().setTitle(R.string.setting);
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                spinner.setVisibility(View.GONE);
                currentItem = id;
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void get() {
        BmobQuery<Classify> query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.findObjects(new FindListener<Classify>() {
            @Override
            public void done(List<Classify> list, BmobException e) {
                if (e == null) {
                    Classify classify = new Classify();
                    classify.name = getString(R.string.all);
                    list.add(0, classify);
                    adapter.setObjects(list);
                } else {
                    AoApplication.showToast(e.toString());
                }
            }
        });
    }

    private void getToken() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                Map<String, String> map = new HashMap<>();
                map.put("userId", BmobUser.getCurrentUser(User.class).getObjectId());
                map.put("name", BmobUser.getCurrentUser(User.class).getNickname());
                map.put("portraitUri", BmobUser.getCurrentUser(User.class).getAvatar().getUrl());
                String result = NetUtil.getInstance().post("http://api.cn.ronghub.com/user/getToken.json", map);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {

                try {
                    if (result != null) {
                        JSONObject object = new JSONObject(result);

                        if (object.getInt("code") == 200) {
                            String token = object.getString("token");
                            SPUtils.setString(MainActivity.this, SPUtils.TOKEN, token);
                            connect(token);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void refresh() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                Map<String, String> map = new HashMap<>();
                map.put("userId", BmobUser.getCurrentUser(User.class).getObjectId());
                map.put("name", BmobUser.getCurrentUser(User.class).getNickname());
                map.put("portraitUri", BmobUser.getCurrentUser(User.class).getAvatar().getUrl());
                String result = NetUtil.getInstance().post("http://api.cn.ronghub.com/user/refresh.json", map);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {

                try {
                    if (result != null) {
                        JSONObject object = new JSONObject(result);

                        if (object.getInt("code") == 200) {

                            RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

                                @Override
                                public UserInfo getUserInfo(String userId) {

                                    return getUser(userId);//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
                                }

                            }, true);


                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private UserInfo getUser(String userId){
        BmobQuery<User> query = new BmobQuery<>();
        query.getObject(userId,new QueryListener<User>() {
            @Override
            public void done(User object,BmobException e) {
                if(e==null){
                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(object.getObjectId(),
                            object.getNickname(),
                            Uri.parse(object.getAvatar().getUrl())));
                }else{
//                    toast("更新用户信息失败:" + e.getMessage());
                }
            }
        });
        return null;
    }

    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    private void connect(String token) {

        if (getApplicationInfo().packageName.equals(AoApplication.getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {

                    Log.d("LoginActivity", "--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {

                    Log.d("LoginActivity", "--onSuccess" + userid);
                    //启动会话界面
                    refresh();
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                    finish();
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 *                  http://www.rongcloud.cn/docs/android.html#常见错误码
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                    Log.d("LoginActivity", "--onError" + errorCode);
                }
            });
        }
    }
}
