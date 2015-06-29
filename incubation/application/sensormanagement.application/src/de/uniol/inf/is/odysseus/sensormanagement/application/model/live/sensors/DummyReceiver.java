package de.uniol.inf.is.odysseus.sensormanagement.application.model.live.sensors;

import de.uniol.inf.is.odysseus.sensormanagement.application.Application;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.Event;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.live.UdpLiveReceiver;
import de.uniol.inf.is.odysseus.sensormanagement.client.executor.RemoteSensor;

public class DummyReceiver extends UdpLiveReceiver
{	
	public DummyReceiver(RemoteSensor sensor)
	{
		super(sensor, 2048);
	}
	
	public void receiveData(String message) 
	{
   		double eventSecondsValid = 0; // TODO: 0, funktioniert sonst nicht. ??? // 80ms gültig, da alle 40ms ein neues Event kommt
    		
   		if (message.endsWith("\n"))
   			message = message.substring(0, message.length()-2);
   					
   		try
   		{
	   		double timestamp = Long.parseLong(message) / 1000.0;
	   		System.out.println("Time @ " + this.getStreamUrl() + ": " + String.format("%.3f", timestamp));
	   		
	   		Event e = new Event(getSensorModel(), message, timestamp, eventSecondsValid);
	   		sensorDataReceived.raise(this, e);
	    		
	   		if (Application.getMainFrame().getCurrentSession().getMap() != null)
	   			Application.getMainFrame().getCurrentSession().getMap().sensorDataReceived(getSensorModel(), e);
   		}
   		catch (Exception e)
   		{
   			e.printStackTrace();
   		}
	}
}
