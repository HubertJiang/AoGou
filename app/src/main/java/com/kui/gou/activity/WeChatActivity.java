package com.kui.gou.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kui.gou.R;
import com.kui.gou.adapter.CollectionPagerAdapter;
import com.kui.gou.entity.ApiResponse;
import com.kui.gou.entity.WeChat;
import com.kui.gou.util.Constant;
import com.kui.gou.util.RetrofitFactory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by liweihui on 2017/3/20.
 */

public class WeChatActivity extends BaseActivity {

    private ViewPager viewPager;
    private CollectionPagerAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_we_chat);
        getSupportActionBar().setTitle(R.string.we_chat);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setEnabled(false);
        adapter = new CollectionPagerAdapter(getSupportFragmentManager());

        swipeRefreshLayout.setRefreshing(true);
        RetrofitFactory.getInstance().getWeChatCategory(Constant.API_KEY).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.body().retCode == 200) {
                    Gson gson = new Gson();
                    List<WeChat> data =
                            gson.fromJson(
                                    response.body().result.getAsJsonArray(),
                                    new TypeToken<List<WeChat>>() {
                                    }.getType
                                            ());
                    adapter.setData(data);
                    viewPager.setAdapter(adapter);
                } else {
                    AoApplication.showToast(R.string.no_network);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                AoApplication.showToast(R.string.no_network);
            }
        });


    }


}
