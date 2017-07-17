package com.java.rpc;

import java.net.InetSocketAddress;

/**
 * @project netty_op
 * @file ClientStart.java 创建时间:2017年7月17日上午11:34:21
 * @description 客户端进行RPC调用的使用类
 * @author dzn
 * @version 1.0
 *
 */
public class ClientStart {
    public static void main(String[] args) {
        HelloService serviceProxy = RPCClient.getRemoteProxy(HelloService.class, new InetSocketAddress("localhost", 8088));
        String resp = serviceProxy.sayHi("tom");
        System.out.println(resp);
    }
}
