package de.uniol.inf.is.odysseus.cep.metamodel;

import de.uniol.inf.is.odysseus.cep.metamodel.exception.UndefinedExpressionLabelException;

public interface IOutputSchemeEntry extends IExpression{
	public void setLabel(String label) throws UndefinedExpressionLabelException;
	public String getLabel();
	public String toString(String indent);
}
