package com.grad_proj.assembletickets.front.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.grad_proj.assembletickets.front.R;
import com.grad_proj.assembletickets.front.UserSharedPreference;

public class AutoLoginActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_login);

        if(UserSharedPreference.getIdToken(AutoLoginActivity.this).length() == 0) {
            // call Login Activity
            Log.d("text", "getnodata");
            intent = new Intent(AutoLoginActivity.this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        } else {
            // Call Next Activity
            intent = new Intent(AutoLoginActivity.this, HomeActivity.class);
            intent.putExtra("ID_TOKEN", UserSharedPreference.getIdToken(this).toString());
            startActivity(intent);
            this.finish();
        }
    }

}