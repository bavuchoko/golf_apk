package com.example.golf_apk.ui;

import static com.example.golf_apk.common.CommonMethod.getAccessToken;
import static com.example.golf_apk.common.CommonMethod.getInfoFromStorage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.golf_apk.R;
import com.example.golf_apk.common.KeyType;

public class MenuActivity extends AppCompatActivity {
    private String accessToken;
    LinearLayout beforeLogin;
    LinearLayout afterLogin;
    TextView userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ImageButton closeButton = findViewById(R.id.btn_close_menu);
        TextView loginButton = findViewById(R.id.login_btn);
        beforeLogin = findViewById(R.id.before_login);
        afterLogin = findViewById(R.id.after_login);
        userName = findViewById(R.id.user_name);

        closeButton.setOnClickListener(closeThisActivityListener);
        loginButton.setOnClickListener(gotToLoginActivityListener);


        if(isLoggedIn()){
            String username = getInfoFromStorage(this, KeyType.NAME.getValue());
            beforeLogin.setVisibility(View.INVISIBLE);
            afterLogin.setVisibility(View.VISIBLE);
            userName.setText(username);
        }

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


    private boolean isLoggedIn() {
        accessToken = getAccessToken(this);
        return  accessToken != null;
    }
}
