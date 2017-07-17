package com.java.nio;

public class TimeClient {
    private static final int port = 8080;
    
    private static final String ip = "127.0.0.1";
    
    public static void main(String[] args) {
        TimeClientHandler clientHandler = new TimeClientHandler(ip, port);
        new Thread(clientHandler, "TimeClient-001").start();
    }
}
