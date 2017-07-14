package com.java.io.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimeServerHandlerExecutePool {
    
    private ExecutorService executor;
    
    public TimeServerHandlerExecutePool(int maxPoolSize, int queueSize){
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        executor = new ThreadPoolExecutor(availableProcessors, maxPoolSize, 120L,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueSize));
    }
    
    public void execute(Runnable task){
        executor.execute(task);
    }
}
