package com.ysk.android_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;

import view_adapter.ViewModelAdapter;
import vo.TimerAddVO;

public class TimerAddActivity extends AppCompatActivity {

    Button btn_add, btn_cancel;

    ListView newTimer;
    ViewModelAdapter adapter;

    TimerAddVO vo;
    ArrayList<TimerAddVO> list;

    Intent intent;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_add);

        btn_add = findViewById(R.id.btn_add);
        btn_cancel = findViewById(R.id.btn_cancel);
        newTimer = findViewById(R.id.newTimer);

        vo = new TimerAddVO();
        list = new ArrayList<TimerAddVO>();

        list.add(new TimerAddVO("준비", "시작하기 전 카운트다운","00:01"));
        list.add(new TimerAddVO("운동", "오래운동하기","00:01"));
        list.add(new TimerAddVO("휴식", "오래휴식하기","00:01"));
        list.add(new TimerAddVO("라운드", "1 라운드는 운동 + 휴식입니다.","1"));


        adapter = new ViewModelAdapter(TimerAddActivity.this,R.layout.timer_item, list, newTimer);
        newTimer.setAdapter(adapter);

        //쉐어드 프리페어
        pref = getSharedPreferences("SHARE",MODE_PRIVATE);

        if(pref.getInt("check",0) != 0) {
            list.get(pref.getInt("index", 0)).setTimer(pref.getString("time","00:00"));

            Toast.makeText(TimerAddActivity.this,list.get(1).getTimer(),Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(TimerAddActivity.this,"값이 없음",Toast.LENGTH_SHORT).show();
        }

        //값 불러오기



        //추가버튼
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //시간 추가 페이지로 이동

            }
        });

        //취소버튼
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        newTimer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //리스트뷰 선택된 값 가져오기
                TimerAddVO vo = list.get(i);

                //선택된 리스트뷰 타이틀이 라운드 => 라운드 설정 페이지로 이동.
                if(vo.getTitle().equals("라운드")){
                    intent = new Intent(TimerAddActivity.this,RoundSetActivity.class);
                    intent.putExtra("list",list);
                    startActivity(intent);

                }else{
                    intent = new Intent(TimerAddActivity.this,TimeSetActivity.class);
                    intent.putExtra("title",vo.getTitle());
                    intent.putExtra("list",list);
                    startActivity(intent);
                }
            }
        });

    }//onCreate()


}