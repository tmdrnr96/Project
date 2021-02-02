package com.ysk.android_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button opendrawer;
    SharedPreferences pref;

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
            pref = getSharedPreferences("SHARE",MODE_PRIVATE);
            SharedPreferences.Editor save = pref.edit();

            save.putString("time1","00:01");
            save.putString("time2","00:01");
            save.putString("time3","00:01");
            save.putString("time4","1");

            save.commit();

             Intent i = new Intent(MainActivity.this,TimerAddActivity.class);
            startActivity(i);
        }
    };
}