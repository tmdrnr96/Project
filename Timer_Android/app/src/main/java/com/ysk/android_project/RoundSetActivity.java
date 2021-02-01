package com.ysk.android_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import vo.TimerAddVO;

public class RoundSetActivity extends AppCompatActivity {

    Button btn_back,btn_complet;
    EditText round_edit;

    ArrayList<TimerAddVO> list;
    Intent intent = null;

    String round;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_set);

        btn_back = findViewById(R.id.btn_back);
        btn_complet = findViewById(R.id.btn_complet);
        round_edit =findViewById(R.id.round_edit);


        //intent 값 가져오기
        intent = getIntent();
        list = (ArrayList<TimerAddVO>)intent.getSerializableExtra("list");

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_complet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(RoundSetActivity.this,TimerAddActivity.class);

                if(round_edit.length() < 1){
                    round_edit.setText("1");
                }

                round = String.valueOf(round_edit.getText());

                for(int i = 0; i < list.size(); i++ ){

                    if(list.get(i).getTitle().equals("라운드")){
                        list.get(i).setTimer(round);
                        intent.putExtra("index",i);
                        intent.putExtra("list",list);
                    }

                }
                startActivity(intent);
                finish();
            }
        });


    }
}