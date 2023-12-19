package com.example.golf_apk.ui;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.golf_apk.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ImageButton closeButton = findViewById(R.id.btn_close_menu);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeMenuActivity();
            }
        });
    }

    private void closeMenuActivity() {
        finish();
        overridePendingTransition(R.anim.slide_in_reverse, R.anim.slide_out_reverse);
    }
}
