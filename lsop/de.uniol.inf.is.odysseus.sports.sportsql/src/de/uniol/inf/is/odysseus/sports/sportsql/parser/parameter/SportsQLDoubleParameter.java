package de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter;

public class SportsQLDoubleParameter implements ISportsQLParameter{	

	private double value;
	
	public SportsQLDoubleParameter(double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	
	
}
