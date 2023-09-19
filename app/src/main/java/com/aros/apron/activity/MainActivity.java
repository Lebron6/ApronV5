package com.aros.apron.activity;

import static android.os.Environment.getExternalStoragePublicDirectory;
import static dji.v5.utils.common.ZipUtil.ZipFiles;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aros.apron.entity.DeviceHealthEntity;
import com.aros.apron.entity.DeviceStatusEntity;
import com.aros.apron.entity.FlightMission;
import com.aros.apron.entity.MissionPoint;
import com.aros.apron.entity.MissionStateEntity;
import com.aros.apron.manager.AirLinkManager;
import com.aros.apron.manager.CameraManager;
import com.aros.apron.manager.DeviceHealthManager;
import com.aros.apron.manager.DeviceStatusManager;
import com.aros.apron.manager.FlightManager;
import com.aros.apron.manager.GimbalManager;
import com.aros.apron.manager.PerceptionManager;
import com.aros.apron.tools.DomParserKML;
import com.aros.apron.tools.DomParserWPML;
import com.aros.apron.tools.DroneHelper;
import com.aros.apron.tools.ZipUtil;
import com.aros.apron.R;
import com.aros.apron.base.BaseActivity;
import com.aros.apron.constant.Constant;
import com.aros.apron.manager.BatteryManager;
import com.aros.apron.manager.MissionManager;
import com.aros.apron.tools.FileUtil;
//import com.aros.apron.tools.OpenCVHelper;
import com.aros.apron.tools.OpenCVHelper;
import com.aros.apron.view.LongTouchBtn;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.aruco.Aruco;
import org.opencv.aruco.Dictionary;
import org.opencv.core.Mat;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import dji.v5.common.video.interfaces.IVideoDecoder;
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
import dji.v5.manager.diagnostic.DJIDeviceHealthInfo;
import dji.v5.manager.interfaces.ILiveStreamManager;
import dji.v5.utils.common.FileUtils;

public class MainActivity extends BaseActivity {

    private String TAG = "MainActivity";
    private SurfaceView mSurfaceView;
    private Button btn_test;
    private Button btn_enterVirtualStickMode;
    private Button btn_start_take_off;
    private Button btn_chose, btn_creat, btn_start_mission, btn_gohome, btn_landing, btn_start_aruco;
    private LongTouchBtn btnUp, btnDown, btnForward, btnBackward, btnLeft, btnRight;

    private static boolean isAppStarted = false;

    public static boolean isStarted() {
        return isAppStarted;
    }

    FlightMission flightMission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isAppStarted = true;
        initViews();
        needConnect();
        initDJIManager();
        openCVHelper = new OpenCVHelper(this);

        mSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableVideoFrame();
            }
        });

