package com.example.golf_apk.ui.create;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.golf_apk.R;

public class CreateField  extends AppCompatActivity {
    private Animation slideOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_field);

        ImageButton closeButton = findViewById(R.id.btn_close_new_field);
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
