package com.netty.halfpackage;

import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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
public class RightTimeClientHandler extends ChannelInboundHandlerAdapter{

    private static final Logger logger = Logger.getLogger(RightTimeClientHandler.class.getName());
    
    private int count;
    
    private byte[] req;
    
    public RightTimeClientHandler(){
        //需要发送的请求数据, 末尾加上换行符
        this.req = ("QUERY TIME ORDER" + System.getProperty("line.separator")).getBytes();
    }
    
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
        String body = (String)msg;
        System.out.println("Now is : " + body + ", the counter is : " + ++this.count);
    }

    /**
     *@description 客户端成功连接到服务器之后向服务器发送数据
     *@time 创建时间:2017年7月26日下午7:51:31
     *@param ctx
     *@throws Exception
     *@author dzn
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf message = null;
        for(int i = 0; i < 100; i ++){
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.channel().writeAndFlush(message);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("出现异常 : " + cause.getMessage());
        ctx.close();
    }
    
}
