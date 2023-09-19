//package com.aros.apron.tools
//
//import android.os.Environment
//import android.util.Log
//import com.google.gson.Gson
//import com.shd.wpmz.Document
//import com.shd.wpmz.Kml
//import com.shd.wpmz.Wayline
//import com.shd.wpmz.WaylineExecuteWaypoint
//import com.shd.wpmz.WaylineMissionConfig
//import com.shd.wpmz.mission.FlyToWaylineMode
//import com.shd.wpmz.mission.WaylineExecuteAltitudeMode
//import com.shd.wpmz.mission.WaylineExitOnRCLostAction
//import com.shd.wpmz.mission.WaylineExitOnRCLostBehavior
//import com.shd.wpmz.mission.WaylineFinishedAction
//import org.eclipse.paho.android.service.MqttAndroidClient
//import java.io.File
//import java.util.zip.ZipEntry
//import java.util.zip.ZipOutputStream
//
//object V5MissionUtil {
//    fun creatMission() {
//
//        val waylineMissionConfig = WaylineMissionConfig().apply {
//            flyToWaylineMode = FlyToWaylineMode.SAFELY
//            finishAction = WaylineFinishedAction.GO_HOME
//            exitOnRCLost = WaylineExitOnRCLostBehavior.EXCUTE_RC_LOST_ACTION
//            executeRCLostAction = WaylineExitOnRCLostAction.GO_BACK
//            takeOffSecurityHeight = 180.0
//            globalTransitionalSpeed = 12.0//
//        }
//        Log.e("添加航点",Gson().toJson(addPoint()))
//
//        val wayline = Wayline().apply {
//            templateId = 1
//            waylineId = 1
//            autoFlightSpeed = 15.0
//            executeHeightMode = WaylineExecuteAltitudeMode.RELATIVE_TO_START_POINT
//            waypoints = addPoint()
//
//        }
//
//        val kml = Kml().apply {
//            document = Document(waylineMissionConfig, mutableListOf(wayline))
//        }
//
//        val waylineFileContent = XmlUtils.kmlToXml(kml)
//
//        val templateFileContent = "<?xml version= \"1.0\" encoding=\"UTF-8\"?>\n" +
//                "<kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:wpml=\"http://www.dji.com/wpmz/1.0.2\">\n" +
//                "<Document></Document>\n" +
//                "</kml>"
//
//        val kmzFile =
//            File("${Environment.getExternalStoragePublicDirectory("KMZ")}${File.separator}${1}.kmz")
//
//        kmzFile.outputStream().use { fos ->
//            val zos = ZipOutputStream(fos)
//            ZipEntry("wpmz/waylines.wpml").apply {
//                zos.putNextEntry(this)
//                zos.write(waylineFileContent.toByteArray())
//            }
//            ZipEntry("wpmz/template.kml").apply {
//                zos.putNextEntry(this)
//                zos.write(templateFileContent.toByteArray())
//            }
//            zos.closeEntry()
//            zos.close()
//            fos.close()
//        }
//
//    }
//
//    fun addPoint(): MutableList<WaylineExecuteWaypoint> {
//        val waypoint0 = WaylineExecuteWaypoint(0, 121.115518,31.507000,  170.0)
//        val waypoint1 = WaylineExecuteWaypoint(1, 121.113845,31.505875, 120.0)
//        return mutableListOf<WaylineExecuteWaypoint>(
//            waypoint0,
//            waypoint1
//        )
//    }
//}