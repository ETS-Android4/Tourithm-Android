package com.example.tourithm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tourithm.R;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.OverlayImage;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MapAdapter extends BaseAdapter {

    Context mContext;
    ViewGroup mParent;

    public MapAdapter(@NonNull Context context, ViewGroup parent){
        mContext = context;
        mParent = parent;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = (View) LayoutInflater.from(mContext).inflate(R.layout.map,mParent,false);

        TextView title = (TextView) view.findViewById(R.id.mark_title);      // 가게 이름
        ImageView image = (ImageView) view.findViewById(R.id.mark_image);    // 가게 이미지
        TextView addr = (TextView) view.findViewById(R.id.mark_addr);        // 가게 주소(도로명)
        TextView tel = (TextView) view.findViewById(R.id.mark_telnum);       // 가게 번호
        TextView coro = (TextView) view.findViewById(R.id.mark_corona);      // 가게 코로나 현황
        //View lineview = (View) view.findViewById(R.id.mark_line);          // 라인(색상변경위함)

        title.setText("뀨");
        image.setImageResource(R.drawable.ex);
        addr.setText("부산광역시 부산진구 엄광로 176");
        tel.setText("051-890-1114");
        coro.setText("100");
        return convertView;
    }

}
