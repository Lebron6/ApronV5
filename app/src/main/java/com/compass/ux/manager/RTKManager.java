package com.compass.ux.manager;

import androidx.annotation.NonNull;

import com.apron.mobilesdk.state.ProtoMessage;
import com.compass.ux.app.ApronApp;
import com.compass.ux.base.BaseManager;
import com.compass.ux.constant.Constant;

import org.eclipse.paho.android.service.MqttAndroidClient;

import dji.sdk.keyvalue.key.FlightControllerKey;
import dji.sdk.keyvalue.key.KeyTools;
import dji.sdk.keyvalue.value.flightcontroller.FailsafeAction;
import dji.sdk.keyvalue.value.rtkbasestation.RTKStationInfo;
import dji.v5.common.callback.CommonCallbacks;
import dji.v5.common.error.IDJIError;
import dji.v5.manager.KeyManager;
import dji.v5.manager.aircraft.rtk.RTKCenter;
import dji.v5.manager.interfaces.IRTKCenter;
import dji.v5.manager.interfaces.IRTKStationManager;

public class RTKManager extends BaseManager {

    private RTKManager() {
    }

    private static class RTKHolder {
        private static final RTKManager INSTANCE = new RTKManager();
    }

    public static RTKManager getInstance() {
        return RTKHolder.INSTANCE;
    }


    //设置rtk是否启用
    public void setRtkEnabled(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect) {
            String type=message.getPara().get(Constant.TYPE);
            RTKCenter.getInstance().setAircraftRTKModuleEnabled(type.equals("1")?true:false, new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient,message);
                }
                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient,message,"RTK模块设置失败:"+error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //设置rtk精度保持
    public void setRTKMaintainAccuracyEnabled(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect) {
            String type=message.getPara().get(Constant.TYPE);
            RTKCenter.getInstance().setRTKMaintainAccuracyEnabled(type.equals("1")?true:false, new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient,message);
                }
                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient,message,"RTK精度保持设置失败:"+error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //开始搜索基站
    public void startSearchRTKStation(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect) {
            IRTKStationManager irtkStationManager=RTKCenter.getInstance().getRTKStationManager();
            irtkStationManager.startSearchRTKStation(new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient,message);
                }
                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient,message,"开始搜索RTK调用失败:"+error);
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //结束搜索
    public void stopSearchRTKStation(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect) {
            IRTKStationManager irtkStationManager=RTKCenter.getInstance().getRTKStationManager();
            irtkStationManager.stopSearchRTKStation(new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient,message);
                }
                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient,message,"结束搜索RTK调用失败:"+error);
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //连接基站
    public void startConnectToRTKStation(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect) {
            int type =Integer.parseInt(message.getPara().get(Constant.TYPE));
            IRTKStationManager irtkStationManager=RTKCenter.getInstance().getRTKStationManager();
            irtkStationManager.startConnectToRTKStation(type,new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient,message);
                }
                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient,message,"结束搜索RTK调用失败:"+error);
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

}
