package com.java.io.pool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TimeServer {
    private static int port = 8080;
    public static void main(String[] args) {
        ServerSocket server = null;
        TimeServerHandlerExecutePool singleExecutor = null;
        try{
            server = new ServerSocket(port);
            singleExecutor = new TimeServerHandlerExecutePool(50, 10000);
            System.out.println("Time Server is started in port" + " : " + port);
            Socket socket = null;
            while(true){
                socket = server.accept();
                singleExecutor.execute(new TimeServerHandler(socket));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(null != server){
                try {
                    server.close();
                    System.out.println("Time Server close");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }
}
