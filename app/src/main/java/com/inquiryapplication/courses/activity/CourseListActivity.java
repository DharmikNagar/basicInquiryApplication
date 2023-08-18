package com.inquiryapplication.courses.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.inquiryapplication.BottomMenuClass.BottomMenu;
import com.inquiryapplication.R;
import com.inquiryapplication.courses.adapter.course_list;
import com.inquiryapplication.courses.model.CoursesModel;
import com.inquiryapplication.inquiryform.activity.FormActivity;
import com.inquiryapplication.inquiryform.sqldir.DBHandler;

import java.util.ArrayList;

public class CourseListActivity extends AppCompatActivity  implements BottomNavigationView
        .OnNavigationItemSelectedListener,course_list.OnCourseListListener{
    BottomNavigationView bottomNavigationView;
    AppCompatButton btnSubmit;
    TextInputEditText edit_text_name;
    private ArrayList<CoursesModel> CourseArrayList;
    RecyclerView recyclerView;
    String name_txt="";
    TextView noData;
    Dialog dialog;
    DBHandler dbHandler;
    AppCompatImageView addCourseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Course List");

        findViewById();
        set_data();
    }
    public void findViewById(){
        dbHandler = new DBHandler(CourseListActivity.this);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            BottomMenu.handleNavigationItemSelected(item,CourseListActivity.this);
            return true;
        });
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.courses);
        menuItem.setChecked(true);
    }
    public void set_data(){
        recyclerView = findViewById(R.id.recyclerView);
        CourseArrayList = new ArrayList<>();
        noData = findViewById(R.id.noData);
        addCourseBtn = findViewById(R.id.addCourseBtn);

        imageView();

        try{

            CourseArrayList = dbHandler.readCourses();
            for (int i=0;i<CourseArrayList.size();i++){
                Log.e("CourseArrayList>>",""+CourseArrayList.get(i).getName());
            }

            course_list adapter = new course_list(CourseArrayList, CourseListActivity.this,this);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CourseListActivity.this, RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
        }catch(Exception e){
            noData.setVisibility(View.VISIBLE);
        }
    }

    public void imageView(){
        addCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(CourseListActivity.this);
                dialog.setContentView(R.layout.dialog_open_course);
                edit_text_name = dialog.findViewById(R.id.edit_text_name);
                btnSubmit = dialog.findViewById(R.id.btnSubmit);

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postData();
                        dialog.hide();
                    }
                });
                dialog.show();
            }
        });
    }

    public void postData(){
        name_txt = edit_text_name.getText().toString();
        try{
            dbHandler.addNewCourse(name_txt);
            set_data();
            Toast.makeText(this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Log.e("Course Exception>>",""+e);
            Toast.makeText(this, "Error While Inserting Data", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onMoreOptionsClick(int adapterPosition, ImageView ivMoreOptions) {
    }

    @Override
    public void onDeleteClick(int position) {
        deleteCourse(CourseArrayList.get(position).getId());
    }

    public void deleteCourse(int id){
        try{
            dbHandler.deleteCourse(""+id);
            set_data();
            Toast.makeText(this, "Course Deleted Successfully", Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Toast.makeText(this, "Error While Deleted Course", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onEditClick(int position) {
//        try{
//            Intent intent = new Intent(CourseListActivity.this, FormActivity.class);
//            intent.putExtra("id", CourseArrayList.get(position).getId());
//            startActivity(intent);
//        }catch(Exception e){
//            Toast.makeText(this, "Unknown Error", Toast.LENGTH_SHORT).show();
//        }
    }


}