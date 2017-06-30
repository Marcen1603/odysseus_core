package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import de.uniol.inf.is.odysseus.core.expression.IExpression;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;

public abstract class AbstractExpressionBuilder<T extends IStreamObject<M>, M extends IMetaAttribute>
		implements IExpressionBuilder<T, M> {

	@Override
	public IExpression<T, M> createExpression(IAttributeResolver resolver, String expression) {
		throw new UnsupportedOperationException();
	}

}
