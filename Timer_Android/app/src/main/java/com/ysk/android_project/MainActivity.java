package com.ysk.android_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button opendrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        opendrawer = findViewById(R.id.opendrawer);

        opendrawer.setOnClickListener(click);

    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //(임시)새타이머 추가 기능 페이지로 이동
             Intent i = new Intent(MainActivity.this,TimerAddActivity.class);
            startActivity(i);
        }
    };
}