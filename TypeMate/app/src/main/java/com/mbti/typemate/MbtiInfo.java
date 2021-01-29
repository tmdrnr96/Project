package com.mbti.typemate;

import android.util.Log;

import java.util.ArrayList;

public class MbtiInfo {
    static private MbtiInfo mbtiInfo = null;
    ArrayList<ArrayList<String[]>> mbti;
    String[] chemi;

    private MbtiInfo() {
        mbti = new ArrayList<ArrayList<String[]>>();
        ArrayList<String[]> temp = new ArrayList<String[]>();
        temp.add(new String[]{"외향형", "폭 넓은 대인관계\n사교적, 활동적"});
        temp.add(new String[]{"내향형", "깊이있는 대인관계\n신중함, 집중력"});
        mbti.add(temp);

        temp = new ArrayList<String[]>();
        temp.add(new String[]{"감각형", "실제 경험 중시\n정확, 철저한 일처리"});
        temp.add(new String[]{"직관형", "직관에 의존\n신속, 비약적"});
        mbti.add(temp);

        temp = new ArrayList<String[]>();
        temp.add(new String[]{"사고형", "진실과 사실에 관심\n논리적, 분석적"});
        temp.add(new String[]{"감정형", "사람, 관계에 관심\n상황적, 포괄적"});
        mbti.add(temp);

        temp = new ArrayList<String[]>();
        temp.add(new String[]{"판단형", "분명한 목적, 방향\n철저한 사전 계획"});
        temp.add(new String[]{"인식형", "상황에 맞는 변화\n융통과 적응"}); // 0806
        mbti.add(temp);

        chemi = new String[5];
        chemi[0] = "우리인연 영원히 뽀에버! 천생연분";
        chemi[1] = "아주 좋은 관계가 될 수 있음!";
        chemi[2] = "안 맞는 것 맞는 것 딱 반반";
        chemi[3] = "뭐...최악은 면했지만...그닥...";
        chemi[4] = "진짜 궁합 최악! 지구 멸망의 길";
    }

    static public MbtiInfo getMbtiInfo() {
        if (mbtiInfo == null) {
            mbtiInfo = new MbtiInfo();
        }

        return mbtiInfo;
    }

    public String[] getMbti(int i, char opMbti) {
        int p = 0;

        if (opMbti == 'I' || opMbti == 'N' || opMbti == 'F' || opMbti == 'P') {
            p = 1;
        }

        return mbti.get(i).get(p);
    }

    public String getChemistry(String myMbti, String opMbti) {
        int i = -1;

        //NF, NT, SP, SJ 순으로 우선순위를 주고 myMbti의 우선순위가 높게
        String temp;
        if ((!myMbti.contains("NF") && opMbti.contains("NF")) ||
                ((myMbti.charAt(1) == 'S' && myMbti.charAt(3) == 'J') && !(opMbti.charAt(1) == 'S' && opMbti.charAt(3) == 'J'))
                || (myMbti.charAt(1) == 'S' && myMbti.charAt(3) == 'P' && opMbti.contains("NT"))) {
            temp = myMbti;
            myMbti = opMbti;
            opMbti = temp;
        }

        if (myMbti.contains("NF")) {
            if (opMbti.contains("NF") || opMbti.contains("NT")) {
                if (opMbti.equals("INTP")) {
                    i = 1;
                } else if (myMbti.charAt(0) != opMbti.charAt(0) && myMbti.charAt(3) != opMbti.charAt(3)) {
                    i = 0;
                } else {
                    i = 1;
                }
            } else {
                if (myMbti.equals("ENFJ") && opMbti.equals("ISFP")) {
                    i = 0;
                } else {
                    i = 4;
                }
            }
        } else if (myMbti.contains("NT")) {
            if (opMbti.contains("NT")) {
                if (myMbti.charAt(0) != opMbti.charAt(0) && myMbti.charAt(3) != opMbti.charAt(3)) {
                    i = 0;
                } else {
                    i = 1;
                }
            } else if (opMbti.charAt(1) == 'S' && opMbti.charAt(3) == 'P') {
                i = 2;
            } else {
                if (myMbti.equals("INTP") && opMbti.equals("ESTJ")) {
                    i = 0;
                } else if (myMbti.equals("ENTJ")) {
                    i = 2;
                } else {
                    i = 3;
                }
            }
        } else if (myMbti.charAt(1) == 'S' && myMbti.charAt(3) == 'P') {
            if(opMbti.charAt(1) == 'S' && opMbti.charAt(3) == 'P'){
                i = 3;
            } else if (myMbti.charAt(0) != opMbti.charAt(0) && myMbti.charAt(3) != opMbti.charAt(3)) {
                i = 0;
            } else{
                i = 2;
            }
        } else {
            i = 1;
        }

        return chemi[i];
    }
}
