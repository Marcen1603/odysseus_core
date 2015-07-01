package de.uniol.inf.is.odysseus.sensormanagement.application.model.live;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public abstract class UdpClient 
{
	private int 				port;
	private DatagramSocket 		serverSocket;
	private DatagramPacket 		receivePacket;
	
	private Thread 				thread;
	protected volatile boolean 	running;
	
	public int getPort()
	{
		return port;
	}
	
	public UdpClient(int port, int readSize) throws UnknownHostException, IOException
	{
		if (readSize == 0) readSize = 2048;
		
		this.port = port;
		
		serverSocket = new DatagramSocket(port);
		
		byte[] receiveData = new byte[readSize];
		receivePacket = new DatagramPacket(receiveData, receiveData.length);
		
		running = true;
		thread = new Thread() 
		{
			public void run() 
			{
				while (running)
				{
					try
					{
						serverSocket.receive(receivePacket);
						UdpClient.this.receiveData(new String(receivePacket.getData(), 0, receivePacket.getLength()));
					}
					catch (SocketException e)
					{
						if (!e.getMessage().equals("socket closed"))
							e.printStackTrace();
					}
					catch (IOException e) 
					{
						e.printStackTrace();
					}										
				}
			}
		};
			
		thread.start();		
	}
	
	public void close()
	{
		try 
		{
			running = false;
			thread.join(1000);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
		serverSocket.close();
	}
	
	public abstract void receiveData(String data);
}
