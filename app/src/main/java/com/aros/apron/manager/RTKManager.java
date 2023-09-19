package com.aros.apron.manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.caelus.framework.iot.gateway.server.entity.ProtoBaseStationList;
import com.caelus.framework.iot.gateway.server.entity.ProtoBattery;
import com.caelus.framework.iot.gateway.server.entity.ProtoMessage;
import com.caelus.framework.iot.gateway.server.entity.ProtoRTKConnectionState;
import com.caelus.framework.iot.gateway.server.entity.ProtoRTKState;
import com.aros.apron.app.ApronApp;
import com.aros.apron.base.BaseManager;
import com.aros.apron.constant.Constant;
import com.aros.apron.constant.MqttConfig;
import com.aros.apron.entity.DataCache;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

import dji.sdk.keyvalue.key.BatteryKey;
import dji.sdk.keyvalue.key.FlightControllerKey;
import dji.sdk.keyvalue.key.KeyTools;
import dji.sdk.keyvalue.value.flightcontroller.FailsafeAction;
import dji.sdk.keyvalue.value.rtkbasestation.RTKStationConnetState;
import dji.sdk.keyvalue.value.rtkbasestation.RTKStationInfo;
import dji.v5.common.callback.CommonCallbacks;
import dji.v5.common.error.IDJIError;
import dji.v5.manager.KeyManager;
import dji.v5.manager.aircraft.rtk.RTKCenter;
import dji.v5.manager.aircraft.rtk.RTKSystemState;
import dji.v5.manager.aircraft.rtk.RTKSystemStateListener;
import dji.v5.manager.aircraft.rtk.station.ConnectedRTKStationInfo;
import dji.v5.manager.aircraft.rtk.station.ConnectedRTKStationInfoListener;
import dji.v5.manager.aircraft.rtk.station.RTKStationConnectStatusListener;
import dji.v5.manager.aircraft.rtk.station.SearchRTKStationListener;
import dji.v5.manager.interfaces.IRTKCenter;
import dji.v5.manager.interfaces.IRTKStationManager;

public class RTKManager extends BaseManager {
    MqttAndroidClient client;

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
            String type = message.getPara().get(Constant.TYPE);
            RTKCenter.getInstance().setAircraftRTKModuleEnabled(type.equals("1") ? true : false, new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "RTK模块设置失败:" + error.description());
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
            String type = message.getPara().get(Constant.TYPE);
            RTKCenter.getInstance().setRTKMaintainAccuracyEnabled(type.equals("1") ? true : false, new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "RTK精度保持设置失败:" + error.description());
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
            IRTKStationManager irtkStationManager = RTKCenter.getInstance().getRTKStationManager();
            irtkStationManager.startSearchRTKStation(new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "开始搜索RTK调用失败:" + error);
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
            IRTKStationManager irtkStationManager = RTKCenter.getInstance().getRTKStationManager();
            irtkStationManager.stopSearchRTKStation(new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "结束搜索RTK调用失败:" + error);
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
            int type = Integer.parseInt(message.getPara().get(Constant.TYPE));
            IRTKStationManager irtkStationManager = RTKCenter.getInstance().getRTKStationManager();
            irtkStationManager.startConnectToRTKStation(type, new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "结束搜索RTK调用失败:" + error);
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    public void initRtkInfo(MqttAndroidClient client) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect) {
            this.client = client;
            //RTK搜索列表监听
            RTKCenter.getInstance().getRTKStationManager().addSearchRTKStationListener(new SearchRTKStationListener() {
                @Override
                public void onUpdate(List<RTKStationInfo> newValue) {
                    String submit = "";
                    if (newValue.size() > 0) {
                        for (int i = 0; i < newValue.size(); i++) {
                            submit += newValue.get(i).getStationName() + "-" + newValue.get(i).getStationId() + ",";
                        }
                    }
                    ProtoBaseStationList.RTKBaseStationList.Builder builder = ProtoBaseStationList.RTKBaseStationList.newBuilder()
                            .setRtkBaseStationInformations(submit);
                    if (isFlyClickTime()) {
                        MqttMessage rtkMessage = new MqttMessage(builder.build().toByteArray());
                        rtkMessage.setQos(2);
//                        publish(client, MqttConfig.MQTT_RTK_LIST_INFO_TOPIC, rtkMessage);
                    }
                }
            });

            //RTK连接状态监听
            RTKCenter.getInstance().getRTKStationManager().addRTKStationConnectStatusListener(new RTKStationConnectStatusListener() {
                @Override
                public void onUpdate(RTKStationConnetState rtkStationConnetState) {
                    ProtoRTKConnectionState.RTKConnectionState.Builder builder = ProtoRTKConnectionState.RTKConnectionState.newBuilder()
                            .setRtkConnectionStateWithBaseStationReferenceSource(ProtoRTKConnectionState.RTKConnectionState.RTKConnectionStateWithBaseStationReferenceSource.values()[rtkStationConnetState.ordinal()]);
                    if (isFlyClickTime()) {
                        MqttMessage message = new MqttMessage(builder.build().toByteArray());
                        message.setQos(2);
//                        publish(client, MqttConfig.MQTT_RTK_CONNECTION_STATE_TOPIC, message);
                    }
                }
            });
            //已连接的RTK基站信息监听器。
//            RTKCenter.getInstance().getRTKStationManager().addConnectedRTKStationInfoListener(new ConnectedRTKStationInfoListener() {
//                @Override
//                public void onUpdate(ConnectedRTKStationInfo stationInfo) {
//                }
//            });
//
//            RTKCenter.getInstance().addRTKSystemStateListener(new RTKSystemStateListener() {
//                @Override
//                public void onUpdate(RTKSystemState rtkSystemState) {
//                    rtkSystemState.getSatelliteInfo().
//                }
//            });
        }

    }


}
