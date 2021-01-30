package com.mbti.typemate.adapter;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mbti.typemate.ChatActivity;
import com.mbti.typemate.R;
import com.mbti.typemate.util.Util;
import com.mbti.typemate.vo.ChatVO;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomAdapter extends ArrayAdapter<ChatVO> {

    Context context;//현재 View
    ArrayList<ChatVO> arr;//어댑터의 내용을 가지고있는 리스트
    int resources;//참조할 xml(View로 Inflate해줄거임)
    ListView chatRoom_list;//사용할 리스트뷰
    String myNickname;

    Dialog dialog;//삭제를 띄워줄 다이얼로그

    AlertDialog alertDialog;
    //얼럿다이얼로그 생성
    AlertDialog.Builder alertDialogBuilder;

    ViewHolder viewHolder;

    public ChatRoomAdapter(@NonNull Context context, int resource, ArrayList<ChatVO> chatRoomArr, ListView chatRoom_list, String myNickname) {
        super(context, resource, chatRoomArr);

        alertDialogBuilder = new AlertDialog.Builder(context);

        this.context = context;
        this.arr = chatRoomArr;
        this.resources = resource;
        this.chatRoom_list = chatRoom_list;
        this.myNickname = myNickname;
    }//오버로딩 생성자

    @NonNull
    @Override
    //position은 객체의 index를 가지고있음
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null){
            convertView = inflater.inflate(resources, parent, false);
            viewHolder = new ChatRoomAdapter.ViewHolder();
            viewHolder.chat_content = (TextView)convertView.findViewById(R.id.chat_content);
            viewHolder.u_nickname = (TextView)convertView.findViewById(R.id.u_nickname);
            viewHolder.chat_time = (TextView) convertView.findViewById(R.id.chat_time);
            viewHolder.gen_img = (ImageView)convertView.findViewById(R.id.gen_img);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ChatRoomAdapter.ViewHolder)convertView.getTag();
        }

        viewHolder.chat_content.setText(arr.get(position).getMessage());
        viewHolder.u_nickname.setText(arr.get(position).getNickname());
        viewHolder.chat_time.setText(arr.get(position).getTime());
        if (arr.get(position).getGender().equals("남")){
            viewHolder.gen_img.setImageResource(R.drawable.profile_man);
        }else{
            viewHolder.gen_img.setImageResource(R.drawable.profile_woman);
        }

        this.chatRoom_list.setOnItemClickListener(onClick);
        this.chatRoom_list.setOnItemLongClickListener(longClick);

        return convertView;
    }//getView()

    AdapterView.OnItemClickListener onClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent chatIntent = new Intent(context, ChatActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("opponentNickname", arr.get(i).getNickname());
            bundle.putString("myNickname", myNickname);
            chatIntent.putExtras(bundle);
            context.startActivity(chatIntent);
        }
    };


    AdapterView.OnItemLongClickListener longClick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {

            dialog = new Dialog(context);
            dialog.setContentView(R.layout.chat_room_dialog);

            Button exit_btn = dialog.findViewById(R.id.exit_btn);
            TextView user_name_txt = dialog.findViewById(R.id.user_name_txt);

            user_name_txt.setText(arr.get(i).getNickname());

            //채팅방 나가기 버튼 눌렀을때 firebase에서 값을 삭제해주는 부분
            exit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    alertDialogBuilder.setTitle("채팅방 나가기").setMessage("채팅방을 나가시겠습니까?\n\n나가기를 하면 대화내용이 모두 삭제되고\n채팅목록에서도 삭제됩니다.");

                    //다이얼로그에 OK버튼 만들고 감지자 정의(누르면 로그아웃이 되고 로그인 액티비티로 전환됨)
                    alertDialogBuilder.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i2) {
                            Util.CHAT_REF.child(myNickname).child(arr.get(i).getNickname()).removeValue();
                            dialog.dismiss();
                        }
                    });
                    //취소버튼을 누르면 다이얼로그 자동 dismiss()
                    alertDialogBuilder.setNegativeButton("취소", null);

                    //설정된 값으로 다이얼로그 최종생성
                    alertDialog = alertDialogBuilder.create();

                    //사용자에게 보여줌
                    alertDialog.show();
                }

            });

            dialog.show();

            return true;
        }
    };

    public void upDateItemList(ArrayList<ChatVO> chatRoomArr){
        this.arr = chatRoomArr;
        notifyDataSetChanged();
    }

    //뷰홀더 패턴
    public static class ViewHolder{
        TextView u_nickname, chat_content, chat_time;
        ImageView gen_img;
    }
}
























