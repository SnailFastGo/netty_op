package com.java.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * @project 知识图谱
 * @file AsyncTimeClientHandler.java 创建时间:2017年7月20日下午3:35:04
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @author dzn
 * @version 1.0
 *
 */
public class AsyncTimeClientHandler implements CompletionHandler<Void, AsyncTimeClientHandler> ,Runnable{
    
    /**
     * @description 服务器端连接地址
     * @value value:host
     */
    private String host;
    
    /**
     * @description 服务器端连接端口号
     * @value value:port
     */
    private int port;
    
    /**
     * @description 客户端Socket
     * @value value:asynchronousSocketChannel
     */
    private AsynchronousSocketChannel asynchronousSocketChannel;
    
    /**
     * @description 同步阻塞
     * @value value:latch
     */
    private CountDownLatch latch; 

    public AsyncTimeClientHandler(String host, int port) {
        this.port = port;
        this.host = host;
        try{
            this.asynchronousSocketChannel = AsynchronousSocketChannel.open();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     *@description 为异步通信创建连接
     *@time 创建时间:2017年7月20日上午10:35:48
     *@author dzn
     */
    @Override
    public void run() {
        latch = new CountDownLatch(1);
        //连接服务器, 并注册handler
        this.asynchronousSocketChannel.connect(new InetSocketAddress(this.host, this.port), this, this);
        try {
            //主线程阻塞住
            this.latch.await();
            //关闭客户端连接
            this.asynchronousSocketChannel.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     *@description 成功连接到服务器之后回调
     *@time 创建时间:2017年7月20日下午3:38:05
     *@param result
     *@param attachment
     *@author dzn
     */
    @Override
    public void completed(Void result, AsyncTimeClientHandler attachment) {
        System.out.println("成功连接服务器");
        String req = "QUERY TIME ORDER";
        byte[] reqBytes = req.getBytes();
        ByteBuffer bb = ByteBuffer.allocate(reqBytes.length);
        bb.put(reqBytes);
        bb.flip();
        System.out.println("向服务器端发送请求数据 : " + req);
        this.asynchronousSocketChannel.write(bb, bb, new CompletionHandler<Integer, ByteBuffer>(){

            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                
                if(attachment.hasRemaining()){
                    //没有写完的话继续写
                    asynchronousSocketChannel.write(attachment, attachment, this);
                }else{
                    //写完了就接收服务器的响应
                    ByteBuffer bb = ByteBuffer.allocate(1024);
                    asynchronousSocketChannel.read(bb, bb, new CompletionHandler<Integer, ByteBuffer>(){

                        /**
                         *@description 从服务器读取数据成功之后回调
                         *@time 创建时间:2017年7月20日下午3:39:45
                         *@param result
                         *@param attachment
                         *@author dzn
                         */
                        @Override
                        public void completed(Integer result, ByteBuffer attachment) {
                            attachment.flip();
                            byte[] bytes = new byte[attachment.remaining()];
                            attachment.get(bytes);
                            try {
                                String body = new String(bytes, "UTF-8");
                                System.out.println("成功读取服务器端响应数据 : " +  body);
                                latch.countDown();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }

                        /**
                         *@description 读服务器的响应数据失败
                         *@time 创建时间:2017年7月20日上午10:56:53
                         *@param exc
                         *@param attachment
                         *@author dzn
                         */
                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {
                            latch.countDown();
                        }
                        
                    });
                }
            }

            /**
             *@description 像服务器写数据失败
             *@time 创建时间:2017年7月20日上午10:57:31
             *@param exc
             *@param attachment
             *@author dzn
             */
            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                latch.countDown();                
            }
            
        });
    }   

    /**
     *@description 连接服务器失败之后回调
     *@time 创建时间:2017年7月20日上午10:58:34
     *@param exc
     *@param attachment
     *@author dzn
     */
    @Override
    public void failed(Throwable exc, AsyncTimeClientHandler attachment) {
        this.latch.countDown();
    }
    
}
