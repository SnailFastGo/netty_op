package com.java.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class MultiplexerTimeServer implements Runnable {

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private volatile boolean stop;

    public MultiplexerTimeServer(int port) {
        try {
            //打开一个selector 在linux上如果要用EPoll, 需要在启动参数中添加如下配置
            //Djava.nio.channels.spi.SelectorProvider=sun.nio.ch.EPollSelectorProvider
            selector = Selector.open();
            
            //打开一个channel
            serverSocketChannel = ServerSocketChannel.open();
            
            //设置cnannel为非阻塞的，向selector注册的channel都应该是非阻塞的
            serverSocketChannel.configureBlocking(false);
            
            //启动server，并监听port指定的端口
            serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);
            System.out.println("Time Server is started in port : " + port);
            
            //把channel注册到selector中
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     *@description 停止服务
     *@time 创建时间:2017年7月17日下午5:56:20
     *@author dzn
     */
    public void stop() {
        stop = true;
    }

    @Override
    public void run() {
        try {
            while (!stop) {
                SelectionKey selectionKey = null;
                selector.select(1000);
                
                //获取就绪的key
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();
                
                //遍历就绪的key
                while (iterator.hasNext()) {
                    selectionKey = iterator.next();
                    iterator.remove();
                    //处理每一个就绪的key
                    try{
                        handleInput(selectionKey);
                    }catch(IOException ioe){
                        if (null != selectionKey) {
                            selectionKey.cancel();
                            if (null != selectionKey.channel()) {
                                selectionKey.channel().close();
                            }
                        }
                    }
                }
            }
            //循环结束, 停止服务
            if(null != selector){
                selector.close();
            }
        }catch (Exception e) {
                e.printStackTrace();
        }
    }

    /**
     *@description 处理就绪的key
     *@time 创建时间:2017年7月17日下午12:44:32
     *@param key
     *@throws Exception
     *@author dzn
     * @throws IOException 
     */
    private void handleInput(SelectionKey key) throws IOException {
        if(key.isValid()){
            if(key.isAcceptable()){
                //处理Acceptable的key
                ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
                //创建服务器端的Socket
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                //将服务器端的Socket注册到selector
                sc.register(selector, SelectionKey.OP_READ);
            }else if(key.isReadable()){
                //处理服务器端Readable的key
                SocketChannel sc = (SocketChannel)key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                //从服务器端channel中读取数据
                int readBytes = sc.read(readBuffer);
                if(readBytes > 0){
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    //从ByteBuffer中读取数据
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("Time server receive order : " + body);
                    String resp = "";
                    String order = "Query time order";
                    if(order.equalsIgnoreCase(body)){
                        resp = new Date().toString();
                    }else{
                        resp = "BAD ORDER";
                    }
                    doWrite(sc, resp);
                }else if(readBytes < 0){
                    key.cancel();
                    sc.close();
                }else{
                    //读到0字节直接忽略
                    ;
                }
            }else if(key.isWritable()){
                //处理Writable的key
                System.out.println("服务器不会主动给Client发送数据");
            }
        }
    }
    
    /**
     *@description 向客户端发送数据
     *@time 创建时间:2017年7月17日下午5:55:47
     *@param sc
     *@param response
     *@throws IOException
     *@author dzn
     */
    public static void doWrite(SocketChannel sc, String response) throws IOException{
        if(null != response && response.trim().length() > 0){
            byte[] bytes = response.getBytes();
            ByteBuffer bb = ByteBuffer.allocate(bytes.length);
            //向ByteBuffer中写入数据
            bb.put(bytes);
            bb.flip();
            sc.write(bb);
        }
    }

}
