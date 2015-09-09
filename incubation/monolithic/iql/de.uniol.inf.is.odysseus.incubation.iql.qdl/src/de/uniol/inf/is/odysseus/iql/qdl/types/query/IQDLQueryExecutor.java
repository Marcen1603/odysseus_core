package de.uniol.inf.is.odysseus.iql.qdl.types.query;

import java.util.List;

import de.uniol.inf.is.odysseus.iql.qdl.types.operator.IQDLOperator;

public interface IQDLQueryExecutor {
	
	public void create(IQDLOperator operator);	
	public void createWithMultipleSinks(List<IQDLOperator> operators);
	public void start(IQDLOperator operator);
	public void startWithMultipleSinks(List<IQDLOperator> operators);
	
	public void create(String name, IQDLOperator operator);	
	public void createWithMultipleSinks(String name,List<IQDLOperator> operators);
	public void start(String name,IQDLOperator operator);
	public void startWithMultipleSinks(String name,List<IQDLOperator> operators);

}
