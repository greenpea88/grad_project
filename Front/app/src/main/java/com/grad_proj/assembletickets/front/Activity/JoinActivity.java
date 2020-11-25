package com.grad_proj.assembletickets.front.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.grad_proj.assembletickets.front.R;
import com.grad_proj.assembletickets.front.Show;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JoinActivity extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

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

    public void onGoogleJoinClicked(View v){
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
            // TODO: 서버에 유저 정보 없으면

            Intent intent = new Intent(JoinActivity.this, HomeActivity.class);
            // 자동 로그인 토큰
            intent.putExtra("id", account.getId());
            intent.putExtra("email", account.getEmail());
            intent.putExtra("username", account.getDisplayName());
            this.finish();
            startActivity(intent);
            // TODO: 서버에 유저 정보 있으면
            // Toast.makeText(this, "이미 존재하는 이메일 입니다.", Toast.LENGTH_LONG).show();
            // mGoogleSignInClient.signOut();
        } catch (ApiException e) {
            Log.d("Login", "Sign In Result: Failed Code = "+e.getStatusCode());
            Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private class GetUserInfo extends AsyncTask<String, Void , Void> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected Void doInBackground(String... strings) {
            List<Show> loadedShows = new ArrayList<>();

            String strUrl = HttpUrl.parse(strings[0]).newBuilder()
                    .addQueryParameter("page",Integer.toString(page))
                    .addQueryParameter("time",now)
                    .build().toString();

            try {
                Request request = new Request.Builder()
                        .url(strUrl)
                        .get()
                        .build();

                Response response = client.newCall(request).execute();

                Gson gson = new Gson();

                Type listType = new TypeToken<ArrayList<Show>>() {}.getType();
                loadedShows = gson.fromJson(response.body().string(), listType);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return loadedShows;
        }
}