/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jd.bdp.hydra.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.remoting.TimeoutException;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcInvocation;
import com.jd.bdp.hydra.Annotation;
import com.jd.bdp.hydra.AnnotationType;
import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.agent.Tracer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Activate(group = {Constants.PROVIDER, Constants.CONSUMER})
public class HydraFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(HydraFilter.class);
    public static final String TID = "traceId";
    public static final String SID = "spanId";
    public static final String PID = "parentId";

    private Tracer tracer = Tracer.getTracer();

    // 调用过程拦截
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        long start = System.currentTimeMillis();
        RpcContext context = RpcContext.getContext();
        boolean isConsumerSide = context.isConsumerSide();
        Span span = null;
        String ip = context.getLocalHost();
        int port = context.getLocalPort();
        try {
            if (context.isConsumerSide()) {
                Span currentSpan = tracer.getCurrentSpan();
                if (currentSpan == null) {
                    span = tracer.genRootSpan(context.getInvoker().getInterface().getCanonicalName(), context.getMethodName());
                } else {
                    span = tracer.genSpan(currentSpan.getTraceId(), currentSpan.getSpanId(), Tracer.getId(), context.getInvoker().getInterface().getCanonicalName(), context.getMethodName());
                }
            } else if (context.isProviderSide()) {
                String traceId, parentId, spanId;
                traceId = invocation.getAttachment(TID);
                parentId = invocation.getAttachment(PID);
                spanId = invocation.getAttachment(SID);
                span = tracer.genSpan(traceId, parentId, spanId, context.getInvoker().getInterface().getCanonicalName(), context.getMethodName());
            }
            invokerBefore(invocation, span, ip, port, start);
            RpcInvocation invocationTmp = (RpcInvocation) invocation;
            setAttachment(span, invocationTmp);
            Result result = invoker.invoke(invocation);
            if (result.getException() != null) {
                catchException(result.getException(), ip, port);
            }
            return result;
        } catch (RpcException e) {
            if (e.getCause() != null && e.getCause() instanceof TimeoutException) {
                catchTimeoutException(e, ip, port);
            } else {
                catchException(e, ip, port);
            }
            throw e;
        } finally {
            if (span != null) {
                long end = System.currentTimeMillis();
                invokerAfter(invocation, ip, port, span, end, isConsumerSide);
            }
        }
    }

    private void catchTimeoutException(RpcException e, String ip, int port) {
        Annotation exAnnotation = new Annotation();
        exAnnotation.setType(AnnotationType.DubboException);
        exAnnotation.setValue(e.getMessage());
        exAnnotation.setIp(ip);
        exAnnotation.setPort(port);
        tracer.addBinaryAnntation(exAnnotation);
    }

    private void catchException(Throwable e, String ip, int port) {
        Annotation exAnnotation = new Annotation();
        exAnnotation.setType(AnnotationType.DubboException);
        exAnnotation.setValue(e.getMessage());
        exAnnotation.setIp(ip);
        exAnnotation.setPort(port);
        tracer.addBinaryAnntation(exAnnotation);
    }

    private void setAttachment(Span span, RpcInvocation invocation) {
        invocation.setAttachment(PID, span.getParentId() != null ? String.valueOf(span.getParentId()) : null);
        invocation.setAttachment(SID, span.getSpanId() != null ? String.valueOf(span.getSpanId()) : null);
        invocation.setAttachment(TID, span.getTraceId() != null ? String.valueOf(span.getTraceId()) : null);
    }

    private void invokerAfter(Invocation invocation, String ip, int port, Span span, long end, boolean isConsumerSide) {
        if (checkFilter(invocation)) {
            if (isConsumerSide) {
                tracer.clientReceiveRecord(span, ip, port, end);
            } else {
                tracer.serverSendRecord(span, ip, port, end);
            }
        }
    }

    private void invokerBefore(Invocation invocation, Span span, String ip, int port, long start) {
        if (checkFilter(invocation)) {
            RpcContext context = RpcContext.getContext();
            if (context.isConsumerSide()) {
                tracer.clientSendRecord(span, ip, port, start);
            } else if (context.isProviderSide()) {
                tracer.serverReceiveRecord(span, ip, port, start);
            }
        }
    }

    private static boolean checkFilter(Invocation invocation) {
        String interfaceCanonicalName = invocation.getInvoker().getInterface().getCanonicalName();
        if ("com.alibaba.dubbo.monitor.MonitorService".equals(interfaceCanonicalName)) {
            return false;
        }
        return true;
    }

    /*加载Filter的时候加载hydra配置上下文*/
    static {
        logger.info("Hydra filter is loading hydra-config file...");
        String resourceName = "classpath*:hydra-config.xml";
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                resourceName
        });
        logger.info("Hydra config context is starting,config file path is:" + resourceName);
        context.start();
    }
}