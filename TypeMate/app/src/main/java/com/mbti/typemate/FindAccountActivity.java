package com.mbti.typemate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mbti.typemate.async.SpringTask;
import com.mbti.typemate.util.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FindAccountActivity extends AppCompatActivity {

    EditText edit_name, edit_tel;//아이디찾기시 입력할 에딧뷰

    TextView u_name_txt;//아이디찾기시 **님의 정보와 일치하는 아이디입니다 표시 할 텍뷰

    //아이디찾기 버튼
    Button findId_btn, login_btn, pwdReset_btn;

    //찾은 아이디를 정렬해줄 RadioGroup
    RadioGroup idRadioGroup;

    //선택한 radio버튼
    RadioButton select_radio;

    //visible 처리해줄때 쓸 레이아웃들
    LinearLayout findId_layout, result_layout;

    //서버에서 받은값을 저장해줄 변수
    String responseStr;

    List<String> userIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_account);

        findId_layout = findViewById(R.id.findId_layout);
        result_layout = findViewById(R.id.result_layout);

        u_name_txt = findViewById(R.id.u_name_txt);

        idRadioGroup = findViewById(R.id.idRadioGroup);

        edit_name = findViewById(R.id.edit_name);
        edit_tel = findViewById(R.id.edit_tel);

        findId_btn = findViewById(R.id.findId_btn);
        login_btn = findViewById(R.id.login_btn);
        pwdReset_btn = findViewById(R.id.pwdReset_btn);

        findId_btn.setOnClickListener(btnEvt);

    }//onCreate()

    View.OnClickListener btnEvt = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.findId_btn :
                    String name = edit_name.getText().toString();
                    String tel = edit_tel.getText().toString();

                    //넘길 파라미터
                    String param = "name=" + name + "&tel=" + tel;

                    //접속할 Spring Mapping
                    String url = Util.SERVER_IP_FIND_ACCOUNT;

                    try {
                        //서버로 접속하여 JSON형태로 반환받은 값을 responseStr에 초기화
                        responseStr = new SpringTask(url, FindAccountActivity.this).execute(param).get();
                        //Json형태를 실제 Json으로 변환
                        JSONObject jsonObject = new JSONObject(responseStr);
                        //Json에서 이름과 전화번호에 해당하는 아이디가 있는지 판별해서 돌아온 값
                        String result = jsonObject.getString("result");
                        //검색한 이름과 전화번호가 있을때만
                        if (result.equals("YES")){
                            //id를 담고있는 Json형태의 resultStr추출
                            String resultStr = jsonObject.getString("resultStr");
                            //실제 Json화 시켜줌
                            JSONObject idListJson = new JSONObject(resultStr);

                            //List객체화
                            userIdList = new ArrayList<>();
                            //넘어온 Json의 length()만큼 List에 담아준다

                            for (int i = 0; i < idListJson.length(); i++){
                                userIdList.add(idListJson.getString("id"+i));
                                RadioButton radioButton = new RadioButton(FindAccountActivity.this);
                                radioButton.setId(i);
                                radioButton.setText(idListJson.getString("id"+i));
                                radioButton.setTextSize(20);
                                radioButton.setPadding(30,0,0,20);
                                idRadioGroup.addView(radioButton);
                            }

                            u_name_txt.setText(name + "님의 정보와 일치하는 아이디입니다.");


                            //로그인하러가기, 비밀번호 재설정버튼 활성화
                            login_btn.setOnClickListener(btnEvt);
                            pwdReset_btn.setOnClickListener(btnEvt);

                            //visible처리시 애니메이션 효과 적용
                            Animation animation = new AlphaAnimation(0, 1);
                            animation.setDuration(1000);
                            //아이디찾는창은 숨겨주고

                            //키보드를 제어할때 사용하는 클래스
                            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(edit_tel.getWindowToken(), 0);
                            findId_layout.setVisibility(View.GONE);
                            //결과창 띄워주기
                            result_layout.setVisibility(View.VISIBLE);
                            result_layout.setAnimation(animation);
                        }else{
                            Toast.makeText(getApplicationContext(), "아이디가 존재하지 않습니다", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.i("CATCH", e.getMessage());
                    }
                    break;

                case R.id.login_btn :
                    //선택한 라디오 버튼 id지정(값 빼오기 위함)
                    select_radio = (RadioButton)findViewById(idRadioGroup.getCheckedRadioButtonId());
                    Intent loginIntent = new Intent(FindAccountActivity.this, LoginActivity.class);
                    if (select_radio != null) {
                        Bundle loginBundle = new Bundle();
                        loginBundle.putString("id", select_radio.getText().toString());
                        loginIntent.putExtras(loginBundle);
                        startActivity(loginIntent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "로그인하실 아이디를 선택해주세요", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.pwdReset_btn :
                    select_radio = (RadioButton)findViewById(idRadioGroup.getCheckedRadioButtonId());
                    Intent findPwdIntent = new Intent(FindAccountActivity.this, FindPwdActivity.class);
                    if (select_radio != null){
                        Bundle findPwdBundle = new Bundle();
                        findPwdBundle.putString("id", select_radio.getText().toString());
                        findPwdIntent.putExtras(findPwdBundle);
                        startActivity(findPwdIntent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "비밀번호를 변경하실 아이디를 선택해주세요", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }//switch
        }//onClick()
    };//OnclickListener
}