package de.uniol.inf.is.odysseus.sensormanagement.application.view;

import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;

public interface ReceiverListener 
{
	public void sensorDataReceived(SensorModel source, Event event);
	public void listeningStarted(Receiver receiver);
	public void listeningStopped(Receiver receiver);
}
