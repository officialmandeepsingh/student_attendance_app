package com.vrone.studentattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AppRegister extends AppCompatActivity {

    EditText username,userid,phoneno,password;
    Button reg;
    MyDatabaseHelper mydatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_register);
        this.setTitle("User Registration");
        username=findViewById(R.id.ed_reg_username);
        userid=findViewById(R.id.ed_reg_userid);
        phoneno=findViewById(R.id.ed_reg_phn);
        mydatabase=new MyDatabaseHelper(AppRegister.this);
        password=findViewById(R.id.ed_reg_password);
        reg=findViewById(R.id.btn_reg);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              try {
                  insertrecord();
              }catch (Exception e){
//                  Toast.makeText(AppRegister.this, "Error Occured due to :"+e.getMessage(), Toast.LENGTH_SHORT).show();
                  Log.e("databaseerror",""+e.getMessage());
              }
            }
        });
    }
    void insertrecord(){
        mydatabase.mydbopen();
//        Long myrow = mydatabase.insertstudetails(AddStudent.this,name.getText().toString(), course.getSelectedItem().toString(), sem.getSelectedItem().toString(), Integer.valueOf(ed_rollno.getText().toString()), (male.isChecked() ? "Male" : "Female"), phoneno.getText().toString(), email.getText().toString());
        Long myrow = mydatabase.createuser(username.getText().toString(),userid.getText().toString(),phoneno.getText().toString(),password.getText().toString());
        if (myrow>0) {
            Toast.makeText(AppRegister.this, "Records Inserted", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AppRegister.this,AppLogin.class));
            finish();
        }
    }
}
