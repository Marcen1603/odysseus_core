package de.uniol.inf.is.odysseus.sensormanagement.application.view;

import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel2;

public interface ReceiverListener 
{
	public void sensorDataReceived(SensorModel2 source, Event event);
	public void listeningStarted(Receiver receiver);
	public void listeningStopped(Receiver receiver);
}
