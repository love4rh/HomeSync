package com.tool4us.homesync;

import static com.tool4us.homesync.Repository.RT;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.UIManager;

import javax.swing.JButton;



/**
 * Tray Icon: 실행 중지 상태, 실행 중 상태, 동기화 중, 오류 발생
 * 
 * @author TurboK
 */
@SuppressWarnings("serial")
public class AppHomeSync extends JFrame
						 implements MouseListener, ActionListener
{
	protected Image			_appIcon;
	protected TrayIcon		_trayIcon;
    protected SystemTray	_systemTray;
    
    protected final HomeSyncServer    _server = new HomeSyncServer();

    
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
    	setSize(340, 248);
    	
    	getContentPane().setLayout(null);
        
        JButton btnNewButton = new JButton("Change Icon");
        btnNewButton.setBounds(12, 10, 144, 55);
        getContentPane().add(btnNewButton);
        
        JButton btnPopUpMessage = new JButton("Pop up Message");
        btnPopUpMessage.setBounds(12, 75, 144, 55);
        getContentPane().add(btnPopUpMessage);
        
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
                    System.exit(0);
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
	
	public static void main(String[] args)
    {
	    AppHomeSync appMain = new AppHomeSync();
	    
	    try
	    {
	        RT.setUpRoot("C:\\temp\\homesync");
	        appMain.startServer();
	    }
	    catch(Exception xe)
	    {
	        xe.printStackTrace();
	    }
    }
}

