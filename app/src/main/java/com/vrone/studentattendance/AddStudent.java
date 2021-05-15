package com.vrone.studentattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class AddStudent extends AppCompatActivity {
    MyDatabaseHelper mydatabase;
    Spinner course,sem;
    Button insert,update,sub_btn;
    RadioButton male,female;
    EditText name,phoneno,ed_rollno,email,getotpcode;
    ArrayList<String> mycourses,mysemesters;
    String sturollno,number;
    int otp,sturoll,maxroll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Add Student");
        course=findViewById(R.id.sp_course);
        sem=findViewById(R.id.sp_semester);
        insert=findViewById(R.id.btn_insert);
        update=findViewById(R.id.btn_update);


        mydatabase=new MyDatabaseHelper(AddStudent.this);

        name=findViewById(R.id.ed_stuname);
        ed_rollno=findViewById(R.id.ed_rollno);
        email=findViewById(R.id.ed_email);
        phoneno=findViewById(R.id.ed_phoneno);
        male=findViewById(R.id.rb_male);
        female=findViewById(R.id.rb_female);

        mycourses=new ArrayList<>();
        mycourses.add("Choose Student Course");
        mycourses.add("MCA");
        mycourses.add("MBA");
        mycourses.add("BCA");
        mycourses.add("BBA");
        mycourses.add("B.Com");
        mycourses.add("B.Tech(CSE)");
        course.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,mycourses));

        mysemesters=new ArrayList<>();
        mysemesters.add("Choose Student Semester ");
        mysemesters.add("1");
        mysemesters.add("2");
        mysemesters.add("3");
        mysemesters.add("4");
        mysemesters.add("5");
        mysemesters.add("6");
        mysemesters.add("7");
        mysemesters.add("8");
        sem.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,mysemesters));

        if(getIntent().getExtras()!=null){
            try {
                insert.setEnabled(false);
//                Toast.makeText(AddStudent.this, "Student Roll no : " + getIntent().getExtras().get("rollno"), Toast.LENGTH_SHORT).show();
                sturoll=getIntent().getExtras().getInt("rollno");
//                getstudentdetails();
                try {
                    mydatabase.mydbopen();
                    ArrayList<String> studentrecord= (ArrayList<String>) mydatabase.fetchstudentdetails(AddStudent.this,getIntent().getExtras().getInt("rollno"));
                    name.setText(studentrecord.get(0));
                    int coursepos=mycourses.indexOf(studentrecord.get(1));
                    course.setSelection(coursepos);
                    int sempos=mysemesters.indexOf(studentrecord.get(2));
                    sem.setSelection(sempos);
//                    Toast.makeText(this, "course: "+mycourses.indexOf(studentrecord.get(1)), Toast.LENGTH_SHORT).show();
                    sturollno=studentrecord.get(3);
                    ed_rollno.setText(studentrecord.get(3));
                    phoneno.setText(studentrecord.get(5));
                    email.setText(studentrecord.get(6));
                   if(studentrecord.get(4).equals("Male"))
                    {
                        male.setChecked(true);
                    }
                    else
                    {
                        female.setChecked(true);
                    }
                    mydatabase.close();
//                    Toast.makeText(this, "Student Record ", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Log.e("StudentUpdate",e.getMessage());
                    }
            }catch (Exception e){
                Log.e("Intent",e.getMessage());
            }
        }
        else{
            insert.setEnabled(true);
        }

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    if(ActivityCompat.checkSelfPermission(AddStudent.this,Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED) {
                        if(ActivityCompat.checkSelfPermission(AddStudent.this,Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AddStudent.this,new String[]{Manifest.permission.SEND_SMS,Manifest.permission.READ_PHONE_STATE},100);
                    }}
                    else {
                            try{
                                if(validation()) {
                              //Toast.makeText(AddStudent.this, "Done all clear", Toast.LENGTH_SHORT).show();
                                    showotpdialog();
                                    sendsms();
//

                                }
                            }catch (Exception ex){
                                Toast.makeText(AddStudent.this, "Error Occured Due to : "+ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                    }
                }catch (Exception e){
                    Toast.makeText(AddStudent.this, "Error Occured Due To \n "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        sem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i!=0)
                Toast.makeText(AddStudent.this, ""+mysemesters.get(i), Toast.LENGTH_SHORT).show();
                mydatabase.mydbopen();
                maxroll=mydatabase.max_rollno(course.getSelectedItem().toString(),sem.getSelectedItem().toString());
                ed_rollno.setText(String.valueOf(maxroll));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             try{
                 mydatabase.mydbopen();
//                 Toast.makeText(AddStudent.this, "You clicked on Update Button", Toast.LENGTH_SHORT).show();
//                (String name,String course,String semester,String rollno,String gender,String phone,String email,String roll)
                 if (mydatabase.StuUpdate(name.getText().toString(),course.getSelectedItem().toString(),sem.getSelectedItem().toString(),ed_rollno.getText().toString(),male.isChecked()?"Male":"Female",phoneno.getText().toString(),email.getText().toString(),sturollno))
                {
                    Toast.makeText(AddStudent.this, "Update record", Toast.LENGTH_SHORT).show();
                }
            }catch(Exception e){
                 Toast.makeText(AddStudent.this, "Error Occured due to : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    void insertrecord(){
        mydatabase.mydbopen();
        Long myrow = mydatabase.insertstudetails(AddStudent.this,name.getText().toString(), course.getSelectedItem().toString(), sem.getSelectedItem().toString(), Integer.valueOf(ed_rollno.getText().toString()), (male.isChecked() ? "Male" : "Female"), phoneno.getText().toString(), email.getText().toString());
        if (myrow>0) {
            clear();
            Toast.makeText(AddStudent.this, "Records Inserted", Toast.LENGTH_SHORT).show();
        }
    }
    void sendsms()
    {
        Random random=new Random();
        otp=random.nextInt(9999);
        Toast.makeText(AddStudent.this, "tel:"+phoneno.getText(), Toast.LENGTH_SHORT).show();
        SmsManager mymanager = SmsManager.getDefault();
        mymanager.sendTextMessage("tel:"+phoneno.getText(),null, "Your Otp Code Is : "+String.valueOf(otp), null, null);
        Toast.makeText(AddStudent.this, "Message Sent", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==100)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                if(grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    showotpdialog();
                    sendsms();
                }
            }
            else
            {
                Toast.makeText(this, "SMS Permission is necessary for contacting customer care", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showotpdialog(){
        final Dialog mydialog = new Dialog(AddStudent.this);
        mydialog.setContentView(R.layout.dialog_otp);
        mydialog.show();
        sub_btn = mydialog.findViewById(R.id.btn_verify_phno);
        getotpcode = mydialog.findViewById(R.id.ed_get_otp_code);
        sub_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cur= Integer.parseInt(String.valueOf(getotpcode.getText()));
                //Toast.makeText(AddStudent.this, "In Dialog button click "+String.valueOf(otp)+" "+cur, Toast.LENGTH_SHORT).show();
                if (cur==otp){
                    try{
                        Toast.makeText(AddStudent.this, "Record ready for inserted in db", Toast.LENGTH_SHORT).show();
                        insertrecord();
                        mydialog.dismiss();
                    }catch (Exception de){
                        Toast.makeText(AddStudent.this, "Database Error Occured due to : "+de.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(AddStudent.this, "You Entered Wrong OTP Code \n Enter OTP again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    void clear(){
        name.setText(null);
        ed_rollno.setText(null);
        phoneno.setText(null);
        email.setText(null);
        male.setSelected(false);
        female.setSelected(false);
        course.setSelection(0);
        sem.setSelection(0);
    }
    boolean validation(){
        if(name.getText().length()<2){
            Toast.makeText(this, "Enter a Student Name (min 2 characters)", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(course.getSelectedItem().toString().equals("Choose Student Course")){
            Toast.makeText(this, "Select Student Course", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(sem.getSelectedItem().toString().equals("Choose Student Semester ")){
            Toast.makeText(this, "Select Student Semester", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(ed_rollno.getText().length()<1){
            Toast.makeText(this, "Enter Student Roll number", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(phoneno.getText().length()<10){
            Toast.makeText(this, "Enter Student Phone No. properly (10 digits)", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(phoneno.getText().length()>10){
            Toast.makeText(this, "Enter Student Phone No. properly (10 digits)", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!email.getText().toString().contains("@")){
            Toast.makeText(this, "Please Check Email id ", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!email.getText().toString().contains(".com")){
            Toast.makeText(this, "Please Check Email id ", Toast.LENGTH_SHORT).show();
            return false;
        }
       /* if((!male.isChecked())||(!female.isChecked())){
            Toast.makeText(this, "Select Student Gender ", Toast.LENGTH_SHORT).show();
            return false;
        }*/
        return  true;
}
}

