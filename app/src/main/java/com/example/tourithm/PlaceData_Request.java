package com.example.tourithm;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class PlaceData_Request extends StringRequest {
    // 서버 URL 설정 ( PHP 파일 연동 - Database 바로 접근 불가, php 중간 매체로 파싱하여 사용 )
    // php 파일 변경 시 수정 해야 함
    final static private String URL = "http://idox23.cafe24.com/Tourithm/PlaceData_insert.php";
    private Map<String, String> map;

    public PlaceData_Request(String name, String categori, String add_road, String add_area, String latitude, String longitude, String intro, String tel, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("name", name);
        map.put("categori", categori);
        map.put("add_road", add_road);
        map.put("add_area", add_area);
        map.put("latitude", latitude);
        map.put("longitude", longitude);
        map.put("intro", intro);
        map.put("tel", tel);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
