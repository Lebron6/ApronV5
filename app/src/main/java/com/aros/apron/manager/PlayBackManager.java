package com.aros.apron.manager;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caelus.framework.iot.gateway.server.entity.ProtoMessage;
import com.aros.apron.adapter.FileListAdapter;
import com.aros.apron.base.BaseManager;
import com.orhanobut.logger.Logger;

import org.eclipse.paho.android.service.MqttAndroidClient;
import java.util.List;
import dji.sdk.keyvalue.key.CameraKey;
import dji.sdk.keyvalue.key.KeyTools;
import dji.v5.common.callback.CommonCallbacks;
import dji.v5.common.error.IDJIError;
import dji.v5.manager.KeyManager;
import dji.v5.manager.datacenter.media.MediaFile;
import dji.v5.manager.datacenter.media.MediaFileFilter;
import dji.v5.manager.datacenter.media.MediaFileListState;
import dji.v5.manager.datacenter.media.MediaFileListStateListener;
import dji.v5.manager.datacenter.media.MediaManager;
import dji.v5.manager.datacenter.media.PullMediaFileListParam;
import dji.v5.manager.interfaces.IMediaManager;

public class PlayBackManager extends BaseManager {

    private PlayBackManager() {
    }

    private static class PerceptionHolder {
        private static final PlayBackManager INSTANCE = new PlayBackManager();
    }

    public static PlayBackManager getInstance() {
        return PerceptionHolder.INSTANCE;
    }

    public void loadMediaFileList(Context context, FileListAdapter adapter) {
        Boolean isConnect = KeyManager.getInstance().getValue(KeyTools.createKey(CameraKey.
                KeyConnection,0));
        if (isConnect) {
            IMediaManager iMediaManager= MediaManager.getInstance();
            iMediaManager.addMediaFileListStateListener(mediaFileListStateListener);
            iMediaManager.pullMediaFileListFromCamera(new PullMediaFileListParam(new PullMediaFileListParam.Builder().filter(MediaFileFilter.ALL)), new CommonCallbacks.CompletionCallback() {
                @Override
                public void onSuccess() {
                    adapter.setData(iMediaManager.getMediaFileListData().getData());
                }

                @Override
                public void onFailure(@NonNull IDJIError error) {
                    Logger.e("获取媒体文件列表失败:"+error.description());
                }
            });
        } else {
            Logger.e("设备未连接");
//            sendMsg2Server(mqttAndroidClient, message, "camera is null");
        }
    }



    MediaFileListStateListener mediaFileListStateListener=new MediaFileListStateListener() {
        @Override
        public void onUpdate(MediaFileListState mediaFileListState) {
        }
    };
}
