package com.vrone.studentattendance;

import com.vrone.studentattendance.AppLogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class forget_password extends AppCompatActivity {
    FrameLayout myframe;
    EditText newpass,confirmpass,otpcode;
    Button verify,updatepass;
    int otpsend;
    String uid;
    MyDatabaseHelper mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        this.setTitle("User Password Recovery");
        myframe=findViewById(R.id.layout_frame);
        myframe.setVisibility(View.GONE);
        otpcode=findViewById(R.id.ed_fp_otpverify);
        newpass=findViewById(R.id.ed_fp_newpass);
        confirmpass=findViewById(R.id.ed_fp_conpass);
        mydb=new MyDatabaseHelper(forget_password.this);
        verify=findViewById(R.id.btn_fp_otpverify);
        updatepass=findViewById(R.id.btn_fp_chngpass);
        if(getIntent().getExtras()!=null){
            uid=getIntent().getExtras().getString("uid");
            otpsend=getIntent().getExtras().getInt("otpcode");
        }
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(otpcode.getText().toString()) ==otpsend){ myframe.setVisibility(View.VISIBLE);}
                else{myframe.setVisibility(View.GONE);}
            }


        });
        updatepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mydb.mydbopen();
                boolean res=mydb.updatepassword(uid,newpass.getText().toString());
                if(res){
                    Toast.makeText(forget_password.this, "Update", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(forget_password.this,AppLogin.class));
                }
            }
        });
        Toast.makeText(this, "OTP sent", Toast.LENGTH_SHORT).show();
    }
}
