package de.uniol.inf.is.odysseus.sensormanagement.application.model;

import java.io.IOException;

import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.Callback;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.CallbackListener;

public abstract class Receiver 
{	
	final private SensorModel sensorModel;
	
	final public Callback<Receiver, Event> sensorDataReceived;
	
	private boolean running = false;	
	
	public boolean isRunning() { return running; }
	public SensorModel getSensorModel() { return sensorModel;  }

	public Receiver(SensorModel sensorModel)
	{
		this.sensorModel = sensorModel;
		
		sensorDataReceived = new Callback<Receiver, Event>()
		{
			@Override public boolean addListener(CallbackListener<Receiver, Event> listener) 	
			{ 
				if (getNumListeners() == 0)
					try {
						start();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				
				if (!super.addListener(listener)) return false;

				listener.listeningStarted(Receiver.this);			
				System.out.println("Receiver " + Receiver.this.toString() + " has " + getNumListeners() + " listeners.");
				return true;
			}

			@Override public boolean removeListener(CallbackListener<Receiver, Event> listener)
			{
				if (!super.removeListener(listener)) return false;
				
				listener.listeningStopped(Receiver.this);
				System.out.println("Receiver " + Receiver.this.toString() + " has " + getNumListeners() + " listeners.");
					
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
