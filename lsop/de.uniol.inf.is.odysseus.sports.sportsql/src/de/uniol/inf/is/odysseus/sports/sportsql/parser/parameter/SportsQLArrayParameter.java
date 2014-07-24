package de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter;

public class SportsQLArrayParameter implements ISportsQLParameter{	

	private Object[] value;
	
	public SportsQLArrayParameter(Object[] value) {
		this.value = value;
	}
	
	public SportsQLArrayParameter() {
	}

	public Object[] getValue() {
		return value;
	}

	public void setValue(Object[] value) {
		this.value = value;
	}
	
	
	
}
