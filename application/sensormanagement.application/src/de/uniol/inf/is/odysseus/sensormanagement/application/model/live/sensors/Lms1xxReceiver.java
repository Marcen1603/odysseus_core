package de.uniol.inf.is.odysseus.sensormanagement.application.model.live.sensors;

import de.uniol.inf.is.odysseus.sensormanagement.application.Application;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.Event;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.live.UdpLiveReceiver;
import de.uniol.inf.is.odysseus.sensormanagement.client.executor.RemoteSensor;
import de.uniol.inf.is.odysseus.wrapper.lms1xx.LMS1xxConstants;
import de.uniol.inf.is.odysseus.wrapper.lms1xx.model.Measurement;
import de.uniol.inf.is.odysseus.wrapper.lms1xx.physicaloperator.access.LMS1xxProtocolHandler;

public class Lms1xxReceiver extends UdpLiveReceiver
{	
	public Lms1xxReceiver(RemoteSensor sensor)
	{
		super(sensor, 20480);
	}
	
	public void receiveData(String message) 
	{
/*		System.out.println("Recv " + message.length());
		if (1==1)
			return;*/
		
		if (message == null || message.length() < 3) return;
		
		int lastIndex = message.indexOf(LMS1xxConstants.ETX);
		if (lastIndex == -1)
			lastIndex = message.length()-1;
		
		message = (String) message.substring(1, lastIndex);
			
    	Measurement measurement = LMS1xxProtocolHandler.parseLMS1xxScanData(message.split(" "));    		
    		
   		double eventSecondsValid = 0; // TODO: 0, funktioniert sonst nicht. ??? // 80ms gültig, da alle 40ms ein neues Event kommt
    		
   		Event e = new Event(getSensorModel(), measurement, measurement.getTimeStamp() / 1000.0, eventSecondsValid);
		sensorDataReceived.raise(this, e);
    		
   		if (Application.getMainFrame().getCurrentSession().getMap() != null)
   			Application.getMainFrame().getCurrentSession().getMap().sensorDataReceived(getSensorModel(), e);
    		
//    		String time = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS").format(calendar.getTime());
//    		System.out.println("Measurement at " + time);    		
	}
}
