package de.uniol.inf.is.odysseus.sensormanagement.application.model.live;

import de.uniol.inf.is.odysseus.sensormanagement.application.SensorFactory;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.AbstractInstance;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.AbstractSensor;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.AbstractSensorManager;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.ChangeListener;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.Scene;
import de.uniol.inf.is.odysseus.sensormanagement.client.executor.RemoteSensor;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.Callback;

public class LiveSensorManager extends AbstractSensorManager 
{
	private ChangeListener boxListener;

	public class ConnectData
	{
		public String ethernetAddr;
		public Exception e;
		
		public ConnectData(String ethernetAddr,	Exception e)
		{
			this.ethernetAddr = ethernetAddr;
			this.e = e;
		}
	}	
	
	public Callback<LiveSensorManager, ConnectData> onConnectFailed = new Callback<>();
	
	public LiveSensorManager(Scene scene)
	{
		super(scene);
		
		boxListener = new ChangeListener() 
		{ 
			@Override public void onInstanceChanged(AbstractInstance instance) { onInstanceChanged.raise(LiveSensorManager.this, instance); }			
			@Override public void onSensorAdded(AbstractSensor sensor) { onSensorAdded.raise(LiveSensorManager.this, sensor); } 
			@Override public void onSensorChanged(AbstractSensor sensor) { onSensorChanged.raise(LiveSensorManager.this, sensor); }
			@Override public void onSensorRemoved(AbstractSensor sensor) { onSensorRemoved.raise(LiveSensorManager.this, sensor); }
		};				
	}
	
	public void connectToInstance(final String instanceName, final String ethernetAddr, final String userName, final String password)
	{
		Thread connectThread = new Thread()
		{
			@Override public void run()
			{
				try 
				{
					LiveInstance instance = new LiveInstance(instanceName, ethernetAddr, userName, password);
					instance.addListener(boxListener);
					
					getInstances().add(instance);
					onInstanceAdded.raise(LiveSensorManager.this, instance);
					for (RemoteSensor remoteSensor : instance.getClient().getSensors())
					{
						AbstractSensor sensor = SensorFactory.getInstance().getSensorType(remoteSensor.getSensorModel().type).createSensor(remoteSensor);
						instance.getSensors().add(sensor);						
						onSensorAdded.raise(LiveSensorManager.this, sensor);
					}
					onInstanceChanged.raise(LiveSensorManager.this, instance);
				} 
				catch (Exception e) 
				{
					onConnectFailed.raise(LiveSensorManager.this, new ConnectData(ethernetAddr, e));					
				}					
			}
		};
		connectThread.start();		
	}
}
