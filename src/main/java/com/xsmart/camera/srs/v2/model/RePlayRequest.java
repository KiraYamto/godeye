package com.xsmart.camera.srs.v2.model;

import java.io.Serializable;

/**
 * @author tian.xubo
 * @created 2019 - 07 - 19 13:33
 */
public class RePlayRequest implements Serializable {

    private String streamPath;
    private String provider;
    private String deviceId;
    private String date;



    public String getStreamPath() {
        return streamPath;
    }

    public void setStreamPath(String streamPath) {
        this.streamPath = streamPath;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
