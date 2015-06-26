package de.uniol.inf.is.odysseus.sensormanagement.application.view;

import javax.swing.tree.DefaultMutableTreeNode;

import de.uniol.inf.is.odysseus.sensormanagement.client.executor.RemoteSensor;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;

public class SensorConfig 
{
	public enum State
	{
		ExistingUnchangedSensor,
		ExistingChangedSensor,
		NewSensor,
		DeletedSensor
	}
	
	private State 					state;
	private SensorModel			sensorInfo;
	private RemoteSensor 				originalSensor;
	private DefaultMutableTreeNode 	node;	
	
	public SensorConfig(RemoteSensor originalSensor, DefaultMutableTreeNode node) 
	{
		this.originalSensor = originalSensor;
		this.node = node;
		
		if (originalSensor != null)
		{
			this.sensorInfo = new SensorModel(originalSensor.getSensorModel());
			state = State.ExistingUnchangedSensor;
		}
		else
		{
			this.sensorInfo = new SensorModel();
			state = State.NewSensor;
		}
	}

	@Override
	public String toString()
	{
		return sensorInfo.displayName;
	}	
	
	public RemoteSensor getOriginalSensor() 
	{
		return originalSensor;
	}	
	
	public SensorModel getSensorInfo() 
	{
		return sensorInfo;
	}

	public void changed() 
	{
		assert(state != State.DeletedSensor);
		
		if (state == State.ExistingUnchangedSensor) 
			state = State.ExistingChangedSensor;
	}

	public DefaultMutableTreeNode getTreeNode() 
	{
		return node;
	}

	public State getState() 
	{
		return state;
	}

	public void markDeleted() 
	{
		assert(	state == State.ExistingUnchangedSensor || 
				state == State.ExistingChangedSensor);
	
		state = State.DeletedSensor;
	}

	public void applied() 
	{
		if (state != State.DeletedSensor)
			state = State.ExistingUnchangedSensor;
	}

	public RemoteSensor createSensor(SensorBox sensorBox) 
	{
		originalSensor = new RemoteSensor(sensorBox.getClient(), getSensorInfo()); 
		return originalSensor;
	}
}
