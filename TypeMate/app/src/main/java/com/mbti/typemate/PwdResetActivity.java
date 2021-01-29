package com.mbti.typemate;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mbti.typemate.async.SpringTask;
import com.mbti.typemate.util.Util;

public class PwdResetActivity extends AppCompatActivity {

    EditText edit_nowPwd, edit_pwd, edit_pwd_confirm;

    Button pwdReset_btn;

    String myNickname, user_id;

    TextView confirm_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd_reset);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            myNickname = bundle.getString("myNickname");
            user_id = bundle.getString("user_id");
        }

        edit_nowPwd = findViewById(R.id.edit_nowPwd);
        edit_pwd = findViewById(R.id.edit_pwd);
        edit_pwd_confirm = findViewById(R.id.edit_pwd_confirm);

        confirm_txt = findViewById(R.id.confirm_txt);

        edit_pwd.addTextChangedListener(textChange);
        edit_pwd_confirm.addTextChangedListener(textChange);

        pwdReset_btn = findViewById(R.id.pwdReset_btn);

        pwdReset_btn.setOnClickListener(resetClick);

    }//onCreate()

    TextWatcher textChange = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        //텍스트의 변경이 감지되었을때
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String pwd = edit_pwd.getText().toString();
            String pwdConfirm = edit_pwd_confirm.getText().toString();


            if(!pwd.equals("") && !pwdConfirm.equals("")){

                //비번이 같을때
                if (pwd.equals(pwdConfirm)) {
                    confirm_txt.setText("사용가능");
                    confirm_txt.setTextColor(getResources().getColor(R.color.colorDefault));
                    pwdReset_btn.setEnabled(true);
                }else{
                    confirm_txt.setText("비밀번호 불일치");
                    confirm_txt.setTextColor(Color.RED);
                    pwdReset_btn.setEnabled(false);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    View.OnClickListener resetClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String nowPwd = edit_nowPwd.getText().toString();
            String newPwd = edit_pwd.getText().toString();

            String url = Util.SERVER_IP_PWD_CHECK_RESET;
            String param = "nickname=" + myNickname + "&pwd=" + nowPwd + "&id=" + user_id + "&new_pwd=" + newPwd;

            try {
                String result = new SpringTask(url, PwdResetActivity.this).execute(param).get();

                if (result.equals("YES")){
                    Toast.makeText(getApplicationContext(), "비밀번호 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();

                    //비밀번호 변경후에도 자동로그인을 위해서 SharedPreferences의 값을 업데이트해준다
                    SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor autoLogin = auto.edit();
                    autoLogin.putString("inputId", user_id);
                    autoLogin.putString("inputPwd", newPwd);
                    autoLogin.commit();

                    //액티비티 종료
                    finish();
                }else if(result.equals("NO")){
                    Toast.makeText(getApplicationContext(), "현재 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    //서버에서 오류가 났을때 result를 ERROR로 반환하게끔 설정해놨다.
                    Toast.makeText(getApplicationContext(), "알수없는 오류 관리자에게 문의해주세요.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.i("CATCH", e.getMessage());
            }
        }
    };

    private String resetPwd(){

        return "";
    }
}