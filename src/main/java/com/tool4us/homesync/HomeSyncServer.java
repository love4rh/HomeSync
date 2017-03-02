package com.tool4us.homesync;

import static com.tool4us.homesync.file.Repository.RT;

import com.tool4us.net.server.ClientSession;
import com.tool4us.net.server.CommonSession;
import com.tool4us.net.server.TCPServer;



public class HomeSyncServer extends TCPServer
{
	protected HomeSyncServer()
	{
		//
	}

    @Override
    public ClientSession newClientSession()
    {
        return new CommonSession("com.tool4us.homesync.handler");
    }
}
