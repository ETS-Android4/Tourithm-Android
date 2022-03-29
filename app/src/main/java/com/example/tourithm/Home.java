package com.example.tourithm;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class Home extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private View drawerView;

    private long backKeyPressedTime = 0;  // 뒤로가기 버튼을 누른 시간 저장
    private Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerView = (View)findViewById(R.id.drawer2);

        //drawerLayout.setDrawerListener(listener);
        drawerLayout.addDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        ImageButton buttonMenu = (ImageButton) findViewById(R.id.iv_hr_menuicon); // NavBar(Side Menu) Open
        buttonMenu.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
            }
        });

        Button buttonClose = (Button) findViewById(R.id.dw_btn_close); // NavBar(Side Menu) Close
        buttonClose.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });

        Button buttonLogin = (Button) findViewById(R.id.nav_login); // Press Login Button
        buttonLogin.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginintent = new Intent(getApplicationContext(), Login.class);
                startActivity(loginintent); // 필수. 안넣으면 인텐트 안보내짐!
            }
        });
    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            // 슬라이드 했을때
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
            // Drawer가 오픈된 상황일때 호출
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
            // 닫힌 상황일 때 호출
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            // 특정상태가 변결될 때 호출
        }
    };

    @Override
    public void onBackPressed() {  // 메인 화면에서 뒤로가기 버튼 2회 누르면 앱 종료
        // 2000 milliseconds = 2 seconds
        // 가장 최근에 뒤로가기 버튼을 누른 시간에 2초를 더해 현재시간과 비교 후, 2초가 지났으면 메시지 출력
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로가기 버튼을 다시 누르면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // 2초가 지나지 않은 시점에서 뒤로가기 다시 클릭시 앱 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            toast.cancel();  // 알림 팝업(Toast) 표시 해제
            ActivityCompat.finishAffinity(this);
            System.exit(0);  // 시스템 종료
        }
    }
}
