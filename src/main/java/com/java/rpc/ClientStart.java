package com.java.rpc;

import java.net.InetSocketAddress;

public class ClientStart {
    public static void main(String[] args) {
        HelloService serviceProxy = RPCClient.getRemoteProxy(HelloService.class, new InetSocketAddress("localhost", 8088));
        String resp = serviceProxy.sayHi("tom");
        System.out.println(resp);
    }
}
