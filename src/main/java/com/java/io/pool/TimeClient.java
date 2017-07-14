package com.java.io.pool;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TimeClient {
    private static int port = 8080;
    
    private static String host = "127.0.0.1";
    
    public static void main(String[] args) {
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try{
            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Query Time Order");
            System.out.println("send order 2 server succeed");
            String resp = in.readLine();
            System.out.println("Now is " + resp);
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(null != in){
                    in.close();
                }
                if(null != out){
                    out.close();
                }
                if(null != socket){
                    socket.close();
                }
            }catch(Exception e1){
                e1.printStackTrace();
            }
        }
        
    }
}
