package com.example.wkj_pc.fitnesslive.tools;


import com.example.wkj_pc.fitnesslive.MainApplication;
import com.example.wkj_pc.fitnesslive.activity.LiveActivity;
import com.example.wkj_pc.fitnesslive.po.LiveChattingMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * Created by wkj_pc on 2017/8/6.
 */

public class WebSocketUtils {
    public static WebSocket baseWebSocket;
    public static void getWebSocket(String address){
        System.out.println("WebSocketUtils client start");
        Request request=new Request.Builder().url(address)
                .build();
        OkHttpClientFactory.getOkHttpClientInstance().newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
               baseWebSocket=webSocket;
                LogUtils.logDebug("WebSocketUtils","client onOpen"+"client request header:" + response.request().headers()
                +"client response header:" + response.headers()+"client response:" + response);
            }
            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                LogUtils.logDebug("WebSocketUtils","text"+text);
                if (text.contentEquals("success")){
                    LiveChattingMessage message=new LiveChattingMessage();
                    message.setMid(0);
                    message.setFrom(MainApplication.loginUser.getAccount());
                    message.setTo("server");
                    message.setContent("来到直播间！");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd:HH/mm/SS");
                    message.setTime(format.format(new Date()));
                    webSocket.send( GsonUtils.getGson().toJson(message));
                }else{
                    System.out.println("---------------------"+text);
                    LiveChattingMessage message = GsonUtils.getGson().fromJson(text, LiveChattingMessage.class);

                    //LiveActivity.liveMessages.add(message);
                }
            }
            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                LogUtils.logDebug("WebSocketUtils","client onClosing"+"code:" + code + " reason:" + reason);
            }
            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                LogUtils.logDebug("webSocketUtils","client onClosed"+"code:" + code + " reason:" + reason);

            }
            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                //出现异常会进入此回调
                LogUtils.logDebug("WebSocketUtils","client onFailure"+"throwable:" + t);
            }
        });
    };

}
