package com.tool4us.homesync;

import static com.tool4us.homesync.file.Repository.RT;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.tool4us.homesync.file.FileDictionary;
import com.tool4us.logging.Logs;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;



/**
 * Tray Icon: 실행 중지 상태, 실행 중 상태, 동기화 중, 오류 발생
 * 
 * @author TurboK
 */
@SuppressWarnings("serial")
public class AppHomeSync extends JFrame
						 implements MouseListener, ActionListener
{
    protected final static AppHomeSync   _appMain = new AppHomeSync();

    protected final HomeSyncServer    _server = new HomeSyncServer();
    
	protected Image			_appIcon;
	protected TrayIcon		_trayIcon;
    protected SystemTray	_systemTray;
    
    private JTextField      _textFolder;

    
    protected AppHomeSync()
    {
        super("HomeSync Server");

        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception xe)
        {
            System.out.println("Unable to set LookAndFeel");
        }
        
        _appIcon = Toolkit.getDefaultToolkit().getImage( getClass().getResource("images/icon.png") );
        
        setIconImage( _appIcon );
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        initializeControls();
        initializeTrayIcon();
        
        setVisible(true);
        
        addWindowStateListener(new WindowStateListener()
        {
            public void windowStateChanged(WindowEvent evt)
            {
            	setVisible( (evt.getNewState() & ICONIFIED) != ICONIFIED );
            }
        });
    }
    
    private void initializeControls()
    {
    	setSize(358, 269);
    	
    	getContentPane().setLayout(null);
        
        JButton btnNewButton = new JButton("Change Icon");
        btnNewButton.setBounds(12, 183, 144, 38);
        getContentPane().add(btnNewButton);
        
        JButton btnPopUpMessage = new JButton("Pop up Message");
        btnPopUpMessage.setBounds(186, 183, 144, 38);
        getContentPane().add(btnPopUpMessage);
        
        JLabel lblFolderToBe = new JLabel("Folder for Sync:");
        lblFolderToBe.setBounds(12, 10, 124, 15);
        getContentPane().add(lblFolderToBe);
        
        _textFolder = new JTextField();
        _textFolder.setBounds(12, 29, 271, 21);
        getContentPane().add(_textFolder);
        _textFolder.setColumns(10);
        
        JButton btnFolder = new JButton("...");
        btnFolder.setBounds(285, 28, 45, 23);
        btnFolder.setActionCommand("FOLDER");
        btnFolder.addActionListener(this);
        getContentPane().add(btnFolder);
        
        btnNewButton.addActionListener( new ActionListener()
        {
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				Image image = Toolkit.getDefaultToolkit().getImage( getClass().getResource("images/icon2.png") );
				
				_trayIcon.setImage(image);
			}
        });
        
        btnPopUpMessage.addActionListener( new ActionListener()
        {
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				_trayIcon.displayMessage("Tray Test", "This is a message for testing pop-up", MessageType.INFO);
			}
        });
    }
    
    private void initializeTrayIcon()
    {
        if( SystemTray.isSupported() )
        {
            System.out.println("system _systemTray supported");
            
            _systemTray = SystemTray.getSystemTray();

            ActionListener exitListener = new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    System.out.println("Exiting....");
                    _appMain.stopServer();
                }
            };

            PopupMenu popup = new PopupMenu();
            MenuItem defaultItem = new MenuItem("Exit");
            
            defaultItem.addActionListener(exitListener);
            popup.add(defaultItem);
            
            defaultItem = new MenuItem("Open");
            
            defaultItem.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    setVisible(true);
                    setExtendedState(JFrame.NORMAL);
                }
            });
            
            popup.add(defaultItem);
            
            _trayIcon = new TrayIcon(_appIcon, "SystemTray Demo", popup);
            
            // _trayIcon.setImage(image);
            _trayIcon.setImageAutoSize(true);
            _trayIcon.setToolTip("HomeSync Tooltip");
            
            _trayIcon.setActionCommand("TRAY");

            _trayIcon.addActionListener(this);
            _trayIcon.addMouseListener(this);
            
            try
            {
                _systemTray.add(_trayIcon);
                // setVisible(false);
                System.out.println("added to SystemTray");
            }
            catch (AWTException ex)
            {
                System.out.println("unable to add to _systemTray");
            }
        }
        else
        {
            System.out.println("system SystemTray not supported");
        }
    }
    
	@Override
	public void actionPerformed(ActionEvent evt)
	{
		String actCmd = evt.getActionCommand();
		
		if( "TRAY".equals(actCmd) )
		{
			System.out.println("clicked tray's popup message");
		}
		else if( "FOLDER".equals(actCmd) )
		{
		    System.out.println("select folder");
		    // _textFolder
		}
	}

	@Override
	public void mouseClicked(MouseEvent evt)
	{
		// TODO Auto-generated method stub
		System.out.println("mouseClicked");
	}

	@Override
	public void mousePressed(MouseEvent evt)
	{
		// TODO Auto-generated method stub
		System.out.println("mousePressed");
		
	}

	@Override
	public void mouseReleased(MouseEvent evt)
	{
		// TODO Auto-generated method stub
		System.out.println("mouseReleased");
		
	}

	@Override
	public void mouseEntered(MouseEvent evt)
	{
		// TODO Auto-generated method stub
		System.out.println("mouseEntered");
		
	}

	@Override
	public void mouseExited(MouseEvent evt)
	{
		// TODO Auto-generated method stub
		System.out.println("mouseExited");
	}
	
	public void startServer() throws Exception
	{
	    _server.start(6070, 1, 4);
	}
	
	public void stopServer()
	{
	    _server.shutdown();
	    System.exit(0);
	}
	
	private static void startConsole()
    {   
        String lineCmd = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        while( true )
        {
            System.out.print(">> ");
            
            // Getting command line.
            try
            {
                lineCmd = in.readLine();
            }
            catch(IOException e)
            {
                e.printStackTrace();
                break;
            }
            
            if( lineCmd == null )
                break;
            
            if( lineCmd.isEmpty() )
                continue;

            if( "t1".equals(lineCmd) )
            {
                System.out.println( RT.getFileDictionary().toJson() );
            }
            else if( "t2".equals(lineCmd) )
            {
                String jsonStr = "{\"version\":1, \"rootPath\":\"C:/temp/homesync\",\"fileMap\":[{\"pathName\":\"/bbb.txt\",\"size\":3,\"time\":1488158126076},{\"pathName\":\"/c1\",\"size\":4096,\"time\":1488158332776},{\"pathName\":\"/c1/c2\",\"size\":0,\"time\":1488158183287},{\"pathName\":\"/c1/icons\",\"size\":0,\"time\":1488158362727},{\"pathName\":\"/c1/icons/128.png\",\"size\":3902,\"time\":1346881299874},{\"pathName\":\"/c1/icons/16-2.png\",\"size\":527,\"time\":1346881299874},{\"pathName\":\"/c1/icons/16.png\",\"size\":527,\"time\":1346881299874},{\"pathName\":\"/c1/icons/32.png\",\"size\":1062,\"time\":1346881299875},{\"pathName\":\"/c1/icons/64.png\",\"size\":2240,\"time\":1346881299875},{\"pathName\":\"/c1/index.html\",\"size\":184,\"time\":1488158346254},{\"pathName\":\"/c1/rainbowsky.jpg\",\"size\":119272,\"time\":1346881299876},{\"pathName\":\"/c1/style.css\",\"size\":38701,\"time\":1346881299337}]}";
                
                FileDictionary.fromJson(jsonStr).debugOut();
            }
            // 종료
            else if( "q".equals(lineCmd) || "quit".equals(lineCmd) )
            {   
                System.exit(0);
            }
        }
    }
	
	public static void main(String[] args)
    {
	    try
	    {
	        Logs.initDefault(null, "HomeSync");
	        Logs.addConsoleLogger();

	        RT.setUpRoot("C:\\temp\\homesync");

	        _appMain.startServer();
	        
	        startConsole();
	    }
	    catch(Exception xe)
	    {
	        xe.printStackTrace();
	    }
    }
}

