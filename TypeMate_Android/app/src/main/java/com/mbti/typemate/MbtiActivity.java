package com.mbti.typemate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;



import java.util.ArrayList;

public class MbtiActivity extends Activity implements OnClickListener {
    
    Intent intent, intent_test;


    //여러개의 버튼을 배열로 처리
    Button[] btn_mbti = new Button[16];


    //각각 다르게 출력할 스트링을 넣어둘리스트
    ArrayList<String> btn_mbtilist;
    Button btn_mbti_test, btn_mbti_exit;

    //joinActivity에서 다시 값을 보내주기 위한 변수(MBTI 타입 선택 후 JoinActivity가 초기화 되는 것 방지)
    String id, pwd, repwd, name, tel, nickname, gender, birth, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mbti);

        btn_mbtilist = new ArrayList<String>();

        btn_mbti[0] = findViewById(R.id.btn_istj);
        btn_mbti[1] = findViewById(R.id.btn_isfj);
        btn_mbti[2] = findViewById(R.id.btn_infj);
        btn_mbti[3] = findViewById(R.id.btn_intj);

        btn_mbti[4] = findViewById(R.id.btn_istp);
        btn_mbti[5] = findViewById(R.id.btn_isfp);
        btn_mbti[6] = findViewById(R.id.btn_infp);
        btn_mbti[7] = findViewById(R.id.btn_intp);

        btn_mbti[8] = findViewById(R.id.btn_estp);
        btn_mbti[9] = findViewById(R.id.btn_esfp);
        btn_mbti[10] = findViewById(R.id.btn_enfp);
        btn_mbti[11] = findViewById(R.id.btn_entp);

        btn_mbti[12] = findViewById(R.id.btn_estj);
        btn_mbti[13] = findViewById(R.id.btn_esfj);
        btn_mbti[14] = findViewById(R.id.btn_enfj);
        btn_mbti[15] = findViewById(R.id.btn_entj);

        btn_mbti_test = findViewById(R.id.btn_mbti_test);
        btn_mbti_exit = findViewById(R.id.btn_mbti_exit);


        //JoinActivity에서 값 받기(MBTI 타입 선택 후 JoinActivity가 초기화 되는 것 방지)

        final Intent[] intent = {getIntent()};

        id = intent[0].getExtras().getString("id");
        pwd = intent[0].getExtras().getString("pwd");
        repwd = intent[0].getExtras().getString("repwd");
        nickname = intent[0].getExtras().getString("nickname");
        gender = intent[0].getExtras().getString("genders");
        birth = intent[0].getExtras().getString("birth");
        name = intent[0].getExtras().getString("name");
        tel = intent[0].getExtras().getString("tel");

        //각 mbti유형 클릭 감지

        //배열로 등록한 버튼 클릭 감지
        for (int i = 0; i < 16; i++) {

            btn_mbti[i].setTag(btn_mbti[i].getText().toString());
    /*-----------------------------수정(08/11)------------------------------*/
            btn_mbti[i].setTextColor(Color.parseColor("#888888"));
    /*-----------------------------수정(08/11)------------------------------*/
            btn_mbti[i].setOnClickListener(this);

            btn_mbtilist.add(btn_mbti[i].getText().toString());

        }

        //검사사이트로 이동
        btn_mbti_test.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                /*-----------------------------수정(08/11)------------------------------*/
                intent_test = new Intent(MbtiActivity.this, MbtiTestActivity.class);

                intent_test.putExtra("mbti",""+type);
                intent_test.putExtra("id", ""+id);
                intent_test.putExtra("pwd", ""+pwd);
                intent_test.putExtra("repwd", ""+repwd);
                intent_test.putExtra("nickname", ""+nickname);
                intent_test.putExtra("gender",""+gender);
                intent_test.putExtra("birth", ""+birth);
                intent_test.putExtra("name",""+name);
                intent_test.putExtra("tel",""+tel);

                startActivity(intent_test);
                /*-----------------------------수정(08/11)------------------------------*/
                finish();

            }
        });

        //뒤로가기
        btn_mbti_exit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });



    }//onCreate()

    //선택한 버튼 출력
    @Override
    public void onClick(View view) {

        // 클릭된 뷰를 버튼으로 받아옴
        Button newButton = (Button) view;

        // 향상된 for문을 사용, 클릭된 버튼을 찾아냄
        for (Button tempButton : btn_mbti) {

            // 클릭된 버튼을 찾았으면
            if (tempButton == newButton) {

                // 위에서 저장한 버튼의 타입 태그로 가져옴
                String type = (String)view.getTag();

                intent = new Intent(MbtiActivity.this, JoinActivity.class);

                //JoinActivity에 들어갈 항목 다시 보내기

                intent.putExtra("mbti",""+type);
                intent.putExtra("id", ""+id);
                intent.putExtra("pwd", ""+pwd);
                intent.putExtra("repwd", ""+repwd);
                intent.putExtra("nickname", ""+nickname);
                intent.putExtra("gender",""+gender);
                intent.putExtra("birth", ""+birth);
                intent.putExtra("name",""+name);
                intent.putExtra("tel",""+tel);

                startActivity(intent);
                finish();

            }
        }
    }
}