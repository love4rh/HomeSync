package com.tool4us.util;

import static com.tool4us.util.CommonTool.CT;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;



/**
 * Application을 실행하기 위하여 필요한 설정 값을 관리하기 위한 클래스.
 * 
 * @author TurboK
 */
public class AppSetting
{
    private final String            _charEncoding = "UTF-8";
    
    private String                  _configFile;
    private Map<String, String>     _option;
    
    
    public AppSetting(String configFile)
    {
        _configFile = configFile;
        _option = new TreeMap<String, String>();
    }
    
    public void save() throws Exception
    {
        BufferedWriter out = null;
        
        try
        {
            out = new BufferedWriter( new OutputStreamWriter(
                    new FileOutputStream(new File(_configFile)), _charEncoding) );
            
            for(Entry<String, String> elem : _option.entrySet())
            {
                out.write(elem.getKey());
                out.write("=");
                out.write(elem.getValue());
                out.write("\n");
            }
        }
        catch(Exception xe)
        {
            xe.printStackTrace();
        }
        finally
        {
            if( out != null )
                out.close();
        }
    }
    
    public void load() throws Exception
    {
        _option.clear();
        
        BufferedReader in = null;
        
        try
        {
            in = new BufferedReader( new InputStreamReader(
                    new FileInputStream(new File(_configFile)), _charEncoding) );
            
            String lineText = in.readLine();
            while( lineText != null && !lineText.isEmpty() )
            {
                int sPos = lineText.indexOf('=');
                
                if( sPos != -1 )
                {
                    _option.put(lineText.substring(0,  sPos), lineText.substring(sPos + 1));
                }
                
                lineText = in.readLine();
            }
        }
        catch(Exception xe)
        {
            xe.printStackTrace();
        }
        finally
        {
            if( in != null )
                in.close();
        }
    }
    
    public String setValue(String name, String value)
    {
        return _option.put(name, CT.makeNoWrapped(value));
    }
    
    public String getValue(String name)
    {
        return CT.makeWrapped(_option.get(name));
    }
    
    public String getValue(String name, String defaultValue)
    {
        String value = _option.get(name);
        
        if( value == null || value.isEmpty() )
        {
            setValue(name, defaultValue);
            value = defaultValue;
        }
        else
            value = CT.makeWrapped(value);
        
        return value;
    }
    
    public String removeValue(String name)
    {
        return _option.remove(name);
    }
}
