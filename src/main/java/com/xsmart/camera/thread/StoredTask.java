package com.xsmart.camera.thread;

import com.xsmart.camera.GodeyeProperties;
import com.xsmart.camera.service.*;
import com.xsmart.camera.srs.v2.model.ClientResponse;
import com.xsmart.camera.srs.v2.model.GstCameraReplayConfigDto;
import com.xsmart.camera.srs.v2.model.SrsClient;
import com.xsmart.camera.util.BeanFactory;
import com.xsmart.camera.util.FfmpegUtil;
import com.xsmart.camera.util.M3U8Util;
import com.xsmart.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * @author tian.xubo
 * @created 2019 - 07 - 03 15:58
 */
public class StoredTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(StoredTask.class);
    //记录上一次执行任务的时间
    private static String lastTime = null;

    private ReplayConfigService replayConfigService;
    private SrsService srsService;
    private GodeyeProperties godeyeProperties;
    private FfmpegUtil ffmpegUtil;
    private M3U8Util m3U8Util;
    public StoredTask() {
         replayConfigService = BeanFactory.lookUp(ReplayConfigServiceImpl.class);
         srsService = BeanFactory.lookUp(SrsServiceImpl.class);
         godeyeProperties = BeanFactory.lookUp(GodeyeProperties.class);
         ffmpegUtil = BeanFactory.lookUp(FfmpegUtil.class);
         m3U8Util = BeanFactory.lookUp(M3U8Util.class);
    }

    @Override
    public void run() {
        logger.info("begin run stored task now is {}",ffmpegUtil.timeFormatForString(new Date()));
        List<GstCameraReplayConfigDto> list = null;
        try {
            list = replayConfigService.queryReplayConfigList();
            if(list == null){
                logger.info("no camera open replay function!");
                return;
            }

            for(GstCameraReplayConfigDto dto:list){
                //如果回放开启了，看下是否被意外终止了推流，如果推流进程不存在，则重新开启。
                if(Constants.ReplayType.ON.getType() == dto.getReplayOn()){
                    //检查是否已经开启了推流
                    String outPutPath = "rtmp://"+godeyeProperties.getReplayHost()+":"+godeyeProperties.getSrsServerPort()+"/replay-"+dto.getProvider()+"-"+dto.getCameraId()+"/"+dto.getCameraId();

                    String PID = srsService.getPID(outPutPath);
                    logger.info("{} has open replay function,and replay process is {} and output path is {}",dto.getCameraId(),PID,outPutPath);
                    if(PID == null){
                        //启动推流
                        List<String> command = ffmpegUtil.buildPushStreamCommand(dto.getRtspAddr(),dto.getProvider(),dto.getCameraId(),dto.getVcodec(),false);
                        try {
                            //调用线程命令进行转码
                            ProcessBuilder builder = new ProcessBuilder(command);
                            builder.command(command);
                            builder.start();
                        } catch (Exception e) {
                            logger.error("startPushStream occur an error",e);
                        }
                    }else {
                        //如果已经跨天 当前时间的下一天跟LastTime的时间相比，不是一样，否则是相同
                        //比如0725的下一天是2019-07-26 00:00:00，如果跨天了则是2019-07-27 00:00:00
                        //
                        if(lastTime == null){
                            //第一次执行任务，程序刚刚启动
                            continue;
                        }
                        String lastTimeNextDate = ffmpegUtil.getNextDateStartByDate(lastTime);
                        String todayNextDate = ffmpegUtil.getNextDateStart();
                        logger.info("lastTimeNextDate is {} and  is today next date is {}",lastTimeNextDate,todayNextDate);
                        if(!lastTimeNextDate.equals(todayNextDate)){
                            //跨天 关闭推流
                            srsService.closeLinuxProcess(PID);
                            //转储
                            m3U8Util.moveFileToOtherDir(dto.getTsFilePath(),ffmpegUtil.getDateStr(lastTime));
                            //重新开
                            List<String> command = ffmpegUtil.buildPushStreamCommand(dto.getRtspAddr(),dto.getProvider(),dto.getCameraId(),dto.getVcodec(),false);
                            try {
                                //调用线程命令进行转码
                                ProcessBuilder builder = new ProcessBuilder(command);
                                builder.command(command);
                                builder.start();
                            } catch (Exception e) {
                                logger.error("startPushStream occur an error",e);
                            }

                        }
                        //如果进程存在，在判定是否到了凌晨转储时间，如果是，则停掉当前推流，转储之后新打开一个推流
                    }

                }
                String outPutPathForOut = "rtmp://"+godeyeProperties.getSrsHost()+":"+godeyeProperties.getSrsServerOutPort()+"/"+dto.getProvider()+"-"+dto.getCameraId()+"/"+dto.getCameraId();
                String outPutPathForIn = "rtmp://"+godeyeProperties.getInnerHost()+":"+godeyeProperties.getSrsServerPort()+"/"+dto.getProvider()+"-"+dto.getCameraId()+"/"+dto.getCameraId();

                String cameraTcUrlForOut = "rtmp://"+godeyeProperties.getSrsHost()+":"+godeyeProperties.getSrsServerOutPort()+"/"+dto.getProvider()+"-"+dto.getCameraId();
                String cameraTcUrlForIn = "rtmp://"+godeyeProperties.getInnerHost()+":"+godeyeProperties.getSrsServerPort()+"/"+dto.getProvider()+"-"+dto.getCameraId();

                String PIDForOut = srsService.getPID(outPutPathForOut);
                String PIDForIn = srsService.getPID(outPutPathForIn);
                String PID = PIDForIn != null?PIDForIn:PIDForOut;
                String cameraTcUrl = PIDForIn != null?cameraTcUrlForIn:cameraTcUrlForOut;

                logger.info("find camera : {} stream process is {}",cameraTcUrl,PID);
                if(PID != null){
                    //PID存在，但是推流客户端不存在
                    ClientResponse response = srsService.getClients();
                    List<SrsClient> clientList = response.getClients();
                    boolean cameraAlive = false;
                    boolean viewer = false;//观众
                    for(SrsClient client:clientList){
                        if(client.getTcUrl().equals(cameraTcUrlForIn)){
                            cameraAlive = true;
                        }else if(client.getTcUrl().equals(cameraTcUrlForOut)){
                            viewer = true;
                        }
                    }
                    if(!cameraAlive){
                        //如果客户端掉线，但是进程还在，要把进程杀掉。重新推流
                        srsService.closeLinuxProcess(PID);
                        //String m3u8Dir = godeyeProperties.getTsBasePath()+"/"+dto.getProvider()+"-"+dto.getCameraId();
                       // m3U8Util.removeAllFile(m3u8Dir);
                        logger.info("camera client is disconnect,and the procees is colsed,stream will be started soon ,{}",cameraTcUrl);
                        //启动推流
                        List<String> command = ffmpegUtil.buildPushStreamCommand(dto.getRtspAddr(),dto.getProvider(),dto.getCameraId(),dto.getVcodec(),true);
                        try {
                            //调用线程命令进行转码
                            ProcessBuilder builder = new ProcessBuilder(command);
                            builder.command(command);
                            builder.start();
                        } catch (Exception e) {
                            logger.error("startPushStream occur an error",e);
                        }
                    }
                    if(cameraAlive&&!viewer){
                        //进程存在，但是没有观众，停止推流
                        logger.info("camera client is alive but no player ,close stream {}",cameraTcUrl);
                        srsService.closeLinuxProcess(PID);
                    }
                }else{
                    //不存在进程，就不存在推流客户端--isPublish==true，
                    ClientResponse response = srsService.getClients();
                    List<SrsClient> clientList = response.getClients();
                    boolean viewing = false;
                    for(SrsClient client:clientList){
                        if(client.getTcUrl().equals(cameraTcUrl)){
                            //有观众
                            viewing = true;
                            break;
                        }
                    }
                    if(viewing){
                        //启动推流
                        logger.info("stream is not exist but somebody is viewing,process will be started soon {}",cameraTcUrl);
                        List<String> command = ffmpegUtil.buildPushStreamCommand(dto.getRtspAddr(),dto.getProvider(),dto.getCameraId(),dto.getVcodec(),true);
                        try {
                            //调用线程命令进行转码
                            ProcessBuilder builder = new ProcessBuilder(command);
                            builder.command(command);
                            builder.start();
                        } catch (Exception e) {
                            logger.error("startPushStream occur an error",e);
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error("run stored task queryReplayConfigList occur an  error! ",e);
        }
        lastTime = ffmpegUtil.timeFormatForString(new Date());
        logger.info("end run stored task now is {}",lastTime);
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
}
