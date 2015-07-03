package de.uniol.inf.is.odysseus.sensormanagement.common.utilities;

import java.util.Vector;

public class Callback<Base, Type>
{
	private Vector<CallbackListener<Base, Type>> listeners = new Vector<>();
	
	public boolean addListener(CallbackListener<Base, Type> listener)
	{
		if (listener == null || listeners.contains(listener)) return false;

		listeners.add(listener);
		return true;
	}
	
	public boolean removeListener(CallbackListener<Base, Type> listener)
	{
		if (listener == null || !listeners.contains(listener)) return false;
		
		listeners.remove(listener);
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