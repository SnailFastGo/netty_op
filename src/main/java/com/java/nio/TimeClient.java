package com.java.nio;

public class TimeClient {
    private static final int port = 8080;
    
    private static final String ip = "127.0.0.1";
    
    public static void main(String[] args) throws Exception {
        TimeClientHandler clientHandler = new TimeClientHandler(ip, port);
        new Thread(clientHandler, "TimeClient-001").start();
        //等待客户端连接服务器
        Thread.sleep(1000);
        NioWriterWrapper nioWriterWrapper = NioWriterWrapper.getInstance();
        String order = "Query time order";
        nioWriterWrapper.writeMsg(order);
    }
}
