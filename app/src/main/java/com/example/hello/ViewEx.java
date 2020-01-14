package com.example.hello;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import java.util.ArrayList;

public class ViewEx extends View {
    ArrayList<SizeClass> sizeClasses;

    public ViewEx(Context context, ArrayList<SizeClass> sizeClasses) {
        super(context);
        this.sizeClasses=sizeClasses;
    }

    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        Paint MyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        MyPaint.setStrokeWidth(7f);
        MyPaint.setStyle(Paint.Style.STROKE);
        MyPaint.setColor(Color.GRAY);

        canvas.drawPath(firstPath(sizeClasses.get(0)),MyPaint);

        ArrayList<Integer> colors=new ArrayList<>();
        colors.add(Color.parseColor("#e53935"));
        colors.add(Color.parseColor("#9575cd"));
        colors.add(Color.parseColor("#1976d2"));
        colors.add(Color.parseColor("#43a047"));
        colors.add(Color.parseColor("#ff7043"));

        MyPaint.setStrokeWidth(3.5f);
        for (int i=1;i<sizeClasses.size();i++){
            MyPaint.setColor(colors.get(i-1));
            canvas.drawPath(path(sizeClasses.get(i)),MyPaint);
        }
    }

    public Path firstPath(SizeClass sizeClass){
        ArrayList<Float> sizeInfo=sizeClass.getSizeInfo();

        float TotalLength = sizeInfo.get(0) * 10;
        float Waist = sizeInfo.get(1) * 8;
        float Thigh = sizeInfo.get(2) * 7;
        float Rise = sizeInfo.get(3) * 10;
        float HemCross = sizeInfo.get(4) * 6;

        Path path = new Path();
        path.moveTo(500,245);//배꼽쓰시작
        float beltLine = 40* (Thigh - Waist/2)/(20 + Rise);

        path.lineTo(( 500 - Waist/2 ), 245);//허리반
        path.cubicTo(( 500 - Waist/2 ),245,500 - Thigh,255 + Rise,(500 - Waist*4/10 - HemCross/2),245+ TotalLength);
        path.lineTo((500 - Waist*4/10 + HemCross/2), 245+ TotalLength);//왼쪽 밑단
        path.lineTo(500, 245 + Rise);//가랭쓰
        path.lineTo(500 + Waist*4/10 - HemCross/2, 245+ TotalLength);//안따리쓰
        path.lineTo(500 + Waist*4/10 + HemCross/2, 245+ TotalLength);//오른밑단쓰
        path.cubicTo(500 + Waist*4/10 + HemCross/2,245+TotalLength,500 + Thigh,255 + Rise,500+ Waist/2,245);
        path.lineTo(500,245);

        path.moveTo(500 - Waist/2 - beltLine,285);//벨트라인
        path.lineTo(500 + Waist/2 + beltLine ,285);

        path.moveTo(500,285);
        path.lineTo(500,245 + Rise);//지퍼
        path.moveTo(500, 285 + (Rise - 40)*15/20);//지퍼 대각선라인
        path.lineTo(530,255 + (Rise - 40)*15/20);
        path.lineTo(530,285);

        return path;
    }

    public Path path(SizeClass sizeClass){
        ArrayList<Float> sizeInfo=sizeClass.getSizeInfo();

        float TotalLength = sizeInfo.get(0) * 10;
        float Waist = sizeInfo.get(1) * 8;
        float Thigh = sizeInfo.get(2) * 7;
        float Rise = sizeInfo.get(3) * 10;
        float HemCross = sizeInfo.get(4) * 6;

        Path path = new Path();
        path.moveTo(500,245);//배꼽쓰시작
        float beltLine = 40* (Thigh - Waist/2)/(20 + Rise);

        path.lineTo(( 500 - Waist/2 ), 245);//허리반
        path.cubicTo(( 500 - Waist/2 ),245,500 - Thigh,255 + Rise,(500 - Waist*4/10 - HemCross/2),245+ TotalLength);
        path.lineTo((500 - Waist*4/10 + HemCross/2), 245+ TotalLength);//왼쪽 밑단
        path.lineTo(500, 245 + Rise);//가랭쓰
        path.lineTo(500 + Waist*4/10 - HemCross/2, 245+ TotalLength);//안따리쓰
        path.lineTo(500 + Waist*4/10 + HemCross/2, 245+ TotalLength);//오른밑단쓰
        path.cubicTo(500 + Waist*4/10 + HemCross/2,245+TotalLength,500 + Thigh,255 + Rise,500+ Waist/2,245);
        path.lineTo(500,245);

        path.moveTo(500 - Waist/2 - beltLine,285);//벨트라인
        path.lineTo(500 + Waist/2 + beltLine ,285);

        return path;
    }
}
