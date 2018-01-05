package com.netty.io;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;

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
                    //使用'\r\n'作为输入分隔符
                    ByteBuf byteBuf = Delimiters.lineDelimiter()[0];
                    ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, byteBuf));
                    
                    //对输入数据进行字符串解码
                    ch.pipeline().addLast(new StringDecoder());
                    
                    //对输入数据进行业务逻辑处理
                    ch.pipeline().addLast(new TimeClientInHandler3());
                    ch.pipeline().addLast(new TimeClientInHandler2());
                    ch.pipeline().addLast(new TimeClientInHandler());
                    ch.pipeline().addLast(new TimeClientOutHandler3());
                    ch.pipeline().addLast(new TimeClientOutHandler2());
                    ch.pipeline().addLast(new TimeClientOutHandler());
                    
                    //对输出数据进行字符串编码
                    ch.pipeline().addLast(new StringEncoder());
                }
                
            });
            
            //连接服务器
            ChannelFuture future = boot.connect(host, port).sync();
            
            //向服务器发送数据
            String name = "zhangsan\r\n" ;
            Channel channel = future.channel();
            channel.writeAndFlush(name);
            
            //等待客户端Channel关闭
            future.channel().closeFuture().sync();
            
            //获取客户端Channle的属性数据
            Object result = channel.attr(AttributeKey.valueOf("mykey")).get();
            System.out.println("读取到客户端Channel附加的属性-mykey, 属性值为 : " + result);
        }finally{
            group.shutdownGracefully();
        }
    }
}
