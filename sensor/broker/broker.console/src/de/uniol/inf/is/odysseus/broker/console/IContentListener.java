package de.uniol.inf.is.odysseus.broker.console;

public interface IContentListener {
	public void newQueryEvent(int port);
	public void inputChanged(int port);
}
