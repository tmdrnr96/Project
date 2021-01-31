package com.ysk.android_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import parse.TimerAdd;
import parse.ViewModelAdapter;
import vo.TimerAddVO;

public class TimerAddActivity extends AppCompatActivity {

    Button btn_add, btn_cancel;

    ListView newTimer;
    TimerAdd timeradd;
    ViewModelAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_add);

        btn_add = findViewById(R.id.btn_add);
        btn_cancel = findViewById(R.id.btn_cancel);
        newTimer = findViewById(R.id.newTimer);
        
        timeradd = new TimerAdd();

        adapter = new ViewModelAdapter(TimerAddActivity.this,R.layout.timer_item, timeradd.vo_list_in(),newTimer);

        newTimer.setAdapter(adapter);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //시간 추가 페이지로 이동
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }//onCreate()


}