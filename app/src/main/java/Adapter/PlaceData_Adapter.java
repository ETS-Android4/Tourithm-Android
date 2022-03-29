package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tourithm.PlaceData_item;
import com.example.tourithm.R;

import java.util.ArrayList;

public class PlaceData_Adapter extends BaseAdapter {
    Context context;
    ArrayList<PlaceData_item> arrayList;

    public PlaceData_Adapter(Context context, ArrayList<PlaceData_item> arrayList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.data_item, parent, false);
        }

        TextView trrsrtNm, trrsrtSe, rdnmadr, lnmadr, latitude, longitude, trrsrtIntrcn, phoneNumber;

        trrsrtNm = convertView.findViewById(R.id.tv_trrsrtNm);
        trrsrtSe = convertView.findViewById(R.id.tv_trrsrtSe);
        rdnmadr = convertView.findViewById(R.id.tv_rdnmadr);
        lnmadr = convertView.findViewById(R.id.tv_lnmadr);
        latitude = convertView.findViewById(R.id.tv_latitude);
        longitude = convertView.findViewById(R.id.tv_longitude);
        trrsrtIntrcn = convertView.findViewById(R.id.tv_trrsrtIntrcn);
        phoneNumber = convertView.findViewById(R.id.tv_phoneNumber);

        trrsrtNm.setText("관광지명 : " + arrayList.get(position).getTrrsrtNm());
        trrsrtSe.setText("관광지 구분 : " + arrayList.get(position).getTrrsrtSe());
        rdnmadr.setText("도로명 주소 : " + arrayList.get(position).getRdnmadr());
        lnmadr.setText("지번 주소 : " + arrayList.get(position).getLnmadr());
        latitude.setText("위도 : " + arrayList.get(position).getLatitude());
        longitude.setText("경도 : " + arrayList.get(position).getLongitude());
        trrsrtIntrcn.setText("관광지 소개 : " + arrayList.get(position).getTrrsrtIntrcn());
        phoneNumber.setText("관리기관 전화번호 : " + arrayList.get(position).getPhoneNumber());


        return convertView;
    }

}
