package de.uniol.inf.is.odysseus.server.keyvalue.predicate;

import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.predicate.NamedAttributePredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.NotOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.OrOperator;

public class KeyValuePredicate<T extends KeyValueObject<?>> extends NamedAttributePredicate<T> {

	private static final long serialVersionUID = 6578575151834596318L;

	public KeyValuePredicate(SDFExpression expression, List<SDFSchema> inputSchema) {
		super(expression, inputSchema);
	}

	public KeyValuePredicate(KeyValuePredicate<T> predicate) {
		super(predicate);
	}

	@Override
	public AbstractPredicate<T> clone() {
		return new KeyValuePredicate<T>(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPredicate<T> and(IPredicate<T> predicate) {
		if (predicate instanceof KeyValuePredicate) {
			SDFExpression expr = ((KeyValuePredicate<T>) predicate).expression;
			AndOperator and = new AndOperator();
			and.setArguments(new IMepExpression<?>[] { expression.getMEPExpression(), expr.getMEPExpression() });
			IAttributeResolver resolver = new DirectAttributeResolver(expression.getAttributeResolver().getSchema(),
					expr.getAttributeResolver().getSchema());
			return new KeyValuePredicate<>(
					new SDFExpression(and.toString(), resolver, expression.getExpressionParser()), inputSchema);
		}
		return super.and(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPredicate<T> or(IPredicate<T> predicate) {
		if (predicate instanceof KeyValuePredicate) {
			SDFExpression expr = ((KeyValuePredicate<T>) predicate).expression;
			OrOperator or = new OrOperator();
			or.setArguments(new IMepExpression<?>[] { expression.getMEPExpression(), expr.getMEPExpression() });
			IAttributeResolver resolver = new DirectAttributeResolver(expression.getAttributeResolver().getSchema(),
					expr.getAttributeResolver().getSchema());

			return new KeyValuePredicate<>(
					new SDFExpression(or.toString(), resolver, expression.getExpressionParser()), inputSchema);
		}
		return super.or(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPredicate<T> not() {
		NotOperator not = new NotOperator();
		not.setArguments(new IMepExpression<?>[] { expression.getMEPExpression() });
		return new KeyValuePredicate<>(
				new SDFExpression(not.toString(), expression.getAttributeResolver(), expression.getExpressionParser()), inputSchema);
	}
}
