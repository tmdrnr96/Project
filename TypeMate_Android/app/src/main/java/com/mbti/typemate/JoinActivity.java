package com.mbti.typemate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mbti.typemate.async.SpringTask;
import com.mbti.typemate.util.Util;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class JoinActivity extends AppCompatActivity {

    LinearLayout layout_1, layout_2, layout_3;
    TextView text_id_check, text_pwd_check, text_nickname, text_tel_check, text_birth, text_mbti;
    EditText edit_id, edit_pwd, edit_repwd, edit_name, edit_nickname, edit_tel;
    Button join_btn, btn_pre, btn_next,  btn_birth, btn_mbti;
    RadioButton select_gender, man, woman;
    RadioGroup radio_gender;

    Dialog dialog;

    //성별을 구분하기 위한 변수(남: 남자, 여 : 여자)[genders -> genderCheck 변경]
    String genderCheck = "";
    String genders = "남";
    //id 중복검사가 정삭적으로 끝났다면 "YES" 아니면 "NO"를 나타낼 변수
    String idCheck = "";
    //password 검사가 정삭적으로 끝났다면 "YES" 아니면 "NO"를 나타낼 변수
    String passwordCheck = "";

    //해당 editText를 입력 변수
    String nameChecke = "";
    String telCheck = "";
    String birtdayCheck = "";

    //다음, 이전 버튼에 사용할 변수
    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);


        //레이아웃의 id검색
        edit_id = findViewById(R.id.edit_id);
        edit_pwd = findViewById(R.id.edit_pwd);
        edit_repwd = findViewById(R.id.edit_repwd);
        edit_name = findViewById(R.id.edit_name);
        edit_nickname = findViewById(R.id.edit_nickname);
        edit_tel = findViewById(R.id.edit_tel);
        text_birth = findViewById(R.id.text_birth);
        text_mbti = findViewById(R.id.text_mbti);
        text_id_check = findViewById(R.id.text_id_check);
        text_pwd_check = findViewById(R.id.text_pwd_check);
        text_tel_check = findViewById(R.id.text_tel_check);

        join_btn = findViewById(R.id.join_btn);

        btn_pre = findViewById(R.id.btn_pre);
        btn_next = findViewById(R.id.btn_next);

        layout_1 = findViewById(R.id.layout_1);
        layout_2 = findViewById(R.id.layout_2);
        layout_3 = findViewById(R.id.layout_3);

        text_nickname = findViewById(R.id.text_nickname);
        btn_birth = findViewById(R.id.btn_birth);
        btn_mbti = findViewById(R.id.btn_mbti);

        //라디오 버튼(gender)
        man = (RadioButton) findViewById(R.id.man);
        woman = (RadioButton) findViewById(R.id.woman);

        radio_gender = (RadioGroup) findViewById(R.id.radio_gender);

        radio_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                if (checkedId == R.id.man) {
                    genderCheck = "남";
                    Btn_next_check_2(); //a

                } else {
                    genderCheck = "여";
                    Btn_next_check_2();
                }

            }
        });

        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            String mbti = intent.getExtras().getString("mbti");
            text_mbti.setText("" + mbti);


            //MBTI 선택시 완료 버튼 활성화
            if(text_mbti.getText().length() == 4){
                /*-----------------------------추가(08/11)------------------------------*/
                //MBTI 선택 후, 이전 페이지 처리를 위해 카운터 3으로 수정
                count = 3;
                /*-----------------------------추가(08/11)------------------------------*/
                join_btn.setEnabled(true);
                join_btn.setBackgroundResource(R.drawable.btn_radius);

                layout_1.setVisibility(View.GONE);
                layout_2.setVisibility(View.GONE);
                layout_3.setVisibility(View.VISIBLE);
                btn_pre.setVisibility(View.VISIBLE);
                btn_next.setVisibility(View.GONE);
                join_btn.setVisibility(View.VISIBLE);

            }

            String id = intent.getExtras().getString("id");
            edit_id.setText("" + id);

            String pwd = intent.getExtras().getString("pwd");
            edit_pwd.setText("" + pwd);

            String repwd = intent.getExtras().getString("repwd");
            edit_repwd.setText("" + repwd);

            String nickname = intent.getExtras().getString("nickname");
            edit_nickname.setText("" + nickname);

            String name = intent.getExtras().getString("name");
            edit_name.setText("" + name);

            String tel = intent.getExtras().getString("tel");
            edit_tel.setText("" + tel);

            String gender = intent.getExtras().getString("gender");
            Log.i("MY", "gender : " + gender);

            if (gender.equals(genders)) {
                man.setChecked(true);
            } else {
                woman.setChecked(true);
            }

            String birth = intent.getExtras().getString("birth");
            text_birth.setText("" + birth);

        }

        //입력한 ID 중복 체크

        edit_id.addTextChangedListener(new TextWatcher() {
            //텍스트를 입력하시 전에
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

                Log.i("MY", "beforeTextChanged");

            }

            //텍스트에 변화가 있을 때(텍스트 입력감지)
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {

                String id = edit_id.getText().toString().trim();

                id = id.replaceAll(" ", "");

                //아이디 체크를 위한 SpringTask클래스를 사용하는부분
                //서버로 보낼 파라미터값으로 변환
                String param = "&id=" + id;

                //AsyncTask 생성 및 연결
                String url = Util.SERVER_IP_ID_CHECK;//서버의 IP


                if(id.length() == 0){
                    btn_next.setEnabled(false);}


                //ID의 길이가 4이상되어야 서버 접속
                if (id.length() >= 4) {

                    try {
                        String result = new SpringTask(url, JoinActivity.this).execute(param).get();

                        JSONObject jsonObject = new JSONObject(result);

                        idCheck = jsonObject.getString("id");

                        if (idCheck.equals("YES")) {
                            text_id_check.setText("사용 가능");
                            text_id_check.setTextColor(Color.parseColor("#80c783"));
                        } else {
                            text_id_check.setText("사용 불가");
                            text_id_check.setTextColor(Color.parseColor("#f55354"));
                            btn_next.setEnabled(false);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    text_id_check.setVisibility(View.VISIBLE);

                }else if(id.length() < 4){
                    text_id_check.setVisibility(View.VISIBLE);
                    text_id_check.setText("4자리 이상 입력");
                    text_id_check.setTextColor(Color.parseColor("#f55354"));
                    btn_next.setEnabled(false);
                }

                Btn_next_check_1();

            }

            //아이디 체크를 위한 SpringTask클래스를 사용하는부분
            //텍스트의 입력이 끝날때
            @Override
            public void afterTextChanged(Editable editable) {

                Log.i("MY", "afterTextChanged");

            }
        });

        //비밀번호 에딧 텍스트 감지
        edit_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Btn_next_check_1();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //비밀번호 체크 확인
        edit_repwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Btn_next_check_1();
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //edit_name에 값이 없다면 다음 버튼 비활성화
        edit_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Btn_next_check_2();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        //edit_tel 값이 없다면 다음 버튼 비활성화
        edit_tel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //edit_tel 10자 이상!
                if (edit_tel.getText().toString().length() >= 10) {
                    text_tel_check.setVisibility(View.GONE);
                    Btn_next_check_2();

                } else if (edit_tel.getText().toString().length() == 0) {
                    text_tel_check.setVisibility(View.GONE);
                    btn_next.setBackgroundResource(R.drawable.btn_radius_no);
                    btn_next.setEnabled(false);

                } else {
                    text_tel_check.setVisibility(View.VISIBLE);
                    btn_next.setBackgroundResource(R.drawable.btn_radius_no);
                    btn_next.setEnabled(false);
                    Btn_next_check_2();

                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        //입력한 NickName 중복 체크
        edit_nickname.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String nickname = edit_nickname.getText().toString();

                //서버로 보낼 파라미터값으로 변환
                String param = "nickname=" + nickname;

                //AsyncTask 생성 및 연결
                String url = Util.SERVER_IP_NICKNAME_CHECK;//서버의 IP

                //nickname의 길이가 2이상되어야 서버 접속
                if (nickname.length() >= 2) {

                    try {
                        String result = new SpringTask(url, JoinActivity.this).execute(param).get();

                        JSONObject jsonObject = new JSONObject(result);

                        Log.i("MY", "" + result);//yes or no

                        String resultStr = jsonObject.getString("nickname");


                        if (resultStr.equals("YES")) {
                            text_nickname.setText("사용 가능");
                            text_nickname.setTextColor(Color.parseColor("#80c783"));
                            text_nickname.setVisibility(View.VISIBLE);

                            Btn_next_check_2();

                        } else {
                            text_nickname.setText("사용 불가");
                            text_nickname.setTextColor(Color.parseColor("#f55354"));
                            text_nickname.setVisibility(View.VISIBLE);

                            btn_next.setBackgroundResource(R.drawable.btn_radius_no);
                            btn_next.setEnabled(false);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else if(nickname.length() == 0){
                    text_nickname.setVisibility(View.INVISIBLE);
                    btn_next.setBackgroundResource(R.drawable.btn_radius_no);
                    btn_next.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        //생년월일 선택하기
        btn_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //생년월일 현재날짜를 보이기 위함과 동시에 날짜 선택을 위한 변수
                Calendar c = Calendar.getInstance();
                int y = c.get(Calendar.YEAR); //년
                int m = c.get(Calendar.MONTH); //월
                int d = c.get(Calendar.DAY_OF_MONTH);//일

                //날짜 선택 다이얼로그 생성
                //DatePickerDialog : 날짜를 선택하는 다이얼로그
                dialog = new DatePickerDialog(JoinActivity.this,
                        dateSetListener,//달력의 날짜변경 감지자
                        y, m, d);
                dialog.show();

            }
        });

        //MBTI 선택하기
        btn_mbti = findViewById(R.id.btn_mbti);

        btn_mbti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //MBTI 선택, 페이지 전환, 값 보내기
                Intent intent = new Intent(JoinActivity.this, MbtiActivity.class);

                intent.putExtra("id", "" + edit_id.getText());
                intent.putExtra("pwd", "" + edit_pwd.getText());
                intent.putExtra("repwd", "" + edit_repwd.getText());
                intent.putExtra("nickname", "" + edit_nickname.getText());
                intent.putExtra("genders", "" + genderCheck);
                intent.putExtra("birth", "" + text_birth.getText());
                intent.putExtra("name", "" + edit_name.getText());
                intent.putExtra("tel", "" + edit_tel.getText());

                startActivity(intent);
                finish();

            }
        });


        //회원가입 버튼을 눌렀을때 이벤트발생
        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //radioGroup에서 체크한 아이디값을 RadioButton변수에 담아준다
                select_gender = findViewById(radio_gender.getCheckedRadioButtonId());

                //각 입력값 변수로 세팅
                String id = edit_id.getText().toString().trim();
                String pwd = edit_pwd.getText().toString().trim();
                String name = edit_name.getText().toString().trim();
                String tel = edit_tel.getText().toString().trim();
                String nickname = edit_nickname.getText().toString().trim();
                String gender = select_gender.getText().toString().trim();
                String birth = text_birth.getText().toString().trim();
                String mbti = text_mbti.getText().toString().trim();

                //서버로 보낼 파라미터값으로 변환
                String param = "id=" + id + "&pwd=" + pwd + "&name=" + name + "&nickname=" + nickname + "&gender=" + gender + "&birth=" + birth + "&mbti=" + mbti + "&tel=" + tel; // result라는 변수를 만들어서 http파라미터 형식으로 담아준다

                //AsyncTask 생성 및 연결 파라미터 두개 넣어줌
                new JoinTask().execute(param);
            }
        });

        //이전 버튼 다음 버튼 감지
        //다음, 이전 버튼 visvible 처리
        btn_pre.setOnClickListener(btn_click);
        btn_next.setOnClickListener(btn_click);

    }//onCreate()


    //이전 버튼 다음 버튼 감지
    //다음, 이전 버튼 visvible 처리
    View.OnClickListener btn_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.btn_pre:
                    --count;

                    if (count == 1) {
                        layout_1.setVisibility(View.VISIBLE);
                        layout_2.setVisibility(View.GONE);
                        layout_3.setVisibility(View.GONE);
                        btn_pre.setVisibility(View.INVISIBLE);
                        btn_next.setVisibility(View.VISIBLE);
                        join_btn.setVisibility(View.GONE);

                        btn_next.setEnabled(true);
                        btn_next.setBackgroundResource(R.drawable.btn_radius);

                    } else if (count == 2) {
                        layout_1.setVisibility(View.GONE);
                        layout_2.setVisibility(View.VISIBLE);
                        layout_3.setVisibility(View.GONE);
                        btn_pre.setVisibility(View.VISIBLE);
                        btn_next.setVisibility(View.VISIBLE);
                        join_btn.setVisibility(View.GONE);

                        btn_next.setEnabled(true);
                        btn_next.setBackgroundResource(R.drawable.btn_radius);
                    }
                    break;

                case R.id.btn_next:
                    ++count;

                    if (count == 2) {
                        layout_1.setVisibility(View.GONE);
                        layout_2.setVisibility(View.VISIBLE);
                        layout_3.setVisibility(View.GONE);
                        btn_pre.setVisibility(View.VISIBLE);
                        btn_next.setVisibility(View.VISIBLE);
                        join_btn.setVisibility(View.GONE);

                        if(!edit_name.getText().equals("") && !edit_tel.getText().equals("") && !text_nickname.getText().equals("사용 불가") && text_birth.getText().length() != 17){
                            btn_next.setEnabled(true);
                            btn_next.setBackgroundResource(R.drawable.btn_radius);
                        }else {
                            btn_next.setEnabled(false);
                            btn_next.setBackgroundResource(R.drawable.btn_radius_no);
                        }

                    } else if (count == 3) {
                        layout_1.setVisibility(View.GONE);
                        layout_2.setVisibility(View.GONE);
                        layout_3.setVisibility(View.VISIBLE);
                        btn_pre.setVisibility(View.VISIBLE);
                        btn_next.setVisibility(View.GONE);
                        join_btn.setVisibility(View.VISIBLE);

                        btn_next.setEnabled(false);
                        btn_next.setBackgroundResource(R.drawable.btn_radius_no);

                    }
                    break;
            }

        }
    };


    class JoinTask extends AsyncTask<String, Void, String> {

        String ip = Util.IP;//서버의 IP
        String sendMsg, receiveMsg;//보낼메세지, 받을메세지 변수 선언
        String server_ip = Util.SERVER_IP_JOIN;//연결할 서버의 주소(url)

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(server_ip);//접속할 URL선언과 함께 생성자로 페이지주소를 넘겨줌

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");//전송방식 설정

                //OutputStream 에 HttpURLConnection의 주소를 연결해줌
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                //Spring에서 받을시에는 VO와 string type이런식으로 받을 예정
                sendMsg = strings[0];

                osw.write(sendMsg);//방금 셋팅한 파라미터 전달
                osw.flush();


                if (conn.getResponseCode() == conn.HTTP_OK) {

                    //서버에서 리턴해준 값을 UTF-8 인코딩으로 읽어옴
                    InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "UTF-8");

                    //데이터처리에서 유리한 버퍼드리더
                    BufferedReader reader = new BufferedReader(isr);

                    receiveMsg = reader.readLine();//반환값을 변수에 저장

                }
            } catch (Exception e) {
                Log.i("CATCH", e.getMessage());//오류값을 Logcat에서 간단하게나만 확인할수 있게끔
            }

            return receiveMsg;

        }//doInBackground

        //실제 서버랑 통신하는건 이쪽인가
        @Override
        protected void onPostExecute(String resultStr) {

            if (resultStr.equals("YES")) {
                // Toast.makeText(JoinActivity.this, "회원가입을 축하드립니다!", Toast.LENGTH_SHORT).show();

                final SweetAlertDialog sweet = new SweetAlertDialog(JoinActivity.this,SweetAlertDialog.SUCCESS_TYPE);
                sweet.setTitleText("회원 가입 완료!");
                sweet.show();
                sweet.findViewById(R.id.confirm_button).setVisibility(View.GONE);

                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        //회원가입 완료후 로그인 페이지로 이동
                        finish();//회원가입 페이지는 없애줌

                        sweet.dismiss();
                        timer.cancel();
                    }
                },900);

            } else {
                new SweetAlertDialog(JoinActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("회원 가입 실패")
                        .setContentText("관리자에게 문의해주세요")
                        .show();
                // Toast.makeText(JoinActivity.this, "회원가입 실패, 관리자에게 문의해주세요", Toast.LENGTH_SHORT).show();
            }

        }//onPostExecute

    }//JoinTask클래스

    //날짜 선택을 위한 클래스
    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        //날짜가 바뀌고, 처음들어온 값은 없어지고, 새로들어온 값이 입력된다.
        @Override
        public void onDateSet(DatePicker datePicker, int y, int m, int d) {

            String str = String.format("%d-%02d-%02d", y, m + 1, d);

            //선택된 생일을 EditText에 담는다.
            text_birth.setText(str);
            Btn_next_check_2();

        }
    };

    //id 중복 체크와 password 확인 여부에 따른 버튼 활성화, 비활성화
    public void Btn_next_check_1(){

        if(edit_repwd.getText().toString().length() == 0){
            text_pwd_check.setVisibility(View.GONE);
            passwordCheck = "NO";

        }else if(edit_pwd.getText().toString().equals(edit_repwd.getText().toString())){
            text_pwd_check.setVisibility(View.GONE);
            passwordCheck = "YES";

        }else{
            text_pwd_check.setVisibility(View.VISIBLE);
            passwordCheck = "NO";
        }

        if(idCheck.equals("YES")&&passwordCheck.equals("YES")){
            btn_next.setEnabled(true);
            btn_next.setBackgroundResource(R.drawable.btn_radius);
        }else{
            btn_next.setEnabled(false);
            btn_next.setBackgroundResource(R.drawable.btn_radius_no);
        }
    }
    /*-----------------------------수정(08/11)------------------------------*/

    public void Btn_next_check_2(){

        nameChecke = edit_name.getText().toString();
        telCheck = edit_tel.getText().toString();
        birtdayCheck = text_birth.getText().toString();

        if(nameChecke.length() != 0 && telCheck.length() >= 10 && birtdayCheck.length() != 17 && !genderCheck.equals("") && !text_nickname.getText().equals("사용 불가")){

            btn_next.setEnabled(true);
            btn_next.setBackgroundResource(R.drawable.btn_radius);
        }else{
            btn_next.setEnabled(false);
            btn_next.setBackgroundResource(R.drawable.btn_radius_no);

        }

    }
    /*-----------------------------수정(08/11)------------------------------*/

}//showDate