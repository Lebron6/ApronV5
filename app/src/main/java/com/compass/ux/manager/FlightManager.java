package com.compass.ux.manager;

import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.apron.mobilesdk.state.ProtoFlightController;
import com.apron.mobilesdk.state.ProtoMessage;
import com.compass.ux.base.BaseManager;
import com.compass.ux.constant.Constant;
import com.compass.ux.constant.MqttConfig;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import dji.sdk.keyvalue.key.FlightControllerKey;
import dji.sdk.keyvalue.key.KeyTools;
import dji.sdk.keyvalue.value.common.Attitude;
import dji.sdk.keyvalue.value.common.EmptyMsg;
import dji.sdk.keyvalue.value.common.LocationCoordinate2D;
import dji.sdk.keyvalue.value.common.LocationCoordinate3D;
import dji.sdk.keyvalue.value.common.Velocity3D;
import dji.sdk.keyvalue.value.flightcontroller.FailsafeAction;
import dji.sdk.keyvalue.value.flightcontroller.FlightMode;
import dji.sdk.keyvalue.value.flightcontroller.GPSSignalLevel;
import dji.sdk.keyvalue.value.flightcontroller.GoHomeState;
import dji.sdk.keyvalue.value.flightcontroller.WindDirection;
import dji.v5.common.callback.CommonCallbacks;
import dji.v5.common.error.IDJIError;
import dji.v5.manager.KeyManager;
import dji.v5.manager.aircraft.virtualstick.VirtualStickManager;

public class FlightManager extends BaseManager {

    private MqttAndroidClient mqttAndroidClient;

    private FlightManager() {
    }

    private static class FlightControlHolder {
        private static final FlightManager INSTANCE = new FlightManager();
    }

    public static FlightManager getInstance() {
        return FlightControlHolder.INSTANCE;
    }

