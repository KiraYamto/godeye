package com.xsmart.camera.controller;

import com.alibaba.fastjson.JSON;
import com.xsmart.camera.GodeyeProperties;
import com.xsmart.camera.http.CameraResponse;
import com.xsmart.camera.service.ReplayConfigService;
import com.xsmart.camera.service.SrsService;
import com.xsmart.camera.srs.v2.model.GstCameraReplayConfigDto;
import com.xsmart.camera.srs.v2.model.RePlayRequest;
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

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author tian.xubo
 * @created 2019 - 06 - 28 10:03
 */
@RestController
public class CameraController {

    @Autowired
    private ReplayConfigService replayConfigService;
    //录像计划

    @Autowired
    private GodeyeProperties godeyeProperties;
    @Autowired
    private M3U8Util m3U8Util;
    @Autowired
    private FfmpegUtil ffmpegUtil;
    @Autowired
    private SrsService srsService;


    private static final Logger logger = LoggerFactory.getLogger(CameraController.class);
    @RequestMapping(value = {"/callback/record"},method = {RequestMethod.POST})
    public String callbackRecord(@RequestBody String request){
        logger.info("callbackRecord begin and filename is {}",request);
        String result = "OK";
        return null;
    }
    @RequestMapping(value = {"/replay/stored"},method = {RequestMethod.GET})
    public CameraResponse stored(){
        CameraResponse cameraResponse = new CameraResponse();
        String date = this.ffmpegUtil.getNowDate();
        String tsFilePath = "/home/ztesoftai/IMChatService/srs/srs-2.0-r6/trunk/objs/nginx/html/live";
        this.m3U8Util.moveFileToOtherDir(tsFilePath,date);
        cameraResponse.setCode(Constants.CameraResponse.SUCCESS.getCode());
        cameraResponse.setDesc(Constants.CameraResponse.SUCCESS.getDesc());
        return cameraResponse;
    }

    @RequestMapping(value = {"/replay/set"},method = {RequestMethod.POST})
    public CameraResponse replaySet(@RequestBody String request, HttpServletRequest httpServletRequest){
        logger.info("replaySet begin and request is {}",request);
        CameraResponse cameraResponse = new CameraResponse();
        GstCameraReplayConfigDto replayConfigDto = JSON.parseObject(request,GstCameraReplayConfigDto.class);
        String tsFilePath = godeyeProperties.getTsBasePath()+"/replay-"+replayConfigDto.getProvider()+"-"+replayConfigDto.getCameraId();
        replayConfigDto.setTsFilePath(tsFilePath);
        try {
            GstCameraReplayConfigDto configExist = this.replayConfigService.queryReplayConfigByCameraId(replayConfigDto);
            if(configExist == null){
                this.replayConfigService.insertReplayConfig(replayConfigDto);
            }else {
                this.replayConfigService.updateReplayConfig(replayConfigDto);
            }
        } catch (Exception e) {
            logger.error("replaySet operate database occur an error!",e);
            cameraResponse.setCode(Constants.CameraResponse.ERROR.getCode());
            cameraResponse.setDesc(Constants.CameraResponse.ERROR.getDesc());
        }
        String outPutPath = "rtmp://"+godeyeProperties.getReplayHost()+":"+godeyeProperties.getSrsServerPort()+"/replay-"+replayConfigDto.getProvider()+"-"+replayConfigDto.getCameraId()+"/"+replayConfigDto.getCameraId();
        String PID = srsService.getPID(outPutPath);
        List<String> commands = null;
        if(Constants.ReplayType.ON.getType() == replayConfigDto.getReplayOn()) {
            //开启回放，推流
            try {
                commands = ffmpegUtil.buildPushStreamCommand(replayConfigDto.getRtspAddr(), replayConfigDto.getProvider(), replayConfigDto.getCameraId(), replayConfigDto.getVcodec(), false);
                if(PID == null){
                    //执行推流命令
                    ProcessBuilder builder = new ProcessBuilder(commands);
                    builder.command(commands);
                    builder.start();
                }
            } catch (Exception e) {
                logger.error("queryReplayConfigList execute commands occur an error { } command is{}!", e, commands);
            }
        }else{
            //关闭回放，关闭推流
            if(PID != null){
                logger.info("find replay process is {}",PID);
                srsService.closeLinuxProcess(PID);
            }

        }


        cameraResponse.setCode(Constants.CameraResponse.SUCCESS.getCode());
        cameraResponse.setDesc(Constants.CameraResponse.SUCCESS.getDesc());
        return cameraResponse;
    }

