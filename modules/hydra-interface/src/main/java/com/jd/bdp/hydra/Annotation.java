package com.jd.bdp.hydra;

import java.io.Serializable;

public class Annotation implements Serializable {
    private static final long serialVersionUID = 1840245774768176307L;
    private long annotationId;
    private String type;
    private String ip;
    private int port;
    private Long time;
    private String spanId;
    private String value;

    public long getAnnotationId() {
        return annotationId;
    }

    public void setAnnotationId(long annotationId) {
        this.annotationId = annotationId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Annotation{" +
                "annotationId=" + annotationId +
                ", type='" + type + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", time=" + time +
                ", spanId='" + spanId + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
