package de.uniol.inf.is.odysseus.sensormanagement.application.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;

public abstract class Receiver 
{	
	final private SensorModel sensorModel;
	
	private boolean running = false;	
	protected List<ReceiverListener> listenerList = new ArrayList<ReceiverListener>();		
	
	public boolean 		isRunning() { return running; }
	public SensorModel	getSensorModel() { return sensorModel;  }

	public Receiver(SensorModel sensorModel)
	{
		this.sensorModel = sensorModel;
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
	
	public void addListener(ReceiverListener listener) throws IOException 	
	{ 
		if (listenerList.size() == 0)
			start();
		
		if (!listenerList.contains(listener))
		{
			listenerList.add(listener);
			listener.listeningStarted(this);
			
			System.out.println("Receiver " + this.toString() + " has " + listenerList.size() + " listeners.");
		}
	}

	public void removeListener(ReceiverListener listener)
	{
		if (listenerList.contains(listener))
		{
			listenerList.remove(listener);
			listener.listeningStopped(this);
		
			System.out.println("Receiver " + this.toString() + " has " + listenerList.size() + " listeners.");
		}
		
		if (listenerList.size() == 0)
			stop();
	}	
	
	protected abstract void onStart() throws IOException;
	protected abstract void onStop();
}
