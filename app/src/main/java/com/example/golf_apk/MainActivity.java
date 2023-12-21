package com.example.golf_apk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.golf_apk.ui.MatchActivity;
import com.example.golf_apk.ui.MenuActivity;
import com.example.golf_apk.ui.PracticeActivity;

public class MainActivity extends AppCompatActivity {

    private Animation slideIn, slideOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        TextView matchButton = findViewById(R.id.btn_match);
        ImageButton menuButton = findViewById(R.id.btn_menu);
        TextView practiceButton = findViewById(R.id.btn_practice);

        matchButton.setOnClickListener(openMatchActivityListener);
        menuButton.setOnClickListener(openMenuActivityListener);
        practiceButton.setOnClickListener(openPracticeActivityListener);


    }


    private final View.OnClickListener openMatchActivityListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            openMatchActivity();
        }
    };


    private final View.OnClickListener openMenuActivityListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            openMenuActivity();
        }
    };

    private final View.OnClickListener openPracticeActivityListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            openPracticeActivity();
        }
    };

    private void openMatchActivity() {
        Intent intent = new Intent(MainActivity.this, MatchActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.up_to_center, R.anim.not_move);
    }
    private void openMenuActivity() {
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_center, R.anim.not_move);
    }
    private void openPracticeActivity() {
        Intent intent = new Intent(MainActivity.this, PracticeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.up_to_center, R.anim.not_move);
    }

    public void onCloseButtonClick(View view) {
        onBackPressed();
    }
}