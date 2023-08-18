package com.inquiryapplication.InquiryList.adapter;

import android.app.ActionBar;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.inquiryapplication.R;
import com.inquiryapplication.inquiryform.Model.InquiryModel;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class inquiry_list extends RecyclerView.Adapter<inquiry_list.ViewHolder>{
    private ArrayList<InquiryModel> dataList;
    private Activity activity;
    private OnInquiryListListener mInquiryListClickListener;

    public inquiry_list(ArrayList<InquiryModel> dataList,Activity activity,OnInquiryListListener mInquiryListClickListener) {
        this.dataList = dataList;
        this.activity = activity;
        this.mInquiryListClickListener = mInquiryListClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inquiry_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txt_name.setText(dataList.get(position).getName());

        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInquiryListClickListener.onCallClick(position);
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInquiryListClickListener.onDeleteClick(position);
            }
        });

        holder.parentRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInquiryListClickListener.onEditClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_name;
        public ImageView deleteButton,callButton;
        public RelativeLayout parentRelative;

        public ViewHolder(View view) {
            super(view);
            txt_name = view.findViewById(R.id.txt_name);
            callButton = view.findViewById(R.id.callButton);
            deleteButton = view.findViewById(R.id.deleteButton);
            parentRelative = view.findViewById(R.id.parentRelative);
        }
    }

    public interface OnInquiryListListener {
        void onMoreOptionsClick(int adapterPosition, ImageView ivMoreOptions);
        void onDeleteClick(int position);
        void onEditClick(int position);
        void onCallClick(int position);
    }
}
