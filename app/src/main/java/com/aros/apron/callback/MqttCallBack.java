package com.aros.apron.callback;

import android.util.Log;

import com.aros.apron.manager.FlightManager;
import com.caelus.framework.iot.gateway.server.entity.ProtoMessage;
import com.aros.apron.constant.Constant;
import com.aros.apron.constant.MqttConfig;
import com.aros.apron.entity.DataCache;
import com.aros.apron.manager.CameraManager;
import com.aros.apron.manager.GimbalManager;
import com.aros.apron.manager.MegaphoneManager;
import com.aros.apron.manager.PerceptionManager;
import com.aros.apron.manager.RTKManager;
import com.aros.apron.manager.StreamManager;
import com.aros.apron.xclog.XcFileLog;
import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;
import com.orhanobut.logger.Logger;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;

public class MqttCallBack implements MqttCallbackExtended {

    private MqttAndroidClient mqttAndroidClient;

    public MqttCallBack(MqttAndroidClient mqttAndroidClient) {
        this.mqttAndroidClient = mqttAndroidClient;
    }

    @Override
    public void connectionLost(Throwable cause) {
        if (cause != null) {
            Logger.e("监听到MQtt断开连接:" + cause.toString());
        }
        XcFileLog.getInstace().i("监听到MQtt断开连接：", "-----");
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {
        ProtoMessage.Message message = null;
        try {
            message = ProtoMessage.Message.parseFrom(mqttMessage.getPayload());
        } catch (InvalidProtocolBufferException e) {
            Logger.e("接收异常:" + e.toString());
            e.printStackTrace();
        }
        try {
            Logger.e("测试监听：" + topic + "：" + new Gson().toJson(new String(mqttMessage.getPayload(), "UTF-8")));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        switch (message.getMethod()) {
            case Constant.LIVE_PATH:
                Logger.e("获取推流地址" + message.getPara().get("desRtmpUrl"));
                DataCache.getInstance().setRtmp_address(message.getPara().get("desRtmpUrl"));
                EventBus.getDefault().post(Constant.FLAG_STREAM_URL);
                break;
            //起飞
            case Constant.START_TAKE_OFF:
                FlightManager.getInstance().startTakeoff(mqttAndroidClient, message);
                break;
            //获取控制权限
            case Constant.ENABLE_VIRTUAL_STICK:
                FlightManager.getInstance().setVirtualStickModeEnabled(mqttAndroidClient, message);
                break;
            //降落
            case Constant.START_LANDING:
                FlightManager.getInstance().startAutoLanding(mqttAndroidClient, message);
                break;
            //取消降落
            case Constant.CANCEL_LANDING:
                FlightManager.getInstance().stopAutoLanding(mqttAndroidClient, message);
                break;
            //取消回家
            case Constant.GO_HOME:
                FlightManager.getInstance().startGoHome(mqttAndroidClient, message);
                break;
            //取消回家
            case Constant.CANCEL_GO_HOME:
                FlightManager.getInstance().stopGoHome(mqttAndroidClient, message);
                break;
            //上升下降
            case Constant.RISE_AND_FALL:
                FlightManager.getInstance().setLeftStickVerticalPosition(mqttAndroidClient, message);
                break;
            //向左向右自转
            case Constant.TURN_LEFT_AND_TURN_RIGHT:
                FlightManager.getInstance().setLeftStickHorizontalPosition(mqttAndroidClient, message);
                break;
            //前进后退
            case Constant.FLY_FORWARD_AND_BACK:
                FlightManager.getInstance().setRightStickVerticalPosition(mqttAndroidClient, message);
                break;
            //往左往右
            case Constant.FLY_LEFT_AND_RIGHT:
                FlightManager.getInstance().setRightStickHorizontalPosition(mqttAndroidClient, message);
                break;
            //设置失控后飞机执行的操作
            case Constant.SET_CONNECT_FAIL_BEHAVIOR:
                FlightManager.getInstance().failsafeAction(mqttAndroidClient, message);
                break;
            //设置返航高度
            case Constant.SET_GO_HOME_HEIGHT:
                FlightManager.getInstance().setGoHomeHeight(mqttAndroidClient, message);
                break;
            //设置限高
            case Constant.SET_MAX_HEIGHT:
                FlightManager.getInstance().setHeightLimit(mqttAndroidClient, message);
                break;
            //设置重心校准
            case Constant.SET_GRAVITY_CENTER_STATE:
//                FlightManager.getInstance().setGravityCenterState(mqttAndroidClient, message);
                break;
            //设置是否启用最大飞行半径限制
            case Constant.SET_MFRL:
                FlightManager.getInstance().setDistanceLimitEnabled(mqttAndroidClient, message);
                break;
            //设置最大飞行半径
            case Constant.SET_DISTANCE_LIMIT:
                FlightManager.getInstance().setDistanceLimit(mqttAndroidClient, message);
                break;
            //开始校准IMU
            case Constant.START_IMU:
//                FlightManager.getInstance().setIMUStart(mqttAndroidClient, message);
                break;
            //设置低电量阈值
            case Constant.SET_LOW_BATTERY:
                FlightManager.getInstance().setLowBatteryWarningThreshold(mqttAndroidClient, message);
                break;
            //设置严重低电量
            case Constant.SET_SERIOUS_LOW_BATTERY:
                FlightManager.getInstance().setSeriousLowBatteryWarningThreshold(mqttAndroidClient, message);
                break;
            //设置智能返航
            case Constant.SET_SMART_GOHOME:
                FlightManager.getInstance().setLowBatteryRTHEnabled(mqttAndroidClient, message);
                break;
            //获取飞行状态
            case Constant.GET_IS_FLYING:
                FlightManager.getInstance().isFlying(mqttAndroidClient, message);
                break;
            //获取返航点位置
            case Constant.GET_GO_HOME_POSITION:
                FlightManager.getInstance().getHomeLocation(mqttAndroidClient, message);
                break;
            //云台根据相对角度控制
            case Constant.GIMBAL_ROTATE_BY_RELATIVE_ANGLE:
                GimbalManager.getInstance().gimbalRotateByAngle(mqttAndroidClient, message);
                break;
            //云台重置
            case Constant.GIMBAL_RESET:
                GimbalManager.getInstance().gimbalReset(mqttAndroidClient, message);
                break;
            //设置云台最大速度
            case Constant.SET_GIMBAL_SPEED:
                GimbalManager.getInstance().setGimbalControlMaxSpeed(mqttAndroidClient, message);
                break;
            //恢复出厂
            case Constant.RESTORE_FACTORY:
                GimbalManager.getInstance().setRestoreFactorySettings(mqttAndroidClient, message);
                break;
            //自动校准
            case Constant.START_CALIBRATION:
                GimbalManager.getInstance().startGimbalCalibrate(mqttAndroidClient, message);
                break;
            //云台俯仰缓启停(0,30)
            case Constant.SET_CONTROLLER_SMOOTHING:
                GimbalManager.getInstance().setSmoothingFactor(mqttAndroidClient, message);
                break;
            //设置云台限位扩展
            case Constant.PITCH_RANGE_EXTENSION:
                GimbalManager.getInstance().setPitchRangeExtensionEnabled(mqttAndroidClient, message);
                break;
            //设置云台模式
            case Constant.SET_GIMBAL_MODE:
                GimbalManager.getInstance().setGimbalMode(mqttAndroidClient, message);
                break;
            //切换广角镜头变焦镜头红外镜头
            case Constant.CHANGE_LENS:
                CameraManager.getInstance().setCameraVideoStreamSource(mqttAndroidClient, message);
                break;
            //切换普通视角FPV视角
//            case Constant.CHANGE_CAMERA_FPV_VISUAL:
//                CameraManager.getInstance().visualAngleType(mqttAndroidClient, message);
//                break;
            //切换相机模式
            case Constant.CHANGE_CAMERA_MODE:
                CameraManager.getInstance().setCameraMode(mqttAndroidClient, message);
                break;
            //开始拍照
            case Constant.CAMERE_START_SHOOT:
                CameraManager.getInstance().startShootPhoto(mqttAndroidClient, message);
                break;
            //结束拍照
            case Constant.CAMERE_STOP_SHOOT:
                CameraManager.getInstance().stopShootPhoto(mqttAndroidClient, message);
                break;
            //开始录像
            case Constant.CAMERE_START_RECORE:
                CameraManager.getInstance().startRecordVideo(mqttAndroidClient, message);
                break;
            //结束录像
            case Constant.CAMERE_STOP_RECORE:
                CameraManager.getInstance().stopRecordVideo(mqttAndroidClient, message);
                break;
            //启用或禁用激光测距
            case Constant.SET_LASER_ENABLE:
//                CameraManager.getInstance().setLaserEnable(mqttAndroidClient, message);
                break;
            //判断是否开启或警用激光测距
            case Constant.GET_LASER_ENABLE:
//                CameraManager.getInstance().getLaserEnable(mqttAndroidClient, message);
                break;
            //设置ISO
            case Constant.SET_ISO:
//                CameraManager.getInstance().setISO(mqttAndroidClient, message);
                break;
            //设置曝光补偿
            case Constant.SET_EXPOSURE_COM:
//                CameraManager.getInstance().setExposureCom(mqttAndroidClient, message);
                break;
            //设置曝光模式
            case Constant.SET_EXPOSURE_MODE:
//                CameraManager.getInstance().setExposureMode(mqttAndroidClient, message);
                break;
            //设置shutter
            case Constant.SET_SHUTTER:
//                CameraManager.getInstance().setShutter(mqttAndroidClient, message);
                break;
            //设置变焦
            case Constant.SET_CAMERA_ZOOM:
                CameraManager.getInstance().setCameraZoomRatios(mqttAndroidClient, message);
                break;
            //设置对焦模式
            case Constant.SET_FOCUS_MODE:
                CameraManager.getInstance().setCameraFocusMode(mqttAndroidClient, message);
                break;
            //设置热像仪变焦
            case Constant.SET_THERMAL_DIGITAL_ZOOM:
                CameraManager.getInstance().setThermalZoomRatios(mqttAndroidClient, message);
                break;
            //曝光锁定
            case Constant.LOCK_EXPOSURE:
//                CameraManager.getInstance().setAELock(mqttAndroidClient, message);
                break;
            //格式化内存卡
            case Constant.FORMAT_SDCARD:
                CameraManager.getInstance().formatStorage(mqttAndroidClient, message);
                break;
            //设置拍照时led自动开关
            case Constant.SET_LED_AUTO_TURN_OFF:
//                CameraManager.getInstance().setBeaconAutoTurnOffEnabled(mqttAndroidClient, message);
                break;
            //设置调色板
            case Constant.SET_THERMAL_PALETTE:
//                CameraManager.getInstance().setThermalPalette(mqttAndroidClient, message);
                break;
            //设置等温线
            case Constant.SET_THERMAL_ISO_THERM_UNIT:
//                CameraManager.getInstance().setThermalIsothermEnabled(mqttAndroidClient, message);
                break;
            //设置水印
            case Constant.SET_WATER_MARK_SETTINGS:
//                CameraManager.getInstance().setWatermarkSettings(mqttAndroidClient, message);
                break;
            //相机重置参数（恢复出厂设置）
            case Constant.CAMERA_RFS:
                CameraManager.getInstance().resetCameraSetting(mqttAndroidClient, message);
                break;
            //设置红外展示模式(红外需要调用两个方法,参考DJI文档)
            case Constant.SET_HY_DISPLAY_MODE:
                CameraManager.getInstance().setThermalDisplayMode(mqttAndroidClient, message);
                CameraManager.getInstance().setThermalPIPPosition(mqttAndroidClient, message);
                break;
            //开始rtmp推流
            case Constant.START_LIVE_WITH_RTMP:
                StreamManager.getInstance().setLiveParametersWithRTMP(mqttAndroidClient, message);
                break;
            //开始国标推流
            case Constant.START_LIVE_WITH_GB28181:
                StreamManager.getInstance().setLiveParametersWithGB28181(mqttAndroidClient, message);
                break;
            //开始RTSP推流
            case Constant.START_LIVE_WITH_RTSP:
                StreamManager.getInstance().setLiveParametersWithRTSP(mqttAndroidClient, message);
                break;
            //结束推流
            case Constant.STOP_LIVE:
                StreamManager.getInstance().stopLive(mqttAndroidClient, message);
                break;
            //重启推流
            case Constant.RESTART_LIVE:
                Logger.e("接收到重推" + "....");
//                StreamManager.getInstance().restartLiveShow(mqttAndroidClient, message);
                break;
            //设置视觉定位
            case Constant.SET_VISION_ASSISTED:
                PerceptionManager.getInstance().setVisionPositioningEnabled(mqttAndroidClient, message);
                break;
            //设置精确着陆
            case Constant.SET_PRECISION_LAND:
                PerceptionManager.getInstance().setPrecisionLanding(mqttAndroidClient, message);
                break;
            //避障总开关类型(V5新增接口)
            case Constant.SET_OBSTACLE_AVOIDANCE_TYPE:
                PerceptionManager.getInstance().setObstacleAvoidanceType(mqttAndroidClient, message);
                break;
            //避障子开关
            case Constant.SET_OBSTACLE_AVOIDANCE_ENABLED:
                PerceptionManager.getInstance().setObstacleAvoidanceEnabled(mqttAndroidClient, message);
                break;
            //避障警告触发距离
            case Constant.SET_OBSTACLE_AVOIDANCE_WARNING_DISTANCE:
                PerceptionManager.getInstance().setObstacleAvoidanceWarningDistance(mqttAndroidClient, message);
                break;
            //避障刹停触发距离
            case Constant.SET_OBSTACLE_AVOIDANCE_BRAKING_DISTANCE:
                PerceptionManager.getInstance().setObstacleAvoidanceBrakingDistance(mqttAndroidClient, message);
                break;
            //rtk开关
            case Constant.SET_RTK:
                RTKManager.getInstance().setRtkEnabled(mqttAndroidClient, message);
                break;
            //rtk状态保持(4.16)
            case Constant.SET_RTK_MAINTAIN_POSITION:
                RTKManager.getInstance().setRTKMaintainAccuracyEnabled(mqttAndroidClient, message);
                break;
            //开始搜索基站
            case Constant.START_SET_BS:
                RTKManager.getInstance().startSearchRTKStation(mqttAndroidClient, message);
                break;
            //结束搜索基站
            case Constant.STOP_SET_BS:
                RTKManager.getInstance().stopSearchRTKStation(mqttAndroidClient, message);
                break;
            //连接基站
            case Constant.CONNECT_BS:
                RTKManager.getInstance().startConnectToRTKStation(mqttAndroidClient, message);
                break;
            //设置喊话器位置
            case Constant.SET_MEGAPHONE_INDEX:
                MegaphoneManager.getInstance().setMegaphoneIndex(mqttAndroidClient, message);
                break;
            //设置喊话器音量
            case Constant.SET_VOLUME:
                MegaphoneManager.getInstance().setVolume(mqttAndroidClient, message);
                break;
            //设置播放模式
            case Constant.SET_PLAY_MODE:
                MegaphoneManager.getInstance().setPlayMode(mqttAndroidClient, message);
                break;
            //设置工作模式
            case Constant.SET_WORK_MODE:
                MegaphoneManager.getInstance().setWorkMode(mqttAndroidClient, message);
                break;
            //开始播放
            case Constant.START_PLAY:
                MegaphoneManager.getInstance().startPlay(mqttAndroidClient, message);
                break;
            //停止播放
            case Constant.STOP_PLAY:
                MegaphoneManager.getInstance().stopPlay(mqttAndroidClient, message);
                break;
                //上传喊话内容
            case Constant.START_PUSHING_FILE_TO_MEGAPHONE:
                MegaphoneManager.getInstance().startPushingFileToMegaphone(mqttAndroidClient, message);
                break;
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        try {
            if (reconnect) {//重新订阅
                Logger.e("重新订阅");
                mqttAndroidClient.subscribe(MqttConfig.MQTT_FLIGHT_CONTROLLER_TOPIC, 1);//订阅主题:飞控            publish(topic,"注册",0);
            }
        } catch (Exception e) {
            Logger.e("重新订阅失败");
        }
    }
}
