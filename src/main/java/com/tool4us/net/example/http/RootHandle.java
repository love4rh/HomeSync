package com.tool4us.net.example.http;

import com.tool4us.net.http.HTTPServiceHandle;
import com.tool4us.net.http.RequestDefine;
import com.tool4us.net.http.Requester;
import com.tool4us.net.http.Responser;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;



@RequestDefine(paths={"/"})
public class RootHandle implements HTTPServiceHandle
{
    static final String html;
    
    static
    {
        StringBuilder sb = new StringBuilder();
        
        sb.append("<!DOCTYPE html>\n")
          .append("<html>\n")
          .append("<head>\n")
          .append("<title>Welcome! Simple WAS Example.</title>\n")
          .append("</head>\n")
          .append("<body>\n")
          .append("<p>call /echo/hello</p>\n")
          .append("</body>\n")
          .append("</html>\n")
          ;
        
        
        html = sb.toString();
    }
    
    @Override
    public String call(Requester req, Responser res) throws Exception
    {
        res.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        
        res.setStatus(HttpResponseStatus.OK);        
        
        return html;
    }
    
}
