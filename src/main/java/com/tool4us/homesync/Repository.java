package com.tool4us.homesync;

import static java.nio.file.StandardWatchEventKinds.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent.Kind;

import com.tool4us.homesync.file.FileDictionary;



/**
 * 서버 내 지정된 Home 위치의 폴더 및 파일 목록을 관리하기 위한 클래스
 * @author TurboK
 */
public enum Repository implements DirectoryCallback
{
    RT;
    
    private FileDictionary      _fileDict;
    
    private DirectoryMonitor    _dirMonitor;
    
    
    private Repository()
    {
        //
    }
    
    public String getRootPath()
    {
        return _fileDict.getRootPath();
    }
    
    public void setUpRoot(String rootPath) throws Exception
    {
        _fileDict = new FileDictionary(rootPath);
        _fileDict.reload();
        
        if( _dirMonitor != null )
        {
            _dirMonitor.close();
        }
        
        _dirMonitor = new DirectoryMonitor(Paths.get(rootPath), true, this);
    }
    
    public void close()
    {
        if( _dirMonitor != null )
        {
            _dirMonitor.close();
            _dirMonitor = null;
        }
    }

    @Override
    public void onChange(Kind<?> kind, Path path)
    {
        // TODO Auto-generated method stub
        System.out.format("RT --> %s: %s\n", kind.name(), path);
        
        File file = path.toFile();
        
        if( kind == ENTRY_DELETE )
            _fileDict.removeEntry(file);
        else
            _fileDict.addEntry(file);
        
        // TODO 연결된 클라이언트에 전달하기
    }
}
