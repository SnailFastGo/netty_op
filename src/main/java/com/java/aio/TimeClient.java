package com.java.aio;

/**
 * @project Client启动类
 * @file TimeClient.java 创建时间:2017年7月20日下午3:59:02
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @author dzn
 * @version 1.0
 *
 */
public class TimeClient {
    private static int port = 8080;
    
    public static void main(String[] args) {
        AsyncTimeClientHandler asyncTimeClientHandler = new AsyncTimeClientHandler("127.0.0.1", port);
        
        new Thread(asyncTimeClientHandler, "AIO-AsyncTimeClientHandler-001").start();
    }
}
