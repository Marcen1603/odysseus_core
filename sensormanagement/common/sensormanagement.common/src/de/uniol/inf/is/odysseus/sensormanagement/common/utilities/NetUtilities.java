package de.uniol.inf.is.odysseus.sensormanagement.common.utilities;

import java.io.IOException;
import java.net.ServerSocket;

public class NetUtilities 
{
	public static int getFreePortNum()
	{
		try
		{
			ServerSocket socket = new ServerSocket(0);
			int curPort = socket.getLocalPort();
			socket.close();
			return curPort;
		}
		catch (IOException e)
		{
			return -1;
		}
		
	}	
}
