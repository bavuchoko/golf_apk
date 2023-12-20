package com.example.golf_apk.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.golf_apk.R;
import com.example.golf_apk.api.ApiService;
import com.example.golf_apk.api.RetrofitClient;
import com.example.golf_apk.common.CommonMethod;
import com.example.golf_apk.common.KeyType;
import com.example.golf_apk.dto.Account;
import com.example.golf_apk.dto.AccountResponse;

import java.util.List;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ApiService api;
    boolean constraint =false;
    EditText username;
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username= findViewById(R.id.user_id);
        password= findViewById(R.id.passwd);
        TextView alert = findViewById(R.id.login_alert);
        Button loginBtn = findViewById(R.id.btn_login);


        ImageButton closeButton = findViewById(R.id.btn_close_login);

        closeButton.setOnClickListener(closeThisActivityListener);


        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String username = charSequence.toString();
                if(!TextUtils.isDigitsOnly(username) || username.length() != 11){
                    alert.setText("아이디는 11자리 숫자입니다");
                    constraint = false;
                }else{
                    EditText use_password = findViewById(R.id.passwd);
                    if(use_password.getEditableText().length()<1){
                        alert.setText("비밀번호를 입력하세요");
                        constraint = false;
                    }else{
                        alert.setText("로그인 하세요");
                        constraint = true;
                    }
                }
                setLoginBtnColor(constraint);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String passwd = charSequence.toString();
                if(passwd.length() < 1){
                    alert.setText("비밀번호를 입력하세요");
                    constraint = false;
                }else{
                    EditText use_username = findViewById(R.id.user_id);
                    if(!TextUtils.isDigitsOnly(use_username.getEditableText()) || use_username.getEditableText().length() != 11){
                        alert.setText("아이디는 11자리 숫자입니다");
                        constraint = false;
                    }else{
                        alert.setText("로그인 하세요");
                        constraint = true;
                    }
                }
                setLoginBtnColor(constraint);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethod.showLoading(LoginActivity.this);
                if (constraint) {
                    Account account= new Account();
                    account.setUsername(username.getText().toString());
                    account.setPassword(password.getText().toString());
                    api = RetrofitClient.getRetrofit().create(ApiService.class);
                    api.login(account).enqueue(new Callback<AccountResponse>() {
                        @Override
                        public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {

                            CommonMethod.hideLoading();

                            if (response.isSuccessful()) {
                                Headers headers = response.headers();
                                List<String> cookies = headers.values("Set-Cookie");
                                for (String cookie : cookies) {
                                    if (cookie.contains("refreshToken")) {
                                        String[] cookieParts = cookie.split(";");
                                        for (String cookiePart : cookieParts) {
                                            if (cookiePart.contains("refreshToken")) {
                                                String[] keyValue = cookiePart.split("=");
                                                String refreshToken = keyValue.length > 1 ? keyValue[1] : "";
                                                CommonMethod.saveRefreshToken(LoginActivity.this, refreshToken);
                                            }
                                        }
                                    }
                                }

                                AccountResponse authenticationResponse = response.body();
                                CommonMethod.saveAccessToken(LoginActivity.this,  authenticationResponse.getAccessToken());
                                CommonMethod.saveInfotoStorage(LoginActivity.this, KeyType.NAME.getValue(), authenticationResponse.getName());
                                CommonMethod.saveInfotoStorage(LoginActivity.this, KeyType.BIRTH.getValue(), authenticationResponse.getBirth());
                                CommonMethod.saveInfotoStorage(LoginActivity.this, KeyType.JOIN_DATE.getValue(), authenticationResponse.getJoinDate());

                                LoginActivity.this.getSupportFragmentManager().popBackStack();
                            }else{
                                CommonMethod.logout(LoginActivity.this);
                            }
                        }

                        //로그인 실패시 저장소에 토큰이 있으면 삭제
                        @Override
                        public void onFailure(Call<AccountResponse> call, Throwable t) {
                            CommonMethod.hideLoading();
                            CommonMethod.logout(LoginActivity.this);
                            CommonMethod.showAlert(LoginActivity.this,"로그인에 실패하였습니다.");
                        }
                    });
                } else {

                    CommonMethod.showAlert(LoginActivity.this,"로그인 정보를 입력하세요");
                }
            }
        });
    }

    private void setLoginBtnColor(Boolean bool) {
        Button loginBtn = findViewById(R.id.btn_login);
        if(bool){
            loginBtn.setBackgroundColor(Color.parseColor("#263177"));
        }else{
            loginBtn.setBackgroundColor(Color.parseColor("#7E7E7E"));
        }

    }


    private final View.OnClickListener closeThisActivityListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            closeMenuActivity();
        }
    };

    private void closeMenuActivity() {
        finish();
        overridePendingTransition(R.anim.not_move, R.anim.donw_to_up);
    }
}
