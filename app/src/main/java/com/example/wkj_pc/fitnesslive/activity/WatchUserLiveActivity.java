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
import com.bumptech.glide.Glide;
import com.example.wkj_pc.fitnesslive.MainApplication;
import com.example.wkj_pc.fitnesslive.R;
import com.example.wkj_pc.fitnesslive.adapter.WatchUserLiveAdapter;
import com.example.wkj_pc.fitnesslive.adapter.LiveChattingMessagesAdapter;
import com.example.wkj_pc.fitnesslive.fragment.LiveUserBottomInfoToastFragment;
import com.example.wkj_pc.fitnesslive.po.LiveChattingMessage;
import com.example.wkj_pc.fitnesslive.po.User;
import com.example.wkj_pc.fitnesslive.tools.AlertProgressDialogUtils;
import com.example.wkj_pc.fitnesslive.tools.GsonUtils;
import com.example.wkj_pc.fitnesslive.tools.OkHttpClientFactory;
import com.google.gson.reflect.TypeToken;
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
    @BindView(R.id.watcher_while_live_close_show_text_view)
    TextView watcherWhileLiveCloseShowTextView;
    @BindView(R.id.watcher_ic_send_watch_comment_message_icon)
    ImageView watcherIcSendWatchCommentMessageIcon;
    @BindView(R.id.watcher_close_watch_live_icon)
    ImageView watcherCloseWatchLiveIcon;
    private String watcherVideoUrl;    //拉取rmtp流的网络地址
    private String watchChattingWsUrl;  //观看者websocket地址
    private WebSocket baseWebSocket;//直播websocket
    private LiveChattingMessage message;    //直播聊天信息;
    private List<LiveChattingMessage> liveMessages ;//直播聊天信息链表
    private LiveChattingMessagesAdapter chattingAdapter;    //直播聊天信息适配器
    private String liveUserAccount; //直播用户的账户
    private Timer timer;    //定时访问websocket防止断线
    private User liveuser; //正在直播的用户
    private String getLiveuserInfoUrl;
    private List<User> watcherUsers;//观看直播用户的信息集合
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_user_live);
        ButterKnife.bind(this);
        liveuser=new User();//正在直播的用户
        liveUserAccount = getIntent().getStringExtra("liveuseraccount");
        liveMessages = new ArrayList<>();
        watcherVideoUrl = getResources().getString(R.string.app_video_upload_srs_server_url) + liveUserAccount;
        getLiveuserInfoUrl = getResources().getString(R.string.app_customer_live_getLiveUserInfo_url);
        watchChattingWsUrl = getResources().getString(R.string.app_message_websocket_customer_live_url) +
                liveUserAccount + "/" + MainApplication.loginUser.getAccount() + "/watchlive";
       /* if (!LibsChecker.checkVitamioLibs(this))websocket/liveaccount/watchaccount/live|watchlive
            return;*/
        initWatcherUserShowRecyclerView();
        watchVideoView.setVideoPath(watcherVideoUrl);   //设置用户拉取视频流地址
        watchVideoView.setBufferSize(1024);
        AlertProgressDialogUtils.alertProgressShow(this, false, "马上开始...");
        watchVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                watchVideoView.setBackground(null);
                AlertProgressDialogUtils.alertProgressClose();
                watchVideoView.start();
            }
        });
        initChattingRecyclerView();
        getWebSocket(watchChattingWsUrl);   //设置用户直播聊天地址
    }
    /**  设置聊天信息显示 */
    private void initChattingRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chattingAdapter = new LiveChattingMessagesAdapter(liveMessages);
        watcherLiveChattingMessageRecyclerView.setAdapter(chattingAdapter);
        watcherLiveChattingMessageRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * 观看者获取websocket，进行直播聊天消息显示更新
     */
    public void getWebSocket(String address) {
        Request request = new Request.Builder().url(address)
                .build();
        OkHttpClientFactory.getOkHttpClientInstance().newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                baseWebSocket = webSocket;
                sendPingToServer();
            }
            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                System.out.println("----------" + text);
               /*处理收到的信息*/
                if (TextUtils.isEmpty(text)) //收到信息为空时，获取 /*如果收到信息为空，则返回不处理*/
                    return;
                if (text.contentEquals("liveclosed"))
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertProgressDialogUtils.alertProgressClose();
                            watcherWhileLiveCloseShowTextView.setVisibility(View.VISIBLE);
                            watcherWatchVideoChattingEditText.setFocusable(false);
                            watcherIcSendWatchCommentMessageIcon.setFocusable(false);
                        }
                    });
                    return;
                }
                /*如果返回信息为success，表示websocket连接建立成功，发送提示信息*/
                if (text.contentEquals("success")) {
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
                                    chattingAdapter.notifyDataSetChanged();
                                    watcherLiveChattingMessageRecyclerView.scrollToPosition(liveMessages.size() - 1);
                                }
                            });
                        } else if (message.getIntent() == 2) {  //粉丝
                            final int fansnum=message.getFansnumber();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    watcherWatchFansPeopleNumber.setText("粉丝:" + fansnum);
                                }
                            });
                        }else if (message.getIntent() == 3) {   //用户头像地址
                            final String content=message.getContent();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Glide.with(WatchUserLiveActivity.this).load(content).asBitmap().into(loginWatchLiveLogo);
                                }
                            });
                        }
                    } catch (Exception e) {
                        /** 发生异常后，验证时候能装换成用户集合，然后展示在listview中*/
                        try {
                            watcherUsers = GsonUtils.getGson().fromJson(text, new TypeToken<List<User>>(){}.getType());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    initWatcherUserShowRecyclerView();
                                }
                            });
                        }catch (Exception e1){
                            e1.printStackTrace();
                        }
                    }
                }
            }
            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                baseWebSocket = null;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        watcherWhileLiveCloseShowTextView.setVisibility(View.VISIBLE);
                        watcherWatchVideoChattingEditText.setFocusable(false);
                        watcherIcSendWatchCommentMessageIcon.setFocusable(false);
                        for (int i=0;i<MainApplication.liveUsers.size();i++){
                            if (MainApplication.liveUsers.get(i).getAccount().contentEquals(liveUserAccount)){
                                MainApplication.liveUsers.remove(i);
                                break;
                            }
                        }
                    }
                });
            }
            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                baseWebSocket = null;
            }
            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                baseWebSocket = null;
            }
        });
    }
    /** 设置观看直播人物的头像显示 */
    private void initWatcherUserShowRecyclerView() {
        if (null!=watcherUsers){
            watcherWatchPeopleNumber.setText("观看人数:" + watcherUsers.size());
            WatchUserLiveAdapter adapter = new WatchUserLiveAdapter(watcherUsers,this,getSupportFragmentManager());
            LinearLayoutManager manager = new LinearLayoutManager(this);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            watcherAttentionUserWatchShowRecyclerView.setLayoutManager(manager);
            watcherAttentionUserWatchShowRecyclerView.setAdapter(adapter);
        }
    }

    @OnClick({R.id.watcher_login_watch_live_logo, R.id.watcher_ic_send_watch_comment_message_icon,
            R.id.watcher_close_watch_live_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            /** 点击直播用户头像，弹出信息*/
            case R.id.watcher_login_watch_live_logo:
                 new LiveUserBottomInfoToastFragment().show(getSupportFragmentManager(),"dialog");
                break;
            case R.id.watcher_ic_send_watch_comment_message_icon:
                String editTextMsg = watcherWatchVideoChattingEditText.getText().toString().trim();
                if (TextUtils.isEmpty(editTextMsg)) {
                    return;
                }
                final LiveChattingMessage sendMsg = new LiveChattingMessage();
                sendMsg.setFrom(MainApplication.loginUser.getNickname());
                sendMsg.setContent(editTextMsg);
                sendMsg.setTo("server");
                sendMsg.setMid(0);
                sendMsg.setIntent(1);
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                sendMsg.setTime(format.format(new Date()));
                if (null != baseWebSocket) {
                    baseWebSocket.send(GsonUtils.getGson().toJson(sendMsg));
                } else {
                    getWebSocket(watchChattingWsUrl);
                    baseWebSocket.send(GsonUtils.getGson().toJson(sendMsg));
                }
                watcherWatchVideoChattingEditText.setText("");
                break;
            case R.id.watcher_close_watch_live_icon:
                finish();
                break;
        }
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        if (null != baseWebSocket) {
            baseWebSocket.cancel();
        }
    }
}