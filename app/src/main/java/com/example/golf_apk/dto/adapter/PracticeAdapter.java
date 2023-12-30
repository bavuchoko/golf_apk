package com.example.golf_apk.dto.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.golf_apk.R;
import com.example.golf_apk.common.CommonMethod;
import com.example.golf_apk.common.KeyType;
import com.example.golf_apk.dto.adapter.service.OnPracticeClickListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PracticeAdapter extends ArrayAdapter<JsonObject> {
    private List<JsonObject> objectList;
    private LayoutInflater inflater;

    private OnPracticeClickListener onPracticeClickListener;
    private List<TextView> players;

    public PracticeAdapter(@NonNull Context context, @NonNull JsonArray jsonArray, OnPracticeClickListener listener) {
        super(context, 0);
        this.objectList = jsonArrayToList(jsonArray);
        inflater = LayoutInflater.from(context);
        this.onPracticeClickListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.adapter_practices, parent, false);
        }
        String accessToken = CommonMethod.getAccessToken(PracticeAdapter.this.getContext());
        TextView playDate = view.findViewById(R.id.play_date);
        TextView playStatus = view.findViewById(R.id.play_status);
        TextView filedName = view.findViewById(R.id.field_name);
        TextView filedAddress = view.findViewById(R.id.field_address);
        ImageView list_occupy = view.findViewById(R.id.list_occupy);


        players = new ArrayList<>();
        TextView player1 =view.findViewById(R.id.btn_join_player1);
        TextView player2 =view.findViewById(R.id.btn_join_player2);
        TextView player3 =view.findViewById(R.id.btn_join_player3);
        TextView player4 =view.findViewById(R.id.btn_join_player4);
        player1.setText("+");
        player1.setBackgroundResource(R.drawable.emptyuser);
        player1.setTextColor(Color.parseColor("#ffffff"));
        player2.setText("+");
        player2.setBackgroundResource(R.drawable.emptyuser);
        player2.setTextColor(Color.parseColor("#ffffff"));
        player3.setText("+");
        player3.setBackgroundResource(R.drawable.emptyuser);
        player3.setTextColor(Color.parseColor("#ffffff"));
        player4.setText("+");
        player4.setBackgroundResource(R.drawable.emptyuser);
        player4.setTextColor(Color.parseColor("#ffffff"));
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        boolean canJoin = false;

        JsonObject practiceObject = getItem(position);
        if (practiceObject != null) {
            final String currentPracticeId = practiceObject.getAsJsonPrimitive("id").getAsString();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToPracticeView(currentPracticeId);
                }
            });

            LocalDateTime dateTime = LocalDateTime.parse(practiceObject.getAsJsonPrimitive("playDate").getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
            String formattedDateTime = dateTime.format(formatter);

            DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("M 월 d 일 a h 시 m 분", Locale.KOREA);
            LocalDateTime parsedDateTime = LocalDateTime.parse(formattedDateTime, formatter);
            String displayDateTime = parsedDateTime.format(displayFormatter);

            playDate.setText(displayDateTime);
            JsonArray playersArray = new JsonArray();
            String statusText = "";
            String status = practiceObject.getAsJsonPrimitive("status").getAsString();
            switch (status) {
                case "PLAYING":
                    statusText = "PLAYING";
                    playStatus.setBackgroundResource(R.drawable.btn_blue_whitestroke);
                    break;
                case "CLOSE":
                    statusText = "END";
                    playStatus.setBackgroundResource(R.drawable.btn_red_whitestroke);
                    break;
                case "OPEN":
                    canJoin = true;
                    statusText = "OPEN";
                    playStatus.setBackgroundResource(R.drawable.btn_green_whitestroke);  //btn_round_empty
                    break;
                case "END":
                    statusText = "END";
                    playStatus.setBackgroundResource(R.drawable.btn_red_whitestroke);
                    break;
            }

            JsonElement fieldElement = practiceObject.get("field");
            filedName.setText(!fieldElement.isJsonNull() ? fieldElement.getAsJsonObject().getAsJsonPrimitive("name").getAsString() : "경기장 미지정");
            filedAddress.setText(!fieldElement.isJsonNull() ? fieldElement.getAsJsonObject().getAsJsonPrimitive("address").getAsString() : "주소 미상");


            JsonElement playersElement = practiceObject.get("players");
            //참가선수가 있으면
            if (playersElement != null && playersElement.isJsonArray()) {
                playersArray = playersElement.getAsJsonArray();

                for(int i =0; i< playersArray.size(); i++){
                    String name = playersArray.get(i).getAsJsonObject().getAsJsonPrimitive(KeyType.NAME.getValue()).getAsString();
                    if(name.length()>=3){
                        name = name.substring(name.length()-2,name.length());
                    }
                    players.get(i).setText(name);
                    players.get(i).setBackgroundResource(R.drawable.btn_player_occupied);
                    players.get(i).setTextColor(Color.parseColor("#000000"));
                }

            } else {  //아무도 참가하지 않았으면

            }




            // _links 확인 및 update 여부에 따라 이미지 변경
            JsonObject linksObject = practiceObject.getAsJsonObject("_links");
            if (linksObject != null && linksObject.has("update")) {
                canJoin = false;
                // update 링크가 있는 경우 -> 내가 만든 경기
                 list_occupy.setImageResource(R.drawable.baseline_star_24);
            }

            // 현재 로그인을 해 있고, 현재 참가자 자리가 여유가 있으며
            if (accessToken == null || playersArray.size() > 3) {
                canJoin = false;
            }
            //현재 이미 참가자로 등록되어 있지 않으면
            for (JsonElement player : playersArray) {
                if (player.isJsonObject()) {
                    JsonObject playerObject = player.getAsJsonObject();
                    String id = playerObject.getAsJsonPrimitive("id").getAsString();
                    if (id != null && id.equals(CommonMethod.getInfoFromStorage(PracticeAdapter.this.getContext(), KeyType.ID.getValue()))) {
                        canJoin = false;
                        break;
                    }
                }
            }
            if (canJoin) {      //참가할 수 있는 상태
                //참가버튼을 활성화 한다.

            }

            playStatus.setText(statusText);
        }


        return view;
    }

    private List<JsonObject> jsonArrayToList(JsonArray jsonArray) {
        List<JsonObject> list = new ArrayList<>();
        if(jsonArray != null) {
            for (JsonElement element : jsonArray) {
                if (element.isJsonObject()) {
                    list.add(element.getAsJsonObject());
                }
            }
        }
        return list;
    }

    public void updateData(JsonArray newData) {
        clear();  // 기존 데이터를 비우고
        addAll(jsonArrayToList(newData));  // 새로운 데이터를 추가
        notifyDataSetChanged();
    }



    private void goToPracticeView(String id) {
        if (onPracticeClickListener != null) {
            onPracticeClickListener.onPracticeClick(id);
        }
    }
}
