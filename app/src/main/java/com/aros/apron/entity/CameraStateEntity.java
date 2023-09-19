package com.aros.apron.entity;

public class CameraStateEntity {


    private static class MovementHolder {
        private static final CameraStateEntity INSTANCE = new CameraStateEntity();
    }

    private CameraStateEntity() {
    }

    public static final CameraStateEntity getInstance() {
        return CameraStateEntity.MovementHolder.INSTANCE;
    }

    //获取相机类型。
    private int cameraType;

    //相机模式。
    private int cameraMode;

    //相机拍照状态。
    private int isShootingPhoto;

    //相机录像状态。
    private int isRecording ;

    //相机录像时间。
    private int recordingTime ;

    public int getCameraType() {
        return cameraType;
    }

    public void setCameraType(int cameraType) {
        this.cameraType = cameraType;
    }


    public int getCameraMode() {
        return cameraMode;
    }

    public void setCameraMode(int cameraMode) {
        this.cameraMode = cameraMode;
    }

    public int getIsShootingPhoto() {
        return isShootingPhoto;
    }

    public void setIsShootingPhoto(int isShootingPhoto) {
        this.isShootingPhoto = isShootingPhoto;
    }

    public int getIsRecording() {
        return isRecording;
    }

    public void setIsRecording(int isRecording) {
        this.isRecording = isRecording;
    }

    public int getRecordingTime() {
        return recordingTime;
    }

    public void setRecordingTime(int recordingTime) {
        this.recordingTime = recordingTime;
    }
}
