package com.example.wkj_pc.fitnesslive.activity;

import android.content.res.Configuration;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.Toast;

import com.example.wkj_pc.fitnesslive.R;
import com.example.wkj_pc.fitnesslive.adapter.AttentionUserAdapter;
import com.example.wkj_pc.fitnesslive.tools.WebSocketUtils;
import com.github.faucamp.simplertmp.RtmpHandler;
import com.seu.magicfilter.utils.MagicFilterType;

import net.ossrs.yasea.SrsCameraView;
import net.ossrs.yasea.SrsEncodeHandler;
import net.ossrs.yasea.SrsPublisher;
import net.ossrs.yasea.SrsRecordHandler;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;


public class LiveActivity extends AppCompatActivity implements View.OnClickListener{

    private SrsCameraView liveView;
    private String [] clarifyitems=new String[]{"BEAUTY","COOL","EARLYBIRD","EVERGREEN","N1977","NOSTALGIA","ROMANCE","SUNRISE",
            "SUNSET","TENDER","TOASTER2","VALENCIA","WALDEN","WARM","NONE"};
    private Button mPublishBtn;
    private ImageView mCameraSwitchBtn;
    private Button mEncoderBtn;
    private EditText mRempUrlEt;
    private SrsPublisher mPublisher;
    private String rtmpUrl;
    private String pushVideoStreamUrl;
    private RecyclerView attentionUserRcyclerView;
    private List<Integer> amatarLists=new ArrayList<>();
    private LinearLayout bottomLiveShowlinearLayout;
    private ImageView closeLiveIconBtn;
    private String messageWebSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        设置页面布局方向，当该window对用户可见时，让设备屏幕处于高亮（bright）状态。
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_live);
        messageWebSocket=getResources().getString(R.string.app_message_websocket_url_edit);
        WebSocketUtils.getWebSocket(messageWebSocket);

//        pushVideoStreamUrl = "rtmp://47.93.20.45:1935/live/livestreams";
//        mPublishBtn = (Button) findViewById(R.id.start_live_btn);
//        mCameraSwitchBtn = (ImageView) findViewById(R.id.swCam);
//        initAmatartLists();
//        attentionUserRcyclerView = (RecyclerView) findViewById(R.id.attention_user_show_recycler_view);
//        initRecyclerView();
//        mPublishBtn.setOnClickListener(this);
//        mCameraSwitchBtn.setOnClickListener(this);
//        mPublisher = new SrsPublisher((SrsCameraView) findViewById(R.id.live_view));
//        initPublisher();
//        closeLiveIconBtn = (ImageView) findViewById(R.id.close_live_icon);
//        closeLiveIconBtn.setOnClickListener(this);
//        bottomLiveShowlinearLayout = (LinearLayout) findViewById(R.id.begin_live_show_linearlayout);
//        initBeautySpinner();
    }
    private void initAmatartLists() {
        for (int i=0 ;i<10;i++){
            amatarLists.add(R.drawable.head_img);
        }
    }
    private void initRecyclerView() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        attentionUserRcyclerView.setLayoutManager(layoutManager);
        AttentionUserAdapter adapter=new AttentionUserAdapter(amatarLists);
        attentionUserRcyclerView.setAdapter(adapter);
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
            public void onNetworkResume() {}
            @Override
            public void onEncodeIllegalArgumentException(IllegalArgumentException e) { }
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
            public void onRecordIllegalArgumentException(IllegalArgumentException e) {}
            @Override
            public void onRecordIOException(IOException e) {}
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
            public void onRtmpVideoStreaming() {}
            @Override
            public void onRtmpAudioStreaming() {}
            @Override
            public void onRtmpStopped() {
                Toast.makeText(getApplicationContext(), "已停止", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onRtmpDisconnected() {
                Toast.makeText(getApplicationContext(), "未连接服务器", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onRtmpVideoFpsChanged(double fps) {}
            @Override
            public void onRtmpVideoBitrateChanged(double bitrate) {}
            @Override
            public void onRtmpAudioBitrateChanged(double bitrate) {}
            @Override
            public void onRtmpSocketException(SocketException e) {}
            @Override
            public void onRtmpIOException(IOException e) {}
            @Override
            public void onRtmpIllegalArgumentException(IllegalArgumentException e) {}
            @Override
            public void onRtmpIllegalStateException(IllegalStateException e) {}
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
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,clarifyitems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        changeBeautySp.setAdapter(adapter);
        changeBeautySp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
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

    public void onClick(View view){
        switch (view.getId()){
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
        if (mPublishBtn.getVisibility()==View.GONE){
            mPublisher.startEncode();
        }
        mPublisher.startCamera();
    }
}
