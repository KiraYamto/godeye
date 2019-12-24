package com.xsmart.camera.service;

import com.xsmart.camera.GodeyeProperties;
import com.xsmart.camera.srs.v2.model.GstCameraReplayConfigDto;
import com.xsmart.camera.thread.FileMonitorTask;
import com.xsmart.camera.thread.StoredTask;
import com.xsmart.camera.util.FfmpegUtil;
import com.xsmart.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author tian.xubo
 * @created 2019 - 07 - 24 14:27
 */
@Component
public class CameraServiceImpl implements CameraService{

    private static final Logger logger = LoggerFactory.getLogger(CameraServiceImpl.class);

    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newScheduledThreadPool(10);

    @Autowired
    private ReplayConfigService replayConfigService;

    //@Autowired
    private GodeyeProperties godeyeProperties;
    @Autowired
    private FfmpegUtil ffmpegUtil;

    @Override
    public void initAllCamera() {
        logger.info("begin init all camera");
        try {
            List<GstCameraReplayConfigDto> list = this.replayConfigService.queryReplayConfigList();
            if(list == null){
                return;
            }
            for(GstCameraReplayConfigDto dto:list){
                //如果回放开启了，初始化打开推流命令。
                if(Constants.ReplayType.ON.getType() == dto.getReplayOn()){
                    logger.info("camera {} replay is on,and start push stream and stored task ",dto.getCameraId());
                     List<String> commands =  ffmpegUtil.buildPushStreamCommand(dto.getRtspAddr(),dto.getProvider(),dto.getCameraId(),dto.getVcodec(),false);
                     try{
                            //执行推流命令
                            ProcessBuilder builder = new ProcessBuilder(commands);
                            builder.command(commands);
                            builder.start();
                        }catch(Exception e){
                            logger.error("queryReplayConfigList execute commands occur an error { } command is{}!",e,commands);
                        }
                }
                //启动定时任务，到期执行转储任务，并且如果推流给关闭，重新拉起
            }
            StoredTask task = new StoredTask();
            SCHEDULED_EXECUTOR_SERVICE.scheduleWithFixedDelay(task,Constants.INTERVAL_TASK_EXECUTE,Constants.INTERVAL_TASK_EXECUTE, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("queryReplayConfigList occur an error!",e);
        }
        //增加监控
        FileMonitorTask fileMonitorTask = new FileMonitorTask();
        SCHEDULED_EXECUTOR_SERVICE.scheduleWithFixedDelay(fileMonitorTask,Constants.INTERVAL_TASK_EXECUTE,60, TimeUnit.SECONDS);

    }

    public void hasStartedPushStream(GstCameraReplayConfigDto dto){
        String outPutPath = "rtmp://"+godeyeProperties.getSrsHost()+":"+godeyeProperties.getSrsServerPort()+"/"+dto.getProvider()+"/"+dto.getCameraId();
       // String PID = srsService.getPID(outPutPath);

    }
}
