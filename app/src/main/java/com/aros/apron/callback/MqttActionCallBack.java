package com.aros.apron.callback;

import android.util.Log;

import com.caelus.framework.iot.gateway.server.entity.ProtoMessage;
import com.aros.apron.constant.MqttConfig;
import com.aros.apron.tools.ToastUtil;
import com.aros.apron.xclog.XcFileLog;
import com.orhanobut.logger.Logger;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MqttActionCallBack implements IMqttActionListener {

    private final String TAG = "MqttActionCallBack";
    private MqttAndroidClient mqttAndroidClient;

    public MqttActionCallBack(MqttAndroidClient mqttAndroidClient) {
        this.mqttAndroidClient = mqttAndroidClient;
    }

    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        ToastUtil.showToast("MQtt连接成功");
        Log.e("MQtt连接成功:" , asyncActionToken.toString());
        XcFileLog.getInstace().i(TAG, "MQtt连接成功：-------");
        try {
            mqttAndroidClient.subscribe(MqttConfig.MQTT_REGISTER_REPLY_TOPIC, 1);//订阅主题:注册
            mqttAndroidClient.subscribe(MqttConfig.MQTT_FLIGHT_CONTROLLER_TOPIC, 1);//订阅主题:飞控            publish(topic,"注册",0);
            publish(MqttConfig.MQTT_REGISTER_TOPIC);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(String topic) throws MqttException {
        if (mqttAndroidClient.isConnected()) {
            ProtoMessage.Message.Builder builder=ProtoMessage.Message.newBuilder();
            builder.setMethod("online").setRequestTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            MqttMessage registerMessage = new MqttMessage(builder.build().toByteArray());
            registerMessage.setQos(1);
            mqttAndroidClient.publish(topic, registerMessage);
        } else {
            XcFileLog.getInstace().e(TAG, "推送失败：MQtt未连接");
        }
    }
    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        Logger.e("MQtt连接失败:" + exception.toString());
        XcFileLog.getInstace().i(TAG, "MQtt连接失败:" + exception.toString());
    }
}
