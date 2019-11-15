package com.xsmart.camera.dao;

import com.xsmart.camera.srs.v2.model.GstCameraReplayConfigDto;

import java.util.List;

/**
 * @author tian.xubo
 * @created 2019 - 07 - 18 10:32
 */
public interface ReplayConfigDao {

    List<GstCameraReplayConfigDto> queryReplayConfigList()throws Exception;
    GstCameraReplayConfigDto insertReplayConfig(GstCameraReplayConfigDto replayConfigDto)throws Exception;
    GstCameraReplayConfigDto updateReplayConfig(GstCameraReplayConfigDto replayConfigDto)throws Exception;
    GstCameraReplayConfigDto queryReplayConfigByCameraId(GstCameraReplayConfigDto replayConfigDto)throws Exception;
    GstCameraReplayConfigDto deleteReplayConfigByCameraId(GstCameraReplayConfigDto replayConfigDto)throws Exception;

}
