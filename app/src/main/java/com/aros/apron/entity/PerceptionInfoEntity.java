package com.aros.apron.entity;

public class PerceptionInfoEntity {


    private static class MovementHolder {
        private static final PerceptionInfoEntity INSTANCE = new PerceptionInfoEntity();
    }

    private PerceptionInfoEntity() {
    }

    public static final PerceptionInfoEntity getInstance() {
        return PerceptionInfoEntity.MovementHolder.INSTANCE;
    }

    //获取感知避障上视传感器是否正在工作。
    private int upwardObstacleAvoidanceWorking;
    //获取感知避障下视传感器是否正在工作。
    private int downwardObstacleAvoidanceWorking;
    //获取感知避障左视传感器是否正在工作。
    private int leftSideObstacleAvoidanceWorking;
    //获取感知避障右视传感器是否正在工作。
    private int rightSideObstacleAvoidanceWorking;
    //获取感知避障前视传感器是否正在工作。
    private int forwardObstacleAvoidanceWorking;
    //获取感知避障后视传感器是否正在工作。
    private int backwardObstacleAvoidanceWorking;
    //获取是否开启感知避障上视开关。
    private int upwardObstacleAvoidanceEnabled;
    //获取是否开启感知避障下视开关。
    private int downwardObstacleAvoidanceEnabled;
    //获取是否开启感知避障水平四向开关。
    private int horizontalObstacleAvoidanceEnabled;
    //获取感知避障上视告警距离，单位：米。
    private Double upwardObstacleAvoidanceWarningDistance;
    //获取感知避障下视告警距离，单位：米。
    private Double downwardObstacleAvoidanceWarningDistance;
    //获取感知避障水平四向告警距离，单位：米。
    private Double horizontalObstacleAvoidanceWarningDistance;
    //获取感知避障上视刹停距离，单位：米。
    private Double upwardObstacleAvoidanceBrakingDistance;
    //获取感知避障下视刹停距离，单位：米。
    private Double downwardObstacleAvoidanceBrakingDistance;
    //获取感知避障水平四向刹停距离，单位：米。
    private Double horizontalObstacleAvoidanceBrakingDistance;
    //获取是否开启视觉定位。
    private int isVisionPositioningEnabled;
    //获取是否开启精准降落。
    private int isPrecisionLandingEnabled;
    //获取是否开启感知避障总开关。0刹停 1绕行 2关闭
    private int obstacleAvoidanceType;

    public int getUpwardObstacleAvoidanceWorking() {
        return upwardObstacleAvoidanceWorking;
    }

    public void setUpwardObstacleAvoidanceWorking(int upwardObstacleAvoidanceWorking) {
        this.upwardObstacleAvoidanceWorking = upwardObstacleAvoidanceWorking;
    }

    public int getDownwardObstacleAvoidanceWorking() {
        return downwardObstacleAvoidanceWorking;
    }

    public void setDownwardObstacleAvoidanceWorking(int downwardObstacleAvoidanceWorking) {
        this.downwardObstacleAvoidanceWorking = downwardObstacleAvoidanceWorking;
    }

    public int getLeftSideObstacleAvoidanceWorking() {
        return leftSideObstacleAvoidanceWorking;
    }

    public void setLeftSideObstacleAvoidanceWorking(int leftSideObstacleAvoidanceWorking) {
        this.leftSideObstacleAvoidanceWorking = leftSideObstacleAvoidanceWorking;
    }

    public int getRightSideObstacleAvoidanceWorking() {
        return rightSideObstacleAvoidanceWorking;
    }

    public void setRightSideObstacleAvoidanceWorking(int rightSideObstacleAvoidanceWorking) {
        this.rightSideObstacleAvoidanceWorking = rightSideObstacleAvoidanceWorking;
    }

    public int getForwardObstacleAvoidanceWorking() {
        return forwardObstacleAvoidanceWorking;
    }

    public void setForwardObstacleAvoidanceWorking(int forwardObstacleAvoidanceWorking) {
        this.forwardObstacleAvoidanceWorking = forwardObstacleAvoidanceWorking;
    }

    public int getBackwardObstacleAvoidanceWorking() {
        return backwardObstacleAvoidanceWorking;
    }

