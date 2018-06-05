package de.uniol.inf.is.odysseus.iql.qdl.types.impl;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.iql.qdl.types.operator.SourceOperator;

public class QDLSourceOperatorImpl extends AbstractSubscribable implements SourceOperator{

	private Map<String, Object> parameters = new HashMap<String, Object>();

	private String name;
	
	public QDLSourceOperatorImpl(String name) {		
		this.name = name;
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
