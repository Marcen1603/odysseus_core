package de.uniol.inf.is.odysseus.iql.qdl.types.query;

import java.util.List;

import de.uniol.inf.is.odysseus.iql.qdl.types.operator.Operator;

public interface IQDLQueryExecutor {
	
	public void create(Operator operator);	
	public void createWithMultipleSinks(List<Operator> operators);
	public void start(Operator operator);
	public void startWithMultipleSinks(List<Operator> operators);
	
	public void create(String name, Operator operator);	
	public void createWithMultipleSinks(String name,List<Operator> operators);
	public void start(String name,Operator operator);
	public void startWithMultipleSinks(String name,List<Operator> operators);

}
