package com.netty.io;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @project 客户端接收服务器响应的处理类
 * @file TimeClientHandler.java 创建时间:2017年7月24日下午2:30:46
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @author dzn
 * @version 1.0
 *
 */
public class TimeClientInHandler3 extends ChannelInboundHandlerAdapter{

    /**
     *@description 接收服务器的响应数据
     *@time 创建时间:2017年7月24日下午2:31:09
     *@param ctx
     *@param msg
     *@throws Exception
     *@author dzn
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("TimeClientHandler3");
        ctx.fireChannelRead(msg);
    }
    
}
