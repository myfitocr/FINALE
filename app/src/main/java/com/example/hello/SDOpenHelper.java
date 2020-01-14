package com.example.hello;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

import androidx.annotation.ArrayRes;

import java.util.ArrayList;
import java.util.List;

public class SDOpenHelper {

    private static final String DATABASE_NAME = "InnerDatabase(SQLite).db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private SavedDatabaseOpenHelper mDBHelper;
    private Context mCtx;

    private class SavedDatabaseOpenHelper extends SQLiteOpenHelper {
        public SavedDatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(DataBases.CreateDB._CREATE0);
            db.execSQL(DataBases.CreateDB._CREATE1);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS "+DataBases.CreateDB._TABLENAME0);
            db.execSQL("DROP TABLE IF EXISTS "+DataBases.CreateDB._TABLENAME1);
            onCreate(db);
        }
    }

    public SDOpenHelper(Context context){
        this.mCtx = context;
    }

    public SDOpenHelper open() throws SQLException {
        mDBHelper = new SavedDatabaseOpenHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void create(){
        mDBHelper.onCreate(mDB);
    }

    public void close(){
        mDB.close();
    }

    public long addPhoto(String photo){
        ContentValues value = new ContentValues();
        value.put(DataBases.CreateDB.Photo, photo);
        return mDB.insert(DataBases.CreateDB._TABLENAME1,null,value);
    }

    public long addStyle(ArrayList<SizeClass> sizeClasses){
        ContentValues values = new ContentValues();

        ArrayList<String> columns=new ArrayList<>();
        columns.add(DataBases.CreateDB.s0);
        columns.add(DataBases.CreateDB.s1);
        columns.add(DataBases.CreateDB.s2);
        columns.add(DataBases.CreateDB.s3);
        columns.add(DataBases.CreateDB.s4);
        columns.add(DataBases.CreateDB.s5);
        columns.add(DataBases.CreateDB.s6);
        columns.add(DataBases.CreateDB.s7);
        columns.add(DataBases.CreateDB.s8);
        columns.add(DataBases.CreateDB.s9);

        for (int i=0;i<sizeClasses.size();i++){
            values.put(columns.get(i),sc2String(sizeClasses.get(i)));
        }

        for (int j=sizeClasses.size();j<10;j++){
            values.put(columns.get(j),"null");
        }

        return mDB.insert(DataBases.CreateDB._TABLENAME0, null, values);
    }

    public String sc2String (SizeClass sizeClass){
        String string;
        string=sizeClass.getSizeName();
        for(int i=0; i<sizeClass.getSizeInfo().size();i++){
            string=string+" "+sizeClass.getSizeInfo().get(i);
        }
        return string;
    }

    /////밑 두개를 같이불러서 삭제해야함

    public boolean deleteSizeArrayColumn(long id){
        return mDB.delete(DataBases.CreateDB._TABLENAME0, "_id="+id,null) > 0;
    }

    public boolean deletePhotoArrayColumn(long id){
        return mDB.delete(DataBases.CreateDB._TABLENAME1, "_id="+id,null) > 0;
    }

    //////////////////////////

    public ArrayList<String> getPhotoArray(SQLiteDatabase db){
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM phototable", null);
        if (cursor.moveToFirst()){
            do{
                arrayList.add(cursor.getString(1));
            }while(cursor.moveToNext());
        }
        return arrayList;
    }



    /*public ArrayList<ArrayList<String>> getSizeArray(SQLiteDatabase db){
        ArrayList<ArrayList<String>> arrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM _TABLENAME0", null);
        if(cursor.moveToFirst()){
            do{
                ArrayList<String> subList=new ArrayList<>();
                for (int i=0;i<10;i++){
                    if (!cursor.getString(i+1).equals("null")) subList.add(cursor.getString(i+1));
                }
                arrayList.add(subList);
            }while(cursor.moveToNext());
        }
        return arrayList;
    }*/

    public ArrayList<String> getSizeArray(SQLiteDatabase db){
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM sizetable", null);
        if(cursor.moveToFirst()){
            do{
                String subList = "";
                for (int i=0;i<10;i++){
                    if (!cursor.getString(i+1).equals("null")) subList=subList+"\n"+(cursor.getString(i+1));
                }arrayList.add(subList);
            }while(cursor.moveToNext());
        }
        return arrayList;
    }
}


class DataBases{

    public static final class CreateDB implements BaseColumns{
        public static final String s0 = "s0";
        public static final String s1 = "s1";
        public static final String s2 = "s2";
        public static final String s3 = "s3";
        public static final String s4 = "s4";
        public static final String s5 = "s5";
        public static final String s6 = "s6";
        public static final String s7 = "s7";
        public static final String s8 = "s8";
        public static final String s9 = "s9";

        public static final String _TABLENAME0 = "sizetable";
        public static final String _CREATE0 = "create table if not exists "+_TABLENAME0+"("
                +_ID+" integer primary key autoincrement, "
                +s0+" text not null , "
                +s1+" text not null , "
                +s2+" text not null , "
                +s3+" text not null , "
                +s4+" text not null , "
                +s5+" text not null , "
                +s6+" text not null , "
                +s7+" text not null , "
                +s8+" text not null , "
                +s9+" text not null );";

        ////포토테이블
        public static final String Photo = "photo";
        public static final String _TABLENAME1 = "phototable";
        public static final String _CREATE1 = "create table if not exists "+_TABLENAME1+"("
                +_ID+" integer primary key autoincrement, "
                +Photo+" text not null );";
    }
}

//class DataBases{
//
//    public static final class CreateDB implements BaseColumns{
//        public static final String Size = "size";
//        public static final String TotalLength = "totalLength";
//        public static final String Waist = "Waist";
//        public static final String Thigh = "Thigh";
//        public static final String Rise = "Rise";
//        public static final String HemCross = "HemCross";
//        public static final String _TABLENAME0 = "sizetable";
//        public static final String _CREATE0 = "create table if not exists "+_TABLENAME0+"("
//                +_ID+" integer primary key autoincrement, "
//                +Size+" text not null , "
//                +TotalLength+" text not null , "
//                +Waist+" text not null , "
//                +Thigh+" text not null , "
//                +Rise+" text not null , "
//                +HemCross+" text not null );";
//        ////포토테이블
//        public static final String Photo = "photo";
//        public static final String _TABLENAME1 = "phototable";
//        public static final String _CREATE1 = "create table if not exists "+_TABLENAME1+"("
//                +_ID+" integer primary key autoincrement, "
//                +Photo+" text not null );";
//    }
//}