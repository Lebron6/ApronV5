//package com.aros.apron.tools;
//
//
//import org.dom4j.Document;
//import org.dom4j.DocumentHelper;
//import org.dom4j.Element;
//import org.dom4j.io.OutputFormat;
//import org.dom4j.io.XMLWriter;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//
//
///**
// * @Author: Combo
// * @Date: 2023/3/15 17:36
// * @Description:
// */
//public class WaylinesUtil {
//
////    public static void main(String[] args) {
////        String s = "120.73020240670375";
////        s = String.format("%.9f", Double.parseDouble(s));
////        System.out.println(s);
////    }
//
//    public static void create(String droneType, String payloadType, FlightPathEntity flightPathEntity,
//                              String tempPath)
//            throws IOException {
//
//        int actionGroupId = 0;
//
//        // 创建document对象
//        Document document = DocumentHelper.createDocument();
//        // 创建根节点 kml
//        Element kml = document.addElement("kml");
//
//        kml.addNamespace("", "http://www.opengis.net/kml/2.2")
//                .addNamespace("wpml", "http://www.dji.com/wpmz/1.0.3");
//
//        // 创建Document节点
//        Element dc = kml.addElement("Document");
//        Element missionConfig = dc.addElement("wpml:missionConfig");
//
//        missionConfig.addElement("wpml:flyToWaylineMode").setText("safely");
//        missionConfig.addElement("wpml:finishAction").setText(WayPointUtil.finishedAction(flightPathEntity));
//        missionConfig.addElement("wpml:exitOnRCLost").setText("executeLostAction");
//        missionConfig.addElement("wpml:executeRCLostAction").setText("goBack");
//        missionConfig.addElement("wpml:takeOffSecurityHeight").setText("100");
//        missionConfig.addElement("wpml:globalTransitionalSpeed").setText("10");
//        missionConfig.addElement("wpml:globalRTHHeight").setText("100");
//
//        Element droneInfo = missionConfig.addElement("wpml:droneInfo");
//        droneInfo.addElement("wpml:droneEnumValue").setText(WayPointUtil.DRONE_INFO.getString(droneType));
//        droneInfo.addElement("wpml:droneSubEnumValue").setText(WayPointUtil.droneSubEnumValue(droneType));
//
//        Element payloadInfo = missionConfig.addElement("wpml:payloadInfo");
//        payloadInfo.addElement("wpml:payloadEnumValue").setText(WayPointUtil.PAYLOAD_INFO.getString(payloadType));
//        if (payloadType.contains("P1")) {
//            payloadInfo.addElement("wpml:payloadSubEnumValue").setText(WayPointUtil.PAYLOAD_INFO.getString(payloadType + "_lens"));
//        } else {
//            payloadInfo.addElement("wpml:payloadSubEnumValue").setText("0");
//        }
//        payloadInfo.addElement("wpml:payloadPositionIndex").setText("0");
//
//        Element folder = dc.addElement("Folder");
//        folder.addElement("wpml:templateId").setText("0");
//        folder.addElement("wpml:executeHeightMode").setText("WGS84");
//        folder.addElement("wpml:waylineId").setText("0");
//        folder.addElement("wpml:distance").setText("360.651885986328");
//        folder.addElement("wpml:duration").setText("73.1895170211792");
//        folder.addElement("wpml:autoFlightSpeed").setText(flightPathEntity.getSpeed() + "");
//
//        List<FlightPointEntity> flightPointEntities = flightPathEntity.getFlightPointList();
//        if (flightPointEntities != null && flightPointEntities.size() > 0) {
//            for (int i = 0; i < flightPointEntities.size(); i++) {
//                FlightPointEntity flightPointEntity = flightPointEntities.get(i);
//
//                Element placemark = folder.addElement("Placemark");
//                Element point = placemark.addElement("Point");
//
//                String lng = flightPointEntity.getLongitude();
//                String lat = flightPointEntity.getLatitude();
//                Gps gps = PositionUtil.gcj02_To_Gps84(Double.parseDouble(lat), Double.parseDouble(lng));
//
//                point.addElement("coordinates").setText(
//                        String.format("%.9f", gps.getWgLon()) + "," + String.format("%.9f", gps.getWgLat()));
//
//                placemark.addElement("wpml:index").setText(i + "");
//                placemark.addElement("wpml:executeHeight").setText(flightPointEntity.getAltitude() + "");
//                placemark.addElement("wpml:waypointSpeed").setText(flightPointEntity.getSpeed() + "");
//
//                Element waypointHeadingParam = placemark.addElement("wpml:waypointHeadingParam");
//                waypointHeadingParam.addElement("wpml:waypointHeadingMode").setText(WayPointUtil.HEADING_MODE.getString(flightPointEntity.getAircraftYawAngle() + ""));
//                waypointHeadingParam.addElement("wpml:waypointHeadingAngle").setText("0");
//                waypointHeadingParam.addElement("wpml:waypointPoiPoint").setText("0.000000,0.000000,0.000000");
//                waypointHeadingParam.addElement("wpml:waypointHeadingAngleEnable").setText("0");
//                waypointHeadingParam.addElement("wpml:waypointHeadingPathMode").setText(WayPointUtil.HEADING_YAW_MODE.getString(flightPointEntity.getTurnMode() + ""));
//                waypointHeadingParam.addElement("wpml:waypointHeadingPoiIndex").setText("0");
//
//                Element waypointTurnParam = placemark.addElement("wpml:waypointTurnParam");
//                waypointTurnParam.addElement("wpml:waypointTurnMode").setText(WayPointUtil.TURN_MODE.getString(flightPointEntity.getFlightPointType() + ""));
//                waypointTurnParam.addElement("wpml:waypointTurnDampingDist").setText("0");
//
//                placemark.addElement("wpml:useStraightLine").setText("1");
//
//                if (flightPointEntity.getFlightActionList() != null
//                        && flightPointEntity.getFlightActionList().size() > 0) {
//
//                    //航点动作
//                    Element actionGroup = placemark.addElement("wpml:actionGroup");
//                    actionGroup.addElement("wpml:actionGroupId").setText(actionGroupId++ + "");
//                    actionGroup.addElement("wpml:actionGroupStartIndex").setText(i + "");
//                    actionGroup.addElement("wpml:actionGroupEndIndex").setText(i + "");
//                    actionGroup.addElement("wpml:actionGroupMode").setText("sequence");
//
//                    //到点执行
//                    //reachPoint：到达航点时执行
//                    //multipleTiming：等时触发
//                    //multipleDistance：等距触发
//                    actionGroup.addElement("wpml:actionTrigger").addElement("wpml:actionTriggerType")
//                            .setText("reachPoint");
//
//                    for (int j = 0; j < flightPointEntity.getFlightActionList().size(); j++) {
//
//                        FlightActionEntity flightActionEntity = flightPointEntity.getFlightActionList().get(j);
//                        Element action = actionGroup.addElement("wpml:action");
//                        action.addElement("wpml:actionId").setText(j + "");
//                        Element actionActuatorFunc = action.addElement("wpml:actionActuatorFunc");
//                        Element actionActuatorFuncParam = action.addElement("wpml:actionActuatorFuncParam");
//
//                        switch (flightActionEntity.getActionType()) {
//
//                            //变焦
//                            case 1:
//                                actionActuatorFunc.setText("zoom");
//                                actionActuatorFuncParam.addElement("wpml:focalLength").setText(Math.round(flightActionEntity.getZoom() * 0.2375f) + "");
//                                //强制使用飞行器1号挂载位置。M300 RTK机型，对应机身左前方。其它机型，对应主云台。
//                                actionActuatorFuncParam.addElement("wpml:payloadPositionIndex").setText("0");
//                                actionActuatorFuncParam.addElement("isUseFocalFactor").setText("0");
//                                break;
//                            //拍照
//                            case 2:
//                                actionActuatorFunc.setText("takePhoto");
//                                actionActuatorFuncParam.addElement("wpml:fileSuffix").setText("航点" + i);
//                                actionActuatorFuncParam.addElement("wpml:payloadPositionIndex").setText("0");
//                                //是否使用全局存储类型
//                                //0：不使用全局设置
//                                //1：使用全局设置
//                                actionActuatorFuncParam.addElement("wpml:useGlobalPayloadLensIndex").setText("1");
//                                actionActuatorFuncParam.addElement("wpml:payloadLensIndex").setText(flightPathEntity.getImageFormat());
//
//                                break;
//                            //开始录像
//                            case 3:
//                                actionActuatorFunc.setText("startRecord");
//                                actionActuatorFuncParam.addElement("wpml:fileSuffix").setText("航点" + i);
//                                actionActuatorFuncParam.addElement("wpml:payloadPositionIndex").setText("0");
//                                actionActuatorFuncParam.addElement("wpml:useGlobalPayloadLensIndex").setText("1");
//                                actionActuatorFuncParam.addElement("wpml:payloadLensIndex").setText(flightPathEntity.getImageFormat());
//                                break;
//                            //结束录像
//                            case 4:
//                                actionActuatorFunc.setText("stopRecord");
//                                actionActuatorFuncParam.addElement("wpml:payloadPositionIndex").setText("0");
//                                break;
//                            //云台偏航角
//                            case 5:
//                                actionActuatorFunc.setText("gimbalRotate");
//                                actionActuatorFuncParam.addElement("wpml:gimbalRotateMode")
//                                        .setText("absoluteAngle");
//                                actionActuatorFuncParam.addElement("wpml:gimbalPitchRotateEnable").setText("0");
//                                actionActuatorFuncParam.addElement("wpml:gimbalPitchRotateAngle").setText("0");
//                                actionActuatorFuncParam.addElement("wpml:gimbalRollRotateEnable").setText("0");
//                                actionActuatorFuncParam.addElement("wpml:gimbalRollRotateAngle").setText("0");
//                                actionActuatorFuncParam.addElement("wpml:gimbalYawRotateEnable").setText("1");
//                                actionActuatorFuncParam.addElement("wpml:gimbalYawRotateAngle").setText(flightActionEntity.getCameraYawAngle() + "");
//                                actionActuatorFuncParam.addElement("wpml:gimbalRotateTimeEnable").setText("0");
//                                actionActuatorFuncParam.addElement("wpml:gimbalRotateTime").setText("0");
//                                actionActuatorFuncParam.addElement("wpml:payloadPositionIndex").setText("0");
//                                break;
//                            //云台俯仰角
//                            case 6:
//                                actionActuatorFunc.setText("gimbalRotate");
//                                actionActuatorFuncParam.addElement("wpml:gimbalRotateMode").setText("absoluteAngle");
//                                actionActuatorFuncParam.addElement("wpml:gimbalPitchRotateEnable").setText("1");
//                                actionActuatorFuncParam.addElement("wpml:gimbalPitchRotateAngle").setText(flightActionEntity.getCameraPitchAngle() + "");
//                                actionActuatorFuncParam.addElement("wpml:gimbalRollRotateEnable").setText("0");
//                                actionActuatorFuncParam.addElement("wpml:gimbalRollRotateAngle").setText("0");
//                                actionActuatorFuncParam.addElement("wpml:gimbalYawRotateEnable").setText("0");
//                                actionActuatorFuncParam.addElement("wpml:gimbalYawRotateAngle").setText("0");
//                                actionActuatorFuncParam.addElement("wpml:gimbalRotateTimeEnable").setText("0");
//                                actionActuatorFuncParam.addElement("wpml:gimbalRotateTime").setText("0");
//                                actionActuatorFuncParam.addElement("wpml:payloadPositionIndex").setText("0");
//                                break;
//                            //飞行器偏航角
//                            case 7:
//                                actionActuatorFunc.setText("rotateYaw");
//                                actionActuatorFuncParam.addElement("wpml:aircraftHeading").setText(flightActionEntity.getUavYawAngle() + "");
//                                //强制逆时针旋转
//                                actionActuatorFuncParam.addElement("wpml:aircraftPathMode").setText("counterClockwise");
//                                break;
//                            //悬停
//                            case 8:
//                                actionActuatorFunc.setText("hover");
//                                actionActuatorFuncParam.addElement("wpml:hoverTime").setText(flightActionEntity.getWaitingTime() + "");
//                                break;
//                            default:
//                        }
//                    }
//                }
//                Element waypointGimbalHeadingParam = placemark.addElement(
//                        "wpml:waypointGimbalHeadingParam");
//                waypointGimbalHeadingParam.addElement("wpml:waypointGimbalPitchAngle").setText("0");
//                waypointGimbalHeadingParam.addElement("wpml:waypointGimbalYawAngle").setText("0");
//
//            }
//        }
//
//        // 设置生成xml的格式
//        OutputFormat format = OutputFormat.createPrettyPrint();
//        format.setEncoding("UTF-8");
//        // 生成xml文件
//        File file1 = new File(
//                tempPath + File.separator + flightPathEntity.getFlightPathId() + File.separator + "wpmz");
//        if (!file1.exists()) {
//            if (file1.mkdirs()) {
//                System.out.println("新建成功");
//            } else {
//                System.out.println("新建失败");
//            }
//        }
//        File file = new File(
//                tempPath + File.separator + flightPathEntity.getFlightPathId() + File.separator + "wpmz"
//                        + "/waylines.wpml");
//        XMLWriter writer = new XMLWriter(Files.newOutputStream(file.toPath()), format);
//        // 设置是否转义，默认使用转义字符
//        writer.setEscapeText(false);
//        writer.write(document);
//        writer.close();
//
//
//    }
//
//}
