package com.inquiryapplication.inquiryform.sqldir;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.inquiryapplication.courses.model.CoursesModel;

import java.util.ArrayList;

public class DBCourseHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "inquery_table.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "courses";

    private static final String ID = "id";
    private static final String NAME = "name";

    public DBCourseHandler(Context context, DBHandler dbHandler) {
        super(context, DB_NAME, null, DB_VERSION);
        dbHandler  = new DBHandler(context);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " TEXT)";

        sqLiteDatabase.execSQL(query);
    }

    public void addNewCourse(String name) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(NAME, name);

        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    public void UpdateCourse(String id,String name) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(NAME, name);

        db.update(TABLE_NAME, values, "id=?", new String[]{id});

        db.close();
    }

    public ArrayList<CoursesModel> readCourses() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        ArrayList<CoursesModel> courseModalArrayList = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {
            do {
                courseModalArrayList.add(new CoursesModel(
                        cursorCourses.getInt(0),
                        cursorCourses.getString(1)
                ));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return courseModalArrayList;
    }

    public ArrayList<CoursesModel> readOnecourse(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = " + id, null);

        ArrayList<CoursesModel> courseModalArrayList = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {
            do {
                courseModalArrayList.add(new CoursesModel(
                        cursorCourses.getInt(0),
                        cursorCourses.getString(1)
                ));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return courseModalArrayList;
    }

    public void deleteCourse(String id) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, "id=?", new String[]{id});
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
