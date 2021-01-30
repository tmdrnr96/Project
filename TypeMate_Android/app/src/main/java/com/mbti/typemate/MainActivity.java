package com.mbti.typemate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.mbti.typemate.adapter.ChatRoomAdapter;
import com.mbti.typemate.adapter.MyPagerAdapter;
import com.mbti.typemate.async.SpringTask;
import com.mbti.typemate.util.Util;
import com.mbti.typemate.vo.ChatVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    ViewPager pager;
    ImageButton[] menus;

    String myNickname;
    String user_gender; // 0807
    static public String user_mbti;
    String user_id;
    String user_pwd;

    SearchList searchList;

    // 페이지 저장을 위한 변수
    SharedPreferences page;
    int curPage;
    boolean checkCnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            user_id =  bundle.getString("user_id");
            user_pwd =  bundle.getString("user_pwd");
        }

        myNickname = intent.getStringExtra("nickname");
        user_gender = intent.getStringExtra("gender");
        user_mbti = intent.getStringExtra("mbti");

        page = getSharedPreferences("page", Activity.MODE_PRIVATE);
        curPage = page.getInt("curPage", 1);

        pager = findViewById(R.id.pager);
        //버튼검색
        menus = new ImageButton[3];
        for (int i = 0; i < menus.length; i++) {

            int getID = getResources().getIdentifier("menu" + (i + 1), "id", "com.mbti.typemate");
            menus[i] = findViewById(getID);
            menus[i].setTag(i);

            menus[i].setOnClickListener(click);
        }

        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager())); // getItem호출
        pager.setCurrentItem((curPage + 1) % 3);
        menus[curPage].setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < menus.length; i++) {
                    if (position == i) {
                        menus[i].setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
                    } else {
                        menus[i].setColorFilter(Color.parseColor("#9a9a9a"), PorterDuff.Mode.SRC_IN);
                    }
                }

                switch (position) {
                    case 0:
                        handler.sendEmptyMessage(2);
                        break;
                    case 1:
                        handler.sendEmptyMessage(3);
                        break;
                    case 2:
                        handler.sendEmptyMessage(4);
                        break;
                }
                curPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        handler.sendEmptyMessage(1);

    } //onCreate()

    private void chatList() {
        Intent intent = new Intent(MainActivity.this, ChatRoomActivity.class);
        intent.putExtra("myNickname", myNickname);
        startActivity(intent);
    } // chatList()

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //클릭한 버튼의 태그를 감지하여 페이지 전환
            pager.setCurrentItem((int) view.getTag());
        }
    }; //click

    final View.OnClickListener logoutClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };//logoutClick

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = page.edit();
        editor.putInt("curPage", curPage);
        editor.commit();
    }//onPause()

    @Override
    public void onBackPressed() {
        if(SearchList.b_mbti_info){
            searchList.showList();
        }else if (!checkCnt) {
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
            switch (msg.what) {
                case 0:
                    checkCnt = false;
                    break;
                case 1:
                    pager.setCurrentItem(curPage); // 채팅리스트부터
                    break;
                case 2:
                    searchList = new SearchList(MainActivity.this, MainActivity.this, myNickname);
                    break;
                case 3:
                    new ChatRoom(MainActivity.this, MainActivity.this, myNickname);
                    break;
                case 4:
                    new Settings(MainActivity.this, MainActivity.this, myNickname, user_gender, user_id);
                    break;
            }
        }
    };

}//MainActivity 클래스