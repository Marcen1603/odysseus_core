package de.uniol.inf.is.odysseus.cep.epa.eventreading;


public abstract class AbstractEventReader<R,W> implements IEventReader<R,W>{
	private String type;
	
	public AbstractEventReader(String type) {
		this.type = type;
	}

	@Override
	public String getType() {
		return type;
	}
}
