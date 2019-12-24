package com.xsmart.camera.dao;

import com.xsmart.camera.srs.v2.model.GstCameraReplayConfigDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tian.xubo
 * @created 2019 - 07 - 18 10:35
 */
@Component
public class ReplayConfigDaoImpl implements ReplayConfigDao {
    private static final Logger logger = LoggerFactory.getLogger(ReplayConfigDaoImpl.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<GstCameraReplayConfigDto> queryReplayConfigList() throws Exception {
        String sql = "SELECT A.CONFIG_ID AS configId,\n" +
                "       A.CAMERA_ID AS cameraId,\n" +
                "       A.Replay_On as replayOn,\n" +
                "       A.Provider,\n" +
                "       A.RTSP_ADDR AS rtspAddr,\n" +
                "       A.MONITOR_FILE_PATH AS monitorFilePath,\n" +
                "       A.VCODEC ,\n" +
                "       A.PLAYER,\n" +
                "       A.TS_FILE_PATH AS tsFilePath\n" +
                "  FROM GST_CAMERA_REPLAY_CONFIG A";
        List<GstCameraReplayConfigDto> list = this.jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(GstCameraReplayConfigDto.class));
        return list;
    }

    //@CachePut(value = "ReplayConfig" ,key = "#p0.cameraId")
    @Override
    public GstCameraReplayConfigDto insertReplayConfig(GstCameraReplayConfigDto replayConfigDto) throws Exception {
        String sql = "insert into gst_camera_replay_config (CONFIG_ID,CAMERA_ID,REPLAY_ON,PROVIDER,RTSP_ADDR,TS_FILE_PATH,MONITOR_FILE_PATH,VCODEC,PLAYER)  values(?,?,?,?,?,?,?,?,?)";
        int row = this.jdbcTemplate.update(sql,new Object[]{replayConfigDto.getConfigId(),replayConfigDto.getCameraId(),replayConfigDto.getReplayOn(),replayConfigDto.getProvider(),replayConfigDto.getRtspAddr()
                ,replayConfigDto.getTsFilePath(),replayConfigDto.getMonitorFilePath(),replayConfigDto.getVcodec(),replayConfigDto.getPlayer()});
        logger.info("insertReplayConfig excuted completed");
        if(row == 1){
            return replayConfigDto;
        }else {
            return null;
        }
    }
    //@CachePut(value = "ReplayConfig" ,key = "#p0.cameraId")
    @Override
    public GstCameraReplayConfigDto updateReplayConfig(GstCameraReplayConfigDto replayConfigDto) throws Exception {
        String sql = "UPDATE gst_camera_replay_config A\n" +
                "   SET A.REPLAY_ON = ?, A.PROVIDER = ?, A.RTSP_ADDR = ?, A.TS_FILE_PATH = ?,A.MONITOR_FILE_PATH = ?,A.VCODEC = ?,A.PLAYER = ?" +
                " where a.CAMERA_ID = ? ";
       int row = this.jdbcTemplate.update(sql,new Object[]{replayConfigDto.getReplayOn(),replayConfigDto.getProvider(),replayConfigDto.getRtspAddr(),replayConfigDto.getTsFilePath(),replayConfigDto.getMonitorFilePath(),replayConfigDto.getVcodec(),replayConfigDto.getPlayer(),replayConfigDto.getCameraId()});
       logger.info("updateReplayConfig excuted completed");
       if(row == 1){
           return replayConfigDto;
       }else {
           return null;
       }

    }
    //@Cacheable(value = "ReplayConfig" ,key = "#p0.cameraId")
    @Override
    public GstCameraReplayConfigDto queryReplayConfigByCameraId(GstCameraReplayConfigDto replayConfigDto) throws Exception {
        String sql = "Select A.CONFIG_ID AS configId,\n" +
                "       A.CAMERA_ID as cameraId,\n" +
                "       A.REPLAY_ON as replayOn,\n" +
                "       A.PROVIDER  as provider,\n" +
                "       A.RTSP_ADDR as rtspAddr,\n" +
                "       A.MONITOR_FILE_PATH AS monitorFilePath,\n" +
                "       A.VCODEC ,\n" +
                "       A.PLAYER,\n" +
                "       A.TS_FILE_PATH AS tsFilePath\n"+
                "  FROM gst_camera_replay_config a where a.camera_id = ?";
        List<GstCameraReplayConfigDto> list = this.jdbcTemplate.query(sql,new Object[]{replayConfigDto.getCameraId()}, BeanPropertyRowMapper.newInstance(GstCameraReplayConfigDto.class));
        logger.info("queryReplayConfigByCameraId excuted completed");
        if(list != null && list.size() == 1){
            return list.get(0);
        }
        return null;
    }

    //@CacheEvict(value = "ReplayConfig" ,key = "#p0.cameraId")
    @Override
    public GstCameraReplayConfigDto deleteReplayConfigByCameraId(GstCameraReplayConfigDto replayConfigDto) throws Exception {
        String sql = "delete from gst_camera_replay_config where camera_id = ?";
       int row =  this.jdbcTemplate.update(sql,new Object[]{replayConfigDto.getCameraId()});
       if(row == 1){
           return replayConfigDto;
       }else {
           return null;
       }
    }
}
