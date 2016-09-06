package de.uniol.inf.is.odysseus.iql.basic.types;

public class ID {
	private final String id;
	
	public ID(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
	
	@Override
	public String toString() {
		return id;
	}
	
	
}
