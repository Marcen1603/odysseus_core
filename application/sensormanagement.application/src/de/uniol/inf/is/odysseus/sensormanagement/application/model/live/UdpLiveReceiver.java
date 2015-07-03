package de.uniol.inf.is.odysseus.sensormanagement.application.model.live;

import java.io.IOException;

import de.uniol.inf.is.odysseus.sensormanagement.client.executor.RemoteSensor;

public abstract class UdpLiveReceiver extends LiveSensor
{	
	private UdpClient liveReceiver;
	private int readSize;

	public UdpLiveReceiver(RemoteSensor sensor, int readSize)
	{		
		super(sensor);
		this.readSize = readSize;
	}
	
	@Override protected void onStart() throws IOException
	{
		super.onStart();
		
		int port = getPortFromUrl(getStreamUrl());
		liveReceiver = new UdpClient(port, readSize)
						{
							@Override public void receiveData(String data) 
							{
								UdpLiveReceiver.this.receiveData(data);
							}
						};
	}

	@Override protected void onStop()
	{
		super.onStop();
		
		try 
		{
			if (liveReceiver != null)
				liveReceiver.close();
		} 
		finally
		{
			liveReceiver = null;
		}
	}

	public abstract void receiveData(String message); 
}
