package com.java.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author snailfast
 * 远程的接口的实现 
 */
public class RMIServiceImpl extends UnicastRemoteObject implements RMIServiceInterface{

    /** 
     * 因为UnicastRemoteObject的构造方法抛出了RemoteException异常，因此这里默认的构造方法必须写，必须声明抛出RemoteException异常 
     * 
     * @throws RemoteException 
     */ 
    public RMIServiceImpl() throws RemoteException { 
    } 

    
	public String helloWorld() throws RemoteException {
		return "Hello World!"; 
	}

	public String sayHelloToSomeBody(String someBodyName) throws RemoteException {
		return "你好，" + someBodyName + "!"; 
	}
	
}
