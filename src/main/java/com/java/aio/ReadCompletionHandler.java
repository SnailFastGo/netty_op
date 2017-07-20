package com.java.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;

/**
 * @project 读取客户端请求数据的处理类
 * @file ReadCompletionHandler.java 创建时间:2017年7月20日上午11:27:11
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @author dzn
 * @version 1.0
 *
 */
public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer>{

    private AsynchronousSocketChannel channel;
    
    public ReadCompletionHandler(AsynchronousSocketChannel channel) {
        if(null == this.channel){
            this.channel = channel;
        }
    }

    /**
     *@description 读取客户端数据成功之后回调
     *@time 创建时间:2017年7月18日上午11:04:30
     *@param result
     *@param attachment
     *@author dzn
     */
    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();
        byte[] body = new byte[attachment.remaining()];
        attachment.get(body);
        try {
            String req = new String(body, "UTF-8");
            System.out.println("成功从客户端读取数据 : " + req);
            String currentTime = "";
            if("Query time order".equalsIgnoreCase(req)){
                currentTime = new Date(System.currentTimeMillis()).toString();
            }else{
                currentTime = "BAD ORDER";
            }
            //把响应写到客户端
            doWrite(currentTime);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     *@description 读取客户端数据失败之后回调
     *@time 创建时间:2017年7月20日下午3:32:24
     *@param exc
     *@param attachment
     *@author dzn
     */
    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            this.channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     *@description 向客户端写入响应数据
     *@time 创建时间:2017年7月20日下午3:32:44
     *@param currentTime
     *@author dzn
     */
    private void doWrite(String currentTime){
        if(null != currentTime && currentTime.trim().length() > 0){
            System.out.println("向客户端写入数据 : " + currentTime);
            byte[] bytes = currentTime.getBytes();
            ByteBuffer bb = ByteBuffer.allocate(bytes.length);
            bb.put(bytes);
            bb.flip();
            this.channel.write(bb, bb, new CompletionHandler<Integer, ByteBuffer>(){

                /**
                 *@description 向客户端输出完响应数据之后回调
                 *@time 创建时间:2017年7月20日下午3:33:23
                 *@param result
                 *@param buffer
                 *@author dzn
                 */
                @Override
                public void completed(Integer result, ByteBuffer buffer) {
                    if(buffer.hasRemaining()){
                        channel.write(bb, bb, this);
                    }
                }

                /**
                 *@description 向客户端输出响应数据失败之后回调
                 *@time 创建时间:2017年7月20日下午3:34:01
                 *@param exc
                 *@param attachment
                 *@author dzn
                 */
                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    try {
                        channel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
