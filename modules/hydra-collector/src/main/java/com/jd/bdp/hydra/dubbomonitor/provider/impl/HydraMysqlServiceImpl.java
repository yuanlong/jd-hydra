package com.jd.bdp.hydra.dubbomonitor.provider.impl;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.dubbomonitor.HydraService;
import com.jd.bdp.hydra.dubbomonitor.provider.impl.support.Configuration;
import com.jd.bdp.hydra.store.inter.InsertService;

public class HydraMysqlServiceImpl implements HydraService {

    private ArrayBlockingQueue<List<Span>> queue; //如果出现性能，可以应用disruptor来测试一下
    private int taskCount=3;
    private ExecutorService executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


    public HydraMysqlServiceImpl() {
        queue = new ArrayBlockingQueue<List<Span>>(2048);
        this.taskCount=3;
        for (int i = 0; i < taskCount; i++) {
            executors.execute(new InsertTask());
        }
    }

    public HydraMysqlServiceImpl(Configuration c) {
       // int queueSize= c.getTaskCount() == null ? 2048 : c.getQueueSize();
    	int queueSize=c.getQueueSize()==null?2048:c.getQueueSize();
        this.taskCount = c.getTaskCount() == null ? 3 : c.getTaskCount();
        queue = new ArrayBlockingQueue<List<Span>>(queueSize);
        for (int i = 0; i < taskCount; i++) {
            executors.execute(new InsertTask());
        }
    }

    class InsertTask implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    List<Span> span = queue.take();
                    if (span != null) {
                        for (Span s : span) {
                            insertService.addSpan(s);
                            insertService.addTrace(s);
                            insertService.addAnnotation(s);
                        }
                    }
                } catch (InterruptedException e) {
                    //ig
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public boolean push(List<Span> span) throws IOException {
        return queue.add(span);
    }

    private InsertService insertService;

    public void setInsertService(InsertService insertService) {
        this.insertService = insertService;
    }


}