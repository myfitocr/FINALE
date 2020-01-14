package com.example.hello;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;

import java.util.ArrayList;

public class MyPantsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    ArrayList<SizeClass> sizeClasses=new ArrayList<>();

    ArrayList<String> items = new ArrayList<>();
    ArrayList<CheckBox> checkBoxes = new ArrayList<>();

    SizeClass myFit = new SizeClass("myFit"); //사용자 사이즈 정보
    ArrayList<SizeClass> checkedSize = new ArrayList<>();

    RecyclerView recyclerView;
    ReSizeAdapter reSizeAdapter;

    ArrayList<SizeClass> unchangedSize = new ArrayList<>();
    ArrayList<SizeClass> unchangedSizeBase = new ArrayList<>();

    SizeClass test;



    private SlidrInterface slidr;
    private SlidrConfig config= new SlidrConfig.Builder()
            .position(SlidrPosition.LEFT)
            .build();

    private SDOpenHelper sdOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_my_pants);
        final SharedPreferences sharedPreferences = this.getSharedPreferences("PersonDB", Context.MODE_PRIVATE);
        Log.v("test_2",(sharedPreferences.getString("totalLength","")));


        slidr= Slidr.attach(this,config);
        slidr.unlock();

        //reSaveAdapter에서 intent 받아옴
        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        sizeClasses=(ArrayList<SizeClass>) bundle.get("sizeClasses");

        unchangedSizeBase=sizeClasses;

        for (int j = 0; j < sizeClasses.size(); j++) {
            items.add(sizeClasses.get(j).getSizeName());
        }

        CheckBox save_cb0 = findViewById(R.id.save_cb0);
        CheckBox save_cb1 = findViewById(R.id.save_cb1);
        CheckBox save_cb2 = findViewById(R.id.save_cb2);
        CheckBox save_cb3 = findViewById(R.id.save_cb3);
        CheckBox save_cb4 = findViewById(R.id.save_cb4);
        CheckBox save_cb5 = findViewById(R.id.save_cb5);
        CheckBox save_cb6 = findViewById(R.id.save_cb6);
        CheckBox save_cb7 = findViewById(R.id.save_cb7);
        CheckBox save_cb8 = findViewById(R.id.save_cb8);
        CheckBox save_cb9 = findViewById(R.id.save_cb9);

        checkBoxes.add(save_cb0);
        checkBoxes.add(save_cb1);
        checkBoxes.add(save_cb2);
        checkBoxes.add(save_cb3);
        checkBoxes.add(save_cb4);
        checkBoxes.add(save_cb5);
        checkBoxes.add(save_cb6);
        checkBoxes.add(save_cb7);
        checkBoxes.add(save_cb8);
        checkBoxes.add(save_cb9);

        for (int k = 0; k < 10; k++) {
            checkBoxes.get(k).setOnCheckedChangeListener(this);
            checkBoxes.get(k).setVisibility(View.INVISIBLE);
        }

        for (int l = 0; l < items.size(); l++) {
            checkBoxes.get(l).setVisibility(View.VISIBLE);
            checkBoxes.get(l).setText(items.get(l));
        }

        /**사용자 정보 sizeClass 에서 sizeInfo 받아오기**/
        ArrayList<Float> Info2 = new ArrayList<>();
        Info2.add(Float.parseFloat(sharedPreferences.getString("totalLength","")));
        Info2.add(Float.parseFloat(sharedPreferences.getString("waist","")));
        Info2.add(Float.parseFloat(sharedPreferences.getString("thigh","")));
        Info2.add(Float.parseFloat(sharedPreferences.getString("rise","")));
        Info2.add(Float.parseFloat(sharedPreferences.getString("hemcross","")));

        myFit.setSizeInfo(Info2);

        checkedSize.add(myFit);

        //recycler adapter
        /**column 너비 조정 필요**/
        recyclerView=findViewById(R.id.recycler_saveSize);
        reSizeAdapter=new ReSizeAdapter(getApplicationContext(),checkedSize);
        recyclerView.setAdapter(reSizeAdapter);

        //layout manager
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        LinearLayout linearLayout=findViewById(R.id.save_pantsImage);
        ViewEx viewEx = new ViewEx(getApplicationContext(),checkedSize);
        linearLayout.addView(viewEx);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        unchangedSize = unchangedSizeBase;
        checkedSize.clear();
        checkedSize.add(myFit);

        //체크박스를 클릭해서 상태가 바뀌었을 경우 호출되는 callback method
        for (int m = 0; m < checkBoxes.size(); m++) {
            if (checkBoxes.get(m).isChecked()) {
                checkedSize.add(unchangedSize.get(m));
            }
        }

        recyclerView=findViewById(R.id.recycler_saveSize);
        reSizeAdapter=new ReSizeAdapter(getApplicationContext(),checkedSize);
        recyclerView.setAdapter(reSizeAdapter);

        //layout manager
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        LinearLayout linearLayout=findViewById(R.id.save_pantsImage);
        linearLayout.removeAllViews();
        ViewEx viewEx = new ViewEx(getApplicationContext(),checkedSize);
        linearLayout.addView(viewEx);
    }
}
