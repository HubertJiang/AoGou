package com.kuiyuan.aogou.activity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

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

import java.util.ArrayList;
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

public class MainActivity extends AppCompatActivity {
    private int currentItem = R.id.goods;
    private ClassifyAdapter adapter;
    private Spinner spinner;
    private List<Fragment> fragments = new ArrayList<>();
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private MenuItem prevMenuItem;

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
//                ((MainFragment) getSupportFragmentManager().findFragmentByTag("goods")).refresh(adapter.getItem(position).getObjectId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }); // set the listener, to perform actions based on item selection


        MainFragment mainFragment = new MainFragment();
        SettingFragment settingFragment = new SettingFragment();
        fragments.add(new MainFragment());
        fragments.add(new SettingFragment());
        fragments.add(mainFragment);
        fragments.add(settingFragment);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.goods:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.delete:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.setting:
                                viewPager.setCurrentItem(2);
                                break;
                            case R.id.me:
                                viewPager.setCurrentItem(3);
                                break;

                        }
                        return false;
                    }
                });

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        spinner.setVisibility(View.VISIBLE);


        get();
        getToken();
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

    private UserInfo getUser(String userId) {
        BmobQuery<User> query = new BmobQuery<>();
        query.getObject(userId, new QueryListener<User>() {
            @Override
            public void done(User object, BmobException e) {
                if (e == null) {
                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(object.getObjectId(),
                            object.getNickname(),
                            Uri.parse(object.getAvatar().getUrl())));
                } else {
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
