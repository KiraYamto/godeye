package com.xsmart.camera;

import com.xsmart.camera.util.BeanFactory;
import com.xsmart.camera.util.YamlPropertySourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author tian.xubo
 * @created 2019 - 07 - 01 15:38
 */
@Component
@PropertySource(value = {"classpath:config/godeye.yml"}, factory = YamlPropertySourceFactory.class)
public class GodeyeProperties {

    @Value("${srs.os}")
    private String serverOs;
    @Value("${srs.host.public}")
    private String srsHost;
    @Value("${srs.host.private}")
    private String innerHost;

    @Value("${srs.dns.livestream}")
    private String liveHost;
    @Value("${srs.dns.replaystream}")
    private String replayHost;

    @Value("${srs.port.server.orign}")
    private String srsServerPort;
    @Value("${srs.port.server.proxy}")
    private String srsServerOutPort;
    @Value("${srs.port.http.orign}")
    private String srsHttpPort;
    @Value("${srs.port.nginx.proxy}")
    private String nginxProxyPort;

    @Value("${srs.pre.screenshot}")
    private String nginxShotPath;
    @Value("${srs.pre.livestream}")
    private String nginxLivePath;
    @Value("${srs.pre.monitor}")
    private String nginxMonitorPath;

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

    @Value("${ffmpeg.file.testone}")
    private String testFileOne;
    @Value("${ffmpeg.file.testtwo}")
    private String testFileTwo;

    @Value("${ffmpeg.path.window}")
    private String ffmpegWinPath;
    @Value("${ffmpeg.path.linux}")
    private String ffmpegLinuxPath;
    @Value("${ffmpeg.path.output}")
    private String ffpmegOutputPath;
    @Value("${ffmpeg.path.ts}")
    private String tsBasePath;
    @Value("${ffmpeg.path.monitor}")
    private String monitorFilePath;
    @Value("${ffmpeg.path.screenshot}")
    private String screenshotPath;
    @Value("${ffmpeg.bitrate}")
    private String bitrate;

    @Value("${http.callback.record}")
    private String recordCallback;
    @Value("${http.callback.monitor}")
    private String monitorHttpCallback;
    @Value("${http.callback.screenshot}")
    private String screenshotCallback;


    public String getScreenshotCallback() {
        return screenshotCallback;
    }

    public void setScreenshotCallback(String screenshotCallback) {
        this.screenshotCallback = screenshotCallback;
    }

    public String getServerOs() {
        return serverOs;
    }

    public void setServerOs(String serverOs) {
        this.serverOs = serverOs;
    }

    public String getSrsHost() {
        return srsHost;
    }

    public void setSrsHost(String srsHost) {
        this.srsHost = srsHost;
    }

    public String getInnerHost() {
        return innerHost;
    }

    public void setInnerHost(String innerHost) {
        this.innerHost = innerHost;
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

    public String getSrsServerPort() {
        return srsServerPort;
    }

    public void setSrsServerPort(String srsServerPort) {
        this.srsServerPort = srsServerPort;
    }

    public String getSrsServerOutPort() {
        return srsServerOutPort;
    }

    public void setSrsServerOutPort(String srsServerOutPort) {
        this.srsServerOutPort = srsServerOutPort;
    }

    public String getSrsHttpPort() {
        return srsHttpPort;
    }

    public void setSrsHttpPort(String srsHttpPort) {
        this.srsHttpPort = srsHttpPort;
    }

    public String getNginxProxyPort() {
        return nginxProxyPort;
    }

    public void setNginxProxyPort(String nginxProxyPort) {
        this.nginxProxyPort = nginxProxyPort;
    }

    public String getNginxShotPath() {
        return "http://"+srsHost+":"+nginxProxyPort+nginxShotPath;
    }

    public void setNginxShotPath(String nginxShotPath) {
        this.nginxShotPath = nginxShotPath;
    }

    public String getNginxLivePath() {
        return "http://"+srsHost+":"+srsServerOutPort+nginxLivePath;
    }

    public void setNginxLivePath(String nginxLivePath) {
        this.nginxLivePath = nginxLivePath;
    }

    public String getNginxMonitorPath() {
        return "http://"+srsHost+":"+srsServerOutPort+nginxMonitorPath;
    }

    public void setNginxMonitorPath(String nginxMonitorPath) {
        this.nginxMonitorPath = nginxMonitorPath;
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

    public String getFfpmegOutputPath() {
        return ffpmegOutputPath;
    }

    public void setFfpmegOutputPath(String ffpmegOutputPath) {
        this.ffpmegOutputPath = ffpmegOutputPath;
    }

    public String getTsBasePath() {
        return tsBasePath;
    }

    public void setTsBasePath(String tsBasePath) {
        this.tsBasePath = tsBasePath;
    }

    public String getMonitorFilePath() {
        return monitorFilePath;
    }

    public void setMonitorFilePath(String monitorFilePath) {
        this.monitorFilePath = monitorFilePath;
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

    public String getMonitorHttpCallback() {
        return monitorHttpCallback;
    }

    public void setMonitorHttpCallback(String monitorHttpCallback) {
        this.monitorHttpCallback = monitorHttpCallback;
    }

    public String getBitrate() {
        return bitrate;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    public static void main(String[] args) {
        SpringApplication.run(GodeyeStart.class);
        TestYml testYml = BeanFactory.lookUp(TestYml.class);

        System.out.println(testYml.getNginxLivePath());
        System.out.println(testYml.getNginxMonitorPath());
        System.out.println(testYml.getNginxShotPath());

       // GodeyePropertiesYml godeyePropertiesYml = BeanFactory.lookUp(GodeyePropertiesYml.class);

       // System.out.println(godeyePropertiesYml.getSrsHost());

    }
}
