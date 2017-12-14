package com.jetty.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.serialize.hessian.HessianSerializer;


/**
 * @project Jetty服务器连接客户端
 * @file JettyClient.java 创建时间:2017年12月12日下午8:38:05
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @author dzn
 * @version 1.0
 *
 */
public class JettyClient {
	private static Logger logger = LoggerFactory.getLogger(JettyClient.class);

	/**
	 *@description 通过httpclient发送post请求并返回响应结果
	 *@time 创建时间:2017年12月14日上午11:34:59
	 *@param request
	 *@return
	 *@throws Exception
	 *@author dzn
	 */
	public RpcResponse send(RpcRequest request) throws Exception {
		try {
			// 将请求数据序列化
			byte[] requestBytes = HessianSerializer.serialize(request);

			// 通过httpclient发送post请求
			byte[] responseBytes = HttpClientUtil.postRequest("http://" + request.getServerAddress() + "/", requestBytes);
			if (responseBytes == null || responseBytes.length==0) {
				RpcResponse rpcResponse = new RpcResponse();
				rpcResponse.setError("RpcResponse byte[] is null");
				return rpcResponse;
            }

            // 将响应数据反序列化
			RpcResponse rpcResponse = (RpcResponse) HessianSerializer.deserialize(responseBytes, RpcResponse.class);
			return rpcResponse;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			RpcResponse rpcResponse = new RpcResponse();
			rpcResponse.setError("Client-error:" + e.getMessage());
			return rpcResponse;
		}
	}

}
