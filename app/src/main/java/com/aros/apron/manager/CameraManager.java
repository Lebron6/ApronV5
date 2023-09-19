package com.aros.apron.manager;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aros.apron.constant.MqttConfig;
import com.aros.apron.entity.AirLinkStateEntity;
import com.aros.apron.entity.BatteryStateAEntity;
import com.aros.apron.entity.CameraStateEntity;
import com.caelus.framework.iot.gateway.server.entity.ProtoMessage;
import com.aros.apron.base.BaseManager;
import com.aros.apron.constant.Constant;
import com.google.gson.Gson;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import dji.sdk.keyvalue.key.AirLinkKey;
import dji.sdk.keyvalue.key.CameraKey;
import dji.sdk.keyvalue.key.DJIKey;
import dji.sdk.keyvalue.key.GimbalKey;
import dji.sdk.keyvalue.key.KeyTools;
import dji.sdk.keyvalue.msdkkeyinfo.KeyCameraVideoStreamSource;
import dji.sdk.keyvalue.msdkkeyinfo.KeyCameraZoomRatios;
import dji.sdk.keyvalue.value.airlink.AirLinkType;
import dji.sdk.keyvalue.value.camera.CameraFlatMode;
import dji.sdk.keyvalue.value.camera.CameraFocusMode;
import dji.sdk.keyvalue.value.camera.CameraMode;
import dji.sdk.keyvalue.value.camera.CameraShootPhotoMode;
import dji.sdk.keyvalue.value.camera.CameraStorageLocation;
import dji.sdk.keyvalue.value.camera.CameraType;
import dji.sdk.keyvalue.value.camera.CameraVideoStreamSourceType;
import dji.sdk.keyvalue.value.camera.ThermalDisplayMode;
//import dji.sdk.keyvalue.value.camera.ThermalPIPPosition;
import dji.sdk.keyvalue.value.camera.ThermalPIPPosition;
import dji.sdk.keyvalue.value.common.CameraLensType;
import dji.sdk.keyvalue.value.common.ComponentIndexType;
import dji.sdk.keyvalue.value.common.EmptyMsg;
import dji.v5.common.callback.CommonCallbacks;
import dji.v5.common.error.IDJIError;
import dji.v5.manager.KeyManager;

public class CameraManager extends BaseManager {

    private MqttAndroidClient client;

    private CameraManager() {
    }

    private static class CameraHolder {
        private static final CameraManager INSTANCE = new CameraManager();
    }

    public static CameraManager getInstance() {
        return CameraHolder.INSTANCE;
    }

