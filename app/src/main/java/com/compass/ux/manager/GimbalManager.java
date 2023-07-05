package com.compass.ux.manager;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.apron.mobilesdk.state.ProtoMessage;
import com.compass.ux.base.BaseManager;
import com.compass.ux.constant.Constant;

import org.eclipse.paho.android.service.MqttAndroidClient;

import dji.sdk.keyvalue.key.GimbalKey;
import dji.sdk.keyvalue.key.KeyTools;
import dji.sdk.keyvalue.value.common.EmptyMsg;
import dji.sdk.keyvalue.value.gimbal.GimbalAngleRotation;
import dji.sdk.keyvalue.value.gimbal.GimbalAngleRotationMode;
import dji.sdk.keyvalue.value.gimbal.GimbalMode;
import dji.sdk.keyvalue.value.gimbal.GimbalResetType;
import dji.v5.common.callback.CommonCallbacks;
import dji.v5.common.error.IDJIError;
import dji.v5.manager.KeyManager;

public class GimbalManager extends BaseManager {
    private GimbalManager() {
    }

    private static class GimbalHolder {
        private static final GimbalManager INSTANCE = new GimbalManager();
    }

    public static GimbalManager getInstance() {
        return GimbalHolder.INSTANCE;
    }

    //用角度模式旋转云台
    public void gimbalRotateByAngle(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        String componentIndex = message.getPara().get(Constant.COMPONENT_INDEX);
        if (TextUtils.isEmpty(componentIndex)) {
            sendMsg2Server(mqttAndroidClient, message, "云台下标参数有误");
        } else {
            Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(GimbalKey.
                    KeyConnection, Integer.parseInt(componentIndex)));
            if (isConnect) {
                String gimbalAngleRotationMode = message.getPara().get(Constant.ROTATION_MODE);
                String yaw = message.getPara().get(Constant.YAW);
                String pitch = message.getPara().get(Constant.PITCH);
                GimbalAngleRotation rotation = new GimbalAngleRotation();
                rotation.setMode(GimbalAngleRotationMode.find(TextUtils.isEmpty(gimbalAngleRotationMode) ? 0 : Integer.parseInt(gimbalAngleRotationMode)));
                rotation.setYaw(TextUtils.isEmpty(yaw) ? 0 : Double.valueOf(yaw));
                rotation.setPitch(TextUtils.isEmpty(pitch) ? 0 : Double.valueOf(pitch));
                KeyManager.getInstance().performAction(KeyTools.createKey(GimbalKey.KeyRotateByAngle, Integer.parseInt(componentIndex)), rotation, new CommonCallbacks.CompletionCallbackWithParam<EmptyMsg>() {
                            @Override
                            public void onSuccess(EmptyMsg emptyMsg) {
                                sendMsg2Server(mqttAndroidClient, message);
                            }

                            @Override
                            public void onFailure(@NonNull IDJIError error) {
                                sendMsg2Server(mqttAndroidClient, message, "云台控制失败:" + error.description());
                            }
                        }
                );
            } else {
                sendMsg2Server(mqttAndroidClient, message, "云台未连接");
            }
        }

    }

    //云台重置
    public void gimbalReset(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        String componentIndex = message.getPara().get(Constant.COMPONENT_INDEX);
        if (TextUtils.isEmpty(componentIndex)) {
            sendMsg2Server(mqttAndroidClient, message, "云台下标参数有误");
        } else {
            Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(GimbalKey.
                    KeyConnection, Integer.parseInt(componentIndex)));
            if (isConnect) {
                String resetMode = message.getPara().get(Constant.RESET_MODE);
                if (TextUtils.isEmpty(resetMode)) {
                    sendMsg2Server(mqttAndroidClient, message, "云台重置方式参数有误");
                } else {
                    KeyManager.getInstance().performAction(KeyTools.createKey(GimbalKey.KeyGimbalReset, Integer.parseInt(componentIndex)), GimbalResetType.find(Integer.parseInt(resetMode)), new CommonCallbacks.CompletionCallbackWithParam<EmptyMsg>() {
                                @Override
                                public void onSuccess(EmptyMsg emptyMsg) {
                                    sendMsg2Server(mqttAndroidClient, message);
                                }

                                @Override
                                public void onFailure(@NonNull IDJIError error) {
                                    sendMsg2Server(mqttAndroidClient, message, "云台复位失败:" + error.description());
                                }
                            }
                    );
                }
            } else {
                sendMsg2Server(mqttAndroidClient, message, "云台未连接");
            }
        }
    }

    //设置云台控制的最大速度[1,100]
    public void setGimbalControlMaxSpeed(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        String componentIndex = message.getPara().get(Constant.COMPONENT_INDEX);
        if (TextUtils.isEmpty(componentIndex)) {
            sendMsg2Server(mqttAndroidClient, message, "云台下标参数有误");
        } else {
            Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(GimbalKey.
                    KeyConnection, Integer.parseInt(componentIndex)));
            if (isConnect) {
                String value = message.getPara().get(Constant.VALUE);
                String type = message.getPara().get(Constant.TYPE);
                KeyManager.getInstance().setValue(KeyTools.createKey(type.equals("0") ? GimbalKey.KeyPitchControlMaxSpeed : GimbalKey.KeyYawControlMaxSpeed, Integer.parseInt(componentIndex)), Integer.parseInt(value), new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onSuccess() {
                        sendMsg2Server(mqttAndroidClient, message);
                    }

                    @Override
                    public void onFailure(@NonNull IDJIError error) {
                        sendMsg2Server(mqttAndroidClient, message, "云台偏航速度设置失败:" + error.description());
                    }
                });
            } else {
                sendMsg2Server(mqttAndroidClient, message, "云台未连接");
            }
        }
    }

    //恢复出厂设置
    public void setRestoreFactorySettings(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        String componentIndex = message.getPara().get(Constant.COMPONENT_INDEX);
        if (TextUtils.isEmpty(componentIndex)) {
            sendMsg2Server(mqttAndroidClient, message, "云台下标参数有误");
        } else {
            Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(GimbalKey.
                    KeyConnection, Integer.parseInt(componentIndex)));
            if (isConnect) {
                KeyManager.getInstance().performAction(KeyTools.createKey(GimbalKey.KeyRestoreFactorySettings, Integer.parseInt(componentIndex)), new CommonCallbacks.CompletionCallbackWithParam<EmptyMsg>() {
                    @Override
                    public void onSuccess(EmptyMsg emptyMsg) {
                        sendMsg2Server(mqttAndroidClient, message);
                    }

                    @Override
                    public void onFailure(@NonNull IDJIError error) {
                        sendMsg2Server(mqttAndroidClient, message, "恢复出厂设置失败：" + error.description());
                    }
                });
            } else {
                sendMsg2Server(mqttAndroidClient, message, "云台未连接");
            }
        }
    }

    //启动自动校准
    public void startGimbalCalibrate(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        String componentIndex = message.getPara().get(Constant.COMPONENT_INDEX);
        if (TextUtils.isEmpty(componentIndex)) {
            sendMsg2Server(mqttAndroidClient, message, "云台下标参数有误");
        } else {
            Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(GimbalKey.
                    KeyConnection, Integer.parseInt(componentIndex)));
            if (isConnect) {
                KeyManager.getInstance().performAction(KeyTools.createKey(GimbalKey.KeyGimbalCalibrate, Integer.parseInt(componentIndex)), new CommonCallbacks.CompletionCallbackWithParam<EmptyMsg>() {
                    @Override
                    public void onSuccess(EmptyMsg emptyMsg) {
                        sendMsg2Server(mqttAndroidClient, message);
                    }

                    @Override
                    public void onFailure(@NonNull IDJIError error) {
                        sendMsg2Server(mqttAndroidClient, message, "启动校准失败：" + error.description());
                    }
                });
            } else {
                sendMsg2Server(mqttAndroidClient, message, "云台未连接");
            }
        }
    }

    //设置云台缓启/停，范围：[0,30]，数值越大，控制云台俯仰轴启动/停止转动的缓冲距离越长。
    public void setSmoothingFactor(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        String componentIndex = message.getPara().get(Constant.COMPONENT_INDEX);
        if (TextUtils.isEmpty(componentIndex)) {
            sendMsg2Server(mqttAndroidClient, message, "云台下标参数有误");
        } else {
            Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(GimbalKey.
                    KeyConnection, Integer.parseInt(componentIndex)));
            if (isConnect) {
                String value = message.getPara().get(Constant.VALUE);
                String type = message.getPara().get(Constant.TYPE);
                KeyManager.getInstance().setValue(KeyTools.createKey(type.equals("0") ? GimbalKey.KeyPitchSmoothingFactor : GimbalKey.KeyYawSmoothingFactor, Integer.parseInt(componentIndex)), Integer.parseInt(value), new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onSuccess() {
                        sendMsg2Server(mqttAndroidClient, message);
                    }

                    @Override
                    public void onFailure(@NonNull IDJIError error) {
                        sendMsg2Server(mqttAndroidClient, message, "云台缓启/停设置失败：" + error.description());
                    }
                });
            } else {
                sendMsg2Server(mqttAndroidClient, message, "云台未连接");
            }
        }
    }


    //设置云台限位扩展
    public void setPitchRangeExtensionEnabled(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        String componentIndex = message.getPara().get(Constant.COMPONENT_INDEX);
        if (TextUtils.isEmpty(componentIndex)) {
            sendMsg2Server(mqttAndroidClient, message, "云台下标参数有误");
        } else {
            Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(GimbalKey.
                    KeyConnection, Integer.parseInt(componentIndex)));
            if (isConnect) {
                String type = message.getPara().get(Constant.TYPE);
                if (!TextUtils.isEmpty(type)) {
                    KeyManager.getInstance().setValue(KeyTools.createKey(GimbalKey.KeyPitchRangeExtensionEnabled,
                            Integer.parseInt(componentIndex)), type.equals("1")?true:false, new CommonCallbacks.CompletionCallback() {
                        @Override
                        public void onSuccess() {
                            sendMsg2Server(mqttAndroidClient, message);
                        }
                        @Override
                        public void onFailure(@NonNull IDJIError error) {
                            sendMsg2Server(mqttAndroidClient, message, "设置云台俯仰扩展失败:" + error.description());
                        }
                    });
                } else {
                    sendMsg2Server(mqttAndroidClient, message, "设置云台俯仰扩展参数有误");
                }
            } else {
                sendMsg2Server(mqttAndroidClient, message, "云台未连接");
            }
        }
    }

    //设置云台模式
    public void setGimbalMode(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        String componentIndex = message.getPara().get(Constant.COMPONENT_INDEX);
        if (TextUtils.isEmpty(componentIndex)) {
            sendMsg2Server(mqttAndroidClient, message, "云台下标参数有误");
        } else {
            Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(GimbalKey.
                    KeyConnection, Integer.parseInt(componentIndex)));
            if (isConnect) {
                String type = message.getPara().get(Constant.TYPE);
                if (!TextUtils.isEmpty(type)) {
                    KeyManager.getInstance().setValue(KeyTools.createKey(GimbalKey.KeyGimbalMode,
                            Integer.parseInt(componentIndex)), GimbalMode.find(Integer.parseInt(type)), new CommonCallbacks.CompletionCallback() {
                        @Override
                        public void onSuccess() {
                            sendMsg2Server(mqttAndroidClient, message);
                        }
                        @Override
                        public void onFailure(@NonNull IDJIError error) {
                            sendMsg2Server(mqttAndroidClient, message, "设置云台模式失败:" + error.description());
                        }
                    });
                } else {
                    sendMsg2Server(mqttAndroidClient, message, "设置云台模式参数有误");
                }
            } else {
                sendMsg2Server(mqttAndroidClient, message, "云台未连接");
            }
        }
    }
}
