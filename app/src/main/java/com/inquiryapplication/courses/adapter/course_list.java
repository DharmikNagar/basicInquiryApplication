package com.inquiryapplication.courses.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inquiryapplication.InquiryList.adapter.inquiry_list;
import com.inquiryapplication.R;
import com.inquiryapplication.courses.model.CoursesModel;

import java.util.ArrayList;

public class course_list  extends RecyclerView.Adapter<course_list.ViewHolder>{
    private ArrayList<CoursesModel> dataList;
    private Activity activity;
    private OnCourseListListener mCourseListClickListener;

    public course_list(ArrayList<CoursesModel> dataList, Activity activity, course_list.OnCourseListListener mCourseListClickListener) {

        this.dataList = dataList;
        for (int i=0;i<dataList.size();i++) {
            Log.e("dataList>>"+i, dataList + "");
        }
        this.activity = activity;
        this.mCourseListClickListener = mCourseListClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inquiry_list, parent, false);
        return new course_list.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txt_name.setText(dataList.get(position).getName());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCourseListClickListener.onDeleteClick(position);
            }
        });

        holder.parentRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCourseListClickListener.onEditClick(position);
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
            callButton.setVisibility(View.GONE);
        }
    }

    public interface OnCourseListListener {
        void onMoreOptionsClick(int adapterPosition, ImageView ivMoreOptions);
        void onDeleteClick(int position);
        void onEditClick(int position);
    }
}
