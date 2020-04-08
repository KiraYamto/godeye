package com.xsmart.camera.controller;


import com.alibaba.fastjson.JSON;
import com.xsmart.camera.GodeyeProperties;
import com.xsmart.camera.http.CameraResponse;
import com.xsmart.camera.service.ReplayConfigService;
import com.xsmart.camera.service.SrsService;
import com.xsmart.camera.srs.v2.model.GstCameraReplayConfigDto;
import com.xsmart.camera.srs.v2.model.PlayRequest;
import com.xsmart.camera.srs.v2.model.RecordRequest;
import com.xsmart.camera.srs.v2.model.ScreenshotRequest;
import com.xsmart.camera.thread.RecordTask;
import com.xsmart.camera.thread.TsRecordTask;
import com.xsmart.camera.util.FfmpegUtil;
import com.xsmart.camera.util.M3U8Util;
import com.xsmart.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author tian.xubo
 * @created 2019 - 07 - 01 17:11
 */
@RestController
public class FfmpegController {
    ///root/godeye/ffmpeg-4.1.3/ffmpeg -re -i /root/godeye/onepiece.mp4
    // -vcodec copy -acodec copy -f flv -y rtmp://172.21.64.133:1935/live/onepiece


    private static final Logger logger = LoggerFactory.getLogger(FfmpegController.class);

    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newScheduledThreadPool(10);


    @Autowired
    private GodeyeProperties godeyeProperties;

    @Autowired
    private SrsService srsService;

    @Autowired
    private FfmpegUtil ffmpegUtil;

    @Autowired
    private M3U8Util m3U8Util;

