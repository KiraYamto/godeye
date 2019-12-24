package com.xsmart.camera.controller.ver2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.mock;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SrsCallbackControllerTest {
    @Autowired
    SrsCallbackControllerV2 srsCallbackControllerV2;

    @Test
    public void onconnect() {
   /*     {
            "action": "on_connect",
                "client_id": 122,
                "ip": "192.168.1.102",
                "vhost": "__defaultVhost__",
                "app": "hikvision-150108",
                "tcUrl": "rtmp://192.168.1.102:1935/hikvision-150108",
                "pageUrl": ""
        }*/
        String request = "{\n" +
                "            \"action\": \"on_connect\",\n" +
                "                \"client_id\": 122,\n" +
                "                \"ip\": \"192.168.1.102\",\n" +
                "                \"vhost\": \"__defaultVhost__\",\n" +
                "                \"app\": \"hikvision-150108\",\n" +
                "                \"tcUrl\": \"rtmp://192.168.1.102:1935/hikvision-150108\",\n" +
                "                \"pageUrl\": \"\"\n" +
                "        }";
        srsCallbackControllerV2.onconnect(request);


    }

    @Test
    public void onclose() {
    }

    @Test
    public void onpublish() {

    }

    @Test
    public void onunpublish() {
    }

    @Test
    public void onplay() {
    }

    @Test
    public void onstop() {
    }

    @Test
    public void ondvr() {
    }
}