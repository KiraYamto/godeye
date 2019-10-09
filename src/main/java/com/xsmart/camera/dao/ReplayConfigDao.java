package com.xsmart.camera.dao;

import com.xsmart.camera.srs.v2.model.GstCameraReplayConfigDto;

import java.util.List;

/**
 * @author tian.xubo
 * @created 2019 - 07 - 18 10:32
 */
public interface ReplayConfigDao {

    List<GstCameraReplayConfigDto> queryReplayConfigList()throws Exception;
    void insertReplayConfig(GstCameraReplayConfigDto replayConfigDto)throws Exception;
    void updateReplayConfig(GstCameraReplayConfigDto replayConfigDto)throws Exception;
    GstCameraReplayConfigDto queryReplayConfigByCameraId(GstCameraReplayConfigDto replayConfigDto)throws Exception;


}
