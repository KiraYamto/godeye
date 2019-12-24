package com.xsmart.camera.controller.ver2;

import com.alibaba.fastjson.JSON;
import com.xsmart.camera.GodeyeProperties;
import com.xsmart.camera.http.SrsCallbackRequestCommon;
import com.xsmart.camera.service.ReplayConfigService;
import com.xsmart.camera.service.SrsService;
import com.xsmart.camera.srs.v2.model.ClientResponse;
import com.xsmart.camera.srs.v2.model.GstCameraReplayConfigDto;
import com.xsmart.camera.srs.v2.model.SrsClient;
import com.xsmart.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tian.xubo
 * @created 2019 - 07 - 02 17:21
 */
public class SrsCallbackControllerV2 {
    private static final Logger logger = LoggerFactory.getLogger(SrsCallbackControllerV2.class);

    @Autowired
    private SrsService srsService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private GodeyeProperties godeyeProperties;

    @Autowired
    private ReplayConfigService replayConfigService;

    @PostMapping("srs/callback/onconnect")
    public int onconnect(@RequestBody String request) {
        int resultCode = 0;
        logger.info("=======http request onconnect begin request body is {} ===== ",request);
        logger.info("=======http request onconnect end and response is \r\n {}===== ",resultCode);
        return resultCode;
    }
    @PostMapping("srs/callback/onclose")
    public int onclose(@RequestBody String request) {
        int resultCode = 0;
        logger.info("=======http request onclose begin request body is {} ===== ",request);
        SrsCallbackRequestCommon requestCommon = JSON.parseObject(request,SrsCallbackRequestCommon.class);
        ClientResponse response = srsService.getClients();
        SrsClient closeClient = null;
        if(response != null && response.getClients() != null){
            List<SrsClient> clientList = response.getClients();
            for(SrsClient client:clientList){
                if(client.getId() == requestCommon.getClient_id()){
                    closeClient = client;
                    logger.info("find closeClient is {}",JSON.toJSONString(closeClient));
                    break;
                }
            }
        }
        //如果开启了回放功能，不关闭推流。
        String deviceId = closeClient.getUrl().split("/")[2];
        GstCameraReplayConfigDto query = new GstCameraReplayConfigDto();
        query.setCameraId(deviceId);
        try {
            GstCameraReplayConfigDto gstCameraReplayConfigDto = replayConfigService.queryReplayConfigByCameraId(query);
            if(gstCameraReplayConfigDto != null && gstCameraReplayConfigDto.getReplayOn() == Constants.ReplayType.ON.getType()){
                //开启回放
                logger.info("srscallback onclose found  camera {} has open replay func,",deviceId);
                return resultCode;
            }
        } catch (Exception e) {
            logger.error("srscallback onclose queryReplayConfigByCameraId occur an error,",e);
        }
        //判断这个被关闭的客户端是推流客户端还是播放客户端
        Map<Long, List<SrsClient>> map = srsService.getClientMap();
        List<SrsClient> clientList = map.get(closeClient.getStream());

        if(closeClient.isPublish() == true && clientList.size() >= 2){

            //如果是推流客户端，并且还有非观看的客户端，重启推流。
        }else{
            //如果关闭的非推流客户端 判断还有多少观众，如果没有观众了，关闭推流
            if(clientList != null ){
                logger.info("publishClient  has {} players",clientList.size());


            }
        }


        logger.info("=======http request onclose end and response is \r\n {}===== ",resultCode);
        return resultCode;
    }
    @PostMapping("srs/callback/onpublish")
    public int onpublish(@RequestBody String request) {
        int resultCode = 0;
        logger.info("=======http request onpublish begin request body is {} ===== ",request);
        logger.info("=======http request onpublish end and response is \r\n {}===== ",resultCode);
        return resultCode;
    }
    @PostMapping("srs/callback/onunpublish")
    public int onunpublish(@RequestBody String request) {
        int resultCode = 0;
        logger.info("=======http request onunpublish begin request body is {} ===== ",request);
        SrsCallbackRequestCommon requestCommon = JSON.parseObject(request,SrsCallbackRequestCommon.class);
        String rtmpPath = "rtmp://"+requestCommon.getIp()+":"+godeyeProperties.getSrsServerPort()+"/"+requestCommon.getApp()+"/"+requestCommon.getStream();
        String PID =  srsService.getPID(rtmpPath);
        if(PID != null){
            srsService.closeLinuxProcess(PID);
        }
        logger.info("=======http request onunpublish end and response is \r\n {}===== ",resultCode);
        return resultCode;
    }
    @PostMapping("srs/callback/onplay")
    public int onplay(@RequestBody String request) {
        int resultCode = 0;
        logger.info("=======http request onplay begin request body is {} ===== ",request);
        logger.info("=======http request onplay end and response is \r\n {}===== ",resultCode);
        return resultCode;
    }
    @PostMapping("srs/callback/onstop")
    public int onstop(@RequestBody String request) {
        int resultCode = 0;
        logger.info("=======http request onstop begin request body is {} ===== ",request);

        logger.info("=======http request onstop end and response is \r\n {}===== ",resultCode);
        return resultCode;
    }
    @PostMapping("srs/callback/ondvr")
    public int ondvr(@RequestBody String request) {
        int resultCode = 0;
        logger.info("=======http request ondvr begin request body is {} ===== ",request);
        SrsCallbackRequestCommon requestCommon = JSON.parseObject(request,SrsCallbackRequestCommon.class);
        String fileName = requestCommon.getCwd()+requestCommon.getFile().substring(1);
        Map<String,String> paraMap = new HashMap<>();
        paraMap.put("fileName",fileName);
        String url = godeyeProperties.getRecordCallback();
        String result = null;
        try {
             result = this.restTemplate.postForObject(url,paraMap,String.class);
        }catch (Exception e){
            logger.error("ondvr request record callback occur error",e);
        }
        logger.info("=======http request ondvr end and fileName is \r\n {} and and get url is {} response is {} ===== ",fileName,url,result);
        return resultCode;
    }

    public static void main(String[] args) {
        String  tt = "{\n" +
                "\t\"action\": \"on_publish\",\n" +
                "\t\"client_id\": 173,\n" +
                "\t\"ip\": \"10.45.47.58\",\n" +
                "\t\"vhost\": \"__defaultVhost__\",\n" +
                "\t\"app\": \"live\",\n" +
                "\t\"tcUrl\": \"rtmp://10.45.47.58:1935/live\",\n" +
                "\t\"stream\": \"onepiece\",\n" +
                "\t\"param\": \"\"\n" +
                "}";
        String tt2 = "{\n" +
                "      \"id\": 8820,\n" +
                "      \"vhost\": 100520,\n" +
                "      \"stream\": 100521,\n" +
                "      \"ip\": \"10.45.47.58\",\n" +
                "      \"pageUrl\": \"\",\n" +
                "      \"swfUrl\": \"\",\n" +
                "      \"tcUrl\": \"rtmp://10.45.47.58:1935/live\",\n" +
                "      \"url\": \"/live/onepiece\",\n" +
                "      \"type\": \"fmle-publish\",\n" +
                "      \"publish\": true,\n" +
                "      \"alive\": 5005\n" +
                "    }";
        SrsClient srsClient = JSON.parseObject(tt2,SrsClient.class);
        String provider = srsClient.getUrl().split("/")[1];
        String deviceId = srsClient.getUrl().split("/")[2];
        System.out.println(provider);
        System.out.println(deviceId);
    }

}
