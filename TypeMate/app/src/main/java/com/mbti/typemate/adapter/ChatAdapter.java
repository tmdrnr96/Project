package com.mbti.typemate.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.mbti.typemate.R;
import com.mbti.typemate.util.Util;
import com.mbti.typemate.vo.ChatVO;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter  extends ArrayAdapter<ChatVO> {

    Context context;//현재 View
    ArrayList<ChatVO> arr;//어댑터의 내용을 가지고있는 리스트
    int resources;//참조할 xml(View로 Inflate해줄거임)
    ListView chatList;//사용할 리스트뷰
    Dialog dialog;//삭제, 복사를 띄워줄 다이얼로그
    String myNickname;//로그인한 사람의 닉네임
    String opponentNickname;//채팅상대의 닉네임
    int position;//롱클릭에서 쓸 변수
    List<String> keyList;

    AlertDialog alertDialog;
    //얼럿다이얼로그 생성
    AlertDialog.Builder alertDialogBuilder;

    ClipboardManager clipboardManager;

    public ChatAdapter(@NonNull Context context, int resource, ArrayList<ChatVO> arr, ListView chatList, String myNickname, String opponentNickname) {
        super(context, resource, arr);

        //얼럿다이얼로그 생성
        alertDialogBuilder = new AlertDialog.Builder(context);

        //클립보드매니저 생성
        clipboardManager = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);

        //지역변수로만 사용가능한 파라미터값들을 getView()에서도 써야하기때문에
        //전역변수에 저장해준다
        this.context = context;
        this.resources = resource;
        this.arr = arr;
        this.chatList = chatList;
        this.myNickname = myNickname;
        this.opponentNickname = opponentNickname;

        //리스트 길게 클릭했을때
        this.chatList.setOnItemLongClickListener(longClickListener);
    }

    @NonNull
    @Override
    //position은 객체의 순서를 담고있다 0이면 첫번째 객체 1이면 두번째 객체
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //첫 항목을 그릴때만 inflate를 하기 위한 recycle뷰를 만들어줌(데이터 처리속도에서 유리)
        ViewHolder viewHolder;

        //Inflater를 사용하여 단독으로 만들어놓은 레이아웃을 실제 View형식으로 변환해줌
        //getSystemService는 Context에 있는 요소라서 Activity에서 쓰는게 아니라면 Context를 불러와서 써야함
        LayoutInflater linf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //레이아웃이 View화 되지 않은 처음에만 한번 chat_list_form레이아웃이 View로 inflate됨
        if(convertView == null){
            convertView = linf.inflate(resources, parent, false);
            viewHolder = new ChatAdapter.ViewHolder();
            viewHolder.my_chat_content = (TextView)convertView.findViewById(R.id.my_chat_content);
            viewHolder.op_chat_content = (TextView)convertView.findViewById(R.id.op_chat_content);
            viewHolder.op_chat_time = (TextView)convertView.findViewById(R.id.op_chat_time);
            viewHolder.my_chat_time= (TextView)convertView.findViewById(R.id.my_chat_time);
            viewHolder.op_u_name = (TextView)convertView.findViewById(R.id.op_u_name);
            //gravity를 조절할수 있게 id를 LinearLayout 검색
            viewHolder.my_chat_linear = (LinearLayout)convertView.findViewById(R.id.my_chat_linear);
            viewHolder.op_chat_linear = (LinearLayout)convertView.findViewById(R.id.op_chat_linear);
            viewHolder.chat_content_linear = (LinearLayout)convertView.findViewById(R.id.chat_content_linear);

            viewHolder.user_img = (ImageView)convertView.findViewById(R.id.user_img);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        //메세지에 담겨있는 닉네임을 판단하여 정렬을 시켜줄것이다

            if (arr.get(position).getNickname().equals(myNickname)){

                viewHolder.op_chat_linear.setVisibility(View.GONE);

                viewHolder.my_chat_linear.setVisibility(View.VISIBLE);
                viewHolder.my_chat_content.setText(arr.get(position).getMessage());
                viewHolder.my_chat_time.setText(arr.get(position).getTime());
            }
            else{
                viewHolder.my_chat_linear.setVisibility(View.GONE);

                viewHolder.op_chat_linear.setVisibility(View.VISIBLE);
                viewHolder.op_u_name.setText(arr.get(position).getNickname());
                viewHolder.op_chat_content.setText(arr.get(position).getMessage());
                viewHolder.op_chat_time.setText(arr.get(position).getTime());
            }


        //어댑터에 넘겨준 ArrayList의 방번호를 이용하여 내용을 content변수에 담아준다
        //담아줄때 두개의 TextView를 아이디로 제어하면 되겠다


        return convertView;
    }//getView()

    @Nullable
    @Override
    public ChatVO getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return arr.size();
    }//전체 데이터의 갯수를  리턴

    @Override
    public long getItemId(int position) {
        return position;
    }//position번째 항목의 index



    AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

            //팝업창이나 다이얼로그 생성할 예정
            position = i;//i로 넘어오는 값이 어댑터에 참조되는 arrayList의 index

            dialog = new Dialog(context);
            dialog.setContentView(R.layout.chat_dialog);

            Button copy_btn = dialog.findViewById(R.id.copy_btn);
            Button del_btn = dialog.findViewById(R.id.del_btn);

            copy_btn.setOnClickListener(dialog_btnEvt);
            del_btn.setOnClickListener(dialog_btnEvt);

            dialog.show();

            return false;//true하면 일반클릭과 롱클릭 둘다 먹고 false하면 롱클릭만 먹음
        }
    };//onItemLongClickListener

    View.OnClickListener dialog_btnEvt = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.copy_btn :
                    ClipData clipData = ClipData.newPlainText(null, arr.get(position).getMessage());
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(context, "클립보드에 복사되었습니다", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();//다이얼로그 숨기기
                    break;

                case R.id.del_btn :
                            //서버에서 받아온 key값을 list로 관리
                            //딜리트를 누를때마다 초기화된다
                            keyList = new ArrayList<>();

                    alertDialogBuilder.setTitle("메세지 삭제").setMessage("메세지를 삭제하시겠습니까?\n\n삭제된 메세지는 내 채팅방에서만 적용되며\n 상대방의 채팅방에서는 삭제되지 않습니다.");

                    //다이얼로그에 OK버튼 만들고 감지자 정의(누르면 로그아웃이 되고 로그인 액티비티로 전환됨)
                    alertDialogBuilder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Util.CHAT_REF.child(myNickname).child(opponentNickname).addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                            //List를 만들어서 서버에서 받아온 key값을 index로 가져다 쓸수있게 관리
                                            keyList.add(snapshot.getKey());
                                            //여러번 돌면서 add를 돌기떄문에 out of bounds를 방지하기 위하여 if문으로 검증
                                            if (keyList.size() > position){
                                                //내가 누른 아이템의 position값을 list의 index로 대입시켜 값을 꺼내온다
                                                String key = keyList.get(position);
                                                //가져온값으로 firebase에서 값을 지워준다
                                                Util.CHAT_REF.child(myNickname).child(opponentNickname).child(key).removeValue();
                                                dialog.dismiss();
                                            }
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

                                }//Positive onClick()

                            });
                    //취소버튼을 누르면 다이얼로그 자동 dismiss()
                    alertDialogBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialog.dismiss();//얼럿 다이얼로그 숨김
                            dialog.dismiss();//커스텀 다이얼로그 숨김
                        }
                    });

                    //설정된 값으로 다이얼로그 최종생성
                    alertDialog = alertDialogBuilder.create();

                    //사용자에게 보여줌
                    alertDialog.show();
                    break;

            }//switch
        }
    };

    //뷰 홀더 패턴
    public static class ViewHolder {
        TextView my_chat_content, op_chat_content, op_u_name, op_chat_time, my_chat_time;
        LinearLayout chat_content_linear, op_chat_linear, my_chat_linear;
        ImageView user_img;
    }
}
























