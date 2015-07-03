package de.uniol.inf.is.odysseus.sensormanagement.application.model;

import java.util.ArrayList;
import java.util.List;

public class AbstractInstance 
{
	private List<AbstractSensor> sensors = new ArrayList<>();
	protected List<ChangeListener> boxListeners = new ArrayList<>();
	private String ethernetAddr;
	private String position;
	private String name;
	
	public AbstractInstance(String name, String ethernetAddr) 
	{
		this.name = name;
		this.ethernetAddr = ethernetAddr;
	}
	
	public String getPosition()	{ return position; }
	public String getName() { return name; }	
	public String getEthernetAddr() { return ethernetAddr; }
	
	public void setPosition(String position) { this.position = position; }	

	@Override public String toString() { return name + " @ " + getEthernetAddr(); }		
	
	public List<AbstractSensor> getSensors() { return sensors; }
	
	public void close()
	{		
	}
	
	protected void instanceChanged() 
	{
		for (ChangeListener l : boxListeners)
			l.onInstanceChanged(AbstractInstance.this);
	}
	
	public void addListener(ChangeListener listener) 
	{
		boxListeners.add(listener);
	}	
	
	public void setName(String name) 					
	{ 
		this.name = name;
		instanceChanged();
	}	
	
	public AbstractSensor getSensorById(String id)
	{
		for (AbstractSensor sensor : sensors)
		{
			if (sensor.getSensorModel().id.equals(id))
				return sensor;
		}
		
		return null;
	}			
}
