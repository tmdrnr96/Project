package com.mbti.typemate.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public interface Util {

    //해당PC의 IP를 입력해서 작업해야 서버통신가능(cmd -> ipconfig -> IPv4주소 : 이곳의 값)
    //열어놓을 IP주소 124.53.113.102
    public static String IP = "124.53.113.102";

    static FirebaseDatabase DATABASE = FirebaseDatabase.getInstance();//데이터베이스 객체
    static DatabaseReference CHAT_REF = DATABASE.getReference("chat");//chat을 참조하는 데이터베이스 개장객체

    //Spring에서의 RequestMapping 주소
    static String SERVER_IP_JOIN = "http://" + IP + ":9090/typematespring/join.do";
    static String SERVER_IP_LOGIN = "http://" + IP + ":9090/typematespring/login.do";
    static String SERVER_IP_SEARCH = "http://" + IP + ":9090/typematespring/search.do";
    static String SERVER_IP_FIND_ACCOUNT = "http://" + IP + ":9090/typematespring/find_account.do";
    static String SERVER_IP_FIND_PWD = "http://" + IP + ":9090/typematespring/find_pwd.do";
    static String SERVER_IP_RESET_PWD = "http://" + IP + ":9090/typematespring/reset_pwd.do";
    static String SERVER_IP_ID_CHECK = "http://" + IP + ":9090/typematespring/id_check.do";
    static String SERVER_IP_NICKNAME_CHECK = "http://" + IP + ":9090/typematespring/nickname_check.do";
    static String SERVER_IP_USER_INFO = "http://" + IP + ":9090/typematespring/user_info.do";
    static String SERVER_IP_CHECK_GENDER = "http://" + IP + ":9090/typematespring/check_gender.do";
    static String SERVER_IP_PWD_CHECK_RESET = "http://" + IP + ":9090/typematespring/pwd_check_reset.do";
    static String SERVER_IP_WITHDRAWAL = "http://" + IP + ":9090/typematespring/user_withdrawal.do";
    static String SERVER_IP_USER_RESET = "http://" + IP + ":9090/typematespring/user_reset.do";
    static String SERVER_IP_RECHECK_PWD = "http://" + IP + ":9090/typematespring/recheck_pwd.do";
    static String SERVER_IP_RESET_MBTI = "http://" + IP + ":9090/typematespring/reset_mbti.do";
}
