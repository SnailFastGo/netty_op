package com.java.rpc;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @project netty_op
 * @file RPCUtils.java 创建时间:2017年7月17日上午11:32:23
 * @description RPC工具类
 * @author dzn
 * @version 1.0
 *
 */
public class RPCUtils {
    public static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    
    public static final HashMap<String, Class> serviceRegistry = new HashMap<String, Class>();
}
