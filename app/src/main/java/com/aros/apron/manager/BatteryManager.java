package com.aros.apron.manager;


import android.util.Log;

import androidx.annotation.Nullable;

import com.aros.apron.constant.MqttConfig;
import com.aros.apron.entity.BatteryBStateEntity;
import com.aros.apron.entity.BatteryStateAEntity;
import com.aros.apron.entity.FlightStateEntity;
import com.aros.apron.base.BaseManager;
import com.google.gson.Gson;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;

import dji.sdk.keyvalue.key.BatteryKey;
import dji.sdk.keyvalue.key.KeyTools;
import dji.v5.common.callback.CommonCallbacks;
import dji.v5.manager.KeyManager;

/**
 * 电池
 */
public class BatteryManager extends BaseManager {
    MqttAndroidClient client;

    private BatteryManager() {
    }

    private static class BatteryManagerHolder {
        private static final BatteryManager INSTANCE = new BatteryManager();
    }

    public static BatteryManager getInstance() {
        return BatteryManagerHolder.INSTANCE;
    }

    public void initBatteryInfo(MqttAndroidClient client) {
        this.client = client;

        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(BatteryKey.KeyConnection, 0));
        if (isConnect!=null&&isConnect) {
            KeyManager.getInstance().listen(KeyTools.createKey(BatteryKey.
                    KeyConnection, 0), this, new CommonCallbacks.KeyListener<Boolean>() {
                @Override
                public void onValueChange(@Nullable Boolean oldValue, @Nullable Boolean newValue) {
                    BatteryStateAEntity.getInstance().setConnectionState(newValue?0:1);
                    publishBatteryA2Server(BatteryStateAEntity.getInstance());
                }
            });
            KeyManager.getInstance().listen(KeyTools.createKey(BatteryKey.
                    KeyChargeRemainingInPercent, 0), this, new CommonCallbacks.KeyListener<Integer>() {
                @Override
                public void onValueChange(@Nullable Integer oldValue, @Nullable Integer newValue) {
                    BatteryStateAEntity.getInstance().setChargeRemainingInPercent(newValue);
                    publishBatteryA2Server(BatteryStateAEntity.getInstance());
                }
            });
            KeyManager.getInstance().listen(KeyTools.createKey(BatteryKey.
                    KeyBatteryTemperature, 0), this, new CommonCallbacks.KeyListener<Double>() {
                @Override
                public void onValueChange(@Nullable Double oldValue, @Nullable Double newValue) {
                    BatteryStateAEntity.getInstance().setTemperature(newValue + "");
                    publishBatteryA2Server(BatteryStateAEntity.getInstance());
                }
            });
            KeyManager.getInstance().listen(KeyTools.createKey(BatteryKey.
                    KeyVoltage, 0), this, new CommonCallbacks.KeyListener<Integer>() {
                @Override
                public void onValueChange(@Nullable Integer oldValue, @Nullable Integer newValue) {
                    BatteryStateAEntity.getInstance().setVoltage(newValue);
                    publishBatteryA2Server(BatteryStateAEntity.getInstance());
                }
            });
            KeyManager.getInstance().listen(KeyTools.createKey(BatteryKey.
                    KeyNumberOfDischarges, 0), this, new CommonCallbacks.KeyListener<Integer>() {
                @Override
                public void onValueChange(@Nullable Integer oldValue, @Nullable Integer newValue) {
                    BatteryStateAEntity.getInstance().setNumberOfDischarges(newValue);
                    publishBatteryA2Server(BatteryStateAEntity.getInstance());
                }
            });
            KeyManager.getInstance().listen(KeyTools.createKey(BatteryKey.
                    KeyNumberOfCells, 0), this, new CommonCallbacks.KeyListener<Integer>() {
                @Override
                public void onValueChange(@Nullable Integer oldValue, @Nullable Integer newValue) {
                    BatteryStateAEntity.getInstance().setNumberOfCells(newValue);
                    publishBatteryA2Server(BatteryStateAEntity.getInstance());

                }
            });
            KeyManager.getInstance().listen(KeyTools.createKey(BatteryKey.
                    KeyCellVoltages, 0), this, new CommonCallbacks.KeyListener<List<Integer>>() {
                @Override
                public void onValueChange(@Nullable List<Integer> oldValue, @Nullable List<Integer> newValue) {
                    BatteryStateAEntity.getInstance().setCellVoltages(newValue);
                    publishBatteryA2Server(BatteryStateAEntity.getInstance());
                }
            });
        }

