package com.compass.ux.manager;


import androidx.annotation.NonNull;

import com.apron.mobilesdk.state.ProtoMessage;
import com.compass.ux.base.BaseManager;
import com.compass.ux.constant.Constant;

import org.eclipse.paho.android.service.MqttAndroidClient;

import dji.sdk.keyvalue.key.FlightControllerKey;
import dji.sdk.keyvalue.key.KeyTools;
import dji.sdk.keyvalue.value.common.LocationCoordinate2D;
import dji.sdk.keyvalue.value.flightassistant.PrecisionLandingMode;
import dji.v5.common.callback.CommonCallbacks;
import dji.v5.common.error.IDJIError;
import dji.v5.manager.KeyManager;
import dji.v5.manager.aircraft.perception.ObstacleAvoidanceType;
import dji.v5.manager.aircraft.perception.PerceptionDirection;
import dji.v5.manager.aircraft.perception.PerceptionInformationListener;
import dji.v5.manager.interfaces.IPerceptionManager;


public class PerceptionManager extends BaseManager {

    private PerceptionManager() {
    }

    private static class PerceptionHolder {
        private static final PerceptionManager INSTANCE = new PerceptionManager();
    }

    public static PerceptionManager getInstance() {
        return PerceptionHolder.INSTANCE;
    }

    public void setPrecisionLanding(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.
                KeyConnection));
        if (isConnect) {
            String type = message.getPara().get(Constant.TYPE);
            IPerceptionManager iPerceptionManager = dji.v5.manager.aircraft.perception.PerceptionManager.getInstance();
            iPerceptionManager.setPrecisionLandingEnabled(type.equals("1") ? true : false, new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "精确着陆设置失败:" + error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    public void setVisionPositioningEnabled(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.
                KeyConnection));
        if (isConnect) {
            String type = message.getPara().get(Constant.TYPE);
            IPerceptionManager iPerceptionManager = dji.v5.manager.aircraft.perception.PerceptionManager.getInstance();
            iPerceptionManager.setVisionPositioningEnabled(type.equals("1") ? true : false, new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "视觉定位设置失败:" + error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //设置避障总开关
    public void setObstacleAvoidanceType(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.
                KeyConnection));
        if (isConnect) {
            String type = message.getPara().get(Constant.TYPE);
            IPerceptionManager iPerceptionManager = dji.v5.manager.aircraft.perception.PerceptionManager.getInstance();
            iPerceptionManager.setObstacleAvoidanceType(type.equals("0") ? ObstacleAvoidanceType.CLOSE : ObstacleAvoidanceType.BRAKE, new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient, message);
                }
                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "设置避障总开关失败:" + error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //设置避障子开关
    public void setObstacleAvoidanceEnabled(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.
                KeyConnection));
        if (isConnect) {
            String type = message.getPara().get(Constant.TYPE);
            String value=message.getPara().get(Constant.VALUE);
            PerceptionDirection perceptionDirection= PerceptionDirection.UPWARD;
            switch (value){
                case "0":
                    perceptionDirection = PerceptionDirection.UPWARD;
                    break;
                case "1":
                    perceptionDirection = PerceptionDirection.DOWNWARD;
                    break;
                case "2":
                    perceptionDirection = PerceptionDirection.HORIZONTAL;
                    break;
            }
            IPerceptionManager iPerceptionManager = dji.v5.manager.aircraft.perception.PerceptionManager.getInstance();
            iPerceptionManager.setObstacleAvoidanceEnabled(type.equals("0")?false:true, perceptionDirection,new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient, message);
                }
                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "设置避障子开关失败:" + error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //设置避障告警距离,单位：米。
    //水平告警距离范围：[1.1, 33.0]。
    //上方告警距离范围：[1.1, 33.0]。
    //下方告警距离范围：[0.6, 33.0]。
    public void setObstacleAvoidanceWarningDistance(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.
                KeyConnection));
        if (isConnect) {
            String value=message.getPara().get(Constant.VALUE);
            String distance=message.getPara().get(Constant.DISTANCE);
            PerceptionDirection perceptionDirection= PerceptionDirection.UPWARD;
            switch (value){
                case "0":
                    perceptionDirection = PerceptionDirection.UPWARD;
                    break;
                case "1":
                    perceptionDirection = PerceptionDirection.DOWNWARD;
                    break;
                case "2":
                    perceptionDirection = PerceptionDirection.HORIZONTAL;
                    break;
            }
            IPerceptionManager iPerceptionManager = dji.v5.manager.aircraft.perception.PerceptionManager.getInstance();
            iPerceptionManager.setObstacleAvoidanceWarningDistance(Double.valueOf(distance), perceptionDirection,new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient, message);
                }
                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "设置避障警告触发距离失败:" + error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //设置避障刹停距离,单位：米。
    //水平刹停距离范围：[1.0, 10.0]。
    //上方刹停距离范围：[1.0, 10.0]。
    //下方刹停距离范围：[0.5, 3.0]。
    public void setObstacleAvoidanceBrakingDistance(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.
                KeyConnection));
        if (isConnect) {
            String value=message.getPara().get(Constant.VALUE);
            String distance=message.getPara().get(Constant.DISTANCE);
            PerceptionDirection perceptionDirection= PerceptionDirection.UPWARD;
            switch (value){
                case "0":
                    perceptionDirection = PerceptionDirection.UPWARD;
                    break;
                case "1":
                    perceptionDirection = PerceptionDirection.DOWNWARD;
                    break;
                case "2":
                    perceptionDirection = PerceptionDirection.HORIZONTAL;
                    break;
            }
            IPerceptionManager iPerceptionManager = dji.v5.manager.aircraft.perception.PerceptionManager.getInstance();
            iPerceptionManager.setObstacleAvoidanceBrakingDistance(Double.valueOf(distance), perceptionDirection,new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient, message);
                }
                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "设置避障刹停触发距离失败:" + error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

}