    @RequestMapping(value = {"/replay/broadcast"},method = {RequestMethod.POST})
    public CameraResponse broadcast(@RequestBody String request){
        logger.info("replay begin and request is {}",request);
        CameraResponse cameraResponse = new CameraResponse();
        RePlayRequest rePlayRequest = JSON.parseObject(request, RePlayRequest.class);
        String replayAddr = null;
        String m3u8Path = null;
        String directoryPath = null;
        String replayM3u8Path = null;
        if(rePlayRequest == null ){
            cameraResponse.setCode(Constants.CameraResponse.NULL_PARAMETER.getCode());
            cameraResponse.setDesc(Constants.CameraResponse.NULL_PARAMETER.getDesc());
            cameraResponse.setOther(replayAddr);
        }else{
            try {
                String today = ffmpegUtil.getNowDate();
                //当天的回放
                if(today.equals(rePlayRequest.getDate())){
                     m3u8Path = godeyeProperties.getTsBasePath()+"/replay-"+rePlayRequest.getProvider()+"-"+rePlayRequest.getDeviceId()+"/"+rePlayRequest.getDeviceId()+Constants.M3U8_SUFFIX;
                     directoryPath = godeyeProperties.getTsBasePath()+"/replay-"+rePlayRequest.getProvider()+"-"+rePlayRequest.getDeviceId();
                     replayM3u8Path = godeyeProperties.getTsBasePath()+"/replay-"+rePlayRequest.getProvider()+"-"+rePlayRequest.getDeviceId()+"/replay_"+rePlayRequest.getDeviceId()+Constants.M3U8_SUFFIX;
                     replayAddr = Constants.PROTOCOL_HTTP+"://"+godeyeProperties.getSrsHost()+"/replay-"+rePlayRequest.getProvider()+"-"+rePlayRequest.getDeviceId()+"/replay_"+rePlayRequest.getDeviceId()+Constants.M3U8_SUFFIX;
                }else{
                     m3u8Path = godeyeProperties.getTsBasePath()+"/replay-"+rePlayRequest.getProvider()+"-"+rePlayRequest.getDeviceId()+"/"+rePlayRequest.getDate()+"/"+rePlayRequest.getDeviceId()+Constants.M3U8_SUFFIX;
                     directoryPath = godeyeProperties.getTsBasePath()+"/replay-"+rePlayRequest.getProvider()+"-"+rePlayRequest.getDeviceId()+"/"+rePlayRequest.getDeviceId()+"/"+rePlayRequest.getDate();
                     replayM3u8Path = godeyeProperties.getTsBasePath()+"/replay-"+rePlayRequest.getProvider()+"-"+rePlayRequest.getDeviceId()+"/"+rePlayRequest.getDeviceId()+"/"+rePlayRequest.getDate()+"/replay_"+rePlayRequest.getDeviceId()+Constants.M3U8_SUFFIX;
                     replayAddr = Constants.PROTOCOL_HTTP+"://"+godeyeProperties.getSrsHost()+"/replay-"+rePlayRequest.getProvider()+"-"+rePlayRequest.getDeviceId()+"/"+rePlayRequest.getDeviceId()+"/"+rePlayRequest.getDate()+"/replay_"+rePlayRequest.getDeviceId()+Constants.M3U8_SUFFIX;
                }
                logger.info("m3u8Path is {} and replayM3u8Path is {} and replayAddr is {} ",m3u8Path,replayM3u8Path,replayAddr);
                boolean replayresult = m3U8Util.buildReplayM3U8File(directoryPath,m3u8Path,replayM3u8Path);
                if(!replayresult){
                    cameraResponse.setCode(Constants.CameraResponse.NOT_EXIST_FILEPATH.getCode());
                    cameraResponse.setDesc(Constants.CameraResponse.NOT_EXIST_FILEPATH.getDesc());
                }
            } catch (Exception e) {
                logger.error("buildReplayM3U8File occur an error ! maybe m3u8 file not found!",e);
                cameraResponse.setCode(Constants.CameraResponse.ERROR.getCode());
                cameraResponse.setDesc(Constants.CameraResponse.ERROR.getDesc());
            }
        }
        cameraResponse.setCode(Constants.CameraResponse.SUCCESS.getCode());
        cameraResponse.setDesc(Constants.CameraResponse.SUCCESS.getDesc());
        cameraResponse.setOther(replayAddr);
        return cameraResponse;
    }


    public  String excute(String[] cmds){
        try{
            //执行命令
            Runtime.getRuntime().exec(cmds);
        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
        return null;
    }
    public  String excuteStr(String cmds){
        Process process = null;
        BufferedReader reader =null;
        try{
            //执行命令
            //杀掉进程
            process = Runtime.getRuntime().exec(cmds);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            logger.info("execute cmd result :",line);
            System.out.println(line);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            process.destroy();
        }
        return null;
    }
}
