package com.vrone.studentattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class view_attendance extends AppCompatActivity {
    ListView mylist;
    ArrayList<String> mycourses,mysemesters,namelist,datelist;
    ArrayList<Integer> rollnolist,total_lectd,present_lectd,absent_lectd;
    ArrayList<Double> per_lectd;
    double per_lect;
    Spinner course,sem,studentname,datechoose;
    Button fetchbtn,viewatt,showbtn;
    int total_lect,present_lect,absent_lect;
    MyDatabaseHelper mydb;
    ArrayList<Integer> total_list,present_list,absent_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);
        this.setTitle("View Attendance");
        course=findViewById(R.id.ma_spinner_course);
        sem=findViewById(R.id.ma_spinner_sem);
        mydb=new MyDatabaseHelper(view_attendance.this);
        fetchbtn=findViewById(R.id.fetch);
        viewatt=findViewById(R.id.my_btn_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mycourses = new ArrayList<>();
        namelist=new ArrayList<>();
        rollnolist=new ArrayList<>();
        total_lectd=new ArrayList<>();
        present_lectd=new ArrayList<>();
        absent_lectd=new ArrayList<>();
        per_lectd=new ArrayList<>();
        mysemesters=new ArrayList<>();
        total_list=new ArrayList<>();
        present_list=new ArrayList<>();
        datelist=new ArrayList<>();
        absent_list=new ArrayList<>();
        mylist=findViewById(R.id.view_list_attendance);
        studentname=findViewById(R.id.ma_spinner_s_name);


        mycourses.add("Choose Student Course");
        mycourses.add("MCA");
        mycourses.add("MBA");
        mycourses.add("BCA");
        mycourses.add("BBA");
        mycourses.add("B.Com");
        mycourses.add("B.Tech(CSE)");
        course.setAdapter(new ArrayAdapter<>( this,android.R.layout.simple_spinner_item,mycourses));

        mysemesters.add("Choose Student Semester ");
        mysemesters.add("1");
        mysemesters.add("2");
        mysemesters.add("3");
        mysemesters.add("4");
        mysemesters.add("5");
        mysemesters.add("6");
        mysemesters.add("7");
        mysemesters.add("8");
        sem.setAdapter(new ArrayAdapter<>( this,android.R.layout.simple_spinner_item,mysemesters));
        viewatt.setEnabled(false);

        fetchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{

                    mydb.mydbopen();
                    TreeMap mygroup = mydb.fetchstudentdetails(view_attendance.this,course.getSelectedItem().toString(),sem.getSelectedItem().toString());
                    //Toast.makeText(view_attendance.this, mygroup.size(), Toast.LENGTH_SHORT).show();
                    namelist = (ArrayList<String>) mygroup.get("nameslist");
                    rollnolist = (ArrayList<Integer>) mygroup.get("rollnolist");
                    Toast.makeText(view_attendance.this, "Total Students : "+String.valueOf(namelist.size()), Toast.LENGTH_SHORT).show();
                    mydb.mydbclose();
                    namelist.add("All");
                    studentname.setAdapter(new ArrayAdapter<>(view_attendance.this,android.R.layout.simple_spinner_item,namelist));
                    viewatt.setEnabled(true);
                }catch(Exception e){
                    Toast.makeText(view_attendance.this, "Error occured :"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> selectstu=new ArrayList<>();
                selectstu.clear();
                mydb.mydbopen();
                Toast.makeText(view_attendance.this, "Total Students : "+String.valueOf(namelist.size()-1), Toast.LENGTH_SHORT).show();
                if(studentname.getSelectedItem().toString().equalsIgnoreCase("All")) {
                    //Toast.makeText(view_attendance.this, "Total Students : "+String.valueOf(namelist.size()-1), Toast.LENGTH_SHORT).show();
                    namelist.remove("All");

                   // Toast.makeText(view_attendance.this, "Total Students : "+namelist.toString(), Toast.LENGTH_SHORT).show();
                    try {
                       // int stusize=namelist.size();
                        for (int i =0; i<namelist.size(); i++) {
                            total_lect = mydb.total_lectures(namelist.get(i), course.getSelectedItem().toString(), sem.getSelectedItem().toString());
                            present_lect = mydb.present_lectures(namelist.get(i), course.getSelectedItem().toString(), sem.getSelectedItem().toString());
                            absent_lect = mydb.absent_lectures(namelist.get(i), course.getSelectedItem().toString(), sem.getSelectedItem().toString());
                           // Toast.makeText(view_attendance.this, "Sname : " + namelist.get(i) + " Total lect : " + total_lect + "\n Present : " + present_lect + " Absent : " + absent_lect, Toast.LENGTH_SHORT).show();

                            //Toast.makeText(view_attendance.this, "Stu name : "+namelist.get(i), Toast.LENGTH_SHORT).show();

                            total_lectd.add(total_lect);
                            present_lectd.add(present_lect);
                            absent_lectd.add(absent_lect);
//
                            per_lect = ((double) ((double) present_lect / (double) total_lect) * 100);
                            per_lect=Double.parseDouble(new DecimalFormat("##.##").format(per_lect));
                            per_lectd.add(per_lect);
                            //Toast.makeText(view_attendance.this, "S name : " + namelist.get(i) + " Per : ", Toast.LENGTH_SHORT).show();
                        }
                        mylist.setAdapter(new myAllAdapter(view_attendance.this, android.R.layout.simple_list_item_1, namelist));
                    }catch (Exception e){
                        Toast.makeText(view_attendance.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                else{
                    total_lect=mydb.total_lectures(studentname.getSelectedItem().toString(),course.getSelectedItem().toString(),sem.getSelectedItem().toString());
                    present_lect=mydb.present_lectures(studentname.getSelectedItem().toString(),course.getSelectedItem().toString(),sem.getSelectedItem().toString());
                    absent_lect=mydb.absent_lectures(studentname.getSelectedItem().toString(),course.getSelectedItem().toString(),sem.getSelectedItem().toString());
                    selectstu.add(studentname.getSelectedItem().toString());
                    per_lect=((double) ((double)present_lect/(double)total_lect)*100);
                    per_lect=Double.parseDouble(new DecimalFormat("##.##").format(per_lect));
                   // Toast.makeText(view_attendance.this, "Toatl "+String.valueOf(total_lect)+" "+String.valueOf(present_lect)+" "+String.valueOf(absent_lect)+" "+String.valueOf(per_lect), Toast.LENGTH_SHORT).show();
                    mylist.setAdapter(new myAdapter(view_attendance.this,android.R.layout.simple_list_item_1,selectstu));
                }
            }
        });
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(view_attendance.this, "Roll number "+String.valueOf(rollnolist.get(i)), Toast.LENGTH_SHORT).show();
            }
        });
    }
    class myAdapter extends ArrayAdapter<String>{
        int indexpos;
        public myAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            indexpos=namelist.indexOf(objects.get(0));
//            Toast.makeText(view_attendance.this, ""+objects.get(0), Toast.LENGTH_SHORT).show();
        }

        @Override
        public View getView(final int position, final View convertView, ViewGroup parent) {
            LayoutInflater inflator= (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myrow=inflator.inflate(R.layout.cs_view_attendance,parent,false);
            TextView rollno,name,total,present,absent,ttlper;

            rollno=myrow.findViewById(R.id.cus_att_rollno);
            name=myrow.findViewById(R.id.cus_att_sname);
            total=myrow.findViewById(R.id.cus_att_ttllec);
            present=myrow.findViewById(R.id.cus_att_ttl_pre_lec);
            absent=myrow.findViewById(R.id.cus_att_ttl_ab_lec);
            ttlper=myrow.findViewById(R.id.cus_att_ttl_pre);

            name.setText(namelist.get(indexpos));
            rollno.setText(String.valueOf(rollnolist.get(indexpos)));
            total.setText(String.valueOf(total_lect));
            present.setText(String.valueOf(present_lect));
            absent.setText(String.valueOf(absent_lect));
            ttlper.setText(String.valueOf(per_lect));


            return myrow;
        }
    }
    class myAllAdapter extends ArrayAdapter<String>{
        int indexpos;
        public myAllAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
//            indexpos=namelist.indexOf(objects.get(0));
//            Toast.makeText(view_attendance.this, ""+objects.get(0), Toast.LENGTH_SHORT).show();
        }

        @Override
        public View getView(final int position, final View convertView, ViewGroup parent) {
            LayoutInflater inflator= (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myrow=inflator.inflate(R.layout.cs_view_attendance,parent,false);
            TextView rollno,name,total,present,absent,ttlper;

            rollno=myrow.findViewById(R.id.cus_att_rollno);
            name=myrow.findViewById(R.id.cus_att_sname);
            total=myrow.findViewById(R.id.cus_att_ttllec);
            present=myrow.findViewById(R.id.cus_att_ttl_pre_lec);
            absent=myrow.findViewById(R.id.cus_att_ttl_ab_lec);
            ttlper=myrow.findViewById(R.id.cus_att_ttl_pre);

            name.setText(namelist.get(position));
            rollno.setText(String.valueOf(rollnolist.get(position)));
            total.setText(String.valueOf(total_lectd.get(position)));
            present.setText(String.valueOf(present_lectd.get(position)));
            absent.setText(String.valueOf(absent_lectd.get(position)));
          ttlper.setText(String.valueOf(per_lectd.get(position)));

//            name.setText("A");
//            rollno.setText(" ");
//            total.setText(" ");
//            present.setText(" ");
//            absent.setText(" ");
          //  ttlper.setText(" ");

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

}
