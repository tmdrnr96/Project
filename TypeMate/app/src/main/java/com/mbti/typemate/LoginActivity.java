package com.mbti.typemate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mbti.typemate.async.SpringTask;
import com.mbti.typemate.util.Util;
import com.mbti.typemate.vo.UserVO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends Activity {


    Button join_btn, sign_in_btn, find_account_btn, find_pwd_btn;
    EditText edit_id, edit_pwd;
    String loginId, loginPwd;

    String param = "";//서버로 보낼 파라미터값을 담을 변수

    String resultStr = "";//async에서 처리한값을 반환받는 변수

    //서버에서 반환받은 값에서 JsonObject로 하나씩 값을 꺼내담을 변수들
    String result = "";
    String nickname = "";
    String gender = "";
    String birth = "";
    String mbti = "";

    boolean checkCnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        join_btn = findViewById(R.id.join_btn);
        sign_in_btn = findViewById(R.id.sign_in_btn);
        find_account_btn = findViewById(R.id.find_account_btn);
        find_pwd_btn = findViewById(R.id.find_pwd_btn);

        edit_id = findViewById(R.id.edit_id);
        edit_pwd = findViewById(R.id.edit_pwd);

        join_btn.setOnClickListener(btnEvt);
        sign_in_btn.setOnClickListener(btnEvt);
        find_account_btn.setOnClickListener(btnEvt);
        find_pwd_btn.setOnClickListener(btnEvt);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        //초기값 설정 키값만 지정하고 기본값 null
        loginId = auto.getString("inputId",null);
        loginPwd = auto.getString("inputPwd", null);

        //값을 가져왔을때 null이 아니고 Id와 Pwd가 DB에 존재한다면 자동으로 액티비티 이동
        if (loginId != null && loginPwd != null){
            //서버에 보낼 파라미터
            param = "id=" + loginId + "&pwd=" + loginPwd;
            //서버에서 유저정보를 받아오는 메서드 호출
            getUserInfo();
            if (result.equals("YES")){
                //메인 액티비티로 이동하게끔 하는 메서드 호출
                startMainIntent();
            }else{
                return;
            }
        }

        //아이디찾기에서 돌아왔을때
            Intent findIdIntent = getIntent();
            Bundle bundle = findIdIntent.getExtras();
            //해당 번들에 값이 있을때만
        if (bundle != null){
            String findId = bundle.getString("id");
            edit_id.setText(findId);
            edit_pwd.setFocusableInTouchMode(true);
            edit_pwd.requestFocus();//비번 바로 입력하게끔 focus()
        }
    }//onCreate()

    View.OnClickListener btnEvt = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.join_btn :
                    Intent joinIntent = new Intent(LoginActivity.this, JoinActivity.class);
                    startActivity(joinIntent);
                    //finish(); //살려도 되는데 뒤로가기 했을때 부자연스러울거 같아서 주석처리 했습니다
                    break;

                case R.id.sign_in_btn:
                    String id = edit_id.getText().toString();
                    String pwd = edit_pwd.getText().toString();

                    param = "id=" + id + "&pwd=" + pwd;

                    //서버와 통신하여 유저의 정보를 가져오는 메서드
                    getUserInfo();


                    //추후 로그인값 검증해서 if문으로 묶어줄 예정
                    if (result.equals("YES")){

                        Toast.makeText(LoginActivity.this, "환영합니다", Toast.LENGTH_SHORT).show();

                        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor autoLogin = auto.edit();
                        autoLogin.putString("inputId", id);
                        autoLogin.putString("inputPwd", pwd);
                        autoLogin.commit();

                        //0807
                        // SharedPreferences에 page 1로 저장
                        SharedPreferences page = getSharedPreferences("page", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editorPage = page.edit();
                        editorPage.putInt("curPage",1);
                        editorPage.commit();

                        startMainIntent();
                    }else{
                        Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.find_account_btn :
                    Intent findId_intent = new Intent(LoginActivity.this, FindAccountActivity.class);
                    startActivity(findId_intent);
                    break;

                case R.id.find_pwd_btn :
                    Intent findPwd_intent = new Intent(LoginActivity.this, FindPwdActivity.class);
                    startActivity(findPwd_intent);
                    break;
            }//switch
        }
    };//onClickListener

    //똑같은 코드가 반복되는일이 있어서 메서드화 시켜줌
    public void startMainIntent(){
        //로그인 완료 후 메인으로 화면전환
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        //번들에 데이터 담고
        Bundle bundle = new Bundle();
        bundle.putString("nickname", nickname);
        bundle.putString("gender", gender);
        bundle.putString("birth", birth);
        bundle.putString("mbti", mbti);
        bundle.putString("user_id", loginId);
        bundle.putString("user_pwd", loginPwd);

        //인텐트에 담은 뒤
        intent.putExtras(bundle);
        //데이터와 함께 액티비티 전환
        startActivity(intent);
        finish();//로그인 페이지는 없애줌
    }//startMainIntent()

    //서버와 통신하여 유저의 정보를 가져오는 메서드
    public void getUserInfo(){

        //서버에서 받아온 JSON타입의 값을 추출하는 과정
        try {
            resultStr = new SpringTask(Util.SERVER_IP_LOGIN, LoginActivity.this).execute(param).get();
            JSONObject jsonObject = new JSONObject(resultStr);
            result =  jsonObject.getString("result");
            JSONObject userObject = jsonObject.getJSONObject("user");
            nickname = userObject.getString("nickname");
            gender = userObject.getString("gender");
            birth = userObject.getString("birth");
            mbti = userObject.getString("mbti");
        } catch (Exception e) {
            Log.i("CATCH", e.getMessage());
        }
    }//getUserInfo

    @Override
    public void onBackPressed() {
        if (!checkCnt) {
            Toast.makeText(getApplicationContext(), "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            checkCnt = true;
            handler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
        }
    }//onBackPressed()

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
                    checkCnt = false;
        }
    };
}