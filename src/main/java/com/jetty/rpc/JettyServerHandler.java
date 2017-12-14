package com.jetty.rpc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;

import com.serialize.hessian.HessianSerializer;


/**
 * @project JettyServer的处理类,用于接收客户端的RPC请求
 * @file JettyServerHandler.java 创建时间:2017年7月29日下午12:06:04
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @author dzn
 * @version 1.0
 *
 */
public class JettyServerHandler extends AbstractHandler {
	private static Logger logger = LoggerFactory.getLogger(JettyServerHandler.class);
	
	/**
	 * @description 业务接口名称和实现类映射
	 * @value value:serviceMap
	 */
	public static Map<String, Object> serviceMap = new HashMap<>();
	
	static{
	    serviceMap.put(ExecutorBiz.class.getName(), new ExecutorBizImpl());
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// RPC调用
        RpcResponse rpcResponse = doInvoke(request);

        // 序列化响应结果
        byte[] responseBytes = HessianSerializer.serialize(rpcResponse);
		
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		baseRequest.setHandled(true);
		
		OutputStream out = response.getOutputStream();
		out.write(responseBytes);
		out.flush();
		
	}

	/**
	 *@description 根据客户端的请求进行RPC调用
	 *@time 创建时间:2017年7月29日下午12:04:54
	 *@param request
	 *@return
	 *@author dzn
	 */
	private RpcResponse doInvoke(HttpServletRequest request) {
		try {
			// 从post请求中读取有效载荷
			byte[] requestBytes = readRequestPayLoadBytes(request);
			
			//请求参数为空则直接返回
			if (requestBytes == null || requestBytes.length==0) {
				RpcResponse rpcResponse = new RpcResponse();
				rpcResponse.setError("RpcRequest byte[] is null");
				return rpcResponse;
			}
			
			// 反序列化请求对象
			RpcRequest rpcRequest = (RpcRequest) HessianSerializer.deserialize(requestBytes, RpcRequest.class);

			// 调用请求对象中指定的本地方法
			RpcResponse rpcResponse = invokeService(rpcRequest);
			return rpcResponse;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			RpcResponse rpcResponse = new RpcResponse();
			rpcResponse.setError("Server-error:" + e.getMessage());
			return rpcResponse;
		}
	}
	
	   /**
     *@description 远程RPC调用
     *@time 创建时间:2017年7月28日下午5:03:27
     *@param request
     *@param serviceBean
     *@return
     *@author dzn
     */
    public static RpcResponse invokeService(RpcRequest request) {
        RpcResponse response = new RpcResponse();
        //动态调用某个对象的指定方法
        try {
            //获取请求调用的接口实例
            String serviceInterfaceName = request.getClassName();
            Object serviceInstance = serviceMap.get(serviceInterfaceName);
            
            //获取接口实例的FastClass对象
            Class<?> serviceInstanceClass = serviceInstance.getClass();
            FastClass serviceFastClass = FastClass.create(serviceInstanceClass);
            
            //获取远程调用的方法
            String methodName = request.getMethodName();
            Class<?>[] parameterTypes = request.getParameterTypes();
            FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
            
            //调用指定的方法
            Object[] parameters = request.getParameters();
            Object result = serviceFastMethod.invoke(serviceInstance, parameters);

            response.setResult(result);
        } catch (Throwable t) {
            t.printStackTrace();
            response.setError(t.getMessage());
        }
        return response;
    }
    
    /**
     *@description 读取Post请求中的有效载荷对象
     *@time 创建时间:2017年12月14日上午11:31:40
     *@param request
     *@return
     *@throws IOException
     *@author dzn
     */
    public static final byte[] readRequestPayLoadBytes(HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        int contentLen = request.getContentLength();
        InputStream is = request.getInputStream();
        if (contentLen > 0) {
            int readLen = 0;
            int readLengthThisTime = 0;
            byte[] message = new byte[contentLen];
            try {
                while (readLen != contentLen) {
                    readLengthThisTime = is.read(message, readLen, contentLen - readLen);
                    if (readLengthThisTime == -1) {
                        break;
                    }
                    readLen += readLengthThisTime;
                }
                return message;
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
        return new byte[] {};
    }
}
