package com.vrone.studentattendance;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ImageButton addstu,viewstu,markatt,viewatt;
    Intent callIntent;
   MyDatabaseHelper mydb;
   FloatingActionButton floatbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        
        floatbtn=findViewById(R.id.fab);
        mydb=new MyDatabaseHelper(MainActivity.this);
        addstu=findViewById(R.id.img_addstudent);
        viewstu=findViewById(R.id.img_view_stu);
        markatt=findViewById(R.id.img_mark_attendance);
        viewatt=findViewById(R.id.img_view_attendance);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        addstu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AddStudent.class));
            }
        });

        viewstu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ViewStudents.class));
            }
        });

        markatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,MarkAttendance.class));
            }
        });

        viewatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,view_attendance.class));
            }
        });
        floatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(MainActivity.this,informtoall.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("Student Registration");
            alert.setMessage("Are You Sure Exit ?");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                   finish();
                }
            });
            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alert.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this,MainActivity.class));
        }else if (id == R.id.nav_addstu) {
            startActivity(new Intent(MainActivity.this,AddStudent.class));
        }else if (id == R.id.nav_viewstulist) {
            startActivity(new Intent(MainActivity.this,ViewStudents.class));
        }/*else if (id == R.id.nav_settings) {
            startActivity(new Intent(MainActivity.this,MyPrefernce.class));
        }*/else if(id==R.id.nav_informtoall){
            startActivity(new Intent(MainActivity.this,informtoall.class));
        }else if(id==R.id.nav_viewattendance){
            startActivity(new Intent(MainActivity.this,view_attendance.class));
        }else if(id==R.id.nav_updateattendance){
            startActivity(new Intent(MainActivity.this,updateAttendance.class));
        }else if(id==R.id.nav_callus){
            if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},5);
            }
            else {
                callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:6239287119"));//change the number
                startActivity(callIntent);
            }

            /*if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED);
            {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},4);
            }*/

        }

//
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==5){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                 callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:6239287119"));//change the number
                startActivity(callIntent);
            }
        }
    }
}
