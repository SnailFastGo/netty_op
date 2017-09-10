package com.java.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RMIServiceClient {
	public static void main(String[] args) {
        try { 
//            在RMI服务注册表中查找名称为service的对象，并调用其上的方法 
        	RMIServiceInterface service = (RMIServiceInterface) Naming.lookup("rmi://localhost:8888/service"); 
            System.out.println(service.helloWorld()); 
            System.out.println(service.sayHelloToSomeBody("熔岩")); 
        } catch (NotBoundException e) { 
            e.printStackTrace(); 
        } catch (MalformedURLException e) { 
            e.printStackTrace(); 
        } catch (RemoteException e) { 
            e.printStackTrace();   
        } 
    } 
}
