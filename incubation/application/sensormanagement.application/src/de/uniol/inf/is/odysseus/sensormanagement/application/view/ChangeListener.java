package de.uniol.inf.is.odysseus.sensormanagement.application.view;

import de.uniol.inf.is.odysseus.sensormanagement.application.view.live.SensorBox;
import de.uniol.inf.is.odysseus.sensormanagement.client.executor.RemoteSensor;

public interface ChangeListener 
{
	public void onBoxChanged(SensorBox sender);
	
	public void onSensorAdded(RemoteSensor sender); 
	public void onSensorChanged(RemoteSensor sender);
	public void onSensorRemoved(RemoteSensor sender);
}
