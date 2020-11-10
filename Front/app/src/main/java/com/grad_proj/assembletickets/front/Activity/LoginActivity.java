package com.grad_proj.assembletickets.front.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.grad_proj.assembletickets.front.Database.DatabaseOpen;
import com.grad_proj.assembletickets.front.R;
import com.grad_proj.assembletickets.front.UserSharedPreference;

//TODO: 비밀번호 잊엇는지에 대한 문구를 언제 띄울 것인가?

public class LoginActivity extends AppCompatActivity {

    TextInputEditText loginIDEditText, loginPWEditText;
    TextView lostPW;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    String inputID = "";
    String inputPW = "";

    private DatabaseOpen databaseOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginIDEditText = findViewById(R.id.loginIDEditText);
        loginPWEditText = findViewById(R.id.loginPWEditText);
        lostPW = findViewById(R.id.lostPW);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

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

        Toast inputToast = Toast.makeText(this.getApplicationContext(),"Email, Password를 모두 입력해주세요", Toast.LENGTH_SHORT);

        inputToast.setGravity(Gravity.CENTER,0,0);
//        inputToast.show();
        Log.d("Text",inputID+" / "+inputPW);
        if("".equals(inputID) || "".equals(inputPW)){
            inputToast.show();
        }
        //TODO: 로그인 토큰 받도록 서버 설정해서 받도록 하기
        else{
            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);

            getCalendarData();

            startActivity(intent);
            UserSharedPreference.setIdToken(LoginActivity.this, "testid" + inputID);
            this.finish();
        }
    }

    public void onGoogleLoginClicked(View v){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // 로그인 인텐트가 꺼진 이후 로그인 결과 처리
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//            UserSharedPreference.setIdToken(LoginActivity.this, account.getIdToken());
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            Toast.makeText(this, account.getDisplayName()+"("+account.getEmail()+")님, 안녕하세요!", Toast.LENGTH_LONG).show();
            this.finish();
        } catch (ApiException e) {
            Log.d("Login", "Sign In Result: Failed Code = "+e.getStatusCode());
            Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    public void onNotJoinYetClicked(View v){
        //값을 주고 받는데 사용하는 규약(현재액티비티.this, 목적지액티비티.class)
        Intent intent = new Intent(LoginActivity.this, JoinActivity.class);

        startActivity(intent);
    }

    public void getCalendarData(){
        //서버로부터 캘린더 데이터 가져오기 + db 생성하고 집어넣기
        databaseOpen = new DatabaseOpen(this);
        databaseOpen.open();
        databaseOpen.create();

        databaseOpen.insertColumn("2020-10-21","test1","test",1,1);
        databaseOpen.insertColumn("2020-10-22","test2","dafsf",1,2);
        databaseOpen.insertColumn("2020-10-21","test3",null,2,2);
        databaseOpen.insertColumn("2020-10-22","test4",null,4,3);
        databaseOpen.insertColumn("2020-10-21","test5","tttt",2,1);

        databaseOpen.close();
    }

}