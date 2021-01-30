package com.mbti.typemate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mbti.typemate.async.SpringTask;
import com.mbti.typemate.util.Util;

import java.util.concurrent.ExecutionException;

public class FindPwdActivity extends AppCompatActivity {

    LinearLayout pwdFind_layout, pwdReset_layout;

    EditText edit_id, edit_name, edit_tel, edit_pwd, edit_pwd_confirm;

    Button pwdReset_btn, pwdFind_btn;

    //visible 할때 layout 에 적용시켜줄 애니메이션
    Animation animation = new AlphaAnimation(0, 2);

    String url = "";
    String param = "";
    String result = "";
    String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pwd);

        pwdFind_layout = findViewById(R.id.pwdFind_layout);
        pwdReset_layout = findViewById(R.id.pwdReset_layout);

        edit_id = findViewById(R.id.edit_id);
        edit_name = findViewById(R.id.edit_name);
        edit_tel = findViewById(R.id.edit_tel);
        edit_pwd = findViewById(R.id.edit_pwd);
        edit_pwd_confirm = findViewById(R.id.edit_pwd_confirm);

        pwdFind_btn = findViewById(R.id.pwdFind_btn);
        pwdReset_btn = findViewById(R.id.pwdReset_btn);

        pwdFind_btn.setOnClickListener(btnEvt);
        pwdReset_btn.setOnClickListener(btnEvt);

        Intent getIdIntent = getIntent();
        Bundle bundle = getIdIntent.getExtras();
        if (bundle != null){
            //1초동안 투명도를 낮추며 뚜렷하게 보이게 해줌
            animation.setDuration(1000);
            pwdFind_layout.setVisibility(View.GONE);
            pwdReset_layout.setVisibility(View.VISIBLE);
            pwdReset_layout.setAnimation(animation);
            id = bundle.getString("id");
        }
    }///onCreate()

    View.OnClickListener btnEvt = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()){

                case R.id.pwdFind_btn :

                    id = edit_id.getText().toString();
                    String name = edit_name.getText().toString();
                    String tel = edit_tel.getText().toString();

                    url = Util.SERVER_IP_FIND_PWD;
                    param = "id=" + id + "&name=" + name + "&tel=" + tel;

                    result = "";

                    try {
                        result = new SpringTask(url, FindPwdActivity.this).execute(param).get();

                        if (result.equals("YES")){
                            //visible처리시 애니메이션 효과 적용
                            animation.setDuration(1000);
                            pwdFind_layout.setVisibility(View.GONE);
                            pwdReset_layout.setVisibility(View.VISIBLE);
                            pwdReset_layout.setAnimation(animation);
                        }else{
                            Toast.makeText(getApplicationContext(), "입력하신 정보를 확인해주세요", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.i("CATCH", e.getMessage());
                    }
                    break;

                case R.id.pwdReset_btn:
                    String pwd = edit_pwd.getText().toString();
                    String pwd_confirm = edit_pwd_confirm.getText().toString();

                    if (pwd.equals(pwd_confirm)){
                        url = Util.SERVER_IP_RESET_PWD;
                        param = "id=" + id +"&pwd=" + pwd;

                        try {
                            result = new SpringTask(url, FindPwdActivity.this).execute(param).get();
                            if (result.equals("YES")){
                                Toast.makeText(getApplicationContext(), "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent loginIntent = new Intent(FindPwdActivity.this, LoginActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("id", id);
                                loginIntent.putExtras(bundle);
                                startActivity(loginIntent);
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(), "알수없는오류 관리자에게 문의해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.i("CATCH", e.getMessage());
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }//switch
        }
    };
}