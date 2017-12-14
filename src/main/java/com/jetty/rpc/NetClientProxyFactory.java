package com.jetty.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/**
 * @project 客户端代理
 * @file NetComClientProxy.java 创建时间:2017年12月12日下午8:29:24
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @author dzn
 * @version 1.0
 *
 */
public class NetClientProxyFactory{
	private Class<?> iface;
	String serverAddress;
	JettyClient client = new JettyClient();
	public NetClientProxyFactory(Class<?> iface, String serverAddress) {
		this.iface = iface;
		this.serverAddress = serverAddress;
	}

	/**
	 *@description 根据接口对象和 服务端地址生成远程代理
	 *@time 创建时间:2017年12月14日上午11:33:47
	 *@return
	 *@throws Exception
	 *@author dzn
	 */
	public Object getProxy() throws Exception {
		return Proxy.newProxyInstance(Thread.currentThread()
				.getContextClassLoader(), new Class[] { iface },
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						
						// 组装request
						RpcRequest request = new RpcRequest();
	                    request.setServerAddress(serverAddress);
	                    request.setCreateMillisTime(System.currentTimeMillis());
	                    request.setClassName(method.getDeclaringClass().getName());
	                    request.setMethodName(method.getName());
	                    request.setParameterTypes(method.getParameterTypes());
	                    request.setParameters(args);
	                    
	                    // 发送request
	                    RpcResponse response = client.send(request);
	                    
	                    // valid response
						if (response == null) {
							throw new Exception("response not found.");
						}
	                    if (response.isError()) {
	                        throw new RuntimeException(response.getError());
	                    } else {
	                        return response.getResult();
	                    }
	                   
					}
				});
	}

}
