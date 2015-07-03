package de.uniol.inf.is.odysseus.sensormanagement.application.model.live;

import java.io.IOException;

import de.uniol.inf.is.odysseus.sensormanagement.application.model.AbstractSensor;
import de.uniol.inf.is.odysseus.sensormanagement.client.executor.RemoteSensor;

public abstract class LiveSensor extends AbstractSensor 
{
	private RemoteSensor sensor;
	private String streamUrl;

	public LiveSensor(RemoteSensor sensor) 
	{
		super(sensor.getSensorModel());
		
		this.sensor = sensor;
	}
	
	public RemoteSensor getRemoteSensor()
	{
		return sensor;
	}
	
	@Override protected void onStart() throws IOException
	{
		streamUrl = sensor.startLiveView();
	}
	
	@Override protected void onStop()
	{
		getRemoteSensor().stopLiveView();
	}

	public String getStreamUrl()
	{
		return streamUrl;
	}
	
	public static int getPortFromUrl(String url) 
	{
		int portIdx = url.lastIndexOf(':');		
		if (portIdx == -1) throw new IllegalArgumentException("No port number given in \"" + url + "\"!");
		int port = Integer.parseInt(url.substring(portIdx+1));
		return port;
	}	
}
