package com.xsmart.camera.util;

import com.alibaba.fastjson.JSON;
import com.xsmart.camera.GodeyeProperties;
import com.xsmart.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author tian.xubo
 * @created 2019 - 07 - 05 16:34
 */
@Component
public class FfmpegUtil {

    private static final Logger logger = LoggerFactory.getLogger(FfmpegUtil.class);
    @Autowired
    private GodeyeProperties godeyeProperties;

    public List<String> buildPushStreamCommand(String streamPath, String provider, String deviceId,String vcodec,boolean live){
        List<String> command = new ArrayList<String>();
        if(Constants.OS.LINUX.getValue().equals(godeyeProperties.getServerOs())){
            command.add(godeyeProperties.getFfmpegLinuxPath());
        }else{
            command.add(godeyeProperties.getFfmpegWinPath());
        }
        //root/godeye/ffmpeg-4.1.3/ffmpeg -i rtsp://admin:gst.123456@192.168.31.64:554/Streaming/Channels/101 -rtsp_transport tcp -vcodec h264 -acodec aac -f flv rtmp://172.21.64.133:1935/hikvision/101/
        //rtmp://172.21.64.133:1935/hikvision/101/2019-07-18

        ///root/godeye/ffmpeg-4.1.3/ffmpeg -re -i /root/godeye/twopiece.mp4 -vcodec copy -acodec copy -f flv -y rtmp://172.21.64.133:1935/live2/twopiece
        String outPutPath = null;
        if(live){
             outPutPath = "rtmp://"+godeyeProperties.getLiveHost()+":"+godeyeProperties.getSrsServerPort()+"/"+provider+"-"+deviceId+"/"+deviceId;
        }else{
             outPutPath = "rtmp://"+godeyeProperties.getReplayHost()+":"+godeyeProperties.getSrsServerPort()+"/replay-"+provider+"-"+deviceId+"/"+deviceId;
        }
        if(streamPath.contains("test")){
            //模拟流 读取文件推流需要这个参数，直接转发rstp则不需要
            command.add("-re");
            command.add("-i");
            //测试文件
            if(streamPath.equals("testone")){
                streamPath = godeyeProperties.getTestFileOne();
            }else if(streamPath.equals("testtwo")){
                streamPath = godeyeProperties.getTestFileTwo();
            }
            command.add(streamPath);
            command.add("-vcodec");
            command.add("copy");
            command.add("-acodec");
            command.add("copy");
            command.add("-f");
            command.add("flv");
            command.add("-y");
            command.add(outPutPath);
        } else {
            //ffmpeg  -re -rtsp_transport tcp -i rtsp://admin:gst.123456@192.168.1.64:554/Streaming/Channels/102
            // -c:v libx264 -ar 44100 -c:a aac -f flv -y rtmp://ge.livestream.com:1935/hikvision-150001/150001
            command.add("-re");
            command.add("-rtsp_transport");
            command.add("tcp");
            command.add("-i");
            command.add(streamPath);
            command.add("-c:v");
            command.add("libx264");
            command.add("-ar");//音频采样率
            command.add("44100");
            command.add("-c:a");
            command.add("aac");
            command.add("-f");
            command.add("flv");
            command.add("-y");
            command.add(outPutPath);
        }
        return command;

    }

    public List<String> buildScreenshotCommand(String streamPath, String deviceId,String userId,String resolution) {

        List<String> command = new ArrayList<String>();
        if (Constants.OS.LINUX.getValue().equals(godeyeProperties.getServerOs())) {
            command.add(godeyeProperties.getFfmpegLinuxPath());
        } else {
            command.add(godeyeProperties.getFfmpegWinPath());
        }
        //ffmpeg -i test.asf -y -f image2 -t 0.001 -s 352x240 a.jpg
        //ffmpeg -i rtsp://admin:gst.123456@192.168.1.64:554/Streaming/Channels/101 -y -f mjpeg -t 0.001 -s 1920x1080 bobo.jpg
        ///home/ztesoftai/IMChatService/ffmpeg/ffmpeg-4.1.3/ffmpeg -i /home/ztesoftai/IMChatService/srs/srs-2.0-r6/video/onepiece.mp4 -f -y
        Date date = new Date();
        String fileName = userId+"."+timeSecondFormatForString(date)+".jpg";
        String outPutPath = godeyeProperties.getScreenshotPath()+"/"+deviceId+"/"+userId+"/"+fileName;
        //模拟流 读取文件推流需要这个参数，直接转发rstp则不需要
        if (streamPath.equals("testone")) {
            streamPath = godeyeProperties.getTestFileOne();
        } else if (streamPath.equals("testtwo")) {
            streamPath = godeyeProperties.getTestFileTwo();
        }
        command.add("-i");
        command.add(streamPath);
        command.add("-y");
        command.add("-f");
        command.add("image2");
        command.add("-t");
        command.add("0.001");
        command.add("-s");
        command.add(resolution);
        command.add(outPutPath);
        return command;
    }

