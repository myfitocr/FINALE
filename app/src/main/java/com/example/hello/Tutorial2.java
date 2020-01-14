package com.example.hello;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Tutorial2 extends AppCompatActivity {
    TextView textView;
    TextView textView1;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tutorial2);
        textView = findViewById(R.id.tutorial3);
        textView1 = findViewById(R.id.tutorial4);
        button = findViewById(R.id.button2);

        SharedPreferences sharedPreferences = getSharedPreferences("start",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.putInt("start",0);
        //editor.apply();
        //editor.commit();


        if(sharedPreferences.getInt("start2",0)==0){
            editor.putInt("start2",1);
            editor.apply();
            Log.v("key","key"+sharedPreferences.getInt("start2",0));

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
            Log.v("key","key"+sharedPreferences.getInt("start2",0));
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }

        });

    }
}
