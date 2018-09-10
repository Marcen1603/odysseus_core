package de.uniol.inf.is.odysseus.sensormanagement.common.utilities;

import java.util.Vector;

public class Callback<Base, Type>
{
	private Base owner;
	private Vector<CallbackListener<Base, Type>> listeners = new Vector<>();
	
	public Callback(Base owner)
	{
		this.owner = owner;
	}
	
	public boolean addListener(CallbackListener<Base, Type> listener)
	{
		if (listener == null || listeners.contains(listener)) return false;

		listeners.add(listener);
		listener.listeningStarted(owner);
		return true;
	}
	
	public boolean removeListener(CallbackListener<Base, Type> listener)
	{
		if (listener == null || !listeners.contains(listener)) return false;
		
		listeners.remove(listener);
		listener.listeningStopped(owner);
		return true;
	}
	
	public void raise(Base sender, Type event)
	{
		for (CallbackListener<Base, Type> l : listeners)
		{
			l.raise(sender, event);
		}
	}

	public int getNumListeners() 
	{
		return listeners.size();
	}
}