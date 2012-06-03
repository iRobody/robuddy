package com.irobuddy.robuddy;

import com.irobuddy.R;
import com.irobuddy.R.layout;

import android.app.Activity;
import android.os.Bundle;

public class SuperActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}