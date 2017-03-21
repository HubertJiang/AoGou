package com.kui.gou.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.kui.gou.R;
import com.kui.gou.adapter.CollectionPagerAdapter;
import com.mob.mobapi.API;
import com.mob.mobapi.APICallback;
import com.mob.mobapi.MobAPI;
import com.mob.mobapi.apis.WxArticle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by liweihui on 2017/3/20.
 */

public class WeChatActivity extends BaseActivity {

    private WxArticle api;
    private ViewPager viewPager;
    private CollectionPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_we_chat);
        MobAPI.initSDK(this, "1c3d38dfff892");
        api = (WxArticle) MobAPI.getAPI(WxArticle.NAME);
        getSupportActionBar().setTitle(R.string.we_chat);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new CollectionPagerAdapter(getSupportFragmentManager());
        api.queryCategory(new APICallback() {
            @Override
            public void onSuccess(API api, int i, Map<String, Object> map) {
                ArrayList<HashMap<String, Object>> res = (ArrayList<HashMap<String, Object>>) map.get("result");
                adapter.setData(res);
                viewPager.setAdapter(adapter);
            }

            @Override
            public void onError(API api, int i, Throwable throwable) {

            }
        });

    }


}
