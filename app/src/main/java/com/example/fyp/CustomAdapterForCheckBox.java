package com.example.fyp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class CustomAdapterForCheckBox extends BaseAdapter {
private Context context;
public static List<RowItem> rowItem;

public CustomAdapterForCheckBox (Context context, List<RowItem> rowItem){
    this.context = context;
    this.rowItem = rowItem;
}

    @Override
    public int getCount() {
        return rowItem.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItem.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.checkboxlist, null);
        }
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.iconCheckBox);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        RowItem row_pos = rowItem.get(position);

        // setting the image resource and title
        imgIcon.setImageResource(row_pos.getIcon());
        txtTitle.setText(row_pos.getTitle());
        return convertView;
    }
}
