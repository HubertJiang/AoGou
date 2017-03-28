package com.kui.gou.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kui.gou.R;
import com.kui.gou.adapter.CookPagerAdapter;
import com.kui.gou.entity.ApiResponse;
import com.kui.gou.entity.CookInfo;
import com.kui.gou.util.Constant;
import com.kui.gou.util.RetrofitFactory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by liweihui on 2017/3/20.
 */

public class CookActivity extends BaseActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ViewPager viewPager;
    private CookPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_we_chat);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setEnabled(false);
        getSupportActionBar().setTitle(R.string.cook);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new CookPagerAdapter(getSupportFragmentManager());
        swipeRefreshLayout.setRefreshing(true);
        RetrofitFactory.getInstance().getCookCategory(Constant.API_KEY).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.body().retCode == 200) {
                    Gson gson = new Gson();
                    List<CookInfo> data =
                            gson.fromJson(
                                    response.body().result.getAsJsonObject().get("childs").getAsJsonArray().get(0).
                                            getAsJsonObject().get("childs").getAsJsonArray(),
                                    new TypeToken<List<CookInfo>>() {
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
