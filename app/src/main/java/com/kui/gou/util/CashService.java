package com.kui.gou.util;


import com.kui.gou.entity.ApiResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by liweihui on 16/7/4.
 */
public interface CashService {


    @GET("http://apicloud.mob.com/v1/cook/category/query" )
    Call<ApiResponse> getCookCategory(@Query("key") String appKey);
    @GET("http://apicloud.mob.com/v1/cook/menu/search" )
    Call<ApiResponse> getCook(@Query("key") String appKey,@Query("cid") String cid,
                                      @Query("page") int page,@Query("size") int size);

//    @GET("https://api.weixin.qq.com/sns/userinfo")
//    Call<WeChatUser> getWeChatUser(@Query("access_token") String token, @Query("openid") String id);




    /**
     * 上传一张图片
     *
     * @return
     */
    @Multipart
    @POST("api/uploadfs")
    Call<String> uploadImage(@Part("uid") RequestBody userId, @Part("source") RequestBody source, @Part("filetype") RequestBody filetype,
                             @Part("resourcekey") RequestBody resourcekey, @Part("resourceid") RequestBody resourceid,
                             @Part MultipartBody.Part picture);


    @Multipart
    @POST("api/uploadfs")
    Call<String> uploadImage(@PartMap Map<String, RequestBody> para, @Part MultipartBody.Part picture);

}
