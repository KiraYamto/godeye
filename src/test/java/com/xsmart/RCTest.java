package com.xsmart;

import com.alibaba.fastjson.JSON;
import com.xsmart.camera.dao.ReplayConfigDao;
import com.xsmart.camera.srs.v2.model.GstCameraReplayConfigDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author tian.xubo
 * @created 2019 - 11 - 06 15:22
 */
public class RCTest {
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
    }

    @Test
    public void updateReplayConfig() {
    }

    @Test
    public void queryReplayConfigByCameraId() {
    }
}
