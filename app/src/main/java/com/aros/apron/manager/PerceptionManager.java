package com.aros.apron.manager;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aros.apron.constant.MqttConfig;
import com.aros.apron.entity.AirLinkStateEntity;
import com.aros.apron.entity.FlightStateEntity;
import com.aros.apron.entity.PerceptionInfoEntity;
import com.caelus.framework.iot.gateway.server.entity.ProtoMessage;
import com.aros.apron.base.BaseManager;
import com.aros.apron.constant.Constant;
import com.google.gson.Gson;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import dji.sdk.keyvalue.key.FlightControllerKey;
import dji.sdk.keyvalue.key.KeyTools;
import dji.sdk.keyvalue.value.common.LocationCoordinate2D;
import dji.sdk.keyvalue.value.flightassistant.PrecisionLandingMode;
import dji.v5.common.callback.CommonCallbacks;
import dji.v5.common.error.IDJIError;
import dji.v5.manager.KeyManager;

import dji.v5.manager.aircraft.perception.data.ObstacleAvoidanceType;
import dji.v5.manager.aircraft.perception.data.PerceptionDirection;
import dji.v5.manager.aircraft.perception.data.PerceptionInfo;
import dji.v5.manager.aircraft.perception.listener.PerceptionInformationListener;
import dji.v5.manager.interfaces.IPerceptionManager;


public class PerceptionManager extends BaseManager {

    MqttAndroidClient mqttAndroidClient;

    private PerceptionManager() {
    }

    private static class PerceptionHolder {
        private static final PerceptionManager INSTANCE = new PerceptionManager();
    }

    public static PerceptionManager getInstance() {
        return PerceptionHolder.INSTANCE;
    }


    public void initPerceptionInfo(MqttAndroidClient mqttAndroidClient) {
        this.mqttAndroidClient = mqttAndroidClient;
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect != null && isConnect) {
            IPerceptionManager iPerceptionManager = dji.v5.manager.aircraft.perception.PerceptionManager.getInstance();
            iPerceptionManager.addPerceptionInformationListener(new PerceptionInformationListener() {
                @Override
                public void onUpdate(@NonNull PerceptionInfo information) {
                    PerceptionInfoEntity.getInstance().setUpwardObstacleAvoidanceWorking(information.getUpwardObstacleAvoidanceWorking()?1:0);
                    PerceptionInfoEntity.getInstance().setDownwardObstacleAvoidanceWorking(information.getDownwardObstacleAvoidanceWorking()?1:0);
                    PerceptionInfoEntity.getInstance().setLeftSideObstacleAvoidanceWorking(information.getLeftSideObstacleAvoidanceWorking()?1:0);
                    PerceptionInfoEntity.getInstance().setRightSideObstacleAvoidanceWorking(information.getRightSideObstacleAvoidanceWorking()?1:0);
                    PerceptionInfoEntity.getInstance().setForwardObstacleAvoidanceWorking(information.getForwardObstacleAvoidanceWorking()?1:0);
                    PerceptionInfoEntity.getInstance().setBackwardObstacleAvoidanceWorking(information.getBackwardObstacleAvoidanceWorking()?1:0);
                    PerceptionInfoEntity.getInstance().setUpwardObstacleAvoidanceEnabled(information.isUpwardObstacleAvoidanceEnabled()?1:0);
                    PerceptionInfoEntity.getInstance().setDownwardObstacleAvoidanceEnabled(information.isDownwardObstacleAvoidanceEnabled()?1:0);
                    PerceptionInfoEntity.getInstance().setHorizontalObstacleAvoidanceEnabled(information.isHorizontalObstacleAvoidanceEnabled()?1:0);
                    PerceptionInfoEntity.getInstance().setUpwardObstacleAvoidanceWarningDistance(information.getUpwardObstacleAvoidanceWarningDistance());
                    PerceptionInfoEntity.getInstance().setDownwardObstacleAvoidanceWarningDistance(information.getDownwardObstacleAvoidanceWarningDistance());
                    PerceptionInfoEntity.getInstance().setHorizontalObstacleAvoidanceWarningDistance(information.getHorizontalObstacleAvoidanceWarningDistance());
                    PerceptionInfoEntity.getInstance().setHorizontalObstacleAvoidanceBrakingDistance(information.getHorizontalObstacleAvoidanceBrakingDistance());
                    PerceptionInfoEntity.getInstance().setDownwardObstacleAvoidanceBrakingDistance(information.getDownwardObstacleAvoidanceBrakingDistance());
                    PerceptionInfoEntity.getInstance().setUpwardObstacleAvoidanceBrakingDistance(information.getUpwardObstacleAvoidanceBrakingDistance());
                    PerceptionInfoEntity.getInstance().setIsVisionPositioningEnabled(information.isVisionPositioningEnabled()?1:0);
                    PerceptionInfoEntity.getInstance().setIsPrecisionLandingEnabled(information.isPrecisionLandingEnabled()?1:0);
                    PerceptionInfoEntity.getInstance().setObstacleAvoidanceType(information.getObstacleAvoidanceType().ordinal());
                    publishPerceptionInfo2Server();

                }
            });

        }
    }
    private void publishPerceptionInfo2Server() {
        if (isFlyClickTime()) {
            //推送飞行状态
            MqttMessage flightMessage = null;
            try {
//                Log.e("推送感知状态", new Gson().toJson(PerceptionInfoEntity.getInstance()));
                flightMessage = new MqttMessage(new Gson().toJson(PerceptionInfoEntity.getInstance()).getBytes("UTF-8"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            flightMessage.setQos(1);
            publish(mqttAndroidClient, MqttConfig.MQTT_PERCEPTIONINFO_TOPIC, flightMessage);
        }
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
            String value = message.getPara().get(Constant.VALUE);
            PerceptionDirection perceptionDirection = PerceptionDirection.UPWARD;
            switch (value) {
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
            iPerceptionManager.setObstacleAvoidanceEnabled(type.equals("0") ? false : true, perceptionDirection, new CommonCallbacks.CompletionCallback() {
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
            String value = message.getPara().get(Constant.VALUE);
            String distance = message.getPara().get(Constant.DISTANCE);
            PerceptionDirection perceptionDirection = PerceptionDirection.UPWARD;
            switch (value) {
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
            iPerceptionManager.setObstacleAvoidanceWarningDistance(Double.valueOf(distance), perceptionDirection, new CommonCallbacks.CompletionCallback() {
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
            String value = message.getPara().get(Constant.VALUE);
            String distance = message.getPara().get(Constant.DISTANCE);
            PerceptionDirection perceptionDirection = PerceptionDirection.UPWARD;
            switch (value) {
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
            iPerceptionManager.setObstacleAvoidanceBrakingDistance(Double.valueOf(distance), perceptionDirection, new CommonCallbacks.CompletionCallback() {
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
