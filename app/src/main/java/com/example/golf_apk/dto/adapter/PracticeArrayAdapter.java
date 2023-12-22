package com.example.golf_apk.dto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.golf_apk.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class PracticeArrayAdapter extends ArrayAdapter<JsonObject> {
    private List<JsonObject> objectList;
    private LayoutInflater inflater;

    public PracticeArrayAdapter(@NonNull Context context, @NonNull JsonArray jsonArray) {
        super(context, 0);
        this.objectList = jsonArrayToList(jsonArray);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.practice_list_adapter, parent, false);
        }

        TextView playDate = view.findViewById(R.id.play_date);
        TextView playStatus = view.findViewById(R.id.play_status);
        TextView filedName = view.findViewById(R.id.field_name);
        TextView filedAddress = view.findViewById(R.id.field_address);
        ImageView list_occupy = view.findViewById(R.id.list_occupy);

        JsonObject practiceObject = getItem(position);
        if (practiceObject != null) {
            LocalDateTime dateTime = LocalDateTime.parse(practiceObject.getAsJsonPrimitive("playDate").getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            int month = dateTime.getMonthValue();
            int day = dateTime.getDayOfMonth();
            String amPm = dateTime.getHour() < 12 ? "오전" : "오후";
            playDate.setText(month + " 월 " + day + " 일 " + amPm);

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
                    statusText = "OPEN";
                    playStatus.setBackgroundResource(R.drawable.btn_green_whitestroke);
                    break;
                case "END":
                    statusText = "END";
                    playStatus.setBackgroundResource(R.drawable.btn_red_whitestroke);
                    break;
            }

            JsonObject fieldObject = practiceObject.getAsJsonObject("field");
            filedName.setText(fieldObject != null ? fieldObject.getAsJsonPrimitive("name").getAsString() : "경기장 미지정");
            filedAddress.setText(fieldObject != null ? fieldObject.getAsJsonPrimitive("address").getAsString() : "주소 미상");

            // _links 확인 및 update 여부에 따라 이미지 변경
            JsonObject linksObject = practiceObject.getAsJsonObject("_links");
            if (linksObject != null && linksObject.has("update")) {
                // update 링크가 있는 경우 이미지를 변경
                 list_occupy.setImageResource(R.drawable.baseline_star_24); // 새로운 이미지 리소스를 설정해주세요
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
}
