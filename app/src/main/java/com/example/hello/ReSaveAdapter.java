package com.example.hello;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hello.ui.Tab2.DashboardFragment;

import java.util.ArrayList;

public class ReSaveAdapter extends RecyclerView.Adapter<ReSaveAdapter.SaveView>{
    private Context mContext;
    private ArrayList<String> list;
    private ArrayList<ArrayList<SizeClass>> sizeClassesArray;

    public ReSaveAdapter(Context mContext, ArrayList<String> list, ArrayList<ArrayList<SizeClass>> sizeClassesArray) {
        this.mContext=mContext;
        this.list=list;
        this.sizeClassesArray=sizeClassesArray;
    }

    //viewHolder class
    public class SaveView extends RecyclerView.ViewHolder{
        ImageView save_capture;
        public SaveView(@NonNull View itemView) {
            super(itemView);

            save_capture=itemView.findViewById(R.id.save_capture);
        }
    }

    @NonNull
    @Override
    public SaveView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.row_save,parent,false);
        return new SaveView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaveView holder, final int position) {
        final Uri item=Uri.parse(list.get(position));

        holder.save_capture.setImageURI(item);

        holder.save_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create intent
                Intent intent=new Intent(mContext, MyPantsActivity.class);
                intent.putExtra("sizeClasses", sizeClassesArray.get(position));
                System.out.println("sizeClasses"+sizeClassesArray.size());
                System.out.println("sizeClasses size"+sizeClassesArray.get(position).size());
                System.out.println("position"+position);

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
