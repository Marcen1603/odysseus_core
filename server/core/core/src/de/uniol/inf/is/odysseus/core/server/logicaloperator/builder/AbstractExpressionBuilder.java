package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import de.uniol.inf.is.odysseus.core.expression.IExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;

public abstract class AbstractExpressionBuilder implements IExpressionBuilder {

	@Override
	public IExpression createExpression(IAttributeResolver resolver, String expression) {
		// FIXME: Implement method
		throw new UnsupportedOperationException();
	}


}
