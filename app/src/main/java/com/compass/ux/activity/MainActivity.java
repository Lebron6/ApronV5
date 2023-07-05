package com.compass.ux.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.compass.ux.R;
import com.compass.ux.base.BaseActivity;
import com.compass.ux.constant.Constant;
import com.compass.ux.entity.DataCache;
import com.compass.ux.manager.BatteryManager;
import com.compass.ux.manager.FlightManager;
import com.compass.ux.manager.MissionManager;
import com.compass.ux.tools.FileUtil;
import com.compass.ux.tools.ToastUtil;
import com.compass.ux.view.LongTouchBtn;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;
import java.util.Locale;
import dji.sdk.keyvalue.key.DJIKey;
import dji.sdk.keyvalue.key.FlightControllerKey;
import dji.sdk.keyvalue.key.KeyTools;
import dji.sdk.keyvalue.key.ProductKey;
import dji.sdk.keyvalue.value.common.EmptyMsg;
import dji.v5.common.callback.CommonCallbacks;
import dji.v5.common.error.IDJIError;
import dji.v5.common.video.channel.VideoChannelState;
import dji.v5.common.video.channel.VideoChannelType;
import dji.v5.common.video.decoder.DecoderOutputMode;
import dji.v5.common.video.decoder.VideoDecoder;
import dji.v5.common.video.interfaces.IVideoChannel;
import dji.v5.common.video.stream.PhysicalDeviceCategory;
import dji.v5.common.video.stream.StreamSource;
import dji.v5.manager.KeyManager;
import dji.v5.manager.aircraft.virtualstick.VirtualStickManager;
import dji.v5.manager.datacenter.livestream.LiveStreamManager;
import dji.v5.manager.datacenter.livestream.LiveStreamSettings;
import dji.v5.manager.datacenter.livestream.LiveStreamType;
import dji.v5.manager.datacenter.livestream.LiveVideoBitrateMode;
import dji.v5.manager.datacenter.livestream.StreamQuality;
import dji.v5.manager.datacenter.livestream.settings.RtmpSettings;
import dji.v5.manager.datacenter.video.VideoStreamManager;
import dji.v5.manager.interfaces.ILiveStreamManager;
import dji.v5.utils.common.FileUtils;

public class MainActivity extends BaseActivity {

    private String TAG = "MainActivity";
    private SurfaceView mSurfaceView;
    private Button btn_test;
    private Button btn_start_live;
    private Button btn_login;
    private Button btn_chose, btn_start_mission;
    private LongTouchBtn btnUp, btnDown, btnForward, btnBackward, btnLeft, btnRight;

    private static boolean isAppStarted = false;

