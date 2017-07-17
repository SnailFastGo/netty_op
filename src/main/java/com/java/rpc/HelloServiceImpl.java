package com.java.rpc;

/**
 * @project netty_op
 * @file HelloServiceImpl.java 创建时间:2017年7月17日上午11:33:40
 * @description 接口实现类，远程RPC实际调用的方法
 * @author dzn
 * @version 1.0
 *
 */
public class HelloServiceImpl implements HelloService{

    @Override
    public String sayHi(String name) {
        return "HI! " + name;
    }
}
