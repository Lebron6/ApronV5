package com.aros.apron.entity;

public class MissionStateEntity {


    private static class MovementHolder {
        private static final MissionStateEntity INSTANCE = new MissionStateEntity();
    }

    private MissionStateEntity() {
    }

    public static final MissionStateEntity getInstance() {
        return MissionStateEntity.MovementHolder.INSTANCE;
    }

    //航线id
    private int waylineID;
    //当前航点位置
    private int currentWaypointIndex;
    //航线执行状态
    private int waypointMissionExecuteState;
    //航线中断原因
    private String waylineExecutingInterruptReason;
    public int getWaylineID() {
        return waylineID;
    }

    public void setWaylineID(int waylineID) {
        this.waylineID = waylineID;
    }

    public int getCurrentWaypointIndex() {
        return currentWaypointIndex;
    }

    public void setCurrentWaypointIndex(int currentWaypointIndex) {
        this.currentWaypointIndex = currentWaypointIndex;
    }

    public int getWaypointMissionExecuteState() {
        return waypointMissionExecuteState;
    }

    public void setWaypointMissionExecuteState(int waypointMissionExecuteState) {
        this.waypointMissionExecuteState = waypointMissionExecuteState;
    }



    public String getWaylineExecutingInterruptReason() {
        return waylineExecutingInterruptReason;
    }

    public void setWaylineExecutingInterruptReason(String waylineExecutingInterruptReason) {
        this.waylineExecutingInterruptReason = waylineExecutingInterruptReason;
    }
}
