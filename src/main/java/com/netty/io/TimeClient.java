package com.netty.io;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @project 客户单启动类
 * @file TimeClient.java 创建时间:2017年7月21日下午4:02:35
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @author dzn
 * @version 1.0
 *
 */
public class TimeClient {
    
    /**
     * @description 连接服务器的端口号
     * @value value:port
     */
    private static int port = 8080;
    
    public static void main(String[] args) throws InterruptedException {
        TimeClient client = new TimeClient();
        client.connect("127.0.0.1", port);
    }
    
    /**
     *@description 连接服务器
     *@time 创建时间:2017年7月21日下午4:15:50
     *@param host
     *@param port
     *@throws InterruptedException
     *@author dzn
     */
    public void connect(String host, int port) throws InterruptedException{
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap boot = new Bootstrap();
            boot.group(group)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY, true)
            .handler(new ChannelInitializer<SocketChannel>() {
                
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TimeClientHandler());
                }
                
            });
            ChannelFuture future = boot.connect(host, port).sync();
            future.channel().closeFuture().sync();
        }finally{
            group.shutdownGracefully();
        }
    }
}
