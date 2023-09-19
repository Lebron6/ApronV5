package com.aros.apron.entity;

public class DeviceStatusEntity {


    private static class MovementHolder {
        private static final DeviceStatusEntity INSTANCE = new DeviceStatusEntity();
    }

    private DeviceStatusEntity() {
    }

    public static final DeviceStatusEntity getInstance() {
        return DeviceStatusEntity.MovementHolder.INSTANCE;
    }

    private String deviceStatusCode;
    private String description;

    public String getDeviceStatusCode() {
        return deviceStatusCode;
    }

    public void setDeviceStatusCode(String deviceStatusCode) {
        this.deviceStatusCode = deviceStatusCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWarningLevel() {
        return warningLevel;
    }

    public void setWarningLevel(int warningLevel) {
        this.warningLevel = warningLevel;
    }

    private int warningLevel;
}
