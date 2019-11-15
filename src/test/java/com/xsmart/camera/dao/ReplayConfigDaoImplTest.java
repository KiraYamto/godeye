package com.xsmart.camera.dao;

import com.alibaba.fastjson.JSON;
import com.xsmart.camera.srs.v2.model.GstCameraReplayConfigDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ReplayConfigDaoImplTest {

    @Autowired
    private ReplayConfigDao replayConfigDao;
    @Test
    public void queryReplayConfigList() {
        try {
            List<GstCameraReplayConfigDto> list =  replayConfigDao.queryReplayConfigList();
            System.out.println(JSON.toJSONString(list));
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void insertReplayConfig() {
        GstCameraReplayConfigDto gstCameraReplayConfigDto = new GstCameraReplayConfigDto();
        gstCameraReplayConfigDto.setCameraId("150002");
        gstCameraReplayConfigDto.setPlayer(0);
        gstCameraReplayConfigDto.setProvider("dahua");
        gstCameraReplayConfigDto.setRtspAddr("rtsp://admin:gst.123456@192.168.1.64:554/Streaming/Channels/101");
        gstCameraReplayConfigDto.setConfigId(150002);
        gstCameraReplayConfigDto.setReplayOn(0);
        gstCameraReplayConfigDto.setTsFilePath("/usr/local/srs/objs/nginx/html/replay-hikvision-150001");
        gstCameraReplayConfigDto.setVcodec("h264");
        try {
           replayConfigDao.insertReplayConfig(gstCameraReplayConfigDto);
        }catch (Exception e){
            System.out.println(e);
        }     //   [{"cameraId":"150001","configId":150001,"player":6,"provider":"hikvision","replayOn":0,"rtspAddr":"rtsp://admin:gst.123456@192.168.1.64:554/Streaming/Channels/102","tsFilePath":"/usr/local/srs/objs/nginx/html/replay-hikvision-150001","vcodec":"h265"},{"cameraId":"150108","configId":150108,"player":0,"provider":"hikvision","replayOn":0,"rtspAddr":"rtsp://admin:gst.123456@192.168.1.64:554/Streaming/Channels/102","tsFilePath":"/usr/local/srs/objs/nginx/html/replay-hikvision-150108","vcodec":"h264"}]

    }

    @Test
    public void updateReplayConfig() {
        GstCameraReplayConfigDto gstCameraReplayConfigDto = new GstCameraReplayConfigDto();
        gstCameraReplayConfigDto.setCameraId("150002");
        gstCameraReplayConfigDto.setPlayer(199);
        gstCameraReplayConfigDto.setProvider("dahua");
        gstCameraReplayConfigDto.setRtspAddr("rtsp://admin:gst.123456@192.168.1.64:554/Streaming/Channels/101");
        gstCameraReplayConfigDto.setConfigId(150002);
        gstCameraReplayConfigDto.setReplayOn(0);
        gstCameraReplayConfigDto.setTsFilePath("/usr/local/srs/objs/nginx/html/replay-hikvision-150001");
        gstCameraReplayConfigDto.setVcodec("h264");
        try {
            replayConfigDao.updateReplayConfig(gstCameraReplayConfigDto);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void queryReplayConfigByCameraId() {
        try {
            GstCameraReplayConfigDto dto = new GstCameraReplayConfigDto();
            dto.setCameraId("150002");
            GstCameraReplayConfigDto gstCameraReplayConfigDto =  replayConfigDao.queryReplayConfigByCameraId(dto);
            System.out.println(JSON.toJSONString(gstCameraReplayConfigDto));
        }catch (Exception e){
            System.out.println(e);
        }
    }
    @Test
    public void deleteReplayConfigByCameraId(){
        try {
            GstCameraReplayConfigDto dto = new GstCameraReplayConfigDto();
            dto.setCameraId("150002");
            GstCameraReplayConfigDto gstCameraReplayConfigDto =  replayConfigDao.deleteReplayConfigByCameraId(dto);
            System.out.println(JSON.toJSONString(gstCameraReplayConfigDto));
        }catch (Exception e){
            System.out.println(e);
        }
    }

}