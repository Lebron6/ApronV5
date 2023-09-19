package com.aros.apron.entity;

import java.util.List;

public class BatteryBStateEntity {
    private static class MovementHolder {
        private static final BatteryBStateEntity INSTANCE = new BatteryBStateEntity();
    }

    private BatteryBStateEntity() {
    }

    public static final BatteryBStateEntity getInstance() {
        return BatteryBStateEntity.MovementHolder.INSTANCE;
    }

    //电芯信息
    private int numberOfCells;

    public int getNumberOfCells() {
        return numberOfCells;
    }

    public void setNumberOfCells(int numberOfCells) {
        this.numberOfCells = numberOfCells;
    }

    //电压(mV)
    private int voltage;

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public int getLifetimeRemaining() {
        return lifetimeRemaining;
    }

    public void setLifetimeRemaining(int lifetimeRemaining) {
        this.lifetimeRemaining = lifetimeRemaining;
    }

    public int getChargeRemainingInPercent() {
        return chargeRemainingInPercent;
    }

    public void setChargeRemainingInPercent(int chargeRemainingInPercent) {
        this.chargeRemainingInPercent = chargeRemainingInPercent;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public int getNumberOfDischarges() {
        return numberOfDischarges;
    }

    public void setNumberOfDischarges(int numberOfDischarges) {
        this.numberOfDischarges = numberOfDischarges;
    }

    public int getConnectionState() {
        return connectionState;
    }

    public void setConnectionState(int connectionState) {
        this.connectionState = connectionState;
    }

    //以百分比形式返回电池的剩余寿命，范围为 [0, 100]。
    private int lifetimeRemaining;

    //返回范围为 [0, 100] 的电池剩余电量百分比。
    private int chargeRemainingInPercent;

    //电池温度
    private String temperature;

    //返回电池在其生命周期内经历的总放电次数。
    private int numberOfDischarges;

    //电池连接状态。0 表示正常，1 表示无效，2 表示通信中的异常。
    private int connectionState;

    //子电池电压
    private List<Integer> cellVoltages;

    public List<Integer> getCellVoltages() {
        return cellVoltages;
    }

    public void setCellVoltages(List<Integer> cellVoltages) {
        this.cellVoltages = cellVoltages;
    }
}
