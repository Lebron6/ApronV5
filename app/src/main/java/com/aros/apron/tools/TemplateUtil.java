//package com.aros.apron.tools;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.util.List;
//import org.dom4j.Document;
//import org.dom4j.DocumentHelper;
//import org.dom4j.Element;
//import org.dom4j.io.OutputFormat;
//import org.dom4j.io.XMLWriter;
//
///**
// * @Author: Combo
// * @Date: 2023/3/15 16:27
// * @Description:
// */
//public class TemplateUtil {
//
//  public static void create(String droneType, String payloadType, FlightPathEntity flightPathEntity,
//      String tempPath)
//      throws IOException {
//
//    String dockLatLngHeight = null;
//    Object obj = CacheUtils.get("dock_osd_" + flightPathEntity.getUavId());
//    if (obj != null) {
//      dockLatLngHeight = (String) obj;
//    }
//    int actionGroupId = 0;
//
//    // 创建document对象
//    Document document = DocumentHelper.createDocument();
//    // 创建根节点 kml
//    Element kml = document.addElement("kml");
//
//    kml.addNamespace("", "http://www.opengis.net/kml/2.2")
//        .addNamespace("wpml", "http://www.dji.com/wpmz/1.0.3");
//
//    // 创建Document节点
//    Element dc = kml.addElement("Document");
//
//    dc.addElement("wpml:author").setText("SZHG");
//    dc.addElement("wpml:createTime").setText(System.currentTimeMillis() + "");
//    dc.addElement("wpml:updateTime").setText(System.currentTimeMillis() + "");
//
//    Element missionConfig = dc.addElement("wpml:missionConfig");
//
//    missionConfig.addElement("wpml:flyToWaylineMode").setText("safely");
//    missionConfig.addElement("wpml:finishAction").setText(WayPointUtil.finishedAction(flightPathEntity));
//    missionConfig.addElement("wpml:exitOnRCLost").setText("executeLostAction");
//    missionConfig.addElement("wpml:executeRCLostAction").setText("goBack");
//    missionConfig.addElement("wpml:takeOffSecurityHeight").setText("120");
//    if (dockLatLngHeight != null) {
//      missionConfig.addElement("wpml:takeOffRefPoint").setText(dockLatLngHeight);
//      missionConfig.addElement("wpml:takeOffRefPointAGLHeight")
//          .setText(dockLatLngHeight.split(",")[2]);
//    }
//    missionConfig.addElement("wpml:globalTransitionalSpeed").setText("10");
//    missionConfig.addElement("wpml:globalRTHHeight").setText("100");
//
//    Element droneInfo = missionConfig.addElement("wpml:droneInfo");
//    droneInfo.addElement("wpml:droneEnumValue").setText(WayPointUtil.DRONE_INFO.getString(droneType));
//    droneInfo.addElement("wpml:droneSubEnumValue").setText(WayPointUtil.droneSubEnumValue(droneType));
//
//    Element payloadInfo = missionConfig.addElement("wpml:payloadInfo");
//    payloadInfo.addElement("wpml:payloadEnumValue").setText(WayPointUtil.PAYLOAD_INFO.getString(payloadType));
//    if (payloadType.contains("P1")) {
//      payloadInfo.addElement("wpml:payloadSubEnumValue").setText(WayPointUtil.PAYLOAD_INFO.getString(payloadType + "_lens"));
//    } else {
//      payloadInfo.addElement("wpml:payloadSubEnumValue").setText("0");
//    }
//    payloadInfo.addElement("wpml:payloadPositionIndex").setText("0");
//
//    Element folder = dc.addElement("Folder");
//    folder.addElement("wpml:templateType").setText("waypoint");
//    folder.addElement("wpml:templateId").setText("0");
//    Element waylineCoordinateSysParam = folder.addElement("wpml:waylineCoordinateSysParam");
//    waylineCoordinateSysParam.addElement("wpml:coordinateMode").setText("WGS84");
//    waylineCoordinateSysParam.addElement("wpml:heightMode").setText("EGM96");
//
//    folder.addElement("wpml:autoFlightSpeed").setText(flightPathEntity.getSpeed() + "");
//    folder.addElement("wpml:globalHeight").setText(flightPathEntity.getAltitude() + "");
//    folder.addElement("wpml:caliFlightEnable").setText("0");
//    folder.addElement("wpml:gimbalPitchMode").setText("manual");
//    Element globalWaypointHeadingParam = folder.addElement("wpml:globalWaypointHeadingParam");
//    globalWaypointHeadingParam.addElement("wpml:waypointHeadingMode").setText("followWayline");
//    globalWaypointHeadingParam.addElement("wpml:waypointHeadingAngle").setText("0");
//    globalWaypointHeadingParam.addElement("wpml:waypointPoiPoint").setText("0.000000,0.000000,0.000000");
//    globalWaypointHeadingParam.addElement("wpml:waypointHeadingPathMode").setText("followBadArc");
//    globalWaypointHeadingParam.addElement("wpml:waypointHeadingPoiIndex").setText("0");
//
//    folder.addElement("wpml:globalWaypointTurnMode").setText("toPointAndStopWithDiscontinuityCurvature");
//    folder.addElement("wpml:globalUseStraightLine").setText("1");
//
//    List<FlightPointEntity> flightPointEntities = flightPathEntity.getFlightPointList();
//    if (flightPointEntities != null && flightPointEntities.size() > 0) {
//      for (int i = 0; i < flightPointEntities.size(); i++) {
//        FlightPointEntity flightPointEntity = flightPointEntities.get(i);
//
//        Element placemark = folder.addElement("Placemark");
//        Element point = placemark.addElement("Point");
//
//        String lng = flightPointEntity.getLongitude();
//        lng = String.format("%.9f", Double.parseDouble(lng));
//        String lat = flightPointEntity.getLatitude();
//        lat = String.format("%.9f", Double.parseDouble(lat));
//
//        Gps gps = PositionUtil.gcj02_To_Gps84(Double.parseDouble(lat), Double.parseDouble(lng));
//
//        point.addElement("coordinates").setText(gps.getWgLon() + "," + gps.getWgLat());
//
//        placemark.addElement("wpml:index").setText(i + "");
//        placemark.addElement("wpml:ellipsoidHeight").setText(flightPointEntity.getAltitude() + "");
//        placemark.addElement("wpml:height").setText(
//            flightPointEntity.getHighAltitude() == null ? flightPointEntity.getAltitude() + ""
//                : flightPointEntity.getHighAltitude() + "");
//        placemark.addElement("wpml:waypointSpeed").setText(flightPointEntity.getSpeed() + "");
//
//        Element waypointHeadingParam = placemark.addElement("wpml:waypointHeadingParam");
//        waypointHeadingParam.addElement("wpml:waypointHeadingMode").setText(WayPointUtil.HEADING_MODE.getString(flightPointEntity.getAircraftYawAngle() + ""));
//        waypointHeadingParam.addElement("wpml:waypointHeadingAngle").setText("0");
//        waypointHeadingParam.addElement("wpml:waypointPoiPoint").setText("0.000000,0.000000,0.000000");
//        waypointHeadingParam.addElement("wpml:waypointHeadingPathMode").setText(WayPointUtil.HEADING_YAW_MODE.getString(flightPointEntity.getTurnMode() + ""));
//        waypointHeadingParam.addElement("wpml:waypointHeadingPoiIndex").setText("0");
//
//        Element waypointTurnParam = placemark.addElement("wpml:waypointTurnParam");
//        waypointTurnParam.addElement("wpml:waypointTurnMode").setText(WayPointUtil.TURN_MODE.getString(flightPointEntity.getFlightPointType() + ""));
//        waypointTurnParam.addElement("wpml:waypointTurnDampingDist").setText("0.2");
//
//        placemark.addElement("wpml:useGlobalHeight").setText(flightPointEntity.getHeightFollow() ? "1" : "0");
//        placemark.addElement("wpml:useGlobalSpeed").setText(flightPointEntity.getSpeedFollow() ? "1" : "0");
//        placemark.addElement("wpml:useGlobalHeadingParam").setText("1");
//        placemark.addElement("wpml:useGlobalTurnParam").setText("1");
//        placemark.addElement("wpml:useStraightLine").setText("1");
//
//        if (flightPointEntity.getFlightActionList() != null
//            && flightPointEntity.getFlightActionList().size() > 0) {
//
//          //航点动作
//          Element actionGroup = placemark.addElement("wpml:actionGroup");
//          actionGroup.addElement("wpml:actionGroupId").setText(actionGroupId++ + "");
//          actionGroup.addElement("wpml:actionGroupStartIndex").setText(i + "");
//          actionGroup.addElement("wpml:actionGroupEndIndex").setText(i + "");
//          actionGroup.addElement("wpml:actionGroupMode").setText("sequence");
//
//          //到点执行
//          //reachPoint：到达航点时执行
//          //multipleTiming：等时触发
//          //multipleDistance：等距触发
//          actionGroup.addElement("wpml:actionTrigger").addElement("wpml:actionTriggerType")
//              .setText("reachPoint");
//
//          for (int j = 0; j < flightPointEntity.getFlightActionList().size(); j++) {
//
//            FlightActionEntity flightActionEntity = flightPointEntity.getFlightActionList().get(j);
//            Element action = actionGroup.addElement("wpml:action");
//            action.addElement("wpml:actionId").setText(j + "");
//            Element actionActuatorFunc = action.addElement("wpml:actionActuatorFunc");
//            Element actionActuatorFuncParam = action.addElement("wpml:actionActuatorFuncParam");
//
//            switch (flightActionEntity.getActionType()) {
//
//              //变焦
//              case 1:
//                actionActuatorFunc.setText("zoom");
//                actionActuatorFuncParam.addElement("wpml:focalLength").setText(Math.round(flightActionEntity.getZoom() * 0.2375f) + "");
//                //强制使用飞行器1号挂载位置。M300 RTK机型，对应机身左前方。其它机型，对应主云台。
//                actionActuatorFuncParam.addElement("wpml:payloadPositionIndex").setText("0");
//                actionActuatorFuncParam.addElement("isUseFocalFactor").setText("0");
//                break;
//              //拍照
//              case 2:
//                actionActuatorFunc.setText("takePhoto");
//                actionActuatorFuncParam.addElement("wpml:fileSuffix").setText("航点" + i);
//                actionActuatorFuncParam.addElement("wpml:payloadPositionIndex").setText("0");
//                //是否使用全局存储类型
//                //0：不使用全局设置
//                //1：使用全局设置
//                actionActuatorFuncParam.addElement("wpml:useGlobalPayloadLensIndex").setText("1");
//                break;
//              //开始录像
//              case 3:
//                actionActuatorFunc.setText("startRecord");
//                actionActuatorFuncParam.addElement("wpml:fileSuffix").setText("航点" + i);
//                actionActuatorFuncParam.addElement("wpml:payloadPositionIndex").setText("0");
//                actionActuatorFuncParam.addElement("wpml:useGlobalPayloadLensIndex").setText("1");
//                break;
//              //结束录像
//              case 4:
//                actionActuatorFunc.setText("stopRecord");
//                actionActuatorFuncParam.addElement("wpml:payloadPositionIndex").setText("0");
//                break;
//              //云台偏航角
//              case 5:
//                actionActuatorFunc.setText("gimbalRotate");
//                actionActuatorFuncParam.addElement("wpml:gimbalRotateMode")
//                    .setText("absoluteAngle");
//                actionActuatorFuncParam.addElement("wpml:gimbalPitchRotateEnable").setText("0");
//                actionActuatorFuncParam.addElement("wpml:gimbalPitchRotateAngle").setText("0");
//                actionActuatorFuncParam.addElement("wpml:gimbalRollRotateEnable").setText("0");
//                actionActuatorFuncParam.addElement("wpml:gimbalRollRotateAngle").setText("0");
//                actionActuatorFuncParam.addElement("wpml:gimbalYawRotateEnable").setText("1");
//                actionActuatorFuncParam.addElement("wpml:gimbalYawRotateAngle").setText(flightActionEntity.getCameraYawAngle() + "");
//                actionActuatorFuncParam.addElement("wpml:gimbalRotateTimeEnable").setText("0");
//                actionActuatorFuncParam.addElement("wpml:gimbalRotateTime").setText("0");
//                actionActuatorFuncParam.addElement("wpml:payloadPositionIndex").setText("0");
//                break;
//              //云台俯仰角
//              case 6:
//                actionActuatorFunc.setText("gimbalRotate");
//                actionActuatorFuncParam.addElement("wpml:gimbalRotateMode")
//                    .setText("absoluteAngle");
//                actionActuatorFuncParam.addElement("wpml:gimbalPitchRotateEnable").setText("1");
//                actionActuatorFuncParam.addElement("wpml:gimbalPitchRotateAngle").setText(flightActionEntity.getCameraPitchAngle() + "");
//                actionActuatorFuncParam.addElement("wpml:gimbalRollRotateEnable").setText("0");
//                actionActuatorFuncParam.addElement("wpml:gimbalRollRotateAngle").setText("0");
//                actionActuatorFuncParam.addElement("wpml:gimbalYawRotateEnable").setText("0");
//                actionActuatorFuncParam.addElement("wpml:gimbalYawRotateAngle").setText("0");
//                actionActuatorFuncParam.addElement("wpml:gimbalRotateTimeEnable").setText("0");
//                actionActuatorFuncParam.addElement("wpml:gimbalRotateTime").setText("0");
//                actionActuatorFuncParam.addElement("wpml:payloadPositionIndex").setText("0");
//                break;
//              //飞行器偏航角
//              case 7:
//                actionActuatorFunc.setText("rotateYaw");
//                actionActuatorFuncParam.addElement("wpml:aircraftHeading").setText(flightActionEntity.getUavYawAngle() + "");
//                //强制逆时针旋转
//                actionActuatorFuncParam.addElement("wpml:aircraftPathMode")
//                    .setText("counterClockwise");
//                break;
//              //悬停
//              case 8:
//                actionActuatorFunc.setText("hover");
//                actionActuatorFuncParam.addElement("wpml:hoverTime").setText(flightActionEntity.getWaitingTime() + "");
//                break;
//              default:
//            }
//          }
//        }
//      }
//    }
//
//    Element payloadParam = folder.addElement("wpml:payloadParam");
//    payloadParam.addElement("wpml:payloadPositionIndex").setText("0");
//    payloadParam.addElement("wpml:focusMode").setText("firstPoint");
//    payloadParam.addElement("wpml:meteringMode").setText("average");
//    payloadParam.addElement("wpml:returnMode").setText("singleReturnFirst");
//    payloadParam.addElement("wpml:samplingRate").setText("240000");
//    payloadParam.addElement("wpml:scanningMode").setText("repetitive");
//
//    String imageFormat = flightPathEntity.getImageFormat();
//    if (null == imageFormat || "".equals(imageFormat)) {
//      payloadParam.addElement("wpml:imageFormat").setText("wide,zoom,ir");
//    } else {
//      payloadParam.addElement("wpml:imageFormat").setText(imageFormat);
//    }
//
//    // 设置生成xml的格式
//    OutputFormat format = OutputFormat.createPrettyPrint();
//    format.setEncoding("UTF-8");
//    // 生成xml文件
//    File file1 = new File(
//        tempPath + File.separator + flightPathEntity.getFlightPathId() + File.separator + "wpmz");
//    if (!file1.exists()) {
//      if (file1.mkdirs()) {
//        System.out.println("新建成功");
//      } else {
//        System.out.println("新建失败");
//      }
//    }
//    File file = new File(
//        tempPath + File.separator + flightPathEntity.getFlightPathId() + File.separator + "wpmz"
//            + "/template.kml");
//    XMLWriter writer = new XMLWriter(Files.newOutputStream(file.toPath()), format);
//    // 设置是否转义，默认使用转义字符
//    writer.setEscapeText(false);
//    writer.write(document);
//    writer.close();
//
//  }
//
//
//}
