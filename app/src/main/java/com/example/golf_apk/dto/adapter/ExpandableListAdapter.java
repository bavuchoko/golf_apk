package com.example.golf_apk.dto.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.golf_apk.R;
import com.example.golf_apk.dto.ExpandableMenuItem;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context context;
        private List<String> listDataHeader; // 그룹 목록
        private HashMap<String, List<ExpandableMenuItem>> listDataChild; // 하위 항목 목록
        private HashMap<String, List<ExpandableMenuItem>> menuDataList;

        public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                     HashMap<String, List<ExpandableMenuItem>> listChildData) {
            this.context = context;
            this.listDataHeader = listDataHeader;
            this.listDataChild = listChildData;
        }

        @Override
        public int getGroupCount() {
            return listDataHeader.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return listDataChild.get(listDataHeader.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return listDataHeader.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.activity_menu_create_parent, null);
            }

            TextView listHeader = convertView.findViewById(R.id.expand_parent_title);
            listHeader.setText(headerTitle);

            // 이미지뷰를 동적으로 변경
            ImageView explainIcon = convertView.findViewById(R.id.explainIcon);
            explainIcon.setImageResource(isExpanded ? R.drawable.baseline_expand_more_24 : R.drawable.baseline_chevron_left_24);

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            final String childText = ((ExpandableMenuItem) getChild(groupPosition, childPosition)).getTitle();

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.activity_menu_create_item, null);
            }

            TextView txtListChild = convertView.findViewById(R.id.menu_create_list_item);
            txtListChild.setText(childText);

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    public void setMenuDataList(HashMap<String, List<ExpandableMenuItem>> menuDataList) {
        this.menuDataList = menuDataList;
    }

    public HashMap<String, List<ExpandableMenuItem>> getMenuDataList() {
        return menuDataList;
    }
}

