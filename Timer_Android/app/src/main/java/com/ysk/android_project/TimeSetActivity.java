package com.ysk.android_project;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import vo.TimerAddVO;

public class TimeSetActivity extends AppCompatActivity {



    EditText min_edit,sec_edit;
    Button btn_result, btn_back;
    TextView p_title;

    Intent intent = null;
    String title;

    ArrayList<TimerAddVO> list;

    SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_set);

        min_edit = findViewById(R.id.min_edit);
        sec_edit = findViewById(R.id.sec_edit);

        btn_back = findViewById(R.id.btn_back);
        btn_result = findViewById(R.id.btn_result);

        p_title = findViewById(R.id.p_title);

        //intent 값 가져오기
        intent = getIntent();

        //title 값 저장
        title = (String) intent.getStringExtra("title");
        list = (ArrayList<TimerAddVO>)intent.getSerializableExtra("list");

        //페이지 title명  변경
        p_title.setText(title);

        btn_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               intent = new Intent(TimeSetActivity.this,TimerAddActivity.class);

               //값이.. 아무것도 없을 때 오류 방지 (00:01를 기본으로 설정)
                if(min_edit.length() < 1){
                    min_edit.setText("00");
                }

                if(sec_edit.length() < 1){
                    sec_edit.setText("01");
                }

               //분,초가 1자리면 앞에 0을 붙여준다.
               if(min_edit.length() == 1){
                   min_edit.setText("0" + min_edit.getText());
               }

               if(sec_edit.length() == 1){
                   sec_edit.setText("0" + sec_edit.getText());
               }

               if(Integer.parseInt(String.valueOf(min_edit.getText())) >= 60){
                    Toast.makeText(TimeSetActivity.this,"분은 0이상 60미만으로 설정해주세요.",Toast.LENGTH_SHORT).show();
                    min_edit.requestFocus();
                    return;
               }

                if(Integer.parseInt(String.valueOf(sec_edit.getText())) >= 60){
                    Toast.makeText(TimeSetActivity.this,"초는 0이상 60미만으로 설정해주세요.",Toast.LENGTH_SHORT).show();
                    sec_edit.requestFocus();
                    return;
                }

               String time = min_edit.getText() + ":" + sec_edit.getText();

                for(int i = 0; i < list.size(); i++ ){

                    if(list.get(i).getTitle().equals(title)){
                        list.get(i).setTimer(time);

                        Toast.makeText(TimeSetActivity.this,list.get(i).getTitle(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(TimeSetActivity.this,time,Toast.LENGTH_SHORT).show();
                        Toast.makeText(TimeSetActivity.this,list.get(2).getSub_title(),Toast.LENGTH_SHORT).show();

                        pref = getSharedPreferences("SHARE",MODE_PRIVATE);

                        SharedPreferences.Editor save = pref.edit();

                        save.putInt("check", 1);
                        save.putInt("index", i);
                        save.putString("time",time);
                        save.commit();

                        intent.putExtra("list",list);

                    }

                }
                startActivity(intent);
                finish();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




    }


    }


