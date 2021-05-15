package com.vrone.studentattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class informtoall extends AppCompatActivity {
    EditText mymsg;
    Button sndsmsbtn;
    MyDatabaseHelper mydb;
    ArrayList<String> mycourses, mysemesters, stuphn;
    Spinner course, sem;
    ArrayList<String> namelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informtoall);
        this.setTitle("Inform to all");
        mymsg = findViewById(R.id.ed_mymsgs);
        sndsmsbtn = findViewById(R.id.btn_sendsmstoall);
        course = findViewById(R.id.spinner_course);
        sem = findViewById(R.id.spinner_sem);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mydb=new MyDatabaseHelper(informtoall.this);
        mycourses = new ArrayList<>();
        mycourses.add("Choose Student Course");
        mycourses.add("MCA");
        mycourses.add("MBA");
        mycourses.add("BCA");
        mycourses.add("BBA");
        mycourses.add("B.Com");
        mycourses.add("B.Tech(CSE)");

        course.setAdapter(new ArrayAdapter<>(informtoall.this, android.R.layout.simple_spinner_item, mycourses));
        mysemesters = new ArrayList<>();
        mysemesters.add("Choose Student Semester ");
        mysemesters.add("1");
        mysemesters.add("2");
        mysemesters.add("3");
        mysemesters.add("4");
        mysemesters.add("5");
        mysemesters.add("6");
        mysemesters.add("7");
        mysemesters.add("8");
        sem.setAdapter(new ArrayAdapter<>(informtoall.this, android.R.layout.simple_spinner_item, mysemesters));
//        smsforall();
        sndsmsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                try {
                    mydb.mydbopen();
                    Toast.makeText(informtoall.this, "" + course.getSelectedItem().toString() + " " + sem.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                    stuphn = mydb.fetchphnnumber(informtoall.this,course.getSelectedItem().toString(),sem.getSelectedItem().toString());
                   mydb.mydbclose();
                Log.i("getdata",stuphn.toString());
                    if (ActivityCompat.checkSelfPermission(informtoall.this,Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(informtoall.this, new String[]{Manifest.permission.SEND_SMS}, 1002);
                    }

                else {
                    Toast.makeText(informtoall.this, "" + stuphn.toString(), Toast.LENGTH_SHORT).show();
                    smsforall();
                    }
//                } catch (Exception e) {
//                    Toast.makeText(informtoall.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1002) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    smsforall();
                Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
            }
        }
    }
//
    void smsforall() {

        for (int i = 0; i < stuphn.size(); i++) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("tel:"+stuphn.get(i), null, "" + mymsg.getText().toString(), null,
                        null);
                Toast.makeText(getApplicationContext(), "SMS Sent to" + " " + stuphn.get(i), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.e("DatabaseError", "" + e.getMessage());
            }


//        SmsManager mysms=SmsManager.getDefault();
//
//            mysms.sendTextMessage("1;5;7", null, "" + mymsg.getText().toString(), null, null);
//
//        Toast.makeText(this, "Sms Sent To All", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}