package com.example.jeonjinhee.loginapplication;

/**
 * Created by JeonJinhee on 2017-06-25.
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.android.volley.toolbox.StringRequest;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    private static String TAG = "phptext_MainActivity";

    private EditText mEditTextId;   //학번/사번
    private EditText mEditTextPassword; //비밀번호
    private EditText mEditTextAddress;  //거주관
    private TextView mTextViewResult;   //결과메시지

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEditTextId = (EditText)findViewById(R.id.editText_register_id);    //id 값을 받아옴
        mEditTextPassword = (EditText)findViewById(R.id.editText_register_password);    //비밀번호 값을 받아옴
        mEditTextAddress =  (EditText)findViewById(R.id.editText_register_address); //거주관 값을 받아옴
        mTextViewResult = (TextView)findViewById(R.id.textView_register_result);

        Button buttonInsert = (Button)findViewById(R.id.button_register_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {    //버튼 클릭시시
           @Override
            public void onClick(View v) {

                String userId = mEditTextId.getText().toString();
                String userPassword = mEditTextPassword.getText().toString();
                String userAddress = mEditTextAddress.getText().toString();
                String result = mTextViewResult.getText().toString();

                InsertData task = new InsertData(); //데이터 삽입
                task.execute(userId, userPassword, userAddress);    //실행
            }
        });
    }


    class InsertData extends AsyncTask<String, Void, String>{   //별도의 스레드로 동작
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();   //Background 작업이 끝난 후 UI 작업을 진행

            progressDialog = ProgressDialog.show(RegisterActivity.this, //로딩중 표시
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();   //로딩 종료
            mTextViewResult.setText(result);    //결과 표시

            if(result.equals("SQL문 처리 성공")){    //가입이 성공하면
                Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();   //가입 성공 메시지 출력
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);   //메인 페이지로 이동
                startActivity(mainIntent);
            }
            else{
                Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();   //가입 실패 메시지 출력

                mEditTextId.setText("");
                mEditTextPassword.setText("");
                mEditTextAddress.setText("");
            }
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String userId = (String)params[0];
            String userPassword = (String)params[1];
            String userAddress = (String)params[2];

            String serverURL = "http://192.168.1.167/insert.php";   //아이피 주소 연결
            String postParameters = "userId=" + userId + "&userPassword=" + userPassword + "&userAddress=" + userAddress;   //파라미터 전달

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST"); //post방식으로 전송
                //httpURLConnection.setRequestProperty("content-type", "application/json");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();    //연결


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString();
            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                return new String("Error: " + e.getMessage());
            }
        }
    }
    public class LoginRequest extends StringRequest
}