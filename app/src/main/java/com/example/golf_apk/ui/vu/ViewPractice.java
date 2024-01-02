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
import com.example.golf_apk.api.SSEService;
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
    private SSEService sseService;

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

        //조회할 경기 아이디를 받아서 경기 조회
        Intent intent = getIntent();
        String practiceId = intent.getStringExtra("id");
        sseService = new SSEService(api);
        sseService.connectToSSE(practiceId);

        // LiveData 관찰
        sseService.getEventLiveData().observe(this, eventData -> {
            // UI 갱신 로직 구현
            setPractice(eventData);
        });

        sseService.getFailureLiveData().observe(this, throwable -> {
            // 실패 처리 로직
            throwable.printStackTrace();
        });

        // 실제 사용할 ID를 전달
        sseService.connectToSSE("your_id");

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

    private void setPractice(String jsonString) {
        practice = JsonParser.parseString(jsonString).getAsJsonObject();
        JsonElement playersElement = practice.get("players");
        if (playersElement != null && playersElement.isJsonArray()) {
            players = practice.getAsJsonArray("players");
            practicePlayerAdapter = new PracticePlayerAdapter(players);
            binding.practiceViewPlayers.setAdapter(practicePlayerAdapter);
        }
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