package com.vrone.studentattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

public class MarkAttendance extends AppCompatActivity {
    ListView mylist;
    ArrayList<String> mycourses,mysemesters,namelist,phno;
    ArrayList<Integer> rollnolist,present,absent;
    Spinner course,sem;
    Button fetchbtn,markattendance,allpresent,allabsent;
    MyDatabaseHelper mydb;
    String curr_date,appuser;

    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Mark Attendance");
        mylist = findViewById(R.id.ma_lv_viewstudents);
        course = findViewById(R.id.ma_spinner_course);
        sem = findViewById(R.id.ma_spinner_sem);
        fetchbtn = findViewById(R.id.btn_vstu);
        allpresent=findViewById(R.id.btn_all_present);
        allabsent=findViewById(R.id.btn_all_absent);
        allabsent.setEnabled(false);
        allpresent.setEnabled(false);
        markattendance=findViewById(R.id.btn_mark_attendance);
        mydb=new MyDatabaseHelper(MarkAttendance.this);

        Date d = new Date();
        CharSequence s  = DateFormat.format("MMMM d, yyyy ", d.getTime());
        curr_date=String.valueOf(s);

        phno=new ArrayList<>();
        mycourses = new ArrayList<>();
        namelist=new ArrayList<>();
        rollnolist=new ArrayList<>();
        present=new ArrayList<>();
        absent=new ArrayList<>();

        mycourses.add("Choose Student Course");
        mycourses.add("MCA");
        mycourses.add("MBA");
        mycourses.add("BCA");
        mycourses.add("BBA");
        mycourses.add("B.Com");
        mycourses.add("B.Tech(CSE)");



        course.setAdapter(new ArrayAdapter<>( MarkAttendance.this,android.R.layout.simple_spinner_item,mycourses));
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
        sem.setAdapter(new ArrayAdapter<>( MarkAttendance.this,android.R.layout.simple_spinner_item,mysemesters));
        checkPermissions();
        fetchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    present.clear();
                    mydb.mydbopen();
                    TreeMap mygroup = mydb.fetchstudentdetails(MarkAttendance.this,course.getSelectedItem().toString(),sem.getSelectedItem().toString());
                    namelist = (ArrayList<String>) mygroup.get("nameslist");
                    rollnolist = (ArrayList<Integer>) mygroup.get("rollnolist");
                    phno= (ArrayList<String>) mygroup.get("phnno");
//
                    mydb.mydbclose();
                    mylist.setAdapter(new myAdapter(MarkAttendance.this,android.R.layout.simple_list_item_1,namelist));
                }catch(Exception e) {
                    Toast.makeText(MarkAttendance.this, "No Record Found", Toast.LENGTH_LONG).show();
                }
                allabsent.setEnabled(true);
                allpresent.setEnabled(true);
                //Toast.makeText(MarkAttendance.this, "Course : " +course.getSelectedItem().toString()+"\n Semester : "+sem.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Animation animation1 = new AlphaAnimation(0.2f, 0.8f);
//                animation1.setDuration(300000);
//                animation1.getRepeatCount();
//                view.startAnimation(animation1);

                int itemclicksize=present.size();
                if(itemclicksize<1){
                    present.add(i);
                    view.setBackgroundColor(R.color.background);
                }
                if(itemclicksize>=1){
//                    Toast.makeText(MarkAttendance.this, "Position "+String.valueOf(i), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(MarkAttendance.this, "Item Click present this poition : "+String.valueOf(itemclick.get(i)), Toast.LENGTH_SHORT).show();

                    present.add(i);
                    view.setBackgroundColor(R.color.background);
                }

            }


