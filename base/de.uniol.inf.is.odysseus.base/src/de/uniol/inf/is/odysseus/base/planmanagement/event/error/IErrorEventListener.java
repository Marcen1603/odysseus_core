package de.uniol.inf.is.odysseus.base.planmanagement.event.error;

public interface IErrorEventListener {
	public void sendErrorEvent(ErrorEvent eventArgs);
}
