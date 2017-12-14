package com.jetty.rpc;

import java.io.Serializable;


/**
 * @project RPC响应类
 * @file RpcResponse.java 创建时间:2017年12月12日下午8:35:42
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @author dzn
 * @version 1.0
 *
 */
public class RpcResponse implements Serializable{
	private static final long serialVersionUID = 1L;
	
    private String error;
    private Object result;

    public boolean isError() {
        return error != null;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

	@Override
	public String toString() {
		return "RPCResponse [error=" + error
				+ ", result=" + result + "]";
	}

}
