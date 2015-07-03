package de.uniol.inf.is.odysseus.sensormanagement.application.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.Callback;

public class AbstractSensorManager 
{	
	private List<AbstractInstance> instances = new ArrayList<>();
	private Scene scene;

	public Callback<AbstractSensorManager, AbstractInstance> onInstanceAdded = new Callback<>();
	public Callback<AbstractSensorManager, AbstractInstance> onInstanceChanged = new Callback<>();
	public Callback<AbstractSensorManager, AbstractSensor> onSensorAdded = new Callback<>();
	public Callback<AbstractSensorManager, AbstractSensor> onSensorChanged = new Callback<>();
	public Callback<AbstractSensorManager, AbstractSensor> onSensorRemoved = new Callback<>();
	
	public List<AbstractInstance> getInstances() { return instances; }
	public Scene getScene() { return scene; }
	
	public AbstractSensorManager(Scene scene) 
	{
		this.scene = scene;
	}
	
	public void start() throws IOException
	{		
	}
	
	public void stop()
	{		
		for (AbstractInstance serverInstance : getInstances())
			serverInstance.close();		
	}
	
	public AbstractSensor getSensorById(String id)
	{
		for (AbstractInstance instance : instances)
		{
			AbstractSensor sensor = instance.getSensorById(id);
			if (sensor != null) 
				return sensor;
		}
		
		return null;
	}		
}
