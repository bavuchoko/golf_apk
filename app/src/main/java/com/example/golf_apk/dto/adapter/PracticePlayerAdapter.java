package com.example.golf_apk.dto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.golf_apk.R;
import com.example.golf_apk.common.KeyType;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class PracticePlayerAdapter extends RecyclerView.Adapter<PracticePlayerAdapter.ViewHolder>  {

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView familyName;
        TextView name;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            familyName = (TextView) itemView.findViewById(R.id.player_family_name);
            name = (TextView) itemView.findViewById(R.id.player_name);
        }
    }

    private JsonArray data = null;

    public PracticePlayerAdapter(JsonArray data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.adapter_view_practice_player, parent, false);
        PracticePlayerAdapter.ViewHolder vh = new PracticePlayerAdapter.ViewHolder(view);
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull PracticePlayerAdapter.ViewHolder holder, int position) {
        JsonObject player = (JsonObject) data.get(position);
        String playerName = player.getAsJsonPrimitive(KeyType.NAME.getValue()).getAsString();
        holder.familyName.setText(playerName.substring(0, 1));
        holder.name.setText(playerName.length() > 1 ? playerName.substring(1) : "");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