//        initMission();

        MissionStateEntity.getInstance().setWaypointMissionExecuteState(1);
        MissionStateEntity.getInstance().setCurrentWaypointIndex(2);
        MissionStateEntity.getInstance().setWaylineID(1);
        MissionStateEntity.getInstance().setWaylineExecutingInterruptReason("航线中断原因");
        Log.e("航线执行状态", new Gson().toJson(MissionStateEntity.getInstance()));

        DeviceHealthEntity.DeviceHealthInfo djiDeviceHealthInfo=new DeviceHealthEntity.DeviceHealthInfo();
        djiDeviceHealthInfo.setInformationCode("获取设备健康信息码。");
        djiDeviceHealthInfo.setDescription("获取设备健康信息描述。");
        djiDeviceHealthInfo.setTitle("获取设备健康信息标题。");
        djiDeviceHealthInfo.setWarningLevel(1);
        List<DeviceHealthEntity.DeviceHealthInfo> deviceHealthInfos=new ArrayList<>();
        deviceHealthInfos.add(djiDeviceHealthInfo);
        DeviceHealthEntity.getInstance().setDjiDeviceHealthInfos(deviceHealthInfos);
        Log.e("设备健康",new Gson().toJson(DeviceHealthEntity.getInstance()));

        DeviceStatusEntity.getInstance().setDeviceStatusCode("获取设备状态码。");
        DeviceStatusEntity.getInstance().setDescription("返回设备状态描述。");
        DeviceStatusEntity.getInstance().setWarningLevel(1);
        Log.e("设备状态",new Gson().toJson(DeviceStatusEntity.getInstance()));

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
        //避障、感知
        initPerception();
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
        //设备状态
        initDeviceStatus();
        //设备健康
        initDeviceHealth();

    }

    private void initDeviceStatus() {
        DeviceStatusManager.getInstance().initDeviceStatus(mqttAndroidClient);
    }

    private void initDeviceHealth() {
        DeviceHealthManager.getInstance().initDeviceHealth(mqttAndroidClient);

    }


    private void initMission() {
        MissionManager.getInstance().initMissionManager(mqttAndroidClient);

        flightMission = new FlightMission();
        flightMission.setMissionId(2);
        flightMission.setTakeOffSecurityHeight(120f);
        flightMission.setSpeed(15.0);

        MissionPoint missionPoint = new MissionPoint();
        missionPoint.setLat(31.507000);
        missionPoint.setLng(121.115518);
        missionPoint.setSpeed(10.0f);
        missionPoint.setExecuteHeight(130);

        MissionPoint.Action action = new MissionPoint.Action();
        action.setType(2);
        MissionPoint.Action action1 = new MissionPoint.Action();
        action1.setType(8);
        action1.setHoverTime(20);
        List<MissionPoint.Action> actions = new ArrayList<>();
        actions.add(action);
        actions.add(action1);
        missionPoint.setActions(actions);

        MissionPoint missionPoint1 = new MissionPoint();
        missionPoint1.setLat(31.505875);
        missionPoint1.setLng(121.113845);
        missionPoint1.setSpeed(15.0f);
        missionPoint1.setExecuteHeight(150);

        MissionPoint.Action action2 = new MissionPoint.Action();
        action2.setType(3);
        MissionPoint.Action action3 = new MissionPoint.Action();
        action3.setType(5);
        action3.setGimbalYawAngle(-100);
        List<MissionPoint.Action> actions1 = new ArrayList<>();
        actions1.add(action2);
        actions1.add(action3);
        missionPoint1.setActions(actions1);

        MissionPoint missionPoint2 = new MissionPoint();
        missionPoint2.setLat(31.506689);
        missionPoint2.setLng(121.113855);
        missionPoint2.setSpeed(15.0f);
        missionPoint2.setExecuteHeight(100);

        MissionPoint.Action action4 = new MissionPoint.Action();
        action4.setType(4);
        MissionPoint.Action action5 = new MissionPoint.Action();
        action5.setType(7);
        action5.setAircraftHeading(30);
        List<MissionPoint.Action> actions2 = new ArrayList<>();
        actions2.add(action2);
        actions2.add(action3);
        missionPoint2.setActions(actions2);

        List<MissionPoint> missionPoints = new ArrayList<>();
        missionPoints.add(missionPoint);
        missionPoints.add(missionPoint1);
        missionPoints.add(missionPoint2);

        flightMission.setPoints(missionPoints);
    }

    private void initCamera() {
        CameraManager.getInstance().initCameraInfo(mqttAndroidClient);
    }

    private void initPerception() {
        PerceptionManager.getInstance().initPerceptionInfo(mqttAndroidClient);
    }

    private void initRTK() {
//        RTKManager.getInstance().initRTKInfo(mqttAndroidClient);
    }

    private void initGimbal() {
        GimbalManager.getInstance().initGimbalInfo(mqttAndroidClient);
    }

    private void initDiagnosticsInfomation() {
//        DiagnosticsManager.getInstance().initDiagnosticsInfo(mqttAndroidClient);
    }

    private void initBattery() {
        BatteryManager.getInstance().initBatteryInfo(mqttAndroidClient);
    }

    private void initAirLink() {
        AirLinkManager.getInstance().initAirLinkInfo(mqttAndroidClient);
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
        btn_start_take_off = findViewById(R.id.btn_start_take_off);
        btn_enterVirtualStickMode = findViewById(R.id.btn_enterVirtualStickMode);
        btn_chose = findViewById(R.id.btn_chose);
        btn_creat = findViewById(R.id.btn_creat);
        btn_start_mission = findViewById(R.id.btn_start_mission);
        btn_gohome = findViewById(R.id.btn_gohome);
        btn_landing = findViewById(R.id.btn_landing);
        btn_start_aruco = findViewById(R.id.btn_start_aruco);
        btn_start_aruco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCVHelper.startDetectAruco(new DroneHelper());
            }
        });

        btn_landing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyManager.getInstance().performAction(KeyTools.createKey(FlightControllerKey.KeyStartAutoLanding), new CommonCallbacks.CompletionCallbackWithParam<EmptyMsg>() {
                    @Override
                    public void onSuccess(EmptyMsg emptyMsg) {
                        showToast("自动降落调用成功");
                    }

                    @Override
                    public void onFailure(@NonNull IDJIError error) {
                        showToast("自动降落调用失败");
                    }
                });
            }
        });
        btn_gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyManager.getInstance().performAction(KeyTools.createKey(FlightControllerKey.KeyStartGoHome), new CommonCallbacks.CompletionCallbackWithParam<EmptyMsg>() {
                    @Override
                    public void onSuccess(EmptyMsg emptyMsg) {
                        showToast("返航调用成功");
                    }

                    @Override
                    public void onFailure(@NonNull IDJIError error) {
                        showToast("返航调用失败");
                    }
                });
            }
        });

        btn_enterVirtualStickMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intiVirtualStick();
            }
        });
        btn_start_take_off.setOnClickListener(new View.OnClickListener() {
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

        btn_creat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                V5MissionUtil.INSTANCE.creatMission();
                // 生成xml文件
                File file1 = new File(
                        getExternalStoragePublicDirectory("KMZ").getAbsolutePath() + File.separator + "wpmz");
                if (!file1.exists()) {
                    if (file1.mkdirs()) {
                        System.out.println("新建成功");
                    } else {
                        System.out.println("新建失败");
                    }
                }
                DomParserKML domParserKML = new DomParserKML(getExternalStoragePublicDirectory("KMZ").getAbsolutePath() + File.separator + "wpmz",
                        "/template.kml");
                domParserKML.createKml(flightMission);

                DomParserWPML domParserWPML = new DomParserWPML(getExternalStoragePublicDirectory("KMZ").getAbsolutePath() + File.separator + "wpmz",
                        "/waylines.wpml");
                domParserWPML.createWpml(flightMission);

                File kmzFile = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + File.separator + "test.kmz");
                kmzFile.getParentFile().mkdirs();

                try {
                    ZipUtil.zip(getExternalStoragePublicDirectory("KMZ").getAbsolutePath() + "/wpmz", getExternalStoragePublicDirectory("KMZ").getAbsolutePath() + File.separator + "test.kmz");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
        Log.e("获取航线URI", data.getData().getPath());
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
            showToast("相机未连接或未获取到视频源");
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
        IVideoDecoder yuvDecoder = new VideoDecoder(
                MainActivity.this,
                availableVideoChannel.getVideoChannelType(),
                DecoderOutputMode.YUV_MODE,
                mSurfaceView.getHolder(),
                mSurfaceView.getWidth(),
                mSurfaceView.getHeight(), false);

        new VideoDecoder(
                MainActivity.this,
                availableVideoChannel.getVideoChannelType(),
                DecoderOutputMode.SURFACE_MODE,
                mSurfaceView.getHolder(),
                mSurfaceView.getWidth(),
                mSurfaceView.getHeight(), false);

        yuvDecoder.addYuvDataListener((mediaFormat, data, width, height) -> {
            Bitmap bitmap = rawByteArray2RGBABitmap2(data, width, height);
            drawProcessedVideo(bitmap);
        });
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
                    VirtualStickManager.getInstance().getLeftStick().setVerticalPosition(50);
                }
            }, 200);
            btnDown.setOnLongTouchListener(new LongTouchBtn.LongTouchListener() {
                @Override
                public void onLongTouch() {
                    VirtualStickManager.getInstance().getLeftStick().setVerticalPosition(-50);
                }
            }, 200);
            btnForward.setOnLongTouchListener(new LongTouchBtn.LongTouchListener() {
                @Override
                public void onLongTouch() {
                    VirtualStickManager.getInstance().getRightStick().setVerticalPosition(50);
                }
            }, 200);
            btnBackward.setOnLongTouchListener(new LongTouchBtn.LongTouchListener() {
                @Override
                public void onLongTouch() {
                    VirtualStickManager.getInstance().getRightStick().setVerticalPosition(-50);
                }
            }, 200);

            btnLeft.setOnLongTouchListener(new LongTouchBtn.LongTouchListener() {
                @Override
                public void onLongTouch() {
                    VirtualStickManager.getInstance().getRightStick().setHorizontalPosition(-50);
                }
            }, 200);

            btnRight.setOnLongTouchListener(new LongTouchBtn.LongTouchListener() {
                @Override
                public void onLongTouch() {
                    VirtualStickManager.getInstance().getRightStick().setHorizontalPosition(50);
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

    /**************************************************************************************************/
    private Bitmap edgeBitmap;
    private Canvas canvas;
    private OpenCVHelper openCVHelper;
    private Dictionary dictionary;
    //    TextureView modifiedVideoStreamPreview;
    private CascadeClassifier faceDetector;

    public void drawProcessedVideo(Bitmap bitmap) {
        if (bitmap != null) {
            Mat source = new Mat();
            Utils.bitmapToMat(bitmap, source);
            openCVHelper.detectArucoTags(source, dictionary);
//            edgeBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//            Utils.matToBitmap(processed, edgeBitmap);
//            canvas = modifiedVideoStreamPreview.lockCanvas();
//            if (canvas != null) {
//                canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
//                if (BuildConfig.DEBUG) {
//                    if (bitmap != null) {
//                        canvas.drawBitmap(bitmap, 0, 0, null);
//                    } else {
//                        Log.e("edgeBitmap为空", "-----------");
//                    }
//                }
//                modifiedVideoStreamPreview.unlockCanvasAndPost(canvas);
//                canvas.setBitmap(null);
//                canvas = null;
//                bitmap.recycle();
            bitmap = null;
//                if (edgeBitmap!=null){
//                    edgeBitmap.recycle();
//                    edgeBitmap = null;
//                }

//            }
            source.release();
        }
    }

    public Mat processImage(Mat input) {
        Mat output;
//        if (needDetectAruco) {
        output = openCVHelper.detectArucoTags(input, dictionary);
//        } else {
//            output = openCVHelper.defaultImageProcessing(input);
//        }
        return output;
    }


    //    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        dictionary = Aruco.getPredefinedDictionary(Aruco.DICT_6X6_250);
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface.SUCCESS) {
                Log.e("opencvload", "OpenCV loaded successfully");
                try {
                    // load cascade file from application resources
                    InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
                    File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                    File cascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
                    FileOutputStream os = new FileOutputStream(cascadeFile);

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                    is.close();
                    os.close();

                    faceDetector = new CascadeClassifier(cascadeFile.getAbsolutePath());
                    if (faceDetector.empty()) {
                        showToast("Failed to load cascade classifier fo face detection");
                        faceDetector = null;
                    } else {
                        Log.i(TAG, "Loaded cascade classifier from " + cascadeFile.getAbsolutePath());
                    }
                    cascadeDir.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
                }
            } else {
                super.onManagerConnected(status);
            }
        }
    };

    public Bitmap rawByteArray2RGBABitmap2(byte[] data, int width, int height) {
        int frameSize = width * height;
        int[] rgba = new int[frameSize];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int y = (0xff & ((int) data[i * width + j]));
                int u = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 0]));
                int v = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 1]));
                y = y < 16 ? 16 : y;
                int r = Math.round(1.164f * (y - 16) + 1.596f * (v - 128));
                int g = Math.round(1.164f * (y - 16) - 0.813f * (v - 128) - 0.391f * (u - 128));
                int b = Math.round(1.164f * (y - 16) + 2.018f * (u - 128));
                r = r < 0 ? 0 : (r > 255 ? 255 : r);
                g = g < 0 ? 0 : (g > 255 ? 255 : g);
                b = b < 0 ? 0 : (b > 255 ? 255 : b);
                rgba[i * width + j] = 0xff000000 + (b << 16) + (g << 8) + r;
            }
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmp.setPixels(rgba, 0, width, 0, 0, width, height);
        return bmp;
    }

}