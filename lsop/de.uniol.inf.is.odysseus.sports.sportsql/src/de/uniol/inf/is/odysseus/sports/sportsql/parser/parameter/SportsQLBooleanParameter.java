package de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter;

public class SportsQLBooleanParameter implements ISportsQLParameter{	

	private boolean value;
	
	public SportsQLBooleanParameter(boolean value) {
		this.value = value;
	}
	
	public SportsQLBooleanParameter() {

	}

	public boolean getValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}
	
	
	
}
