package com.compass.ux.entity;

public class DataCache {

    private static class DataCacheHolder {
        private static final DataCache INSTANCE = new DataCache();
    }

    private DataCache(){}

    public static final DataCache getInstance() {
        return DataCacheHolder.INSTANCE;
    }

    //rtmp地址
    private String rtmp_address;

    private int missionWaypointSize;

    private int targetWaypointIndex;

    private int missionExecuteState;

    //图传信号
    private int downlinkQuality;
    //SN
    private String sn;

    //RTKStatus
    private String rtkStatus;
    //录像状态
    private int recordStatus=0;
    private String recordTime="";

    public int getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(int recordStatus) {
        this.recordStatus = recordStatus;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getRtkStatus() {
        return rtkStatus;
    }

    public void setRtkStatus(String rtkStatus) {
        this.rtkStatus = rtkStatus;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public int getDownlinkQuality() {
        return downlinkQuality;
    }

    public void setDownlinkQuality(int downlinkQuality) {
        this.downlinkQuality = downlinkQuality;
    }

    public int getUplinkQuality() {
        return uplinkQuality;
    }

    public void setUplinkQuality(int uplinkQuality) {
        this.uplinkQuality = uplinkQuality;
    }

    //遥控信号
    private int uplinkQuality;

    public int getTargetWaypointIndex() {
        return targetWaypointIndex;
    }

    public void setTargetWaypointIndex(int targetWaypointIndex) {
        this.targetWaypointIndex = targetWaypointIndex;
    }

    public int getMissionExecuteState() {
        return missionExecuteState;
    }

    public void setMissionExecuteState(int missionExecuteState) {
        this.missionExecuteState = missionExecuteState;
    }

    public int getMissionWaypointSize() {
        return missionWaypointSize;
    }

    public void setMissionWaypointSize(int missionWaypointSize) {
        this.missionWaypointSize = missionWaypointSize;
    }

    public String getRtmp_address() {
        return rtmp_address;
    }

    public void setRtmp_address(String rtmp_address) {
        this.rtmp_address = rtmp_address;
    }

}
