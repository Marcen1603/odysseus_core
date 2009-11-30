package de.uniol.inf.is.odysseus.cep.metamodel;

import java.util.Set;

public interface IExpression {
	public Set<String> getVarNames();
	public double getValue();
	public Object getValueAsObject();
	public String getErrorInfo();
	public void setValue(String varName, Object newValue);
}
