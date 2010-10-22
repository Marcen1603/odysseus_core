package de.uniol.inf.is.odysseus.cep.metamodel;

import java.util.Set;

public interface ICepExpression<T> {
	public Set<CepVariable> getVarNames();
	public T getValue();
	public void setValue(CepVariable varName, Object newValue);
}
