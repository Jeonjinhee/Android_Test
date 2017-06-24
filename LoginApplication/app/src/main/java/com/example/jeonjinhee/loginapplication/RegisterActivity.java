package com.example.jeonjinhee.loginapplication;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ArrayList<String> addressList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        addressList = new ArrayList<String>();  //거주관 목록
        addressList.add("예솔관");
        addressList.add("소울관");
        addressList.add("다솔관");
        addressList.add("은솔관");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, addressList);
        //스피너 속성
        final Spinner sp = (Spinner) this.findViewById(R.id.addressSpinner);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(this);

        final EditText idText = (EditText) findViewById(R.id.idText);   //아이디 입력
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);   //비밀번호 입력
        final Button registerButton = (Button) findViewById(R.id.registerButton);   //거주관 선택
        final String isAdmin = "";   //초기 가입자는 관리자가 아님

        //회원 가입 버튼 클릭 이벤트
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = idText.getText().toString();
                String userPassword = passwordText.getText().toString();
                String UserAddress = sp.getSelectedItem().toString();


                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();
                        JSONObject jsonResponse = null;

                        try {
                            jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            //회원가입 성공했을 경우
                            if(success){
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("회원가입 성공").setPositiveButton("확인", null).create().show();

                                Intent registerIntent = new Intent(getApplicationContext(), LoginActivity.class);    //로그인인화면으로 이동
                                startActivity(registerIntent);
                            }
                            //회원가입 실패했을 경우우
                           else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("회원가입 실패").setNegativeButton("확인", null).create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //회원가입 신청
                RegisterRequest registerRequest = new RegisterRequest(userId, userPassword, UserAddress, isAdmin, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(this, addressList.get(position), Toast.LENGTH_LONG).show(); //해당 항목 눌렸을때
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


}