    @Autowired
    private ReplayConfigService replayConfigService;
    /***
     * streamPath rtsp地址 或者测试文件地址，
     * rtsp://admin:gst.123456@192.168.31.64:554/Streaming/Channels/101
     * provider 网络摄像头供应商 例如 hikvision
     * deviceId 设备标识 150001
     */
    @RequestMapping(value = {"/playStream"},method = {RequestMethod.POST})
    public CameraResponse playStream(@RequestBody String request){
        logger.info("=======http request playStream begin request is {} ===== ",request);

        PlayRequest playRequest = JSON.parseObject(request,PlayRequest.class);
        //地址使用直播vhost
        String outPutPath = "rtmp://"+godeyeProperties.getLiveHost()+":"+godeyeProperties.getSrsServerPort()+"/"+playRequest.getProvider()+"-"+playRequest.getDeviceId()+"/"+playRequest.getDeviceId();
        String outPutPathMobility = "rtmp://"+godeyeProperties.getLiveHost()+":"+godeyeProperties.getSrsServerPort()+"/"+playRequest.getProvider()+"-mobility"+"-"+playRequest.getDeviceId()+"/"+playRequest.getDeviceId();
        if(CameraStaticObject.getCameraMap().get(outPutPath) == null){
            CameraStaticObject.getCameraMap().put(outPutPath,outPutPath);
        }
        ///usr/local/srs/objs/nginx/html/replay-hikvision-150001 存放回放ts文件路径
        String tsFilePath = godeyeProperties.getTsBasePath()+"/replay-"+playRequest.getProvider()+"-"+playRequest.getDeviceId();
        GstCameraReplayConfigDto replayConfigDto = new GstCameraReplayConfigDto();
        CameraResponse response = new CameraResponse();
        replayConfigDto.setTsFilePath(tsFilePath);
        replayConfigDto.setCameraId(playRequest.getDeviceId());
        //默认不开启回放
        replayConfigDto.setReplayOn(0);
        //观看中，0无人观看，1有人观看。
        replayConfigDto.setProvider(playRequest.getProvider());
        replayConfigDto.setRtspAddr(playRequest.getStreamPath());
        //视频编码格式，H264 H265，默认使用H264
        String vcodec = playRequest.getVcodec() == null?Constants.VODEC_H264:playRequest.getVcodec();
        replayConfigDto.setVcodec(vcodec);
        try {
            replayConfigDto.setConfigId(Long.parseLong(playRequest.getDeviceId()));
            GstCameraReplayConfigDto configExist = this.replayConfigService.queryReplayConfigByCameraId(replayConfigDto);
            if(configExist == null){
                this.replayConfigService.insertReplayConfig(replayConfigDto);
            }else {
                int players = configExist.getPlayer()+1;
                replayConfigDto.setPlayer(players);
                logger.info("GstCameraReplayConfigDto is {} and players is {}",JSON.toJSONString(configExist),players);
                this.replayConfigService.updateReplayConfig(replayConfigDto);
            }
        } catch (Exception e) {
            logger.error("replaySet operate database occur an error!",e);
            response.setCode(Constants.CameraResponse.ERROR.getCode());
            response.setDesc(Constants.CameraResponse.ERROR.getDesc());
        }
        String PID = srsService.getPID(outPutPath);
        String PIDMobility = srsService.getPID(outPutPathMobility);
        synchronized (CameraStaticObject.getCameraMap().get(outPutPath)){
            logger.info("=====lock begin========");
            if(PID == null){
                //启动推流
                //
                List<String> command = ffmpegUtil.buildPushStreamCommand(playRequest.getStreamPath(),playRequest.getProvider(),playRequest.getDeviceId(),vcodec,true);
                try {
                    //调用线程命令进行转码
                    ProcessBuilder builder = new ProcessBuilder(command);
                    builder.command(command);
                    builder.start();
                } catch (Exception e) {
                    logger.error("startPushStream occur an error",e);
                }
            }
            if(PIDMobility == null){
                 //增加手机低码率配置
                List<String> commandMobility = ffmpegUtil.buildPushStreamCommandMobility(playRequest.getStreamPath(),playRequest.getProvider(),playRequest.getDeviceId(),godeyeProperties.getBitrate());
                try {
                    //调用线程命令进行转码
                    ProcessBuilder builder = new ProcessBuilder(commandMobility);
                    builder.command(commandMobility);
                    builder.start();
                } catch (Exception e) {
                    logger.error("startPushStream occur an error",e);
                }
            }
            logger.info("=====lock end========");
        }
        response.setCode(Constants.CameraResponse.SUCCESS.getCode());
        response.setDesc(Constants.CameraResponse.SUCCESS.getDesc());

        logger.info("=======http request playStream end response is {} ===== ",JSON.toJSONString(response));
        String m3u8Addr = m3U8Util.transformAddr(outPutPath.replace(godeyeProperties.getLiveHost(),godeyeProperties.getSrsHost()),godeyeProperties.getSrsServerPort());
        String m3u8AddrMobility = m3U8Util.transformAddr(outPutPathMobility.replace(godeyeProperties.getLiveHost(),godeyeProperties.getSrsHost()),godeyeProperties.getSrsServerPort());

        Map<String,String> map = new HashMap<>();
        outPutPath = outPutPath.replace(godeyeProperties.getLiveHost(),godeyeProperties.getSrsHost()).replace(godeyeProperties.getSrsServerPort(),godeyeProperties.getSrsServerOutPort());
        outPutPathMobility = outPutPathMobility.replace(godeyeProperties.getLiveHost(),godeyeProperties.getSrsHost()).replace(godeyeProperties.getSrsServerPort(),godeyeProperties.getSrsServerOutPort());

        map.put("RTMP",outPutPath);
        map.put("RTMP-Mobility",outPutPathMobility);
        map.put("M3U8",m3u8Addr);
        map.put("M3U8-Mobility",m3u8AddrMobility);
        response.setOther(map);
        return response;
    }




