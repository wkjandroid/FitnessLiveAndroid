package com.example.wkj_pc.fitnesslive.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.wkj_pc.fitnesslive.R;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class VideoPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        String videourl = getIntent().getStringExtra("videourl");
        JCVideoPlayerStandard jcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.videoplayer);
        jcVideoPlayerStandard.setUp("http://47.93.20.45:8080/a.mp4"
                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "马上开始");
        ImageView videoView = jcVideoPlayerStandard.thumbImageView;
        Glide.with(this).load("http://47.93.20.45:8080/tomcat.png").asBitmap().into(videoView);
    }
    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
