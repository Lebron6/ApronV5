package com.compass.ux.manager;

import androidx.annotation.NonNull;

import com.apron.mobilesdk.state.ProtoMessage;
import com.compass.ux.base.BaseManager;
import com.compass.ux.constant.Constant;

import org.eclipse.paho.android.service.MqttAndroidClient;

import java.nio.charset.StandardCharsets;

import dji.sdk.keyvalue.key.FlightControllerKey;
import dji.sdk.keyvalue.key.KeyTools;
import dji.sdk.keyvalue.key.PayloadKey;
import dji.sdk.keyvalue.value.common.ComponentIndexType;
import dji.v5.common.callback.CommonCallbacks;
import dji.v5.common.error.IDJIError;
import dji.v5.manager.KeyManager;
import dji.v5.manager.aircraft.megaphone.FileInfo;
import dji.v5.manager.aircraft.megaphone.MegaphoneIndex;
import dji.v5.manager.aircraft.megaphone.PlayMode;
import dji.v5.manager.aircraft.megaphone.UploadType;
import dji.v5.manager.aircraft.megaphone.WorkMode;
import dji.v5.manager.interfaces.IMegaphoneManager;


public class MegaphoneManager extends BaseManager {

    private MegaphoneManager() {
    }

    private static class MegaphoneHolder {
        private static final MegaphoneManager INSTANCE = new MegaphoneManager();
    }

    public static MegaphoneManager getInstance() {
        return MegaphoneHolder.INSTANCE;
    }

    //设置目标喊话器的位置
    public void setMegaphoneIndex(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect) {
            String type = message.getPara().get(Constant.TYPE);
            IMegaphoneManager iMegaphoneManager = dji.v5.manager.aircraft.megaphone.MegaphoneManager.getInstance();
            iMegaphoneManager.setMegaphoneIndex(MegaphoneIndex.find(Integer.parseInt(type)), new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "设置喊话器位置失败:" + error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //设置喊话器音量
    public void setVolume(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect) {
            String type = message.getPara().get(Constant.TYPE);
            IMegaphoneManager iMegaphoneManager = dji.v5.manager.aircraft.megaphone.MegaphoneManager.getInstance();
            iMegaphoneManager.setVolume(Integer.parseInt(type), new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "设置喊话器音量失败:" + error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }

    }

    //设置播放模式
    public void setPlayMode(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect) {
            String type = message.getPara().get(Constant.TYPE);
            IMegaphoneManager iMegaphoneManager = dji.v5.manager.aircraft.megaphone.MegaphoneManager.getInstance();
            iMegaphoneManager.setPlayMode(PlayMode.find(Integer.parseInt(type)), new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "设置喊话器播放模式失败:" + error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }

    }

    //设置工作模式(TTS/语音)
    public void setWorkMode(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect) {
            String type = message.getPara().get(Constant.TYPE);
            IMegaphoneManager iMegaphoneManager = dji.v5.manager.aircraft.megaphone.MegaphoneManager.getInstance();
            iMegaphoneManager.setWorkMode(WorkMode.find(Integer.parseInt(type)), new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "设置喊话器工作模式失败:" + error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //播放
    public void startPlay(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect) {
            IMegaphoneManager iMegaphoneManager = dji.v5.manager.aircraft.megaphone.MegaphoneManager.getInstance();
            iMegaphoneManager.startPlay(new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "播放失败:" + error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //停止播放
    public void stopPlay(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect) {
            IMegaphoneManager iMegaphoneManager = dji.v5.manager.aircraft.megaphone.MegaphoneManager.getInstance();
            iMegaphoneManager.stopPlay(new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "播放失败:" + error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //上传文件到喊话器
    public void startPushingFileToMegaphone(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(PayloadKey.KeyConnection, ComponentIndexType.LEFT_OR_MAIN));
        if (isConnect) {
            String msg = message.getPara().get(Constant.VALUE);
            IMegaphoneManager iMegaphoneManager = dji.v5.manager.aircraft.megaphone.MegaphoneManager.getInstance();
            FileInfo fileInfo = new FileInfo(UploadType.TTS_DATA, null, msg.getBytes(StandardCharsets.UTF_8));
            iMegaphoneManager.startPushingFileToMegaphone(fileInfo, new CommonCallbacks.CompletionCallbackWithProgress() {
                @Override
                public void onProgressUpdate(Object progress) {
                }
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient, message);
                }
                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "上传TTS失败:" + error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "未挂载喊话器");
        }
    }
}
