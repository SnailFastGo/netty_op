package com.java.rpc;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RPCUtils {
    public static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    
    public static final HashMap<String, Class> serviceRegistry = new HashMap<String, Class>();
}
