package com.inquiryapplication.BottomMenuClass;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.inquiryapplication.InquiryList.activity.MainActivity;
import com.inquiryapplication.R;
import com.inquiryapplication.courses.activity.CourseListActivity;
import com.inquiryapplication.inquiryform.activity.FormActivity;

public class BottomMenu {

    public static boolean handleNavigationItemSelected(MenuItem item,Activity activity) {
        int itemId = item.getItemId();

        if (itemId == R.id.inquiryList) {
            Intent intent = new Intent(activity,MainActivity.class);
            activity.startActivity(intent);

            return true;
        } else if (itemId == R.id.inquiryForm) {
            Intent intent = new Intent(activity, FormActivity.class);
            activity.startActivity(intent);
            return true;
        } else if (itemId == R.id.courses) {
            Intent intent = new Intent(activity, CourseListActivity.class);
            activity.startActivity(intent);
            return true;
        }
        return false;
    }

}
