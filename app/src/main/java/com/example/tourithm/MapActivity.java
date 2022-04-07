package com.example.tourithm;

import android.Manifest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.LocationButtonView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private NaverMap naverMap;
    private FusedLocationSource locationSource;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    LatLng[] latlng = new LatLng[100];
    String[][] infodata = new String[100][5];
    LinearLayout info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        new MapActivity.Select_local_Request().execute();

        info = findViewById(R.id.map_mark_info);
        // 정보창 가리기
        info.setVisibility(View.GONE);

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_view);

        if(mapFragment == null){
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map_view, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
        locationSource = new FusedLocationSource(this,LOCATION_PERMISSION_REQUEST_CODE);

    }

    // DB - 데이터 불러오기
    class Select_local_Request extends AsyncTask<String, Integer, String> {
        String result = null;
        @Override
        protected String doInBackground(String... rurls) {
            try {
                URL url = new URL("https://idox23.cafe24.com/Tourithm/PlaceData_result.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                if(conn.getResponseCode()==HttpURLConnection.HTTP_OK) {
                    InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    result = stringBuilder.toString();
                } else {
                    result = "error";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String _result) {
            try {
                JSONObject root = new JSONObject(_result);
                JSONArray results = new JSONArray(root.getString("results"));

                for (int index = 0; index < results.length(); index++) {
                    JSONObject Content = results.getJSONObject(index);

                    // 관광지명, 도로주소, 위도, 경도, 전번 순
                    String name = Content.getString("name");
                    String road = Content.getString("add_road");
                    double latitude = Content.getDouble("latitude");
                    double longitude = Content.getDouble("longitude");
                    String tel = Content.getString("tel");

                    // 데이터 위도 경도 배열에 넣기
                    latlng[index] = new LatLng(latitude, longitude);

                    // double -> String
                    String st_latitude = Double.toString(latitude);
                    String st_longitude = Double.toString(latitude);
                    // 데이터 배열에 넣기
                    infodata[index][0] = st_latitude;
                    infodata[index][1] = st_longitude;
                    infodata[index][2] = name;
                    infodata[index][3] = road;
                    infodata[index][4] = tel;

                    for(int index2 = 0; index2 < 5; index2++)
                        System.out.println("[infodata]"+infodata[index][index2]);
                }
                Log.d("[latlng]", String.valueOf(latlng.length));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;

        // 지도 유형
        naverMap.setMapType(NaverMap.MapType.Basic);

        // 위도 경도
        LatLng exlatlng = new LatLng(35.145452, 129.036782);
        // 지도 중심 (임의)
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(exlatlng);
        naverMap.moveCamera(cameraUpdate);

        // ui : GPS
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(false);
        LocationButtonView locationButtonView = findViewById(R.id.map_gps_bt);
        locationButtonView.setMap(naverMap);
        // 내위치로 이동하기 ( 폰으로 할때 주석 풀기 )
        //naverMap.setLocationSource(locationSource);
        //naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        // 권한 확인
        ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);

        // 마커 찍기
        for(int index = 0; index < 100; index++){ //
            // addInfo(latlng[index]);
        }

        // -- 정보창 띄우기 --

        // 지도를 클릭하면 정보 창을 닫음
        naverMap.setOnMapClickListener((coord, point) -> {
            info.setVisibility(View.GONE);
        });

    }

    // 마커 찍기
    public void addInfo(LatLng latLng) {
        Marker marker = new Marker();

        //marker.setPosition(latLng);
        marker.setMap(naverMap);

        // 마커 크기
        marker.setWidth(70);
        marker.setHeight(100);

        // 마커 모양
        marker.setIcon(OverlayImage.fromResource(R.drawable.place_mark));
    }

    // 현재위치 권한 확인
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
                return;
            } else { // 권한 수락
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}