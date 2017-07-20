package com.java.aio;

/**
 * @project Server启动类
 * @file TimeServer.java 创建时间:2017年7月17日下午8:41:10
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @author dzn
 * @version 1.0
 *
 */
public class TimeServer {
    
    private static int port = 8080;
    
    public static void main(String[] args) {
        AsyncTimeServerHandler timeserver = new AsyncTimeServerHandler(port);
        new Thread(timeserver, "AIO-AsyncTimeServerHandler-001").start();
    }
}
