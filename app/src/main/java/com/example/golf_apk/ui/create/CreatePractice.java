package com.example.golf_apk.ui.create;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.golf_apk.R;
import com.example.golf_apk.api.ApiService;
import com.example.golf_apk.api.RetrofitClient;
import com.example.golf_apk.common.CommonMethod;
import com.example.golf_apk.dto.adapter.FieldListAdapter;
import com.example.golf_apk.dto.adapter.service.OnFieldClickListener;
import com.example.golf_apk.ui.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePractice extends AppCompatActivity implements OnFieldClickListener {

    ApiService api;
    private Animation slideOut;
    private ListView listView;
    private FieldListAdapter adapter;

    private TextView btnCreate;
    private TextView btnFavorite;
    private EditText search;

    private ImageView deleteField;
    private LinearLayout searchFieldLayout;
    private LinearLayout selectedFieldLayout;
    private JsonArray fieldDtoList;
    private boolean fieldSelected = false;
    private String selectedFieldId;
    private TextView selectedFieldName;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_practice_create);
        ImageButton closeButton = findViewById(R.id.btn_close_practice_create);
        closeButton.setOnClickListener(closeThisActivityListener);
        search = findViewById(R.id.field_search);
        listView = findViewById(R.id.list_practice_create_field);
        adapter = new FieldListAdapter(CreatePractice.this, fieldDtoList, this);
        btnCreate = findViewById(R.id.btn_practice_create);
        btnFavorite = findViewById(R.id.btn_field_favorite);
        deleteField = findViewById(R.id.btn_delete_field);
        selectedFieldLayout = findViewById(R.id.selected_field);
        searchFieldLayout = findViewById(R.id.search_field_layout);
        selectedFieldName = findViewById(R.id.selected_field_name);


        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    getFields();
                    return true;
                }
                return false;
            }
        });

        getFields();
        listView.setAdapter(adapter);

        btnFavorite.setOnClickListener(favoriteFieldListener);
        btnCreate.setOnClickListener(createPracticeListener);
        deleteField.setOnClickListener(deleteFieldListener);
    }



    private void getFields() {
        api = RetrofitClient.getRetrofit().create(ApiService.class);

        int page =0;
        int size=10000;
        Call<ResponseBody> call;

        String searchTxt ="";
        if (search != null) {
            searchTxt = search.getText().toString();
        }
        call = api.geFields(searchTxt, page, size);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    // 리스트에 데이터를 추가하는 부분을 수정
                    try {
                        if (responseBody != null) {
                            String jsonString= responseBody.string();

                            JsonElement jsonElement= JsonParser.parseString(jsonString);

                            fieldDtoList = new JsonArray();
                            if (jsonElement != null) {
                                JsonObject embeddedObject = jsonElement.getAsJsonObject().getAsJsonObject("_embedded");
                                if (embeddedObject != null) {
                                    JsonElement warmupGameListElement = embeddedObject.get("fieldsDtoList");
                                    if (warmupGameListElement != null && warmupGameListElement.isJsonArray()) {
                                        fieldDtoList = warmupGameListElement.getAsJsonArray();

                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        responseBody.close();
                    }
                    if (fieldDtoList != null) {
                        adapter.updateData(fieldDtoList);
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
    private final View.OnClickListener createPracticeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            createPractice();
        }
    };

    private void createPractice() {
        String accessToken = CommonMethod.getAccessToken(CreatePractice.this);
        Call<ResponseBody> call;
        api = RetrofitClient.getRetrofit().create(ApiService.class);

        Gson gson = new Gson();
        String json = gson.toJson("dataModel");

        // JSON 문자열을 RequestBody로 변환
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        if (accessToken != null) {
            call = api.createPractice(requestBody, accessToken);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        // response.body()를 사용하여 응답 데이터에 접근할 수 있음
                        ResponseBody responseBody = response.body();
                        // responseBody를 처리하는 로직 추가
                    } else {
                        // 요청이 실패한 경우
                        // response.errorBody()를 사용하여 에러 응답에 접근할 수 있음
                        ResponseBody errorBody = response.errorBody();
                        // errorBody를 처리하는 로직 추가
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // 네트워크 요청 실패 또는 응답을 받지 못한 경우
                    // t에는 실패의 이유에 대한 정보가 포함되어 있음
                }
            });
        } else {
            // 알림창을 띄웁니다.
            AlertDialog.Builder builder = new AlertDialog.Builder(CreatePractice.this);
            builder.setMessage("로그인이 필요합니다.")
                    .setPositiveButton("로그인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // 확인 버튼을 누를 때 LoginActivity로 이동하는 코드
                            Intent intent = new Intent(CreatePractice.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // 취소 버튼을 누를 때 액티비티를 닫는 코드
                            closeThisActivityListener.onClick(null);
                        }
                    });
            builder.create().show();
        }
    }

    private final View.OnClickListener deleteFieldListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            deleteField();
        }
    };

    private void deleteField() {
        fieldSelected = false;
        selectedFieldId = null;
        selectedFieldLayout.setVisibility(View.GONE);
        selectedFieldName.setText("");
        listView.setVisibility(View.VISIBLE);
        searchFieldLayout.setVisibility(View.VISIBLE);
        btnFavorite.setVisibility(View.VISIBLE);
    }

    private final View.OnClickListener favoriteFieldListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            favoriteField();
        }
    };

    private void favoriteField() {
        finish();
        overridePendingTransition(R.anim.not_move, R.anim.center_to_up);
    }
    @Override
    public void onFieldClick(String fieldId, String fieldName) {
            fieldSelected = true;
            selectedFieldId = fieldId;
            selectedFieldLayout.setVisibility(View.VISIBLE);
            selectedFieldName.setText(fieldName);
            listView.setVisibility(View.GONE);
            searchFieldLayout.setVisibility(View.GONE);
            btnFavorite.setVisibility(View.GONE);
    }
}
