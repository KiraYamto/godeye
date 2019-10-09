package com.xsmart.camera.http;

import java.io.Serializable;

/**
 * @author tian.xubo
 * @created 2019 - 07 - 02 16:37
 */
public class CameraResponse implements Serializable {
    private int code;
    private String desc;
    private Object other;

    public Object getOther() {
        return other;
    }

    public void setOther(Object other) {
        this.other = other;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
