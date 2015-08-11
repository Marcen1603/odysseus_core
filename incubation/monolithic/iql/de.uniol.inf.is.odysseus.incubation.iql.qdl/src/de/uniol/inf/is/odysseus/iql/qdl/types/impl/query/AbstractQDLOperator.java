package de.uniol.inf.is.odysseus.iql.qdl.types.impl.query;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.iql.qdl.service.QDLServiceBinding;
import de.uniol.inf.is.odysseus.iql.qdl.types.operator.IQDLOperator;

public abstract class AbstractQDLOperator implements IQDLOperator {
	private ILogicalOperator operator;
	private Map<String, Object> parameters = new HashMap<String, Object>();
	
	public AbstractQDLOperator(ILogicalOperator operator) {
		this.operator = operator;
	}
	
	public AbstractQDLOperator(String operator) {
		this.operator = QDLServiceBinding.createOperator(operator);
	}
	
	public AbstractQDLOperator(String operator, IQDLOperator source) {
		this.operator = QDLServiceBinding.createOperator(operator);
		source.subscribeSink(this);		
	}
	
	public AbstractQDLOperator(String operator, IQDLOperator source1, IQDLOperator source2) {
		this.operator = QDLServiceBinding.createOperator(operator);
		source1.subscribeSink(this);	
		source2.subscribeSink(this);
	}
	
	@Override
	public ILogicalOperator getLogicalOperator() {
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
	public void subscribeSink(IQDLOperator sink, int sinkInPort,int sourceOutPort) {
		operator.subscribeSink(sink.getLogicalOperator(), sinkInPort, sourceOutPort, operator.getOutputSchema(sourceOutPort));
	}

	@Override
	public void subscribeToSource(IQDLOperator source, int sinkInPort, int sourceOutPort) {
		operator.subscribeToSource(source.getLogicalOperator(), sinkInPort, sourceOutPort, source.getLogicalOperator().getOutputSchema(sourceOutPort));
	}
	
	@Override
	public void subscribeSink(IQDLOperator sink, int sourceOutPort) {
		int sinkInPort = sink.getLogicalOperator().getSubscribedToSource().size();
		operator.subscribeSink(sink.getLogicalOperator(), sinkInPort, sourceOutPort, operator.getOutputSchema(sourceOutPort));
	}

	@Override
	public void subscribeToSource(IQDLOperator source, int sourceOutPort) {
		int sinkInPort = this.getLogicalOperator().getSubscribedToSource().size();
		operator.subscribeToSource(source.getLogicalOperator(), sinkInPort, sourceOutPort, source.getLogicalOperator().getOutputSchema(sourceOutPort));
	}
	
	@Override
	public void subscribeSink(IQDLOperator sink) {
		int sinkInPort= sink.getLogicalOperator().getSubscribedToSource().size();
		int sourceOutPort = 0;
		operator.subscribeSink(sink.getLogicalOperator(), sinkInPort, sourceOutPort, operator.getOutputSchema(sourceOutPort));
	}

	@Override
	public void subscribeToSource(IQDLOperator source) {
		int sinkInPort = this.getLogicalOperator().getSubscribedToSource().size();
		int sourceOutPort = 0;
		operator.subscribeToSource(source.getLogicalOperator(), sinkInPort, sourceOutPort, source.getLogicalOperator().getOutputSchema(sourceOutPort));
	}
	
}
