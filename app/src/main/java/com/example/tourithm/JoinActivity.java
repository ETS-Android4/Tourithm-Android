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

public class JoinActivity extends AppCompatActivity {
    // 디버깅 위해 logcat 사용함. 사용방법: Log.d("태그명(prefix)", "보여질 값");
    // "태그명: 보여질 값" 형태로 Run, Logcat에 출력됨. Logcat 검색창에 태그명 검색하면 편함!

    private String id, pw, name, phone, profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // 액티비티 시작시 처음으로 실행되는 생명주기
        // 인텐트 사용하고자 하거나, 액티비티로서 사용하려면 OnCreate 필수로 오버라이딩 해줘야한다.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        Intent joinIntent = getIntent();
        id = joinIntent.getStringExtra("regi_id");
        pw = joinIntent.getStringExtra("regi_pw");
        name = joinIntent.getStringExtra("regi_name");
        phone = joinIntent.getStringExtra("regi_phonenum");
        profile = joinIntent.getStringExtra("profile");

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // 인코딩 문제때문에 한글 DB인 경우 로그인 불가
                    Log.d("[TAG] 회원가입 디버깅", "DB 연결 여부 " + response); // 디버깅 - php 파일 전부 보여줌
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success"); // 회원가입 성공시 success = true

                    if (success) { // 회원가입에 성공한 경우
                        //String userID = jsonObject.getString("userID");
                        //String userPW = jsonObject.getString("userPW");

                        Toast.makeText(getApplicationContext(), "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                        Log.d("[TAG] 회원가입 디버깅", "(회원가입 성공) 중복 아이디 없음");
                    }

                    else { // 회원가입에 실패한 경우
                        Toast.makeText(getApplicationContext(), "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        Log.d("[TAG] 회원가입 디버깅", "(회원가입 실패) 중복된 아이디 입력");
                        return;
                    }

                } catch (JSONException e) { // 데이터베이스 연결 실패한 경우
                    Log.d("[TAG] 회원가입 디버깅", "(데이터베이스 연결 실패) catch exception");
                    e.printStackTrace();
                }
            }
        };
        JoinRequest joinRequest = new JoinRequest(id, pw, name, phone, profile, responseListener);
        RequestQueue queue = Volley.newRequestQueue(JoinActivity.this);
        queue.add(joinRequest);

        finish(); // 현재 액티비티 (회원가입 화면) 종료
    }
}
