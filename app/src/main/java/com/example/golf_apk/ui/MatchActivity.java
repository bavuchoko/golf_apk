package com.example.golf_apk.ui;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.golf_apk.R;

public class MatchActivity extends AppCompatActivity {
    private Animation slideOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        ImageButton closeButton = findViewById(R.id.btn_close_match);
        closeButton.setOnClickListener(closeThisActivityListener);
    }
    private final View.OnClickListener closeThisActivityListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            closeMatchActivity();
        }
    };
    private void closeMatchActivity() {

        finish();
    }
}
