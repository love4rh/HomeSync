package com.tool4us.homesync.handler;

import static com.tool4us.net.common.NetSetting.NS;
import static com.tool4us.homesync.file.Repository.RT;

import java.util.List;

import com.tool4us.homesync.file.CompResult;
import com.tool4us.homesync.file.FileDictionary;
import com.tool4us.homesync.file.FileElement;
import com.tool4us.net.common.ErrorCode;
import com.tool4us.net.common.ISession;
import com.tool4us.net.common.Protocol;
import com.tool4us.net.common.ProtocolExecutor;
import com.tool4us.net.handler.MessageDefine;
import com.tool4us.net.handler.MessageHandler;



/**
 * 파일 요청.
 * @author TurboK
 */
@MessageDefine(id=TypeConstant.REQ_FILE)
public class RequestFileEvent extends MessageHandler
{
    public RequestFileEvent(ProtocolExecutor executor)
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
        String jsonStr = (String) msg.getParameter(0);
        FileDictionary fd = FileDictionary.fromJson(jsonStr);
        
        List<CompResult> diff = RT.getFileDictionary().diffList(fd);
        
        NS.info(session, "Compare", diff.size());
        
        Protocol rMsg = msg.createReply();
        
        rMsg.addParameter((byte) 0x01);
        rMsg.addParameter(diff.size());

        for(CompResult elem : diff)
        {
            int compResult = elem.getCompResult();
            FileElement fe = (FileElement) elem.getRelatedData();
            
            rMsg.addParameter(compResult);
            rMsg.addParameter(fe.getKey());
            rMsg.addParameter(fe.getFileSize());
            rMsg.addParameter(fe.getModifiedTime());
            rMsg.addParameter(fe.isDirectory() ? 1 : 0);
        }
        
        return rMsg;
    }
}
