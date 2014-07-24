package de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter;

public class SportsQLIntegerParameter implements ISportsQLParameter{	

	private int value;
	
	public SportsQLIntegerParameter(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	
	
}
