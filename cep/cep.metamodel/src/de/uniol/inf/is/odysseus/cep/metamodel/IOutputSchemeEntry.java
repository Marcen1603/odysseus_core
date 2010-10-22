package de.uniol.inf.is.odysseus.cep.metamodel;

import de.uniol.inf.is.odysseus.cep.metamodel.exception.UndefinedExpressionLabelException;

public interface IOutputSchemeEntry extends ICepExpression<Object>{
	public void setLabel(String label) throws UndefinedExpressionLabelException;
	public String getLabel();
}
