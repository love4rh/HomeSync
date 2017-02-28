package com.tool4us.homesync.handler;

import com.tool4us.net.common.ErrorCode;
import com.tool4us.net.common.ISession;
import com.tool4us.net.common.Protocol;
import com.tool4us.net.common.ProtocolExecutor;
import com.tool4us.net.handler.MessageDefine;
import com.tool4us.net.handler.MessageHandler;



/**
 * Echo된 메시지 처리 핸들러
 * 파라미터는 String으로 메시지 하나 받음.
 * 
 * @author TurboK
 */
@MessageDefine(id=TypeConstant.HELLO)
public class HelloEvent extends MessageHandler
{
    public HelloEvent(ProtocolExecutor executor)
    {
        super(executor);
    }
    
    @Override
    public void clear()
    {
        // Nothing to do
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
        
        System.out.println("[CLIENT] " + text);
        
        return null;
    }
}
