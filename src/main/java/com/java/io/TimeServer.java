package com.java.io;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TimeServer {
    private static int port = 8080;
    public static void main(String[] args) {
        ServerSocket server = null;
        try{
            server = new ServerSocket(port);
            System.out.println("Time Server is started in port" + " : " + port);
            Socket socket = null;
            while(true){
                socket = server.accept();
                new Thread(new ServerHandler(socket)).start();
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
