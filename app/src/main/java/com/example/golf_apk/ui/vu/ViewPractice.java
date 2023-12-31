package com.example.golf_apk.ui.vu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.golf_apk.R;
import com.example.golf_apk.api.ApiService;
import com.example.golf_apk.api.RetrofitClient;
import com.example.golf_apk.common.CommonMethod;
import com.example.golf_apk.databinding.ViewPracticeBinding;
import com.example.golf_apk.dto.adapter.PracticePlayerAdapter;
import com.example.golf_apk.ui.fragment.FragmentViewPracticeCurrent;
import com.example.golf_apk.ui.fragment.FragmentViewPracticePrev;
import com.google.android.material.tabs.TabLayout;
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
    private ViewPracticeBinding binding;

    private ApiService api;

    private JsonObject practice;

    private PracticePlayerAdapter practicePlayerAdapter;

    private JsonArray players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ViewPracticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnClosePracticeView.setOnClickListener(closeThisActivityListener);

        // 참가자들
        binding.practiceViewPlayers.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        // 점수조정 버튼
        binding.playerScoreSetter.scoreUp.setOnClickListener(scoreUpClickListener);
        binding.playerScoreSetter.scoreDown.setOnClickListener(scoreDownClickListener);

        // 다음 홀 버튼, 저장 버튼
        binding.playerScoreSetter.btnSaveScore.setOnClickListener(saveScoreClickListener);
        binding.playerScoreSetter.btnNextHole.setOnClickListener(nextHoleClickListener);

        Intent intent = getIntent();
        String practiceId = intent.getStringExtra("id");
        getPractice(practiceId);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_score, new FragmentViewPracticeCurrent())
                .commit();

        // TabLayout 설정을 분리한 메서드 호출
        setupTabLayout();
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
        if (accessToken != null) {
            call = api.getPractice(practiceId, "Bearer " + accessToken);
        } else {
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
                            binding.practiceViewPlayers.setAdapter(practicePlayerAdapter);
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


    private void setupTabLayout() {
        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getText().toString()) {
                    case "현재 라운드":
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container_score, new FragmentViewPracticeCurrent())
                                .commit();
                        break;
                    default:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container_score, new FragmentViewPracticePrev())
                                .commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // NOT IMPLEMENTED
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // NOT IMPLEMENTED
            }
        });
    }
}