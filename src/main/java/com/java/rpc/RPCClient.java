package com.java.rpc;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @project netty_op
 * @file RPCClient.java 创建时间:2017年7月17日上午11:34:53
 * @description 生成服务端接口的代理对象
 * @author dzn
 * @param <T>
 * @version 1.0
 *
 */
public class RPCClient<T> {
    @SuppressWarnings("unchecked")
    public static <T> T getRemoteProxy(final Class<?> serviceInterface, final InetSocketAddress addr){
        return (T)Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class<?>[]{serviceInterface}, new InvocationHandler(){

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Socket socket = null;
                ObjectOutputStream output = null;
                ObjectInputStream input = null;
                try{
                    socket = new Socket();
                    socket.connect(addr);
                    output = new ObjectOutputStream(socket.getOutputStream());
                    input = new ObjectInputStream(socket.getInputStream());
                    //将服务接口、方法名、参数类型、参数值传到服务端，由服务端的实现类来调用实际的方法，并将方法的返回值传回客户端
                    output.writeUTF(serviceInterface.getName());
                    output.writeUTF(method.getName());
                    output.writeObject(method.getParameterTypes());
                    output.writeObject(args);
                    Object resp = input.readObject();
                    return resp;
                }finally{
                    if(null != output){
                        output.close();
                    }
                    if(null != input){
                        input.close();
                    }
                    if(null != socket){
                        socket.close();
                    }
                }
            }
            
        });
    }
}
