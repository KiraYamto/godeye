package com.xsmart.camera.srs.v2.model;

import java.io.Serializable;

/**
 * @author tian.xubo
 * @created 2019 - 07 - 18 10:18
 */
public class GstCameraReplayConfigDto implements Serializable {
    private long configId;
    private String cameraId;
    private int replayOn;
    private String provider;
    private String rtspAddr;
    private String tsFilePath;
    private String vcodec;
    private int player;
    private String monitorFilePath;

    public String getMonitorFilePath() {
        return monitorFilePath;
    }

    public void setMonitorFilePath(String monitorFilePath) {
        this.monitorFilePath = monitorFilePath;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public String getVcodec() {
        return vcodec;
    }

    public void setVcodec(String vcodec) {
        this.vcodec = vcodec;
    }



    public String getTsFilePath() {
        return tsFilePath;
    }

    public void setTsFilePath(String tsFilePath) {
        this.tsFilePath = tsFilePath;
    }

    public long getConfigId() {
        return configId;
    }

    public void setConfigId(long configId) {
        this.configId = configId;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public int getReplayOn() {
        return replayOn;
    }

    public void setReplayOn(int replayOn) {
        this.replayOn = replayOn;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getRtspAddr() {
        return rtspAddr;
    }

    public void setRtspAddr(String rtspAddr) {
        this.rtspAddr = rtspAddr;
    }
}
