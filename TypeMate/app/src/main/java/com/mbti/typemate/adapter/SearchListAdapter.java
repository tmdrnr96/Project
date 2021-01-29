package com.mbti.typemate.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mbti.typemate.ChatActivity;
import com.mbti.typemate.MainActivity;
import com.mbti.typemate.MbtiInfo;
import com.mbti.typemate.R;
import com.mbti.typemate.vo.UserVO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SearchListAdapter extends ArrayAdapter<UserVO> implements AdapterView.OnItemClickListener {

    Context context;
    int resource;
    ArrayList<UserVO> list;

    UserVO vo;
    Dialog dialog;

    //0805
    MbtiInfo mbtiInfo;

    String myNickname;

    public SearchListAdapter(Context context, int resource, ArrayList<UserVO> list, ListView myListView, String myNickname) {
        super(context, resource, list);

        this.context = context;
        this.resource = resource;
        this.list = list;
        this.myNickname = myNickname;

        mbtiInfo = mbtiInfo.getMbtiInfo();

        //리스트뷰에 이벤트 감지자 등록
        myListView.setOnItemClickListener(this);

    }

    //0805
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater linf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = linf.inflate(resource, null);
        vo = list.get(position);

        ImageView profile_img = convertView.findViewById(R.id.profile_img);
        TextView txt_nickname = convertView.findViewById(R.id.txt_nickname);
        TextView txt_mbti = convertView.findViewById(R.id.txt_mbti);

        if (!vo.getGender().equals("남")) {
            profile_img.setImageResource(R.drawable.profile_woman1);
        }
        txt_nickname.setText(vo.getNickname());
        txt_mbti.setText(vo.getMbti());

        return convertView;
    }// getView()

    //0805
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //리스트뷰의 항목 클릭 감지자

        final String nickname = list.get(i).getNickname();
        String name = list.get(i).getName();
        String birth = list.get(i).getBirth();
        String gender = list.get(i).getGender();
        String mbti = list.get(i).getMbti();

        //0806
        String age = "";
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(birth);
            Calendar c = Calendar.getInstance();
            int now = c.getWeekYear();

            c.setTime(date);
            int bir = c.getWeekYear();

            age = (now - bir + 1) + "";
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 다이얼로그 생성
        dialog = new Dialog(context);

        // 다이얼로그의 자체 배경을 투명하게 설정
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //다이얼로그가 참조할 레이아웃 등록
        dialog.setContentView(R.layout.searchlist_dialog);

        ScrollView dialog_scroll = dialog.findViewById(R.id.dialog_scroll);
        dialog_scroll.setClipToOutline(true);

        ImageView profile_img = dialog.findViewById(R.id.profile_img); //0805
        TextView txt_nickname = dialog.findViewById(R.id.txt_nickname);
        TextView txt_name = dialog.findViewById(R.id.txt_name);
        TextView txt_age = dialog.findViewById(R.id.txt_age);
        TextView txt_birth = dialog.findViewById(R.id.txt_birth);
        TextView txt_mbti = dialog.findViewById(R.id.txt_mbti);
        TextView txt_chemi = dialog.findViewById(R.id.txt_chemi);

        //0806
        if (!gender.equals("남")) {
            profile_img.setImageResource(R.drawable.profile_woman1);
        }

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

        txt_nickname.setText(nickname);
        txt_name.setText(name);
        txt_age.setText(age);
        txt_birth.setText(birth);
        txt_mbti.setText(mbti);
        txt_chemi.setText(MbtiInfo.getMbtiInfo().getChemistry(MainActivity.user_mbti, mbti)); // 0806

        //버튼에 이벤트 감지자 등로
        Button btn_chat = dialog.findViewById(R.id.btn_chat);

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(context, ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("myNickname", myNickname);
                bundle.putString("opponentNickname", nickname);
                chatIntent.putExtras(bundle);
                context.startActivity(chatIntent);
                dialog.dismiss();//채팅창으로 이동하면 현재 보고있느 다이얼로그 숨겨줌
            }
        });

        dialog.show();
    }
}
