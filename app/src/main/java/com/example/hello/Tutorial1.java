package com.example.hello;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Tutorial1 extends AppCompatActivity {
    TextView textView;
    TextView textView1;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial1);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        textView = findViewById(R.id.tutorial1);
        textView1 = findViewById(R.id.tutorial2);
        button = findViewById(R.id.button);

        SharedPreferences sharedPreferences = getSharedPreferences("start",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.putInt("start",0);
        //editor.apply();
        //editor.commit();
        Log.v("key","key"+sharedPreferences.getInt("start",0));

        if(sharedPreferences.getInt("start",0)==0){
            editor.putInt("start",1);
            editor.apply();
            Log.v("key","key"+sharedPreferences.getInt("start",0));

            final AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
            final AlphaAnimation fadeIn1 = new AlphaAnimation(0.0f, 1.0f);
            final AlphaAnimation fadeIn2 = new AlphaAnimation(0.0f, 1.0f);

            fadeIn1.setDuration(1200);
            fadeIn.setDuration(1200);
            fadeIn2.setDuration(1200);



            textView.startAnimation(fadeIn);
            textView1.startAnimation(fadeIn1);
            button.startAnimation(fadeIn2);
            fadeIn1.setStartOffset(1200+fadeIn.getStartOffset());
            fadeIn2.setStartOffset(1200+fadeIn1.getStartOffset());

            editor.commit();
        }
        else {
            Log.v("key","key"+sharedPreferences.getInt("start",0));
            Intent intent = new Intent(this,Tutorial2.class);
            startActivity(intent);
            finish();
        }

        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Tutorial2.class);
                startActivity(intent);
                finish();
            }

        });

    }

}
