package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tourithm.R;
import com.naver.maps.map.overlay.InfoWindow;

public class MapAdapter extends InfoWindow.DefaultViewAdapter {

    private final Context mContext;
    private final ViewGroup mParent;

    public MapAdapter(@NonNull Context context, ViewGroup parent){
        super(context);
        mContext = context;
        mParent = parent;
    }

    @NonNull
    @Override
    protected View getContentView(@NonNull InfoWindow infoWindow) {
        View view = (View) LayoutInflater.from(mContext).inflate(R.layout.marker_info_item,mParent,false);
// 위도 경도 정보도 있어야할거같은데 ㅇㅅㅇ
        TextView txtTitle = (TextView) view.findViewById(R.id.mark_title);
        ImageView imagePoint = (ImageView) view.findViewById(R.id.mark_image);
        TextView txtAddr = (TextView) view.findViewById(R.id.mark_addr);
        TextView txtTel = (TextView) view.findViewById(R.id.mark_telnum);

        txtTitle.setText("동의대학교 정보공학관");
        imagePoint.setImageResource(R.drawable.ex);
        txtAddr.setText("부산광역시 부산진구 엄광로 176");
        txtTel.setText("051-890-1114");

        return view;
    }
}
