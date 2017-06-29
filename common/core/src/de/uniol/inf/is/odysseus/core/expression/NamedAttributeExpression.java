package de.uniol.inf.is.odysseus.core.expression;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Pair;
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

	@Override
	public Object evaluate(T input, List<ISession> sessions, List<T> history) {

		// This is nearly the same as in NamedAttributePredicate ... TODO: Find a better solution

		Object[] values = new Object[neededAttributes.size()];
		for (int i = 0; i < values.length; ++i) {
			values[i] = input.getAttribute(neededAttributes.get(i).getURI());
			if (values[i] == null) {
				if (inputSchema.get(0) != null) {
					Pair<Integer, Integer> pos = inputSchema.get(0).indexOfMetaAttribute(neededAttributes.get(i).getURI());
					if (pos != null) {
						values[i] = input.getMetadata().getValue(pos.getE1(), pos.getE2());
					}
				}
			}
		}
		this.expression.setSessions(sessions);
		this.expression.bindVariables(values);
		return this.expression.getValue();

	}

}
