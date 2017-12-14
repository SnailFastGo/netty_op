package com.jetty.rpc;

/**
 * @project Job执行器
 * @file JobExecutor.java 创建时间:2017年7月28日下午5:17:01
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @author dzn
 * @version 1.0
 *
 */
public class JettyServerStarter{
    
    //创建一个Jetty的Server实例
    JettyServer server = new JettyServer();
    
    public void start() throws Exception {
        //启动Jetty服务器作为RPC调用服务器
        server.start(9000);
    }
    
    public void destroy(){
        server.destroy();
    }
    
    public static void main(String[] args) throws Exception {
        JettyServerStarter jobExecutor = new JettyServerStarter();
        jobExecutor.start();
    }

}
