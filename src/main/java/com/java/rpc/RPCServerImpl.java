package com.java.rpc;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

/**
 * @project netty_op
 * @file RPCServerImpl.java 创建时间:2017年7月17日上午11:31:04
 * @description 客户端请求的连接类
 * @author dzn
 * @version 1.0
 *
 */
public class RPCServerImpl implements RPCServer{
    
    private boolean isRunning = false;
    
    private static int port;
    
    public RPCServerImpl(int port) {
        this.port = port;
    }
    
    /**
     *@description 停止SocketServer服务器
     *@time 创建时间:2017年7月14日下午5:42:21
     *@author dzn
     */
    @Override
    public void stop() {
        isRunning = false;
        RPCUtils.executor.shutdown();
    }

    /**
     *@description 启动SocketServer服务器
     *@time 创建时间:2017年7月14日下午5:41:53
     *@throws IOException
     *@author dzn
     */
    @Override
    public void start() throws IOException {
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress(this.port));
        this.isRunning = true;
        System.out.println("start server");
        try{
            while(isRunning){
                // 1.监听客户端的TCP连接，接到TCP连接后将其封装成task，由线程池执行
                RPCUtils.executor.execute(new ServiceTask(server.accept()));
            }
        }finally{
            server.close();
        }
    }

    /**
     *@description 通过接口的名称来关联实现类
     *@time 创建时间:2017年7月14日下午5:40:57
     *@param serviceInterface 接口
     *@param serviceImpl 接口的实现类
     *@author dzn
     */
    @Override
    public void register(Class serviceInterface, Class serviceImpl) {
        RPCUtils.serviceRegistry.put(serviceInterface.getName(), serviceImpl);
    }

    /**
     *@description 查看服务器是否在运行
     *@time 创建时间:2017年7月14日下午5:42:44
     *@return
     *@author dzn
     */
    @Override
    public boolean isRunning() {
        return isRunning;
    }

    /**
     *@description 获取服务器的端口
     *@time 创建时间:2017年7月14日下午5:43:04
     *@return
     *@author dzn
     */
    @Override
    public int getPort() {
        return this.port;
    }
}
