package com.xsmart.camera.controller;

import com.xsmart.camera.GodeyeProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author tian.xubo
 * @created 2019 - 07 - 01 15:21
 */
@RestController
public class SrsController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private GodeyeProperties godeyeProperties;

    private String protocolHttp = "http";

    private static final Logger logger = LoggerFactory.getLogger(SrsController.class);


    @GetMapping("srs/versions")
    public String getVersions() {
        logger.info("=======http request getVersions begin ===== ");
        StringBuffer urlBuffer = new StringBuffer(protocolHttp).append("://")
                .append(godeyeProperties.getSrsHost()).append(":").append(godeyeProperties.getSrsHttpPort())
                .append(godeyeProperties.getVersions());
        String result = restTemplate.getForObject(urlBuffer.toString(),String.class);

        logger.info("=======http request getVersions end and response is \r\n {}===== ",result);

        return result;
    }
    @GetMapping("srs/summaries")
    public String getSummaries() {
        logger.info("=======http request getSummaries begin ===== ");
        StringBuffer urlBuffer = new StringBuffer(protocolHttp).append("://")
                .append(godeyeProperties.getSrsHost()).append(":").append(godeyeProperties.getSrsHttpPort())
                .append(godeyeProperties.getSummaries());
        String result = restTemplate.getForObject(urlBuffer.toString(),String.class);
        logger.info("=======http request getSummaries end and response is \r\n {}===== ",result);
        return result;
    }
    @GetMapping("srs/rusages")
    public String getRusages() {
        logger.info("=======http request getRusages begin ===== ");
        StringBuffer urlBuffer = new StringBuffer(protocolHttp).append("://")
                .append(godeyeProperties.getSrsHost()).append(":").append(godeyeProperties.getSrsHttpPort())
                .append(godeyeProperties.getRusages());
        String result = restTemplate.getForObject(urlBuffer.toString(),String.class);
        logger.info("=======http request getRusages end and response is \r\n {}===== ",result);
        return result;
    }
    @GetMapping("srs/self_proc_stats")
    public String getselfProcStats() {
        logger.info("=======http request getselfProcStats begin ===== ");
        StringBuffer urlBuffer = new StringBuffer(protocolHttp).append("://")
                .append(godeyeProperties.getSrsHost()).append(":").append(godeyeProperties.getSrsHttpPort())
                .append(godeyeProperties.getSelfProcStats());
        String result = restTemplate.getForObject(urlBuffer.toString(),String.class);
        logger.info("=======http request getselfProcStats end and response is \r\n {}===== ",result);
        return result;
    }
    @GetMapping("srs/system_proc_stats")
    public String getSystemProcStats() {
        logger.info("=======http request getSystemProcStats begin ===== ");
        StringBuffer urlBuffer = new StringBuffer(protocolHttp).append("://")
                .append(godeyeProperties.getSrsHost()).append(":").append(godeyeProperties.getSrsHttpPort())
                .append(godeyeProperties.getSystemProcStats());
        String result = restTemplate.getForObject(urlBuffer.toString(),String.class);
        logger.info("=======http request getSystemProcStats end and response is \r\n {}===== ",result);
        return result;
    }
    @GetMapping("srs/meminfos")
    public String getMeminfos() {
        logger.info("=======http request getMeminfos begin ===== ");
        StringBuffer urlBuffer = new StringBuffer(protocolHttp).append("://")
                .append(godeyeProperties.getSrsHost()).append(":").append(godeyeProperties.getSrsHttpPort())
                .append(godeyeProperties.getMeminfos());
        String result = restTemplate.getForObject(urlBuffer.toString(),String.class);
        logger.info("=======http request getMeminfos end and response is \r\n {}===== ",result);
        return result;
    }
    @GetMapping("srs/authors")
    public String getAuthors() {
        logger.info("=======http request getAuthors begin ===== ");
        StringBuffer urlBuffer = new StringBuffer(protocolHttp).append("://")
                .append(godeyeProperties.getSrsHost()).append(":").append(godeyeProperties.getSrsHttpPort())
                .append(godeyeProperties.getAuthors());
        String result = restTemplate.getForObject(urlBuffer.toString(),String.class);
        logger.info("=======http request getAuthors end and response is \r\n {}===== ",result);
        return result;
    }
    @GetMapping("srs/features")
    public String getFeatures() {
        logger.info("=======http request getAuthors begin ===== ");
        StringBuffer urlBuffer = new StringBuffer(protocolHttp).append("://")
                .append(godeyeProperties.getSrsHost()).append(":").append(godeyeProperties.getSrsHttpPort())
                .append(godeyeProperties.getFeatures());
        String result = restTemplate.getForObject(urlBuffer.toString(),String.class);
        logger.info("=======http request getAuthors end and response is \r\n {}===== ",result);
        return result;
    }
    @GetMapping("srs/requests")
    public String getRequests() {
        logger.info("=======http request getRequests begin ===== ");
        StringBuffer urlBuffer = new StringBuffer(protocolHttp).append("://")
                .append(godeyeProperties.getSrsHost()).append(":").append(godeyeProperties.getSrsHttpPort())
                .append(godeyeProperties.getRequests());
        String result = restTemplate.getForObject(urlBuffer.toString(),String.class);
        logger.info("=======http request getRequests end and response is \r\n {}===== ",result);
        return result;
    }
    @GetMapping("srs/streams")
    public String getStreams() {
        logger.info("=======http request getStreams begin ===== ");
        StringBuffer urlBuffer = new StringBuffer(protocolHttp).append("://")
                .append(godeyeProperties.getSrsHost()).append(":").append(godeyeProperties.getSrsHttpPort())
                .append(godeyeProperties.getStreams());
        String result = restTemplate.getForObject(urlBuffer.toString(),String.class);
        logger.info("=======http request getStreams end and response is \r\n {}===== ",result);
        return result;
    }
    @GetMapping("srs/clients")
    public String getClients() {
        logger.info("=======http request getClients begin ===== ");
        StringBuffer urlBuffer = new StringBuffer(protocolHttp).append("://")
                .append(godeyeProperties.getInnerHost()).append(":").append(godeyeProperties.getSrsHttpPort())
                .append(godeyeProperties.getClients());
        String result = restTemplate.getForObject(urlBuffer.toString(),String.class);
        logger.info("=======http request getClients end and response is \r\n {}===== ",result);
        return result;
    }
    @GetMapping("srs/configs")
    public String getConfigs() {
        logger.info("=======http request getConfigs begin ===== ");
        StringBuffer urlBuffer = new StringBuffer(protocolHttp).append("://")
                .append(godeyeProperties.getSrsHost()).append(":").append(godeyeProperties.getSrsHttpPort())
                .append(godeyeProperties.getConfigs());
        String result = restTemplate.getForObject(urlBuffer.toString(),String.class);
        logger.info("=======http request getConfigs end and response is \r\n {}===== ",result);
        return result;
    }

}
