package com.tool4us.net.example.echo;

import com.tool4us.net.common.ErrorCode;
import com.tool4us.net.common.ISession;
import com.tool4us.net.common.Protocol;
import com.tool4us.net.common.ProtocolExecutor;
import com.tool4us.net.handler.MessageDefine;
import com.tool4us.net.handler.MessageHandler;



/**
 * Echo 예제에서 서버에서 메시지 받은 경우를 처리하기 위한 핸들러.
 * 파라미터는 String으로 메시지 하나 받음.
 * 
 * @author TurboK
 */
@MessageDefine(id=MessageType.SEND_MSG_SERVER)
public class ReceiveMessage extends MessageHandler
{
    public ReceiveMessage(ProtocolExecutor executor)
    {
        super(executor);
    }

    @Override
    public ErrorCode setAndCheck(Protocol msg, ISession session) throws Exception
    {
        if( msg.sizeOfParam() < 1 )
        {
            return ErrorCode.errApiInsufficientParameter;
        }

        return ErrorCode.errSuccess;
    }
    
    @Override
    public Protocol work(Protocol msg, ISession session) throws Exception
    {
        String text = (String) msg.getParameter(0);
        
        System.out.println("[SERVER] " + text);
        
        return Protocol.newProtocol(MessageType.ECHO_MSG, text + " echoed.");
    }
    
    @Override
    public void clear()
    {
        // Nothing to do
    }
}
