package com.mbti.typemate;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class IntroActivity extends Activity {

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        handler.postDelayed(runnable,800);
    }
    @Override
    protected void onPause() {
        super.onPause();
        // 화면을 벗어나면, handler 에 예약해 놓은 작업을 취소
        handler.removeCallbacks(runnable); // 예약 취소
    }


}