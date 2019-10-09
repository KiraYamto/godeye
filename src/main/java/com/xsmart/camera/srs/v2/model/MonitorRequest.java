package com.xsmart.camera.srs.v2.model;

import java.io.Serializable;

/**
 * @author tian.xubo
 * @created 2019 - 09 - 27 9:47
 */
public class MonitorRequest implements Serializable {
  /*  /serviceManager/videoAiWarn
    {
        "videoId":"摄像头ID",
            "alertType":"告警类型（有几种？)",
            "happendTime":"YYYY-mm-dd HH:mi:ss",
            "imgPath":"图片路径"
    }*/
    private String videoId;
    private String alertType;
    private String happendTime;
    private String imgPath;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getHappendTime() {
        return happendTime;
    }

    public void setHappendTime(String happendTime) {
        this.happendTime = happendTime;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
