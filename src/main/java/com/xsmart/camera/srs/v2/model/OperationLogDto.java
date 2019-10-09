package com.xsmart.camera.srs.v2.model;

import java.io.Serializable;

/**
 * @author tian.xubo
 * @created 2019 - 07 - 12 13:56
 */
public class OperationLogDto implements Serializable {

    private String userId;
    private String operationUrl;
    private String requestBody;
    private String requestTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOperationUrl() {
        return operationUrl;
    }

    public void setOperationUrl(String operationUrl) {
        this.operationUrl = operationUrl;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }
}
