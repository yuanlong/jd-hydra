package com.jd.bdp.hydra;

/**
 * @author:杨果
 * @date:16/5/26 下午2:17
 *
 * Description:
 *
 */
public interface TraceService {
    void insertTrace(Trace trace);

    void insertSpan(Span span);

    void insertAnnotation(Annotation annotation);
}
