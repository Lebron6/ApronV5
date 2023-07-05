package com.compass.ux.manager;


import androidx.annotation.Nullable;

import com.apron.mobilesdk.state.ProtoBattery;
import com.apron.mobilesdk.state.ProtoFlightController;
import com.compass.ux.base.BaseManager;

import org.eclipse.paho.android.service.MqttAndroidClient;

import java.util.Arrays;
import java.util.List;

import dji.sdk.keyvalue.key.BatteryKey;
import dji.sdk.keyvalue.key.FlightControllerKey;
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
        if (isConnect) {
            ProtoBattery.Battery.Builder builder = ProtoBattery.Battery.newBuilder();
            KeyManager.getInstance().listen(KeyTools.createKey(BatteryKey.
                    KeyChargeRemainingInPercent, 0), this, new CommonCallbacks.KeyListener<Integer>() {
                @Override
                public void onValueChange(@Nullable Integer oldValue, @Nullable Integer newValue) {
                    builder.setChargeRemainingInPercent(newValue);
                }
            });
            KeyManager.getInstance().listen(KeyTools.createKey(BatteryKey.
                    KeyBatteryTemperature, 0), this, new CommonCallbacks.KeyListener<Double>() {
                @Override
                public void onValueChange(@Nullable Double oldValue, @Nullable Double newValue) {
                    builder.setTemperature(newValue);
                }
            });
            KeyManager.getInstance().listen(KeyTools.createKey(BatteryKey.
                    KeyVoltage, 0), this, new CommonCallbacks.KeyListener<Integer>() {
                @Override
                public void onValueChange(@Nullable Integer oldValue, @Nullable Integer newValue) {
                    builder.setVoltage(newValue);
                }
            });
            KeyManager.getInstance().listen(KeyTools.createKey(BatteryKey.
                    KeyNumberOfDischarges, 0), this, new CommonCallbacks.KeyListener<Integer>() {
                @Override
                public void onValueChange(@Nullable Integer oldValue, @Nullable Integer newValue) {
                    builder.setNumberOfDischarges(newValue);
                }
            });
            KeyManager.getInstance().listen(KeyTools.createKey(BatteryKey.
                    KeyNumberOfCells, 0), this, new CommonCallbacks.KeyListener<Integer>() {
                @Override
                public void onValueChange(@Nullable Integer oldValue, @Nullable Integer newValue) {
                    builder.setNumberOfCells(newValue);
                }
            });
            KeyManager.getInstance().listen(KeyTools.createKey(BatteryKey.
                    KeyCellVoltages, 0), this, new CommonCallbacks.KeyListener<List<Integer>>() {
                @Override
                public void onValueChange(@Nullable List<Integer> oldValue, @Nullable List<Integer> newValue) {
                    builder.setCellVoltage(newValue.toString());
                }
            });
        }
    }
}
