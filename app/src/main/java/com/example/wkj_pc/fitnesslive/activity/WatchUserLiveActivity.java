package com.example.wkj_pc.fitnesslive.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wkj_pc.fitnesslive.R;
import com.example.wkj_pc.fitnesslive.adapter.AttentionUserAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.widget.VideoView;

public class WatchUserLiveActivity extends AppCompatActivity {

    @BindView(R.id.watch_video_view)
    VideoView watchVideoView;
    @BindView(R.id.login_watch_live_logo)
    ImageView loginWatchLiveLogo;
    @BindView(R.id.watch_people_number)
    TextView watchPeopleNumber;
    @BindView(R.id.watch_fans_people_number)
    TextView watchFansPeopleNumber;
    @BindView(R.id.attention_user_watch_show_recycler_view)
    RecyclerView attentionUserWatchShowRecyclerView;

    @BindView(R.id.watch_video_comment_edit_text)
    EditText watchVideoCommentEditText;
    @BindView(R.id.begin_watch_live_show_linearlayout)
    LinearLayout beginWatchLiveShowLinearlayout;
    @BindView(R.id.start_watch_live_btn)
    Button startWatchLiveBtn;
    @BindView(R.id.ic_send_watch_comment_message_icon)
    ImageView icSendWatchCommentMessageIcon;
    @BindView(R.id.close_watch_live_icon)
    ImageView closeWatchLiveIcon;
    private String path;    //播放视频的网络地址
    List<Integer> amatartLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_user_live);
        ButterKnife.bind(this);
        if (!LibsChecker.checkVitamioLibs(this))
            return;
        watchVideoView.setBufferSize(1024);
        watchVideoView.setVideoQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        watchVideoView.setHardwareDecoder(true);
        path = getResources().getString(R.string.app_video_upload_srs_server_url);
        initAmatarLists();
        initAttentionUserShowRecyclerView();
    }

    private void initAmatarLists() {
        amatartLists = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            amatartLists.add(R.drawable.head_img);
        }
    }

    private void initAttentionUserShowRecyclerView() {
        AttentionUserAdapter adapter = new AttentionUserAdapter(amatartLists);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        attentionUserWatchShowRecyclerView.setLayoutManager(manager);
        attentionUserWatchShowRecyclerView.setAdapter(adapter);
    }

    @OnClick({R.id.login_watch_live_logo, R.id.start_watch_live_btn, R.id.ic_send_watch_comment_message_icon,
            R.id.close_watch_live_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_watch_live_logo:
                break;
            case R.id.start_watch_live_btn:
                watchVideoView.setVideoPath(path);
                watchVideoView.start();
                startWatchLiveBtn.setVisibility(View.GONE);
                //watch_fans_people_number
                //  watch_people_number
                break;
            case R.id.ic_send_watch_comment_message_icon:
                break;
            case R.id.close_watch_live_icon:
                if (startWatchLiveBtn.getVisibility()!=View.GONE)
                    return;
                finish();
                break;
        }
    }
}