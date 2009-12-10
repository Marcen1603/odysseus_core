package de.uniol.inf.is.odysseus.cep.epa.eventreading;


public abstract class AbstractEventReader<R> implements IEventReader<R,Object>{
	private String type;
	
	public AbstractEventReader(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
	final public Object getValue(String identifier, R event) {
		if (identifier.endsWith(".type")){ // TODO: Hier eine effizientere Loesung finden
			return type;
		}else{
			return getValue_internal(identifier, event);
		}		
	};
	
	abstract protected Object getValue_internal(String id, R event);
	
}
