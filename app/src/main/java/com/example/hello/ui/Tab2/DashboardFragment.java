package com.example.hello.ui.Tab2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.Size;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.hello.FloatingWidgetService;
import com.example.hello.FloatingWidgetService2;
import com.example.hello.MainActivity;
import com.example.hello.R;
import com.example.hello.ReSaveAdapter;
import com.example.hello.SDOpenHelper;
import com.example.hello.SizeClass;
import com.example.hello.ui.Tab1.HomeViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    RecyclerView recyclerView;
    ReSaveAdapter reSaveAdapter;
    ArrayList<String> uriList=new ArrayList<>();
    ArrayList<ArrayList<SizeClass>> saveList=new ArrayList<>();

    Uri recentUri;

    SDOpenHelper sdOpenHelper;
    ArrayList<String> stringList=new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        sdOpenHelper=new SDOpenHelper(getContext());
        sdOpenHelper.open().create();
        int a = ((MainActivity)getActivity()).code_1;

        if(((MainActivity)getActivity()).code_1 == 20){
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String sortOrder = MediaStore.Images.Media._ID + " COLLATE LOCALIZED ASC";
            Cursor cursor = getContext().getContentResolver().query(uri,null,null,null,sortOrder);

            if(cursor.moveToLast()) {
                String idColumn=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                recentUri = Uri.parse("content://media/external/images/media/" + idColumn);
            }while (cursor.moveToNext());

            uriList=sdOpenHelper.getPhotoArray(SDOpenHelper.mDB);
            uriList.add(recentUri.toString());

            sdOpenHelper.addPhoto(recentUri.toString());
        }
        else {
            uriList = sdOpenHelper.getPhotoArray(SDOpenHelper.mDB);
        }

        stringList=sdOpenHelper.getSizeArray(SDOpenHelper.mDB); //ArrayList<String>
        for (int i=0;i<stringList.size();i++){
            saveList.add(getSizeInfo(stringList.get(i))); //String-> ArrayList<SizeClass>-> ArrayList<ArrayList<SizeClass>>
        }

        if (uriList.size()!=0){
            //recycler adapter
            recyclerView=view.findViewById(R.id.save_recycler);
            reSaveAdapter=new ReSaveAdapter(getContext(), uriList, saveList);
            recyclerView.setAdapter(reSaveAdapter);

            //layout manager
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            SnapHelper snapHelper=new PagerSnapHelper();
            recyclerView.setLayoutManager(layoutManager);
            snapHelper.attachToRecyclerView(recyclerView);
        }
        return view;
    }
    /*  start floating widget service  */


    private ArrayList<SizeClass> getSizeInfo(String getSize){
        String[] array=getSize.split("\n");
        ArrayList<SizeClass> sizeClasses=new ArrayList<>();

        for(int i=1;i<array.length;i++){
            String[] getSizeInfo=array[i].split(" ");
            SizeClass sizeClass=new SizeClass(getSizeInfo[0]);

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
}