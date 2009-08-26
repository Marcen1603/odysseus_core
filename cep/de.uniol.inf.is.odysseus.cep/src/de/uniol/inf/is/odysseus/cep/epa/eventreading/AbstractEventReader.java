package de.uniol.inf.is.odysseus.cep.epa.eventreading;

public abstract class AbstractEventReader {

	public AbstractEventReader() {
		
	}
	
	abstract public Object getValue(String identifier, Object event); 
}
