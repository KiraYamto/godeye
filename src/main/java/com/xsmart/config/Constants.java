package com.xsmart.config;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author tian.xubo
 * @created 2019 - 07 - 01 17:31
 */
public class Constants {

    public static final String TS_SUFFIX = ".ts";
    public static final String M3U8_SUFFIX = ".m3u8";
    public static final String MP4_SUFFIX = ".mp4";
    public static final String CHARSET_UTF8 = "UTF-8";
    public static final String CONCAT_FILE_NAME = "playlist.txt";
    public static final String REPLAY_FILE_NAME = "replay.m3u8";
    public static final String M3U8_TARGETDURATION_LABEL = "#EXT-X-TARGETDURATION";
    public static final String M3U8_VOD_LABEL = "#EXT-X-PLAYLIST-TYPE:VOD";
    public static final String M3U8_EXTINF_LABEL = "#EXTINF";
    public static final String M3U8_ENDLIST_LABEL = "#EXT-X-ENDLIST";
    public static final String PROTOCOL_HTTP = "http";
    public static final String VODEC_H264 = "h264";
    public static final String VODEC_H265 = "h265";

    public static final int INTERVAL_TASK_EXECUTE = 30 ;

    public enum OS{
        LINUX("linux"),WINDOWS("windows");
        private String value;
        public String getValue(){
            return value;
        }
        private OS(String value){
            this.value = value;
        }
    }
    public enum CameraResponse{
        SUCCESS(200,"success"),SUCCESS_NO_OPERATION(203,"do nothing"),
        BUSY(409,"server busy,retry after a later"),
        NULL_PARAMETER(405," parameter not allowed null!"),
        NOT_EXIST_FILEPATH(406," FILEPATH is not exists!"),

        ERROR(500,"server occur an error!");
        private int code;
        private String desc;
        public String getDesc(){
            return desc;
        }
        public int getCode(){
            return code;
        }
        private CameraResponse(int code ,String desc){
            this.desc = desc;
            this.code = code;
        }
    }
    public enum ReplayType{
        OFF(0,"不开启回放"),
        ON(1,"开启回放");
        private int type;
        private String desc;
        ReplayType(int type,String desc){
            this.type = type;
            this.desc = desc;
        }
       public   int getType() {
            return type;
        }

         void setType(int type) {
            this.type = type;
        }

        public String getDesc() {
            return desc;
        }
    }

}