    public static boolean isStarted() {
        return isAppStarted;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isAppStarted = true;
        initViews();
        needConnect();
        initDJIManager();
        mSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableVideoFrame();
            }
        });
    }

    private void initDJIManager() {
        Boolean isAircraftConnected = KeyManager.getInstance().getValue(DJIKey.create(ProductKey.KeyConnection));
        if (isAircraftConnected == null || !isAircraftConnected) {
            showToast("无人机未连接");
            return;
        }

        //飞控参数、RTK开关、避障等
        initFlightController();
        //遥控器、图传等
        initAirLink();
        //图传链路
        initOcuSyncLink();
        //避障、感知
        initAssistant();
        //电池信息
        initBattery();
        //云台
        initGimbal();
        //相机
        initCamera();
        //RTK
        initRTK();
        //警告信息
        initDiagnosticsInfomation();
        //推流
        initLiveStreamManager();
        //航线任务
        initMission();

    }

    private void initMission() {
//        MissionManager.getInstance().initMissionInfo(mqttAndroidClient);
    }

    private void initCamera() {
//        CameraManager.getInstance().initCameraInfo();
    }

    private void initAssistant() {
//        AssistantManager.getInstance().initAssistant();
    }

    private void initRTK() {
//        RTKManager.getInstance().initRTKInfo(mqttAndroidClient);
    }

    private void initGimbal() {
//        GimbalManager.getInstance().initGimbalInfo(mqttAndroidClient);
    }

    private void initDiagnosticsInfomation() {
//        DiagnosticsManager.getInstance().initDiagnosticsInfo(mqttAndroidClient);
    }

    private void initBattery() {
        BatteryManager.getInstance().initBatteryInfo(mqttAndroidClient);
    }

    private void initAirLink() {
//        AirLinkManager.getInstance().initLinkInfo(mqttAndroidClient);
    }

    private void initOcuSyncLink() {
//        AirLinkManager.getInstance().initOcuSyncLink();
    }

    private void initFlightController() {
        FlightManager.getInstance().initFlightInfo(this, mqttAndroidClient);
    }

    private void initLiveStreamManager() {
//        StreamManager.getInstance().initStreamManager();
    }

    private void initViews() {
        mSurfaceView = findViewById(R.id.surface);
        btn_test = findViewById(R.id.btn_test);
        btnUp = findViewById(R.id.btn_up);
        btnDown = findViewById(R.id.btn_down);
        btnLeft = findViewById(R.id.btn_left);
        btnRight = findViewById(R.id.btn_right);
        btnForward = findViewById(R.id.btn_forward);
        btnBackward = findViewById(R.id.btn_backward);
        btn_login = findViewById(R.id.btn_login);
        btn_start_live = findViewById(R.id.btn_start_live);
        btn_chose = findViewById(R.id.btn_chose);
        btn_start_mission = findViewById(R.id.btn_start_mission);
        btn_start_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intiVirtualStick();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyManager.getInstance().performAction(KeyTools.createKey(FlightControllerKey.KeyStartTakeoff), new CommonCallbacks.CompletionCallbackWithParam<EmptyMsg>() {
                    @Override
                    public void onSuccess(EmptyMsg emptyMsg) {
                        showToast("起飞成功");
                    }

                    @Override
                    public void onFailure(@NonNull IDJIError error) {
                        showToast("起飞调用失败");
                    }
                });
            }
        });
        btn_chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(
                        Intent.createChooser(intent, "Select KMZ File"), 0
                );
            }
        });
        btn_start_mission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(filePath)) {
                    showToast("请选择航线");
                } else {
                    MissionManager.getInstance().startMission(FileUtils.getFileName(filePath, ".kmz"));
                }
            }
        });
    }

    String filePath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.e("获取航线URI" + data.getData().getPath());
        if (data.getData().getPath().contains(".kmz") == false) {
            showToast("Please choose KMZ file");
        } else {
            filePath = FileUtil.getPath(this, data.getData());
            File waypointFile = new File(filePath);
            if (waypointFile.exists()) {
                Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.
                        KeyConnection));
                if (isConnect) {
                    MissionManager.getInstance().pushKMZFileToAircraft(filePath, this);
                }
            } else {
                Logger.e("无文件");
            }
        }
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    private void startLiveWithRtmp() {
        ILiveStreamManager iLiveStreamManager = LiveStreamManager.getInstance();
        LiveStreamSettings.Builder streamSettingBuilder = new LiveStreamSettings.Builder();
        LiveStreamSettings streamSettings = streamSettingBuilder.setLiveStreamType(LiveStreamType.RTMP)
                .setRtmpSettings(new RtmpSettings.Builder().setUrl("").build()).build();
        iLiveStreamManager.setLiveStreamSettings(streamSettings);
        iLiveStreamManager.setVideoChannelType(VideoChannelType.PRIMARY_STREAM_CHANNEL);
        iLiveStreamManager.setLiveStreamQuality(StreamQuality.FULL_HD);
        iLiveStreamManager.setLiveVideoBitrateMode(LiveVideoBitrateMode.AUTO);
        iLiveStreamManager.startStream(new CommonCallbacks.CompletionCallback() {
            @Override
            public void onSuccess() {
                showToast("startStream success");
            }

            @Override
            public void onFailure(@NonNull IDJIError error) {
                showToast("startStream fail:" + error.errorCode());
            }
        });
    }


    private void enableVideoFrame() {
        Boolean isAircraftConnected = KeyManager.getInstance().getValue(DJIKey.create(ProductKey.KeyConnection));
        if (isAircraftConnected == null || !isAircraftConnected) {
            showToast("无人机未连接");
            return;
        }
        //获取可用的码流源
        List<StreamSource> availableStreamSources = VideoStreamManager.getInstance().getAvailableStreamSources();
        if (availableStreamSources == null || availableStreamSources.size() == 0) {
            showToast("相机未连接或为获取到视频源");
            return;
        }
        StreamSource surfaceStreamSource = null;
        for (StreamSource availableStreamSource : availableStreamSources) {
            if (availableStreamSource.getPhysicalDeviceCategory() == PhysicalDeviceCategory.CAMERA &&
                    !availableStreamSource.getPhysicalDeviceType().getDeviceType().toUpperCase(Locale.ROOT).equals("FOV")) {
                surfaceStreamSource = availableStreamSource;
                showToast("获取到视频源");
            }
        }
        if (surfaceStreamSource == null) {
            showToast("未获取到视频源");
            return;
        }
        //获取可用码流通道
        IVideoChannel availableVideoChannel = VideoStreamManager.getInstance()
                .getAvailableVideoChannel(VideoChannelType.PRIMARY_STREAM_CHANNEL);
        if (availableVideoChannel == null) {
            showToast("绑定视频流失败");
            return;
        }
        //如果码流通道已开启,绑定视频源
        if (availableVideoChannel.getVideoChannelStatus() == VideoChannelState.SOCKET_ON ||
                availableVideoChannel.getVideoChannelStatus() == VideoChannelState.ON) {
            bindFpv(availableVideoChannel);
            return;
        }

        //如果码流通道未开启,开启码流通道后绑定视频源
        availableVideoChannel.startChannel(surfaceStreamSource, new CommonCallbacks.CompletionCallback() {
            @Override
            public void onSuccess() {
                showToast("视频源绑定成功");
                bindFpv(availableVideoChannel);
            }

            @Override
            public void onFailure(@NonNull IDJIError error) {
                showToast("视频源绑定失败");
            }
        });
    }

    private void bindFpv(IVideoChannel availableVideoChannel) {
//        IVideoDecoder yuvDecoder = new VideoDecoder(
//                MainActivity.this,
//                availableVideoChannel.getVideoChannelType(),
//                DecoderOutputMode.YUV_MODE,
//                mSurfaceView.getHolder(),
//                mSurfaceView.getWidth(),
//                mSurfaceView.getHeight(), false);

        new VideoDecoder(
                MainActivity.this,
                availableVideoChannel.getVideoChannelType(),
                DecoderOutputMode.SURFACE_MODE,
                mSurfaceView.getHolder(),
                mSurfaceView.getWidth(),
                mSurfaceView.getHeight(), true);


//        yuvDecoder.addYuvDataListener((mediaFormat, data, width, height) -> {
//        });
    }

    private void showToast(String str) {
        Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
    }


    private void intiVirtualStick() {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(FlightControllerKey.KeyConnection));
        if (isConnect) {
            setVirtualStickModeEnabled();
            btnUp.setOnLongTouchListener(new LongTouchBtn.LongTouchListener() {
                @Override
                public void onLongTouch() {
                    VirtualStickManager.getInstance().getLeftStick().setVerticalPosition(200);

                }
            }, 200);
            btnDown.setOnLongTouchListener(new LongTouchBtn.LongTouchListener() {
                @Override
                public void onLongTouch() {
                    VirtualStickManager.getInstance().getLeftStick().setVerticalPosition(-200);
                }
            }, 200);
            btnForward.setOnLongTouchListener(new LongTouchBtn.LongTouchListener() {
                @Override
                public void onLongTouch() {
                    VirtualStickManager.getInstance().getRightStick().setVerticalPosition(200);
                }
            }, 200);
            btnBackward.setOnLongTouchListener(new LongTouchBtn.LongTouchListener() {
                @Override
                public void onLongTouch() {
                    VirtualStickManager.getInstance().getRightStick().setVerticalPosition(-200);
                }
            }, 200);

            btnLeft.setOnLongTouchListener(new LongTouchBtn.LongTouchListener() {
                @Override
                public void onLongTouch() {
                    VirtualStickManager.getInstance().getRightStick().setHorizontalPosition(-200);
                }
            }, 200);

            btnRight.setOnLongTouchListener(new LongTouchBtn.LongTouchListener() {
                @Override
                public void onLongTouch() {
                    VirtualStickManager.getInstance().getRightStick().setHorizontalPosition(200);
                }
            }, 200);
        } else {
            showToast("飞行器未连接");
        }

    }

    //设置虚拟摇杆控制权
    public void setVirtualStickModeEnabled() {
        VirtualStickManager.getInstance().enableVirtualStick(new CommonCallbacks.CompletionCallback() {
            @Override
            public void onSuccess() {
                showToast("获取控制权成功");
            }

            @Override
            public void onFailure(@NonNull IDJIError error) {
                showToast("获取控制权失败:" + error.description());
            }
        });
//        KeyManager.getInstance().setValue(KeyTools.createKey(FlightControllerKey.KeyVirtualStickControlModeEnabled), true, new CommonCallbacks.CompletionCallback() {
//            @Override
//            public void onSuccess() {
//                showToast("获取控制权成功");
//            }
//
//            @Override
//            public void onFailure(@NonNull IDJIError error) {
//                showToast("获取控制权失败:" + error.description());
//            }
//        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String message) {
        switch (message) {
            case Constant.FLAG_CONNECT:
                initDJIManager();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isAppStarted = false;
    }
}