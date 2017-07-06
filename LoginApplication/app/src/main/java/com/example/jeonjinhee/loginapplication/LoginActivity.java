package com.example.jeonjinhee.loginapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText EditTextId = (EditText) findViewById(R.id.editText_login_id);
        EditText EditTextPassword = (EditText) findViewById(R.id.editText_login_password);
        Button ButtonLogin = (Button) findViewById(R.id.button_login);
        TextView ButtonRegister = (TextView) findViewById(R.id.textView_login_join);

        ButtonRegister.setOnClickListener(new View.OnClickListener() {  //회원가입 버튼 클릭시
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);    //회원가입 페이지로 이동
                startActivity(registerIntent);
            }
        });
        ButtonLogin.setOnClickListener(new View.OnClickListener() { //로그인 버튼 클릭시
            @Override
            public void onClick(View v) {
                try {
                    String userId = EditTextId.getText().toString();
                } catch (NullPointerException e) {
                    Log.e("err", e.getMessage());
                }

                loginDB lDB = new loginDB();
                lDB.execute();

                Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);   //메인화면으로 이동
                startActivity(loginIntent);
            }
        });
    }

    public class loginDB extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... unused) {

            /* 인풋 파라메터값 생성 */
            String param = "u_id=" + userId + "";
            Log.e("POST", param);
            try {
                /* 서버연결 */
                URL url = new URL(
                        "http://웹서버주소/login.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

/* 서버 -> 안드로이드 파라메터값 전달 */
                InputStream is = null;
                BufferedReader in = null;
                String data = "";

                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();

/* 서버에서 응답 */
                Log.e("RECV DATA", data);

                if (data.equals("0")) {
                    Log.e("RESULT", "성공적으로 처리되었습니다!");
                } else {
                    Log.e("RESULT", "에러 발생! ERRCODE = " + data);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
