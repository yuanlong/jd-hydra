package com.jd.bdp.hydra.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import com.sohu.idcenter.IdWorker;
/**
 * 前端应用系统，在web.xml中配置该filter,在该filter产生traceid,并且开始日志记录，
 * service 类通过dubbo服务调用，利用MDCHydraFilter进行
 * 调用上下文透传,
 * 对于非web应用前端，无需配置该filter,该filter 启发于org.springframework.web.filter.Log4jNestedDiagnosticContextFilter类
 * @author Administrator
 *
 */
public class Log4jMapDiagnosticContextFilter extends AbstractRequestLoggingFilter {

    /** Logger available to subclasses */
    protected final Logger log4jLogger = Logger.getLogger(getClass());

    /**
     * Logs the before-request message through Log4J and adds a message the
     * Log4J NDC before the request is processed.
     */
    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        if(worker==null){
            worker=new IdWorker(workerId, datacenterId, 0L);
        }
        MDC.put("traceId",  String.valueOf(worker.getId()));
       /* if (log4jLogger.isDebugEnabled()) {
            log4jLogger.debug(message);
        }*/
        //NDC.push(getNestedDiagnosticContextMessage(request));
        log4jLogger.info(message);
    }

    /**
     * Determine the message to be pushed onto the Log4J nested diagnostic
     * context.
     * <p>
     * Default is a plain request log message without prefix or suffix.
     * 
     * @param request
     *            current HTTP request
     * @return the message to be pushed onto the Log4J NDC
     * @see #createMessage
     */
    /*
     * protected String getNestedDiagnosticContextMessage(HttpServletRequest
     * request) { return createMessage(request, "", ""); }
     */

    /**
     * Removes the log message from the Log4J NDC after the request is processed
     * and logs the after-request message through Log4J.
     */
    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        /*NDC.pop();
        if (NDC.getDepth() == 0) {
            NDC.remove();
        }*/
        /*if (log4jLogger.isDebugEnabled()) {
            log4jLogger.debug(message);
        }*/
        log4jLogger.info(message);
        MDC.remove("traceId");
    }

    private Long workerId = 0L;
    private Long datacenterId = 0L;

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public void setDatacenterId(Long datacenterId) {
        this.datacenterId = datacenterId;
    }

    private IdWorker worker;

    @Override
    protected void initFilterBean() throws ServletException {
        if (getFilterConfig().getInitParameter("workerId") != null) {
            workerId = Long.valueOf(getFilterConfig().getInitParameter("workerId"));
        }

        if (getFilterConfig().getInitParameter("datacenterId") != null) {
            datacenterId = Long.valueOf(getFilterConfig().getInitParameter("datacenterId"));
        }
        worker = new IdWorker(workerId, datacenterId, 0L);
        super.initFilterBean();
    }

}
