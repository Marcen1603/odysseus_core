package de.uniol.inf.is.odysseus.sparql.parser.helper;

public class Aggregation {

	private Variable var;
	private AggregateFunction func;
	
	public Aggregation(Variable v, AggregateFunction func){
		this.var = v;
		this.func = func;
	}
	
	public Variable getVar(){
		return this.var;
	}
	
	public AggregateFunction getAggFunc(){
		return this.func;
	}
}
