package de.uniol.inf.is.odysseus.iql.qdl.types.operator;

import java.util.Map;


public interface Operator {

	public String getName();
	
	Map<String, Object> getParameters();

	void setParameter(String name, Object parameter);

	Object getParameter(String name);
	
}
