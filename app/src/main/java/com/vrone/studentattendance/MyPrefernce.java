package com.vrone.studentattendance;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class MyPrefernce extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.mysharedprefernce);
    }
}
