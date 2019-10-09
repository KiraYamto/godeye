package com.xsmart.camera.thread;

import com.xsmart.camera.GodeyeProperties;
import com.xsmart.camera.srs.v2.model.RecordRequest;
import com.xsmart.camera.util.FfmpegUtil;
import com.xsmart.camera.util.M3U8Util;
import com.xsmart.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
    public TsRecordTask(RecordRequest recordRequest,GodeyeProperties godeyeProperties,M3U8Util m3U8Util, FfmpegUtil ffmpegUtil, String startFile) {
        this.m3U8Util = m3U8Util;
        this.ffmpegUtil = ffmpegUtil;
        this.startFile = startFile;
        this.recordRequest = recordRequest;
        this.godeyeProperties = godeyeProperties;

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
        logger.info("excute commands is {}",commands.toString());
        try {
            //调用线程命令进行转码
            ProcessBuilder builder = new ProcessBuilder(commands);
            builder.command(commands);
            builder.start();
        } catch (Exception e) {
            logger.error("concat file occur an error",e);
        }
    }
}
