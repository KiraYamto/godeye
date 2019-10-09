package com.xsmart.camera.service;

import com.xsmart.camera.dao.ReplayConfigDao;
import com.xsmart.camera.srs.v2.model.GstCameraReplayConfigDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tian.xubo
 * @created 2019 - 07 - 18 14:00
 */
@Service
public class ReplayConfigServiceImpl implements ReplayConfigService {

    private static final Logger logger = LoggerFactory.getLogger(ReplayConfigServiceImpl.class);

    @Autowired
    private ReplayConfigDao replayConfigDao;
    @Override
    public List<GstCameraReplayConfigDto> queryReplayConfigList() throws Exception {
        return replayConfigDao.queryReplayConfigList();
    }

    @Override
    public void insertReplayConfig(GstCameraReplayConfigDto replayConfigDto) throws Exception {
        this.replayConfigDao.insertReplayConfig(replayConfigDto);
    }

    @Override
    public void updateReplayConfig(GstCameraReplayConfigDto replayConfigDto) throws Exception {
        this.replayConfigDao.updateReplayConfig(replayConfigDto);
    }

    @Override
    public GstCameraReplayConfigDto queryReplayConfigByCameraId(GstCameraReplayConfigDto replayConfigDto) throws Exception {
        return this.replayConfigDao.queryReplayConfigByCameraId(replayConfigDto);
    }
}
