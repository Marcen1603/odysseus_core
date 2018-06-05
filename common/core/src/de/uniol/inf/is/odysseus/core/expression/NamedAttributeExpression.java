package de.uniol.inf.is.odysseus.core.expression;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.INamedAttributeStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.NamedAttributePredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class NamedAttributeExpression<T extends INamedAttributeStreamObject<M>,M extends IMetaAttribute> extends NamedAttributePredicate<INamedAttributeStreamObject<?>> implements IExpression<T, M> {

	private static final long serialVersionUID = -1581090263626622691L;

	public NamedAttributeExpression(SDFExpression expression, List<SDFSchema> inputSchema) {
		super(expression, inputSchema);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object evaluate(T object, List<ISession> sessions, List<T> history) {
		return super.evaluate_internal(object, sessions, (List<INamedAttributeStreamObject<?>>) history);
	}



}