//            }
        });
        markattendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date d = new Date();
                CharSequence s  = DateFormat.format("MMMM d, yyyy ", d.getTime());
                try{mydb.mydbopen();
                    appuser= mydb.getuserdetails().get(0).toString();
                    Toast.makeText(MarkAttendance.this, "User name : "+appuser, Toast.LENGTH_SHORT).show();
                }catch(Exception e){
                    Toast.makeText(MarkAttendance.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(MarkAttendance.this, "User : "+appuser, Toast.LENGTH_SHORT).show();
                if(check_attendance_marker(curr_date, course.getSelectedItem().toString(), sem.getSelectedItem().toString())) {

                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm a");
                    Date date = new Date();
                    for (int i = 0; i < present.size(); i++) {
                        mydb.mydbopen();

                        mydb.mark_attendance(MarkAttendance.this, Integer.parseInt(String.valueOf(rollnolist.get(present.get(i)))), String.valueOf(namelist.get(present.get(i))), course.getSelectedItem().toString(), sem.getSelectedItem().toString(), String.valueOf(s), "present");
                        //Toast.makeText(MarkAttendance.this, "Present Student is " + String.valueOf(namelist.get(present.get(i))) + "\n Demo : " + String.valueOf(phno.get(present.get(i))), Toast.LENGTH_SHORT).show();
                        psendsms(String.valueOf(phno.get(present.get(i))), String.valueOf(formatter.format(date)));
                    }
                    for (int j = 0; j < absent.size(); j++) {
                        mydb.mydbopen();
                        //Toast.makeText(MarkAttendance.this, "Absent Student is " + String.valueOf(namelist.get(absent.get(j))), Toast.LENGTH_SHORT).show();
                        mydb.mark_attendance(MarkAttendance.this, Integer.parseInt(String.valueOf(rollnolist.get(absent.get(j)))), String.valueOf(namelist.get(absent.get(j))), course.getSelectedItem().toString(), sem.getSelectedItem().toString(), String.valueOf(s), "absent");
                        asendsms(String.valueOf(phno.get(absent.get(j))), String.valueOf(formatter.format(date)));
                    }
                }
            }
        });
        allpresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n=namelist.size();
                absent.clear();
                present.clear();
                allpresent.setBackgroundColor(Color.GREEN);
                allabsent.setBackgroundColor(Color.GRAY);
                for (int i=0;i<n;i++){
                    present.add(i);
                }
                //Toast.makeText(MarkAttendance.this, ""+present.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        allabsent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n=namelist.size();
                present.clear();
                absent.clear();
                allabsent.setBackgroundColor(Color.RED);
                allpresent.setBackgroundColor(Color.GRAY);
                for (int i=0;i<n;i++){
                    absent.add(i);
                }
                // Toast.makeText(MarkAttendance.this, ""+absent.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    class myAdapter extends ArrayAdapter<String>{

        public myAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(final int position, final View convertView, ViewGroup parent) {
            LayoutInflater inflator= (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myrow=inflator.inflate(R.layout.attendance_layout,parent,false);
            TextView rollno,name;
            Button p,a,l;

            rollno=myrow.findViewById(R.id.mark_srollno);
            name=myrow.findViewById(R.id.mark_sname);
            p=myrow.findViewById(R.id.mark_p);
            a=myrow.findViewById(R.id.mark_a);
            l=myrow.findViewById(R.id.mark_l);
            name.setText(namelist.get(position));
            rollno.setText(String.valueOf(rollnolist.get(position)));

            p.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View view) {
//                   view.setBackgroundColor(R.color.textbackground);
                    allpresent.setBackgroundColor(Color.GRAY);
                    allabsent.setBackgroundColor(Color.GRAY);
                    View parent = (View)view.getParent();
                    parent.setBackgroundColor(Color.GREEN);
                    present.add(position);
                    if(absent.contains(position)) {
                        int index = absent.indexOf(position);
                        absent.remove(index);
                    }

                }
            });
            a.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View view) {
                    allpresent.setBackgroundColor(Color.GRAY);
                    allabsent.setBackgroundColor(Color.GRAY);
                    View parent = (View)view.getParent();
                    parent.setBackgroundColor(Color.RED);
                    absent.add(position);
                    if(present.contains(position)) {
                        int index = present.indexOf(position);
                        present.remove(index);
                    }
                }
            });
            l.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View view) {
                    View parent = (View)view.getParent();
                    parent.setBackgroundColor(Color.YELLOW);
                    absent.add(position);
                }
            });
            return myrow;
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    void checkPermissions(){
        if(ActivityCompat.checkSelfPermission(MarkAttendance.this,Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.checkSelfPermission(MarkAttendance.this,Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MarkAttendance.this,new String[]{Manifest.permission.SEND_SMS,Manifest.permission.READ_PHONE_STATE},100);
            }}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==100)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                if(grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            else
            {
                Toast.makeText(this, "SMS Permission is necessary for contacting customer care", Toast.LENGTH_SHORT).show();
            }
        }
    }
    void psendsms(String no,String dt)
    {
        SmsManager mymanager = SmsManager.getDefault();
        mymanager.sendTextMessage("tel:"+no,null, "On "+dt+" You marked present in Web lab by "+appuser, null, null);
        Toast.makeText(MarkAttendance.this, "Message Sent", Toast.LENGTH_LONG).show();
    }
    void asendsms(String no,String dt)
    {
        Date d = new Date();
        CharSequence s  = DateFormat.format("MMMM d, yyyy ", d.getTime());
        SmsManager mymanager = SmsManager.getDefault();
        mymanager.sendTextMessage("tel:"+no,null, "On  "+dt+"  You marked Absent in Web lab by "+appuser, null, null);
        Toast.makeText(MarkAttendance.this, "Message Sent", Toast.LENGTH_LONG).show();
    }
    boolean check_attendance_marker(String dt,String course,String sem){
        mydb.mydbopen();
        boolean f=mydb.check_attendace_marker(dt,course,sem);
        if(f){

            return true;
        }
        else{
            Toast.makeText(this, "You Already mark Attendance for this class.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}

