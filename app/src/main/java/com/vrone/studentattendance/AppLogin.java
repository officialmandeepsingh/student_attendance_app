package com.vrone.studentattendance;
import com.vrone.studentattendance.splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Random;

import static com.vrone.studentattendance.R.layout.activity_forget_password;

public class AppLogin extends AppCompatActivity {
    
    EditText userid,password;
    Button login,forget;
    MyDatabaseHelper mydb;
    FrameLayout myframe;
    int sms,otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("User Authentication & Authorization");
        setContentView(R.layout.activity_app_login);
        userid=findViewById(R.id.ed_login_userid);
        password=findViewById(R.id.ed_login_password);
        forget=findViewById(R.id.btn_forgetpassword);
        mydb=new MyDatabaseHelper(AppLogin.this);
        login=findViewById(R.id.btn_loginapp);
        myframe=findViewById(R.id.layout_frame);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()) {
                    try {
                        mydb.mydbopen();
                        boolean flag = mydb.checkcredential(userid.getText().toString(), password.getText().toString());
                        if (flag) {
                            startActivity(new Intent(AppLogin.this, MainActivity.class));
                            finish();
                        }
                        else
                            Toast.makeText(AppLogin.this, "You Entered Wrong Credential", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(AppLogin.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    mydb.mydbopen();
                    sms=mydb.getcontactno(userid.getText().toString());
                    if (ActivityCompat.checkSelfPermission(AppLogin.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AppLogin.this, new String[]{Manifest.permission.SEND_SMS}, 1002);
                    }
                    else{
                        sendsms();
                    }
                    //Toast.makeText(AppLogin.this, "Your Number is "+String.valueOf(sms), Toast.LENGTH_SHORT).show();
                }catch (Exception e){

                }

            }
        });

    }
    boolean validation(){
        if((userid.length()<1)&&(password.length()<1) ){
            userid.setError("Enter User ID");
            password.setError("Enter password");
            return false;
        }

        if(userid.length()<1) {
            userid.setError("Enter User ID");
            return false;
        }
        if(password.length()<1) {
            password.setError("Enter password");
            return false;
        }
        else
            return true;

    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1002) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendsms();
                Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
            }
        }
    }
   public void sendsms()
    {
        Random random=new Random();
      otp=random.nextInt(9999);
        Toast.makeText(AppLogin.this, "tel:"+sms, Toast.LENGTH_SHORT).show();
        SmsManager mymanager = SmsManager.getDefault();
        mymanager.sendTextMessage("tel:"+sms,null, "Your Otp Code Is : "+String.valueOf(otp), null, null);
        Toast.makeText(AppLogin.this, "Message Sent", Toast.LENGTH_LONG).show();
        startActivity(new Intent(AppLogin.this,forget_password.class).putExtra("otpcode",otp).putExtra("uid",userid.getText().toString()));
    }

}
