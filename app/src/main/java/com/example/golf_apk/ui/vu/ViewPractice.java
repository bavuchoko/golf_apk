package com.example.golf_apk.ui.vu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.golf_apk.R;
import com.example.golf_apk.api.ApiService;
import com.example.golf_apk.api.RetrofitClient;
import com.example.golf_apk.common.CommonMethod;
import com.example.golf_apk.dto.adapter.PracticePlayerAdapter;
import com.example.golf_apk.ui.PracticeListActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewPractice extends AppCompatActivity {
    private Animation slideOut;
    private ApiService api;

    private JsonObject practice;

    private PracticePlayerAdapter practicePlayerAdapter;

    private JsonArray players;

    private RecyclerView playersView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_practice);

        ImageButton closeButton = findViewById(R.id.btn_close_practice_view);
        closeButton.setOnClickListener(closeThisActivityListener);
        playersView = findViewById(R.id.practice_view_players);
        playersView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        Intent intent = getIntent();
        String practiceId = intent.getStringExtra("id");

        getPractice(practiceId);

    }

    private void getPractice(String practiceId) {
        api = RetrofitClient.getRetrofit().create(ApiService.class);
        Call<ResponseBody> call;
        String accessToken = CommonMethod.getAccessToken(ViewPractice.this);
        if(accessToken !=null){
            call = api.getPractice(practiceId,"Bearer " + accessToken);
        }else{
            call = api.getPractice(practiceId);
        }
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseBody = response.body();
                try {
                    if (responseBody != null) {
                        String jsonString = responseBody.string();
                        practice = JsonParser.parseString(jsonString).getAsJsonObject();
                        JsonElement playersElement = practice.get("players");
                        if (playersElement != null && playersElement.isJsonArray()) {
                            players = practice.getAsJsonArray("players");
                            practicePlayerAdapter = new PracticePlayerAdapter(players);
                            playersView.setAdapter(practicePlayerAdapter);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private final View.OnClickListener closeThisActivityListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            closethisActivity();
        }
    };
    private void closethisActivity() {
        finish();
        overridePendingTransition(R.anim.not_move, R.anim.center_to_up);
    }
}
