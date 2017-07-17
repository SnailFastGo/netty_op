package com.java.rpc;

import java.io.IOException;

/**
 * @project netty_op
 * @file ServerStart.java 创建时间:2017年7月17日上午11:29:53
 * @description RPC服务启动类
 * @author dzn
 * @version 1.0
 *
 */
public class ServerStart {
    public static void main(String[] args) throws IOException {
        RPCServer serviceServer = new RPCServerImpl(8088);
        serviceServer.register(HelloService.class, HelloServiceImpl.class);
        serviceServer.start();
    }
}
