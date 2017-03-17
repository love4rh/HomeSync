package com.tool4us.net.example.http;

import static com.tool4us.net.common.NetSetting.NS;

import com.tool4us.logging.Logs;
import com.tool4us.net.http.SimpleHttpServer;



public class SimpleWASExample
{
    public static void main(String[] args)
    {
        Logs.initDefault(null, "studyM");

        int port = 3100;
        SimpleHttpServer server = new SimpleHttpServer("com.tool4us.net.example.http");
        
        try
        {
            server.start(port, 1, 4);
            
            System.out.println("http://" + NS.localAddress() + ":" + port);
            
            server.sync();
        }
        catch(Exception xe)
        {
            xe.printStackTrace();
        }
    }
}
