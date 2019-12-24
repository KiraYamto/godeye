package com.xsmart.camera.thread;

import com.xsmart.camera.GodeyeProperties;
import com.xsmart.camera.service.SrsService;
import com.xsmart.camera.srs.v2.model.RecordRequest;
import com.xsmart.camera.util.FfmpegUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author tian.xubo
 * @created 2019 - 07 - 05 10:37
 */
public class RecordTask implements Callable {

    private static final Logger logger = LoggerFactory.getLogger(RecordTask.class);
    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    private SrsService srsService;

    private FfmpegUtil ffmpegUtil;

    private boolean stop;

    private RecordRequest request;

    private GodeyeProperties godeyeProperties;
    public RecordTask(RecordRequest request, SrsService srsService, FfmpegUtil ffmpegUtil, GodeyeProperties godeyeProperties, boolean stop) {
        this.srsService = srsService;
        this.ffmpegUtil = ffmpegUtil;
        this.stop = stop;
        this.request = request;
        this.godeyeProperties = godeyeProperties;

    }

    @Override
    public Object call() throws Exception {
        List<String> command = ffmpegUtil.buildPushStreamCommand(request.getStreamPath(),request.getProvider()+"_record",request.getUserId(),null,false);
        if(stop){
            String outPutPath = "rtmp://"+godeyeProperties.getSrsHost()+":"+godeyeProperties.getSrsServerPort()+"/"+request.getProvider()+"_record/"+request.getUserId();
            String PID =  srsService.getPID(outPutPath);
            logger.info("record task execute stop command and kill process {}",PID);
            if(PID != null){
                srsService.closeLinuxProcess(PID);
            }
        }else {
            logger.info("record task execute start command ");
            try {
                //调用线程命令进行转码
                ProcessBuilder builder = new ProcessBuilder(command);
                builder.command(command);
                builder.start();
            } catch (Exception e) {
                logger.error("record start task occur an error",e);
            }
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
