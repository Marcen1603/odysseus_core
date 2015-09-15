package de.uniol.inf.is.odysseus.iql.qdl.types.impl;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.iql.qdl.types.operator.SinkOperator;
import de.uniol.inf.is.odysseus.iql.qdl.types.subscription.Subscribable;

public class QDLSinkOperatorImpl extends AbstractSubscriber implements SinkOperator{

	private Map<String, Object> parameters = new HashMap<String, Object>();

	private String name;
	
	public QDLSinkOperatorImpl(String name) {		
		this.name = name;
	}
	
	public QDLSinkOperatorImpl(String name, Subscribable source) {		
		this.name = name;
		source.subscribeSink(this);
	}
	
	public QDLSinkOperatorImpl(String name, Subscribable source1, Subscribable source2) {		
		this.name = name;
		source1.subscribeSink(this);
		source2.subscribeSink(this);
	}
	
	@Override
	public void setParameter(String name, Object parameter) {
		parameters.put(name.toLowerCase(),  parameter);
	}
	
	@Override
	public Object getParameter(String name) {
		return parameters.get(name.toLowerCase());
	}
	
	@Override
	public Map<String, Object> getParameters() {
		return parameters;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

}
