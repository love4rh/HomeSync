package com.tool4us.net.example.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.tool4us.net.client.TCPClient;
import com.tool4us.net.common.Protocol;
import com.tool4us.net.handler.CommonExecutor;
import com.tool4us.net.server.ClientSession;
import com.tool4us.net.server.CommonSession;
import com.tool4us.net.server.TCPServer;



/**
 * TCP/IP 기반 TCPServer 예제.
 * CommonExecutor를 이용한 버전임
 * 
 * @author TurboK
 */
class EchoServer extends TCPServer
{
    public EchoServer()
    {
        super();
    }
    
    @Override
    public ClientSession newClientSession()
    {
        return new CommonSession("com.tool4us.net.example.echo");
    }
}



/**
 * TCPClient Example
 * 
 * @author TurboK
 */
class EchoClient extends TCPClient
{
    public EchoClient()
    {
        super( CommonExecutor.newInstance("com.tool4us.net.example.echo") );
    }
    
    public void sendMessage(String msg)
    {
        try
        {
            this.send( Protocol.newProtocol(MessageType.SEND_MSG_SERVER, msg) );
        }
        catch(Exception xe)
        {
            xe.printStackTrace();
        }
    }
}



/**
 * Echo Server Example
 * 
 * @author TurboK
 */
public class EchoServerExample
{
    static final int    testPort = 7070;
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {        
        // 서버 생성
        EchoServer echoServer = new EchoServer();
        
        // 클라이언트 생성
        EchoClient echoClient = new EchoClient();
        
        try
        {
            // 서버 시작
            echoServer.start(testPort, 0, 0);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return;
        }
        
        // Console Mode
        String lineCmd = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        while( true )
        {
            System.out.print(">> ");
            
            try { lineCmd = in.readLine(); }
            catch (IOException e) { e.printStackTrace(); break; }
            
            if( "q".equals(lineCmd) )
                break;
            else
            {
                String[] cmd = lineCmd.split(" ");
                
                if( "c".equals(cmd[0]) )
                {
                    String server = cmd.length < 2 ? "localhost" : cmd[1];
                    
                    try
                    {
                        if( echoClient.connect(server, testPort) )
                        {
                            System.out.println("connected to " + server);
                        }
                    }
                    catch( Exception e )
                    {
                        System.out.println("can not connect to " + server + ": " + e.getMessage());
                    }
                }
                else if( "s".equals(cmd[0]) )
                {
                    if( echoClient.isConnected() )
                    {
                        echoClient.sendMessage(lineCmd.substring(2));
                    }
                }
            }
        }
        
        echoClient.disconnect();
        echoServer.shutdown();
    }
}
