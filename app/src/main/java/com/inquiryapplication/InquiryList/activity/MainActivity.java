package com.inquiryapplication.InquiryList.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.inquiryapplication.BottomMenuClass.BottomMenu;
import com.inquiryapplication.InquiryList.adapter.inquiry_list;
import com.inquiryapplication.R;
import com.inquiryapplication.courses.model.CoursesModel;
import com.inquiryapplication.inquiryform.Model.InquiryModel;
import com.inquiryapplication.inquiryform.activity.FormActivity;
import com.inquiryapplication.inquiryform.sqldir.DBHandler;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements BottomNavigationView
        .OnNavigationItemSelectedListener, inquiry_list.OnInquiryListListener {
    BottomNavigationView bottomNavigationView;
    final int REQUEST_CALL = 1;
    DBHandler dbHandler;
    ListView listView;
    TextView noData,cancle;
    private ArrayList<InquiryModel> InquiryArrayList,filterArrayList;
    RecyclerView recyclerView;
    AppCompatImageView filterBtn,courseBtn;
    AppCompatEditText filter;
    private ArrayList<CoursesModel> CourseArrayList,newCourseArrayList;
    ArrayList<String> Coursename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Inquiry List");

        findViewById();
        set_data();
    }

    public void set_data(){
        CourseArrayList = dbHandler.readCourses();
        Coursename = new ArrayList<>();
        newCourseArrayList = new ArrayList<>();
        for (int i=0;i<CourseArrayList.size();i++) {
            Coursename.add(""+CourseArrayList.get(i).getName());
        }

        recyclerView = findViewById(R.id.recyclerView);
        noData = findViewById(R.id.noData);
        try{
            InquiryArrayList = new ArrayList<>();
            InquiryArrayList = dbHandler.readinquiries();
            for (int i=0;i<InquiryArrayList.size();i++){
                Log.e("InquiryArrayList>>",""+InquiryArrayList.get(i).getName());
            }

           setAdapter(InquiryArrayList);
        }catch(Exception e){
            noData.setVisibility(View.VISIBLE);
        }
    }

    public void setAdapter(ArrayList<InquiryModel> inquiryArrayList){
        Log.e("inquiryArrayList>>>",""+inquiryArrayList);
        inquiry_list adapter = new inquiry_list(inquiryArrayList,MainActivity.this,this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void findViewById(){
        dbHandler = new DBHandler(MainActivity.this);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        filterBtn = findViewById(R.id.filterBtn);
        courseBtn = findViewById(R.id.courseBtn);
        filter = findViewById(R.id.filter);
        cancle = findViewById(R.id.cancle);

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAdapter(InquiryArrayList);
                filter.setVisibility(View.GONE);
                noData.setVisibility(View.GONE);
                cancle.setVisibility(View.GONE);
            }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        courseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCourseDialog();
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            bottomNavigationView.setOnNavigationItemSelectedListener(this);
            BottomMenu.handleNavigationItemSelected(item,MainActivity.this);
            return true;
        });
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.inquiryList);
        menuItem.setChecked(true);
    }

    public void openCourseDialog(){
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_open_coursefilter);
        listView = dialog.findViewById(R.id.list);

        Log.e("List>>>>",""+Coursename);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, Coursename);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("User clicked ", Coursename.get(position));
//                newCourseArrayList
                filterArrayList = new ArrayList<>();
                for(int i=0;i<InquiryArrayList.size();i++){
                    if(InquiryArrayList.get(i).getCourse().contains(CourseArrayList.get(position).getId()+"")){

                        filter.setVisibility(View.VISIBLE);
                        cancle.setVisibility(View.VISIBLE);
                        filter.setText(CourseArrayList.get(position).getName()+"");

                        noData.setVisibility(View.GONE);
                        filterArrayList.add(InquiryArrayList.get(i));
                        setAdapter(filterArrayList);
                        dialog.hide();
                    }else{
                        filter.setVisibility(View.VISIBLE);
                        cancle.setVisibility(View.VISIBLE);
                        filter.setText(CourseArrayList.get(position).getName()+"");
                        noData.setVisibility(View.VISIBLE);
                        setAdapter(filterArrayList);
                        dialog.hide();
                    }
                }
            }
        });
        dialog.show();
    }

    public void openDialog(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;

                filterArrayList = new ArrayList<>();
                for(int i=0;i<InquiryArrayList.size();i++){
                    if(InquiryArrayList.get(i).getDate().contains(selectedDate)){

                        filter.setVisibility(View.VISIBLE);
                        cancle.setVisibility(View.VISIBLE);
                        filter.setText(selectedDate);

                        noData.setVisibility(View.GONE);
                        filterArrayList.add(InquiryArrayList.get(i));
                        setAdapter(filterArrayList);
                    }else{
                        filter.setVisibility(View.VISIBLE);
                        cancle.setVisibility(View.VISIBLE);
                        filter.setText(selectedDate);
                        noData.setVisibility(View.VISIBLE);
                        setAdapter(filterArrayList);
                    }
                }
            }
        }, year, month, day);
        datePicker.show();
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
        deleteInquiry(InquiryArrayList.get(position).getId());
    }

    public void deleteInquiry(int id){
        try{
            dbHandler.deleteinquiry(""+id);
            set_data();
            Toast.makeText(this, "Inquiry Deleted Successfully", Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Toast.makeText(this, "Error While Deleted Inquiry", Toast.LENGTH_SHORT).show();
        }
    }

    public void onEditClick(int position){
        try{
            Intent intent = new Intent(MainActivity.this, FormActivity.class);
            intent.putExtra("id", InquiryArrayList.get(position).getId());
            startActivity(intent);
        }catch(Exception e){
            Toast.makeText(this, "Unknown Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCallClick(int position) {
        try{
            Intent phone_intent = new Intent(Intent.ACTION_CALL);
            phone_intent.setData(Uri.parse("tel:"+InquiryArrayList.get(position).getNumber()+""));
            startActivity(phone_intent);
        }catch(Exception e){
            Log.e("Error>>>",e+"");
            ActivityCompat.requestPermissions((Activity) MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);ActivityCompat.requestPermissions((Activity)MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
        }
    }
}