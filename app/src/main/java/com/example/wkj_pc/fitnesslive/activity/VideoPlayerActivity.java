package com.example.wkj_pc.fitnesslive.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.wkj_pc.fitnesslive.R;

public class VideoPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        String videourl = getIntent().getStringExtra("videourl");


    }
}
