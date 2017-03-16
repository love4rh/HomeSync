package com.tool4us.net.example.http;

import com.tool4us.net.http.SimpleHttpServer;



public class SimpleWASExample
{
    public static void main(String[] args)
    {
        SimpleHttpServer server = new SimpleHttpServer("com.tool4us.net.example.http");
        
        try
        {
            server.start(8383, 1, 4);
            
            System.out.println("http://127.0.0.1:8383");
            
            server.sync();
        }
        catch(Exception xe)
        {
            xe.printStackTrace();
        }
    }
}
