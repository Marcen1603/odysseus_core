package de.uniol.inf.is.odysseus.cep.epa.eventreading;

public interface IEventReader<R, W> {
	public W getValue(String identifier, R event);
	public String getType();
}
