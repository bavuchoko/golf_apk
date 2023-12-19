package com.example.golf_apk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.golf_apk.ui.MatchActivity;
import com.example.golf_apk.ui.MenuActivity;

public class MainActivity extends AppCompatActivity {

    private Animation slideIn, slideOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView matchButton = findViewById(R.id.btn_match);
        matchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMatchActivity();
            }
        });

        ImageButton menuButton = findViewById(R.id.btn_menu);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMenuActivity();
            }
        });
    }
    private void openMatchActivity() {
        Intent intent = new Intent(MainActivity.this, MatchActivity.class);
        startActivity(intent);
    }
    private void openMenuActivity() {
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void onCloseButtonClick(View view) {
        onBackPressed();
    }
}