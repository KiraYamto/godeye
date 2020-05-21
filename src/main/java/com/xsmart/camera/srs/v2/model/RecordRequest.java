package com.xsmart.camera.srs.v2.model;

import java.io.Serializable;

/**
 * @author tian.xubo
 * @created 2019 - 07 - 09 9:46
 */
public class RecordRequest implements Serializable {
    private String streamPath;
    private String provider;
    private String deviceId;
    private String startMoment;
    private String finishMoment;
    private String userId;
    private int recordType;
    private String date;
    private String warnId;

    public String getWarnId() {
        return warnId;
    }

    public void setWarnId(String warnId) {
        this.warnId = warnId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

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

    public String getStartMoment() {
        return startMoment;
    }

    public void setStartMoment(String startMoment) {
        this.startMoment = startMoment;
    }

    public String getFinishMoment() {
        return finishMoment;
    }

    public void setFinishMoment(String finishMoment) {
        this.finishMoment = finishMoment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRecordType() {
        return recordType;
    }

    public void setRecordType(int recordType) {
        this.recordType = recordType;
    }
}
