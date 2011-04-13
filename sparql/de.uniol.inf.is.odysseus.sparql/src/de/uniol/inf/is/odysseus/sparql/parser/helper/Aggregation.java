package de.uniol.inf.is.odysseus.sparql.parser.helper;

public class Aggregation {

	private Variable v;
	private AggregateFunctionName func;
	
	public Aggregation(Variable var, AggregateFunctionName func){
		this.v = var;
		this.func = func;
	}
		
	public AggregateFunctionName getAggFunc(){
		return this.func;
	}

	public Variable getVariable() {
		return v;
	}
}
