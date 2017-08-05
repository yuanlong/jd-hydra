package com.jd.bdp.hydra.mysql.persistent.entity;

import com.jd.bdp.hydra.Annotation;
import com.jd.bdp.hydra.BinaryAnnotation;
import com.jd.bdp.hydra.Span;

/**
 * Date: 13-5-7
 * Time: 上午10:54
 */
public class Absannotation {
    private long annotationId;
    private String k;
    private String ip;
    private Integer port;
    private Long timestamp;
    private String spanId;

    public Absannotation(BinaryAnnotation binaryAnnotation, Span span){
        this.spanId = span.getSpanId();
        this.k = binaryAnnotation.getKey();
        this.ip = binaryAnnotation.getHost().getIp();
        this.port = binaryAnnotation.getHost().getPort();
    }

    public Absannotation(Annotation annotation, Span span){
        this.spanId = span.getSpanId();
        this.k = annotation.getValue();
        this.timestamp = annotation.getTimestamp();
        this.ip = annotation.getHost().getIp();
        this.port = annotation.getHost().getPort();
    }

    @Override
    public String toString() {
        return "Absannotation{" +
                "annotationId=" + annotationId +
                ", k='" + k + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", timestamp=" + timestamp +
                ", spanId='" + spanId + '\'' +
                '}';
    }

    public long getAnnotationId() {
        return annotationId;
    }

    public void setAnnotationId(long annotationId) {
        this.annotationId = annotationId;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }
}
