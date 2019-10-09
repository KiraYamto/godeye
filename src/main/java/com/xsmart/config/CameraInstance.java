package com.xsmart.config;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tian.xubo
 * @created 2019 - 07 - 18 14:19
 */
public class CameraInstance {
    //用来保存已启动的推流摄像头地址，单点情况下可用JVM，如果需要部署集群，需要放到redis上
    public static ConcurrentHashMap<String,String> CAMERA_HTTP_ADDR = new ConcurrentHashMap<>();

}
