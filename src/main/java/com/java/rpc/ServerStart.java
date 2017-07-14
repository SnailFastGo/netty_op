package com.java.rpc;

import java.io.IOException;

public class ServerStart {
    public static void main(String[] args) throws IOException {
        RPCServer serviceServer = new RPCServerImpl(8088);
        serviceServer.register(HelloService.class, HelloServiceImpl.class);
        serviceServer.start();
    }
}
