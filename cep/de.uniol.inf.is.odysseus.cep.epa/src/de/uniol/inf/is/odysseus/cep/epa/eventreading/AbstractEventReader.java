package de.uniol.inf.is.odysseus.cep.epa.eventreading;

public abstract class AbstractEventReader<R,W> {

	public AbstractEventReader() {
		
	}
	
	abstract public W getValue(String identifier, R event); 
}
