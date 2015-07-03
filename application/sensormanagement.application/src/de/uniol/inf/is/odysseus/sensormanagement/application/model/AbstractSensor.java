package de.uniol.inf.is.odysseus.sensormanagement.application.model;

import java.io.IOException;

import de.uniol.inf.is.odysseus.sensormanagement.application.SensorFactory;
import de.uniol.inf.is.odysseus.sensormanagement.application.SensorFactory.SensorFactoryEntry;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.Callback;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.CallbackListener;

public abstract class AbstractSensor 
{
	private boolean running = false;
	final private SensorModel sensorModel;
	private SensorFactoryEntry sensorEntry;
	
	final public Callback<AbstractSensor, Event> sensorDataReceived;
	
	public boolean isRunning() { return running; }
	public SensorModel getSensorModel() { return sensorModel;  }
	public SensorFactoryEntry getSensorEntry() { return sensorEntry; }

	public AbstractSensor(SensorModel sensorModel)
	{
		assert(sensorModel != null);
		this.sensorModel = sensorModel;
		
		sensorEntry = SensorFactory.getInstance().getSensorType(sensorModel.type);		
		sensorDataReceived = new Callback<AbstractSensor, Event>()
		{
			@Override public boolean addListener(CallbackListener<AbstractSensor, Event> listener) 	
			{ 
				if (getNumListeners() == 0)
					try {
						start();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				
				if (!super.addListener(listener)) return false;

				listener.listeningStarted(AbstractSensor.this);			
				System.out.println("Receiver " + AbstractSensor.this.toString() + " has " + getNumListeners() + " listeners.");
				return true;
			}

			@Override public boolean removeListener(CallbackListener<AbstractSensor, Event> listener)
			{
				if (!super.removeListener(listener)) return false;
				
				listener.listeningStopped(AbstractSensor.this);
				System.out.println("Receiver " + AbstractSensor.this.toString() + " has " + getNumListeners() + " listeners.");
					
				if (getNumListeners() == 0)
					stop();
				
				return true;
			}	
			
		};
	}		
	
	final public void start() throws IOException
	{
		if (running) return;
		
		running = true;
		onStart();
	}
	
	final public void stop()
	{
		if (!running) return;
		
		running = false;
		onStop();
	}
		
	protected abstract void onStart() throws IOException;
	protected abstract void onStop();
}
