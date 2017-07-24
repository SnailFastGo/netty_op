package com.netty.io;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
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
public class TimeServerHandler extends ChannelInboundHandlerAdapter{

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
        if(msg instanceof ByteBuf){
            ByteBuf req = (ByteBuf)msg;
            String content = req.toString(Charset.defaultCharset());
            System.out.println("接收到ByteBuf类型的请求数据 : " + content);
            ctx.channel().writeAndFlush(msg);
        }else if(msg instanceof String){
            System.out.println("接收到String类型的请求数据 : " + msg.toString());
            ctx.channel().writeAndFlush(msg + "\r\n");
            System.out.println("向客户端返回响应数据 : " + msg);
        }
    }
    
}
