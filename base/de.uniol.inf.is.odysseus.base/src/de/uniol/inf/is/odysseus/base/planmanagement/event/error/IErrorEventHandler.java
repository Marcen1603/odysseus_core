package de.uniol.inf.is.odysseus.base.planmanagement.event.error;

public interface IErrorEventHandler {
	public void addErrorEventListener(IErrorEventListener errorEventListener);

	public void removeErrorEventListener(IErrorEventListener errorEventListener);
}
