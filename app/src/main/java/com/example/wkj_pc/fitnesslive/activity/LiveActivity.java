package com.example.wkj_pc.fitnesslive.activity;

import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.wkj_pc.fitnesslive.MainApplication;
import com.example.wkj_pc.fitnesslive.R;
import com.example.wkj_pc.fitnesslive.adapter.AttentionUserAdapter;
import com.example.wkj_pc.fitnesslive.po.LiveChattingMessage;
import com.example.wkj_pc.fitnesslive.tools.WebSocketUtils;
import com.github.faucamp.simplertmp.RtmpHandler;
import com.seu.magicfilter.utils.MagicFilterType;
import net.ossrs.yasea.SrsCameraView;
import net.ossrs.yasea.SrsEncodeHandler;
import net.ossrs.yasea.SrsPublisher;
import net.ossrs.yasea.SrsRecordHandler;
import java.io.IOException;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;


public class LiveActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.live_chatting_message_recycler_view)
    RecyclerView liveChattingMessageRecyclerView;   //直播聊天信息显示
    @BindView(R.id.login_live_logo)     //直播人logo
            ImageView loginLiveLogo;
    @BindView(R.id.watch_people_number) //观看直播人数
            TextView watchPeopleNumber;
    @BindView(R.id.fans_people_number)  //粉丝数量
            TextView fansPeopleNumber;
    @BindView(R.id.attention_user_show_recycler_view)   //观众的logo
            RecyclerView attentionUserRcyclerView;
    @BindView(R.id.below_linearlayout)  //下面的布局
            LinearLayout belowLinearlayout;
    @BindView(R.id.change_beauty_spinner)   //改变滤镜
            Spinner changeBeautySpinner;
    @BindView(R.id.start_live_btn)      //开始直播按钮
            Button mPublishBtn;
    @BindView(R.id.editText)        //聊天信息输入框
            EditText editText;
    @BindView(R.id.swCam)           //切换摄像头
            ImageView mCameraSwitchBtn;
    @BindView(R.id.close_live_icon)     //关闭直播按钮
            ImageView closeLiveIconBtn;
    @BindView(R.id.begin_live_show_linearlayout)    // 底部线性布局
            LinearLayout bottomLiveShowlinearLayout;

    private SrsCameraView liveView;
    private String[] clarifyitems = new String[]{"BEAUTY", "COOL", "EARLYBIRD", "EVERGREEN", "N1977", "NOSTALGIA", "ROMANCE", "SUNRISE",
            "SUNSET", "TENDER", "TOASTER2", "VALENCIA", "WALDEN", "WARM", "NONE"};
    //滤镜类型
    private Button mEncoderBtn; //编码按钮
    private String pushVideoStreamUrl;  //srs服务器推流地址
    private List<Integer> amatarLists = new ArrayList<>();  //观看者头像集合
    private String messageWebSocketUrl; //直播聊天传输地址
    private SrsPublisher mPublisher;       //直播推流发着者

    public static List<LiveChattingMessage> liveMessages = new ArrayList<>();//直播聊天信息
    private LiveChattingMessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        设置页面布局方向，当该window对用户可见时，让设备屏幕处于高亮（bright）状态。
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_live);
        ButterKnife.bind(this);
        /* 获取websocket地址，设置聊天*/
        messageWebSocketUrl = getResources().getString(R.string.app_message_websocket_url_edit) +
                MainApplication.loginUser.getAccount()+"/" + MainApplication.loginUser.getAccount()+"/live";
        WebSocketUtils.getWebSocket(messageWebSocketUrl);
        /*设置直播推流地址*/
        pushVideoStreamUrl = getResources().getString(R.string.app_video_upload_srs_server_url);
        mPublisher = new SrsPublisher((SrsCameraView) findViewById(R.id.live_view));
        initAmatartLists();
        /*设置观看者横向显示*/
        initAmatartRecyclerView();
        /*设置聊天信息展示*/
        initChattingMessageShowRecyclerView();
        mPublishBtn.setOnClickListener(this);
        mCameraSwitchBtn.setOnClickListener(this);
        initPublisher();
        closeLiveIconBtn.setOnClickListener(this);
        initBeautySpinner();
    }

    /* 初始化观看者头像 */
    private void initAmatartLists() {
        for (int i = 0; i < 10; i++) {
            amatarLists.add(R.drawable.head_img);
        }
    }

    /* 设置观看者头像显示 */
    private void initAmatartRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        attentionUserRcyclerView.setLayoutManager(layoutManager);
        AttentionUserAdapter adapter = new AttentionUserAdapter(amatarLists);
        attentionUserRcyclerView.setAdapter(adapter);
    }

    /*设置直播聊天信息展示*/
    public void initChattingMessageShowRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        liveChattingMessageRecyclerView.setLayoutManager(layoutManager);
        adapter = new LiveChattingMessageAdapter(liveMessages);
        liveChattingMessageRecyclerView.setAdapter(adapter);
    }

    /*  设置发布直播视频*/
    private void initPublisher() {
        //设置编码状态回调
        mPublisher.setEncodeHandler(new SrsEncodeHandler(new SrsEncodeHandler.SrsEncodeListener() {
            @Override
            public void onNetworkWeak() {
                Toast.makeText(getApplicationContext(), "网络型号弱", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNetworkResume() {
            }
            @Override
            public void onEncodeIllegalArgumentException(IllegalArgumentException e) {
            }
        }));
        mPublisher.setRecordHandler(new SrsRecordHandler(new SrsRecordHandler.SrsRecordListener() {
            @Override
            public void onRecordPause() {
                Toast.makeText(getApplicationContext(), "Record paused", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onRecordResume() {
                Toast.makeText(getApplicationContext(), "Record resumed", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onRecordStarted(String msg) {
                Toast.makeText(getApplicationContext(), "Recording file: " + msg, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onRecordFinished(String msg) {
                Toast.makeText(getApplicationContext(), "MP4 file saved: " + msg, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onRecordIllegalArgumentException(IllegalArgumentException e) {
            }
            @Override
            public void onRecordIOException(IOException e) {
            }
        }));
        //rtmp推流状态回调
        mPublisher.setRtmpHandler(new RtmpHandler(new RtmpHandler.RtmpListener() {
            @Override
            public void onRtmpConnecting(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onRtmpConnected(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onRtmpVideoStreaming() {
            }
            @Override
            public void onRtmpAudioStreaming() {
            }
            @Override
            public void onRtmpStopped() {
                Toast.makeText(getApplicationContext(), "已停止", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onRtmpDisconnected() {
                Toast.makeText(getApplicationContext(), "未连接服务器", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onRtmpVideoFpsChanged(double fps) {
            }
            @Override
            public void onRtmpVideoBitrateChanged(double bitrate) {
            }
            @Override
            public void onRtmpAudioBitrateChanged(double bitrate) {
            }
            @Override
            public void onRtmpSocketException(SocketException e) {
            }
            @Override
            public void onRtmpIOException(IOException e) {
            }
            @Override
            public void onRtmpIllegalArgumentException(IllegalArgumentException e) {
            }
            @Override
            public void onRtmpIllegalStateException(IllegalStateException e) {
            }
        }));
        //预览分辨率
        mPublisher.setPreviewResolution(1280, 720);
        //推流分辨率
        mPublisher.setOutputResolution(720, 1280);
        //传输率
        mPublisher.setVideoHDMode();
        //开启美颜（其他滤镜效果在MagicFilterType中查看）
        mPublisher.switchCameraFilter(MagicFilterType.CALM);
        //打开摄像头，开始预览（未推流）
        mPublisher.startCamera();
    }

    /*  设置美颜效果*/
    private void initBeautySpinner() {
        final Spinner changeBeautySp = (Spinner) findViewById(R.id.change_beauty_spinner);
        changeBeautySp.setDropDownWidth(300);
        changeBeautySp.setPrompt("滤镜");
        changeBeautySp.setDropDownHorizontalOffset(20);
        changeBeautySp.setGravity(Gravity.CENTER);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, clarifyitems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        changeBeautySp.setAdapter(adapter);
        changeBeautySp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        mPublisher.switchCameraFilter(MagicFilterType.BEAUTY);
                        break;
                    case 2:
                        mPublisher.switchCameraFilter(MagicFilterType.COOL);
                        break;
                    case 3:
                        mPublisher.switchCameraFilter(MagicFilterType.EARLYBIRD);
                        break;
                    case 4:
                        mPublisher.switchCameraFilter(MagicFilterType.EVERGREEN);
                        break;
                    case 5:
                        mPublisher.switchCameraFilter(MagicFilterType.N1977);
                        break;
                    case 6:
                        mPublisher.switchCameraFilter(MagicFilterType.NOSTALGIA);
                        break;
                    case 7:
                        mPublisher.switchCameraFilter(MagicFilterType.ROMANCE);
                        break;
                    case 8:
                        mPublisher.switchCameraFilter(MagicFilterType.SUNRISE);
                        break;
                    case 9:
                        mPublisher.switchCameraFilter(MagicFilterType.SUNSET);
                        break;
                    case 10:
                        mPublisher.switchCameraFilter(MagicFilterType.TENDER);
                        break;
                    case 11:
                        mPublisher.switchCameraFilter(MagicFilterType.TOASTER2);
                        break;
                    case 12:
                        mPublisher.switchCameraFilter(MagicFilterType.VALENCIA);
                        break;
                    case 13:
                        mPublisher.switchCameraFilter(MagicFilterType.WALDEN);
                        break;
                    case 14:
                        mPublisher.switchCameraFilter(MagicFilterType.WARM);
                        break;
                    case 15:
                    default:
                        mPublisher.switchCameraFilter(MagicFilterType.NONE);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_live_btn:
                //开始
                mPublishBtn.setVisibility(View.GONE);
                bottomLiveShowlinearLayout.setVisibility(View.VISIBLE);
                mPublisher.startPublish(pushVideoStreamUrl);
                mPublisher.startCamera();
                break;
            //停止推流
            case R.id.close_live_icon:
                mPublisher.stopRecord();
                mPublisher.stopEncode();
                mPublisher.stopPublish();
                mPublisher.stopCamera();
                finish();
                break;
            //切换摄像头
            case R.id.swCam:
                mPublisher.switchCameraFace((mPublisher.getCamraId() + 1) % Camera.getNumberOfCameras());
                break;
        }
    }
    /*发送直播聊天信息到服务器*/
    public void sendLiveChattingMessage(View view){
        String editTextMsg = editText.getText().toString().trim();
        if (TextUtils.isEmpty(editTextMsg)){
            return;
        }
        final LiveChattingMessage sendMsg=new LiveChattingMessage();
        sendMsg.setFrom(MainApplication.loginUser.getAccount());
        sendMsg.setContent(editTextMsg);
        sendMsg.setTo("server");
        sendMsg.setMid(1);
        SimpleDateFormat format=new SimpleDateFormat("HH:mm:ss");
        sendMsg.setTime(format.format(new Date()));
        //WebSocketUtils.baseWebSocket.send( GsonUtils.getGson().toJson(sendMsg));
        editText.setText("");
    }
    @Override
    protected void onResume() {
        super.onResume();
        mPublisher.resumeRecord();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mPublisher.pauseRecord();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPublisher.stopPublish();
        mPublisher.stopRecord();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mPublisher.stopEncode();
        mPublisher.stopRecord();
        mPublisher.setScreenOrientation(newConfig.orientation);
        if (mPublishBtn.getVisibility() == View.GONE) {
            mPublisher.startEncode();
        }
        mPublisher.startCamera();
    }
}