    public void initCameraInfo(MqttAndroidClient client){
        this.client = client;
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(CameraKey.KeyConnection,0));
        if (isConnect!=null&&isConnect) {

            KeyManager.getInstance().listen(KeyTools.createKey(CameraKey.
                    KeyCameraType,0), this, new CommonCallbacks.KeyListener<CameraType>() {
                @Override
                public void onValueChange(@Nullable CameraType oldValue, @Nullable CameraType newValue) {
                    CameraStateEntity.getInstance().setCameraType(newValue.value());                    publishCamera2Server();
                    publishCamera2Server();

                }
            });

            KeyManager.getInstance().listen(KeyTools.createKey(CameraKey.
                    KeyCameraMode,0), this, new CommonCallbacks.KeyListener<CameraMode>() {
                @Override
                public void onValueChange(@Nullable CameraMode oldValue, @Nullable CameraMode newValue) {
                    CameraStateEntity.getInstance().setCameraMode(newValue.value());
                    publishCamera2Server();

                }
            });

            KeyManager.getInstance().listen(KeyTools.createKey(CameraKey.
                    KeyIsShootingPhoto,0), this, new CommonCallbacks.KeyListener<Boolean>() {
                @Override
                public void onValueChange(@Nullable Boolean oldValue, @Nullable Boolean newValue) {
                    CameraStateEntity.getInstance().setIsShootingPhoto(newValue?1:0);                    publishCamera2Server();
                    publishCamera2Server();

                }
            });

            KeyManager.getInstance().listen(KeyTools.createKey(CameraKey.
                    KeyIsRecording,0), this, new CommonCallbacks.KeyListener<Boolean>() {
                @Override
                public void onValueChange(@Nullable Boolean oldValue, @Nullable Boolean newValue) {
                    CameraStateEntity.getInstance().setIsRecording(newValue?1:0);                    publishCamera2Server();
                    publishCamera2Server();

                }
            });

            KeyManager.getInstance().listen(KeyTools.createKey(CameraKey.
                    KeyRecordingTime,0), this, new CommonCallbacks.KeyListener<Integer>() {
                @Override
                public void onValueChange(@Nullable Integer oldValue, @Nullable Integer newValue) {
                    CameraStateEntity.getInstance().setRecordingTime(newValue);
                    publishCamera2Server();
                }
            });
        }
    }

    private void publishCamera2Server() {
        if (isFlyClickTime()) {
            MqttMessage flightMessage = null;
            try {
//                Log.e("推送相机状态",new Gson().toJson(CameraStateEntity.getInstance()));
                flightMessage = new MqttMessage(new Gson().toJson(CameraStateEntity.getInstance()).getBytes("UTF-8"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            flightMessage.setQos(1);
            publish(client, MqttConfig.MQTT_CAMERA_TOPIC, flightMessage);
        }
    }



    //切换广角变焦红外
    public void setCameraVideoStreamSource(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(CameraKey.
                KeyConnection));
        if (isConnect!=null&&isConnect) {
            Boolean aBoolean = KeyManager.getInstance().getValue(DJIKey.create(CameraKey.KeyIsMultiVideoStreamSourceSupported));
            if (aBoolean) {
                String type = message.getPara().get(Constant.TYPE);
                if (TextUtils.isEmpty(type)) {
                    sendMsg2Server(mqttAndroidClient, message, "切换相机视频源参数有误");
                } else {
                    KeyManager.getInstance().setValue(DJIKey.create(CameraKey.KeyCameraVideoStreamSource), CameraVideoStreamSourceType.find(Integer.parseInt(type)), new CommonCallbacks.CompletionCallback() {
                        @Override
                        public void onSuccess() {
                            sendMsg2Server(mqttAndroidClient, message);
                        }
                        @Override
                        public void onFailure(@NonNull IDJIError error) {
                            sendMsg2Server(mqttAndroidClient, message, "切换失败:" + error.description());
                        }
                    });
                }
            } else {
                sendMsg2Server(mqttAndroidClient, message, "切换失败:" + "相机型号不支持");
            }
        } else {
            sendMsg2Server(mqttAndroidClient, message, "相机未连接");
        }
    }

    //切换相机拍照录像模式
    public void setCameraMode(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(CameraKey.
                KeyConnection));
        if (isConnect!=null&&isConnect) {
            String type = message.getPara().get(Constant.TYPE);
            if (TextUtils.isEmpty(type)) {
                sendMsg2Server(mqttAndroidClient, message, "切换相机拍照录像模式参数有误");
            } else {
                KeyManager.getInstance().setValue(DJIKey.create(CameraKey.KeyCameraMode), CameraMode.find(Integer.parseInt(type)), new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onSuccess() {
                        sendMsg2Server(mqttAndroidClient, message);
                    }

                    @Override
                    public void onFailure(@NonNull IDJIError error) {
                        sendMsg2Server(mqttAndroidClient, message, "切换失败:" + error.description());
                    }
                });
            }

        } else {
            sendMsg2Server(mqttAndroidClient, message, "相机未连接");
        }
    }

    //开始拍照
    public void startShootPhoto(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(CameraKey.
                KeyConnection));
        if (isConnect!=null&&isConnect) {
            KeyManager.getInstance().performAction(DJIKey.create(CameraKey.KeyStartShootPhoto), new CommonCallbacks.CompletionCallbackWithParam<EmptyMsg>() {
                @Override
                public void onSuccess(EmptyMsg emptyMsg) {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "拍照失败:" + error.description());

                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "相机未连接");
        }
    }

    //结束拍照
    public void stopShootPhoto(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(CameraKey.
                KeyConnection));
        if (isConnect!=null&&isConnect) {
            KeyManager.getInstance().performAction(DJIKey.create(CameraKey.KeyStopShootPhoto), new CommonCallbacks.CompletionCallbackWithParam<EmptyMsg>() {
                @Override
                public void onSuccess(EmptyMsg emptyMsg) {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "停止拍照失败:" + error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "相机未连接");
        }
    }

    //开始录像
    public void startRecordVideo(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(CameraKey.
                KeyConnection));
        if (isConnect!=null&&isConnect) {
            KeyManager.getInstance().performAction(DJIKey.create(CameraKey.KeyStartRecord), new CommonCallbacks.CompletionCallbackWithParam<EmptyMsg>() {
                @Override
                public void onSuccess(EmptyMsg emptyMsg) {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "开始录像失败:" + error.description());

                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "相机未连接");
        }
    }

    //停止录像
    public void stopRecordVideo(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(CameraKey.
                KeyConnection));
        if (isConnect!=null&&isConnect) {
            KeyManager.getInstance().performAction(DJIKey.create(CameraKey.KeyStopRecord), new CommonCallbacks.CompletionCallbackWithParam<EmptyMsg>() {
                @Override
                public void onSuccess(EmptyMsg emptyMsg) {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "停止录像失败:" + error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "相机未连接");
        }
    }

    //设置变焦倍率
    public void setCameraZoomRatios(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(CameraKey.
                KeyConnection));
        if (isConnect!=null&&isConnect) {
            String type = message.getPara().get(Constant.TYPE);
            if (TextUtils.isEmpty(type)) {
                sendMsg2Server(mqttAndroidClient, message, "变焦倍率数值有误");
            } else {
                KeyManager.getInstance().setValue(KeyTools.createCameraKey(CameraKey.KeyCameraZoomRatios, ComponentIndexType.LEFT_OR_MAIN, CameraLensType.CAMERA_LENS_ZOOM), Double.valueOf(Integer.parseInt(type)), new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onSuccess() {
                        sendMsg2Server(mqttAndroidClient, message);
                    }
                    @Override
                    public void onFailure(@NonNull IDJIError error) {
                        sendMsg2Server(mqttAndroidClient, message, "设置变焦倍率失败:" + error.description());
                    }
                });
            }
        } else {
            sendMsg2Server(mqttAndroidClient, message, "相机未连接");
        }
    }

    //设置红外变焦倍率(支持1x、2x、4x、8x变焦倍率)
    public void setThermalZoomRatios(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(CameraKey.
                KeyConnection));
        if (isConnect!=null&&isConnect) {
            String type = message.getPara().get(Constant.TYPE);
            if (TextUtils.isEmpty(type)) {
                sendMsg2Server(mqttAndroidClient, message, "红外变焦倍率数值有误");
            } else {
                KeyManager.getInstance().setValue(KeyTools.createCameraKey(CameraKey.KeyThermalZoomRatios, ComponentIndexType.LEFT_OR_MAIN, CameraLensType.CAMERA_LENS_THERMAL), Double.valueOf(Integer.parseInt(type)), new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onSuccess() {
                        sendMsg2Server(mqttAndroidClient, message);
                    }
                    @Override
                    public void onFailure(@NonNull IDJIError error) {
                        sendMsg2Server(mqttAndroidClient, message, "设置红外变焦倍率失败:" + error.description());
                    }
                });
            }
        } else {
            sendMsg2Server(mqttAndroidClient, message, "相机未连接");
        }
    }


    //设置对焦模式
    public void setCameraFocusMode(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(CameraKey.
                KeyConnection));
        if (isConnect!=null&&isConnect) {
            String type = message.getPara().get(Constant.TYPE);
            if (TextUtils.isEmpty(type)) {
                sendMsg2Server(mqttAndroidClient, message, "对焦模式设置参数有误");
            } else {
                KeyManager.getInstance().setValue(KeyTools.createCameraKey(CameraKey.KeyCameraFocusMode, ComponentIndexType.LEFT_OR_MAIN, CameraLensType.CAMERA_LENS_ZOOM), CameraFocusMode.find(Integer.valueOf(type)), new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onSuccess() {
                        sendMsg2Server(mqttAndroidClient, message);
                    }
                    @Override
                    public void onFailure(@NonNull IDJIError error) {
                        sendMsg2Server(mqttAndroidClient, message, "设置对焦模式设置失败:" + error.description());
                    }
                });
            }
        } else {
            sendMsg2Server(mqttAndroidClient, message, "相机未连接");
        }
    }

    //格式化SD卡
    public void formatStorage(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(CameraKey.
                KeyConnection));
        if (isConnect!=null&&isConnect) {

                KeyManager.getInstance().setValue(DJIKey.create(CameraKey.KeyFormatStorage), CameraStorageLocation.SDCARD, new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onSuccess() {
                        sendMsg2Server(mqttAndroidClient, message);
                    }
                    @Override
                    public void onFailure(@NonNull IDJIError error) {
                        sendMsg2Server(mqttAndroidClient, message, "格式化失败:" + error.description());
                    }
                });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "相机未连接");
        }
    }

    //重置相机参数
    public void resetCameraSetting(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(CameraKey.
                KeyConnection));
        if (isConnect!=null&&isConnect) {
            KeyManager.getInstance().performAction(DJIKey.create(CameraKey.KeyResetCameraSetting), new CommonCallbacks.CompletionCallbackWithParam<EmptyMsg>() {
                @Override
                public void onSuccess(EmptyMsg emptyMsg) {
                    sendMsg2Server(mqttAndroidClient, message);
                }
                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "重置相机参数失败:"+error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "相机未连接");
        }
    }

    //设置红外镜头的显示模式
    public void setThermalDisplayMode(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(CameraKey.
                KeyConnection));
        if (isConnect!=null&&isConnect) {
                KeyManager.getInstance().setValue(KeyTools.createCameraKey(CameraKey.KeyThermalDisplayMode,ComponentIndexType.LEFT_OR_MAIN,CameraLensType.CAMERA_LENS_THERMAL), ThermalDisplayMode.PIP, new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onSuccess() {
                        sendMsg2Server(mqttAndroidClient, message);
                    }
                    @Override
                    public void onFailure(@NonNull IDJIError error) {
                        sendMsg2Server(mqttAndroidClient, message, "红外镜头的显示模式模式设置失败:" + error.description());
                    }
                });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "相机未连接");
        }
    }

    //设置红外镜头分屏显示位置
    public void setThermalPIPPosition(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(CameraKey.
                KeyConnection));
        if (isConnect!=null&&isConnect) {
                KeyManager.getInstance().setValue(DJIKey.create(CameraKey.KeyThermalPIPPosition), ThermalPIPPosition.SIDE_BY_SIDE, new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onSuccess() {
                        sendMsg2Server(mqttAndroidClient, message);
                    }
                    @Override
                    public void onFailure(@NonNull IDJIError error) {
                        sendMsg2Server(mqttAndroidClient, message, "分屏的显示位置设置失败:" + error.description());
                    }
                });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "相机未连接");
        }
    }
}
