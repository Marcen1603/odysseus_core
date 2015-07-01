package de.uniol.inf.is.odysseus.sensormanagement.application.model.live.sensors;

import de.uniol.inf.is.odysseus.sensormanagement.application.Application;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.Event;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.live.UdpLiveReceiver;
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
   		
   		Event e = new Event(getSensorModel(), message, timestamp, eventSecondsValid);
   		sensorDataReceived.raise(this, e);
    		
   		if (Application.getMainFrame().getCurrentSession().getMap() != null)
   			Application.getMainFrame().getCurrentSession().getMap().sensorDataReceived(getSensorModel(), e);
	}
}
