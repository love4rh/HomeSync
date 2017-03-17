package com.tool4us.net.example.http;

import static com.tool4us.util.CommonTool.CT;
import static com.tool4us.net.common.NetSetting.NS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.tool4us.net.http.HTTPServiceHandle;
import com.tool4us.net.http.RequestDefine;
import com.tool4us.net.http.Requester;
import com.tool4us.net.http.Responser;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;



@RequestDefine(paths={"/study/download"})
public class DownloadHandle implements HTTPServiceHandle
{
    @Override
    public String call(Requester req, Responser res) throws Exception
    {
        res.headers().set(HttpHeaderNames.CONTENT_TYPE, "Application/json; charset=UTF-8");
        
        res.setStatus(HttpResponseStatus.OK);

        String returnJson = "";
        BufferedReader in = null;
        
        try
        {
            StringBuilder sb = new StringBuilder();
            
            String code = req.parameter("code");
            String dataFilePath = CT.getAppPath("data/");
            
            if( "cm001".equals(code) )
            {
                dataFilePath += "dialog100.json";
            }
            else
            {
                throw new Exception("Invalid Code");
            }
            
            in = new BufferedReader( new InputStreamReader(
                    new FileInputStream(new File(dataFilePath)), "UTF-8") );
            
            String lineText = in.readLine();
            while( lineText != null && !lineText.isEmpty() )
            {
                sb.append(lineText).append("\n");

                lineText = in.readLine();
            }
            
            returnJson = sb.toString();
        }
        catch(Exception xe)
        {
            NS.trace(null, xe);
            returnJson = "{ \"error\": \"" + xe.getMessage() + "\" }";
        }
        finally
        {
            if( in != null )
                in.close();
        } 
        
        return returnJson;
    }
    
}
