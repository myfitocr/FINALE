package com.example.hello;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class SizeClass implements Parcelable {
    private String sizeName;
    private ArrayList<Float> sizeInfo;

    public SizeClass(String sizeName) {
        this.sizeName = sizeName;
    }

    //pants activity가 객체 받았을 때 직렬화 풀어줌
    protected SizeClass(Parcel in) {
        this.sizeName = in.readString();
        sizeInfo=new ArrayList<>();
        in.readList(sizeInfo, ClassLoader.getSystemClassLoader());
    }

    //데이터 직렬화
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sizeName);
        dest.writeList(sizeInfo);
    }

    //creator
    public static final Creator<SizeClass> CREATOR = new Creator<SizeClass>() {
        @Override
        public SizeClass createFromParcel(Parcel in) {
            return new SizeClass(in);
        }

        @Override
        public SizeClass[] newArray(int size) {
            return new SizeClass[size];
        }
    };

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public ArrayList<Float> getSizeInfo() {
        return sizeInfo;
    }

    public void setSizeInfo(ArrayList<Float> sizeInfo) {
        this.sizeInfo = sizeInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
