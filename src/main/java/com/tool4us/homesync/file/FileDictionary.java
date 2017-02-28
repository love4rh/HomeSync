package com.tool4us.homesync.file;

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
    
    public void addEntry(File file)
    {
        synchronized(_fileMap)
        {
            FileElement elem = new FileElement(_rootPath, file);
            
            _fileMap.put(elem.getKey(), elem);
            
            elem.debugOut();
        }
    }
    
    public FileElement removeEntry(File file)
    {
        synchronized(_fileMap)
        {
            FileElement elem = new FileElement(_rootPath, file);
            
            return _fileMap.remove(elem.getKey());
        }
    }
    
    // for testing
    public void addEntry(String uniquePathName, long fileSize, long mTime)
    {
        synchronized(_fileMap)
        {
            FileElement elem = new FileElement(uniquePathName, fileSize, mTime);
            
            _fileMap.put(elem.getKey(), elem);
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
    
    public FileElement get(String key)
    {
        return _fileMap.get(key);
    }
    
    public String getRootPath()
    {
        return _rootPath;
    }
    
    /**
     * 두 파일 목록을 비교하여 변경된 것을 반환하는 메소드.
     * 내 것을 기준으로 하여 나한테만 있는 것, 없는 것, 변경된 것을 구분하여 반환.
     * 비교 결과 - 1: 나한테만 있음, 2: 쟤한테만 있음, 3: 양쪽에 있는데 다름.
     * @param that
     * @return
     */
    public List<CompResult> diffList(FileDictionary that)
    {
        List<CompResult> dList = new ArrayList<CompResult>();
        
        List<String> k1 = new ArrayList<String>();
        List<String> k2 = new ArrayList<String>();
        
        k1.addAll( this._fileMap.keySet() );
        k2.addAll( that._fileMap.keySet() );
        
        int jOnlyPos = -1;
        int i = 0, j = 0, comp = 0;
        
        while( i < k1.size() )
        {
            if( jOnlyPos != -1 )
            {
                // 쟤한테서 jOnlyPos에 있는 것을 2로 추가
                dList.add(new CompResult(that.get(k2.get(jOnlyPos)), CompResult.YOU_HAVE));
                jOnlyPos = -1;
            }
            
            if( j >= k2.size() )
                break;
            
            comp = k1.get(i).compareTo(k2.get(j));
            
            // 양쪽에 다 있음
            if( comp == 0 )
            {
                FileElement e1 = this.get( k1.get(i) );
                FileElement e2 = that.get( k2.get(j) );
                
                dList.add(new CompResult(e1, e1.equals(e2) ? CompResult.BOTH_HAVE : CompResult.DIFF_HAVE));
                
                i += 1;
                j += 1;
            }
            // 내가 더 큰 경우. 나 한테는 없는 것이 있는 경우임.
            else if( comp > 0 )
            {
                // 이 위치를 기록하고 같은 것이 나올 때 까지 루핑
                jOnlyPos = j;
                j += 1;
            }
            // 내가 더 작은 경우. 나 한테만 있는 것이 있는 경우임.
            else if( comp < 0 )
            {
                dList.add(new CompResult(this.get(k1.get(i)), CompResult.I_HAVE));
                i += 1;
            }
        }
        
        while( j < k2.size() )
        {
            dList.add(new CompResult(that.get(k2.get(j)), CompResult.YOU_HAVE));
        }
        
        return dList;
    }
    
    public static void main(String[] args)
    {
        FileDictionary d1 = new FileDictionary("/home/server");
        FileDictionary d2 = new FileDictionary("/home/client");
        
        d1.addEntry("/f1.txt", 1, 2);
        d1.addEntry("/d1", 0, 3);
        d1.addEntry("/d1/f2.txt", 2, 3);
        d1.addEntry("/d1/f3.txt", 2, 4);
        d1.addEntry("/d2", 0, 3);
        d1.addEntry("/d2/f5.txt", 2, 3);
        
        d2.addEntry("/a1.txt", 1, 2);
        d2.addEntry("/f1.txt", 1, 2);
        d2.addEntry("/d1", 0, 3);
        d2.addEntry("/d1/f3.txt", 2, 3);
        d2.addEntry("/d2", 0, 3);
        d2.addEntry("/d2/f4.txt", 2, 3);
        d2.addEntry("/d2/f5.txt", 2, 3);
        d2.addEntry("/d3", 2, 4);
        
        List<CompResult> dList = d1.diffList(d2);
        
        for(CompResult comp : dList)
        {
            System.out.print(comp.getCompResult() + " --> ");
            ((FileElement) comp.getRelatedData()).debugOut();
        }
        
        
        System.out.println("\n\n");
        
        dList = d2.diffList(d1);
        
        for(CompResult comp : dList)
        {
            System.out.print(comp.getCompResult() + " --> ");
            ((FileElement) comp.getRelatedData()).debugOut();
        }
    }
}
