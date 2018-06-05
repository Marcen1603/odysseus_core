package de.uniol.inf.is.odysseus.sensormanagement.common.utilities;

public abstract class SimpleCallbackListener<Base, Type> implements CallbackListener<Base, Type>
{
	@Override public void listeningStarted(Base sender) {}
	@Override public void listeningStopped(Base sender) {}
}
