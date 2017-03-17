package com.tool4us.net.example.http;

import com.tool4us.net.http.HTTPServiceHandle;
import com.tool4us.net.http.RequestDefine;
import com.tool4us.net.http.Requester;
import com.tool4us.net.http.Responser;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;



@RequestDefine(paths={"/study/hello"})
public class StudyTestHandle implements HTTPServiceHandle
{
    @Override
    public String call(Requester req, Responser res) throws Exception
    {
        res.headers().set(HttpHeaderNames.CONTENT_TYPE, "Application/json; charset=UTF-8");
        
        res.setStatus(HttpResponseStatus.OK);
        
        return "{ \"message\": \"Hello World!\" }";
    }
    
}
