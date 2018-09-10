package de.uniol.inf.is.odysseus.sensormanagement.common.utilities;

public interface CallbackListener<Base, Type>
{
	void listeningStarted(Base sender);
	void listeningStopped(Base sender);
	void raise(Base sender, Type event);
}