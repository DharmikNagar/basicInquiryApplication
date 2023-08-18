package com.inquiryapplication.inquiryform.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.inquiryapplication.BottomMenuClass.BottomMenu;
import com.inquiryapplication.InquiryList.activity.MainActivity;
import com.inquiryapplication.R;
import com.inquiryapplication.courses.activity.CourseListActivity;
import com.inquiryapplication.courses.model.CoursesModel;
import com.inquiryapplication.inquiryform.Model.InquiryModel;
import com.inquiryapplication.inquiryform.sqldir.DBHandler;

import java.util.ArrayList;
import java.util.Calendar;

public class FormActivity extends AppCompatActivity implements BottomNavigationView
        .OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    TextInputEditText edit_text_name,edit_text_number,edit_text_email,edit_date_inquiry;
    AppCompatSpinner spn_course;
    String course = "" , name="",number="",email="",date="";
    DBHandler dbHandler;
    int id = 0;
    private ArrayList<CoursesModel> CourseArrayList;
    private ArrayList<InquiryModel> InquiryArrayList;
    private ArrayList<String> courseList;
    private String mStrSelectedCourseId="";
    AppCompatButton btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Inquiry Form");
        actionBar.setDisplayHomeAsUpEnabled(true);

        findViewById();
        init();
    }

    public void init(){
        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        if(id != 0){
            setValue();
        }


        InquiryArrayList = new ArrayList<>();
    }

    public void setValue(){
        InquiryArrayList = new ArrayList<>();
        InquiryArrayList = dbHandler.readOneinquiry(""+id);

        Log.e("InquiryArrayListExist>>",InquiryArrayList.get(0).getName()+" "+InquiryArrayList.get(0).getNumber()+" "+InquiryArrayList.get(0).getEmail()+" "+InquiryArrayList.get(0).getDate());

        edit_text_name.setText(InquiryArrayList.get(0).getName());
        edit_text_number.setText(InquiryArrayList.get(0).getNumber());
        edit_text_email.setText(InquiryArrayList.get(0).getEmail());
        edit_date_inquiry.setText(InquiryArrayList.get(0).getDate());

        for (int i =0;i<CourseArrayList.size();i++) {
            if(Integer.parseInt(InquiryArrayList.get(0).getCourse()) == CourseArrayList.get(i).getId()){
                spn_course.setSelection(i);
            }
        }

        btnSubmit.setText("Update");
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StoreInquiry();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the back button click event
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void findViewById(){
        dbHandler= new DBHandler(FormActivity.this);
        CourseArrayList = dbHandler.readCourses();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        spn_course = findViewById(R.id.spn_course);
        edit_text_name = findViewById(R.id.edit_text_name);
        edit_text_number = findViewById(R.id.edit_text_number);
        edit_text_email = findViewById(R.id.edit_text_email);
        edit_date_inquiry = findViewById(R.id.edit_date_inquiry);

        btnSubmit = findViewById(R.id.btnSubmit);

        spn_course.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mStrSelectedCourseId = "" + CourseArrayList.get(i).getId();
                Log.e("mStrSelectedCourseId>>",mStrSelectedCourseId+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edit_date_inquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(FormActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        edit_date_inquiry.setText(selectedDate);
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StoreInquiry();
            }
        });


        if(CourseArrayList.size()>0){
            setCourseAdapter();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Courses Are Empty")
                    .setMessage(" Add More Courses To Insert Inquiry")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(FormActivity.this, CourseListActivity.class);
                            FormActivity.this.startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Handle negative button click
                        }
                    })
                    .setCancelable(false) // Set whether the dialog can be canceled by clicking outside
                    .show();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            BottomMenu.handleNavigationItemSelected(item,FormActivity.this);
            return true;
        });
        if(id == 0) {
            Menu menu = bottomNavigationView.getMenu();
            MenuItem menuItem = menu.findItem(R.id.inquiryForm);
            menuItem.setChecked(true);
        }else{
            Menu menu = bottomNavigationView.getMenu();
            MenuItem menuItem = menu.findItem(R.id.inquiryList);
            menuItem.setChecked(true);
        }
    }
    public void setCourseAdapter(){
        courseList = new ArrayList<>();

        for (int i =0;i<CourseArrayList.size();i++) {
            courseList.add(CourseArrayList.get(i).getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, courseList);
        spn_course.setAdapter(adapter);
    }

    public void StoreInquiry(){
        name = edit_text_name.getText().toString();
        number = edit_text_number.getText().toString();
        email = edit_text_email.getText().toString();
        date = edit_date_inquiry.getText().toString();
        Log.e("SubmitData>>>","Name"+name+"Number"+number+"email"+email+"date"+date);
        validatioinform();

    }

    public void validatioinform(){
        if(name.isEmpty()){
            Toast.makeText(this, "Name Is Required", Toast.LENGTH_SHORT).show();
            return;
        }
        if(number.isEmpty()){
            Toast.makeText(this, "Number Is Required", Toast.LENGTH_SHORT).show();
            return;
        }
        if(email.isEmpty()){
            Toast.makeText(this, "Email Is Required", Toast.LENGTH_SHORT).show();
            return;
        }
        if(date.isEmpty()){
            Toast.makeText(this, "Date Is Required", Toast.LENGTH_SHORT).show();
            return;
        }
        if(mStrSelectedCourseId.isEmpty()){
            Toast.makeText(this, "Course Is Required", Toast.LENGTH_SHORT).show();
            return;
        }
        if(id != 0){
            updateData();
        }else{
            storeData();
        }
    }

    public void storeData(){

        try{
            dbHandler.addNewInquiry(name,number,email,date,mStrSelectedCourseId);
            Log.e("StoreData>>>",mStrSelectedCourseId+" "+name+" "+number+" "+email+" "+date);
            Toast.makeText(this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Toast.makeText(this, "Error While Inserting Data", Toast.LENGTH_SHORT).show();
        }

        removeData();
    }

    public void updateData(){
        try{
            dbHandler.Updateinquiry(""+id,name,number,email,date,mStrSelectedCourseId);
            Log.e("UpdateData>>>",id+" "+mStrSelectedCourseId+" "+name+" "+number+" "+email+" "+date);
            Toast.makeText(this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(FormActivity.this, MainActivity.class);
            startActivity(intent);
        }catch(Exception e){
            Toast.makeText(this, "Error While Updating Data", Toast.LENGTH_SHORT).show();
        }
    }

    public void removeData(){
        edit_text_name.setText("");
        edit_text_number.setText("");
        edit_text_email.setText("");
        edit_date_inquiry.setText("");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}