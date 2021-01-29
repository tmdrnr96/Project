package com.mbti.typemate;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mbti.typemate.async.SpringTask;
import com.mbti.typemate.util.Util;
import com.mbti.typemate.vo.UserVO;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProfileModifyActivity extends AppCompatActivity {

    UserVO vo;
    Dialog dialog;
    AlertDialog alertDialog;
    AlertDialog.Builder alertDialogBuilder;
    InputMethodManager imm; // 회원탈퇴 됐을 때 키보드 강제로 내리기 위한 변수

    TextView text_nickname, text_birth;
    EditText edit_name, edit_nickname, edit_tel;
    Button btn_birth, btn_withdrawal, btn_reset_profile;
    RadioButton select_gender, man, woman;
    RadioGroup radio_gender;

    String nickname = "";
    String gender = "";

    //성별을 구분하기 위한 변수(남: 남자, 여 : 여자)[genders -> genderCheck 변경]
    String genderCheck = "";

    //해당 editText를 입력했다면 "YES" 아니면 "NO"를 나타낼 변수
    String nameChecke = "";
    String telCheck = "";
    String birtdayCheck = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_modify);

        alertDialogBuilder = new AlertDialog.Builder(this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras(); // 0811
        vo = (UserVO) bundle.getSerializable("u_vo"); // 0811

        edit_name = findViewById(R.id.edit_name);
        edit_nickname = findViewById(R.id.edit_nickname);
        edit_tel = findViewById(R.id.edit_tel);
        text_nickname = findViewById(R.id.text_nickname);
        text_birth = findViewById(R.id.text_birth);
        btn_birth = findViewById(R.id.btn_birth);
        btn_withdrawal = findViewById(R.id.btn_withdrawal);
        btn_reset_profile = findViewById(R.id.btn_reset_profile);

        //라디오 버튼(gender)
        man = (RadioButton) findViewById(R.id.man);
        woman = (RadioButton) findViewById(R.id.woman);

        radio_gender = (RadioGroup) findViewById(R.id.radio_gender);

        radio_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                if (checkedId == R.id.man) {
                    genderCheck = "남";
                } else {
                    genderCheck = "여";
                }
            }
        });

        //0811
        //회원정보 값 넣어놓기
        edit_nickname.setText("" + vo.getNickname());
        edit_name.setText("" + vo.getName());
        edit_tel.setText("" + vo.getTel());
        if (vo.getGender().equals("남")) {
            man.setChecked(true);
        } else {
            woman.setChecked(true);
        }
        text_birth.setText("" + vo.getBirth());

        btn_birth.setOnClickListener(click);
        btn_withdrawal.setOnClickListener(click);
        btn_reset_profile.setOnClickListener(click);

        //edit_name에 값이 없다면 다음 버튼 비활성화
        edit_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnResetProfileCheck();
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
                btnResetProfileCheck();
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

                // 원래 자신의 닉네임이라면 가능
                if (nickname.equals(vo.getNickname())) {
                    text_nickname.setText("사용 가능");
                    text_nickname.setTextColor(Color.parseColor("#80c783"));
                    text_nickname.setVisibility(View.VISIBLE);

                    btnResetProfileCheck();
                    return;
                }

                //서버로 보낼 파라미터값으로 변환
                String param = "nickname=" + nickname;

                //AsyncTask 생성 및 연결
                String url = Util.SERVER_IP_NICKNAME_CHECK;//서버의 IP

                //nickname의 길이가 2이상되어야 서버 접속
                if (nickname.length() >= 2) {

                    try {
                        String result = new SpringTask(url, getApplicationContext()).execute(param).get();

                        JSONObject jsonObject = new JSONObject(result);

                        String resultStr = jsonObject.getString("nickname");

                        if (resultStr.equals("YES")) {
                            text_nickname.setText("사용 가능");
                            text_nickname.setTextColor(Color.parseColor("#80c783"));
                            text_nickname.setVisibility(View.VISIBLE);

                            btnResetProfileCheck();//08/10 추가

                        } else {
                            text_nickname.setText("사용 불가");
                            text_nickname.setTextColor(Color.parseColor("#f55354"));
                            text_nickname.setVisibility(View.VISIBLE);

                            btn_reset_profile.setBackgroundResource(R.drawable.btn_radius_no);
                            btn_reset_profile.setEnabled(false);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (nickname.length() == 0) {
                    text_nickname.setVisibility(View.INVISIBLE);
                    btn_reset_profile.setBackgroundResource(R.drawable.btn_radius_no);
                    btn_reset_profile.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    //0810
    //회원탈퇴를 위한 스프링 연동
    private void withdrawalResult(String param) {
        String resultStr = "";//async에서 처리한값을 반환받는 변수

        try {
            resultStr = new SpringTask(Util.SERVER_IP_WITHDRAWAL, this).execute(param).get();
            // YES 반환 받으면 회원 탈퇴 성공이므로 로그인창으로 이동
            if (resultStr.equals("YES")) {
                //SharedPreferences에 저장됐던 유저의 정보는 다 지워지게끔 처리
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                editor.clear();
                //커밋 꼭 해줘야 적용됨
                editor.commit();

                final SweetAlertDialog sweet = new SweetAlertDialog(ProfileModifyActivity.this,SweetAlertDialog.SUCCESS_TYPE);
                sweet.setTitleText("회원 탈퇴 완료!");
                sweet.show();
                sweet.findViewById(R.id.confirm_button).setVisibility(View.GONE);

                final Timer timer = new Timer();

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //회원 탈퇴 후 로그인Activity로 이동하는 Intent생성
                        Intent intent = new Intent(ProfileModifyActivity.this, LoginActivity.class);
                        //로그인Activity로 이동
                        startActivity(intent);
                        finish();
                        sweet.dismiss();
                        dialog.dismiss();
                        timer.cancel();
                    }
                },900);

            } else {
                new SweetAlertDialog(ProfileModifyActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("회원 탈퇴 실패")
                        .setContentText("다시 시도해 주세요.")
                        .show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //0811
    //날짜 선택을 위한 클래스
    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        //날짜가 바뀌고, 처음들어온 값은 없어지고, 새로들어온 값이 입력된다.
        @Override
        public void onDateSet(DatePicker datePicker, int y, int m, int d) {
            String str = String.format("%d-%02d-%02d", y, m + 1, d);

            //선택된 생일을 EditText에 담는다.
            text_birth.setText(str);
            btnResetProfileCheck();
        }
    };

    public void btnResetProfileCheck() {

        nameChecke = edit_name.getText().toString();
        telCheck = edit_tel.getText().toString();
        birtdayCheck = text_birth.getText().toString();

        if (nameChecke.length() != 0 && telCheck.length() >= 10 && birtdayCheck.length() != 17 && !genderCheck.equals("")) {

            btn_reset_profile.setEnabled(true);
            btn_reset_profile.setBackgroundResource(R.drawable.btn_radius);
        } else {
            btn_reset_profile.setEnabled(false);
            btn_reset_profile.setBackgroundResource(R.drawable.btn_radius_no);

        }

    }

    //0811
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String param = "";

            switch (view.getId()) {
                case R.id.btn_birth:
                    //생년월일 현재날짜를 보이기 위함과 동시에 날짜 선택을 위한 변수
                    Calendar c = Calendar.getInstance();
                    int y = c.get(Calendar.YEAR); //년
                    int m = c.get(Calendar.MONTH); //월
                    int d = c.get(Calendar.DAY_OF_MONTH);//일

                    //날짜 선택 다이얼로그 생성
                    //DatePickerDialog : 날짜를 선택하는 다이얼로그
                    dialog = new DatePickerDialog(ProfileModifyActivity.this,
                            dateSetListener,//달력의 날짜변경 감지자
                            y, m, d);
                    dialog.show();
                    break;

                case R.id.btn_withdrawal:

                    //다이얼로그 생성
                    dialog = new Dialog(ProfileModifyActivity.this);

                    // 다이얼로그의 자체 배경을 투명하게 설정
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    //다이얼로그가 참조할 레이아웃 등록
                    dialog.setContentView(R.layout.check_pwd_dialog);

                    TextView txt_id = dialog.findViewById(R.id.txt_id);
                    Button btn_check = dialog.findViewById(R.id.btn_check);

                    txt_id.setText(vo.getId());

                    btn_check.setOnClickListener(click);

                    dialog.show();;
                    break;
                case R.id.btn_reset_profile:
                    //radioGroup에서 체크한 아이디값을 RadioButton변수에 담아준다
                    select_gender = findViewById(radio_gender.getCheckedRadioButtonId());

                    //각 입력값 변수로 세팅
                    String name = edit_name.getText().toString();
                    String tel = edit_tel.getText().toString();
                    nickname = edit_nickname.getText().toString();
                    gender = select_gender.getText().toString();
                    String birth = text_birth.getText().toString();

                    //서버로 보낼 파라미터값으로 변환
                    param = "u_idx=" + vo.getU_idx() +"&name=" + name + "&nickname=" + nickname + "&gender=" + gender + "&birth=" + birth + "&tel=" + tel;
                    resetProfile(param);

                    break;

                case R.id.btn_check:
                    //키보드 내리기
                    EditText edit_pwd = dialog.findViewById(R.id.edit_pwd);
                    imm.hideSoftInputFromWindow(edit_pwd.getWindowToken(), 0);

                    if(edit_pwd.length() != 0){
                        param = "u_idx=" + vo.getU_idx() + "&pwd=" + edit_pwd.getText().toString();
                        checkPwd(param);
                    } else{
                        new SweetAlertDialog(ProfileModifyActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setContentText("비밀번호를 입력해 주세요.")
                                .show();
                    }

                    break;
            }
        }
    };

    public void checkPwd(String param){
        String resultStr = "";

        try {
            resultStr = new SpringTask(Util.SERVER_IP_RECHECK_PWD,ProfileModifyActivity.this).execute(param).get();

            if (resultStr.equals("YES")) {

                //타이틀과 메세지 지정
                alertDialogBuilder.setTitle("회원탈퇴").setMessage("정말 탈퇴 하시겠습니까?");

                //다이얼로그에 OK버튼 만들고 감지자 정의(누르면 회원탈퇴)
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String param = "nickname=" + vo.getNickname();
                        withdrawalResult(param);
                    }
                });

                //취소버튼을 누르면 다이얼로그 자동 dismiss()
                alertDialogBuilder.setNegativeButton("취소", null);

                //설정된 값으로 다이얼로그 최종생성
                alertDialog = alertDialogBuilder.create();

                //사용자에게 보여줌
                alertDialog.show();

            } else {
                new SweetAlertDialog(ProfileModifyActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setContentText("비밀번호가 틀렸습니다.")
                        .show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetProfile(String param){
        String resultStr = "";
        try {
             resultStr = new SpringTask(Util.SERVER_IP_USER_RESET,ProfileModifyActivity.this).execute(param).get();

            if (resultStr.equals("YES")) {

                final SweetAlertDialog sweet = new SweetAlertDialog(ProfileModifyActivity.this,SweetAlertDialog.SUCCESS_TYPE);
                sweet.setTitleText("정보 수정 완료!");
                sweet.show();
                sweet.findViewById(R.id.confirm_button).setVisibility(View.GONE);

                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //settings로 보내기 위해 SharedPreferences에 page 2로 저장
                        SharedPreferences page = getSharedPreferences("page", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editorPage = page.edit();
                        editorPage.putInt("curPage",2);
                        editorPage.commit();

                        //정보 수정 후 설정 페이지로 이동
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                        //번들에 데이터 담고
                        Bundle bundle = new Bundle();
                        bundle.putString("nickname", nickname );
                        bundle.putString("gender", gender);
                        bundle.putString("mbti", vo.getMbti());

                        //인텐트에 담은 뒤
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                        sweet.dismiss();
                        timer.cancel();
                    }
                },900);

            } else {
                new SweetAlertDialog(ProfileModifyActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("정보 수정 실패")
                        .setContentText("다시 시도해 주세요.")
                        .show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        //번들에 데이터 담고
        Bundle bundle = new Bundle();
        bundle.putString("nickname", vo.getNickname());
        bundle.putString("gender", vo.getGender());
        bundle.putString("mbti", vo.getMbti());

        //인텐트에 담은 뒤
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}