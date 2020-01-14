package com.example.hello;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class PantsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    TessBaseAPI tess;
    String dataPath = "";
    ArrayList<SizeClass> sizeClasses=new ArrayList<>();
    SizeClass sizeClass;

    Button createbtn;
    Button createbn1;
    private static final int DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE = 1222;

    ArrayList<String> items = new ArrayList<>();
    ArrayList<CheckBox> checkBoxes = new ArrayList<>();

    SizeClass myFit = new SizeClass("myFit"); //사용자 사이즈 정보
    RecyclerView recyclerView;
    ReSizeAdapter reSizeAdapter;

    ArrayList<SizeClass> unchangedSize = new ArrayList<>();
    ArrayList<SizeClass> checkedSize = new ArrayList<>();
    ArrayList<SizeClass> unchangedSizeBase = new ArrayList<>();

    Bitmap b;

    public static final BitmapFactory.Options options = new BitmapFactory.Options();

    private SDOpenHelper sdOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pants);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final SharedPreferences sharedPreferences = this.getSharedPreferences("PersonDB", Context.MODE_PRIVATE);
        Log.v("test_2",(sharedPreferences.getString("totalLength","")));
        dataPath = getFilesDir() + "/tesseract/";
        checkFile(new File(dataPath+"tessdata/"),"kor");
        checkFile(new File(dataPath+"tessdata/"),"eng");

        String lang = "kor+eng";
        tess = new TessBaseAPI();
        tess.init(dataPath,lang);

        Uri uri_1 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media._ID};
        ContentResolver res = getApplicationContext().getContentResolver();
        Cursor imageCursor = getContentResolver().query(uri_1, projection, null, null, null);
        if(imageCursor.moveToLast()){
            do {
                Uri photoUri = Uri.withAppendedPath(uri_1, imageCursor.getString(0));
                //photoUri = MediaStore.setRequireOriginal(photoUri);
                if (photoUri != null) {
                    ParcelFileDescriptor fd = null;
                    try {
                        fd = res.openFileDescriptor(photoUri, "r");

                        //크기를 얻어오기 위한옵션 ,
                        //inJustDecodeBounds값이 true로 설정되면 decoder가 bitmap object에 대해 메모리를 할당하지 않고, 따라서 bitmap을 반환하지도 않는다.
                        // 다만 options fields는 값이 채워지기 때문에 Load 하려는 이미지의 크기를 포함한 정보들을 얻어올 수 있다.

                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFileDescriptor(
                                fd.getFileDescriptor(), null, options);
                        int scale = 0;
                        options.inJustDecodeBounds = false;
                        options.inSampleSize = scale;

                        b = BitmapFactory.decodeFileDescriptor(
                                fd.getFileDescriptor(), null, options);

                        if (b != null) {
                            // finally rescale to exactly the size we need
                        }

                    } catch (FileNotFoundException e) {
                    } finally {
                        try {
                            if (fd != null)
                                fd.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }while (imageCursor.moveToNext());
        }

        b = ARGBBitmap(b);
        sizeClasses=processImage(b);
        unchangedSizeBase=sizeClasses;

        for (int j = 0; j < sizeClasses.size(); j++) {
            items.add(sizeClasses.get(j).getSizeName());
        }

        CheckBox cb0 = findViewById(R.id.cb0);
        CheckBox cb1 = findViewById(R.id.cb1);
        CheckBox cb2 = findViewById(R.id.cb2);
        CheckBox cb3 = findViewById(R.id.cb3);
        CheckBox cb4 = findViewById(R.id.cb4);
        CheckBox cb5 = findViewById(R.id.cb5);

        checkBoxes.add(cb0);
        checkBoxes.add(cb1);
        checkBoxes.add(cb2);
        checkBoxes.add(cb3);
        checkBoxes.add(cb4);
        checkBoxes.add(cb5);

        for (int k = 0; k < 6; k++) {
            checkBoxes.get(k).setOnCheckedChangeListener(this);
            checkBoxes.get(k).setVisibility(View.INVISIBLE);
        }

        for (int l = 0; l < items.size(); l++) {
            checkBoxes.get(l).setVisibility(View.VISIBLE);
            checkBoxes.get(l).setText(items.get(l));
        }

        ArrayList<Float> Info = new ArrayList<>();
        System.out.println(sharedPreferences.getString("totalLength",""));
        Info.add(Float.parseFloat(sharedPreferences.getString("totalLength","")));
        Info.add(Float.parseFloat(sharedPreferences.getString("waist","")));
        Info.add(Float.parseFloat(sharedPreferences.getString("thigh","")));
        Info.add(Float.parseFloat(sharedPreferences.getString("rise","")));
        Info.add(Float.parseFloat(sharedPreferences.getString("hemcross","")));
        myFit.setSizeInfo(Info);

        checkedSize.add(myFit);

        //recycler adapter
        recyclerView=findViewById(R.id.recycler_size);
        reSizeAdapter=new ReSizeAdapter(getApplicationContext(),checkedSize);
        recyclerView.setAdapter(reSizeAdapter);

        //layout manager
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        LinearLayout linearLayout=findViewById(R.id.pantsImage);
        ViewEx viewEx = new ViewEx(getApplicationContext(),checkedSize);
        linearLayout.addView(viewEx);


        createbtn = findViewById(R.id.create2);
        createbn1 = findViewById(R.id.create3);

        sdOpenHelper=new SDOpenHelper(this);
        sdOpenHelper.open();
        sdOpenHelper.create();

        createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sdOpenHelper.addStyle(unchangedSizeBase);
                createFloatingWidget1(v);
            }
        });

        createbn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFloatingWidget2(v);
            }
        });

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

        recyclerView=findViewById(R.id.recycler_size);
        reSizeAdapter=new ReSizeAdapter(getApplicationContext(),checkedSize);
        recyclerView.setAdapter(reSizeAdapter);

        //layout manager
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        LinearLayout linearLayout=findViewById(R.id.pantsImage);
        linearLayout.removeAllViews();
        ViewEx viewEx = new ViewEx(getApplicationContext(),checkedSize);
        linearLayout.addView(viewEx);
    }

    public ArrayList<SizeClass> processImage(Bitmap bitmap){
        String result = null;
        tess.setImage(bitmap);
        result = tess.getUTF8Text();

        String target="보세요\n";
        if(!result.contains(target)){
            target="MY\n";
        }

        int target_num=result.indexOf(target);
        String size;
        size= result.substring(target_num+4);

        sizeClasses=getSizeInfo(size);

        return sizeClasses;
    }

    /**추가적인 끝조건 위해서 필요하면 사용**/
    private char getEndOfSize(String size){
        for(char c:size.toCharArray()){
            if (c>=32 && c<=126 || c==10){
            }else return c;
        }
        return 0;
    }

    private ArrayList<SizeClass> getSizeInfo(String getSize){
        String[] array=getSize.split("\n");
        String[] getType=array[0].split(" ");

        for(int i=0;i<array.length;i++){
            String[] getSizeInfo=array[i].split(" ");
            sizeClass=new SizeClass(getSizeInfo[0]);

            ArrayList<Float> info=new ArrayList<>();
            if (getSizeInfo.length == 6) {
                for(int j=1;j<getSizeInfo.length;j++){
                    Float size=Float.parseFloat(getSizeInfo[j]);

                    if(size>110.0) size=size/10;
                    info.add(size);
                }
                sizeClass.setSizeInfo(info);
                sizeClasses.add(sizeClass);
            } else break;
        }
        return sizeClasses;
    }

    private void checkFile(File dir,String lang){
        if(!dir.exists()&&dir.mkdirs()){
            copyFiles(lang);
        }
        if(dir.exists()){
            String datafilePath = dataPath + "/tessdata/"+lang + ".traineddata";
            File datafile= new File(datafilePath);
            if(!datafile.exists()){
                copyFiles(lang);
            }
        }
    }

    private void copyFiles(String lang) {
        try {
            String filepath = dataPath + "/tessdata/" + lang + ".traineddata";
            AssetManager assetManager = getAssets();
            InputStream instream = assetManager.open("tessdata/"+lang+".traineddata");
            OutputStream outstream = new FileOutputStream(filepath);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap ARGBBitmap(Bitmap img) {
        return img.copy(Bitmap.Config.ARGB_8888,true);
    }

    /*  start floating widget service  */

    public void createFloatingWidget1(View view) {
        //Check if the application has draw over other apps permission or not?
        //This permission is by default available for API<23. But for API > 23
        //you have to ask for the permission in runtime.
        //getcontext 부분 수정함.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            //If the draw over permission is not available open the settings screen
            //to grant the permission. 권한을 받기 위한 부분
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE);
        } else
            //If permission is granted start floating widget service
            startFloatingWidgetService1();

    }

    /*  Start Floating widget service and finish current activity */
    private void startFloatingWidgetService1() {
        startService(new Intent(PantsActivity.this, FloatingWidgetService2.class));
        //이부분은 버튼 누르면 어플이 꺼지도록 함.
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK)
                //If permission granted start floating widget service
                startFloatingWidgetService1();
            /**else
             //Permission is not available then display toast
             Toast.makeText(this,
             getResources().getString(R.string.draw_other_app_permission_denied),
             Toast.LENGTH_SHORT).show();*/

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    public void createFloatingWidget2(View view) {
        //Check if the application has draw over other apps permission or not?
        //This permission is by default available for API<23. But for API > 23
        //you have to ask for the permission in runtime.
        //getcontext 부분 수정함.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            //If the draw over permission is not available open the settings screen
            //to grant the permission. 권한을 받기 위한 부분
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE);
        } else
            //If permission is granted start floating widget service
            startFloatingWidgetService2();

    }

    /*  Start Floating widget service and finish current activity */
    private void startFloatingWidgetService2() {
        startService(new Intent(PantsActivity.this, FloatingWidgetService.class));
        //이부분은 버튼 누르면 어플이 꺼지도록 함.
        finish();
    }


}

