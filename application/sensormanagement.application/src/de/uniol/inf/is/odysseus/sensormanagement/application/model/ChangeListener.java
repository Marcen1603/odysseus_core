package de.uniol.inf.is.odysseus.sensormanagement.application.model;

public interface ChangeListener 
{
	public void onInstanceChanged(AbstractInstance sender);
	
	public void onSensorAdded(AbstractSensor sender); 
	public void onSensorChanged(AbstractSensor sender);
	public void onSensorRemoved(AbstractSensor sender);
}
