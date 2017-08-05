package com.jd.bdp.hydra.dubbomonitor.provider.impl;

import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.dubbomonitor.provider.impl.support.Configuration;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HydraMysqlServiceImpl {

    private ArrayBlockingQueue<List<Span>> queue;
    private int taskCount = 3;
    private ExecutorService executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


    public HydraMysqlServiceImpl() {
        queue = new ArrayBlockingQueue<List<Span>>(2048);
        this.taskCount = 3;
        for (int i = 0; i < taskCount; i++) {
            executors.execute(new InsertTask());
        }
    }

    public HydraMysqlServiceImpl(Configuration c) {
        int queueSize = c.getTaskCount() == null ? 2048 : c.getQueueSize();
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
}