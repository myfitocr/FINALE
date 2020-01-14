package com.example.hello;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReSizeAdapter extends RecyclerView.Adapter<ReSizeAdapter.SizeView> {
    private Context mContext;
    private ArrayList<SizeClass> list;

    public ReSizeAdapter(Context mContext, ArrayList<SizeClass> list) {
        this.mContext = mContext;
        this.list = list;
    }

    //viewholder class
    public class SizeView extends RecyclerView.ViewHolder {
        TextView tr0, tr1, tr2, tr3, tr4, tr5;

        public SizeView(@NonNull View itemView) {
            super(itemView);

            tr0=itemView.findViewById(R.id.tr0);
            tr1=itemView.findViewById(R.id.tr1);
            tr2=itemView.findViewById(R.id.tr2);
            tr3=itemView.findViewById(R.id.tr3);
            tr4=itemView.findViewById(R.id.tr4);
            tr5=itemView.findViewById(R.id.tr5);
        }
    }

    @NonNull
    @Override
    public SizeView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.row_size,parent,false);
        return new SizeView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SizeView holder, int position) {
        final SizeClass item=list.get(position);
        final ArrayList<Float> sizeInfo=item.getSizeInfo();

        holder.tr0.setText(item.getSizeName());
        holder.tr1.setText(String.valueOf(sizeInfo.get(0)));
        holder.tr2.setText(String.valueOf(sizeInfo.get(1)));
        holder.tr3.setText(String.valueOf(sizeInfo.get(2)));
        holder.tr4.setText(String.valueOf(sizeInfo.get(3)));
        holder.tr5.setText(String.valueOf(sizeInfo.get(4)));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
