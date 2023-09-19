package com.aros.apron.constant;

public class MqttConfig {
    /**
     * 服务器IP地址
     */
    //测试环境
    public static String SOCKET_HOST = "tcp://212.64.64.69:1883";

    /**
     * 账号
     */
    public static String USER_NAME = "admin";

    /**
     * 密码
     */
    public static String USER_PASSWORD = "1qw23er45t";

    /**
     * SN码
     */
    public static String SERIAL_NUMBER = "serial_number";//测试

    /**
     * 注册
     */
    public static String MQTT_REGISTER_TOPIC = "uav/product/" + SERIAL_NUMBER + "/status";

    /**
     * 注册成功订阅推流地址
     */
    public static String MQTT_REGISTER_REPLY_TOPIC = "uav/product/" + SERIAL_NUMBER + "/status_reply";

    /**
     * 订阅飞控消息
     */
    public static String MQTT_FLIGHT_CONTROLLER_TOPIC = "/v1/devices/BH_AQFHBYJD_00001/topo/action";

    /**
     * 飞控是否调用成功的返回结果
     */
    public static String MQTT_FLIGHT_CONTROLLER_REPLY_TOPIC = "uav/product/" + SERIAL_NUMBER + "/controller_reply";

    /**
     * 推送电池A
     */
    public static String MQTT_BATTERY_A_TOPIC = "uav/product/" + SERIAL_NUMBER + "/batteryA";

    /**
     * 推送电池B
     */
    public static String MQTT_BATTERY_B_TOPIC = "uav/product/" + SERIAL_NUMBER + "/batteryB";

    /**
     * 推送相机
     */
    public static String MQTT_CAMERA_TOPIC = "uav/product/" + SERIAL_NUMBER + "/camera";

    /**
     * 推送航线执行状态
     */
    public static String MQTT_MISSION_STATE_TOPIC = "uav/product/" + SERIAL_NUMBER + "/mission_state_info";

    /**
     * 推送报警
     */
    public static String MQTT_DIAGNOSTICS_TOPIC = "uav/product/" + SERIAL_NUMBER + "/diagnostics";

    /**
     * 推送图传信号
     */
    public static String MQTT_DOWNlINK_TOPIC = "uav/product/" + SERIAL_NUMBER + "/downlink_signal_quality";

    /**
     * 推送AirLink
     */
    public static String MQTT_AIRLINK_TOPIC = "uav/product/" + SERIAL_NUMBER + "/airLink";

    /**
     * 设备状态
     */
    public static String MQTT_DEVICE_STATUS = "uav/product/" + SERIAL_NUMBER + "/device_status";

    /**
     * 设备健康
     */
    public static String MQTT_DEVICE_HEALTH = "uav/product/" + SERIAL_NUMBER + "/device_health";

    /**
     * 飞行状态
     */
    public static String MQTT_FLIGHT_STATE_TOPIC = "uav/product/" + SERIAL_NUMBER + "/flight_state";

    /**
     * 云台A
     */
    public static String MQTT_GIMBAL_A_TOPIC = "uav/product/" + SERIAL_NUMBER + "/gimbalA";

    /**
     * 云台B
     */
    public static String MQTT_GIMBAL_B_TOPIC = "uav/product/" + SERIAL_NUMBER + "/gimbalB";

    /**
     * 感知
     */
    public static String MQTT_PERCEPTIONINFO_TOPIC = "uav/product/" + SERIAL_NUMBER + "/perceptionInfo";



}
