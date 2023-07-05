package com.compass.ux.callback;

import static com.compass.ux.constant.Constant.FLAG_CONNECT;
import static com.compass.ux.constant.Constant.FLAG_DISCONNECT;

import android.util.Log;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import dji.v5.common.error.IDJIError;
import dji.v5.common.register.DJISDKInitEvent;
import dji.v5.manager.SDKManager;
import dji.v5.manager.interfaces.SDKManagerCallback;

public class DJISDKRegistrationCallBack implements SDKManagerCallback {
    private String TAG="DJISDKRegistrationCallBack";
    @Override
    public void onRegisterSuccess() {
        Logger.e(TAG+":onRegisterSuccess");
        if (SDKManager.getInstance().isRegistered()) {
            return;
        }
        SDKManager.getInstance().registerApp();
    }

    @Override
    public void onRegisterFailure(IDJIError error) {
        Log.i("Ronny", "register failure: " + error.description());
    }

    @Override
    public void onProductDisconnect(int productId) {
        Logger.e(TAG+":onProductDisconnect");
        EventBus.getDefault().post(FLAG_DISCONNECT);
    }

    @Override
    public void onProductConnect(int productId) {
        Logger.e(TAG+":onProductConnect");
        EventBus.getDefault().post(FLAG_CONNECT);
    }

    @Override
    public void onProductChanged(int productId) {

    }

    @Override
    public void onInitProcess(DJISDKInitEvent event, int totalProcess) {
        if (event == DJISDKInitEvent.INITIALIZE_COMPLETE) {
            SDKManager.getInstance().registerApp();
        }
    }

    @Override
    public void onDatabaseDownloadProgress(long current, long total) {
    }

}
