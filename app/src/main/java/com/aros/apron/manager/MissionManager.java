package com.aros.apron.manager;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.aros.apron.adapter.FileListAdapter;
import com.aros.apron.base.BaseManager;
import com.aros.apron.constant.MqttConfig;
import com.aros.apron.entity.CameraStateEntity;
import com.aros.apron.entity.MissionStateEntity;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.shd.wpmz.WaylineMissionConfig;
import com.shd.wpmz.mission.FlyToWaylineMode;
import com.shd.wpmz.mission.WaylineExitOnRCLostAction;
import com.shd.wpmz.mission.WaylineExitOnRCLostBehavior;
import com.shd.wpmz.mission.WaylineFinishedAction;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import dji.sdk.keyvalue.key.CameraKey;
import dji.sdk.keyvalue.key.FlightControllerKey;
import dji.sdk.keyvalue.key.KeyTools;
import dji.v5.common.callback.CommonCallbacks;
import dji.v5.common.error.IDJIError;
import dji.v5.manager.KeyManager;
import dji.v5.manager.aircraft.waypoint3.WaylineExecutingInfoListener;
import dji.v5.manager.aircraft.waypoint3.WaypointMissionExecuteStateListener;
import dji.v5.manager.aircraft.waypoint3.WaypointMissionManager;
import dji.v5.manager.aircraft.waypoint3.model.WaylineExecutingInfo;
import dji.v5.manager.aircraft.waypoint3.model.WaypointMissionExecuteState;
import dji.v5.manager.datacenter.media.MediaFileFilter;
import dji.v5.manager.datacenter.media.MediaFileListState;
import dji.v5.manager.datacenter.media.MediaFileListStateListener;
import dji.v5.manager.datacenter.media.MediaManager;
import dji.v5.manager.datacenter.media.PullMediaFileListParam;
import dji.v5.manager.interfaces.IMediaManager;
import dji.v5.manager.interfaces.IWaypointMissionManager;

public class MissionManager extends BaseManager {
    private MqttAndroidClient client;

    private MissionManager() {
    }

    private static class PerceptionHolder {
        private static final MissionManager INSTANCE = new MissionManager();
    }

    public static MissionManager getInstance() {
        return PerceptionHolder.INSTANCE;
    }


    public void initMissionManager(MqttAndroidClient client) {
        this.client = client;
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.
                KeyConnection));
        if (isConnect) {
            IWaypointMissionManager missionManager = WaypointMissionManager.getInstance();
            missionManager.addWaylineExecutingInfoListener(new WaylineExecutingInfoListener() {
                @Override
                public void onWaylineExecutingInfoUpdate(WaylineExecutingInfo excutingWaylineInfo) {
                    MissionStateEntity.getInstance().setWaylineID(excutingWaylineInfo.getWaylineID());
                    MissionStateEntity.getInstance().setCurrentWaypointIndex(excutingWaylineInfo.getCurrentWaypointIndex());
                    publishMission2Server();
                }

                @Override
                public void onWaylineExecutingInterruptReasonUpdate(IDJIError error) {
                    MissionStateEntity.getInstance().setWaylineExecutingInterruptReason(error.description());
                    publishMission2Server();
                }
            });
            missionManager.addWaypointMissionExecuteStateListener(new WaypointMissionExecuteStateListener() {
                @Override
                public void onMissionStateUpdate(WaypointMissionExecuteState missionState) {
                    MissionStateEntity.getInstance().setWaypointMissionExecuteState(missionState.value());
                    publishMission2Server();
                }
            });
        } else {
            Logger.e("设备未连接");
        }
    }

    private void publishMission2Server() {
        if (isFlyClickTime()) {
            MqttMessage flightMessage = null;
            try {
//                Log.e("推送航线状态",new Gson().toJson(MissionStateEntity.getInstance()));
                flightMessage = new MqttMessage(new Gson().toJson(MissionStateEntity.getInstance()).getBytes("UTF-8"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            flightMessage.setQos(1);
            publish(client, MqttConfig.MQTT_MISSION_STATE_TOPIC, flightMessage);
        }
    }

    public void pushKMZFileToAircraft(String filePath, Context context) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.
                KeyConnection));
        if (isConnect) {
            IWaypointMissionManager missionManager = WaypointMissionManager.getInstance();
            Logger.e("航线上传:" + filePath);
            missionManager.pushKMZFileToAircraft(filePath, new CommonCallbacks.CompletionCallbackWithProgress<Double>() {
                @Override
                public void onProgressUpdate(Double progress) {
                    Logger.e("航线上传进度:" + progress);
                }

                @Override
                public void onSuccess() {
                    Logger.e("航线上传成功");
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    Logger.e("航线上传失败" + error.description());
                }
            });
        } else {
            Logger.e("设备未连接");
        }
    }

    public void startMission(String missionName) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.
                KeyConnection));
        if (isConnect) {
            IWaypointMissionManager missionManager = WaypointMissionManager.getInstance();
            missionManager.startMission(missionName, new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    Logger.e("航线开始成功");
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    Logger.e("航线开始失败:" + error.description() + "---" + error.errorCode());

                }
            });
        } else {
            Logger.e("设备未连接");
//            sendMsg2Server(mqttAndroidClient, message, "camera is null");
        }
    }


}
