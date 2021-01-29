package com.mbti.typemate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.drawerlayout.widget.DrawerLayout;

import com.mbti.typemate.adapter.SearchListAdapter;
import com.mbti.typemate.async.SpringTask;
import com.mbti.typemate.util.Util;
import com.mbti.typemate.vo.UserVO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchList {
    Activity activity; // 0807
    Context context;

    Button btn_search;
    ImageButton btn_filter, btn_mbti_info; //0811
    CheckBox btn_man, btn_woman;
    CheckBox[] btn_mbti;
    DrawerLayout drawer_layout;
    ListView search_list;
    View drawer;
    WebView web_mbti_info; // 0811

    static boolean b_mbti_info;
    String user_nickname;
    String gender;
    String mbti;
    ArrayList<String> mbtiArr;
    ArrayList<UserVO> s_list;
    SearchListAdapter adapter;

    int start = 1;

    //0807
    public SearchList(final Activity activity, Context context, String user_nickname) {
        this.activity = activity;
        this.context = context;
        this.user_nickname = user_nickname;
        gender = "";
        mbti = "";
        mbtiArr = new ArrayList<>();
        s_list = new ArrayList<>();
        adapter = null;

        drawer_layout = activity.findViewById(R.id.drawer_layout);
        drawer = activity.findViewById(R.id.drawer);
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        search_list = activity.findViewById(R.id.search_list);
        btn_filter = activity.findViewById(R.id.btn_filter);
        btn_man = activity.findViewById(R.id.btn_man);
        btn_woman = activity.findViewById(R.id.btn_woman);
        btn_search = activity.findViewById(R.id.btn_search);
        btn_mbti_info = activity.findViewById(R.id.btn_mbti_info); //0811
        web_mbti_info = activity.findViewById(R.id.web_mbti_info); //0811

        // css 호환문제를 해결해주는 코드
        web_mbti_info.getSettings().setJavaScriptEnabled(true);

        web_mbti_info.loadUrl("https://www.16personalities.com/ko/성격-유형");

        //다른 페이지 못가도록 막기
        web_mbti_info.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }
        });

        btn_mbti = new CheckBox[16];
        for (int i = 0; i < btn_mbti.length; i++) {

            int getID = activity.getResources().getIdentifier("btn_mbti" + (i + 1), "id", "com.mbti.typemate");
            btn_mbti[i] = activity.findViewById(getID);

            btn_mbti[i].setOnCheckedChangeListener(mbtiClick);
        }

        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.openDrawer(drawer);
            }
        });

        btn_mbti_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(b_mbti_info){
                    showList();
                } else {
                    b_mbti_info = true;
                    search_list.setVisibility(View.INVISIBLE);
                    web_mbti_info.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_man.setOnCheckedChangeListener(genderClick);
        btn_woman.setOnCheckedChangeListener(genderClick);

        btn_search.setOnClickListener(searchClick);

        String param = "nickname=" + user_nickname + "&gender=" + gender + "&mbti=" + mbti;
        SearchResult(param);
    } // 생성자

    CompoundButton.OnCheckedChangeListener mbtiClick = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked) {
                mbtiArr.add(compoundButton.getText().toString());
            } else {
                mbtiArr.remove(compoundButton.getText().toString());
            }
        }
    }; // mbtiClick

    CompoundButton.OnCheckedChangeListener genderClick = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            switch (compoundButton.getId()){
                case R.id.btn_man:
                    if(isChecked){
                        gender = "남";
                        btn_woman.setChecked(false);
                    }
                    break;
                case R.id.btn_woman:
                    if(isChecked){
                        gender = "여";
                        btn_man.setChecked(false);
                    }
                    break;
            }
        }
    };

    View.OnClickListener searchClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!btn_man.isChecked() && ! btn_woman.isChecked()) {
                gender = "";
            }

            mbti = "";

            //ISFP|INTJ...
            if (mbtiArr.size() > 0) {
                for (String arr : mbtiArr) {
                    mbti += arr + "|";
                }
                mbti = mbti.substring(0, mbti.length() - 1);
            }

            s_list.clear();
            adapter = null;
            drawer_layout.closeDrawer(drawer);

            String param = "nickname=" + user_nickname + "&gender=" + gender + "&mbti=" + mbti;
            SearchResult(param);

        }
    }; // searchClick

    private void SearchResult(String param) {
        String resultStr = "";//async에서 처리한값을 반환받는 변수

        try {
            resultStr = new SpringTask(Util.SERVER_IP_SEARCH, context).execute(param).get();
            if (resultStr.equals("")) {
                return;
            }
            JSONObject jsonObject = new JSONObject(resultStr);
            int cnt = jsonObject.getInt("cnt");
            JSONArray res = jsonObject.getJSONArray("res");

            //0805
            for (int i = 0; i < cnt; i++) {
                UserVO vo = new UserVO();
                JSONObject object = res.getJSONObject(i);
                vo.setU_idx(object.getInt("u_idx"));
                vo.setNickname(object.getString("nickname"));
                vo.setMbti(object.getString("mbti"));
                vo.setGender(object.getString("gender"));
                vo.setId(object.getString("id"));
                vo.setBirth(object.getString("birth"));
                vo.setName(object.getString("name"));
                s_list.add(vo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //doInBackground의 통신이 완료되면 return값을 가지고 호출되는 메서드
        if (adapter == null) {
            adapter = new SearchListAdapter(
                    context, R.layout.search_item, s_list, search_list, user_nickname);

            //리스트뷰에 스크롤 감지자를 등록
            //search_list.setOnScrollListener(scrollListener);

            //리스트뷰에 footer를 등록(반드시 setAdapter보다 위에서 작성!
            //search_list.addFooterView(footerView);
            //footerView.setVisibility(View.VISIBLE);

            search_list.setAdapter(adapter);
        }

        //어댑터에 변경사항이 발생하면 내용을 갱신!
        adapter.notifyDataSetChanged();
        //mLockListView = false;
    }

    // mbti_info 보고 뒤로 누르면 다시 리스트보여주도록
    public void showList(){
        b_mbti_info = false;
        search_list.setVisibility(View.VISIBLE);
        web_mbti_info.setVisibility(View.GONE);
    }

}