    //关闭播放窗口
    @RequestMapping(value = {"/closeStream"},method = {RequestMethod.POST})
    public CameraResponse closeStream(@RequestBody String request){
        logger.info("=======http request closeStream begin request is {} ===== ",request);
        PlayRequest playRequest = JSON.parseObject(request,PlayRequest.class);
        String outPutPath = "rtmp://"+godeyeProperties.getLiveHost()+":"+godeyeProperties.getSrsServerPort()+"/"+playRequest.getProvider()+"-"+playRequest.getDeviceId()+"/"+playRequest.getDeviceId();
        String outPutPathMobility = "rtmp://"+godeyeProperties.getLiveHost()+":"+godeyeProperties.getSrsServerPort()+"/"+playRequest.getProvider()+"-mobility"+"-"+playRequest.getDeviceId()+"/"+playRequest.getDeviceId();
        CameraResponse response = new CameraResponse();
        GstCameraReplayConfigDto replayConfigDto = new GstCameraReplayConfigDto();
        replayConfigDto.setCameraId(playRequest.getDeviceId());
        if(CameraStaticObject.getCameraMap().get(outPutPath) == null){
            CameraStaticObject.getCameraMap().put(outPutPath,outPutPath);
        }
        try {
            GstCameraReplayConfigDto gstCameraReplayConfigDto = this.replayConfigService.queryReplayConfigByCameraId(replayConfigDto);
            int players = gstCameraReplayConfigDto.getPlayer()-1 < 0 ? 0:gstCameraReplayConfigDto.getPlayer()-1;
            gstCameraReplayConfigDto.setPlayer(players);
            this.replayConfigService.updateReplayConfig(gstCameraReplayConfigDto);

            synchronized (CameraStaticObject.getCameraMap().get(outPutPath)){
                String PID = srsService.getPID(outPutPath);
                String PIDMobility = srsService.getPID(outPutPathMobility);
                if(PID != null && players == 0){
                    srsService.closeLinuxProcess(PID);
                    srsService.closeLinuxProcess(PIDMobility);
                    String m3u8Dir = godeyeProperties.getTsBasePath()+"/"+playRequest.getProvider()+"-"+playRequest.getDeviceId();
                    String m3u8DirMobility = godeyeProperties.getTsBasePath()+"/"+playRequest.getProvider()+"-"+playRequest.getDeviceId()+"-mobility";
                    m3U8Util.removeAllFile(m3u8Dir);
                    m3U8Util.removeAllFile(m3u8DirMobility);
                }
            }
            response.setCode(Constants.CameraResponse.SUCCESS.getCode());
            response.setDesc(Constants.CameraResponse.SUCCESS.getDesc());
        } catch (Exception e) {
            logger.error("stopStream replaySet operate database occur an error!",e);
            response.setCode(Constants.CameraResponse.ERROR.getCode());
            response.setDesc(Constants.CameraResponse.ERROR.getDesc());
        }
        response.setOther(m3U8Util.transformAddr(outPutPath.replace(godeyeProperties.getLiveHost(),godeyeProperties.getSrsHost()),godeyeProperties.getSrsServerPort()));
        return response;
    }
    //强制停止推流，观看人数置为0.
    @RequestMapping(value = {"/stopStream"},method = {RequestMethod.POST})
    public CameraResponse stopStream(@RequestBody String request){
        logger.info("=======http request stopStream begin request is {} ===== ",request);
        PlayRequest playRequest = JSON.parseObject(request,PlayRequest.class);
        String outPutPath = "rtmp://"+godeyeProperties.getLiveHost()+":"+godeyeProperties.getSrsServerPort()+"/"+playRequest.getProvider()+"-"+playRequest.getDeviceId()+"/"+playRequest.getDeviceId();
        String outPutPathMobility = "rtmp://"+godeyeProperties.getLiveHost()+":"+godeyeProperties.getSrsServerPort()+"/"+playRequest.getProvider()+"-mobility"+"-"+playRequest.getDeviceId()+"/"+playRequest.getDeviceId();
        CameraResponse response = new CameraResponse();
        GstCameraReplayConfigDto replayConfigDto = new GstCameraReplayConfigDto();
        if(CameraStaticObject.getCameraMap().get(outPutPath) == null){
            CameraStaticObject.getCameraMap().put(outPutPath,outPutPath);
        }
        replayConfigDto.setCameraId(playRequest.getDeviceId());
        try {
            GstCameraReplayConfigDto gstCameraReplayConfigDto = this.replayConfigService.queryReplayConfigByCameraId(replayConfigDto);
            gstCameraReplayConfigDto.setPlayer(0);
            this.replayConfigService.updateReplayConfig(gstCameraReplayConfigDto);
            synchronized (CameraStaticObject.getCameraMap().get(outPutPath)){
                String PID = srsService.getPID(outPutPath);
                String PIDMobility = srsService.getPID(outPutPathMobility);
                logger.info("stop stream find process is  {}----{} and find command is {}---{}",PID,PIDMobility,outPutPath,outPutPathMobility);
                if(PID != null ){
                    srsService.closeLinuxProcess(PID);
                    srsService.closeLinuxProcess(PIDMobility);
                }
            }
            response.setCode(Constants.CameraResponse.SUCCESS.getCode());
            response.setDesc(Constants.CameraResponse.SUCCESS.getDesc());
        } catch (Exception e) {
            logger.error("stopStream replaySet operate database occur an error!",e);
            response.setCode(Constants.CameraResponse.ERROR.getCode());
            response.setDesc(Constants.CameraResponse.ERROR.getDesc());
        }
        response.setOther(m3U8Util.transformAddr(outPutPath.replace(godeyeProperties.getLiveHost(),godeyeProperties.getSrsHost()),godeyeProperties.getSrsServerPort()));
        logger.info("=======http request stopStream end response is {} ===== ",JSON.toJSONString(response));
        return response;
    }
    //判断是否推流中
    @RequestMapping(value = {"/judgeStream"},method = {RequestMethod.POST})
    public CameraResponse judgeStream(@RequestBody String request){
        logger.info("=======http request stopStream begin request is {} ===== ",request);
        PlayRequest playRequest = JSON.parseObject(request,PlayRequest.class);
        String outPutPath = "rtmp://"+godeyeProperties.getLiveHost()+":"+godeyeProperties.getSrsServerPort()+"/"+playRequest.getProvider()+"-"+playRequest.getDeviceId()+"/"+playRequest.getDeviceId();
        String outPutPathMobility = "rtmp://"+godeyeProperties.getLiveHost()+":"+godeyeProperties.getSrsServerPort()+"/"+playRequest.getProvider()+"-mobility"+"-"+playRequest.getDeviceId()+"/"+playRequest.getDeviceId();

        String PID = srsService.getPID(outPutPath);
        String PIDMobility = srsService.getPID(outPutPathMobility);

        CameraResponse response = new CameraResponse();
        if(PID == null && PIDMobility == null){
            response.setOther(0-0);
        }else if(PID != null && PIDMobility == null) {
            response.setOther(1-0);
        }else if(PID == null && PIDMobility != null) {
            response.setOther(0-1);
        }else if(PID != null && PIDMobility != null) {
            response.setOther(1-1);
        }
        response.setCode(Constants.CameraResponse.SUCCESS.getCode());
        response.setDesc(Constants.CameraResponse.SUCCESS.getDesc());
        return response;
    }


