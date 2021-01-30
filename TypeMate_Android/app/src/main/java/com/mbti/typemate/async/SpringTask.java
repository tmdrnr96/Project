package com.mbti.typemate.async;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mbti.typemate.LoginActivity;
import com.mbti.typemate.MainActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SpringTask extends AsyncTask<String, Void, String> {

    private String sendMsg, receiveMsg, serverUrl;
    Context context;

    //어떤 페이지로 값을 전달할지 url을 생성자로 받아옴
    public SpringTask(String serverUrl, Context context) {
        this.serverUrl = serverUrl;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {

        try{
            URL url = new URL(serverUrl);//접속할 URL선언과 함께 생성자로 페이지주소를 넘겨줌

            HttpURLConnection conn  = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");//전송방식 설정

            //OutputStream 에 HttpURLConnection의 주소를 연결해줌
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

            //Spring에서 받을시에는 VO와 string type이런식으로 받을 예정
            sendMsg = strings[0];//execute()에서 파라미터로 넘겨준 값이 배열형태로 넘어온다

            osw.write(sendMsg);//방금 셋팅한 파라미터 전달
            osw.flush();


            if (conn.getResponseCode() == conn.HTTP_OK  ){

                //서버에서 리턴해준 값을 UTF-8 인코딩으로 읽어옴
                InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "UTF-8");

                //데이터처리에서 유리한 버퍼드리더
                BufferedReader reader = new BufferedReader(isr);

                receiveMsg = reader.readLine();//반환값을 변수에 저장

            }

        }catch (Exception e){
            Log.i("CATCH", e.getMessage());//오류값을 Logcat에서 간단하게나만 확인할수 있게끔
        }
        return receiveMsg;
    }

    @Override
    protected void onPostExecute(String resultStr) {

    }
}
