package com.xsmart.camera;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author tian.xubo33
 * @created 2019 - 07 - 01 15:38
 */
//@Component
//@Configuration
@PropertySource("classpath:config/godeye.properties")
public class GodeyePropertiesBase {
    @Value("${srs.host}")
    private String srsHost;
    @Value("${srs.inner.host}")
    private String innerHost;
    @Value("${srs.livestream.host}")
    private String liveHost;
    @Value("${srs.replaystream.host}")
    private String replayHost;
    @Value("${srs.server.port}")
    private String srsServerPort;
    @Value("${srs.server.out.port}")
    private String srsServerOutPort;
    @Value("${srs.http.port}")
    private String srsHttpPort;

    @Value("${srs.api.v1.versions}")
    private String versions;
    @Value("${srs.api.v1.summaries}")
    private String summaries;
    @Value("${srs.api.v1.rusages}")
    private String rusages;
    @Value("${srs.api.v1.self_proc_stats}")
    private String selfProcStats;
    @Value("${srs.api.v1.system_proc_stats}")
    private String systemProcStats;
    @Value("${srs.api.v1.meminfos}")
    private String meminfos;
    @Value("${srs.api.v1.authors}")
    private String authors;
    @Value("${srs.api.v1.features}")
    private String features;
    @Value("${srs.api.v1.requests}")
    private String requests;
    @Value("${srs.api.v1.vhosts}")
    private String vhosts;
    @Value("${srs.api.v1.streams}")
    private String streams;
    @Value("${srs.api.v1.clients}")
    private String clients;
    @Value("${srs.api.v1.configs}")
    private String configs;
    @Value("${server.os}")
    private String serverOs;
    @Value("${ffmpeg.window.path}")
    private String ffmpegWinPath;
    @Value("${ffmpeg.linux.path}")
    private String ffmpegLinuxPath;
    @Value("${ffmpeg.output.path}")
    private String ffpmegOutputPath;
    @Value("${ffmpeg.file.testone}")
    private String testFileOne;
    @Value("${ffmpeg.file.testtwo}")
    private String testFileTwo;
    @Value("${north.http.record.callback}")
    private String recordCallback;
    @Value("${ffmpeg.screenshot.store.path}")
    private String screenshotPath;
    @Value("${ts.base.path}")
    private String tsBasePath;
    @Value("${nginx.screenshot.pre}")
    private String nginxShotPath;
    @Value("${nginx.livestream.pre}")
    private String nginxLivePath;
    @Value("${nginx.proxy.port}")
    private String nginxProxyPort;
    @Value("${monitor.file.path}")
    private String monitorFilePath;
    @Value("${nginx.monitor.pre}")
    private String nginxMonitorPath;
    @Value("${monitor.http.callback}")
    private String monitorHttpCallback;


    public String getSrsServerOutPort() {
        return srsServerOutPort;
    }

    public void setSrsServerOutPort(String srsServerOutPort) {
        this.srsServerOutPort = srsServerOutPort;
    }

    public String getInnerHost() {
        return innerHost;
    }

    public void setInnerHost(String innerHost) {
        this.innerHost = innerHost;
    }

    public String getMonitorHttpCallback() {
        return monitorHttpCallback;
    }

    public void setMonitorHttpCallback(String monitorHttpCallback) {
        this.monitorHttpCallback = monitorHttpCallback;
    }

    public String getNginxMonitorPath() {
        return nginxMonitorPath;
    }

    public void setNginxMonitorPath(String nginxMonitorPath) {
        this.nginxMonitorPath = nginxMonitorPath;
    }

    public String getNginxProxyPort() {
        return nginxProxyPort;
    }

    public String getMonitorFilePath() {
        return monitorFilePath;
    }

    public void setMonitorFilePath(String monitorFilePath) {
        this.monitorFilePath = monitorFilePath;
    }

    public void setNginxProxyPort(String nginxProxyPort) {
        this.nginxProxyPort = nginxProxyPort;
    }

    public String getNginxLivePath() {
        return nginxLivePath;
    }

    public void setNginxLivePath(String nginxLivePath) {
        this.nginxLivePath = nginxLivePath;
    }

