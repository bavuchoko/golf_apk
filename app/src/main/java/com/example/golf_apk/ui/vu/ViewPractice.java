package com.example.golf_apk.ui.vu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

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
    private ImageButton scoreUp;
    private ImageButton scoreDown;

    private TextView btnSave;
    private TextView btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_practice);

        ImageButton closeButton = findViewById(R.id.btn_close_practice_view);
        closeButton.setOnClickListener(closeThisActivityListener);

        //참가자들
        playersView = findViewById(R.id.practice_view_players);
        playersView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        //점수조정 버튼
        scoreUp = findViewById(R.id.score_up);
        scoreDown = findViewById(R.id.score_down);

        scoreUp.setOnClickListener(scoreUpClickListener);
        scoreDown.setOnClickListener(scoreDownClickListener);

        //다음 홀 버튼, 저장 버튼
        btnSave = findViewById(R.id.btn_save_score);
        btnNext = findViewById(R.id.btn_next_hole);

        scoreUp.setOnClickListener(saveScoreClickListener);
        scoreDown.setOnClickListener(nextHoleClickListener);

        Intent intent = getIntent();
        String practiceId = intent.getStringExtra("id");
        getPractice(practiceId);

    }

    private final View.OnClickListener scoreUpClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Animation clickAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_click_animation);
            view.startAnimation(clickAnimation);
            System.out.println("up");
        }
    };

    private final View.OnClickListener scoreDownClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Animation clickAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_click_animation);
            view.startAnimation(clickAnimation);
            System.out.println("down");
        }
    };


    private final View.OnClickListener saveScoreClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Animation clickAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_click_animation);
            view.startAnimation(clickAnimation);
            System.out.println("save");
        }
    };

    private final View.OnClickListener nextHoleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Animation clickAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_click_animation);
            view.startAnimation(clickAnimation);
            System.out.println("next");
        }
    };

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
