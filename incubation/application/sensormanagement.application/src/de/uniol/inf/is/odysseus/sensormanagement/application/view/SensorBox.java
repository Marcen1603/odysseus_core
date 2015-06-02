package de.uniol.inf.is.odysseus.sensormanagement.application.view;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.client.WsClient;
import de.uniol.inf.is.odysseus.sensormanagement.client.executor.ILoggable;
import de.uniol.inf.is.odysseus.sensormanagement.client.executor.SensorClient;
import de.uniol.inf.is.odysseus.sensormanagement.client.executor.RemoteSensor;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorType;

public class SensorBox implements ILoggable
{
	private SensorClient client;
	
	private String ethernetAddr;
	private String position;
	private String name;

	protected List<ChangeListener> boxListeners = new ArrayList<>();

	public SensorClient getClient() { return client; }
	public String getPosition()	{ return position; }
	public String getName() { return name; }	
	public String getEthernetAddr() { return ethernetAddr; }
	
	public void setPosition(String position) { this.position = position;	}	

	@Override public String toString() { return name + " @ " + getEthernetAddr(); }	
	
	public SensorBox(String ethernetAddr, String userName, String password) throws MalformedURLException
	{
		this.name = "Box";
		this.ethernetAddr = ethernetAddr;
		
		WsClient odysseusClient = new WsClient();
//		client.addUpdateEventListener(this, IUpdateEventListener.SCHEDULING, null);
//		client.addUpdateEventListener(this, IUpdateEventListener.QUERY, null);
		String url = "http://" + ethernetAddr + "/odysseus?wsdl;http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/;WebserviceServerService";
		
		ISession odysseusSession = odysseusClient.login(userName, password.getBytes(), "", url);
		if (odysseusSession == null) throw new PlanManagementException("Login on server failed");
		
		odysseusClient.reloadStoredQueries(odysseusSession);
				
		client = new SensorClient(odysseusClient, odysseusSession, "http://" + ethernetAddr + "/odysseus/sensormanagement?wsdl")
		{
			@Override protected void onSensorAdded(RemoteSensor sensor) 
			{
				for (ChangeListener l : boxListeners)
					l.onSensorAdded(sensor);
			}

			@Override protected void onSensorChanged(RemoteSensor sensor) 
			{
				for (ChangeListener l : boxListeners)
					l.onSensorChanged(sensor);
			}

			@Override protected void onSensorRemoved(RemoteSensor sensor) 
			{
				for (ChangeListener l : boxListeners)
					l.onSensorRemoved(sensor);
			}

			@Override protected void onClientChanged() 
			{
				boxChanged();
			}			
		};
	}
	
	protected void boxChanged() 
	{
		for (ChangeListener l : boxListeners)
			l.onBoxChanged(SensorBox.this);
	}
	
	public void addBoxListener(ChangeListener listener) 
	{
		boxListeners.add(listener);
	}	
	
	public void setName(String name) 					
	{ 
		this.name = name;
		boxChanged();
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
