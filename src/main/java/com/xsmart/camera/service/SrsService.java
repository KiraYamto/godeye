package com.xsmart.camera.service;

import com.xsmart.camera.srs.v2.model.ClientResponse;
import com.xsmart.camera.srs.v2.model.MonitorRequest;
import com.xsmart.camera.srs.v2.model.SrsClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

/**
 * @author tian.xubo
 * @created 2019 - 07 - 02 15:48
 */
public interface SrsService {


     String getStreams() ;
     ClientResponse getClients();
     Map<Long, List<SrsClient>> getClientMap();
     void kickoffClient(SrsClient client);
     String getPID(String command);
     void closeLinuxProcess(String Pid);
     void monitorCallBack(MonitorRequest request);

}