        /********************************************************************************************************************/

        Boolean isConnectBatteryB = KeyManager.getInstance().getValue(KeyTools.createKey(BatteryKey.KeyConnection, 1));
        if (isConnectBatteryB!=null&&isConnectBatteryB) {
            KeyManager.getInstance().listen(KeyTools.createKey(BatteryKey.
                    KeyConnection, 1), this, new CommonCallbacks.KeyListener<Boolean>() {
                @Override
                public void onValueChange(@Nullable Boolean oldValue, @Nullable Boolean newValue) {
                    BatteryBStateEntity.getInstance().setConnectionState(newValue?1:0);
                    publishBatteryB2Server(BatteryBStateEntity.getInstance());
                }
            });
            KeyManager.getInstance().listen(KeyTools.createKey(BatteryKey.
                    KeyChargeRemainingInPercent, 1), this, new CommonCallbacks.KeyListener<Integer>() {
                @Override
                public void onValueChange(@Nullable Integer oldValue, @Nullable Integer newValue) {
                    BatteryBStateEntity.getInstance().setChargeRemainingInPercent(newValue);
                    publishBatteryB2Server(BatteryBStateEntity.getInstance());
                }
            });
            KeyManager.getInstance().listen(KeyTools.createKey(BatteryKey.
                    KeyBatteryTemperature, 1), this, new CommonCallbacks.KeyListener<Double>() {
                @Override
                public void onValueChange(@Nullable Double oldValue, @Nullable Double newValue) {
                    BatteryBStateEntity.getInstance().setTemperature(newValue + "");
                    publishBatteryB2Server(BatteryBStateEntity.getInstance());
                }
            });
            KeyManager.getInstance().listen(KeyTools.createKey(BatteryKey.
                    KeyVoltage, 1), this, new CommonCallbacks.KeyListener<Integer>() {
                @Override
                public void onValueChange(@Nullable Integer oldValue, @Nullable Integer newValue) {
                    BatteryBStateEntity.getInstance().setVoltage(newValue);
                    publishBatteryB2Server(BatteryBStateEntity.getInstance());
                }
            });
            KeyManager.getInstance().listen(KeyTools.createKey(BatteryKey.
                    KeyNumberOfDischarges, 1), this, new CommonCallbacks.KeyListener<Integer>() {
                @Override
                public void onValueChange(@Nullable Integer oldValue, @Nullable Integer newValue) {
                    BatteryBStateEntity.getInstance().setNumberOfDischarges(newValue);
                    publishBatteryB2Server(BatteryBStateEntity.getInstance());
                }
            });
            KeyManager.getInstance().listen(KeyTools.createKey(BatteryKey.
                    KeyNumberOfCells, 1), this, new CommonCallbacks.KeyListener<Integer>() {
                @Override
                public void onValueChange(@Nullable Integer oldValue, @Nullable Integer newValue) {
                    BatteryBStateEntity.getInstance().setNumberOfCells(newValue);
                    publishBatteryB2Server(BatteryBStateEntity.getInstance());

                }
            });
            KeyManager.getInstance().listen(KeyTools.createKey(BatteryKey.
                    KeyCellVoltages, 1), this, new CommonCallbacks.KeyListener<List<Integer>>() {
                @Override
                public void onValueChange(@Nullable List<Integer> oldValue, @Nullable List<Integer> newValue) {
                    BatteryBStateEntity.getInstance().setCellVoltages(newValue);
                    publishBatteryB2Server(BatteryBStateEntity.getInstance());
                }
            });
        }
    }

    private void publishBatteryA2Server(BatteryStateAEntity batteryState) {
        if (isFlyClickTime()) {
            MqttMessage flightMessage = null;
            try {
//                Log.e("推送电池A状态",new Gson().toJson(batteryState));
                flightMessage = new MqttMessage(new Gson().toJson(batteryState).getBytes("UTF-8"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            flightMessage.setQos(1);
            publish(client, MqttConfig.MQTT_BATTERY_A_TOPIC, flightMessage);
        }
    }

    private void publishBatteryB2Server(BatteryBStateEntity batteryState) {
        if (isFlyClickTime()) {
            MqttMessage flightMessage = null;
            try {
//                Log.e("推送电池B状态",new Gson().toJson(batteryState));
                flightMessage = new MqttMessage(new Gson().toJson(batteryState).getBytes("UTF-8"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            flightMessage.setQos(1);
            publish(client, MqttConfig.MQTT_BATTERY_B_TOPIC, flightMessage);
        }
    }
}
