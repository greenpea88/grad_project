package com.grad_proj.assembletickets.front.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.grad_proj.assembletickets.front.R;

public class JoinActivity extends AppCompatActivity {

    TextInputEditText joinUserEditText,joinIDEditText, joinPWEditText;
    TextView welcomeTextView;

    String inputUserName = "";
    String inputID = "";
    String inputPW = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        joinUserEditText = findViewById(R.id.joinUserEditText);
        joinIDEditText = findViewById(R.id.joinIDEditText);
        joinPWEditText = findViewById(R.id.joinPWEditText);
        welcomeTextView = findViewById(R.id.welcomeTextView);

        joinUserEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputUserName=charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        joinIDEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputID=charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        joinPWEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputPW=charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void onJoinBtnClicked(View v){
        Toast inputToast = Toast.makeText(this.getApplicationContext(),"회원 정보를 모두 입력해주세요.",Toast.LENGTH_SHORT);
        Handler timer = new Handler();

        inputToast.setGravity(Gravity.CENTER,0,0);
//        inputToast.show();
        Log.d("Text",inputID+" / "+inputPW);
        if(inputUserName.equals("") || inputID.equals("") || inputPW.equals("")){
            inputToast.show();
        }
        //TODO: 입력받은 회원정보 서버로 넘기기
        else{
            welcomeTextView.setVisibility(View.VISIBLE);

            //회면 전환에 딜레이 주기
            timer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(JoinActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            },1200);
        }
    }
}