package com.example.tourithm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class localAdapter extends BaseAdapter {
    Context context;
    ArrayList<localitem> arrayList;

    public localAdapter(Context context, ArrayList<localitem> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.localitem, parent, false);
        }

        TextView local;
        local = convertView.findViewById(R.id.local_item);

        local.setText(arrayList.get(position).getLocal());

        return convertView;
    }

}
