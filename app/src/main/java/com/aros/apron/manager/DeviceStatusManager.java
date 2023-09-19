package com.aros.apron.manager;

import androidx.annotation.Nullable;

import com.aros.apron.base.BaseManager;
import com.aros.apron.constant.MqttConfig;
import com.aros.apron.entity.AirLinkStateEntity;
import com.aros.apron.entity.DeviceStatusEntity;
import com.google.gson.Gson;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;

import dji.sdk.keyvalue.key.AirLinkKey;
import dji.sdk.keyvalue.key.FlightControllerKey;
import dji.sdk.keyvalue.key.KeyTools;
import dji.sdk.keyvalue.value.airlink.AirLinkType;
import dji.sdk.keyvalue.value.airlink.FrequencyInterferenceInfo;
import dji.v5.common.callback.CommonCallbacks;
import dji.v5.manager.KeyManager;
import dji.v5.manager.diagnostic.DJIDeviceStatus;
import dji.v5.manager.diagnostic.DJIDeviceStatusChangeListener;
import dji.v5.manager.interfaces.IDeviceStatusManager;

public class DeviceStatusManager extends BaseManager {

    MqttAndroidClient client;


    private DeviceStatusManager() {
    }

    private static class DeviceStatusHolder {
        private static final DeviceStatusManager INSTANCE = new DeviceStatusManager();
    }

    public static DeviceStatusManager getInstance() {
        return DeviceStatusHolder.INSTANCE;
    }

    public void initDeviceStatus(MqttAndroidClient client) {
        this.client = client;

        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect != null && isConnect) {

            IDeviceStatusManager iDeviceStatusManager = dji.v5.manager.diagnostic.DeviceStatusManager.getInstance();
            iDeviceStatusManager.addDJIDeviceStatusChangeListener(new DJIDeviceStatusChangeListener() {
                @Override
                public void onDeviceStatusUpdate(DJIDeviceStatus from, DJIDeviceStatus to) {
                    DeviceStatusEntity.getInstance().setDeviceStatusCode(to.statusCode());
                    DeviceStatusEntity.getInstance().setDescription(to.description());
                    DeviceStatusEntity.getInstance().setWarningLevel(to.warningLevel().ordinal());
                    publishDeviceStatus2Server();
                }
            });

        }

    }

    private void publishDeviceStatus2Server() {
        if (isFlyClickTime()) {
            MqttMessage flightMessage = null;
            try {
//                Log.e("推送DeviceStatus状态", new Gson().toJson(DeviceStatusEntity.getInstance()));
                flightMessage = new MqttMessage(new Gson().toJson(DeviceStatusEntity.getInstance()).getBytes("UTF-8"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            flightMessage.setQos(1);
            publish(client, MqttConfig.MQTT_DEVICE_STATUS, flightMessage);
        }
    }

}
