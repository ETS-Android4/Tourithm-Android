package com.example.tourithm;

import android.Manifest;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import Adapter.MapAdapter;


public class MapActivity extends FragmentActivity implements com.naver.maps.map.OnMapReadyCallback{

    private NaverMap naverMap;
    private FusedLocationSource locationSource;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private Marker pl_marker = new Marker(); // 장소 마커
    private Marker fo_marker = new Marker(); // 음식 마커

    private InfoWindow infoWindow = new InfoWindow();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        FragmentManager fm = getSupportFragmentManager();

        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_view);

        if(mapFragment == null){
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map_view, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
        locationSource = new FusedLocationSource(this,LOCATION_PERMISSION_REQUEST_CODE);

    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        // 지도 유형
        naverMap.setMapType(NaverMap.MapType.Basic);

        /* 현재 위치 표시 _ GPS 나중에 폰으로 할 때 주석 풀기
        naverMap.setLocationSource(locationSource);
        // 권환확인
        ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);
        */

        // 위도 경도 _ 필요하면 많이 만들기
        LatLng latlng = new LatLng(35.145452, 129.036782);

        // 지도 중심 (임의)
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(latlng);
        naverMap.moveCamera(cameraUpdate);

        // 마커 _ 관광지
        // 숫자 대신 Marker.SIZE_AUTO 하면 자동으로 크기 설정됨
        pl_marker.setWidth(70);
        pl_marker.setHeight(100);
        // 마커 모양
        pl_marker.setIcon(OverlayImage.fromResource(R.drawable.place_mark));
        // 마커 위치
        pl_marker.setPosition(latlng);
        pl_marker.setMap(naverMap);
        // 지도를 기울려도 마커 모양이 그대로 유지
        pl_marker.setFlat(false);


        /*
        * https://navermaps.github.io/android-map-sdk/guide-ko/5-3.html
        * 여기 참고
        * pl_marker 이것도 바꿔야할거같기도 ㅇㅅㅇ 나중에 음식한다면..?
        */

        // 지도를 클릭하면 정보 창을 닫음
        naverMap.setOnMapClickListener((coord, point) -> {
            infoWindow.close();
        });

        // 마커를 클릭하면
        Overlay.OnClickListener listener = overlay1 -> {

            if (pl_marker.getInfoWindow() == null) {
                // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                infoWindow.open(pl_marker);

                // 정보창 띄우기
                ViewGroup rootView = (ViewGroup) findViewById(R.id.fragment_container);
                MapAdapter adapter = new MapAdapter(MapActivity.this,rootView);

                infoWindow.setAdapter(adapter);

                //인포창의 우선순위
                infoWindow.setZIndex(10);
                //투명도 조정
                infoWindow.setAlpha(0.95f);
                //인포창 표시
                infoWindow.open(pl_marker);
            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                infoWindow.close();
            }
            return true;
        };

        pl_marker.setOnClickListener(listener); // 마커 클릭시

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
