package com.xsmart.camera.service;

import com.alibaba.fastjson.JSON;
import com.xsmart.camera.GodeyeProperties;
import com.xsmart.camera.srs.v2.model.ClientResponse;
import com.xsmart.camera.srs.v2.model.MonitorRequest;
import com.xsmart.camera.srs.v2.model.SrsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tian.xubo
 * @created 2019 - 07 - 02 15:52
 */
@Service
public class SrsServiceImpl implements SrsService {
    private static final Logger logger = LoggerFactory.getLogger(SrsServiceImpl.class);
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private GodeyeProperties godeyeProperties;

    private String protocolHttp = "http";

    @Override
    public String getStreams() {
        logger.info("=======SrsServiceImpl getStreams begin ===== ");
        StringBuffer urlBuffer = new StringBuffer(protocolHttp).append("://")
                .append(godeyeProperties.getInnerHost()).append(":").append(godeyeProperties.getSrsHttpPort())
                .append(godeyeProperties.getStreams());
        String result = restTemplate.getForObject(urlBuffer.toString(),String.class);
        logger.info("=======SrsServiceImpl getStreams end and response is \r\n {}===== ",result);
        return result;    }
    @Override
    public ClientResponse getClients() {
        logger.info("=======SrsServiceImpl getClients begin ===== ");
        StringBuffer urlBuffer = new StringBuffer(protocolHttp).append("://")
                .append(godeyeProperties.getInnerHost()).append(":").append(godeyeProperties.getSrsHttpPort())
                .append(godeyeProperties.getClients());
        ClientResponse result = restTemplate.getForObject(urlBuffer.toString(), ClientResponse.class);
        logger.info("=======SrsServiceImpl getClients end and response is \r\n {}===== ", JSON.toJSONString(result));
        return result;
    }

    @Override
    public Map<Long, List<SrsClient>> getClientMap() {
        logger.info("=======SrsServiceImpl getClientMap begin ===== ");
        Map<Long, List<SrsClient>> map = new HashMap<>();
        StringBuffer urlBuffer = new StringBuffer(protocolHttp).append("://")
                .append(godeyeProperties.getInnerHost()).append(":").append(godeyeProperties.getSrsHttpPort())
                .append(godeyeProperties.getClients());
        ClientResponse result = restTemplate.getForObject(urlBuffer.toString(), ClientResponse.class);
        if(result != null && result.getClients() != null){
            for(SrsClient client:result.getClients()){
                List<SrsClient> clientList = map.get(client.getStream());
                if(clientList == null){
                    clientList = new ArrayList<>();
                }
                clientList.add(client);
                map.put(client.getStream(),clientList);
            }
        }
        logger.info("=======SrsServiceImpl getClientMap end and response is \r\n {}===== ",map);
        return map;
    }

    @Override
    public Map<Long, List<SrsClient>> getClientMap(ClientResponse clientResponse) {
        Map<Long, List<SrsClient>> map = new HashMap<>();
        ClientResponse result = clientResponse;
        if(result != null && result.getClients() != null){
            for(SrsClient client:result.getClients()){
                List<SrsClient> clientList = map.get(client.getStream());
                if(clientList == null){
                    clientList = new ArrayList<>();
                }
                clientList.add(client);
                map.put(client.getStream(),clientList);
            }
        }
        logger.info("=======SrsServiceImpl getClientMap end and response is \r\n {}===== ",map);
        return map;
    }

    @Override
    public void kickoffClient(SrsClient client) {
        logger.info("=======SrsServiceImpl kickoffClient begin ===== ");
        StringBuffer urlBuffer = new StringBuffer(protocolHttp).append("://")
                .append(godeyeProperties.getInnerHost()).append(":").append(godeyeProperties.getSrsHttpPort())
                .append(godeyeProperties.getClients()).append("/").append(client.getId());
        restTemplate.delete(urlBuffer.toString());
        //检查进程在不在
        String pid = getPID(client.getTcUrl());
        if(pid != null){
            closeLinuxProcess(pid);
        }
        logger.info("=======SrsServiceImpl kickoffClient end ===== ");
    }
    /**
     * 获取Linux进程的PID
     * @param command
     * @return
     */
    @Override
    public  String getPID(String command){
        String match = godeyeProperties.getFfmpegLinuxPath();
        BufferedReader reader = null;
        Process process = null;
        try{
            //显示所有进程
            String[] cmds = {"/bin/sh","-c","ps -ef|grep "+command};
            process = Runtime.getRuntime().exec(cmds);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line ;
            while((line = reader.readLine()) != null){
                if(line.contains(match)){
                    String[] strs = line.split("\\s+");
                    if(logger.isDebugEnabled()){
                        logger.debug("judge process line is {} and commnad is {} and str last is {}-----> ",line,command,strs[7]);
                    }
                    if(strs[7].indexOf(godeyeProperties.getFfmpegLinuxPath()) != -1){
                        logger.info("find line is {} and commnad is {}-----> ",line,command);
                        return strs[1];
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {

                }
            }
        }
        return null;
    }
    /**
     * 关闭Linux进程
     * @param Pid 进程的PID
     */
    @Override
    public  void closeLinuxProcess(String Pid){
        Process process = null;
        BufferedReader reader =null;
        try{
            //杀掉进程
            process = Runtime.getRuntime().exec("kill -9 "+Pid);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while((line = reader.readLine()) != null){
                logger.info("kill PID return info -----> "+line);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(process!=null){
                process.destroy();
            }

            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {

                }
            }
        }
    }

    @Override
    public void monitorCallBack(MonitorRequest request) {
        logger.info("=======SrsServiceImpl monitorCallBack begin ===== request is {}",JSON.toJSONString(request));
        String url = godeyeProperties.getMonitorHttpCallback();
        String result = restTemplate.postForObject(url,request, String.class);
        logger.info("=======SrsServiceImpl monitorCallBack end and response is \r\n {}===== ", JSON.toJSONString(result));
    }
}
