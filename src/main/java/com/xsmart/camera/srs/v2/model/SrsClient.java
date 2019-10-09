package com.xsmart.camera.srs.v2.model;

import java.io.Serializable;

/**
 * @author tian.xubo
 * @created 2019 - 07 - 02 16:03
 */
public class SrsClient implements Serializable {

    private long id;
    private long vhost;
    private Long stream;
    private String ip;
    private String pageUrl;
    private String swfUrl;
    private String tcUrl;
    private String url;
    private String type;
    private boolean publish;
    private long alive;
//    "id": 2000,
//            "vhost": 14610,
//            "stream": 14613,
//            "ip": "172.21.64.133",
//            "pageUrl": "",
//            "swfUrl": "",
//            "tcUrl": "rtmp://172.21.64.133:1935/live2",
//            "url": "/live2/twopiece",
//            "type": "fmle-publish",
//            "publish": true,
//            "alive": 15002

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVhost() {
        return vhost;
    }

    public void setVhost(long vhost) {
        this.vhost = vhost;
    }

    public Long getStream() {
        return stream;
    }

    public void setStream(Long stream) {
        this.stream = stream;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getSwfUrl() {
        return swfUrl;
    }

    public void setSwfUrl(String swfUrl) {
        this.swfUrl = swfUrl;
    }

    public String getTcUrl() {
        return tcUrl;
    }

    public void setTcUrl(String tcUrl) {
        this.tcUrl = tcUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPublish() {
        return publish;
    }

    public void setPublish(boolean publish) {
        this.publish = publish;
    }

    public long getAlive() {
        return alive;
    }

    public void setAlive(long alive) {
        this.alive = alive;
    }
}
