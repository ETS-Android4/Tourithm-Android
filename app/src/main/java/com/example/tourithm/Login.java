package com.example.tourithm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class Login extends AppCompatActivity {
    EditText user_id;

    private long backKeyPressedTime = 0;  // 뒤로가기 버튼을 누른 시간 저장
    private Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // JOIN(회원가입)
        Button joinButton = (Button) findViewById(R.id.lo_join_bt);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getApplicationContext(), Join.class);
                //startActivity(intent);
            }
        });

        // 사용자가 입력한 값 받아옴.
        Button loginButton = (Button) findViewById(R.id.lo_login_bt); // LOGIN(로그인)
        user_id = findViewById(R.id.lo_user_id); // ID
        EditText user_pw = findViewById(R.id.lo_user_pw); // PW

        // 로그인 버튼 누를 시 LoginActivity.java에 값 전달 -> 로그인 성공시 메인 화면으로 이동
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사용자가 EditText(아이디, 비밀번호)에 입력한 값을 LoginActivity.java로 전달
                // 전달할 때 일반적인 방법X 안드로이드에서는 무조건 인텐트를 사용해서 값을 넘긴다. 값 여러 개 한 번에 가능.
                // 값 넣어서 전달할 때는 xxxExtra 사용함 (보내기 put, 받기 get)
                // LoginActivity.java에서는 인텐트에 담긴 user_id, user_pw 값 받아서 로그인여부 확인
                Intent loginintent = new Intent(getApplicationContext(), LoginActivity.class);
                loginintent.putExtra("user_id", user_id.getText().toString()); //값 넘길 때 String으로 변환하여 넘겨주자
                loginintent.putExtra("user_pw", user_pw.getText().toString());
                startActivity(loginintent); // 필수. 안넣으면 인텐트 안보내짐!
            }
        });
    }

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
