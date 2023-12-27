package com.example.golf_apk.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.golf_apk.R;
import com.example.golf_apk.api.ApiService;
import com.example.golf_apk.api.RetrofitClient;
import com.example.golf_apk.common.CommonMethod;
import com.example.golf_apk.common.KeyType;
import com.example.golf_apk.dto.adapter.PracticeAdapter;
import com.example.golf_apk.dto.adapter.service.OnPracticeClickListener;
import com.example.golf_apk.ui.create.CreatePractice;
import com.example.golf_apk.ui.update.UpdatePractice;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoUnit;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PracticeListActivity extends AppCompatActivity implements OnPracticeClickListener {
    ApiService api;
    private Animation slideOut;
    private ListView listView;
    private TextView datePickerStartDate;
    private TextView datePickerEndDate;
    private String startDate;
    private String endDate;
    private LinearLayout empty;
    private PracticeAdapter adapter;
    private  JsonArray warmupGameDtoList;
    ImageButton closeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);


        closeButton = findViewById(R.id.btn_close_practice);
        closeButton.setOnClickListener(closeThisActivityListener);
        CommonMethod.showLoading(PracticeListActivity.this);

        datePickerStartDate = findViewById(R.id.showStartDatePicker);
        datePickerEndDate = findViewById(R.id.showEndDatePicker);
        listView = findViewById(R.id.list_practice);

        adapter = new PracticeAdapter(PracticeListActivity.this, warmupGameDtoList, this);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd");

        LocalDate currentDate = LocalDate.now();
        LocalDate threeDaysAgo = currentDate.minus(3, ChronoUnit.DAYS);
        startDate = threeDaysAgo.format(formatter);
        endDate = currentDate.format(formatter);
        datePickerStartDate.setText(startDate);
        datePickerEndDate.setText(endDate);


        datePickerStartDate.setOnClickListener(openDatePickerStart);
        datePickerEndDate.setOnClickListener(openDatePickerEnd);

        datePickerStartDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getPractices();
            }
        });
        datePickerEndDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getPractices();
            }
        });
        getPractices();
        listView.setAdapter(adapter);
    }



    private final View.OnClickListener closeThisActivityListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            closethisActivity();
        }
    };
    private void closethisActivity() {
        closeButton.setBackgroundResource(R.drawable.btn_clicked_gray);
        finish();
        overridePendingTransition(R.anim.not_move, R.anim.center_to_up);
    }

    private final View.OnClickListener openDatePickerStart = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showDatePickerDialog(KeyType.START_DATE);
        }
    };
    private final View.OnClickListener openDatePickerEnd = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showDatePickerDialog(KeyType.END_DATE);
        }
    };
    private void showDatePickerDialog(KeyType keyType) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                PracticeListActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
                        String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, monthOfYear + 1, dayOfMonth);
                        if(keyType== KeyType.START_DATE){
                            startDate =selectedDate.substring(2);
                            datePickerStartDate.setText(selectedDate.substring(2));
                        }else{
                            endDate =selectedDate.substring(2);
                            datePickerEndDate.setText(selectedDate.substring(2));
                        }
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void getPractices() {
        api = RetrofitClient.getRetrofit().create(ApiService.class);
        String start="20"+startDate+"T00:00:00";
        String end="20"+endDate+"T23:59:59";
        int page =0;
        int size=10;
        Call<ResponseBody> call;
        String accessToken = CommonMethod.getAccessToken(PracticeListActivity.this);
        if(accessToken !=null){
            call = api.getPractices(start, end, page, size,"playDate,desc", "Bearer " + accessToken);
        }else{
            call = api.getPractices(start, end, page, size,"playDate,desc");
        }
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    empty = findViewById(R.id.list_noContent);
                    // 리스트에 데이터를 추가하는 부분을 수정
                    try {
                        if (responseBody != null) {
                            String jsonString= responseBody.string();

                            JsonElement jsonElement= JsonParser.parseString(jsonString);

                            warmupGameDtoList = new JsonArray();
                            if (jsonElement != null) {
                                JsonObject embeddedObject = jsonElement.getAsJsonObject().getAsJsonObject("_embedded");
                                if (embeddedObject != null) {
                                    JsonElement warmupGameListElement = embeddedObject.get("warmupGameDtoList");
                                    if (warmupGameListElement != null && warmupGameListElement.isJsonArray()) {
                                        warmupGameDtoList = warmupGameListElement.getAsJsonArray();
                                        empty.setVisibility(View.INVISIBLE);
                                    }
                                }else{
                                    empty.setVisibility(View.VISIBLE);
                                }
                            }else{
                                empty.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        responseBody.close();
                    }
                    if (warmupGameDtoList != null) {
                        adapter.updateData(warmupGameDtoList);
                    }
                    CommonMethod.hideLoading();
                } else {
                    // 서버 응답이 실패한 경우 처리
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 네트워크 오류 등에 대한 처리
                t.printStackTrace();
            }
        });
    }


    @Override
    public void onPracticeClick(String id) {
        Intent intent = new Intent(PracticeListActivity.this, UpdatePractice.class);
        intent.putExtra("id", id);
        startActivity(intent);
        overridePendingTransition(R.anim.center_to_up, R.anim.not_move);
    }
}
