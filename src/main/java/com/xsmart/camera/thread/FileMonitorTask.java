package com.xsmart.camera.thread;

import com.alibaba.fastjson.JSON;
import com.xsmart.camera.GodeyeProperties;
import com.xsmart.camera.service.ReplayConfigService;
import com.xsmart.camera.service.ReplayConfigServiceImpl;
import com.xsmart.camera.service.SrsService;
import com.xsmart.camera.service.SrsServiceImpl;
import com.xsmart.camera.srs.v2.model.GstCameraReplayConfigDto;
import com.xsmart.camera.srs.v2.model.MonitorRequest;
import com.xsmart.camera.util.BeanFactory;
import com.xsmart.camera.util.FfmpegUtil;
import com.xsmart.camera.util.M3U8Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tian.xubo
 * @created 2019 - 09 - 23 14:00
 */
public class FileMonitorTask implements Runnable {

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    private static final Logger logger = LoggerFactory.getLogger(FileMonitorTask.class);
    private ReplayConfigService replayConfigService;
    private SrsService srsService;
    private GodeyeProperties godeyeProperties;
    private FfmpegUtil ffmpegUtil;
    private M3U8Util m3U8Util;
    private Map<String,Integer> devicePhotoMap = new HashMap<>();
/*
    private Map<File,List<>>
*/
    //192.168.1.64_01_20190827095800502_MOTION_DETECTION.jpg
    public FileMonitorTask() {
        replayConfigService = BeanFactory.lookUp(ReplayConfigServiceImpl.class);
        srsService = BeanFactory.lookUp(SrsServiceImpl.class);
        godeyeProperties = BeanFactory.lookUp(GodeyeProperties.class);
        ffmpegUtil = BeanFactory.lookUp(FfmpegUtil.class);
        m3U8Util = BeanFactory.lookUp(M3U8Util.class);
    }
    @Override
    public void run() {
        logger.info("begin run FileMonitorTask  now is {}",ffmpegUtil.timeFormatForString(new Date()));
        File file = new File(godeyeProperties.getMonitorFilePath());
        if(!file.exists() ){
           logger.warn("Monitor path is not exist！ path is {}",godeyeProperties.getMonitorFilePath());
        }
        MonitorRequest request = null;
        File[] fileDeviceArr = file.listFiles();
        for(File device :fileDeviceArr){
            File[] photos = device.listFiles();
            if(devicePhotoMap.get(device.getName()) == null){
                devicePhotoMap.put(device.getName(),photos.length);
                request = getMonitorRequest(device,photos[photos.length -1]);
                logger.info("photos num is {}",photos.length);
            }else {
                int oldPhotos = devicePhotoMap.get(device.getName());
                if(oldPhotos < photos.length){
                    devicePhotoMap.put(device.getName(),photos.length);
                    request = getMonitorRequest(device,photos[photos.length -1]);
                    logger.info("last time photos num is {} and now is {}",oldPhotos,photos.length);
                }
            }
            if(request != null){
                logger.info("monitor request is {}", JSON.toJSONString(request));
                srsService.monitorCallBack(request);
                request = null;

            }
        }
        logger.info("end run FileMonitorTask task now is {}",ffmpegUtil.timeFormatForString(new Date()));

    }
    private MonitorRequest getMonitorRequest(File device,File file){
        //192.168.1.64_01_20190827095735268_MOTION_DETECTION.jpg
        //192.168.1.64_01_20190927140538112_REGION_ENTRANCE_DETECTION.jpg
        //logger.info("getMonitorRequest device is {},and lastFile is {}",device.getName(),file.getName());
        String name  = file.getName();
        String[] photosArr = name.split("_");
        MonitorRequest request = new MonitorRequest();

        if(photosArr.length == 5){
            request.setAlertType(photosArr[3]);
        }else if(photosArr.length == 6 ){
            request.setAlertType(photosArr[3]+"_"+photosArr[4]);

        }
        request.setVideoId(device.getName());
        request.setHappendTime(photosArr[2]);
        String filePath = godeyeProperties.getNginxMonitorPath()+"/"+device.getName()+"/"+file.getName();
        request.setImgPath(filePath);
        return request;

    }

    public static void main(String[] args) {

    }
}
