package com.example.tourithm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlaceData_Activity extends AppCompatActivity {

    ArrayList<String> list = new ArrayList<String>();

    String name, categori, add_road, add_area, latitude, longitude, intro, tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_list);

        // PlaceData.java 에서 리스트 인텐트 받아옴
        Intent intent = getIntent();
        list = intent.getExtras().getStringArrayList("placedata_list");

        // DB 연동
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("[TAG] 요청 디버깅", "DB 연결 여부 " + response); // 디버깅 - php 파일 전부 보여줌
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success"); // 연결 성공시 success = true

                    if (success) { // 요청에 성공한 경우
                        Toast.makeText(getApplicationContext(), "요청에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                        Log.d("[TAG] 요청 디버깅", "(요청 성공)");
                    }

                    else { // 요청에 실패한 경우
                        Toast.makeText(getApplicationContext(), "요청에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        Log.d("[TAG] 요청 디버깅", "(요청 실패) 입력한 값 오류");
                        return;
                    }

                } catch (JSONException e) {
                    Log.d("[TAG] 요청 디버깅", "(데이터베이스 연결 실패) catch exception");
                    e.printStackTrace();
                }
            }
        };

        // PlaceData_Request.java에 값 넘겨줌
        for (int i=0; i<list.size(); i+=8 ){
            name = list.get(i);
            categori = list.get(i+1);
            add_road = list.get(i+2);
            add_area = list.get(i+3);
            latitude = list.get(i+4);
            longitude = list.get(i+5);
            intro = list.get(i+6);
            tel = list.get(i+7);

            PlaceData_Request placeData_request = new PlaceData_Request(name, categori, add_road, add_area, latitude, longitude, intro, tel, responseListener);
            RequestQueue queue = Volley.newRequestQueue(PlaceData_Activity.this);
            queue.add(placeData_request);
        }

    }
}
