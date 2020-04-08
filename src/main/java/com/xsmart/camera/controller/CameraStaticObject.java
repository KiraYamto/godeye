package com.xsmart.camera.controller;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tian.xubo
 * @created 2020 - 04 - 08 13:21
 */
public  class CameraStaticObject {

    private static Map<String,String> cameraMap = new HashMap<String,String>();

    public static Map<String, String> getCameraMap() {
        return cameraMap;
    }

    public static void setCameraMap(Map<String, String> cameraMap) {
        CameraStaticObject.cameraMap = cameraMap;
    }
}
