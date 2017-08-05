package com.jd.bdp.hydra.agent;

import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.TraceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author:杨果
 * @date:16/6/1 下午9:21
 *
 * Description:
 *
 */
public class Transfer {
    private static Logger logger = LoggerFactory.getLogger(Transfer.class);
    private ArrayBlockingQueue<Span> queue;
    private List<Span> spansCache;
    private TransferTask task;
    private TraceService traceService;

    public void setTraceService(TraceService traceService) {
        this.traceService = traceService;
    }

    public Transfer() {
        this.queue = new ArrayBlockingQueue<Span>(1024);
        this.spansCache = new ArrayList<Span>();
        this.task = new TransferTask();
    }

    private class TransferTask extends Thread {
        TransferTask() {
            this.setName("TransferTask-Thread");
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Span first = queue.take();
                    spansCache.add(first);
                    queue.drainTo(spansCache);
                    traceService.sendSpan(spansCache);
                    spansCache.clear();
                } catch (Throwable e) {
                    logger.info("TraceService send span ignore,because of:{}", e.getMessage());
                }
            }
        }
    }

    public void send(Span span) {
        try {
            queue.add(span);
        } catch (Exception e) {
            logger.info("Span ignore,because of:{}", e.getMessage());
        }
    }

    public void start() throws Exception {
        if (traceService != null && !task.isAlive()) {
            task.start();
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    cancel();
                }
            });
        } else if (traceService == null) {
            throw new Exception("TraceServie is null.can't starting Transfer");
        }
    }

    private void cancel() {
        task.interrupt();
    }
}
