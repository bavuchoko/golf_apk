package com.example.golf_apk.dto.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.golf_apk.R;
import com.example.golf_apk.dto.PracticeGame;

import java.util.List;

public class PracticeArrayAdapter extends ArrayAdapter<PracticeGame> {
    private LayoutInflater inflater;
    public PracticeArrayAdapter(@NonNull Context context, @NonNull List<PracticeGame> objects) {
        super(context, 0, objects);
        inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.practice_list_adapter, parent, false);
        }

        TextView textView1 = view.findViewById(R.id.play_date);
        TextView textView2 = view.findViewById(R.id.play_status);

        PracticeGame practiceGame = getItem(position);
        if (practiceGame != null) {
            textView1.setText(practiceGame.getPlayDate().substring(5,10));
            String statusText ="";
            String status = practiceGame.getStatus();
            switch (status){
                case "PLAYING": statusText= "플레이중...";
                    textView2.setTextColor(Color.parseColor("#0000ff"));
                    break;
                case "CLOSE": statusText= "종료";
                    textView2.setTextColor(Color.parseColor("#b30000"));
                    break;
                case "OPEN": statusText= "등록중..";
                    textView2.setTextColor(Color.parseColor("#727274"));
                case "END": statusText= "종료";
                    textView2.setTextColor(Color.parseColor("#b30000"));
            }
            textView2.setText(statusText);

        }

        return view;
    }
}
