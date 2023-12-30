package com.example.golf_apk.api;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
        private static Retrofit retrofit;

        public static Retrofit getRetrofit(){
            if(retrofit==null){

                OkHttpClient client = TrustOkHttpClientUtil.getUnsafeOkHttpClient().build();

                Retrofit.Builder builder = new Retrofit.Builder();
                builder.baseUrl("https://sejong-parkgolf.com:18090/");
//                builder.baseUrl("http://192.168.1.100:8090/");
                builder.client(client);
                builder.addConverterFactory(GsonConverterFactory.create());

                retrofit = builder.build();
            }
            return retrofit;
        }
}
