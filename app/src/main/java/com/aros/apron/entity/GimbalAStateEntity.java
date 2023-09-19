package com.aros.apron.entity;

public class GimbalAStateEntity {

    private static class MovementHolder {
        private static final GimbalAStateEntity INSTANCE = new GimbalAStateEntity();
    }

    private GimbalAStateEntity() {
    }

    public static final GimbalAStateEntity getInstance() {
        return GimbalAStateEntity.MovementHolder.INSTANCE;
    }

    //云台相对于飞机的姿态，以度数表示。(平台上表示云台角度)
   private Double pitch;//俯仰
    private Double roll;//横滚
    private  Double yaw;//偏航

    public Double getPitch() {
        return pitch;
    }

    public void setPitch(Double pitch) {
        this.pitch = pitch;
    }

    public Double getRoll() {
        return roll;
    }

    public void setRoll(Double roll) {
        this.roll = roll;
    }

    public Double getYaw() {
        return yaw;
    }

    public void setYaw(Double yaw) {
        this.yaw = yaw;
    }

    public int getGimbalMode() {
        return gimbalMode;
    }

    public void setGimbalMode(int gimbalMode) {
        this.gimbalMode = gimbalMode;
    }

    //云台的工作模式
    private int gimbalMode ;
}
