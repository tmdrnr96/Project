package com.mbti.typemate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.mbti.typemate.adapter.ChatRoomAdapter;
import com.mbti.typemate.async.SpringTask;
import com.mbti.typemate.util.Util;
import com.mbti.typemate.vo.ChatVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatRoomActivity extends AppCompatActivity {

    String myNickname;

    ListView chatRoom_list;
    ArrayList<ChatVO> chatRoom_arr;

    ChatVO chatListVO;

    ChatRoomAdapter chatRoomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Intent intent = getIntent();
        myNickname = intent.getStringExtra("myNickname");

        //0806하준 추가
        chatRoom_list = findViewById(R.id.chatRoom_list);
        //어댑터객체 생성

        chatRoom_arr = new ArrayList<>();
        chatRoomAdapter = new ChatRoomAdapter(ChatRoomActivity.this, R.layout.chatroom_list_form, chatRoom_arr, chatRoom_list, myNickname);

        //리스트에 어댑터를 넣어줌(이때 getView()가 자동 호출됨)
        chatRoom_list.setAdapter(chatRoomAdapter);

        //chat\내 닉네임의 하위목록에 값 변경에 대한 감지자
        Util.CHAT_REF.child(myNickname).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                chatListVO = new ChatVO();
                Log.i("MY", "key : "+snapshot.getKey());
                Log.i("MY", "value : "+snapshot.getValue());

                //key값으로 존재하는 상대방의 닉네임
                String opponentNickname = snapshot.getKey();

                String url = Util.SERVER_IP_CHECK_GENDER;

                String param = "nickname=" + opponentNickname;

                try {
                    //여러개의 PC에서 다른DB값으로 접근할시에 firebase의 값과 데이터가 혼동되어 null값이 넘어오고 오류가 난다.
                    String result = new SpringTask(url, ChatRoomActivity.this).execute(param).get();
                    chatListVO.setGender(result);
                } catch (Exception e) {
                    Log.i("CATCH", e.getMessage());
                }

                Map map = (HashMap)snapshot.getValue();

                Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();

                while (iterator.hasNext()){
                    String key = iterator.next().getKey();//Hashmap형태를 Iterator로 반환해주는 Children()에서 Key()값을 꺼재온다
                    Map result = (HashMap) map.get(key);//HashMap형태로 담겨있는 value값을 map에 담아준 후
                    chatListVO.setMessage((String) result.get("message"));//그 안에 key가 message인 value값을 꺼내온다
                    chatListVO.setTime((String) result.get("time"));
                }

                //위에서 추출했던 상대 유저의 닉네임을 VO에 담는다
                chatListVO.setNickname(opponentNickname);
                //상대 유저의 닉네임을 담고있는 VO를 어댑터에 넣어줄 ArrayList에 넣어준다
                chatRoom_arr.add(chatListVO);
                //어댑터에서 값이 변경될때마다 list를 업데이트해줌
                chatRoomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}