package com.java.rpc;

/**
 * @project netty_op
 * @file HelloService.java 创建时间:2017年7月17日上午11:32:52
 * @description 接口，定义RPC可调用的方法
 * @author dzn
 * @version 1.0
 *
 */
public interface HelloService {
    String sayHi(String name);
}
