package com.tool4us.homesync.client;

import static com.tool4us.homesync.file.Repository.RT;

import com.tool4us.homesync.file.FileElement;
import com.tool4us.homesync.handler.TypeConstant;
import com.tool4us.net.client.TCPClient;
import com.tool4us.net.common.Protocol;
import com.tool4us.net.handler.CommonExecutor;

import io.netty.channel.ConnectTimeoutException;



/**
 * 
 * @author TurboK
 */
public class HomeSyncClient extends TCPClient
{
    /**
     * 최근에 접속했던 서버 IP
     */
    private String      _lastServer = null;
    private String      _foundServer = null;
    
    private String      _rootPath = null;
    
    
    public HomeSyncClient()
    {
        super( CommonExecutor.newInstance("com.tool4us.homesync.handler") );
        
        // TODO 이전 수행 정보 읽어 오기. _lastServer 등 설정
        
        _lastServer = "192.168.0.24";
        _rootPath = "C:\\temp\\clientsync";
    }
    
    public void start() throws Exception
    {
        RT.setUpRoot(_rootPath);
    }
    
    public void close()
    {
        this.disconnect();
        
        // _foundServer를 last server로 저장
    }
    
    public boolean findServer(int port)
    {
        String localIp = this.localAddress();
        Protocol helloMsg = Protocol.newProtocol(TypeConstant.HELLO, localIp);
        
        localIp = localIp.substring(0, localIp.lastIndexOf('.') + 1);
        
        boolean found = false;
        for(int i = 1; !found && i < 255; ++i)
        {
            String checkIp = localIp + i;
            
            if( i == 1 )
            {
                if( _lastServer == null )
                    continue;

                checkIp = _lastServer;
            }

            try
            {
                if( this.connect(checkIp, port, 500) )
                {
                    Protocol rMsg = this.sendSync(helloMsg, 1000);
                    if( rMsg.type() == TypeConstant.HELLO + 1 )
                    {
                        found = (0x01 == (Byte) rMsg.getParameter(0));
                    }
                    
                    if( !found )
                        this.disconnect();
                }
            }
            catch(ConnectTimeoutException xe)
            {
                // to do nothing
            }
            catch(Exception xe)
            {
                xe.printStackTrace();
            }
            
            if( !found )
            {
                System.out.println("check " + checkIp + ", but it's not.");
            }
            else
            {
                System.out.println("check " + checkIp + ", that's it.");
                
                _foundServer = checkIp;
            }
        }

        return true;
    }
    
    public void compareList()
    {
        if( !isConnected() )
            return;
        
        try
        {
            Protocol msg = Protocol.newProtocol(TypeConstant.COMPARE);
            msg.addParameter( RT.getFileDictionary().toJson() );
            
            Protocol rMsg = this.sendSync(msg, 5000);
            
            boolean isOk = 0x01 == (Byte) rMsg.getParameter(0);
            
            if( isOk )
            {
                final int vCount = 5;
                int count = (Integer) rMsg.getParameter(1);
                
                for(int i = 0; i < count; ++i)
                {
                    int compResult = (Integer) rMsg.getParameter(i * vCount + 2);
                    String uniquePathName = (String) rMsg.getParameter(i * vCount + 3);
                    long fileSize = (Long) rMsg.getParameter(i * vCount + 4);
                    long mTime = (Long) rMsg.getParameter(i * vCount + 5);
                    int dir = (Integer) rMsg.getParameter(i * vCount + 6);
                    
                    FileElement fe = new FileElement(uniquePathName, fileSize, mTime, dir == 1);
                    
                    System.out.print("Comp: " + compResult + " --> ");
                    fe.debugOut();
                }
            }
            else
            {
                System.out.println("Error occurred: " + rMsg.getParameter(1) + " | " + rMsg.getParameter(2));
            }
        }
        catch(Exception xe)
        {
            xe.printStackTrace();
        }
    }
    
    public void sendMessage(String msg)
    {
        /*
        try
        {
            this.send( Protocol.newProtocol(MessageType.SEND_MSG_SERVER, msg) );
        }
        catch(Exception xe)
        {
            xe.printStackTrace();
        } // */
    }
}

