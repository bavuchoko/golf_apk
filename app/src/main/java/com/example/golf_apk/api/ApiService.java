package com.example.golf_apk.api;

import com.example.golf_apk.dto.Account;
import com.example.golf_apk.dto.AccountResponse;
import com.example.golf_apk.dto.PagedResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

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

    @GET("api/warmup")
    Call<ResponseBody> getPractices(
            @Query("startDate") String startDate,
            @Query("endDate") String endDate,
            @Query("page") int page,
            @Query("size") int size,
            @Query("sort") String sort);
}
