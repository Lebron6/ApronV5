package com.aros.apron.entity;

import java.util.Date;
import java.util.List;

public class FlightMission {
    private String id;

    private Integer missionId=1;


    private String title;

    private Double height;

    private Double speed=5.0;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

    private String address;

    private String screenShotPath;

    private Date startTime;

    private Date endTime;

    /**
     * 飞行状态（0：未飞行，1：飞行完成）
     */
    private Integer status;

    /**
     * 0：沿航线防线  1：手动控制  2：依照每个航点设置
     */
    private Integer headingMode;

    /**
     * 0：悬停  1：自动返航  2：原地降落 3：返回航线起始点
     */
    private Integer finishedAction;

    /**
     * 暂停点的索引(从1开始，索引前的点表示已经执行)
     */
    private Integer stopMissionIndex;

    /**
     * 云台仰角[0-90]
     */
    private Integer gimbalPitch;

    /**
     * 暂停点的位置
     */
    private String stopMissionLocation;

    /**
     * 最后一次中断的时间（用来过滤多媒体文件）
     */
    private Date lastInterruptTime;

    private String deviceId;

    /**
     * 本地选择使用
     */
    private boolean selectState;
    /**
     * 失控是否继续执行航线
     */
    private boolean exitOnRCLost;
    /**
     * 失控动作类型
     * 0:悬停", 1:原地降落, 2:自动返航
     */
    private Integer executeRCLostAction;
    /**
     * 安全起飞高度[1.5, 1500] （高度模式：相对起飞点高度）
     * * 注：飞行器起飞后，先爬升至该高度，再根据“飞向首航点模式”的设置飞至首航点。该元素仅在飞行器未起飞时生效。
     */
    private Float takeOffSecurityHeight;

    /**
     * 航点信息
     * @return
     */
    private List<MissionPoint> points;

    public List<MissionPoint> getPoints() {
        return points;
    }

    public void setPoints(List<MissionPoint> points) {
        this.points = points;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getMissionId() {
        return missionId;
    }

    public void setMissionId(Integer missionId) {
        this.missionId = missionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }


    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getScreenShotPath() {
        return screenShotPath;
    }

    public void setScreenShotPath(String screenShotPath) {
        this.screenShotPath = screenShotPath;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getHeadingMode() {
        return headingMode;
    }

    public void setHeadingMode(Integer headingMode) {
        this.headingMode = headingMode;
    }

    public Integer getFinishedAction() {
        return finishedAction;
    }

    public void setFinishedAction(Integer finishedAction) {
        this.finishedAction = finishedAction;
    }

    public Integer getStopMissionIndex() {
        return stopMissionIndex;
    }

    public void setStopMissionIndex(Integer stopMissionIndex) {
        this.stopMissionIndex = stopMissionIndex;
    }

    public Integer getGimbalPitch() {
        return gimbalPitch;
    }

    public void setGimbalPitch(Integer gimbalPitch) {
        this.gimbalPitch = gimbalPitch;
    }

    public String getStopMissionLocation() {
        return stopMissionLocation;
    }

    public void setStopMissionLocation(String stopMissionLocation) {
        this.stopMissionLocation = stopMissionLocation;
    }

    public Date getLastInterruptTime() {
        return lastInterruptTime;
    }

    public void setLastInterruptTime(Date lastInterruptTime) {
        this.lastInterruptTime = lastInterruptTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isSelectState() {
        return selectState;
    }

    public void setSelectState(boolean selectState) {
        this.selectState = selectState;
    }

    public boolean isExitOnRCLost() {
        return exitOnRCLost;
    }

    public void setExitOnRCLost(boolean exitOnRCLost) {
        this.exitOnRCLost = exitOnRCLost;
    }

    public Integer getExecuteRCLostAction() {
        return executeRCLostAction;
    }

    public void setExecuteRCLostAction(Integer executeRCLostAction) {
        this.executeRCLostAction = executeRCLostAction;
    }

    public Float getTakeOffSecurityHeight() {
        return takeOffSecurityHeight;
    }

    public void setTakeOffSecurityHeight(Float takeOffSecurityHeight) {
        this.takeOffSecurityHeight = takeOffSecurityHeight;
    }
}
