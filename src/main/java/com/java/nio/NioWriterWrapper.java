package com.java.nio;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioWriterWrapper {
    private SocketChannel socketChannel;
    private static NioWriterWrapper INSTANCE = new NioWriterWrapper();
    private NioWriterWrapper(){
        
    }
    
    public static NioWriterWrapper getInstance(){
        return INSTANCE;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }
    
    public void writeMsg(String data) throws Exception{
        if(null == data){
            return;
        }
        byte[] bytes = data.getBytes("UTF-8");
        int length = bytes.length;
        ByteBuffer bb = ByteBuffer.allocate(length);
        bb.put(bytes);
        bb.flip();
        this.socketChannel.write(bb);
    }
    
}
