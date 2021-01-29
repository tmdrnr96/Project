package com.mbti.typemate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MbtiTestActivity extends Activity {

    WebView web_view_join;
    String id, pwd, repwd, name, tel, nickname, gender, birth, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mbti_test);

        Intent intent = getIntent();

        id = intent.getExtras().getString("id");
        pwd = intent.getExtras().getString("pwd");
        repwd = intent.getExtras().getString("repwd");
        nickname = intent.getExtras().getString("nickname");
        name = intent.getExtras().getString("name");
        tel = intent.getExtras().getString("tel");
        gender = intent.getExtras().getString("gender");
        birth = intent.getExtras().getString("birth");

        web_view_join = findViewById(R.id.web_view_join);

        // css 호환문제를 해결해주는 코드
        web_view_join.getSettings().setJavaScriptEnabled(true);

        web_view_join.loadUrl("http://www.16personalities.com/ko/무료-성격-유형-검사");

        web_view_join.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView web_view, WebResourceRequest request) {
                String url = request.getUrl().toString();

                //url에 -e이나 -i가 있을 때 얼럿창 띄우기
                if (url.contains("-e") || url.contains("-i")) {
                    /*코드작성*/
                    type = url.substring(url.indexOf('-') + 1, url.length()).toUpperCase();

                    showAlert();

                    return true;
                }
                return false;
            }
        });

    }//onCreate()

    private void showAlert() {
        //타이틀과 메세지 지정
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MbtiTestActivity.this);

        alertDialogBuilder.setTitle("나의 MBTI type").setMessage("회원님의 MBTI는 '" + type + "'입니다.'");

        //다이얼로그에 OK버튼 만들고 감지자 정의
        alertDialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = new Intent(MbtiTestActivity.this, JoinActivity.class);

                //JoinActivity에 들어갈 항목 다시 보내기

                intent.putExtra("mbti",""+type);
                intent.putExtra("id", ""+id);
                intent.putExtra("pwd", ""+pwd);
                intent.putExtra("repwd", ""+repwd);
                intent.putExtra("nickname", ""+nickname);
                intent.putExtra("gender",""+gender);
                intent.putExtra("birth", ""+ birth);
                intent.putExtra("name",""+name);
                intent.putExtra("tel",""+tel);

                startActivity(intent);

                finish();
            }
        });

        //설정된 값으로 다이얼로그 최종생성
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);//뒤로가기 버튼, 배경 터치로 인해 꺼짐 방지
        //사용자에게 보여줌
        alertDialog.show();

    }




}