package com.jetty.rpc;

public class RPCDemo {
    public static void main(String[] args) throws Exception {
        NetClientProxyFactory factory = new NetClientProxyFactory(ExecutorBiz.class, "127.0.0.1:9000");
        ExecutorBiz proxy = (ExecutorBiz) factory.getProxy();
        String runResult = proxy.run(null);
        System.out.println(runResult);
    }
}