    public void setBackwardObstacleAvoidanceWorking(int backwardObstacleAvoidanceWorking) {
        this.backwardObstacleAvoidanceWorking = backwardObstacleAvoidanceWorking;
    }

    public int getUpwardObstacleAvoidanceEnabled() {
        return upwardObstacleAvoidanceEnabled;
    }

    public void setUpwardObstacleAvoidanceEnabled(int upwardObstacleAvoidanceEnabled) {
        this.upwardObstacleAvoidanceEnabled = upwardObstacleAvoidanceEnabled;
    }

    public int getDownwardObstacleAvoidanceEnabled() {
        return downwardObstacleAvoidanceEnabled;
    }

    public void setDownwardObstacleAvoidanceEnabled(int downwardObstacleAvoidanceEnabled) {
        this.downwardObstacleAvoidanceEnabled = downwardObstacleAvoidanceEnabled;
    }

    public int getHorizontalObstacleAvoidanceEnabled() {
        return horizontalObstacleAvoidanceEnabled;
    }

    public void setHorizontalObstacleAvoidanceEnabled(int horizontalObstacleAvoidanceEnabled) {
        this.horizontalObstacleAvoidanceEnabled = horizontalObstacleAvoidanceEnabled;
    }

    public Double getUpwardObstacleAvoidanceWarningDistance() {
        return upwardObstacleAvoidanceWarningDistance;
    }

    public void setUpwardObstacleAvoidanceWarningDistance(Double upwardObstacleAvoidanceWarningDistance) {
        this.upwardObstacleAvoidanceWarningDistance = upwardObstacleAvoidanceWarningDistance;
    }

    public Double getDownwardObstacleAvoidanceWarningDistance() {
        return downwardObstacleAvoidanceWarningDistance;
    }

    public void setDownwardObstacleAvoidanceWarningDistance(Double downwardObstacleAvoidanceWarningDistance) {
        this.downwardObstacleAvoidanceWarningDistance = downwardObstacleAvoidanceWarningDistance;
    }

    public Double getHorizontalObstacleAvoidanceWarningDistance() {
        return horizontalObstacleAvoidanceWarningDistance;
    }

    public void setHorizontalObstacleAvoidanceWarningDistance(Double horizontalObstacleAvoidanceWarningDistance) {
        this.horizontalObstacleAvoidanceWarningDistance = horizontalObstacleAvoidanceWarningDistance;
    }

    public Double getUpwardObstacleAvoidanceBrakingDistance() {
        return upwardObstacleAvoidanceBrakingDistance;
    }

    public void setUpwardObstacleAvoidanceBrakingDistance(Double upwardObstacleAvoidanceBrakingDistance) {
        this.upwardObstacleAvoidanceBrakingDistance = upwardObstacleAvoidanceBrakingDistance;
    }

    public Double getDownwardObstacleAvoidanceBrakingDistance() {
        return downwardObstacleAvoidanceBrakingDistance;
    }

    public void setDownwardObstacleAvoidanceBrakingDistance(Double downwardObstacleAvoidanceBrakingDistance) {
        this.downwardObstacleAvoidanceBrakingDistance = downwardObstacleAvoidanceBrakingDistance;
    }

    public Double getHorizontalObstacleAvoidanceBrakingDistance() {
        return horizontalObstacleAvoidanceBrakingDistance;
    }

    public void setHorizontalObstacleAvoidanceBrakingDistance(Double horizontalObstacleAvoidanceBrakingDistance) {
        this.horizontalObstacleAvoidanceBrakingDistance = horizontalObstacleAvoidanceBrakingDistance;
    }

    public int getIsVisionPositioningEnabled() {
        return isVisionPositioningEnabled;
    }

    public void setIsVisionPositioningEnabled(int isVisionPositioningEnabled) {
        this.isVisionPositioningEnabled = isVisionPositioningEnabled;
    }

    public int getIsPrecisionLandingEnabled() {
        return isPrecisionLandingEnabled;
    }

    public void setIsPrecisionLandingEnabled(int isPrecisionLandingEnabled) {
        this.isPrecisionLandingEnabled = isPrecisionLandingEnabled;
    }

    public int getObstacleAvoidanceType() {
        return obstacleAvoidanceType;
    }

    public void setObstacleAvoidanceType(int obstacleAvoidanceType) {
        this.obstacleAvoidanceType = obstacleAvoidanceType;
    }
}