    public String getNginxShotPath() {
        return nginxShotPath;
    }

    public void setNginxShotPath(String nginxShotPath) {
        this.nginxShotPath = nginxShotPath;
    }

    public String getLiveHost() {
        return liveHost;
    }

    public void setLiveHost(String liveHost) {
        this.liveHost = liveHost;
    }

    public String getReplayHost() {
        return replayHost;
    }

    public void setReplayHost(String replayHost) {
        this.replayHost = replayHost;
    }

    public String getTsBasePath() {
        return tsBasePath;
    }

    public void setTsBasePath(String tsBasePath) {
        this.tsBasePath = tsBasePath;
    }

    public String getScreenshotPath() {
        return screenshotPath;
    }

    public void setScreenshotPath(String screenshotPath) {
        this.screenshotPath = screenshotPath;
    }

    public String getRecordCallback() {
        return recordCallback;
    }

    public void setRecordCallback(String recordCallback) {
        this.recordCallback = recordCallback;
    }

    public String getTestFileOne() {
        return testFileOne;
    }

    public void setTestFileOne(String testFileOne) {
        this.testFileOne = testFileOne;
    }

    public String getTestFileTwo() {
        return testFileTwo;
    }

    public void setTestFileTwo(String testFileTwo) {
        this.testFileTwo = testFileTwo;
    }

    public String getFfpmegOutputPath() {
        return ffpmegOutputPath;
    }

    public void setFfpmegOutputPath(String ffpmegOutputPath) {
        this.ffpmegOutputPath = ffpmegOutputPath;
    }

    public String getServerOs() {
        return serverOs;
    }

    public void setServerOs(String serverOs) {
        this.serverOs = serverOs;
    }

    public String getFfmpegWinPath() {
        return ffmpegWinPath;
    }

    public void setFfmpegWinPath(String ffmpegWinPath) {
        this.ffmpegWinPath = ffmpegWinPath;
    }

    public String getFfmpegLinuxPath() {
        return ffmpegLinuxPath;
    }

    public void setFfmpegLinuxPath(String ffmpegLinuxPath) {
        this.ffmpegLinuxPath = ffmpegLinuxPath;
    }

    public String getVersions() {
        return versions;
    }

    public void setVersions(String versions) {
        this.versions = versions;
    }

    public String getSummaries() {
        return summaries;
    }

    public void setSummaries(String summaries) {
        this.summaries = summaries;
    }

    public String getRusages() {
        return rusages;
    }

    public void setRusages(String rusages) {
        this.rusages = rusages;
    }

    public String getSelfProcStats() {
        return selfProcStats;
    }

    public void setSelfProcStats(String selfProcStats) {
        this.selfProcStats = selfProcStats;
    }

    public String getSystemProcStats() {
        return systemProcStats;
    }

    public void setSystemProcStats(String systemProcStats) {
        this.systemProcStats = systemProcStats;
    }

    public String getMeminfos() {
        return meminfos;
    }

    public void setMeminfos(String meminfos) {
        this.meminfos = meminfos;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getRequests() {
        return requests;
    }

    public void setRequests(String requests) {
        this.requests = requests;
    }

    public String getVhosts() {
        return vhosts;
    }

    public void setVhosts(String vhosts) {
        this.vhosts = vhosts;
    }

    public String getStreams() {
        return streams;
    }

    public void setStreams(String streams) {
        this.streams = streams;
    }

    public String getClients() {
        return clients;
    }

    public void setClients(String clients) {
        this.clients = clients;
    }

    public String getConfigs() {
        return configs;
    }

    public void setConfigs(String configs) {
        this.configs = configs;
    }

    public String getSrsHost() {
        return srsHost;
    }

    public void setSrsHost(String srsHost) {
        this.srsHost = srsHost;
    }

    public String getSrsServerPort() {
        return srsServerPort;
    }

    public void setSrsServerPort(String srsServerPort) {
        this.srsServerPort = srsServerPort;
    }

    public String getSrsHttpPort() {
        return srsHttpPort;
    }

    public void setSrsHttpPort(String srsHttpPort) {
        this.srsHttpPort = srsHttpPort;
    }
}