    public void initFlightInfo(Context context, MqttAndroidClient mqttAndroidClient) {
        this.mqttAndroidClient=mqttAndroidClient;
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect) {
            ProtoFlightController.FlightController.Builder builder = ProtoFlightController.FlightController.newBuilder();
            KeyManager.getInstance().listen(KeyTools.createKey(FlightControllerKey.KeyIsFlying), this, new CommonCallbacks.KeyListener<Boolean>() {
                @Override
                public void onValueChange(@Nullable Boolean oldValue, @Nullable Boolean newValue) {
                    builder.setIsFlying(newValue);
                    publishFlightState2Server(builder);

                }
            });

            KeyManager.getInstance().listen(KeyTools.createKey(FlightControllerKey.KeyAreMotorsOn), this, new CommonCallbacks.KeyListener<Boolean>() {
                @Override
                public void onValueChange(@Nullable Boolean oldValue, @Nullable Boolean newValue) {
                    builder.setAreMotorsOn(newValue);
                    publishFlightState2Server(builder);

                }
            });

            KeyManager.getInstance().listen(KeyTools.createKey(FlightControllerKey.KeyAircraftLocation3D), this, new CommonCallbacks.KeyListener<LocationCoordinate3D>() {
                @Override
                public void onValueChange(@Nullable LocationCoordinate3D oldValue, @Nullable LocationCoordinate3D newValue) {
                    builder.setLatitude(newValue.getLatitude());
                    builder.setLongitude(newValue.getLongitude());
                    builder.setAltitude(newValue.getAltitude());
                    publishFlightState2Server(builder);

                }
            });

            KeyManager.getInstance().listen(KeyTools.createKey(FlightControllerKey.KeyTakeoffLocationAltitude), this, new CommonCallbacks.KeyListener<Double>() {
                @Override
                public void onValueChange(@Nullable Double oldValue, @Nullable Double newValue) {
                    builder.setTakeoffLocationAltitude(newValue);
                    publishFlightState2Server(builder);

                }
            });
            KeyManager.getInstance().listen(KeyTools.createKey(FlightControllerKey.KeyAircraftAttitude), this, new CommonCallbacks.KeyListener<Attitude>() {
                @Override
                public void onValueChange(@Nullable Attitude oldValue, @Nullable Attitude newValue) {
                    builder.setYaw(newValue.getYaw());
                    builder.setPitch(newValue.getPitch());
                    builder.setRoll(newValue.getRoll());
                    publishFlightState2Server(builder);

                }
            });

            KeyManager.getInstance().listen(KeyTools.createKey(FlightControllerKey.KeyAircraftVelocity), this, new CommonCallbacks.KeyListener<Velocity3D>() {
                @Override
                public void onValueChange(@Nullable Velocity3D oldValue, @Nullable Velocity3D newValue) {
                    builder.setVelocityX(newValue.getX());
                    builder.setVelocityY(newValue.getY());
                    builder.setVelocityZ(newValue.getZ());
                    publishFlightState2Server(builder);

                }
            });

            KeyManager.getInstance().listen(KeyTools.createKey(FlightControllerKey.KeyGPSSatelliteCount), this, new CommonCallbacks.KeyListener<Integer>() {
                @Override
                public void onValueChange(@Nullable Integer oldValue, @Nullable Integer newValue) {
                    builder.setSatelliteCount(newValue);
                    publishFlightState2Server(builder);

                }
            });

            KeyManager.getInstance().listen(KeyTools.createKey(FlightControllerKey.KeyHomeLocation), this, new CommonCallbacks.KeyListener<LocationCoordinate2D>() {
                @Override
                public void onValueChange(@Nullable LocationCoordinate2D oldValue, @Nullable LocationCoordinate2D newValue) {
                    builder.setHomeLocationLatitude(newValue.getLatitude());
                    builder.setHomeLocationLongitude(newValue.getLongitude());
                    publishFlightState2Server(builder);

                }
            });

            KeyManager.getInstance().listen(KeyTools.createKey(FlightControllerKey.KeyGoHomeHeight), this, new CommonCallbacks.KeyListener<Integer>() {
                @Override
                public void onValueChange(@Nullable Integer oldValue, @Nullable Integer newValue) {
                    builder.setGoHomeHeight(newValue);
                    publishFlightState2Server(builder);

                }
            });

            KeyManager.getInstance().listen(KeyTools.createKey(FlightControllerKey.KeyWindSpeed), this, new CommonCallbacks.KeyListener<Integer>() {
                @Override
                public void onValueChange(@Nullable Integer oldValue, @Nullable Integer newValue) {
                    builder.setWindSpeed(newValue);
                    publishFlightState2Server(builder);

                }
            });

            KeyManager.getInstance().listen(KeyTools.createKey(FlightControllerKey.KeyCompassHeading), this, new CommonCallbacks.KeyListener<Double>() {
                @Override
                public void onValueChange(@Nullable Double oldValue, @Nullable Double newValue) {
                    builder.setCompassHeading(newValue);
                    publishFlightState2Server(builder);

                }
            });

            KeyManager.getInstance().listen(KeyTools.createKey(FlightControllerKey.KeyFlightMode), this, new CommonCallbacks.KeyListener<FlightMode>() {
                @Override
                public void onValueChange(@Nullable FlightMode oldValue, @Nullable FlightMode newValue) {
                    builder.setFlightModeString(ProtoFlightController.FlightController.FlightModeString.values()[newValue.ordinal()]);
                    publishFlightState2Server(builder);
                }
            });

            KeyManager.getInstance().listen(KeyTools.createKey(FlightControllerKey.KeyGPSSignalLevel), this, new CommonCallbacks.KeyListener<GPSSignalLevel>() {
                @Override
                public void onValueChange(@Nullable GPSSignalLevel oldValue, @Nullable GPSSignalLevel newValue) {
                    builder.setGPSsignalLevel(ProtoFlightController.FlightController.GPSSignalLevel.values()[newValue.ordinal()]);
                    publishFlightState2Server(builder);
                }
            });

            KeyManager.getInstance().listen(KeyTools.createKey(FlightControllerKey.KeyWindDirection), this, new CommonCallbacks.KeyListener<WindDirection>() {
                @Override
                public void onValueChange(@Nullable WindDirection oldValue, @Nullable WindDirection newValue) {
                    builder.setWindDirection(ProtoFlightController.FlightController.WindDirection.values()[newValue.ordinal()]);
                    publishFlightState2Server(builder);
                }
            });

            KeyManager.getInstance().listen(KeyTools.createKey(FlightControllerKey.KeyGoHomeStatus), this, new CommonCallbacks.KeyListener<GoHomeState>() {
                @Override
                public void onValueChange(@Nullable GoHomeState oldValue, @Nullable GoHomeState newValue) {
                    builder.setGoHomeExecutionState(ProtoFlightController.FlightController.GoHomeExecutionState.values()[newValue.ordinal()]);
                    publishFlightState2Server(builder);
                }
            });
        }
    }

    private void publishFlightState2Server(ProtoFlightController.FlightController.Builder builder){
        if (isFlyClickTime()){
            //推送飞行状态
            MqttMessage flightMessage = new MqttMessage(builder.build().toByteArray());
            flightMessage.setQos(1);
            publish(mqttAndroidClient, MqttConfig.MQTT_FLIGHT_STATE_TOPIC, flightMessage);
        }
    }

    //起飞
    public void startTakeoff(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect) {
            KeyManager.getInstance().performAction(KeyTools.createKey(FlightControllerKey.KeyStartTakeoff), new CommonCallbacks.CompletionCallbackWithParam<EmptyMsg>() {
                @Override
                public void onSuccess(EmptyMsg emptyMsg) {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "起飞失败:" + error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //设置虚拟摇杆控制权
    public void setVirtualStickModeEnabled(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect) {
            String enabled = message.getPara().get(Constant.TYPE);
            if (TextUtils.isEmpty(enabled)) {
                sendMsg2Server(mqttAndroidClient, message, "参数有误");
            } else {
                KeyManager.getInstance().setValue(KeyTools.createKey(FlightControllerKey.KeyVirtualStickControlModeEnabled), Integer.parseInt(enabled) == 1 ? true : false, new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onSuccess() {
                        sendMsg2Server(mqttAndroidClient, message);
                    }

                    @Override
                    public void onFailure(@NonNull IDJIError error) {
                        sendMsg2Server(mqttAndroidClient, message, "控制权设置失败:" + error.description());
                    }
                });
            }
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //降落
    public void startAutoLanding(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect) {
            KeyManager.getInstance().performAction(KeyTools.createKey(FlightControllerKey.KeyStartAutoLanding), new CommonCallbacks.CompletionCallbackWithParam<EmptyMsg>() {
                @Override
                public void onSuccess(EmptyMsg emptyMsg) {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "降落失败:" + error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //取消降落
    public void stopAutoLanding(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect) {
            KeyManager.getInstance().performAction(KeyTools.createKey(FlightControllerKey.KeyStopAutoLanding), new CommonCallbacks.CompletionCallbackWithParam<EmptyMsg>() {
                @Override
                public void onSuccess(EmptyMsg emptyMsg) {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "取消降落失败:" + error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //飞行器旋转
    public void setLeftStickHorizontalPosition(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect) {
            VirtualStickManager.getInstance().getLeftStick().setHorizontalPosition(60);
            sendMsg2Server(mqttAndroidClient, message, "移动...");
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //飞行器左摇杆
    public void setLeftStickVerticalPosition(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect) {
            VirtualStickManager.getInstance().getLeftStick().setVerticalPosition(60);
            sendMsg2Server(mqttAndroidClient, message, "移动...");
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //飞行器旋转
    public void setRightStickHorizontalPosition(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect) {
            VirtualStickManager.getInstance().getRightStick().setHorizontalPosition(60);
            sendMsg2Server(mqttAndroidClient, message, "移动...");
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //飞行器右摇杆
    public void setRightStickVerticalPosition(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect) {
            VirtualStickManager.getInstance().getRightStick().setVerticalPosition(60);
            sendMsg2Server(mqttAndroidClient, message, "移动...");
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //返航
    public void startGoHome(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect) {
            KeyManager.getInstance().performAction(KeyTools.createKey(FlightControllerKey.KeyStartGoHome), new CommonCallbacks.CompletionCallbackWithParam<EmptyMsg>() {
                @Override
                public void onSuccess(EmptyMsg emptyMsg) {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "返航执行失败:" + error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //取消返航
    public void stopGoHome(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect) {
            KeyManager.getInstance().performAction(KeyTools.createKey(FlightControllerKey.KeyStopGoHome), new CommonCallbacks.CompletionCallbackWithParam<EmptyMsg>() {
                @Override
                public void onSuccess(EmptyMsg emptyMsg) {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "取消返航执行失败:" + error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //飞机失联后的自动操作
    public void failsafeAction(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect) {
            String type = message.getPara().get(Constant.TYPE);
            KeyManager.getInstance().setValue(KeyTools.createKey(FlightControllerKey.KeyFailsafeAction), FailsafeAction.find(Integer.parseInt(type)), new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "失控执行动作更新失败:" + error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //设置返航高度
    public void setGoHomeHeight(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect) {
            String type = message.getPara().get(Constant.TYPE);
            KeyManager.getInstance().setValue(KeyTools.createKey(FlightControllerKey.KeyGoHomeHeight), Integer.parseInt(type), new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "返航高度更新失败:" + error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //设置限高
    public void setHeightLimit(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect) {
            String type = message.getPara().get(Constant.TYPE);
            KeyManager.getInstance().setValue(KeyTools.createKey(FlightControllerKey.KeyHeightLimit), Integer.parseInt(type), new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "限高更新失败:" + error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //设置是否启用限远
    public void setDistanceLimitEnabled(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.
                KeyConnection));
        if (isConnect) {
            String type = message.getPara().get(Constant.TYPE);
            KeyManager.getInstance().setValue(KeyTools.createKey(FlightControllerKey.KeyDistanceLimitEnabled), type.equals("1") ? true : false, new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "限远开关设置失败:" + error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //设置限远
    public void setDistanceLimit(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.
                KeyConnection));
        if (isConnect) {
            String value = message.getPara().get(Constant.VALUE);
            if (TextUtils.isEmpty(value)) {
                sendMsg2Server(mqttAndroidClient, message, "限远参数设置有误");
            } else {
                KeyManager.getInstance().setValue(KeyTools.createKey(FlightControllerKey.KeyDistanceLimit), Integer.parseInt(value), new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onSuccess() {
                        sendMsg2Server(mqttAndroidClient, message);
                    }

                    @Override
                    public void onFailure(@NonNull IDJIError error) {
                        sendMsg2Server(mqttAndroidClient, message, "限远设置失败:" + error.description());
                    }
                });
            }
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //设置低电量阈值【15-50】
    public void setLowBatteryWarningThreshold(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.
                KeyConnection));
        if (isConnect) {
            String type = message.getPara().get(Constant.TYPE);
            KeyManager.getInstance().setValue(KeyTools.createKey(FlightControllerKey.KeyLowBatteryWarningThreshold), Integer.parseInt(type), new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "低电量阈值设置失败:" + error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //设置严重低电量阈值(该值默认为10%，Matrice 30 Series不可设置。)
    public void setSeriousLowBatteryWarningThreshold(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.
                KeyConnection));
        if (isConnect) {
            String type = message.getPara().get(Constant.TYPE);
            KeyManager.getInstance().setValue(KeyTools.createKey(FlightControllerKey.KeySeriousLowBatteryWarningThreshold), Integer.parseInt(type), new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "严重低电量阈值设置失败:" + error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //设置只能低电量返航
    public void setLowBatteryRTHEnabled(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.
                KeyConnection));
        if (isConnect) {
            String type = message.getPara().get(Constant.TYPE);
            KeyManager.getInstance().setValue(KeyTools.createKey(FlightControllerKey.KeyLowBatteryRTHEnabled), type.equals("1") ? true : false, new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    sendMsg2Server(mqttAndroidClient, message);
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    sendMsg2Server(mqttAndroidClient, message, "智能低电量返航更新失败:" + error.description());
                }
            });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //获取飞行状态
    public void isFlying(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.
                KeyConnection));
        if (isConnect) {
            KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyIsFlying),
                    new CommonCallbacks.CompletionCallbackWithParam<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            sendMsg2Server(mqttAndroidClient, message, true, aBoolean ? "1" : "0");
                        }

                        @Override
                        public void onFailure(@NonNull IDJIError error) {
                            sendMsg2Server(mqttAndroidClient, message, "获取飞行状态失败:" + error.description());

                        }
                    });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

    //获取飞行状态
    public void getHomeLocation(MqttAndroidClient mqttAndroidClient, ProtoMessage.Message message) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.
                KeyConnection));
        if (isConnect) {
            KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyHomeLocation),
                    new CommonCallbacks.CompletionCallbackWithParam<LocationCoordinate2D>() {
                        @Override
                        public void onSuccess(LocationCoordinate2D locationCoordinate2D) {
                            sendMsg2Server(mqttAndroidClient, message, locationCoordinate2D.getLatitude() + "," + locationCoordinate2D.getLongitude());
                        }

                        @Override
                        public void onFailure(@NonNull IDJIError error) {
                            sendMsg2Server(mqttAndroidClient, message, "获取返航点位置失败:" + error.description());
                        }
                    });
        } else {
            sendMsg2Server(mqttAndroidClient, message, "flightController is null");
        }
    }

}
