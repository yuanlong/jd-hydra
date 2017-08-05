package com.jd.bdp.hydra.agent;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.jd.bdp.hydra.Annotation;
import com.jd.bdp.hydra.AnnotationType;
import com.jd.bdp.hydra.Span;

import java.util.UUID;

public class Tracer {
    private static final Logger logger = LoggerFactory.getLogger(Tracer.class);
    private static final ThreadLocal<Span> spanThreadLocal = new ThreadLocal<Span>();
    private Transfer transfer;

    public void setTransfer(Transfer transfer) {
        this.transfer = transfer;
    }

    private Tracer() {
    }

    private static class TraceHolder {
        static Tracer instance = new Tracer();
    }

    public static Tracer getTracer() {
        return TraceHolder.instance;
    }

    public Span getCurrentSpan() {
        return spanThreadLocal.get();
    }

    public Span genSpan(String traceId, String pid, String id, String service, String spanname) {
        Span span = new Span();
        span.setTraceId(traceId);
        span.setSpanId(id);
        span.setParentId(pid);
        span.setServiceId(service);
        span.setSpanName(spanname);
        return span;
    }

    /**
     * 只有consumer会生成RootSpan
     */
    public Span genRootSpan(String service, String spanname) {
        Span span = new Span();
        span.setTraceId(getId());
        span.setSpanId(getId());
        span.setServiceId(service);
        span.setSpanName(spanname);
        return span;
    }

    public void addBinaryAnntation(Annotation b) {
        Span span = spanThreadLocal.get();
        if (span != null) {
            span.addAnnotation(b);
        }
    }

    //cs annotation
    public void clientSendRecord(Span span, String ip, int port, long start) {
        Annotation annotation = new Annotation();
        annotation.setType(AnnotationType.ClientSend);
        annotation.setTime(start);
        annotation.setIp(ip);
        annotation.setPort(port);
        annotation.setSpanId(span.getSpanId());
        span.addAnnotation(annotation);
    }


    //cr annotation
    public void clientReceiveRecord(Span span, String ip, int port, long end) {
        Annotation annotation = new Annotation();
        annotation.setType(AnnotationType.ClientRecevie);
        annotation.setIp(ip);
        annotation.setPort(port);
        annotation.setTime(end);
        annotation.setSpanId(span.getSpanId());
        span.addAnnotation(annotation);
        transfer.send(span);
    }

    //sr annotation
    public void serverReceiveRecord(Span span, String ip, int port, long start) {
        Annotation annotation = new Annotation();
        annotation.setType(AnnotationType.ServerReceive);
        annotation.setIp(ip);
        annotation.setPort(port);
        annotation.setTime(start);
        annotation.setSpanId(span.getSpanId());
        span.addAnnotation(annotation);
        spanThreadLocal.set(span);
    }

    //ss annotation
    public void serverSendRecord(Span span, String ip, int port, long end) {
        Annotation annotation = new Annotation();
        annotation.setType(AnnotationType.ServerSend);
        annotation.setTime(end);
        annotation.setIp(ip);
        annotation.setPort(port);
        annotation.setSpanId(span.getSpanId());
        span.addAnnotation(annotation);
        spanThreadLocal.remove();
        transfer.send(span);
    }

    public static String getId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}

