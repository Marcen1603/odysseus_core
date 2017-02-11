package de.uniol.inf.is.odysseus.server.keyvalue.predicate;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.NotOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.OrOperator;

public class KeyValuePredicate<T extends KeyValueObject<?>> extends AbstractPredicate<T> {

	private static final long serialVersionUID = 6578575151834596318L;

	final private SDFExpression expression;
	final private List<SDFAttribute> neededAttributes;
	final private List<SDFSchema> inputSchema;

	public KeyValuePredicate(SDFExpression expression, List<SDFSchema> inputSchema) {
		this.expression = expression;
		this.neededAttributes = expression.getAllAttributes();
		if (inputSchema == null){
			this.inputSchema = new ArrayList<>();
		}else{
			this.inputSchema = new ArrayList<>(inputSchema);
		}
	}

	public KeyValuePredicate(KeyValuePredicate<T> predicate) {
		this.expression = predicate.expression == null ? null : predicate.expression.clone();
		this.neededAttributes = expression.getAllAttributes();
		this.inputSchema = predicate.inputSchema;
	}

	@Override
	public Boolean evaluate(T input) {
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
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

	@Override
	public Boolean evaluate(T left, T right) {
		Object[] values = new Object[neededAttributes.size()];
		for (int i = 0; i < values.length; ++i) {
			values[i] = left.getAttribute(neededAttributes.get(i).getURI());
			if (values[i] == null) {
				values[i] = right.getAttribute(neededAttributes.get(i).getURI());
			}
		}

		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
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
			and.setArguments(new IExpression<?>[] { expression.getMEPExpression(), expr.getMEPExpression() });
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
			or.setArguments(new IExpression<?>[] { expression.getMEPExpression(), expr.getMEPExpression() });
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
		not.setArguments(new IExpression<?>[] { expression.getMEPExpression() });
		return new KeyValuePredicate<>(
				new SDFExpression(not.toString(), expression.getAttributeResolver(), expression.getExpressionParser()), inputSchema);
	}
}
