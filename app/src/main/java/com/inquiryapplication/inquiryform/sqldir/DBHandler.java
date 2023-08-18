package com.inquiryapplication.inquiryform.sqldir;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import com.inquiryapplication.courses.model.CoursesModel;
import com.inquiryapplication.inquiryform.Model.InquiryModel;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "inquery_table.db";
    private static final int DB_VERSION = 1;

    private static final String Inquiry_Table = "inquiries";
    private static final String Course_Table = "courses";

    private static final String ID = "id";
    private static final String NAME = "name";


    private static final String C_ID = "id";
    private static final String C_NAME = "name";
    private static final String NUMBER = "number";
    private static final String EMAIL = "email";
    private static final String DATE = "date";
    private static final String COURSE = "course";
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query = "CREATE TABLE " + Inquiry_Table + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " TEXT,"
                + NUMBER + " TEXT,"
                + EMAIL + " TEXT,"
                + DATE + " TEXT,"
                + COURSE + " TEXT)";
        sqLiteDatabase.execSQL(query);

        String query_ = "CREATE TABLE " + Course_Table + " ("
                + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + C_NAME + " TEXT)";

        sqLiteDatabase.execSQL(query_);


    }


    public void addNewInquiry(String name, String number, String email, String date ,String course) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(NAME, name);
        values.put(NUMBER, number);
        values.put(EMAIL, email);
        values.put(DATE, date);
        values.put(COURSE, course);

        db.insert(Inquiry_Table, null, values);

        db.close();
    }

    public void addNewCourse(String name) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(C_NAME, name);

        db.insert(Course_Table, null, values);

        db.close();
    }

    public void Updateinquiry(String id,String name, String number, String email, String date ,String course) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(NAME, name);
        values.put(NUMBER, number);
        values.put(EMAIL, email);
        values.put(DATE, date);
        values.put(COURSE, course);

        db.update(Inquiry_Table, values, "id=?", new String[]{id});

        db.close();
    }

    public ArrayList<InquiryModel> readinquiries() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + Inquiry_Table, null);

        ArrayList<InquiryModel> courseModalArrayList = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {
            do {
                courseModalArrayList.add(new InquiryModel(
                        cursorCourses.getInt(0),
                        cursorCourses.getString(1),
                        cursorCourses.getString(2),
                        cursorCourses.getString(3),
                        cursorCourses.getString(4),cursorCourses.getString(5)
                        ));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return courseModalArrayList;
    }

    public ArrayList<CoursesModel> readCourses() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + Course_Table, null);
        ArrayList<CoursesModel> courseModalArrayList1 = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {
            do {
                courseModalArrayList1.add(new CoursesModel(
                        cursorCourses.getInt(0),
                        cursorCourses.getString(1)
                ));
            } while (cursorCourses.moveToNext());
        }

        cursorCourses.close();
        return courseModalArrayList1;
    }

    public ArrayList<InquiryModel> readOneinquiry(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + Inquiry_Table + " WHERE " + ID + " = " + id, null);

        ArrayList<InquiryModel> courseModalArrayList = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {
            do {
                courseModalArrayList.add(new InquiryModel(
                        cursorCourses.getInt(0),
                        cursorCourses.getString(1),
                        cursorCourses.getString(2),
                        cursorCourses.getString(3),
                        cursorCourses.getString(4),cursorCourses.getString(5)
                ));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return courseModalArrayList;
    }

    public void deleteinquiry(String id) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Inquiry_Table, "id=?", new String[]{id});
        db.close();
    }

    public void deleteCourse(String id) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Course_Table, "id=?", new String[]{id});
        db.close();
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Inquiry_Table);
        onCreate(sqLiteDatabase);
    }
}
