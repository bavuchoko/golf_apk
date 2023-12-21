package com.example.golf_apk.ui.create;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.golf_apk.R;

public class CreatePractice extends AppCompatActivity {
    private Animation slideOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_practice);

        ImageButton closeButton = findViewById(R.id.btn_close_create_practice);
        closeButton.setOnClickListener(closeThisActivityListener);


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
