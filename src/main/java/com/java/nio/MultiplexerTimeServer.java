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
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);
            System.out.println("Time Server is started in port : " + port);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop() {
        stop = true;
    }

    @Override
    public void run() {
        while (!stop) {
            SelectionKey selectionKey = null;
            try {
                selector.select(1000);
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();
                while (iterator.hasNext()) {
                    selectionKey = iterator.next();
                    iterator.remove();
                    handleInput(selectionKey);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                try {
                    if (null != selectionKey) {
                        selectionKey.cancel();
                        if (null != selectionKey.channel()) {
                            selectionKey.channel().close();
                        }
                    }
                    
                    if(null != selector){
                        selector.close();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private void handleInput(SelectionKey key) throws Exception {
        if(key.isValid()){
            if(key.isAcceptable()){
                //遇到接收事件
                ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                //将接收到的Socket注册到selector
                sc.register(selector, SelectionKey.OP_READ);
            }else if(key.isReadable()){
                SocketChannel sc = (SocketChannel)key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if(readBytes > 0){
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
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
                System.out.println("暂时无内容需要发送");
            }
        }
    }
    
    public static void doWrite(SocketChannel sc, String response) throws IOException{
        if(null != response && response.trim().length() > 0){
            byte[] bytes = response.getBytes();
            ByteBuffer bb = ByteBuffer.allocate(bytes.length);
            bb.put(bytes);
            bb.flip();
            sc.write(bb);
        }
    }

}
