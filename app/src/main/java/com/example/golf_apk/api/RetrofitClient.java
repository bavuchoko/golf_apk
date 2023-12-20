package com.example.golf_apk.api;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
        private static Retrofit retrofit;

        public static Retrofit getRetrofit(){
            if(retrofit==null){
                Retrofit.Builder builder = new Retrofit.Builder();
                builder.baseUrl("https://sejong-parkgolf.com:18090/");
                builder.addConverterFactory(GsonConverterFactory.create());

                retrofit = builder.build();
            }
            return retrofit;
        }
}
