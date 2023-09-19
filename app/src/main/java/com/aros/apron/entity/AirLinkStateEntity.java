package com.aros.apron.entity;

import java.util.List;

import dji.sdk.keyvalue.value.airlink.FrequencyInterferenceInfo;

public class AirLinkStateEntity {


    private static class MovementHolder {
        private static final AirLinkStateEntity INSTANCE = new AirLinkStateEntity();
    }

    private AirLinkStateEntity() {
    }

    public static final AirLinkStateEntity getInstance() {
        return AirLinkStateEntity.MovementHolder.INSTANCE;
    }

    //获取图传类型。
    private int airLinkType;

    //获取图传信号质量，单位：百分比。如果获取到的信号质量小于40%，则表示当前信号质量差；如果获取到的信号质量在40%和60%之间，则表示当前信号质量普通；如果获取到的信号质量大于60%，则表示当前的信号质量好。
    private int signalQuality;

    //获取图传码率，单位：Mbps。该码率是无线下行链路（飞行器到遥控器）的图传传输速率。
    private Double dynamicDataRate;


    //获取图传频率干扰信息。
//    private List<FrequencyInterferenceInfo> frequencyInterferences;

    public int getAirLinkType() {
        return airLinkType;
    }

    public void setAirLinkType(int airLinkType) {
        this.airLinkType = airLinkType;
    }

    public int getSignalQuality() {
        return signalQuality;
    }

    public void setSignalQuality(int signalQuality) {
        this.signalQuality = signalQuality;
    }

    public Double getDynamicDataRate() {
        return dynamicDataRate;
    }

    public void setDynamicDataRate(Double dynamicDataRate) {
        this.dynamicDataRate = dynamicDataRate;
    }

//    public List<FrequencyInterferenceInfo> getFrequencyInterferences() {
//        return frequencyInterferences;
//    }
//
//    public void setFrequencyInterferences(List<FrequencyInterferenceInfo> frequencyInterferences) {
//        this.frequencyInterferences = frequencyInterferences;
//    }
}
