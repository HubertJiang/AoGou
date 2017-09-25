package com.kui.gou.util;


import com.kui.gou.entity.ApiResponse;
import com.kui.gou.entity.Goods;
import com.kui.gou.entity.Image;
import com.kui.gou.entity.User;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by liweihui on 16/7/4.
 */
public interface CashService {
    /**
     * cloud api
     */
    @GET("goods")
    Call<List<Goods>> getGoods(@Query("filter") String filter);

    @GET("goods/{id}")
    Call<Goods> getGoodsDetail(@Path("id") String id);

    @FormUrlEncoded
    @POST("user/login")
    Call<User> signIn(@Field("username") String name, @Field("password") String password);

    @FormUrlEncoded
    @POST("user")
    Call<User> signUp(@Field("username") String name, @Field("password") String password, @Field("nickname") String nickname);

    @FormUrlEncoded
    @PUT("user/{id}")
    Call<User> modifyNickname(@Path("id") String id, @Field("nickname") String nickname);

    @FormUrlEncoded
    @PUT("user/{id}")
    Call<User> modifyGender(@Path("id") String id, @Field("gender") String gender);

    @FormUrlEncoded
    @PUT("user/{id}")
    Call<User> modifyAddress(@Path("id") String id, @Field("address") String address);

    @FormUrlEncoded
    @PUT("user/{id}")
    Call<User> modifyPassword(@Path("id") String id, @Field("password") String password);

    @FormUrlEncoded
    @PUT("user/{id}")
    Call<User> modifyAvatar(@Path("id") String id, @Field("avatar") String avatar);

    @GET("user/{id}")
    Call<User> getUser(@Path("id") String id);

    @FormUrlEncoded
    @POST("user/{id}/collection")
    Call<User> collection(@Path("id") String id, @Field("id") String goodsId);


    /**
     * mob api
     */
    @GET("http://apicloud.mob.com/v1/cook/category/query")
    Call<ApiResponse> getCookCategory(@Query("key") String appKey);

    @GET("http://apicloud.mob.com/v1/cook/menu/search")
    Call<ApiResponse> getCook(@Query("key") String appKey, @Query("cid") String cid,
                              @Query("page") int page, @Query("size") int size);

    @GET("http://apicloud.mob.com/wx/article/category/query")
    Call<ApiResponse> getWeChatCategory(@Query("key") String appKey);

    @GET("http://apicloud.mob.com/wx/article/search")
    Call<ApiResponse> getWeChat(@Query("key") String appKey, @Query("cid") String cid,
                                @Query("page") int page, @Query("size") int size);


    /**
     * 上传一张图片
     *
     * @return
     */
    @Multipart
    @POST("file")
    Call<Image> uploadImage(
            @Part MultipartBody.Part picture);


    @Multipart
    @POST("api/uploadfs")
    Call<String> uploadImage(@PartMap Map<String, RequestBody> para, @Part MultipartBody.Part picture);

}
