package com.netty.halfpackage;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @project 服务器端启动类(没有处理TCP半包粘包处理)
 * @file TimeServer.java 创建时间:2017年7月20日下午8:37:59
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @author dzn
 * @version 1.0
 *
 */
public class ErrorTimeServer {
    
    /**
     * @description 监听的端口号
     * @value value:port
     */
    private static int port = 8080;
    
    public static void main(String[] args) throws InterruptedException {
        ErrorTimeServer server = new ErrorTimeServer();
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
        //分配任务线程池
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        
        //执行任务线程池
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try{
            //netty Server端
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChildChannelHandler());
            
            //启动netty服务器
            ChannelFuture cf = server.bind(port).sync();
            System.out.println("服务器已启动, 监控端口号为 : " + port);
            
            //等待服务器端关闭
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
            //对输入数据进行业务逻辑处理
            ch.pipeline().addLast(new ErrorTimeServerHandler());
        }
        
    }
}
