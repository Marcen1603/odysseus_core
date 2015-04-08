package de.uniol.inf.is.odysseus.sensormanagement.client.executor;

import java.net.MalformedURLException;
import java.util.List;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.client.WsClient;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel2;

public abstract class SensorClient implements ILoggable
{	
	private String userName;	
	protected boolean initialized = false;
	
	protected WsClient odysseusClient;
	protected ISession odysseusSession;
	protected WsSensorClient sensorClient;
	protected List<RemoteSensor> sensors;
	
	public List<RemoteSensor> getSensors() { return sensors; }
	public boolean isInitialized() { return initialized; }
	public String getUserName()	{ return userName; }	
	
	public SensorClient(WsClient client, ISession session, String wsdlLocation) throws MalformedURLException
	{
		this.odysseusClient = client;
		this.odysseusSession = session;
				
		sensorClient = new WsSensorClient(wsdlLocation + ";http://sensors.odysseus.is.inf.uniol.de/;SensorServiceService");
		
		List<String> sensorIds = sensorClient.getSensorIds(odysseusSession);

		for (String sensorId : sensorIds)
			sensors.add(new RemoteSensor(this, sensorClient.getSensorById(odysseusSession, sensorId)));		
		
		initialized = true;
	}

	public void close()
	{			
		initialized = false;
		for (RemoteSensor sensor : sensors)
			sensorClient.stopLiveView(odysseusSession, sensor.getId());
		
		try
		{
			odysseusClient.logout(odysseusSession);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void addSensor(RemoteSensor sensor)
	{
		sensorClient.addSensor(odysseusSession, sensor.getSensorModel2());
		sensors.add(sensor);	
		
		if (isInitialized())
			onSensorAdded(sensor);
	}	
	
	public void updateSensor(RemoteSensor sensor, SensorModel2 newSensorInfo)
	{
		sensorClient.modifySensor(odysseusSession, sensor.getId(), newSensorInfo);
		
		if (isInitialized())
			onSensorChanged(sensor);
	}		
	
	public void removeSensor(RemoteSensor sensor)
	{
		sensorClient.removeSensor(odysseusSession, sensor.getId());
		sensors.remove(sensor);		
		
		if (isInitialized())
			onSensorRemoved(sensor);
	}
	
	@Override public void startLogging() 
	{
		for (RemoteSensor sensor : sensors) 
			sensorClient.startLogging(odysseusSession, sensor.getId());
		
		if (isInitialized())
			onClientChanged();
	}

	@Override public void stopLogging() 
	{
		for (RemoteSensor sensor : sensors) 
			sensorClient.stopLogging(odysseusSession, sensor.getId());
		
		if (isInitialized())
			onClientChanged();
	}	

	protected abstract void onSensorAdded(RemoteSensor sensor);
	protected abstract void onSensorChanged(RemoteSensor sensor);
	protected abstract void onSensorRemoved(RemoteSensor sensor);
	protected abstract void onClientChanged();
}
