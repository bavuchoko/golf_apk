package com.example.golf_apk.ui.update;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.golf_apk.R;

public class UpdatePractice extends AppCompatActivity {
    private Animation slideOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_practice);

        ImageButton closeButton = findViewById(R.id.btn_close_update_practice);
        closeButton.setOnClickListener(closeThisActivityListener);

        Intent intent = getIntent();
        String practiceId = intent.getStringExtra("id");
        System.out.println(practiceId);
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
