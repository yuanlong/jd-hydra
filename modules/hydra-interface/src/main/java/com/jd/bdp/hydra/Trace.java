package com.jd.bdp.hydra;

import java.io.Serializable;

public class Trace implements Serializable {
    private static final long serialVersionUID = 535352885272916500L;
    private String traceId;
    private long duration;
    private long serviceId;
    private long time;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