    /**
     * 废弃
     */
    @Deprecated
    @RequestMapping(value = {"/startRecord"},method = {RequestMethod.POST})
    public CameraResponse startRecord(@RequestBody String request){
        logger.info("=======http request startRecord begin request is {} ===== ",request);
        CameraResponse response = new CameraResponse();
        RecordRequest recordRequest = JSON.parseObject(request,RecordRequest.class);
        RecordTask end = new RecordTask(recordRequest,srsService,ffmpegUtil,godeyeProperties,true);
        Date startMoment = recordRequest.getStartMoment() == null?null:ffmpegUtil.timeFormatForDate(recordRequest.getStartMoment());
        Date endMoment = recordRequest.getFinishMoment() == null?null:ffmpegUtil.timeFormatForDate(recordRequest.getFinishMoment());
        Date now  = new Date();
        if(startMoment != null){
            long intervalStarted = startMoment.getTime() - now.getTime();
            logger.info("duration {} to {} record a video,and remain {} ms begin !",recordRequest.getStartMoment(),
                    recordRequest.getFinishMoment(),intervalStarted);
            RecordTask start = new RecordTask(recordRequest,srsService,ffmpegUtil,godeyeProperties,false);
            if(intervalStarted < 0){
                intervalStarted = 0;
            }
            SCHEDULED_EXECUTOR_SERVICE.schedule(start,intervalStarted,TimeUnit.MILLISECONDS);
        }
        if(endMoment != null){
            long intervalEnd = endMoment.getTime() - now.getTime();
            logger.info("duration {} to {} record a video,and remain {} ms end !",recordRequest.getStartMoment(),
                    recordRequest.getFinishMoment(),intervalEnd);
            if(startMoment == null){
                //手动停止 立刻执行
                SCHEDULED_EXECUTOR_SERVICE.schedule(end,0,TimeUnit.MILLISECONDS);

            }
            if(intervalEnd < 0){
                response.setCode(Constants.CameraResponse.SUCCESS_NO_OPERATION.getCode());
                response.setDesc("finishMoment has expired! please check");
                return response;
            }else {
                SCHEDULED_EXECUTOR_SERVICE.schedule(end,intervalEnd,TimeUnit.MILLISECONDS);
            }
        }
        response.setCode(Constants.CameraResponse.SUCCESS.getCode());
        response.setDesc(Constants.CameraResponse.SUCCESS.getDesc());
        return response;
    }
    /**
     *
     * 测试点，直播录像，回放录像，手动点击开始结束，点击开始，自动录N秒。
     */
    @RequestMapping(value = {"/startRecordWithTs"},method = {RequestMethod.POST})
    public CameraResponse startRecordWithTs(@RequestBody String request){
        logger.info("=======http request startRecordWithTs begin request is {} ===== ",request);
        CameraResponse response = new CameraResponse();
        RecordRequest recordRequest = JSON.parseObject(request,RecordRequest.class);
        //m3u8路径
        //如果日期为空，说明是直播中进行录像，否则是在查看回放过程中进行录像
        //日期格式为yyyy-MM-dd
        Date startMoment = recordRequest.getStartMoment() == null?null:ffmpegUtil.timeFormatForDate(recordRequest.getStartMoment());
        Date endMoment = recordRequest.getFinishMoment() == null?null:ffmpegUtil.timeFormatForDate(recordRequest.getFinishMoment());
        if(startMoment == null || endMoment == null){
            response.setCode(Constants.CameraResponse.NULL_PARAMETER.getCode());
            response.setDesc("startMoment or endMoment " +Constants.CameraResponse.NULL_PARAMETER.getDesc());
            return response;
        }
        Date now  = new Date();
        String startFile = null;
        if(startMoment != null){
            String m3u8Path = godeyeProperties.getTsBasePath();
            if(recordRequest.getDate() == null){
                m3u8Path = m3u8Path+"/"+recordRequest.getProvider()+"-"+recordRequest.getDeviceId()+"/"+recordRequest.getDeviceId()+Constants.M3U8_SUFFIX;
            }else {
                //---
                m3u8Path = m3u8Path+"/replay-"+recordRequest.getProvider()+"-"+recordRequest.getDeviceId()+"/"+recordRequest.getDate()+"/"+recordRequest.getDeviceId()+Constants.M3U8_SUFFIX;
            }
             startFile = m3U8Util.readLastTsFile(m3u8Path);
            logger.info("startRecordWithTs startTsFile is {}",startFile);
        }
        if(endMoment != null){
            long intervalEnd = endMoment.getTime() - now.getTime();
            logger.info("duration {} to {} record a video,and remain {} ms end !",recordRequest.getStartMoment(),
                    recordRequest.getFinishMoment(),intervalEnd);
            TsRecordTask end = new TsRecordTask(recordRequest,godeyeProperties,m3U8Util,ffmpegUtil,startFile);
            if(startMoment == null){
                //手动停止 立刻执行
                SCHEDULED_EXECUTOR_SERVICE.schedule(end,0,TimeUnit.MILLISECONDS);
            }
            if(intervalEnd < 0){
                response.setCode(Constants.CameraResponse.SUCCESS_NO_OPERATION.getCode());
                response.setDesc("finishMoment has expired! please check");
                return response;
            }else {
                SCHEDULED_EXECUTOR_SERVICE.schedule(end,intervalEnd,TimeUnit.MILLISECONDS);
            }
        }
        response.setCode(Constants.CameraResponse.SUCCESS.getCode());
        response.setDesc(Constants.CameraResponse.SUCCESS.getDesc());
        return response;
    }


