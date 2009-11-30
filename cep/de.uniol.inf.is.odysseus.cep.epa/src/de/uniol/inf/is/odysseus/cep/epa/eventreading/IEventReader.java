package de.uniol.inf.is.odysseus.cep.epa.eventreading;

public interface IEventReader<R, W> {
	abstract public W getValue(String identifier, R event);
}
