package com.example.golf_apk.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.golf_apk.R;
import com.example.golf_apk.api.ApiService;
import com.example.golf_apk.api.RetrofitClient;
import com.example.golf_apk.common.CommonMethod;
import com.example.golf_apk.dto.PagedResponse;
import com.example.golf_apk.dto.PracticeGame;
import com.example.golf_apk.dto.adapter.PracticeArrayAdapter;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PracticeActivity extends AppCompatActivity {
    ApiService api;
    private Animation slideOut;
    private ListView listView;
    private TextView datePickerStartDate;
    private String startDate;
    private LinearLayout empty;
    private ArrayAdapter<PracticeGame> adapter;
    private List<PracticeGame> practiceGames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);


        ImageButton closeButton = findViewById(R.id.btn_close_practice);
        closeButton.setOnClickListener(closeThisActivityListener);
        CommonMethod.showLoading(PracticeActivity.this);

        datePickerStartDate = findViewById(R.id.showDatePicker);
        listView = findViewById(R.id.list_practice);
        practiceGames = new ArrayList<>();
        adapter = new ArrayAdapter<>(PracticeActivity.this, android.R.layout.simple_list_item_1, practiceGames);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd");

        LocalDate currentDate = LocalDate.now();
        startDate = currentDate.format(formatter);
        datePickerStartDate.setText(startDate);


        datePickerStartDate.setOnClickListener(openDatePickerStart);

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
        finish();
        overridePendingTransition(R.anim.not_move, R.anim.center_to_up);
    }

    private final View.OnClickListener openDatePickerStart = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showDatePickerDialog();
        }
    };
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                PracticeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
                        String selectedDate = selectedYear + "-" + (monthOfYear + 1)  + "-" + dayOfMonth;
                        startDate =selectedDate.substring(2);
                        datePickerStartDate.setText(selectedDate.substring(2));
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void getPractices() {
        api = RetrofitClient.getRetrofit().create(ApiService.class);
        String start="20"+startDate+"T00:00:00";
        String end="20"+startDate+"T23:59:59";
        int page =0;
        int size=10;
        Call<PagedResponse<PracticeGame>> call = api.getPractices(start, end, page, size,"playDate,desc");
        call.enqueue(new Callback<PagedResponse<PracticeGame>>() {
            @Override
            public void onResponse(Call<PagedResponse<PracticeGame>> call, Response<PagedResponse<PracticeGame>> response) {
                if (response.isSuccessful()) {
                    practiceGames.clear();
                    PagedResponse<PracticeGame> pagedResponse = response.body();
                    empty = findViewById(R.id.list_noContent);
                    // 리스트에 데이터를 추가하는 부분을 수정
                    if(pagedResponse.getEmbedded()!= null) {
                        practiceGames.addAll(pagedResponse.getEmbedded().getPractices());
                        empty.setVisibility(View.INVISIBLE);
                    }else{
                        empty.setVisibility(View.VISIBLE);
                    }
                    adapter = new PracticeArrayAdapter(PracticeActivity.this, practiceGames);
                    listView.setAdapter(adapter);
                    CommonMethod.hideLoading();
                } else {
                    // 서버 응답이 실패한 경우 처리
                }
            }

            @Override
            public void onFailure(Call<PagedResponse<PracticeGame>> call, Throwable t) {
                // 네트워크 오류 등에 대한 처리
                t.printStackTrace();
            }
        });
    }

}
