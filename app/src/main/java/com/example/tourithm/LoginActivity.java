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

public class LoginActivity extends AppCompatActivity {
    // 디버깅 위해 logcat 사용함. 사용방법: Log.d("태그명(prefix)", "보여질 값");
    // "태그명: 보여질 값" 형태로 Run, Logcat에 출력됨. Logcat 검색창에 태그명 검색하면 편함!

    private String user_id, user_pw;
    private boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // 액티비티 시작시 처음으로 실행되는 생명주기
        // 인텐트 사용하고자 하거나, 액티비티로서 사용하려면 OnCreate 필수로 오버라이딩 해줘야한다.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Intent loginIntent = getIntent();
        user_id = loginIntent.getStringExtra("user_id");
        user_pw = loginIntent.getStringExtra("user_pw");
        Log.d("[TAG] 로그인 디버깅", "사용자 입력값: " + user_id + " " + user_pw + "/[INTENT]");

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // 인코딩 문제때문에 한글 DB인 경우 로그인 불가
                    Log.d("[TAG] 로그인 디버깅", "DB 연결 여부 " + response); // 디버깅 - php 파일 전부 보여줌
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success"); // 연결 성공시 success = true

                    if (success) { // 로그인에 성공한 경우
                        String name = jsonObject.getString("name");
                        // String userPW = jsonObject.getString("userPW"); // 해시 값 String으로 받아올 수 없어서 주석처리.

                        Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                        Log.d("[TAG] 로그인 디버깅", "(로그인 성공) 아이디, 비밀번호 일치");

                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        intent.putExtra("user_id", user_id);

                        startActivity(intent);
                    }
                    else { // 로그인에 실패한 경우
                        Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        Log.d("[TAG] 로그인 디버깅", "(로그인 실패) 입력한 아이디 or 비밀번호 오류");
                        return;
                    }

                } catch (JSONException e) { // 데이터베이스 연결 실패한 경우
                    Log.d("[TAG] 로그인 디버깅", "(데이터베이스 연결 실패) catch exception");
                    e.printStackTrace();
                }
            }
        };
        LoginRequest loginRequest = new LoginRequest(user_id, user_pw, responseListener);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(loginRequest);

        finish(); // 현재 액티비티 (로그인 화면) 종료
    }
}
