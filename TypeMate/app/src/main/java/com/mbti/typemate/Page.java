package com.mbti.typemate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

public class Page extends Fragment {
    // 각각의 Fragment가 하나로 연결되어 슬라이드를 통해 페이징 전환이 가능하도록 처리
    int position;

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout layout = null;

        switch (position){
            case 0:
                return (DrawerLayout) inflater.inflate(R.layout.fragment_page_search, container, false);
            case 1:
                layout = (LinearLayout) inflater.inflate(R.layout.fragment_page_chat, container, false);
                break;
            case 2:
                layout = (LinearLayout) inflater.inflate(R.layout.fragment_page_settings, container, false);
                break;
        }
        return layout;
    }
}