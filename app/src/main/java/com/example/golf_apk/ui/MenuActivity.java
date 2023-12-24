package com.example.golf_apk.ui;

import static com.example.golf_apk.common.CommonMethod.getAccessToken;
import static com.example.golf_apk.common.CommonMethod.getInfoFromStorage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.golf_apk.R;
import com.example.golf_apk.common.CommonMethod;
import com.example.golf_apk.common.KeyType;
import com.example.golf_apk.dto.ExpandableMenuItem;
import com.example.golf_apk.ui.create.CreateField;
import com.example.golf_apk.ui.create.CreateMatch;
import com.example.golf_apk.ui.create.CreatePractice;
import com.example.golf_apk.dto.adapter.ExpandableListAdapter;
import com.example.golf_apk.ui.update.UpdatePractice;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Period;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<ExpandableMenuItem>> listDataChild;

    private String accessToken;
    LinearLayout beforeLogin;
    LinearLayout afterLogin;
    TextView userName;
    TextView userAge;
    TextView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        
        ImageButton closeButton = findViewById(R.id.btn_close_menu);
        TextView loginButton = findViewById(R.id.login_btn);
        beforeLogin = findViewById(R.id.before_login);
        afterLogin = findViewById(R.id.after_login);
        userName = findViewById(R.id.user_name);
        userAge = findViewById(R.id.user_age);
        logout = findViewById(R.id.btn_logout);

        closeButton.setOnClickListener(closeThisActivityListener);
        loginButton.setOnClickListener(gotToLoginActivityListener);
        logout.setOnClickListener(actionLogout);
        LocalDate currentDate = LocalDate.now();


        if(isLoggedIn()){
            String username = getInfoFromStorage(this, KeyType.NAME.getValue());
            String birth = getInfoFromStorage(this, KeyType.BIRTH.getValue());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

            char lastChar = birth.charAt(birth.length() - 1);
            String preYear = (lastChar == '1' || lastChar == '2') ? "19" : "20";

            LocalDate birthLocal = LocalDate.parse(preYear + birth.substring(0,6), formatter);
            beforeLogin.setVisibility(View.INVISIBLE);
            afterLogin.setVisibility(View.VISIBLE);
            Period age = Period.between(birthLocal, currentDate);
            userName.setText(username);
            userAge.setText("만 "+age.getYears()+"세");
        }

        // ExpandableListView 초기화
        expandableListView = findViewById(R.id.expandable_menu_playing);

        // 그룹 데이터 및 하위 항목 데이터 초기화
        initData();

        // 어댑터 생성 및 설정
        expandableListAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expandableListView.setAdapter(expandableListAdapter);
        
        //확장메뉴의 자식메뉴 클릭 이벤트
        expandableListView.setOnChildClickListener(expandableChildClickListener);

    }

    private final View.OnClickListener closeThisActivityListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            closeMenuActivity();
        }
    };

    private final View.OnClickListener actionLogout = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CommonMethod.clearAll(MenuActivity.this);
            finish();
            overridePendingTransition(0, 0);
            // 액티비티 다시 시작
            startActivity(getIntent());
        }
    };



    private final ExpandableListView.OnChildClickListener expandableChildClickListener = new  ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

            if (groupPosition == 0) {
                // 클릭된 아이템에 해당하는 정보 가져오기
                ExpandableMenuItem clickedItem = expandableListAdapter.getMenuDataList().get(listDataHeader.get(groupPosition)).get(childPosition);

                // view 페이지로 이동
                Intent intent = new Intent(MenuActivity.this, UpdatePractice.class);
                intent.putExtra("id", clickedItem.getId());
                startActivity(intent);
                
                
            } else if (groupPosition == 1) {
                //새로운 등록 페이지
                Intent intent = null;

                switch (childPosition) {
                    case 0:
                        // 필드 등록 페이지로 이동
                        intent = new Intent(MenuActivity.this, CreateField.class);
                        overridePendingTransition(R.anim.up_to_center, R.anim.not_move);
                        break;
                    case 1:
                        // 경기 등록 페이지로 이동
                        intent = new Intent(MenuActivity.this, CreateMatch.class);
                        overridePendingTransition(R.anim.up_to_center, R.anim.not_move);
                        break;
                    case 2:
                        // 연습경기 등록하기 페이지로 이동
                        intent = new Intent(MenuActivity.this, CreatePractice.class);
                        overridePendingTransition(R.anim.up_to_center, R.anim.not_move);
                        break;
                }
                startActivity(intent);
            }
            return true;
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
        overridePendingTransition(R.anim.not_move, R.anim.center_to_left);
    }

    private void openLoginActivity() {
        Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.up_to_center, R.anim.not_move);
    }


    private boolean isLoggedIn() {
        accessToken = getAccessToken(this);
        return  accessToken != null;
    }

    private void initData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        List<List<ExpandableMenuItem>> childList = new ArrayList<>();

        // 그룹 추가
        listDataHeader.add("진행중인 경기보기");
        List<ExpandableMenuItem> nowPlaying = new ArrayList<>();
        childList.add(nowPlaying);

        listDataHeader.add("새로운 정보 등록");
        List<ExpandableMenuItem> createMenu = new ArrayList<>();
        createMenu.add(new ExpandableMenuItem(1,"필드 정보"));
        createMenu.add(new ExpandableMenuItem(1,"경기 등록"));
        createMenu.add(new ExpandableMenuItem(1,"연습경기 등록하기"));
        childList.add(createMenu);

        for(int i =0; i< listDataHeader.size(); i++){
            listDataChild.put(listDataHeader.get(i), childList.get(i));
        }
    }
}
