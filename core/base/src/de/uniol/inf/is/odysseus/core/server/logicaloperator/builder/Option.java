package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

public class Option {
	
	String name;
	String value;
	
	public Option(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}

}
