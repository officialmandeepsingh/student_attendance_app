package com.vrone.studentattendance;
import android.content.Intent;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class splash extends AppCompatActivity {
    TextView t1,d1;
    ImageView img1;
    ArrayList<String> mylist;
    MyDatabaseHelper mydatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        t1=findViewById(R.id.textViewappname);
        d1=findViewById(R.id.textViewdeveloper);
        img1=findViewById(R.id.imageViewapplogo);
        mydatabase=new MyDatabaseHelper(splash.this);
        Animation myanimation = AnimationUtils.loadAnimation(this, R.anim.myanimation);
        myanimation.setFillAfter(true);
        img1.startAnimation(myanimation);
//
        mylist=new ArrayList<>();
        Animation mytextanimation = AnimationUtils.loadAnimation(this, R.anim.mytextanim);
//        myanimation.setFillAfter(true);
        t1.startAnimation(mytextanimation);
        d1.startAnimation(mytextanimation);
        try{
            mydatabase.mydbopen();
          mylist= mydatabase.checkuseage();
            //Toast.makeText(splash.this, "Count : "+mylist.get(0), Toast.LENGTH_SHORT).show();
            if(Integer.parseInt(mylist.get(0))==1){
                new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(splash.this,AppLogin.class));finish();
            }}, 5000);

            }
            else{
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(splash.this,AppRegister.class));
                    }}, 5000);

            }
        }catch(Exception e){
            Toast.makeText(this, "Error Occured due to : "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(splash.this,AppRegister.class));
//            }
//        }, 5000);

    }


}
