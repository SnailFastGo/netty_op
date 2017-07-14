package com.java.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class ServerHandler implements Runnable{
    
    private Socket socket;
    
    public ServerHandler(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            String currentTime = null;
            String body = null;
            while(true){
                body = in.readLine();
                if(null == body){
                    break;
                }
                System.out.println("Time Server receive order : " + body);
                if("Query Time Order".equalsIgnoreCase(body)){
                    currentTime = new Date().toString();
                }else{
                    currentTime = "Bad Order";
                }
                out.println(currentTime);
            }
            
        }catch(Exception e){
            try {
                if(null != in){
                    in.close();
                }
                if(null != out){
                    out.close();
                }
                if(null != socket){
                    socket.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
    
}
