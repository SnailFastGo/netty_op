package com.netty.io;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @project 服务器端启动类
 * @file TimeServer.java 创建时间:2017年7月20日下午8:37:59
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @author dzn
 * @version 1.0
 *
 */
public class TimeServer {
    
    /**
     * @description 监听的端口号
     * @value value:port
     */
    private static int port = 8080;
    
    public static void main(String[] args) throws InterruptedException {
        TimeServer server = new TimeServer();
        server.bind(port);
    }
    
    /**
     *@description 监听指定端口
     *@time 创建时间:2017年7月21日下午3:50:26
     *@param port
     *@throws InterruptedException
     *@author dzn
     */
    public void bind(int port) throws InterruptedException{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
            .option(ChannelOption.SO_BACKLOG, 1024).childHandler(new ChildChannelHandler());
            ChannelFuture cf = server.bind(port).sync();
            cf.channel().closeFuture().sync();
        }finally{
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
    
    /**
     * @project 处理链
     * @file TimeServer.java 创建时间:2017年7月21日下午3:50:46
     * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
     * @author dzn
     * @version 1.0
     *
     */
    private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{
        
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new TimeServerHandler());
        }
        
    }
}
