package com.compass.ux.manager;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.apron.mobilesdk.state.ProtoMessage;
import com.compass.ux.base.BaseManager;

import org.eclipse.paho.android.service.MqttAndroidClient;

import dji.sdk.keyvalue.key.DJIKey;
import dji.sdk.keyvalue.key.ProductKey;
import dji.v5.common.callback.CommonCallbacks;
import dji.v5.common.error.IDJIError;
import dji.v5.common.video.channel.VideoChannelType;
import dji.v5.manager.KeyManager;
import dji.v5.manager.datacenter.livestream.LiveStreamManager;
import dji.v5.manager.datacenter.livestream.LiveStreamSettings;
import dji.v5.manager.datacenter.livestream.LiveStreamType;
import dji.v5.manager.datacenter.livestream.LiveVideoBitrateMode;
import dji.v5.manager.datacenter.livestream.StreamQuality;
import dji.v5.manager.datacenter.livestream.settings.GB28181Settings;
import dji.v5.manager.datacenter.livestream.settings.RtmpSettings;
import dji.v5.manager.datacenter.livestream.settings.RtspSettings;
import dji.v5.manager.interfaces.ILiveStreamManager;


public class StreamManager extends BaseManager {

    private StreamManager() {
    }

    private static class StreamHolder {
        private static final StreamManager INSTANCE = new StreamManager();
    }

    public static StreamManager getInstance() {
        return StreamHolder.INSTANCE;
    }

    public void setLiveParametersWithRTMP(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isAircraftConnected = KeyManager.getInstance().getValue(DJIKey.create(ProductKey.KeyConnection));
        if (isAircraftConnected == null || !isAircraftConnected) {
            sendMsg2Server(mqttAndroidClient, message, "飞行器未连接");
        } else {
            String url = message.getPara().get("url");

            if (TextUtils.isEmpty(url)) {
                sendMsg2Server(mqttAndroidClient, message, "推流地址配置有误");
                return;
            }

            LiveStreamSettings.Builder streamSettingBuilder = new LiveStreamSettings.Builder();
            LiveStreamSettings streamSettings = streamSettingBuilder.setLiveStreamType(LiveStreamType.RTMP)
                    .setRtmpSettings(new RtmpSettings.Builder().setUrl(url).build()).build();
            startLive(mqttAndroidClient, message, streamSettings);
        }
    }

    public void setLiveParametersWithRTSP(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isAircraftConnected = KeyManager.getInstance().getValue(DJIKey.create(ProductKey.KeyConnection));
        if (isAircraftConnected == null || !isAircraftConnected) {
            sendMsg2Server(mqttAndroidClient, message, "飞行器未连接");
        } else {
            String username = message.getPara().get("username");
            String password = message.getPara().get("password");
            String port = message.getPara().get("port");

            if (TextUtils.isEmpty(username)) {
                sendMsg2Server(mqttAndroidClient, message, "RTSP username 设置有误");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                sendMsg2Server(mqttAndroidClient, message, "RTSP password 设置有误");
                return;
            }if (TextUtils.isEmpty(port)) {
                sendMsg2Server(mqttAndroidClient, message, "RTSP port 设置有误");
                return;
            }

            LiveStreamSettings.Builder streamSettingBuilder = new LiveStreamSettings.Builder();
            LiveStreamSettings streamSettings = streamSettingBuilder.setLiveStreamType(LiveStreamType.RTSP)
                    .setRtspSettings(new RtspSettings.Builder().setPassWord(password).setPort(Integer.parseInt(port)).setUserName(username).build()).build();
            startLive(mqttAndroidClient, message, streamSettings);
        }
    }

