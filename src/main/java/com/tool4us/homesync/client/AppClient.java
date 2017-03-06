package com.tool4us.homesync.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.tool4us.logging.Logs;



/**
 * 테스트 클라이언트
 * @author TurboK
 */
public class AppClient
{
    private static HomeSyncClient   _client = new HomeSyncClient();

    
    public static void main(String[] args)
    {
        System.out.println("HomeSync Client Test Console:");
        
        Logs.initDefault(null, "ClientSync");
        Logs.addConsoleLogger();
        
        try
        {
            _client.start();
            
            startConsole();
        }
        catch( Exception xe )
        {
            xe.printStackTrace();
        }        
    }
    
    private static void startConsole()
    {   
        String lineCmd = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        while( true )
        {
            System.out.print(">> ");
            
            // Getting command line.
            try
            {
                lineCmd = in.readLine();
            }
            catch(IOException e)
            {
                e.printStackTrace();
                break;
            }
            
            if( lineCmd == null )
                break;
            
            if( lineCmd.isEmpty() )
                continue;

            // 이전에 실행했던 명령 다시 실행
            if( "/".equals(lineCmd) )
            {
                //
            }
            else if( "find".equals(lineCmd) )
            {
                _client.findServer(6070);
            }
            else if( "synclist".equals(lineCmd) )
            {
                _client.compareList();
            }
            // 종료
            else if( "q".equals(lineCmd) || "quit".equals(lineCmd) )
            {
                _client.close();
                System.exit(0);
            }
        }
    }
    
}
