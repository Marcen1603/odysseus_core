package de.uniol.inf.is.odysseus.sensormanagement.application.view.sensors;

import de.uniol.inf.is.odysseus.sensormanagement.application.Application;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Event;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.ReceiverListener;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.live.UdpLiveReceiver;
import de.uniol.inf.is.odysseus.sensormanagement.client.executor.RemoteSensor;

public class GPSReceiver extends UdpLiveReceiver
{	
	public GPSReceiver(RemoteSensor sensor)
	{
		super(sensor, 2048);
	}
	
	public void receiveData(String message) 
	{
		if (message.length() == 0) return;
		
   		double eventSecondsValid = 0; // TODO: 0, funktioniert sonst nicht. ??? // 80ms gültig, da alle 40ms ein neues Event kommt
   		double timestamp;
   		
   		try
   		{
   			timestamp = Long.parseLong(message.split(",")[0]) / 1000.0;
   		}
   		catch (Exception e)
   		{
   			return;
   		}
   		
   		Event e = new Event(message, timestamp, eventSecondsValid);
   		for (ReceiverListener listener : listenerList)
   		{
   			listener.sensorDataReceived(getSensorModel(), e);
   		}
    		
   		Application.getMainFrame().getCurrentSession().getMap().sensorDataReceived(getSensorModel(), e);
	}
}
