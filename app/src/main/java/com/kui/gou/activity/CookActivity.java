package com.kui.gou.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kui.gou.R;
import com.kui.gou.adapter.CookPagerAdapter;
import com.kui.gou.entity.ApiResponse;
import com.kui.gou.entity.CookInfo;
import com.kui.gou.util.Constant;
import com.kui.gou.util.RetrofitFactory;
import com.mob.mobapi.MobAPI;
import com.mob.mobapi.apis.Cook;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by liweihui on 2017/3/20.
 */

public class CookActivity extends BaseActivity {

    private Cook api;
    private ViewPager viewPager;
    private CookPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_we_chat);
        MobAPI.initSDK(this, Constant.API_KEY);
        api = (Cook) MobAPI.getAPI(Cook.NAME);
        getSupportActionBar().setTitle(R.string.cook);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new CookPagerAdapter(getSupportFragmentManager());

        RetrofitFactory.getInstance().getCookCategory(Constant.API_KEY).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
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
                AoApplication.showToast(R.string.no_network);
            }
        });


//        api.queryCategory(new APICallback() {
//            @Override
//            public void onSuccess(API api, int i, Map<String, Object> result) {
//                result = (Map<String, Object>) result.get("result");
//                ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) result.get("childs");
//                ArrayList<HashMap<String, Object>> data = (ArrayList<HashMap<String, Object>>) list.get(0).get("childs");
//                adapter.setData(data);
//                viewPager.setAdapter(adapter);
//            }
//
//            @Override
//            public void onError(API api, int i, Throwable throwable) {
//
//            }
//        });

    }


}
