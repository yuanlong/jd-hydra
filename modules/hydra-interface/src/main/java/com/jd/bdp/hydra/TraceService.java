package com.jd.bdp.hydra;

import java.util.List;

/**
 * @author:杨果
 * @date:16/5/26 下午2:17
 *
 * Description:
 *
 */
public interface TraceService {
    void sendSpan(List<Span> spans);
}