    @RequestMapping(value = {"/screenshot"},method = {RequestMethod.POST})
    public CameraResponse screenshot(@RequestBody String request){
        logger.info("=======http request screenshot begin request is {}===== ", request);
        ScreenshotRequest screenshotRequest = JSON.parseObject(request,ScreenshotRequest.class);
        if(!godeyeProperties.getInnerHost().equals(godeyeProperties.getSrsHost())){
            String tempPath = screenshotRequest.getStreamPath().replaceAll(godeyeProperties.getSrsHost(),godeyeProperties.getInnerHost()).replaceAll(godeyeProperties.getSrsServerOutPort(),godeyeProperties.getSrsServerPort());
            logger.info("====transfer outStreamPath {} to {}",screenshotRequest.getStreamPath(),tempPath);
            screenshotRequest.setStreamPath(tempPath);
        }
        logger.info("=======json parseobj is {}======",JSON.toJSONString(screenshotRequest));
        //判断文件夹是否存在
        File path = new File(godeyeProperties.getScreenshotPath());
        if(!path.exists()||!path.isDirectory()){
            boolean makeResult = path.mkdir();
            logger.info("path is not exist! and path is {} and create result is {}",path,makeResult);
        }
        File devicePath = new File(godeyeProperties.getScreenshotPath()+"/"+screenshotRequest.getDeviceId());
        if(!devicePath.exists() || !devicePath.isDirectory()){
            boolean makeResult = devicePath.mkdir();
            logger.info("devicePath is not exist! and devicePath is {} and create result is {}",devicePath,makeResult);
        }
        File userPath = new File(godeyeProperties.getScreenshotPath()+"/"+screenshotRequest.getDeviceId()+"/"+screenshotRequest.getUserId());
        if(!userPath.exists() || !userPath.isDirectory()){
            boolean makeResult =  userPath.mkdir();
            logger.info("userPath is not exist! and userPath is {} and create result is {}",userPath,makeResult);
        }
        List<String> command = ffmpegUtil.buildScreenshotCommand(screenshotRequest.getStreamPath()
                ,screenshotRequest.getDeviceId(),screenshotRequest.getUserId(),screenshotRequest.getResolution());
        String filename = command.get(command.size()-1);
        String nginxFilePath = filename.replace(godeyeProperties.getScreenshotPath(),godeyeProperties.getNginxShotPath());
        logger.info("filename is {} and nginxpath is {} and the screenshot command is {}",filename,nginxFilePath,JSON.toJSONString(command));
        try{
            //执行截图命令
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.command(command);
            builder.start();
        }catch(Exception e){
            e.printStackTrace();
        }
        CameraResponse result = new CameraResponse();
        result.setCode(Constants.CameraResponse.SUCCESS.getCode());
        result.setDesc(Constants.CameraResponse.SUCCESS.getDesc());
        result.setOther(nginxFilePath);
        return result;
    }



