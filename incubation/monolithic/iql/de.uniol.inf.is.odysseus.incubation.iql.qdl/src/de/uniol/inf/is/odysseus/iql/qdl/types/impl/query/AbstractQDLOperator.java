package de.uniol.inf.is.odysseus.iql.qdl.types.impl.query;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.iql.qdl.types.operator.IQDLOperator;

public abstract class AbstractQDLOperator<T extends ILogicalOperator> implements IQDLOperator<T> {
	private T operator;
	private Map<String, Object> parameters = new HashMap<String, Object>();
	
	public AbstractQDLOperator(T operator) {
		this.operator = operator;
	}
	
	public AbstractQDLOperator(T operator, IQDLOperator<?> source) {
		this.operator = operator;
		source.subscribeSink(this);		
	}
	
	public AbstractQDLOperator(T operator, IQDLOperator<?> source1, IQDLOperator<?> source2) {
		this.operator = operator;
		source1.subscribeSink(this);	
		source2.subscribeSink(this);
	}
	
	@Override
	public T getLogicalOperator() {
		return operator;
	}
	
	public void setParameter(String name, Object parameter) {
		parameters.put(name.toLowerCase(),  parameter);
	}
	
	public Object getParameter(String name) {
		return parameters.get(name.toLowerCase());
	}
	
	@Override
	public Map<String, Object> getParameters() {
		return parameters;
	}
	
	@Override
	public void subscribeSink(IQDLOperator<?> sink, int sinkInPort,int sourceOutPort) {
		operator.subscribeSink(sink.getLogicalOperator(), sinkInPort, sourceOutPort, operator.getOutputSchema(sourceOutPort));
	}

	@Override
	public void subscribeToSource(IQDLOperator<?> source, int sinkInPort, int sourceOutPort) {
		operator.subscribeToSource(source.getLogicalOperator(), sinkInPort, sourceOutPort, source.getLogicalOperator().getOutputSchema(sourceOutPort));
	}
	
	@Override
	public void subscribeSink(IQDLOperator<?> sink, int sourceOutPort) {
		int sinkInPort = sink.getLogicalOperator().getSubscribedToSource().size();
		operator.subscribeSink(sink.getLogicalOperator(), sinkInPort, sourceOutPort, operator.getOutputSchema(sourceOutPort));
	}

	@Override
	public void subscribeToSource(IQDLOperator<?> source, int sourceOutPort) {
		int sinkInPort = this.getLogicalOperator().getSubscribedToSource().size();
		operator.subscribeToSource(source.getLogicalOperator(), sinkInPort, sourceOutPort, source.getLogicalOperator().getOutputSchema(sourceOutPort));
	}
	
	@Override
	public void subscribeSink(IQDLOperator<?> sink) {
		int sinkInPort= sink.getLogicalOperator().getSubscribedToSource().size();
		int sourceOutPort = 0;
		operator.subscribeSink(sink.getLogicalOperator(), sinkInPort, sourceOutPort, operator.getOutputSchema(sourceOutPort));
	}

	@Override
	public void subscribeToSource(IQDLOperator<?> source) {
		int sinkInPort = this.getLogicalOperator().getSubscribedToSource().size();
		int sourceOutPort = 0;
		operator.subscribeToSource(source.getLogicalOperator(), sinkInPort, sourceOutPort, source.getLogicalOperator().getOutputSchema(sourceOutPort));
	}
	
}
