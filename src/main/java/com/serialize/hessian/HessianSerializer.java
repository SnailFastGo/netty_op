package com.serialize.hessian;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @project 基于Hessian的序列化和反序列化
 * @file HessianSerializer.java 创建时间:2017年12月12日下午12:23:34
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @author dzn
 * @version 1.0
 *
 */
public class HessianSerializer  {

	public static <T> byte[] serialize(T obj){
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		HessianOutput ho = new HessianOutput(os);
		try {
			ho.writeObject(obj);
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
		return os.toByteArray();
	}

	public static <T> Object deserialize(byte[] bytes, Class<T> clazz) {
		ByteArrayInputStream is = new ByteArrayInputStream(bytes);
		HessianInput hi = new HessianInput(is);
		try {
			return hi.readObject();
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}
	
}
