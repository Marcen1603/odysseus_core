package de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter;


public class SportsQLStringParameter implements ISportsQLParameter {
	
	private String value;

	public SportsQLStringParameter(String value) {
		this.value = value;
	}
	
	public SportsQLStringParameter() {

	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
