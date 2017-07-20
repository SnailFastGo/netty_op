package com.java.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @project 服务端接收请求的处理类
 * @file AcceptCompletionHandler.java 创建时间:2017年7月20日上午11:04:27
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @author dzn
 * @version 1.0
 *
 */
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncTimeServerHandler>{

    /**
     *@description 接收客户端请求成功之后回调
     *@time 创建时间:2017年7月20日上午11:05:16
     *@param channel
     *@param serverHandler
     *@author dzn
     */
    @Override
    public void completed(AsynchronousSocketChannel channel,
            AsyncTimeServerHandler serverHandler) {
        
        System.out.println("成功接收到客户端连接 : " + channel.toString());
        
        //继续注册一个接收请求的处理类
        serverHandler.asynchronousServerSocketChannel.accept(serverHandler, this);
        
        //创建一个数据缓冲区
        ByteBuffer bb = ByteBuffer.allocate(1024);
        
        //去读取客户端发送的数据，并注册一个数据处理类
        channel.read(bb, bb, new ReadCompletionHandler(channel));
    }

    /**
     *@description 接收请求失败之后回调
     *@time 创建时间:2017年7月20日上午11:26:49
     *@param exc
     *@param serverHandler
     *@author dzn
     */
    @Override
    public void failed(Throwable exc, AsyncTimeServerHandler serverHandler) {
        serverHandler.latch.countDown();
    }
    
}
