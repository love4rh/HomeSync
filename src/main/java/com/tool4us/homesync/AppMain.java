package com.tool4us.homesync;

import static com.tool4us.homesync.file.Repository.RT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.tool4us.homesync.file.FileDictionary;
import com.tool4us.logging.Logs;



/**
 * Application Main Class
 * @author TurboK
 */
public class AppMain
{
    protected final static AppHomeSync   _appMain = new AppHomeSync();
	
	public static void main(String[] args)
    {
	    try
	    {
	        Logs.initDefault(null, "HomeSync");
	        Logs.addConsoleLogger();

	        RT.setUpRoot( _appMain.appSetting().getValue("syncfolder", "C:\\temp\\homesync"), true );

	        _appMain.startServer();
	        
	        startConsole();
	    }
	    catch(Exception xe)
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

            if( "t1".equals(lineCmd) )
            {
                System.out.println( RT.getFileDictionary().toJson() );
            }
            else if( "t2".equals(lineCmd) )
            {
                String jsonStr = "{\"version\":1, \"rootPath\":\"C:/temp/homesync\",\"fileMap\":[{\"pathName\":\"/bbb.txt\",\"size\":3,\"time\":1488158126076},{\"pathName\":\"/c1\",\"size\":4096,\"time\":1488158332776},{\"pathName\":\"/c1/c2\",\"size\":0,\"time\":1488158183287},{\"pathName\":\"/c1/icons\",\"size\":0,\"time\":1488158362727},{\"pathName\":\"/c1/icons/128.png\",\"size\":3902,\"time\":1346881299874},{\"pathName\":\"/c1/icons/16-2.png\",\"size\":527,\"time\":1346881299874},{\"pathName\":\"/c1/icons/16.png\",\"size\":527,\"time\":1346881299874},{\"pathName\":\"/c1/icons/32.png\",\"size\":1062,\"time\":1346881299875},{\"pathName\":\"/c1/icons/64.png\",\"size\":2240,\"time\":1346881299875},{\"pathName\":\"/c1/index.html\",\"size\":184,\"time\":1488158346254},{\"pathName\":\"/c1/rainbowsky.jpg\",\"size\":119272,\"time\":1346881299876},{\"pathName\":\"/c1/style.css\",\"size\":38701,\"time\":1346881299337}]}";
                
                FileDictionary.fromJson(jsonStr).debugOut();
            }
            // 종료
            else if( "q".equals(lineCmd) || "quit".equals(lineCmd) )
            {   
                _appMain.stopServer();
            }
        }
    }
}

