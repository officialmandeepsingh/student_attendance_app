package com.vrone.studentattendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.TreeMap;


public class MyDatabaseHelper extends SQLiteOpenHelper {
    SQLiteDatabase mydb;
    public MyDatabaseHelper(Context context) {
        super(context,"stuattendancedb",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLite) {
        String query = "create table if not exists studentinfo(name text, course text,semester text,rollno Integer Primary Key,gender text,phone text,email text)";
        String query2 ="create table if not exists appuser(name text, userid text Primary Key,phone text,password text)";
        String query3 = "create table if not exists stu_attendance(stu_rollno Integer , stu_name text, course text,semester text,date_of_attendance Date,status text)";
        try
        {
           sqLite.execSQL(query);
            sqLite.execSQL(query2);
            sqLite.execSQL(query3);
        }
        catch (Exception e)
        {
            Log.e("databaseproject", "Error creating table due to " + e.getMessage(), e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old, int i1) {

    }
    public void mydbopen()
    {
        mydb=this.getReadableDatabase();
    }
    public void mydbclose()
    {
        mydb.close();
    }

    public  long insertstudetails(Context c,String name , String course, String semester , int rollno , String gender , String phone , String email ){

        ContentValues cursor=new ContentValues();
        cursor.put("name",name);
        cursor.put("course",course);
        cursor.put("semester",semester);
        cursor.put("rollno",rollno);
        cursor.put("gender",gender);
        cursor.put("phone",phone);
        cursor.put("email",email);
        Toast.makeText(c, "Insertion....... ", Toast.LENGTH_SHORT).show();
        return mydb.insert("studentinfo",null,cursor);

    }
    public TreeMap fetchstudentdetails(Context c,String course,String sem)
    {

        Cursor myresult = mydb.rawQuery("select * from studentinfo where course=? and semester=?", new String[]{course,sem});
        ArrayList<String> nameslist=new ArrayList<>();
        ArrayList<Integer> rollno=new ArrayList<>();
        ArrayList<String> phnno=new ArrayList<>();
        TreeMap mygroup = new TreeMap();
        if(myresult.moveToNext())
        {
            do {
                nameslist.add(myresult.getString(myresult.getColumnIndex("name")));
                rollno.add(myresult.getInt(myresult.getColumnIndex("rollno")));
                phnno.add(myresult.getString(myresult.getColumnIndex("phone")));
            }
            while (myresult.moveToNext());
            mygroup.put("nameslist", nameslist);
            mygroup.put("rollnolist", rollno);
            mygroup.put("phnno", phnno);
        }
        else
        {
            Toast.makeText(c, "No Record Found in Database", Toast.LENGTH_SHORT).show();
//            nameslist.clear();
//            mygroup.put("nameslist", nameslist);
        }
        return mygroup;

    }

    public TreeMap fetchstudentdetails(Context c,String course)
    {

        Cursor myresult = mydb.rawQuery("select * from studentinfo where course=? ", new String[]{course});
        ArrayList<String> nameslist=new ArrayList<>();

        ArrayList<Integer> rollno=new ArrayList<>();
        TreeMap mygroup = new TreeMap();
        if(myresult.moveToNext())
        {
            do {
                nameslist.add(myresult.getString(myresult.getColumnIndex("name")));
                rollno.add(myresult.getInt(myresult.getColumnIndex("rollno")));

            }
            while (myresult.moveToNext());
            mygroup.put("nameslist", nameslist);

            mygroup.put("rollnolist", rollno);
        }
        else
        {
            Toast.makeText(c, "No Record Found in Database", Toast.LENGTH_SHORT).show();
//            nameslist.clear();
//            mygroup.put("nameslist", nameslist);
        }
        return mygroup;

    }

    public ArrayList<String> fetchstudentdetails(Context c,int rollno)
    {
        ArrayList<String> studentrecord = new ArrayList<>();
        try {
            Cursor myresult = mydb.rawQuery("select * from studentinfo where rollno=?", new String[]{String.valueOf(rollno)});
            if (myresult.moveToNext()) {
                studentrecord.add(myresult.getString(0));
                studentrecord.add(myresult.getString(1));
                studentrecord.add(myresult.getString(2));
                studentrecord.add(String.valueOf(myresult.getInt(3)));
                studentrecord.add(myresult.getString(4));
                studentrecord.add(myresult.getString(5));
                studentrecord.add(myresult.getString(6));
            } else {
                studentrecord.clear();
            }
        }catch (Exception e){
            Toast.makeText(c, "Error : "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return studentrecord;
    }


    //public int StuUpdate(Context c, String name, String course, String semester, int rollno, String gender, String phone, String email, String roll)
    public boolean StuUpdate(String name,String course,String semester,String rollno,String gender,String phone,String email,String roll)
    {
        //Toast.makeText(c, "In Update Function ", Toast.LENGTH_SHORT).show();
        ContentValues cursor = new ContentValues();
        try {
            cursor.put("name",name);
            cursor.put("course",course);
            cursor.put("semester",semester);
            cursor.put("rollno",rollno);
            cursor.put("gender",gender);
            cursor.put("phone",phone);
            cursor.put("email",email);
        }catch (Exception e){
            Log.e("database","Error Occured Due to : "+e.getMessage());
        }
     //    Toast.makeText(c, "Updtion ....", Toast.LENGTH_SHORT).show();
        return mydb.update("studentinfo",cursor,"rollno=?",new String[]{roll}) >0;
    }
    public ArrayList<String> fetchphnnumber(Context c,String course,String sem)
    {

        Cursor myresult = mydb.rawQuery("select phone from studentinfo where course=? and semester=?", new String[]{course,sem});
        ArrayList<String> phnlist=new ArrayList<>();
        if(myresult.moveToNext())
        {
            do {
                phnlist.add(myresult.getString(0));
            }
            while (myresult.moveToNext());
        }
        else
        {
            Toast.makeText(c, "No Record Found in Database", Toast.LENGTH_SHORT).show();
            phnlist.add("1");
//            nameslist.clear();
//            mygroup.put("nameslist", nameslist);
        }
        return phnlist;
    }

    public  long createuser(String name,String userid,String phone,String password){

        ContentValues cursor=new ContentValues();
        cursor.put("name",name);
       cursor.put("userid",userid);
       cursor.put("phone",phone);
       cursor.put("password",password);

        return mydb.insert("appuser",null,cursor);

    }
    public ArrayList<String> checkuseage(){
        ArrayList<String> mylist=new ArrayList<>();
        Cursor myresult = mydb.rawQuery("select count(*) from appuser",null,null);
        if (myresult.moveToNext()) {
            mylist.add(myresult.getString(0));
        }
        else {
            mylist.clear();
        }
        return mylist;
    }
    public boolean checkcredential(String userid,String password){

        Cursor myresult=mydb.rawQuery("select * from appuser where userid=? and password=?",new String[]{userid,password});
        if (myresult.moveToNext()) {
        return true;
        }
        else{
            return false;
        }
    }

    public  long mark_attendance(Context c, int stu_rollno  , String stu_name, String course, String semester, String date_of_attendance,String status ){

        ContentValues cursor=new ContentValues();
        cursor.put("stu_rollno",stu_rollno);
        cursor.put("stu_name",stu_name);
        cursor.put("course",course);
        cursor.put("semester",semester);
        cursor.put("date_of_attendance",date_of_attendance);
        cursor.put("status",status);

        Toast.makeText(c, "Insertion....... ", Toast.LENGTH_SHORT).show();
        return mydb.insert("stu_attendance",null,cursor);

    }
//    stu_attendance(stu_rollno Integer , stu_name text, course text,semester text,date_of_attendance Date,status text)";
   public int total_lectures(String sname,String course,String sem){
     Cursor lectures=mydb.rawQuery("select count(date_of_attendance) from stu_attendance where stu_name=? and course=? and semester=?",new String[]{sname,course,sem});
     int lects=0;
     String o=lectures.getColumnName(0);
     if(lectures.moveToNext()) {
         Log.i("total", "Get value from db is : " + String.valueOf(lectures.getString(lectures.getColumnIndex("count(date_of_attendance)"))));
        lects=Integer.parseInt(lectures.getString(lectures.getColumnIndex("count(date_of_attendance)")));
     }

     return lects;

    }
    public int present_lectures(String sname,String course,String sem){
        Cursor lectures=mydb.rawQuery("select count(status) from stu_attendance where stu_name=? and status=? and course=? and semester=?",new String[]{sname,"present",course,sem});
//        String o=lectures.getColumnName(0);
        int lects=0;
        if(lectures.moveToNext()) {
            lects=Integer.parseInt(lectures.getString(lectures.getColumnIndex("count(status)")));
            Log.i("total", "Present in Lectures : " + String.valueOf(lectures.getString(lectures.getColumnIndex("count(status)"))));
        }
        return lects;
    }
    public int absent_lectures(String sname,String course,String sem){
        Cursor lectures=mydb.rawQuery("select count(status) from stu_attendance where stu_name=? and status=? and course=? and semester=?",new String[]{sname,"absent",course,sem});
//        String o=lectures.getColumnName(0);
        int lects=0;
        if(lectures.moveToNext()) {
            lects=Integer.parseInt(lectures.getString(lectures.getColumnIndex("count(status)")));
            Log.i("total", "Present in Lectures : " + String.valueOf(lectures.getString(lectures.getColumnIndex("count(status)"))));
        }
        return lects;
    }


    public TreeMap attendance_record(Context c,String course,String sem){
        ArrayList<String> datelist=new ArrayList<>();
        ArrayList<Integer> total_lect=new ArrayList<>();
        ArrayList<Integer> present_lect=new ArrayList<>();
        ArrayList<Integer> absent_lect=new ArrayList<>();
        TreeMap mygroup = new TreeMap();
       try{
        Cursor lectures=mydb.rawQuery("select * from stu_attendance where course=? and semester=? ",new String[]{course,sem});
        if (lectures.moveToNext()){
            do{
                datelist.add(lectures.getString(lectures.getColumnIndex("date_of_attendance")));
//                present_lect.add(lectures.getInt(lectures.getColumnIndex("status")));
            }while (lectures.moveToNext());
            mygroup.put("datelist",datelist);
//            mygroup.put("present",present_lect);
        }
       }catch (Exception e){
           Toast.makeText(c, "Error occured "+e.getMessage(), Toast.LENGTH_SHORT).show();
       }
        return mygroup;

    }
    public ArrayList date_of_attendance(String course,String sem){
        ArrayList<String> datelist=new ArrayList<>();
        Cursor lectures=mydb.rawQuery("select date_of_attendance from stu_attendance where course=? and status=?",new String[]{course,sem});
//        String o=lectures.getColumnName(0);
        if(lectures.moveToNext()) {
            do{
                Log.i("demo",""+lectures.getString(lectures.getColumnIndex("date_of_attendance")));
            datelist.add(lectures.getString(lectures.getColumnIndex("date_of_attendance")));
        }while (lectures.moveToNext());
        }
        return datelist;
    }
    public  int getcontactno(String uname){
        int smsnumber=0;
        Cursor myresult=mydb.rawQuery("select phone from appuser where userid=?",new String[]{uname});
        if(myresult.moveToNext()){
           smsnumber=Integer.parseInt(myresult.getString(0));
        }
        return smsnumber;
    }
    public boolean updatepassword(String uid,String newpass)
    {
        //Toast.makeText(c, "In Update Function ", Toast.LENGTH_SHORT).show();
        ContentValues cursor = new ContentValues();
        try {
            cursor.put("password",newpass);
        }catch (Exception e){
            Log.e("database","Error Occured Due to : "+e.getMessage());
        }
//            Toast.makeText(c, "Updtion ....", Toast.LENGTH_SHORT).show();
        return mydb.update("appuser",cursor,"userid=?",new String[]{uid}) >0;
    }
    public boolean check_attendace_marker(String dt,String course,String sem){
        Cursor myresult=mydb.rawQuery("select * from stu_attendance where course =? and semester =? and date_of_attendance =?",new String[]{course,sem,dt});
        if (myresult.getCount()>0){
            return false;
        }
        else {
            return true;
        }
    }

    public int max_rollno(String course,String semester){
        int rollnumber = 0;
        Cursor myresult=mydb.rawQuery("select max(rollno) from studentinfo where course =? and semester=?",new String[]{course,semester});
        if(myresult.moveToNext()){
            rollnumber=myresult.getInt(0);

        }
        return rollnumber+1;
    }

    public ArrayList<String> getuserdetails(){
        ArrayList<String> user=new ArrayList<>();
        Cursor myresult=mydb.rawQuery("select name,phone from appuser",null,null);
        user.clear();
        if(myresult.moveToNext()){
            user.add(myresult.getString(myresult.getColumnIndex("name")));
            user.add(myresult.getString(myresult.getColumnIndex("phone")));
        }
        else{
            user.clear();
        }
        return user;
    }

    public ArrayList<String> getstudentdates(String rollno){
        ArrayList<String> student=new ArrayList<>();
        // stu_attendance(stu_rollno Integer , stu_name text, course text,semester text,date_of_attendance Date,status text)
        Cursor myresult=mydb.rawQuery("select * from stu_attendance where stu_rollno=? ",new String[]{rollno});
        student.clear();
        while (myresult.moveToNext()){
           student.add(myresult.getString(myresult.getColumnIndex("date_of_attendance")));
        }
        return student;
    }

    public ArrayList<String> getattrec(String rollno, String date){
        ArrayList<String> studentdet=new ArrayList<>();
        // stu_attendance(stu_rollno Integer , stu_name text, course text,semester text,date_of_attendance Date,status text)
        Cursor myresult=mydb.rawQuery("select * from stu_attendance where stu_rollno=? and date_of_attendance=?",new String[]{rollno,date});
        studentdet.clear();
        while (myresult.moveToNext()){
//            studentdet.add(myresult.getString(myresult.getColumnIndex("date_of_attendance")));
            studentdet.add(myresult.getString(myresult.getColumnIndex("stu_rollno")));
            studentdet.add(myresult.getString(myresult.getColumnIndex("stu_name")));
            studentdet.add(myresult.getString(myresult.getColumnIndex("course")));
            studentdet.add(myresult.getString(myresult.getColumnIndex("semester")));
            studentdet.add(myresult.getString(myresult.getColumnIndex("date_of_attendance")));
            studentdet.add(myresult.getString(myresult.getColumnIndex("status")));
        }
        return studentdet;
    }

    public int update_attendance(Context c, String r, String d, String stu_rollno  , String stu_name, String course, String semester, String date_of_attendance, String status ){

        ContentValues cursor=new ContentValues();
        cursor.put("stu_rollno",stu_rollno);
        cursor.put("stu_name",stu_name);
        cursor.put("course",course);
        cursor.put("semester",semester);
        cursor.put("date_of_attendance",date_of_attendance);
        cursor.put("status",status);
        Toast.makeText(c, ""+r+" "+d, Toast.LENGTH_SHORT).show();
        return mydb.update("stu_attendance",cursor,"stu_rollno=? and date_of_attendance =?",new String[]{r,d}) ;

    }
}
