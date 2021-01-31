package parse;


import java.util.ArrayList;

import vo.TimerAddVO;

public class TimerAdd {

    TimerAddVO vo;
    ArrayList<TimerAddVO> list;

    public ArrayList<TimerAddVO> vo_list_in(){

        vo = new TimerAddVO();
        list = new ArrayList<>();

        list.add(new TimerAddVO("준비", "시작하기 전 카운트다운",":01"));
        list.add(new TimerAddVO("운동", "오래운동하기",":01"));
        list.add(new TimerAddVO("휴식", "오래휴식하기",":01"));
        list.add(new TimerAddVO("라운드", "1라운드는 운동 + 휴식입니다.","1"));

        return list;
    }
}
