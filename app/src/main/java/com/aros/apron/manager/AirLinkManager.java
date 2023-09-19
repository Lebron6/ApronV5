package com.aros.apron.manager;

import android.util.Log;

import androidx.annotation.Nullable;

import com.aros.apron.base.BaseManager;
import com.aros.apron.constant.MqttConfig;
import com.aros.apron.entity.AirLinkStateEntity;
import com.aros.apron.entity.GimbalAStateEntity;
import com.google.gson.Gson;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;

import dji.sdk.keyvalue.key.AirLinkKey;
import dji.sdk.keyvalue.key.KeyTools;
import dji.sdk.keyvalue.value.airlink.AirLinkType;
import dji.sdk.keyvalue.value.airlink.FrequencyInterferenceInfo;
import dji.v5.common.callback.CommonCallbacks;
import dji.v5.manager.KeyManager;

public class AirLinkManager extends BaseManager {

    MqttAndroidClient client;


    private AirLinkManager() {
    }

    private static class AirLinkHolder {
        private static final AirLinkManager INSTANCE = new AirLinkManager();
    }

    public static AirLinkManager getInstance() {
        return AirLinkHolder.INSTANCE;
    }

    public void initAirLinkInfo(MqttAndroidClient client) {
        this.client = client;

        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(AirLinkKey.KeyConnection, 0));
        if (isConnect!=null&&isConnect) {


            KeyManager.getInstance().listen(KeyTools.createKey(AirLinkKey.
                    KeyAirLinkType), this, new CommonCallbacks.KeyListener<AirLinkType>() {
                @Override
                public void onValueChange(@Nullable AirLinkType oldValue, @Nullable AirLinkType newValue) {
                    AirLinkStateEntity.getInstance().setAirLinkType(newValue.value());
                    publishAirLink2Server();

                }
            });

            KeyManager.getInstance().listen(KeyTools.createKey(AirLinkKey.
                    KeySignalQuality), this, new CommonCallbacks.KeyListener<Integer>() {
                @Override
                public void onValueChange(@Nullable Integer oldValue, @Nullable Integer newValue) {
                    AirLinkStateEntity.getInstance().setSignalQuality(newValue);
                    publishAirLink2Server();

                }
            });

            KeyManager.getInstance().listen(KeyTools.createKey(AirLinkKey.
                    KeyDynamicDataRate), this, new CommonCallbacks.KeyListener<Double>() {
                @Override
                public void onValueChange(@Nullable Double oldValue, @Nullable Double newValue) {
                    AirLinkStateEntity.getInstance().setDynamicDataRate(newValue);
                    publishAirLink2Server();

                }
            });

            KeyManager.getInstance().listen(KeyTools.createKey(AirLinkKey.
                    KeyFrequencyInterference), this, new CommonCallbacks.KeyListener<List<FrequencyInterferenceInfo>>() {
                @Override
                public void onValueChange(@Nullable List<FrequencyInterferenceInfo> oldValue, @Nullable List<FrequencyInterferenceInfo> newValue) {
//                    AirLinkStateEntity.getInstance().setFrequencyInterferences(newValue);
//                    publishAirLink2Server();
                }
            });
        }

    }

    private void publishAirLink2Server() {
        if (isFlyClickTime()) {
            //推送飞行状态
            MqttMessage flightMessage = null;
            try {
//                Log.e("推送AirLink状态", new Gson().toJson(AirLinkStateEntity.getInstance()));
                flightMessage = new MqttMessage(new Gson().toJson(AirLinkStateEntity.getInstance()).getBytes("UTF-8"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            flightMessage.setQos(1);
            publish(client, MqttConfig.MQTT_AIRLINK_TOPIC, flightMessage);
        }
    }

}
