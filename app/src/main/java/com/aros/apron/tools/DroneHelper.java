package com.aros.apron.tools;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import dji.sdk.keyvalue.key.GimbalKey;
import dji.sdk.keyvalue.key.KeyTools;
import dji.sdk.keyvalue.value.common.EmptyMsg;
import dji.sdk.keyvalue.value.gimbal.GimbalAngleRotation;
import dji.sdk.keyvalue.value.gimbal.GimbalAngleRotationMode;
import dji.v5.common.callback.CommonCallbacks;
import dji.v5.common.error.IDJIError;
import dji.v5.manager.KeyManager;
import dji.v5.manager.aircraft.virtualstick.VirtualStickManager;


public class DroneHelper {

    private static final int GIMBAL_FORWARD = 0;
    private static final int GIMBAL_DOWN = 1;
    private int gimbalAction;

    public DroneHelper() {
        gimbalAction = GIMBAL_FORWARD;
    }

    public void enterVirtualStickMode() {
        VirtualStickManager.getInstance().enableVirtualStick(new CommonCallbacks.CompletionCallback() {
            @Override
            public void onSuccess() {
                Log.e("获取虚拟摇杆控制权", "success");
            }

            @Override
            public void onFailure(@NonNull IDJIError error) {
                Log.e("获取虚拟摇杆控制权", "fail:" + error.toString());
            }

        });
    }


    public void setVerticalModeToVelocity() {

    }


    public void setGimbalPitchDegree(float pitchAngleDegree) {

        GimbalAngleRotation rotation = new GimbalAngleRotation();
        rotation.setMode(GimbalAngleRotationMode.ABSOLUTE_ANGLE);
        rotation.setYaw(Double.valueOf(0));
        rotation.setPitch(Double.valueOf(-90.0f));
        KeyManager.getInstance().performAction(KeyTools.createKey(GimbalKey.KeyRotateByAngle, 0), rotation, new CommonCallbacks.CompletionCallbackWithParam<EmptyMsg>() {
                    @Override
                    public void onSuccess(EmptyMsg emptyMsg) {
                        Log.e("云台朝下", "success");
                    }
                    @Override
                    public void onFailure(@NonNull IDJIError error) {
                        Log.e("云台朝下", "fail:" + error.toString());
                    }
                }
        );
    }

}
