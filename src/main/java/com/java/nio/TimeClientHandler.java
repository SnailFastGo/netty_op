package com.java.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TimeClientHandler implements Runnable{

    private String host ;
    
    private int port;
    
    private Selector selector;
    
    private SocketChannel socketChannel;
    
    private volatile boolean stop;
    
    public TimeClientHandler(String host, int port) {
        this.host = host == null ? "127.0.0.1" : host;
        this.port = port;
        try{
            this.selector = Selector.open();
            this.socketChannel = SocketChannel.open();
            this.socketChannel.configureBlocking(false);
            NioWriterWrapper.getInstance().setSocketChannel(socketChannel);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(!this.stop){
            try{
                int readyNum = this.selector.select(1000);
                if(readyNum > 0){
                    Set<SelectionKey> selectedKeys = this.selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectedKeys.iterator();
                    SelectionKey key = null;
                    while(iterator.hasNext()){
                        key = iterator.next();
                        iterator.remove();
                        try{
                            handleInput(key);
                        }catch(Exception e){
                            if(null != key){
                                key.cancel();
                                if(null != key.channel()){
                                    key.channel().close();
                                }
                            }
                        }
                    }
                }
                
                
            }catch(Exception e){
                e.printStackTrace();
                System.exit(1);
            }
        }
        
        //多路复用器关闭后，所有注册在上面Channel和Pipe等资源多会被自动去注册关闭，不需要重复释放资源
        if(null != this.selector){
            try {
                this.selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /** 连接服务器
     *@description 相关说明
     *@time 创建时间:2017年7月17日下午2:26:26
     *@author dzn
     * @throws IOException 
     * @throws ClosedChannelException 
     */
    private void doConnect() throws IOException{
        if(this.socketChannel.connect(new InetSocketAddress(this.host, this.port))){
            //如果客户端连接服务器成功了，则向selector注册读取事件，并向服务器端发送命令
            this.socketChannel.register(this.selector, SelectionKey.OP_READ);
            doWrite(this.socketChannel);
        }else{
            //如果客户端连接服务器没有成功，则向selector注册连接事件
            this.socketChannel.register(this.selector, SelectionKey.OP_CONNECT);
        }
    }
    
    /** 向服务器端发送命令
     *@description 相关说明
     *@time 创建时间:2017年7月17日下午2:50:23
     *@param socketChannel
     *@throws IOException
     *@author dzn
     */
    private void doWrite(SocketChannel socketChannel) throws IOException{
        byte[] req = "QUERY TIME ORDER".getBytes();
        ByteBuffer bb = ByteBuffer.allocate(req.length);
        bb.put(req);
        bb.flip();
        socketChannel.write(bb);
        if(!bb.hasRemaining()){
            System.out.println("Send order 2 server succeed");
        }
    }
    
    /** 对接收到的key进行处理
     *@description 相关说明
     *@time 创建时间:2017年7月17日下午2:55:56
     *@param key
     *@throws IOException
     *@author dzn
     */
    private void handleInput(SelectionKey key) throws IOException{
        if(key.isValid()){
           SocketChannel sc = (SocketChannel)key.channel();
           if(key.isConnectable()){
               if(sc.finishConnect()){
                   sc.register(this.selector, SelectionKey.OP_READ);
                   this.doWrite(sc);
               }else{
                   System.exit(1);//连接失败，进程退出
               }
           }
           
           if(key.isReadable()){
               ByteBuffer bb = ByteBuffer.allocate(1024);
               int readBytes = sc.read(bb);
               if(readBytes > 0){
                   bb.flip();
                   byte[] bytes = new byte[bb.remaining()];
                   bb.get(bytes);
                   String body = new String(bytes, "UTF-8");
                   System.out.println("Now is " + body);
//                   this.stop = true;
               }else if(readBytes < 0){
                   key.cancel();
                   sc.close();
               }else{
                   ;// 读到0字节，忽略
               }
           }
        }
    }
    
}
