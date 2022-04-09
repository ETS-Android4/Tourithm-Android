package com.example.tourithm;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tourithm.databinding.MainScreenBinding;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainScreen extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private MainScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = MainScreenBinding.inflate(getLayoutInflater());
        // binding = DataBindingUtil.setContentView(this, R.layout.main_screen);
        setContentView(binding.getRoot());

        /*String loginUser;
        Intent intent = getIntent();
        loginUser = intent.getStringExtra("user_id");
        Log.d("[DEBUG] USER ID INTENT RESPONSE: ", loginUser + " 님 로그인");*/

        setSupportActionBar(binding.appBarMain.toolbar);
        /*** Floating Action Button ***/
        /*binding.toolbar.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navigationView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_item_login, R.id.nav_item_scrap, R.id.nav_item_root, R.id.nav_item_intro, R.id.nav_item_mypage)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        /*** 로그인한 경우 네비게이션 헤더에 현재 로그인중인 사용자의 닉네임을 출력함(동적) ***/
        /*if (loginUser != null) {
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
        }*/
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                Toast.makeText(this, "Back Button", Toast.LENGTH_SHORT);
                return true;
            }
            case R.id.tb_menu:{
                drawerLayout.openDrawer(Gravity.RIGHT);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navbar_btn, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}