    public List<String> buildConcatCommand(String playListPath,String outputPath,String deviceId){
        //ffmpeg -f concat -safe 0 -i mylist.txt -c copy output.mp4
        //http://172.21.64.133:1935/hikvision/101.m3u8
        // playListPath = home/ztesoftai/IMChatService/srs/srs-2.0-r6/trunk/objs/nginx/html/live/playlist.txt
       //outputPath = home/ztesoftai/IMChatService/srs/srs-2.0-r6/trunk/objs/nginx/html/live/
        //home/ztesoftai/IMChatService/ffmpeg/ffmpeg-4.1.3/ffmpeg -f concat -safe 0 -i playlist.txt -c copy onepiece.mp4
        List<String> command = new ArrayList<String>();
        if (Constants.OS.LINUX.getValue().equals(godeyeProperties.getServerOs())) {
            command.add(godeyeProperties.getFfmpegLinuxPath());
        } else {
            command.add(godeyeProperties.getFfmpegWinPath());
        }
        String outputFile = outputPath+"/"+deviceId+"-"+timeFormatForString(new Date())+Constants.MP4_SUFFIX;
        command.add("-f");
        command.add("concat");
        command.add("-safe");
        command.add("0");
        command.add("-i");
        command.add(playListPath);
        command.add("-c");
        command.add("copy");
        command.add(outputFile);
        return command;
    }
    public Date timeFormatForDate(String time){
        //2019-08-06-01:08:42
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(time);
        } catch (ParseException e) {
            logger.error("time format occur an error time is {}!",time,e);
        }

        return date;
    }
    public  String timeFormatForString(Date date){
        String res = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            res = simpleDateFormat.format(date);
        } catch (Exception e) {
            logger.error("time format occur an error time is {}!",date,e);
        }

        return res;
    }
    public  String timeSecondFormatForString(Date date){
        String res = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        try {
            res = simpleDateFormat.format(date);
        } catch (Exception e) {
            logger.error("time format occur an error time is {}!",date,e);
        }

        return res;
    }
    public  String timeFormatForString(long timestamp){
        String res = null;
        Date date = new Date(timestamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            res = simpleDateFormat.format(date);
        } catch (Exception e) {
            logger.error("time format occur an error time is {}!",date,e);
        }
        return res;
    }
    public  String getNowDate(){
        String res = null;
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            res = simpleDateFormat.format(date);
        } catch (Exception e) {
            logger.error("time format occur an error time is {}!",date,e);
        }
        return res;
    }
    public  String getDateStr(String dateStr){
        String res = null;
        Date date = timeFormatForDate(dateStr);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            res = simpleDateFormat.format(date);
        } catch (Exception e) {
            logger.error("time format occur an error time is {}!",date,e);
        }
        return res;
    }
    public  String getNextDateStart(){
        String res = null;
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        try {
            res = simpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            logger.error("time format  getNextDateStart occur an error time is {}!",date,e);
        }
        return res;
    }
    public  String getNextDateStartByDate(String lastTime){
        String res = null;
        Date date = timeFormatForDate(lastTime);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        try {
            res = simpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            logger.error("time format  getNextDateStart occur an error time is {}!",date,e);
        }
        return res;
    }

    public List<String> buildStoredCommands(String date,String filePath,String deviceId){
        List<String> command = new ArrayList<>();
        command.add("cd");
        command.add(filePath);
        command.add("mkdir");
        command.add(date);
        command.add("mv");
        command.add(deviceId+"*");
        command.add(date);
        return command;
    }
    public static void main(String[] args) {
        FfmpegUtil ffmpegUtil = new FfmpegUtil();
      //  System.out.println(ffmpegUtil.getNextDateStart());
        try {
            Runtime.getRuntime().exec("D:\\");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
