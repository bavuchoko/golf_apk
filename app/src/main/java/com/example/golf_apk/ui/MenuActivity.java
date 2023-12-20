package com.example.golf_apk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.golf_apk.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ImageButton closeButton = findViewById(R.id.btn_close_menu);
        TextView loginButton = findViewById(R.id.login_btn);


        closeButton.setOnClickListener(closeThisActivityListener);
        loginButton.setOnClickListener(gotToLoginActivityListener);


    }

    private final View.OnClickListener closeThisActivityListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            closeMenuActivity();
        }
    };


    private final View.OnClickListener gotToLoginActivityListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            openLoginActivity();
        }
    };

    private void closeMenuActivity() {
        finish();
        overridePendingTransition(R.anim.not_move, R.anim.right_to_left);
    }

    private void openLoginActivity() {
        Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.up_to_down, R.anim.not_move);

    }

}
