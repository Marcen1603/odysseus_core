package de.uniol.inf.is.odysseus.sensormanagement.application.model.live;

import java.net.MalformedURLException;
import java.util.List;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.client.WsClient;
import de.uniol.inf.is.odysseus.sensormanagement.application.SensorFactory;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.AbstractInstance;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.AbstractSensor;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.ChangeListener;
import de.uniol.inf.is.odysseus.sensormanagement.client.executor.ILoggable;
import de.uniol.inf.is.odysseus.sensormanagement.client.executor.RemoteSensor;
import de.uniol.inf.is.odysseus.sensormanagement.client.executor.SensorClient;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorType;

public class LiveInstance extends AbstractInstance implements ILoggable
{
	private SensorClient client;
	
	public SensorClient getClient() { return client; }
	
	public LiveInstance(String instanceName, String ethernetAddr, String userName, String password) throws MalformedURLException
	{
		super("Box", ethernetAddr);
		
		WsClient odysseusClient = new WsClient();
		String url = "http://" + ethernetAddr + "/odysseus?wsdl;http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/;WebserviceServerService";
		
		ISession odysseusSession = odysseusClient.login(userName, password.getBytes(), "", url);
		if (odysseusSession == null) throw new PlanManagementException("Login on server failed");
		
		odysseusClient.reloadStoredQueries(odysseusSession);
				
		client = new SensorClient(instanceName, odysseusClient, odysseusSession, "http://" + ethernetAddr + "/odysseus/sensormanagement?wsdl")
		{
			@Override protected void onSensorAdded(RemoteSensor remoteSensor) 
			{
				AbstractSensor sensor = SensorFactory.getInstance().getSensorType(remoteSensor.getSensorModel().type).createSensor(remoteSensor);
				LiveInstance.this.getSensors().add(sensor);
				
				for (ChangeListener l : boxListeners)
					l.onSensorAdded(getSensorById(sensor.getSensorModel().id));
			}

			@Override protected void onSensorChanged(RemoteSensor sensor) 
			{
				for (ChangeListener l : boxListeners)
					l.onSensorChanged(getSensorById(sensor.getSensorModel().id));
			}

			@Override protected void onSensorRemoved(RemoteSensor sensor) 
			{
				for (ChangeListener l : boxListeners)
					l.onSensorRemoved(getSensorById(sensor.getSensorModel().id));
			}

			@Override protected void onClientChanged() 
			{
				instanceChanged();
			}			
		};
	}
	
	@Override public void startLogging() 
	{
		client.startLogging();
	}
	
	@Override public void stopLogging() 
	{
		client.stopLogging();
	}
	
	public void close() 
	{
		client.close();
	}
	
	public List<SensorType> getSensorTypes() 
	{
		return client.getSensorTypes();
	}
}
