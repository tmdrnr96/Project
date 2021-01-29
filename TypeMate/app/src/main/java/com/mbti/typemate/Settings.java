package com.mbti.typemate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mbti.typemate.adapter.SearchListAdapter;
import com.mbti.typemate.async.SpringTask;
import com.mbti.typemate.util.Util;
import com.mbti.typemate.vo.UserVO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Settings {
    Activity activity;
    Context context;
    private UserVO vo;
    String user_nickname;
    String gender;
    String user_id;

    Dialog dialog;
    AlertDialog alertDialog;
    AlertDialog.Builder alertDialogBuilder;

    ImageView profile_img;
    TextView txt_nickname;
    ImageButton btn_profile, btn_reset_pwd, btn_reset_mbti, btn_logout;

    public Settings(Activity activity, Context context, String user_nickname, String gender, String user_id) {
        vo = new UserVO();
        alertDialogBuilder = new AlertDialog.Builder(context);

        this.activity = activity;
        this.context = context;
        this.user_nickname = user_nickname;
        this.gender = gender;
        this.user_id = user_id;

        String param = "nickname=" + user_nickname;
        UserInfo(param);

        profile_img = activity.findViewById(R.id.set_profile_img);
        txt_nickname = activity.findViewById(R.id.txt_set_nickname);

        btn_profile = activity.findViewById(R.id.btn_profile);
        btn_reset_pwd = activity.findViewById(R.id.btn_reset_pwd);
        btn_reset_mbti = activity.findViewById(R.id.btn_reset_mbti);
        btn_logout = activity.findViewById(R.id.btn_logout);

        if (!gender.equals("남")) {
            profile_img.setImageResource(R.drawable.profile_woman1);
        }
        txt_nickname.setText(user_nickname);

        btn_profile.setOnClickListener(click);
        btn_reset_pwd.setOnClickListener(click);
        btn_reset_mbti.setOnClickListener(click);
        btn_logout.setOnClickListener(click);

    } // 생성자

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = null;
            Bundle bundle = null;
            switch (view.getId()) {
                case R.id.btn_profile:
                    showProfile();
                    break;
                case R.id.btn_reset_pwd:
                    resetPwd();
                    break;
                case R.id.btn_reset_mbti:
                    intent = new Intent(context, MbtiModifyActivity.class);
                    bundle = new Bundle();
                    bundle.putSerializable("u_vo", vo);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    activity.finish();
                    break;
                case R.id.btn_logout:
                    logout();
                    break;
                case R.id.btn_reset_profile:
                    intent = new Intent(context, ProfileModifyActivity.class);
                    bundle = new Bundle();
                    bundle.putSerializable("u_vo", vo);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    dialog.dismiss();
                    activity.finish();
                    break;
            }
        }
    };

    private void showProfile() {


        // 다이얼로그 생성
        dialog = new Dialog(context);

        // 다이얼로그의 자체 배경을 투명하게 설정
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //다이얼로그가 참조할 레이아웃 등록
        dialog.setContentView(R.layout.settings_dialog);

        ScrollView dialog_scroll = dialog.findViewById(R.id.dialog_scroll);
        dialog_scroll.setClipToOutline(true);

        ImageView profile_img = dialog.findViewById(R.id.profile_img); //0805
        TextView txt_id = dialog.findViewById(R.id.txt_id);
        TextView txt_name = dialog.findViewById(R.id.txt_name);
        TextView txt_nickname = dialog.findViewById(R.id.txt_nickname);
        TextView txt_age = dialog.findViewById(R.id.txt_age);
        TextView txt_birth = dialog.findViewById(R.id.txt_birth);
        TextView txt_tel = dialog.findViewById(R.id.txt_tel);
        TextView txt_mbti = dialog.findViewById(R.id.txt_mbti);

        if (!gender.equals("남")) {
            profile_img.setImageResource(R.drawable.profile_woman1);
        }

        String mbti = vo.getMbti();
        TextView[] txt_mbtiArr = new TextView[4];
        TextView[] txt_mbtiArr_info = new TextView[4];
        for (int j = 0; j < mbti.length(); j++) {
            int getID = context.getResources().getIdentifier("txt_mbti" + (j + 1), "id", "com.mbti.typemate");
            int getID_info = context.getResources().getIdentifier("txt_mbti" + (j + 1) + "_info", "id", "com.mbti.typemate");

            txt_mbtiArr[j] = dialog.findViewById(getID);
            txt_mbtiArr_info[j] = dialog.findViewById(getID_info);

            String[] temp = MbtiInfo.getMbtiInfo().getMbti(j, mbti.charAt(j));
            txt_mbtiArr[j].setText(temp[0]);
            txt_mbtiArr_info[j].setText(temp[1]);
        }

        String age = "";
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(vo.getBirth());
            Calendar c = Calendar.getInstance();
            int now = c.getWeekYear();

            c.setTime(date);
            int bir = c.getWeekYear();

            age = (now - bir + 1) + "";
        } catch (ParseException e) {
            e.printStackTrace();
        }

        txt_id.setText(vo.getId());
        txt_nickname.setText(vo.getNickname());
        txt_name.setText(vo.getName());
        txt_age.setText(age);
        txt_birth.setText(vo.getBirth());
        txt_mbti.setText(mbti);

        String tel = vo.getTel();
        Log.i("MY", "tel : " + tel );
        Log.i("MY", "tel : " + tel );
        Log.i("MY", "tel : " + tel );
        Log.i("MY", "tel : " + tel );
        Log.i("MY", "tel : " + tel );
        String tel1 = tel.substring(0, 3);
        String tel3 = tel.substring(tel.length() - 4, tel.length());
        String tel2 = tel.substring(tel1.length(), tel.length()-tel3.length());
        txt_tel.setText(tel1 + "-" + tel2 + "-" + tel3);

        //0810
        //버튼에 이벤트 감지자 등록
        Button btn_reset_profile = dialog.findViewById(R.id.btn_reset_profile);
        btn_reset_profile.setOnClickListener(click);

        dialog.show();
    }//showProfile()


    private void resetPwd() {
        Intent intent = new Intent(context, PwdResetActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("myNickname", user_nickname);
        bundle.putString("user_id", user_id);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    //유저 정보 가져오기
    private void UserInfo(String param) {
        String resultStr = "";//async에서 처리한값을 반환받는 변수

        try {
            resultStr = new SpringTask(Util.SERVER_IP_USER_INFO, context).execute(param).get();
            if (resultStr.equals("")) {
                return;
            }
            JSONObject object = new JSONObject(resultStr);

            vo.setU_idx(object.getInt("u_idx"));
            vo.setId(object.getString("id"));
            vo.setPwd(object.getString("pwd"));
            vo.setNickname(object.getString("nickname"));
            vo.setMbti(object.getString("mbti"));
            vo.setGender(object.getString("gender"));
            vo.setBirth(object.getString("birth"));
            vo.setName(object.getString("name"));
            vo.setTel(object.getString("tel"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //로그아웃
    private void logout() {
        //타이틀과 메세지 지정
        alertDialogBuilder.setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?");

        //다이얼로그에 OK버튼 만들고 감지자 정의(누르면 로그아웃이 되고 로그인 액티비티로 전환됨)
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //로그인Activity로 이동하는 Intent생성
                Intent loginPageIntent = new Intent(context, LoginActivity.class);

                //로그인Activity로 이동
                context.startActivity(loginPageIntent);

                //SharedPreferences에 저장됐던 유저의 정보는 다 지워지게끔 처리
                SharedPreferences auto = context.getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                editor.clear();
                //커밋 꼭 해줘야 적용됨
                editor.commit();

                Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                activity.finish();
            }
        });

        //취소버튼을 누르면 다이얼로그 자동 dismiss()
        alertDialogBuilder.setNegativeButton("취소", null);

        //설정된 값으로 다이얼로그 최종생성
        alertDialog = alertDialogBuilder.create();

        //사용자에게 보여줌
        alertDialog.show();
    }
}
