package com.xsmart.camera.srs.v2.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author tian.xubo
 * @created 2019 - 07 - 02 16:00
 */
public class ClientResponse implements Serializable {
    private String code;
    private String server;
    private List<SrsClient> clients;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public List<SrsClient> getClients() {
        return clients;
    }

    public void setClients(List<SrsClient> clients) {
        this.clients = clients;
    }
}
