package de.uniol.inf.is.odysseus.sensormanagement.client.executor;

import java.net.UnknownHostException;

import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.NetUtilities;

public class RemoteSensor implements ILoggable 
{
	private boolean loggingRunning;
	private boolean liveViewRunning;
	private SensorModel sensor;
	private SensorClient client;
	
	public String getId() { return sensor.id; }
	public SensorClient getClient() { return client; }
	public SensorModel getSensorModel() { return sensor; }
	public boolean isLogging() { return loggingRunning; }
	public boolean isLive() { return liveViewRunning; }	

	public void updateSensorModel(SensorModel sensor) { this.sensor.assign(sensor); }
	
	public RemoteSensor(SensorClient client, SensorModel sensor) 
	{
		this.sensor = sensor;
		this.client = client;
	}

	@Override
	public void startLogging() 
	{
		client.sensorClient.startLogging(client.odysseusSession, sensor.id);		
		client.onSensorChanged(this);
		loggingRunning = true;
	}

	@Override
	public void stopLogging() 
	{
		client.sensorClient.stopLogging(client.odysseusSession, sensor.id);		
		client.onSensorChanged(this);
		loggingRunning = false;
	}

	public String startLiveView() throws UnknownHostException 
	{
		String localIp = client.getLocalIp();
		int port = NetUtilities.getFreePortNum();
		
		String streamUrl = client.sensorClient.startLiveView(client.odysseusSession, sensor.id, localIp, port);		
		client.onSensorChanged(this);
		liveViewRunning = true;
		return streamUrl;
	}

	public void stopLiveView() 
	{
		client.sensorClient.stopLiveView(client.odysseusSession, sensor.id);		
		client.onSensorChanged(this);		
		liveViewRunning = false;
	}		
}
