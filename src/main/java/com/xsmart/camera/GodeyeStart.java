package com.xsmart.camera;

import com.xsmart.camera.service.CameraService;
import com.xsmart.camera.service.CameraServiceImpl;
import com.xsmart.camera.util.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author tian.xubo
 * @created 2019 - 06 - 28 10:03
 */
@SpringBootApplication
@EnableCaching
public class GodeyeStart {
    private static final Logger logger = LoggerFactory.getLogger(GodeyeStart.class);

    public static void main(String[] args) {
        SpringApplication.run(GodeyeStart.class);
        CameraService cameraService = BeanFactory.lookUp(CameraServiceImpl.class);
        cameraService.initAllCamera();
        logger.info("=================================");
        logger.info("=================================");
        logger.info("=====godeye start completed======");
        logger.info("=================================");
        logger.info("=================================");


    }
}
