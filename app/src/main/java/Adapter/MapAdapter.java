package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tourithm.R;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.OverlayImage;

import org.w3c.dom.Text;

public class MapAdapter extends InfoWindow.DefaultViewAdapter {

    Context mContext;
    ViewGroup mParent;

    public MapAdapter(@NonNull Context context, ViewGroup parent){
        super(context);
        mContext = context;
        mParent = parent;
    }

    @NonNull
    @Override
    protected View getContentView(@NonNull InfoWindow infoWindow) {
        View view = (View) LayoutInflater.from(mContext).inflate(R.layout.marker_info_item,mParent,false);

        TextView txtTitle = (TextView) view.findViewById(R.id.mark_title);
        ImageView imagePoint = (ImageView) view.findViewById(R.id.mark_image);
        TextView txtAddr = (TextView) view.findViewById(R.id.mark_addr);
        TextView txtTel = (TextView) view.findViewById(R.id.mark_telnum);
        TextView txtCoro = (TextView) view.findViewById(R.id.mark_corona);

        txtTitle.setText("동의대학교 정보공학관");
        imagePoint.setImageResource(R.drawable.ex);
        txtAddr.setText("부산광역시 부산진구 엄광로 176");
        txtTel.setText("051-890-1114");
        txtCoro.setText("100");

        return view;
    }

}