    public void setLiveParametersWithGB28181(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isAircraftConnected = KeyManager.getInstance().getValue(DJIKey.create(ProductKey.KeyConnection));
        if (isAircraftConnected == null || !isAircraftConnected) {
            sendMsg2Server(mqttAndroidClient, message, "飞行器未连接");
        } else {
            String serverIP=message.getPara().get("serverIP");
            String serverPort=message.getPara().get("serverPort");
            String serverID=message.getPara().get("serverID");
            String agentID=message.getPara().get("agentID");
            String password=message.getPara().get("password");
            String localPort=message.getPara().get("localPort");
            String channel=message.getPara().get("channel");
            if (TextUtils.isEmpty(serverIP)) {
                sendMsg2Server(mqttAndroidClient, message, "serverIP error");
                return;
            }
            if (TextUtils.isEmpty(serverPort)) {
                sendMsg2Server(mqttAndroidClient, message, "serverPort error");
                return;
            }
            if (TextUtils.isEmpty(serverID)) {
                sendMsg2Server(mqttAndroidClient, message, "serverID error");
                return;
            }
            if (TextUtils.isEmpty(agentID)) {
                sendMsg2Server(mqttAndroidClient, message, "agentID error");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                sendMsg2Server(mqttAndroidClient, message, "password error");
                return;
            }
            if (TextUtils.isEmpty(localPort)) {
                sendMsg2Server(mqttAndroidClient, message, "localPort error");
                return;
            }
            if (TextUtils.isEmpty(channel)) {
                sendMsg2Server(mqttAndroidClient, message, "channel error");
                return;
            }
            LiveStreamSettings.Builder streamSettingBuilder = new LiveStreamSettings.Builder();
            LiveStreamSettings streamSettings = streamSettingBuilder.setLiveStreamType(LiveStreamType.GB28181)
                    .setGB28181Settings(new GB28181Settings.Builder().setAgentID(agentID)
                            .setChannel(channel).setLocalPort(Integer.parseInt(localPort))
                            .setServerIP(serverIP).setServerID(serverID)
                            .setPassword(password).setServerPort(Integer.parseInt(serverPort)).build()).build();
            startLive(mqttAndroidClient, message, streamSettings);
        }
    }

    private void startLive(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message, LiveStreamSettings streamSettings) {

        String liveStreamQuality = message.getPara().get("liveStreamQuality");
        String liveVideoBitrateMode = message.getPara().get("liveVideoBitrateMode");
        String liveVideoBitrate = message.getPara().get("liveVideoBitrate");
        if (TextUtils.isEmpty(liveStreamQuality)) {
            sendMsg2Server(mqttAndroidClient, message, "推流分辨率参数有误");
            return;
        }
        if (TextUtils.isEmpty(liveVideoBitrateMode)) {
            sendMsg2Server(mqttAndroidClient, message, "推流码率设置方式参数有误");
            return;
        }

        ILiveStreamManager iLiveStreamManager = LiveStreamManager.getInstance();
        iLiveStreamManager.setLiveStreamSettings(streamSettings);
        iLiveStreamManager.setVideoChannelType(VideoChannelType.PRIMARY_STREAM_CHANNEL);
        iLiveStreamManager.setLiveStreamQuality(StreamQuality.find(Integer.parseInt(liveStreamQuality)));
        if (liveVideoBitrateMode.equals("1") && !TextUtils.isEmpty(liveVideoBitrate)) {
            iLiveStreamManager.setLiveVideoBitrateMode(LiveVideoBitrateMode.MANUAL);
            iLiveStreamManager.setLiveVideoBitrate(Integer.parseInt(liveVideoBitrate));
        }else{
            iLiveStreamManager.setLiveVideoBitrateMode(LiveVideoBitrateMode.AUTO);
        }
        iLiveStreamManager.startStream(new CommonCallbacks.CompletionCallback() {
            @Override
            public void onSuccess() {
                sendMsg2Server(mqttAndroidClient,message);
            }
            @Override
            public void onFailure(@NonNull IDJIError error) {
                sendMsg2Server(mqttAndroidClient,message,"开启直播失败:"+error.description());
            }
        });
    }

    public void stopLive(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        ILiveStreamManager iLiveStreamManager = LiveStreamManager.getInstance();
        iLiveStreamManager.stopStream(new CommonCallbacks.CompletionCallback() {
            @Override
            public void onSuccess() {
                sendMsg2Server(mqttAndroidClient,message);
            }
            @Override
            public void onFailure(@NonNull IDJIError error) {
                sendMsg2Server(mqttAndroidClient,message,"停止直播失败:"+error.description());
            }
        });
    }

}