    //关闭播放窗口
    @RequestMapping(value = {"/getRecordFile"},method = {RequestMethod.POST})
    public CameraResponse getRecordFile(@RequestBody String request){
        logger.info("=======http request getRecordFile begin request is {} ===== ",request);
        PlayRequest playRequest = JSON.parseObject(request,PlayRequest.class);

        CameraResponse response = new CameraResponse();
        GstCameraReplayConfigDto replayConfigDto = new GstCameraReplayConfigDto();
        replayConfigDto.setCameraId(playRequest.getDeviceId());
        try {
            response.setCode(Constants.CameraResponse.SUCCESS.getCode());
            response.setDesc(Constants.CameraResponse.SUCCESS.getDesc());
            String prePath = Constants.PROTOCOL_HTTP+"://"+godeyeProperties.getSrsHost()+":"+godeyeProperties.getNginxProxyPort()+"/"+playRequest.getProvider()+"-"+playRequest.getDeviceId();
            List<String> fileList = this.m3U8Util.getAllMp4File(godeyeProperties.getTsBasePath()+"/"+playRequest.getProvider()+"-"+playRequest.getDeviceId(),prePath);
            response.setOther(fileList);
        } catch (Exception e) {
            logger.error("stopStream getRecordFile operate database occur an error!",e);
            response.setCode(Constants.CameraResponse.ERROR.getCode());
            response.setDesc(Constants.CameraResponse.ERROR.getDesc());
        }
        return response;
    }

    public static void main(String[] args) {

    }
}
