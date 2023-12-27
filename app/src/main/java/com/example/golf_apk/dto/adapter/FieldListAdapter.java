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
import com.example.golf_apk.dto.adapter.service.OnFieldClickListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class FieldListAdapter extends ArrayAdapter<JsonObject> {
    private List<JsonObject> objectList;

    private OnFieldClickListener onFieldClickListener;
    private LayoutInflater inflater;

    public FieldListAdapter(@NonNull Context context, @NonNull JsonArray jsonArray, OnFieldClickListener listener) {
        super(context, 0);
        this.objectList = jsonArrayToList(jsonArray);
        inflater = LayoutInflater.from(context);
        this.onFieldClickListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.field_list_adapter, parent, false);
        }

        TextView fieldId = view.findViewById(R.id.field_id);
        TextView filedName = view.findViewById(R.id.field_name);
        TextView filedAddress = view.findViewById(R.id.field_address);
        TextView filedCourse = view.findViewById(R.id.field_course);
        ImageView list_occupy = view.findViewById(R.id.list_occupy);

        JsonObject data = getItem(position);
        if (data != null) {
            fieldId.setText(data.getAsJsonPrimitive("id").getAsString());
            filedName.setText(data.getAsJsonPrimitive("name").getAsString());
            filedAddress.setText(data.getAsJsonPrimitive("address").getAsString());
            int holeCnt = Integer.parseInt(data.getAsJsonPrimitive("holes").getAsString());
            filedCourse.setText(data.getAsJsonPrimitive("holes").getAsString() + " 코스  "  + holeCnt * 9 + " 홀" );
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject data = getItem(position);
                if (data != null) {
                    String fieldId = data.getAsJsonPrimitive("id").getAsString();
                    String fieldName = data.getAsJsonPrimitive("name").getAsString();
                    // 인터페이스를 통해 클릭 이벤트를 PracticeActivity에 전달
                    if (onFieldClickListener != null) {
                        onFieldClickListener.onFieldClick(fieldId, fieldName);
                    }
                }
            }
        });

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
