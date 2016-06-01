package com.jd.bdp.hydra;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Span implements Serializable {
    private static final long serialVersionUID = -8494927317993745408L;
    private String spanId;
    private String spanName;
    private String traceId;
    private String parentId;
    private String serviceId;
    private List<Annotation> annotations;

    public Span() {
        annotations = new ArrayList<Annotation>();
    }

    public void addAnnotation(Annotation a) {
        annotations.add(a);
    }

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public String getSpanName() {
        return spanName;
    }

    public void setSpanName(String spanName) {
        this.spanName = spanName;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    @Override
    public String toString() {
        return "Span{" +
                "spanId='" + spanId + '\'' +
                ", spanName='" + spanName + '\'' +
                ", traceId='" + traceId + '\'' +
                ", parentId='" + parentId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", annotations=" + annotations +
                '}';
    }
}
