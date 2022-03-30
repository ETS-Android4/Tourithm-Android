package com.example.tourithm;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class Home extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private View drawerView;

    private long backKeyPressedTime = 0;  // 뒤로가기 버튼을 누른 시간 저장
    private Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        String loginUser;
        Intent intent = getIntent();
        loginUser = intent.getStringExtra("user_id");
        Log.d("[DEBUG] USER ID INTENT RESPONSE: ", loginUser + " 님");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // 상단 ToolBar 설정 (API 25 이후 AppBar 사용 X -> ToolBar로 대체)
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar)); // 액션바를 툴바로 바꿔줌 (해당 액티비티에서 툴바를 사용하겠다는 의미)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 활성화
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 앱 기본 타이틀(Tourithm) 없애기
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#cae3ff")));

        // Navigation Bar 아이템 동적 추가
        LinearLayout ll_navigation_container = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.navbar_head, null);
        // Navigation Drawer 헤더 설정
        ll_navigation_container.setBackground(new ColorDrawable(Color.parseColor("#999999")));
        ll_navigation_container.setPadding(20, 150, 40, 50);
        ll_navigation_container.setOrientation(LinearLayout.VERTICAL);
        ll_navigation_container.setGravity(Gravity.BOTTOM);
        ll_navigation_container.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        // TEXTVIEW: UserName 님,
        final TextView tv_username = new TextView(this);
        tv_username.setTextColor(getResources().getColor(R.color.black));
        tv_username.setTextSize(20);
        tv_username.setTypeface(null, Typeface.BOLD);
        tv_username.setPadding(0, 2, 0, 2);
        param1.setMargins(20, 20, 20, 5);
        tv_username.setLayoutParams(param1);

        // TEXTVIEW: 안녕하세요!
        final TextView tv_userhi = new TextView(this);
        tv_userhi.setTextColor(getResources().getColor(R.color.black));
        tv_userhi.setTextSize(18);
        tv_userhi.setPadding(0, 2, 0, 2);
        param2.setMargins(20, 0, 20, 10);
        tv_userhi.setLayoutParams(param2);

        // TEXTVIEW: 로그인(No Login) or 로그아웃(Login)
        final TextView tv_login_or_out = new TextView(this);
        tv_login_or_out.setTextColor(getResources().getColor(R.color.black));
        tv_login_or_out.setTextSize(15);
        tv_login_or_out.setPadding(0, 2, 0, 2);
        param3.setMargins(20, 30, 20, 20);
        tv_login_or_out.setGravity(Gravity.RIGHT);
        tv_login_or_out.setLayoutParams(param3);

        /*** 로그인한 경우 네비게이션 헤더에 현재 로그인중인 사용자의 닉네임을 출력함(동적) ***/
        if (loginUser != null) {
            // 유저 아이디 혹은 이름(표시할 정보) String으로 받아옴 -> 추후 유저 이름으로 수정
            tv_username.setText(loginUser + " 님,");
            tv_userhi.setText("안녕하세요!");

            tv_login_or_out.setText("로그아웃");
            tv_login_or_out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent logoutIntent = new Intent(getApplicationContext(), Login.class);
                    startActivity(logoutIntent);
                }
            });
        } else {
            tv_username.setText("로그인이 필요합니다.");
            tv_userhi.setVisibility(View.GONE);

            tv_login_or_out.setText("로그인");
            tv_login_or_out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent loginIntent = new Intent(getApplicationContext(), Login.class);
                    startActivity(loginIntent);
                }
            });
        }

        ll_navigation_container.addView(tv_username);
        ll_navigation_container.addView(tv_userhi);
        ll_navigation_container.addView(tv_login_or_out);

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.addHeaderView(ll_navigation_container); // 네비게이션 메뉴에서 메뉴들이 눌렸을 때의 행동들

        // Navigation View에서 아이템을 클릭한 경우 (OnClick과 동일)
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                int id = menuItem.getItemId();

                if(id == R.id.item_login) { // Side Bar - 로그인 버튼 클릭
                    Intent loginIntent = new Intent(getApplicationContext(), Login.class);
                    startActivity(loginIntent);
                }

                /*else if (id == R.id.***) {
                    Intent *** = new Intent(getApplicationContext(), ***.class);
                    startActivity(***);
                }*/

                return true;
            }
        });


        /*drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
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
                startActivity(loginintent);
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.navbar_btn, menu);
        return true;
    }

    // 메인 화면에서 메뉴 버튼을 클릭 (Navigation Drawer Open)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tb_menu : {
                if (!drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /*DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
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
    };*/

    @Override
    public void onBackPressed() {  // 뒤로가기 버튼 클릭한 경우
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
        } // Navigation Drawer가 열려있다면 종료

        // 메인 화면에서 뒤로가기 버튼 2회 누르면 앱 종료
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
