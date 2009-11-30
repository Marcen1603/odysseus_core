package de.uniol.inf.is.odysseus.cep.metamodel;

import java.util.Set;

import de.uniol.inf.is.odysseus.cep.metamodel.exception.UndefinedExpressionLabelException;

public interface IOutputSchemeEntry {
	public void setLabel(String label) throws UndefinedExpressionLabelException;
	public String getLabel();
	public String toString(String indent);
	public Set<String> getVarNames();
	public void setValue(String varName, Object newValue);
	public Object getValueAsObject();
}
