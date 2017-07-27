package com.netty.halfpackage;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @project 服务器对客户端请求的处理类
 * @file TimeServerHandler.java 创建时间:2017年7月24日下午2:30:14
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @author dzn
 * @version 1.0
 *
 */
public class ErrorTimeServerHandler extends ChannelInboundHandlerAdapter{
    
    private int counter;

    /**
     *@description 接收客户端的请求数据
     *@time 创建时间:2017年7月24日下午2:30:30
     *@param ctx
     *@param msg
     *@throws Exception
     *@author dzn
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8").substring(0, req.length - System.getProperty("line.separator").length());
        System.out.println("服务器端收到请求数据 : " + body + ", 计数器的值为 : " + ++counter);
        String currentTime = "";
        if("QUERY TIME ORDER".equalsIgnoreCase(body)){
            currentTime = new Date(System.currentTimeMillis()).toString();
        }else{
            currentTime = "BAD ORDER";
        }
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.channel().writeAndFlush(resp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
    
    
}
