package com.mbti.typemate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mbti.typemate.async.SpringTask;
import com.mbti.typemate.util.Util;
import com.mbti.typemate.vo.UserVO;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MbtiModifyActivity extends AppCompatActivity {

    UserVO vo;
    String mbti;

    WebView web_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mbti_modify);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        vo = (UserVO) bundle.getSerializable("u_vo");
        Log.i("TEST",vo.getU_idx() + ", " + vo.getGender() + ", " + vo.getNickname());

        web_view = findViewById(R.id.web_view);
        // css&javascript 호환문제를 해결해주는 코드
        web_view.getSettings().setJavaScriptEnabled(true);

        web_view.loadUrl("https://www.16personalities.com/ko/무료-성격-유형-검사");

        web_view.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();

                //url에 -e이나 -i가 있을 때 얼럿창 띄우기
                if (url.contains("-e") || url.contains("-i")) {
                    mbti = url.substring(url.indexOf('-') + 1, url.length()).toUpperCase();

                    showAlert();

                    return true;
                }
                return false;
            }
        });
    }

    private void showAlert() {
        //타이틀과 메세지 지정
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MbtiModifyActivity.this);

        alertDialogBuilder.setTitle("MBTI 변경").setMessage("회원님의 MBTI는 '" + mbti + "'입니다.\n'" + mbti + "'로 변경하시겠습니까?");

        //다이얼로그에 OK버튼 만들고 감지자 정의(누르면 회원탈퇴)
        alertDialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String param = "u_idx=" + vo.getU_idx() + "&mbti=" + mbti;
                resetMbti(param);
            }
        });

        //취소버튼을 누르면 다이얼로그 자동 dismiss()
        alertDialogBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cancleReset();
            }
        });

        //설정된 값으로 다이얼로그 최종생성
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        //사용자에게 보여줌
        alertDialog.show();

    }

    // mbti 수정을 위한 스프링 연동
    private void resetMbti(String param) {
        String resultStr = "";

        try {
            resultStr = new SpringTask(Util.SERVER_IP_RESET_MBTI, this).execute(param).get();

            // YES 반환 받으면 mbti 변경 성공이므로 로그인창으로 이동
            if (resultStr.equals("YES")) {
                final SweetAlertDialog sweet = new SweetAlertDialog(MbtiModifyActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                sweet.setTitleText("MBTI 변경 완료!");
                sweet.show();
                sweet.findViewById(R.id.confirm_button).setVisibility(View.GONE);

                //정보 수정 후 설정 페이지로 이동
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                //메인에 필요한 값들을 bundle에 담아 준다.
                Bundle bundle = new Bundle();
                bundle.putString("nickname", vo.getNickname());
                bundle.putString("gender", vo.getGender());
                bundle.putString("mbti", mbti);

                //인텐트에 담은 뒤
                intent.putExtras(bundle);
                startActivity(intent);
                sweet.dismiss();
                finish();

            } else {
                new SweetAlertDialog(MbtiModifyActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("MBTI 변경경 실패")
                        .setContentText("다시 시도해 주세요.")
                        .show();
                showAlert();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancleReset() {
        Intent intent = new Intent(MbtiModifyActivity.this, MainActivity.class);

        //메인에 필요한 값들을 bundle에 담아 준다.
        Bundle bundle = new Bundle();
        bundle.putString("nickname", vo.getNickname());
        bundle.putString("gender", vo.getGender());
        bundle.putString("mbti", vo.getMbti());

        //인텐트에 담은 뒤
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        cancleReset();
    }
}