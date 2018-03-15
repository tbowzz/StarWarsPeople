package com.tbowdev.starwarspeople.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tbowdev.starwarspeople.Model.SwapiClient;
import com.tbowdev.starwarspeople.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SwapiClient.queryAllPeople();
    }
}
