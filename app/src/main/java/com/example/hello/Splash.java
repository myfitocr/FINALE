package com.example.hello;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 1300);

    }

    private class splashhandler implements Runnable {
        public void run() {
            startActivity(new Intent(getApplication(), Tutorial1.class)); //로딩이 끝난 후, ChoiceFunction 이동
            Splash.this.finish();
        }
    }

    @Override
    public void onBackPressed() {

    }

}
