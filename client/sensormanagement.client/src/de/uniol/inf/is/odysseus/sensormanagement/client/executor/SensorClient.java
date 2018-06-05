package de.uniol.inf.is.odysseus.sensormanagement.client.executor;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.client.WsClient;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorType;

public abstract class SensorClient implements ILoggable
{	
	private String userName;	
	private String localIp;
	protected boolean initialized = false;
	
	protected WsClient odysseusClient;
	protected ISession odysseusSession;
	protected WsSensorClient sensorClient;
	protected List<RemoteSensor> sensors;
	protected List<SensorType> sensorTypes;
	
	public List<RemoteSensor> getSensors() { return sensors; }
	public List<SensorType> getSensorTypes() { return sensorTypes; }
	
	public boolean isInitialized() { return initialized; }
	public String getUserName()	{ return userName; }	
	public String getLocalIp() { return localIp; }
	
	public SensorClient(String name, WsClient client, ISession session, String wsdlLocation, String localIp) throws MalformedURLException
	{
		this.odysseusClient = client;
		this.odysseusSession = session;
		this.localIp = localIp;
				
		sensorClient = new WsSensorClient(wsdlLocation + ";http://server.sensormanagement.odysseus.is.inf.uniol.de/;SensorServiceService");
		sensorClient.init(session, name, "D:/test/records");
		
		List<String> sensorIds = sensorClient.getSensorIds(odysseusSession);
		sensors = new ArrayList<RemoteSensor>(sensorIds.size());

		for (String sensorId : sensorIds)
			sensors.add(new RemoteSensor(this, sensorClient.getSensorById(odysseusSession, sensorId)));		
		
		List<String> sensorTypes = sensorClient.getSensorTypes(odysseusSession);
		this.sensorTypes = new ArrayList<SensorType>(sensorTypes.size());

		for (String sensorType : sensorTypes)
			this.sensorTypes.add(sensorClient.getSensorType(odysseusSession, sensorType));				
		
		initialized = true;
	}

	public void close()
	{			
		initialized = false;

		for (RemoteSensor sensor : sensors)
		{
			try {
				sensorClient.stopLiveView(odysseusSession, sensor.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void addSensor(RemoteSensor sensor)
	{
		sensor.getSensorModel().id = sensorClient.addSensor(odysseusSession, sensor.getSensorModel());
		sensors.add(sensor);	
		
		if (isInitialized())
			onSensorAdded(sensor);
	}	
	
	public void updateSensor(RemoteSensor sensor, SensorModel newSensorInfo)
	{
		sensor.updateSensorModel(newSensorInfo);
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

	public void removeAllSensors()
	{
		try {		
			for (int i = sensors.size()-1; i >= 0; i--)
				removeSensor(sensors.get(i));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	@Override public void startLogging() 
	{
		for (RemoteSensor sensor : sensors) 
			sensorClient.startLogging(odysseusSession, sensor.getId(), SensorModel.DEFAULT_LOGGING_STYLE);
		
		if (isInitialized())
			onClientChanged();
	}

	@Override public void startLogging(String loggingStyle) 
	{
		for (RemoteSensor sensor : sensors) 
			sensorClient.startLogging(odysseusSession, sensor.getId(), loggingStyle);
		
		if (isInitialized())
			onClientChanged();
	}
	
	
	@Override public void stopLogging(String loggingStyle) 
	{
		for (RemoteSensor sensor : sensors) 
			sensorClient.stopLogging(odysseusSession, sensor.getId(), loggingStyle);
		
		if (isInitialized())
			onClientChanged();
	}	

	@Override public void stopAllLogging() 
	{
		for (RemoteSensor sensor : sensors) 
			sensorClient.stopAllLogging(odysseusSession, sensor.getId());
		
		if (isInitialized())
			onClientChanged();
	}		
	
	protected abstract void onSensorAdded(RemoteSensor sensor);
	protected abstract void onSensorChanged(RemoteSensor sensor);
	protected abstract void onSensorRemoved(RemoteSensor sensor);
	protected abstract void onClientChanged();
}
