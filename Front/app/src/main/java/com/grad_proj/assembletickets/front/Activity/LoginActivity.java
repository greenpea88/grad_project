package com.grad_proj.assembletickets.front.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.grad_proj.assembletickets.front.R;

//TODO: 비밀번호 잊엇는지에 대한 문구를 언제 띄울 것인가?
//TODO: 키보드 올라가면 가리는만큼 올리기

public class LoginActivity extends AppCompatActivity {

    TextInputEditText loginIDEditText, loginPWEditText;
    TextView lostPW;

    String inputID = "";
    String inputPW = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginIDEditText = findViewById(R.id.loginIDEditText);
        loginPWEditText = findViewById(R.id.loginPWEditText);
        lostPW = findViewById(R.id.lostPW);

        loginIDEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence!=null){
                    inputID=charSequence.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        loginPWEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence!=null){
                    inputPW=charSequence.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void onLoginBtnClicked(View v){

        Toast inputToast = Toast.makeText(this.getApplicationContext(),"Email,Password를 모두 입력해주세요",Toast.LENGTH_SHORT);

        inputToast.setGravity(Gravity.CENTER,0,0);
//        inputToast.show();
        Log.d("Text",inputID+" / "+inputPW);
        if(inputID.equals("") || inputPW.equals("")){
            inputToast.show();
        }
        //TODO: 로그인 토큰 받도록 서버 설정해서 받도록 하기
        else{
            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);

            startActivity(intent);
        }
    }

    public void onNotJoinYetClicked(View v){
        //값을 주고 받는데 사용하는 규약(현재액티비티.this, 목적지액티비티.class)
        Intent intent = new Intent(LoginActivity.this, JoinActivity.class);

        startActivity(intent);
    }
}