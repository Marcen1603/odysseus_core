package de.uniol.inf.is.odysseus.sensormanagement.application.view.live;

import java.io.IOException;

import de.uniol.inf.is.odysseus.sensormanagement.application.view.Receiver;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.ViewException;
import de.uniol.inf.is.odysseus.sensormanagement.client.executor.RemoteSensor;

public abstract class LiveReceiver extends Receiver 
{
	private RemoteSensor sensor;
	private String streamUrl;

	public LiveReceiver(RemoteSensor sensor) 
	{
		super(sensor.getSensorModel2());
		
		this.sensor = sensor;
	}
	
	public RemoteSensor getSensor()
	{
		return sensor;
	}
	
	@Override protected void onStart() throws IOException
	{
		streamUrl = sensor.startLiveView();
	}
	
	@Override protected void onStop()
	{
		getSensor().stopLiveView();
	}

	public String getStreamUrl()
	{
		return streamUrl;
	}
	
	public static int getPortFromUrl(String url) 
	{
		int portIdx = url.lastIndexOf(':');		
		if (portIdx == -1) throw new ViewException("No port number given in \"" + url + "\"!");
		int port = Integer.parseInt(url.substring(portIdx+1));
		return port;
	}	
}