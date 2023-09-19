package com.aros.apron.entity;

public class FlightStateEntity {

    private static class MovementHolder {
        private static final FlightStateEntity INSTANCE = new FlightStateEntity();
    }

    private FlightStateEntity() {
    }

    public static final FlightStateEntity getInstance() {
        return MovementHolder.INSTANCE;
    }

    //电机是否启动
    private int areMotorsOn;

    //是否处于飞行状态
    private int isFlying;

    //飞行器当前经纬度(WGS84精准坐标系,平台可以转换为GCJ02坐标系使用)
    private String latitude;
    private String longitude;

    //飞行器当前高度
    private String altitude;

    //飞机原地位置相对于海平面的相对高度，以米为单位。
    private String takeoffLocationAltitude;

    //俯仰、横滚和偏航值将在 [-180, 180] 度范围内的飞机姿态。
    private String pitch;
    private String roll;
    private String yaw;

    //飞机在 x，y，z，方向上的当前速度，以米/秒为单位，使用 NED（North-East-Down）坐标系。
    //平台取(velocityX²+velocityY²)开根值作为水平速度
    private String velocityX;
    private String velocityY;

    //平台取此值做为垂直速度
    private String velocityZ;

    //返回 GPS 卫星计数。
    private int satelliteCount;

    //返航点经纬度
    private String homeLocationLatitude;
    private String homeLocationLongitude;

    //返航高度
    private String goHomeHeight;

    //风速(分米)
    private String windSpeed;

    //表示航向，以度为单位。正北为 0 度，正航向为北东，负航向为北西。范围为 [-180, 180]。
    private String compassHeading;


    //用于测量信号质量
    private int GPSsignalLevel;

    //风速
    private String flightWindWarning;

    //风向
    private int windDirection;

    //自飞行器电机起转以来累计的飞行时间，单位：0.1秒。该数值在电池重新上电后才会清零。
    private String flightTimeInSeconds;

    //总体飞行时长，单位：秒。飞行器断电后不会清零。
    private String aircraftTotalFlightDuration ;

    //总体飞行距离，单位：米。飞行器断电后不会清零。
    private String aircraftTotalFlightDistance  ;

    public String getFlightTimeInSeconds() {
        return flightTimeInSeconds;
    }

    public void setFlightTimeInSeconds(String flightTimeInSeconds) {
        this.flightTimeInSeconds = flightTimeInSeconds;
    }

    public String getAircraftTotalFlightDuration() {
        return aircraftTotalFlightDuration;
    }

    public void setAircraftTotalFlightDuration(String aircraftTotalFlightDuration) {
        this.aircraftTotalFlightDuration = aircraftTotalFlightDuration;
    }

    public String getAircraftTotalFlightDistance() {
        return aircraftTotalFlightDistance;
    }

    public void setAircraftTotalFlightDistance(String aircraftTotalFlightDistance) {
        this.aircraftTotalFlightDistance = aircraftTotalFlightDistance;
    }

    public String getAircraftTotalFlightTimes() {
        return aircraftTotalFlightTimes;
    }

    public void setAircraftTotalFlightTimes(String aircraftTotalFlightTimes) {
        this.aircraftTotalFlightTimes = aircraftTotalFlightTimes;
    }

    //总体飞行次数，飞行器断电后不会清零。
    private String aircraftTotalFlightTimes   ;

    public int getAreMotorsOn() {
        return areMotorsOn;
    }

    public void setAreMotorsOn(int areMotorsOn) {
        this.areMotorsOn = areMotorsOn;
    }

    public int getIsFlying() {
        return isFlying;
    }

    public void setIsFlying(int isFlying) {
        this.isFlying = isFlying;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getTakeoffLocationAltitude() {
        return takeoffLocationAltitude;
    }

    public void setTakeoffLocationAltitude(String takeoffLocationAltitude) {
        this.takeoffLocationAltitude = takeoffLocationAltitude;
    }

    public String getPitch() {
        return pitch;
    }

    public void setPitch(String pitch) {
        this.pitch = pitch;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getYaw() {
        return yaw;
    }

    public void setYaw(String yaw) {
        this.yaw = yaw;
    }

    public String getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(String velocityX) {
        this.velocityX = velocityX;
    }

    public String getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(String velocityY) {
        this.velocityY = velocityY;
    }

    public String getVelocityZ() {
        return velocityZ;
    }

    public void setVelocityZ(String velocityZ) {
        this.velocityZ = velocityZ;
    }

    public int getSatelliteCount() {
        return satelliteCount;
    }

    public void setSatelliteCount(int satelliteCount) {
        this.satelliteCount = satelliteCount;
    }

    public String getHomeLocationLatitude() {
        return homeLocationLatitude;
    }

    public void setHomeLocationLatitude(String homeLocationLatitude) {
        this.homeLocationLatitude = homeLocationLatitude;
    }

    public String getHomeLocationLongitude() {
        return homeLocationLongitude;
    }

    public void setHomeLocationLongitude(String homeLocationLongitude) {
        this.homeLocationLongitude = homeLocationLongitude;
    }

    public String getGoHomeHeight() {
        return goHomeHeight;
    }

    public void setGoHomeHeight(String goHomeHeight) {
        this.goHomeHeight = goHomeHeight;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getCompassHeading() {
        return compassHeading;
    }

    public void setCompassHeading(String compassHeading) {
        this.compassHeading = compassHeading;
    }

    public int getGPSsignalLevel() {
        return GPSsignalLevel;
    }

    public void setGPSsignalLevel(int GPSsignalLevel) {
        this.GPSsignalLevel = GPSsignalLevel;
    }

    public String getFlightWindWarning() {
        return flightWindWarning;
    }

    public void setFlightWindWarning(String flightWindWarning) {
        this.flightWindWarning = flightWindWarning;
    }

    public int getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(int windDirection) {
        this.windDirection = windDirection;
    }

    public int getGoHomeExecutionState() {
        return goHomeExecutionState;
    }

    public void setGoHomeExecutionState(int goHomeExecutionState) {
        this.goHomeExecutionState = goHomeExecutionState;
    }

    //智能返航状态。
    private int goHomeExecutionState;

    //飞行器的飞行模式。(V5暂时使用此参数作为飞行状态)
    private int flightMode;

    public int getFlightMode() {
        return flightMode;
    }

    public void setFlightMode(int flightMode) {
        this.flightMode = flightMode;
    }
}
