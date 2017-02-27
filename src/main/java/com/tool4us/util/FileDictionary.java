package com.tool4us.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;



/**
 * 지정된 폴더 내 하위 폴더 및 파일을 일괄 관리하기 위한 클래스
 * 
 * @author TurboK
 */
public class FileDictionary
{
    private String      _rootPath = null;
    private File        _rootFile = null;
    
    private Map<String, FileElement>    _fileMap = null;
    
    
    public FileDictionary(String rootPath)
    {
        _rootFile = new File(rootPath);
        _rootPath = _rootFile.getAbsolutePath();
        
        _fileMap = new TreeMap<String, FileElement>();
    }
    
    public void reload()
    {
        if( _rootPath == null )
            return;
        
        synchronized(_fileMap)
        {
            _fileMap.clear();
        }
        
        System.out.println("ROOT: " + _rootPath);
        
        navigate(_rootFile);
    }
    
    private void addEntry(File file)
    {
        synchronized(_fileMap)
        {
            FileElement elem = new FileElement(_rootPath, file);
            
            _fileMap.put(elem.getKey(), elem);
            
            elem.debugOut();
        }
    }
    
    private void navigate(File rootFile)
    {
        File[] subList = rootFile.listFiles();
        
        try
        {
            for(int i = 0; i < subList.length; ++i)
            {
                File f = subList[i];
                
                // 추가
                addEntry(f);

                if( f.isDirectory() )
                {
                    navigate(f);
                }
            }
        }
        catch(Exception xe)
        {
            xe.printStackTrace();
        }
    }
    
    /**
     * 두 파일 목록을 비교하여 변경된 것을 반환하는 메소드.
     * 내 것을 기준으로 하여 나한테만 있는 것, 없는 것, 변경된 것을 구분하여 반환.
     * @param that
     * @return
     */
    public List<FileElement> diffList(FileDictionary that)
    {
        List<FileElement> dList = new ArrayList<FileElement>();
        
        List<String> k1 = new ArrayList<String>();
        List<String> k2 = new ArrayList<String>();
        
        k1.addAll( this._fileMap.keySet() );
        k2.addAll( that._fileMap.keySet() );
        
        int i = 0, j = 0, comp = 0;
        
        while( i < k1.size() )
        {
            comp = k1.get(i).compareTo(k2.get(j));
            
            // 양쪽에 다 있음
            if( comp == 0 )
            {
                i += 1;
                j += 1;
            }
            // 내가 더 큰 경우. 나 한테는 없는 것이 있는 경우임.
            else if( comp > 0 )
            {
                // 이 위치를 기록하고 같은 것이 나올 때 까지 루핑
                j += 1;
            }
            // 내가 더 작은 경우. 나 한테만 있는 것이 있는 경우임.
            else if( comp < 0 )
            {
                // 새로 추가된 것으로 간주하면 됨.
                i += 1;
            }
        }
        
        return dList;
    }
}
