package com.example.wkj_pc.fitnesslive.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wkj_pc.fitnesslive.MainApplication;
import com.example.wkj_pc.fitnesslive.R;
import com.example.wkj_pc.fitnesslive.adapter.AttentionUserAdapter;
import com.example.wkj_pc.fitnesslive.adapter.LiveChattingMessagesAdapter;
import com.example.wkj_pc.fitnesslive.po.LiveChattingMessage;
import com.example.wkj_pc.fitnesslive.tools.AlertDialogTools;
import com.example.wkj_pc.fitnesslive.tools.AlertProgressDialogUtils;
import com.example.wkj_pc.fitnesslive.tools.GsonUtils;
import com.example.wkj_pc.fitnesslive.tools.LogUtils;
import com.example.wkj_pc.fitnesslive.tools.OkHttpClientFactory;
import com.example.wkj_pc.fitnesslive.tools.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.VideoView;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WatchUserLiveActivity extends AppCompatActivity {

    @BindView(R.id.watch_video_view)
    VideoView watchVideoView;
    @BindView(R.id.watcher_login_watch_live_logo)
    ImageView loginWatchLiveLogo;
    @BindView(R.id.watcher_watch_people_number)
    TextView watcherWatchPeopleNumber;
    @BindView(R.id.watcher_watch_fans_people_number)
    TextView watcherWatchFansPeopleNumber;
    @BindView(R.id.watcher_attention_user_watch_show_recycler_view)
    RecyclerView watcherAttentionUserWatchShowRecyclerView;
    @BindView(R.id.watcher_watch_video_chatting_edit_text)
    EditText watcherWatchVideoChattingEditText;

    @BindView(R.id.watcher_live_chatting_message_recycler_view)
    RecyclerView watcherLiveChattingMessageRecyclerView;
    private String watcherVideoUrl;    //拉取rmtp流的网络地址
    List<Integer> amatartLists;
    private String watchChattingWsUrl;  //观看者websocket地址
    private WebSocket baseWebSocket;//直播websocket
    private LiveChattingMessage message;
    private List<LiveChattingMessage> liveMessages = new ArrayList<>();//直播聊天信息;
    private LiveChattingMessagesAdapter adapter;
    private String liveUserAccount;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_user_live);
        ButterKnife.bind(this);
        adapter = new LiveChattingMessagesAdapter(liveMessages);
        liveUserAccount = getIntent().getStringExtra("liveuseraccount");
        ToastUtils.showToast(this,liveUserAccount,Toast.LENGTH_SHORT);
        watcherVideoUrl = getResources().getString(R.string.app_video_upload_srs_server_url)+liveUserAccount;
        watchChattingWsUrl = getResources().getString(R.string.app_message_websocket_customer_live_url)+
                liveUserAccount+"/"+MainApplication.loginUser.getAccount() + "/watchlive";
       /* if (!LibsChecker.checkVitamioLibs(this))websocket/liveaccount/watchaccount/live|watchlive
            return;*/
        initAmatarLists();
        initAttentionUserShowRecyclerView();
        watchVideoView.setVideoPath(watcherVideoUrl);   //设置用户拉取视频流地址
        watchVideoView.setBufferSize(1024);
        AlertProgressDialogUtils.alertProgressShow(this,"马上开始...");
        watchVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                watchVideoView.setBackground(null);
                AlertProgressDialogUtils.alertProgressClose();
                watchVideoView.start();
            }
        });
        getWebSocket(watchChattingWsUrl);   //设置用户直播聊天地址
    }
    /**
     * 观看者获取websocket，进行直播聊天消息显示更新
     */
    public void getWebSocket(String address) {
        System.out.println("WebSocketUtils client start");
        Request request = new Request.Builder().url(address)
                .build();
        OkHttpClientFactory.getOkHttpClientInstance().newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                baseWebSocket = webSocket;
                sendPingToServer();
                System.out.println("WebSocketUtils client onOpen\"+\"client request header:\" + response.request().headers()\n" +
                        "                +\"client response header:\" + response.headers()+\"client response:\" + response");
                LogUtils.logDebug("WebSocketUtils", "client onOpen" + "client request header:" + response.request().headers()
                        + "client response header:" + response.headers() + "client response:" + response);
            }
            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                System.out.println("----------" + text);
               /*处理收到的信息*/
                if (TextUtils.isEmpty(text)) //收到信息为空时，获取 /*如果收到信息为空，则返回不处理*/
                    return;
                /*如果返回信息为success，表示websocket连接建立成功，发送提示信息*/
                if (text.contentEquals("liveclosed")){
                    Toast.makeText(WatchUserLiveActivity.this, "直播已经结束", Toast.LENGTH_SHORT).show();
                }else if (text.contentEquals("success")) {
                    LiveChattingMessage message = new LiveChattingMessage();
                    message.setMid(0);
                    message.setFrom(MainApplication.loginUser.getNickname());
                    message.setTo("server");
                    message.setContent("来到直播间！");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd:HH/mm/SS");
                    message.setTime(format.format(new Date()));
                    message.setIntent(1);
                    webSocket.send(GsonUtils.getGson().toJson(message));
                } else {
                    try {
                        message = GsonUtils.getGson().fromJson(text, LiveChattingMessage.class);
                        if (message.getIntent() == 1) {    //聊天
                            liveMessages.add(message);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyItemInserted(liveMessages.size() - 1);
                                    watcherLiveChattingMessageRecyclerView.scrollToPosition(liveMessages.size() - 1);
                                }
                            });
                        } else if (message.getIntent() == 2) {  //粉丝
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    watcherWatchFansPeopleNumber.setText("粉丝:" + message.getFansnumber());
                                }
                            });
                        } else if (message.getIntent() == 3) {   //当前在线人数
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    watcherWatchPeopleNumber.setText("观看人数:" + message.getFansnumber());
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                baseWebSocket = null;
                LogUtils.logDebug("WebSocketUtils", "client onClosing" + "code:" + code + " reason:" + reason);
                System.out.println("WebSocketUtils" + "client onClosing" + "code:" + code + " reason:" + reason);
            }
            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                baseWebSocket = null;
                LogUtils.logDebug("webSocketUtils", "client onClosed" + "code:" + code + " reason:" + reason);
                System.out.println("webSocketUtils" + "client onClosed" + "code:" + code + " reason:" + reason);
            }
            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                baseWebSocket = null;
                //出现异常会进入此回调
                LogUtils.logDebug("WebSocketUtils", "client onFailure" + "throwable:" + t);
                System.out.println("WebSocketUtils" + "client onFailure" + "throwable:" + t);
            }
        });
    }
    //设置心跳防止websocket断线
    public void sendPingToServer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (null != baseWebSocket) {
                    baseWebSocket.send("");
                }
            }
        }, 0, 3000);
    }
    private void initAmatarLists() {
        amatartLists = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            amatartLists.add(R.drawable.head_img);
        }
    }
    private void initAttentionUserShowRecyclerView() {
        AttentionUserAdapter adapter = new AttentionUserAdapter(amatartLists);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        watcherAttentionUserWatchShowRecyclerView.setLayoutManager(manager);
        watcherAttentionUserWatchShowRecyclerView.setAdapter(adapter);
    }

    @OnClick({R.id.watcher_login_watch_live_logo, R.id.watcher_ic_send_watch_comment_message_icon,
            R.id.watcher_close_watch_live_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.watcher_login_watch_live_logo:
                break;
            case R.id.watcher_ic_send_watch_comment_message_icon:
                String editTextMsg = watcherWatchVideoChattingEditText.getText().toString().trim();
                if (TextUtils.isEmpty(editTextMsg)){
                    return;
                }
                final LiveChattingMessage sendMsg=new LiveChattingMessage();
                sendMsg.setFrom(MainApplication.loginUser.getNickname());
                sendMsg.setContent(editTextMsg);
                sendMsg.setTo("server");
                sendMsg.setMid(0);
                sendMsg.setIntent(1);
                SimpleDateFormat format=new SimpleDateFormat("HH:mm:ss");
                sendMsg.setTime(format.format(new Date()));
                if (null!=baseWebSocket){
                    baseWebSocket.send( GsonUtils.getGson().toJson(sendMsg));
                }else{
                    getWebSocket(watchChattingWsUrl);
                    baseWebSocket.send( GsonUtils.getGson().toJson(sendMsg));
                }
                watcherWatchVideoChattingEditText.setText("");
                break;
            case R.id.watcher_close_watch_live_icon:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        if (null!=baseWebSocket){
            baseWebSocket.cancel();
        }
    }
}