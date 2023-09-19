package com.aros.apron.entity;

import java.util.List;

import dji.v5.manager.diagnostic.DJIDeviceHealthInfo;

public class DeviceHealthEntity {


    private static class MovementHolder {
        private static final DeviceHealthEntity INSTANCE = new DeviceHealthEntity();
    }

    private DeviceHealthEntity() {
    }

    public static final DeviceHealthEntity getInstance() {
        return DeviceHealthEntity.MovementHolder.INSTANCE;
    }

    private List<DeviceHealthInfo> djiDeviceHealthInfos;

    public List<DeviceHealthInfo> getDjiDeviceHealthInfos() {
        return djiDeviceHealthInfos;
    }

    public void setDjiDeviceHealthInfos(List<DeviceHealthInfo> djiDeviceHealthInfos) {
        this.djiDeviceHealthInfos = djiDeviceHealthInfos;
    }

    public static class DeviceHealthInfo {
        private String informationCode;
        private String title;
        private String description;
        private int warningLevel;

        public String getInformationCode() {
            return informationCode;
        }

        public void setInformationCode(String informationCode) {
            this.informationCode = informationCode;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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
    }
}
