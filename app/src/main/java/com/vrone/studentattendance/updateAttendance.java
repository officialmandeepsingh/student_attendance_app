package com.vrone.studentattendance;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class updateAttendance extends AppCompatActivity {
    ListView mylist;
    ArrayList<String>attrec,sdates;
    ArrayList<Integer> rollnolist,present,absent,leave;
    Button fetchbtn,updateattendance,fetchdates;
    MyDatabaseHelper mydb;
    String curr_date,appuser,att_record;
    EditText getrollno;
    Spinner getdateof_att;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_attendance);
        mydb=new MyDatabaseHelper(updateAttendance.this);
        getrollno=(EditText)findViewById(R.id.ua_srollno);
        fetchbtn=(Button)findViewById(R.id.ua_getRolldetail);
        updateattendance=(Button)findViewById(R.id.btn_update_attendance);
        fetchdates=findViewById(R.id.ua_fetch_roll);
        mylist=(ListView)findViewById(R.id.ua_lv_viewstudents);
        attrec=new ArrayList<>();
        getdateof_att=(Spinner) findViewById(R.id.ua_dateof_att);
        sdates=new ArrayList<>();
        present=new ArrayList<>();
        absent=new ArrayList<>();
        leave=new ArrayList<>();

        fetchdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    getstudentdates();
                }catch (Exception e){
                    Toast.makeText(updateAttendance.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        fetchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getattrecord();
            }
        });

        updateattendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy ");
                Date date = new Date();
                for (int i = 0; i < present.size(); i++) {
                    mydb.mydbopen();

                    int status=mydb.update_attendance(updateAttendance.this,attrec.get(0),attrec.get(4), String.valueOf(attrec.get(0)), String.valueOf(attrec.get(1)), String.valueOf(attrec.get(2)), String.valueOf(attrec.get(3)), String.valueOf(formatter.format(date)), "present");
                    Toast.makeText(updateAttendance.this, "Attendance updated Successfully", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MarkAttendance.this, "Present Student is " + String.valueOf(namelist.get(present.get(i))) + "\n Demo : " + String.valueOf(phno.get(present.get(i))), Toast.LENGTH_SHORT).show();
                    //psendsms(String.valueOf(phno.get(present.get(i))), String.valueOf(formatter.format(date)));
                }
                for (int j = 0; j < absent.size(); j++) {
                    mydb.mydbopen();
                    //Toast.makeText(MarkAttendance.this, "Absent Student is " + String.valueOf(namelist.get(absent.get(j))), Toast.LENGTH_SHORT).show();
                    int status=mydb.update_attendance(updateAttendance.this,attrec.get(0),attrec.get(4), String.valueOf(attrec.get(0)), String.valueOf(attrec.get(1)), String.valueOf(attrec.get(2)), String.valueOf(attrec.get(3)), String.valueOf(formatter.format(date)), "absent");
                    Toast.makeText(updateAttendance.this, "Attendance updated Successfully", Toast.LENGTH_SHORT).show();
                    //asendsms(String.valueOf(phno.get(absent.get(j))), String.valueOf(formatter.format(date)));
                }
            }
        });

    }
    private void getstudentdates() {
        String roll=getrollno.getText().toString().trim();

        mydb.mydbopen();
        sdates=mydb.getstudentdates(roll);
        getdateof_att.setAdapter(new ArrayAdapter<>( updateAttendance.this,android.R.layout.simple_spinner_item,sdates));
    }

    private void getattrecord(){
        String roll=getrollno.getText().toString().trim();
        String date=getdateof_att.getSelectedItem().toString();
        attrec=mydb.getattrec(roll,date);
        String sname=attrec.get(1);
        mylist.setAdapter(new myAdapter(updateAttendance.this,android.R.layout.simple_list_item_1, Collections.singletonList(sname)));
        Log.i("mydata",Arrays.asList(attrec).toString());
    }
    private  void splitstr(String al){

        Log.i("mydata",al);
    }

    void checkPermissions(){
        if(ActivityCompat.checkSelfPermission(updateAttendance.this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.checkSelfPermission(updateAttendance.this,Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(updateAttendance.this,new String[]{Manifest.permission.SEND_SMS,Manifest.permission.READ_PHONE_STATE},100);
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
        Toast.makeText(updateAttendance.this, "Message Sent", Toast.LENGTH_LONG).show();
    }
    void asendsms(String no,String dt)
    {
        Date d = new Date();
        CharSequence s  = DateFormat.format("MMMM d, yyyy ", d.getTime());
        SmsManager mymanager = SmsManager.getDefault();
        mymanager.sendTextMessage("tel:"+no,null, "On  "+dt+"  You marked Absent in Web lab by "+appuser, null, null);
        Toast.makeText(updateAttendance.this, "Message Sent", Toast.LENGTH_LONG).show();
    }

    class myAdapter extends ArrayAdapter<String> {

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


            name.setText(attrec.get(1));
            rollno.setText(String.valueOf(attrec.get(0)));

            p.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View view) {
//                   view.setBackgroundColor(R.color.textbackground);

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
}

