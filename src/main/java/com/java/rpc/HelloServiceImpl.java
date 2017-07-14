package com.java.rpc;

public class HelloServiceImpl implements HelloService{

    @Override
    public String sayHi(String name) {
        return "HI! " + name;
    }
}
