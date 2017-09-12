package com.example.wkj_pc.fitnesslive.po;

/**
 * Created by wkj on 2017/9/12.
 */

public class UploadVideo {
    private int vid;    //视频id
    private String titile;  //视频标题
    private String videourl;    //视频地址
    private String thumbnailurl;    //视频缩略图地址
    private String uploadtime;      //上传时间
    private int uid;

    public String getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(String uploadtime) {
        this.uploadtime = uploadtime;
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public String getTitile() {
        return titile;
    }

    public void setTitile(String titile) {
        this.titile = titile;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public String getThumbnailurl() {
        return thumbnailurl;
    }

    public void setThumbnailurl(String thumbnailurl) {
        this.thumbnailurl = thumbnailurl;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
