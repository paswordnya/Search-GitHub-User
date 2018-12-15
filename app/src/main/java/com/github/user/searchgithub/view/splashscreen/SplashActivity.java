package com.github.user.searchgithub.view.splashscreen;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.user.searchgithub.R;
import com.github.user.searchgithub.view.detail.DetailUserGithub;
import com.github.user.searchgithub.view.search.SearchUserActivity;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intDetail = new Intent(SplashActivity.this, SearchUserActivity.class);
                startActivity(intDetail);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}
