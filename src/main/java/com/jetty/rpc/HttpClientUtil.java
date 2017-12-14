package com.jetty.rpc;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpClientUtil {
	private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    	/**
	 *@description 使用HttpClient发送post请求
	 *@time 创建时间:2017年12月14日上午11:27:11
	 *@param reqURL
	 *@param data
	 *@return
	 *@throws Exception
	 *@author dzn
	 */
	public static byte[] postRequest(String reqURL, byte[] data) throws Exception {
		byte[] responseBytes = null;
		
		HttpPost httpPost = new HttpPost(reqURL);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			// timeout
			RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(10000)
                    .setSocketTimeout(10000)
                    .setConnectTimeout(10000)
                    .build();

			httpPost.setConfig(requestConfig);

			// 设置post请求的有效载荷
			if (data != null) {
				httpPost.setEntity(new ByteArrayEntity(data, ContentType.DEFAULT_BINARY));
			}
			// 发送 post请求
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				responseBytes = EntityUtils.toByteArray(entity);
				EntityUtils.consume(entity);
			}
		} catch (Exception e) {
			logger.error("", e);
			throw e;
		} finally {
			httpPost.releaseConnection();
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseBytes;
	}
}
