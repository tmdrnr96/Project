package com.mbti.typemate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mbti.typemate.adapter.ChatAdapter;
import com.mbti.typemate.util.Util;
import com.mbti.typemate.vo.ChatVO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ChatActivity extends AppCompatActivity {

    EditText message;
    Button back_btn, send_btn;
    LinearLayout send_btn_linear;
    ListView chat_list;
    ArrayList<ChatVO> arr;
    TextView opponent;

    //내 닉네임, 상대방 닉네임, 내 성별, 내 생일, 내 성격유형, 서버에서 받아온 닉네임
    String myNickname, opponentNickname, gender, birth, mbti;//안드로이드는 전역변수도 자동초기화가 안된다?


    ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        if (bundle != null){
            opponentNickname = bundle.getString("opponentNickname");
            myNickname = bundle.getString("myNickname");
            gender = bundle.getString("gender");
            birth = bundle.getString("birth");
            mbti = bundle.getString("mbti");
        }

        message = findViewById(R.id.message);
        send_btn = findViewById(R.id.send_btn);
        back_btn = findViewById(R.id.back_btn);
        chat_list = findViewById(R.id.chat_list);

        send_btn_linear = findViewById(R.id.send_btn_linear);

        opponent  = findViewById(R.id.opponent);

        //채팅창 상단의 누구와의 대화방인지 setting
        opponent.setText(opponentNickname);

        //어댑터에 넘겨줄 ArrayList 객체 생성
        arr = new ArrayList<>();

        //어댑터 생성
        //파라미터 순서는 화면제어권자(context), 참조할 레이아웃(int값), 리스트뷰에 넣어줄 아이템, 사용할 리스트뷰
        chatAdapter = new ChatAdapter(ChatActivity.this, R.layout.my_chat_list_form, arr, chat_list, myNickname, opponentNickname);

        //리스트뷰에 어댑터를 추가
        //이때 자동으로 Adapter에 있는 getView()메서드가 자동호출됨(arr의 size만큼)
        chat_list.setAdapter(chatAdapter);

        //전송 버튼 클릭시 이벤트처리
        send_btn.setOnClickListener(sendBtnClick);
        send_btn_linear.setOnClickListener(sendBtnClick);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //onCreate()시 디비의 값을 감지해 호출되는 감지자를 만듬
        Util.CHAT_REF.child(myNickname).child(opponentNickname).addChildEventListener(firebaseEvt);

    }//onCreate()

    ChildEventListener firebaseEvt = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            //채팅 보내기
            Log.i("MY", ""+snapshot.getKey());
            ChatVO vo = snapshot.getValue(ChatVO.class);//ChatVO타입으로 DB에서 데이터를 받아온다
            vo.setMsgKey(snapshot.getKey());//메세지의 key를 VO에 담아서 어댑터에 넣어준다
            arr.add(vo);
            chatAdapter.notifyDataSetChanged();
            chat_list.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            chat_list.setSelection(arr.size());

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            //채팅내용은 변경할일이 없으니 생략
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            int cnt;
            cnt = chatAdapter.getCount();//어댑터 아이템의 갯수

            Log.i("MY",snapshot.getKey());

            //어댑터에 아이템이 있을때만 실행
            if (cnt > 0) {

                //key값으로 존재하는 상대방의 닉네임이 넘어온다
                String msgKey = snapshot.getKey();

                //아이템 수정
                for (int i = 0; i < arr.size(); i++) {
                    if (arr.get(i).getMsgKey().equals(msgKey)) {
                        arr.remove(i);
                        chatAdapter.notifyDataSetChanged();
                    }
                }




            }
        }//onChildRemoved()


        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            ChatVO value = snapshot.getValue(ChatVO.class);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    //전송 버튼 눌렀을때 이벤트 처리
    View.OnClickListener sendBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Date today = new Date();
            SimpleDateFormat timeNow = new SimpleDateFormat("a K:mm");

            //채팅에 아무내용이 없으면 리스트에 추가가 안되게끔
            if (message.getText().toString().equals(""))
                return;

            //하나의 채팅 메세지객체 생성
            ChatVO vo = new ChatVO();
            vo.setNickname(myNickname);//로그인후 UserVO에 담아서 닉네임을 가져옴
            vo.setMessage(message.getText().toString());//작성한 메세지 담기
            vo.setTime(timeNow.format(today));//현재시간 담기

            //firebase\chat\내 닉네임\상대닉네임\ChatVO값
            Util.CHAT_REF.child(myNickname).child(opponentNickname).push().setValue(vo); // 서버에 채팅객체 데이터 쓰기
            //firebase\chat\상대\내 닉네임\ChatVO값
            Util.CHAT_REF.child(opponentNickname).child(myNickname).push().setValue(vo);
            //firebase메서드에서 갱신하기 때문에 따로 갱신x
            //arr.add(vo);//리스트뷰에 들어갈 ArrayList에 add
            //chatAdapter.notifyDataSetChanged();//어댑터의 getView()를 갱신하는 메서드
            message.setText("");

        }//onClick
    };//sendBtnClick

}//ChatActivity 클래스














