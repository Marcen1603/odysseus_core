package de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter;

public class SportsQLLongParameter implements ISportsQLParameter{	

	private long value;
	
	public SportsQLLongParameter(long value) {
		this.value = value;
	}
	
	public SportsQLLongParameter() {

	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}
	
	
	
}
