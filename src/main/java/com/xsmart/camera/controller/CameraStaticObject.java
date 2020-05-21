package com.xsmart.camera.controller;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tian.xubo
 * @created 2020 - 04 - 08 13:21
 */
public  class CameraStaticObject {

    private static Map<String,String> cameraMap = new HashMap<String,String>();

    private static Map<String,Boolean> cameraIsNvrMap = new HashMap<String,Boolean>();

    public static Map<String, String> getCameraMap() {
        return cameraMap;
    }

    public static void setCameraMap(Map<String, String> cameraMap) {
        CameraStaticObject.cameraMap = cameraMap;
    }

    public static Map<String, Boolean> getCameraIsNvrMap() {
        return cameraIsNvrMap;
    }

    public static void setCameraIsNvrMap(Map<String, Boolean> cameraIsNvrMap) {
        CameraStaticObject.cameraIsNvrMap = cameraIsNvrMap;
    }
}
