package com.java.rpc;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @project netty_op
 * @file ServiceTask.java 创建时间:2017年7月17日上午11:27:49
 * @description 客户端请求的处理类
 * @author dzn
 * @version 1.0
 *
 */
public class ServiceTask implements Runnable{
    private Socket socket;
    
    public ServiceTask(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        ObjectInputStream input = null;
        ObjectOutputStream output = null;
        
        try{
            input = new ObjectInputStream(this.socket.getInputStream());
            output = new ObjectOutputStream(this.socket.getOutputStream());
            //读取服务类名
            String serviceName = input.readUTF();
            
            //读取服务方法名
            String methodName = input.readUTF();
            
            //读取方法参数类型
            Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
            
            //读取方法参数值
            Object[] arguments = (Object[])input.readObject();
            
            //通过类名获取到相应接口的实现类
            Class serviceClass = RPCUtils.serviceRegistry.get(serviceName);
            if (serviceClass == null) {
                throw new ClassNotFoundException(serviceName + " not found");
            }
            
            //通过方法名和参数类型获取方法
            Method method = serviceClass.getMethod(methodName, parameterTypes);
            
            //调用方法
            Object result = method.invoke(serviceClass.newInstance(), arguments);
            
            //发送方法调用的结果
            output.writeObject(result);
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            try{
                if(null != output){
                    output.close();
                }
                if(null != input){
                    input.close();
                }
                if(null != socket){
                    socket.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
}
