package com.tool4us.util;

import java.io.File;

/**
 * 동기화 대상 파일 및 디렉토리 정보 관리 클래스
 * 
 * @author TurboK
 */
public class FileElement
{
    private String      _uniquePathName = null;
    private long        _fileSize = 0L;
    private long        _modifiedTime = 0L;
    
    
    public FileElement(String rootPath, File file)
    {
        String absPath = file.getAbsolutePath();
        
        // absPath가 rootPath의 하위 폴더가 아닌 경우는 무시해야 함.
        // if( absPath.indexOf(rootPath) == -1 )
        
        String uniquePath = absPath.substring(rootPath.length());
        
        _uniquePathName = makeKey(uniquePath);

        _fileSize = file.length();
        _modifiedTime = file.lastModified();
    }
    
    public static String makeKey(String uniquePath)
    {
        return uniquePath.replace('\\', '/');
    }
    
    public String getKey()
    {
        return _uniquePathName;
    }
    
    public String getFileName()
    {
        return _uniquePathName.substring(_uniquePathName.lastIndexOf('/') + 1);
    }
    
    public String getUniquePath()
    {
        return _uniquePathName.substring(0, _uniquePathName.lastIndexOf('/'));
    }
    
    public String getAbsolutePath(String rootPath)
    {
        return rootPath + _uniquePathName.replace('/', File.separatorChar);
    }
    
    public long getFileSize()
    {
        return _fileSize;
    }
    
    public long getModifiedTime()
    {
        return _modifiedTime;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if( !(obj instanceof FileElement) )
            return false;
        
        if( this == obj )
            return true;
        
        FileElement that = (FileElement) obj;

        return this._uniquePathName.equals(that._uniquePathName)
                && this._fileSize == that._fileSize
                && this._modifiedTime == that._modifiedTime;
    }
    
    @Override
    public int hashCode()
    {
        return _uniquePathName.hashCode();
    }
    
    public void debugOut()
    {
        System.out.println(getKey() + ": "
            + getFileName() + " | "
            + getUniquePath() + " | "
            + _fileSize + " | "
            + _modifiedTime
        );
    }
}
