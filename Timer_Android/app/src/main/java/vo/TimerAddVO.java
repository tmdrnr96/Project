package vo;

import java.io.Serializable;

public class TimerAddVO implements Serializable {

    private String title;
    private String sub_title;
    private String timer;

    public TimerAddVO(){

    }

    public TimerAddVO(String title, String sub_title, String timer){
        this.title = title;
        this.sub_title = sub_title;
        this.timer = timer;

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }
}
