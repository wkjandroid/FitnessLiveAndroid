package com.example.wkj_pc.fitnesslive.po;


/**
 * Created by wkj_pc on 2017/8/7.
 */
/**
 * 直播间传递的消息实体类
 */
public class LiveChattingMessage {
    private int mid;
    private String from;
    private String to;
    private String content;
    private String time;

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
