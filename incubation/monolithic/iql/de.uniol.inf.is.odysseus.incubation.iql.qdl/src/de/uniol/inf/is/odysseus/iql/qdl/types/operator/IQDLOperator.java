package de.uniol.inf.is.odysseus.iql.qdl.types.operator;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public interface IQDLOperator<T extends ILogicalOperator> {

	public T getLogicalOperator();
	
	public void subscribeSink(IQDLOperator<?> sink, int sinkInPort, int sourceOutPort);
	public void subscribeToSource(IQDLOperator<?> source, int sinkInPort, int sourceOutPort);
	
	public void subscribeSink(IQDLOperator<?> sink, int sourceOutPort);
	public void subscribeToSource(IQDLOperator<?> source, int sourceOutPort);
	
	public void subscribeSink(IQDLOperator<?> sink);
	public void subscribeToSource(IQDLOperator<?> source);

	Map<String, Object> getParameters();

}
