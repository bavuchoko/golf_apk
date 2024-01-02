package com.example.golf_apk.api;

import com.example.golf_apk.dto.Account;
import com.example.golf_apk.dto.AccountResponse;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface ApiService {
    @POST("api/user/authentication")
    Call<AccountResponse> login(@Body Account account);

    @GET("api/warmup")
    Call<ResponseBody> getPractices(
            @Query("startDate") String startDate,
            @Query("endDate") String endDate,
            @Query("page") int page,
            @Query("size") int size,
            @Query("sort") String sort,
            @Header("Authorization") String authorizationHeader);


    @GET("api/warmup/{id}")
    Call<ResponseBody> getPractice(
            @Path("id") String id,
            @Header("Authorization") String authorizationHeader);

    @GET("api/warmup/{id}")
    Call<ResponseBody> getPractice(
            @Path("id") String id);

    @GET("api/warmup")
    Call<ResponseBody> getPractices(
            @Query("startDate") String startDate,
            @Query("endDate") String endDate,
            @Query("page") int page,
            @Query("size") int size,
            @Query("sort") String sort);

    @GET("api/field")
    Call<ResponseBody> geFields(
            @Query("searchTxt") String searchTxt,
            @Query("page") int page,
            @Query("size") int size);


    @POST("api/warmup/create")
    Call<ResponseBody> createPractice(
            @Body RequestBody requestBody,
            @Header("Authorization") String authorizationHeader);

    @GET("api/warmup/subscribe{id}")
    @Streaming
    Call<ResponseBody> getPracticeEvents(@Path("id") String id);
}
