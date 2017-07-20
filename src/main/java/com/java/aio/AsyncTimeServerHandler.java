package com.java.aio;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * @project 服务端的处理类
 * @file AsyncTimeServerHandler.java 创建时间:2017年7月17日下午8:41:34
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @author dzn
 * @version 1.0
 *
 */
public class AsyncTimeServerHandler implements Runnable{
    
    /**
     * @description 服务器端监听端口号
     * @value value:port
     */
    private int port;
    
    /**
     * @description 阻塞主线程
     * @value value:latch
     */
    CountDownLatch latch;
    
    /**
     * @description 服务器端Socket
     * @value value:asynchronousServerSocketChannel
     */
    AsynchronousServerSocketChannel asynchronousServerSocketChannel;
    
    public AsyncTimeServerHandler(int port){
        this.port = port;
        try{
            //创建AsyncTimeServerHandler并启动服务端Socket
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            asynchronousServerSocketChannel.bind(new InetSocketAddress(this.port));
            System.out.println("服务器已启动, 端口号: " + port);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch= new CountDownLatch(1);
        doAccept();
        try {
            this.latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     *@description 注册接收客户端连接请求的处理类
     *@time 创建时间:2017年7月17日下午8:43:42
     *@author dzn
     */
    private void doAccept(){
        //注册一个接收请求的处理类
        this.asynchronousServerSocketChannel.accept(this, new AcceptCompletionHandler());
    }
    
}
