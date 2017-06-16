package com.tool4us.homesync;

import static com.tool4us.util.CommonTool.CT;
import static com.tool4us.homesync.file.Repository.RT;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.tool4us.util.AppSetting;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;



/**
 * Tray Icon: 실행 중지 상태, 실행 중 상태, 동기화 중, 오류 발생
 * 
 * @author TurboK
 */
@SuppressWarnings("serial")
public class AppHomeSync extends JFrame
						 implements MouseListener, ActionListener
{
	protected final HomeSyncServer   _server = new HomeSyncServer();
	
    protected AppSetting    _setting;
    
	protected Image			_appIcon;
	protected TrayIcon		_trayIcon;
    protected SystemTray	_systemTray;
    
    private JTextField      _textFolder;
    
    private Container		_contentPane;
    private JPanel			_panelMain;

    
    protected AppHomeSync()
    {
        super("HomeSync Server");
        
        _setting = new AppSetting( CT.getAppPath("homesync.cfg") );

        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            _setting.load();
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
        
        addComponentListener(new ComponentAdapter()
        {
        	@Override
        	public void componentResized(ComponentEvent evt)
        	{
       			_panelMain.setBounds(10, 10, _contentPane.getWidth() - 20, _contentPane.getHeight() - 20);
        	}
        });
        
        addWindowStateListener(new WindowStateListener()
        {
        	@Override
            public void windowStateChanged(WindowEvent evt)
            {
            	setVisible( (evt.getNewState() & ICONIFIED) != ICONIFIED );
            }
        });
    }
    
    private void initializeControls()
    {
    	setSize(468, 373);
    	
    	_contentPane = getContentPane();
    	
    	_panelMain = new JPanel();
    	_panelMain.setBounds(10, 10, 449, 324);
    	_panelMain.setLayout(new BorderLayout(0, 0));
        
        JPanel panelFolder = new JPanel();
        panelFolder.setLayout(new BorderLayout(0, 5));
        
        _textFolder = new JTextField();
        panelFolder.add(_textFolder, BorderLayout.CENTER);
        _textFolder.setColumns(10);
        
        JButton btnFolder = new JButton("...");
        panelFolder.add(btnFolder, BorderLayout.EAST);
        btnFolder.setActionCommand("FOLDER");
        
        JLabel lblFolderToBe = new JLabel("Folder for Sync:");
        panelFolder.add(lblFolderToBe, BorderLayout.NORTH);
        btnFolder.addActionListener(this);
        getContentPane().setLayout(null);
        
        _panelMain.add(panelFolder, BorderLayout.NORTH);
        
        _contentPane.add(_panelMain);
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

                    stopServer();
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
			
			setVisible(true);
            setExtendedState(JFrame.NORMAL);
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
	    RT.close();

	    try
        {
            _setting.save();
        }
        catch( Exception xe )
        {
            xe.printStackTrace();
        }
	    
	    _server.shutdown();

	    System.exit(0);
	}
	
	public AppSetting appSetting()
	{
	    return _setting;
	}
}

