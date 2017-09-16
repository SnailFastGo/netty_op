package com.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @project 基于Netty的文件服务器
 * @file NettyHttpFileServer.java 创建时间:2017年9月16日下午12:04:49
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @author dzn
 * @version 1.0
 *
 */
public class NettyHttpFileServer {
    
    /**
     * @description 访问的基本路径
     * @value value:DEFAULT_URL
     */
    private static final String DEFAULT_URL = "/";
    
    public void run(final int port, final String url) throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workGroup)
                  .channel(NioServerSocketChannel.class)
                  .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //增加HTTP请求消息解码器
                        ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                        
                        //将多个HTTP消息转换成多个单一的FullHttpRequest或者FullHttpResponse
                        ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65535));
                        
                        //增加Http响应消息编码器
                        ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                        
                        //支持异步发送大码流
                        ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                        
                        //增加文件服务器的业务处理器
                        ch.pipeline().addLast("fileServerHandler", new NettyHttpFileServerHandler(url));
                    }
                  });
            ChannelFuture future = server.bind(port).sync();
            future.channel().closeFuture().sync();
        }finally{
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
    
    public static void main(String[] args) throws Exception {
        int port = 8080;
        String url = DEFAULT_URL;
        NettyHttpFileServer nettyHttpFileServer = new NettyHttpFileServer();
        nettyHttpFileServer.run(port, url);
    }
}
