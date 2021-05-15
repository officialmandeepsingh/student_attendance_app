package com.vrone.studentattendance;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.ContextMenu;
        import android.view.LayoutInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ListView;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.ArrayList;
        import java.util.List;
        import java.util.TreeMap;

public class ViewStudents extends AppCompatActivity {
    ListView mylist;
    ArrayList<String> mycourses,mysemesters,namelist;
    ArrayList<Integer> rollnolist;
    Spinner course,sem;
    Button fetchbtn;
    MyDatabaseHelper mydb;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view_students);
        this.setTitle("View Student");
            mylist = findViewById(R.id.lv_viewstudents);
            course = findViewById(R.id.spinner_course);
            sem = findViewById(R.id.spinner_sem);
            fetchbtn = findViewById(R.id.btn_fetch_vstu);
            mydb=new MyDatabaseHelper(ViewStudents.this);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            registerForContextMenu(mylist);
        mycourses = new ArrayList<>();
            namelist=new ArrayList<>();
            rollnolist=new ArrayList<>();
            mycourses.add("Choose Student Course");
            mycourses.add("MCA");
            mycourses.add("MBA");
            mycourses.add("BCA");
            mycourses.add("BBA");
            mycourses.add("B.Com");
            mycourses.add("B.Tech(CSE)");

            course.setAdapter(new ArrayAdapter<>( ViewStudents.this,android.R.layout.simple_spinner_item,mycourses));
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
            mysemesters.add("All");
        sem.setAdapter(new ArrayAdapter<>( ViewStudents.this,android.R.layout.simple_spinner_item,mysemesters));

            fetchbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{

                        mydb.mydbopen();
                        TreeMap mygroup;
                        namelist.clear();
                        rollnolist.clear();

                        if(sem.getSelectedItem().toString().equalsIgnoreCase("All")){
                            mygroup = mydb.fetchstudentdetails(ViewStudents.this,course.getSelectedItem().toString());
                        }
                        else{
                            mygroup = mydb.fetchstudentdetails(ViewStudents.this,course.getSelectedItem().toString(),sem.getSelectedItem().toString());
                        }

                        namelist = (ArrayList<String>) mygroup.get("nameslist");
                        rollnolist = (ArrayList<Integer>) mygroup.get("rollnolist");
                        mydb.mydbclose();
                        /*if(mygroup.size()<0){
                            Toast.makeText(ViewStudents.this, "No record found", Toast.LENGTH_SHORT).show();
                        }
                        if(namelist.size()>0) {
                            Toast.makeText(ViewStudents.this, "Total Student in " + course.getSelectedItem().toString() + " " + sem.getSelectedItem().toString() + " is : " + String.valueOf(namelist.size()), Toast.LENGTH_SHORT).show();
                        }*/
                        mylist.setAdapter(new ArrayAdapter<String>(ViewStudents.this,android.R.layout.simple_list_item_1,namelist));

                    }catch(Exception e) {
                       Toast.makeText(ViewStudents.this, "No Record", Toast.LENGTH_LONG).show();
                    }

//
//
                }
            });
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                position=mylist.getSelectedItemPosition();
                Toast.makeText(ViewStudents.this, String.valueOf(rollnolist.get(i)), Toast.LENGTH_SHORT).show();
            }
        });
    }

    class myadapter extends ArrayAdapter<String>{

        public myadapter(Context context, int resource, ArrayList<String> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater myinflator= (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myrow= (View) myinflator.inflate(R.layout.xml_viewstudent_lv,parent,false);
            TextView myrollno=myrow.findViewById(R.id.xml_lv_Rollno);
            TextView myname=myrollno.findViewById(R.id.xml_lv_Stuname);
            return  myrow;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(1,1,1,"Edit");
       // menu.add(1,2,2,"View Profile");
    }

    @Override
    public boolean onContextItemSelected( MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = menuInfo.position;
        if(item.getItemId()==1){
            Toast.makeText(this, "Edit Student"+ rollnolist.get(position), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ViewStudents.this,AddStudent.class).putExtra("rollno", rollnolist.get(position)));
        }
        else if(item.getItemId()==2){
            Toast.makeText(this, "View Profile", Toast.LENGTH_SHORT).show();
        }
        return true;
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

