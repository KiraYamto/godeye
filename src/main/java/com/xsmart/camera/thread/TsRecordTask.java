package com.xsmart.camera.thread;

import com.xsmart.camera.GodeyeProperties;
import com.xsmart.camera.srs.v2.model.RecordRequest;
import com.xsmart.camera.util.FfmpegUtil;
import com.xsmart.camera.util.M3U8Util;
import com.xsmart.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tian.xubo
 * @created 2019 - 07 - 18 16:37
 */
public class TsRecordTask implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(TsRecordTask.class);
    private M3U8Util m3U8Util;
    private FfmpegUtil ffmpegUtil;
    private String startFile;
    private RecordRequest recordRequest;
    private GodeyeProperties godeyeProperties;
    private RestTemplate restTemplate;
    public TsRecordTask(RecordRequest recordRequest, GodeyeProperties godeyeProperties, M3U8Util m3U8Util, FfmpegUtil ffmpegUtil, String startFile,RestTemplate restTemplate) {
        this.m3U8Util = m3U8Util;
        this.ffmpegUtil = ffmpegUtil;
        this.startFile = startFile;
        this.recordRequest = recordRequest;
        this.godeyeProperties = godeyeProperties;
        this.restTemplate = restTemplate;

    }

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
    @Override
    public void run() {
        // 直播地址/usr/local/srs/objs/nginx/html/hikvision-1500001/150001.m3u8
        //回放地址/usr/local/srs/objs/nginx/html/replay-hikvision-1500001/2019-08-27/150001.m3u8
        String playlistPath = null;
        String m3u8Path = null;
        String tsFilePath = null;
        if(recordRequest.getDate() == null){
            playlistPath = godeyeProperties.getTsBasePath()+"/"+recordRequest.getProvider()+"-"+recordRequest.getDeviceId()+"/"+Constants.CONCAT_FILE_NAME;
            m3u8Path = godeyeProperties.getTsBasePath()+"/"+recordRequest.getProvider()+"-"+recordRequest.getDeviceId()+"/"+recordRequest.getDeviceId()+Constants.M3U8_SUFFIX;
            tsFilePath = godeyeProperties.getTsBasePath()+"/"+recordRequest.getProvider()+"-"+recordRequest.getDeviceId();
        }else {
            //---
            playlistPath = godeyeProperties.getTsBasePath()+"/replay-"+recordRequest.getProvider()+"-"+recordRequest.getDeviceId()+"/"+recordRequest.getDate()+"/"+Constants.CONCAT_FILE_NAME;
            m3u8Path = godeyeProperties.getTsBasePath()+"/replay-"+recordRequest.getProvider()+"-"+recordRequest.getDeviceId()+"/"+recordRequest.getDate()+"/"+recordRequest.getDeviceId()+Constants.M3U8_SUFFIX;
            tsFilePath = godeyeProperties.getTsBasePath()+"/replay-"+recordRequest.getProvider()+"-"+recordRequest.getDeviceId()+"/"+recordRequest.getDate();
        }
        logger.info("playlistPath is {} ,\r\n and m3u8path is {}\r\n tsFilePath is {}\r\n",playlistPath,m3u8Path,tsFilePath);
        m3U8Util.buildConcatListOther(startFile,playlistPath,m3u8Path,tsFilePath);
        List<String> commands = ffmpegUtil.buildConcatCommand(playlistPath,tsFilePath,recordRequest.getDeviceId());
        String filename = commands.get(commands.size()-1);
        logger.info("excute commands is {}",commands.toString());
        try {
            //调用线程命令进行转码
            ProcessBuilder builder = new ProcessBuilder(commands);
            builder.command(commands);
            builder.start();
        } catch (Exception e) {
            logger.error("concat file occur an error    ",e);
        }
        try{
           /* 参数：
            {
                "type": "回调类型：SCREENSHOT(截图回调)/RECORDING(录像回调)",
                    "warnId": "警情ID",
                    "deviceId": "摄像头ID",
                    "url": "截图／录像文件可访问URL"
            }*/
            String prePath = Constants.PROTOCOL_HTTP+"://"+godeyeProperties.getSrsHost()+":"+godeyeProperties.getNginxProxyPort()+"/"+recordRequest.getProvider()+"-"+recordRequest.getDeviceId();
            String[] filenameArr = filename.split("/");
            String url = prePath+"/"+filenameArr[filenameArr.length-1];
            Map<String,String> paraMap = new HashMap<>();
            paraMap.put("type",Constants.CallbackType.RECORDING.getType());
            paraMap.put("warnId",recordRequest.getWarnId());
            paraMap.put("deviceId",recordRequest.getDeviceId());
            paraMap.put("url",url);
            logger.info("screenshot callback request is {}",paraMap);
            String result = this.restTemplate.postForObject(godeyeProperties.getScreenshotCallback(),paraMap,String.class);
            logger.info("TsRecord callback result is {}",result);
        }catch (Exception e){
            logger.error("TsRecord callback occur an error! err info is {}",e);
        }
    }
}
