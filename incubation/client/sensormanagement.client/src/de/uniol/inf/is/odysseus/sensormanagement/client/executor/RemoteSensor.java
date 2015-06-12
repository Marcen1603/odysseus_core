package de.uniol.inf.is.odysseus.sensormanagement.client.executor;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.NetUtilities;

@SuppressWarnings(value = { "all" })
public class RemoteSensor implements ILoggable 
{
	private SensorModel sensor;
	private SensorClient client;
	
	public String getId() { return sensor.id; }
	public SensorClient getClient() { return client; }
	public SensorModel getSensorModel2() { return sensor; }
	
	public void setSensorModel2(SensorModel sensor) { this.sensor = sensor; }
	
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
	}

	@Override
	public void stopLogging() 
	{
		client.sensorClient.stopLogging(client.odysseusSession, sensor.id);		
		client.onSensorChanged(this);
	}

	public String startLiveView() throws UnknownHostException 
	{
		String localIp = Inet4Address.getLocalHost().getHostAddress();
		int port = NetUtilities.getFreePortNum();
		
		String streamUrl = client.sensorClient.startLiveView(client.odysseusSession, sensor.id, localIp, port);		
		client.onSensorChanged(this);
		
		return streamUrl;
	}

	public void stopLiveView() 
	{
		client.sensorClient.stopLiveView(client.odysseusSession, sensor.id);		
		client.onSensorChanged(this);
	}	
	
	public boolean isLogging()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isLive() 
	{
		// TODO Auto-generated method stub		
		return false;
	}	
}
