package de.uniol.inf.is.odysseus.core.expression;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class RelationalExpression<T extends IMetaAttribute> extends AbstractRelationalExpression<T>
		implements IPredicate<Tuple<T>> {

	private static final long serialVersionUID = 317537629492663024L;

	public RelationalExpression(SDFExpression expression) {
		super(expression);
	}

	public RelationalExpression(RelationalExpression<T> other) {
		super(other);
	}

    @Override
    public IPunctuation processPunctuation(IPunctuation punctuation) {
    	return punctuation;
    }

	@Override
	public List<RelationalExpression<T>> conjunctiveSplit() {
		List<?> splits = expression.getMEPExpression().conjunctiveSplit();
		if (splits.size() >= 0) {
			List<RelationalExpression<T>> ret = new ArrayList<>(splits.size());
			for (Object e : splits) {
				SDFExpression sdfExpression = new SDFExpression(e.toString(), getAttributeResolver(),
						getExpressionParser());
				ret.add(new RelationalExpression<T>(sdfExpression));
			}
			return ret;
		}
		return null;
	}

	@Override
	public IPredicate<Tuple<T>> and(IPredicate<Tuple<T>> predicate) {
		if (predicate instanceof AbstractRelationalExpression) {

			IMepExpression<?> expr = this.expression.getMEPExpression();
			List<SDFSchema> schemalist = new ArrayList<>();
			schemalist.addAll(((AbstractRelationalExpression<T>) predicate).getAttributeResolver().getSchema());
			schemalist.addAll((expression.getAttributeResolver().getSchema()));
			DirectAttributeResolver attributeResolver = new DirectAttributeResolver(schemalist);
			// We need to reparse the expression because of multiple
			// instances of the same variable may exist
			SDFExpression newExpression = new SDFExpression(
					expr.and(((AbstractRelationalExpression<T>) predicate).getMEPExpression()).toString(), attributeResolver,
					expression.getExpressionParser());

			return new RelationalExpression<T>(newExpression);
		}
		throw new IllegalArgumentException("Cannot process with " + predicate);

	}

	@Override
	public IPredicate<Tuple<T>> or(IPredicate<Tuple<T>> predicate) {
		if (predicate instanceof AbstractRelationalExpression) {
				IMepExpression<?> expr =  this.expression.getMEPExpression();
				List<SDFSchema> schemalist = new ArrayList<>();
				schemalist.addAll(((AbstractRelationalExpression<T>) predicate).getAttributeResolver().getSchema());
				schemalist.addAll(( expression.getAttributeResolver().getSchema()));
				DirectAttributeResolver attributeResolver = new DirectAttributeResolver(schemalist);

				// We need to reparse the expression because of multiple
				// instances of the same variable may exist
				SDFExpression newExpression = new SDFExpression(
						expr.or(((AbstractRelationalExpression<T>) predicate).getMEPExpression()).toString(), attributeResolver,
						expression.getExpressionParser());
				return new RelationalExpression<T>(newExpression);

		}
		throw new IllegalArgumentException("Cannot process with " + predicate);
	}

	@Override
	public IPredicate<Tuple<T>> not() {
			IMepExpression<?> expr = this.expression.getMEPExpression();
			// We need to reparse the expression because of multiple instances
			// of the same variable may exist

			IAttributeResolver resolver = null;
			resolver = getAttributeResolver();

			SDFExpression newExpression = new SDFExpression(expr.not().toString(), resolver,
					expression.getExpressionParser());
			return new RelationalExpression<T>(newExpression);
	}

	@Override
	public boolean equals(IPredicate<Tuple<T>> predicate) {

		return false;
	}

	@Override
	public RelationalExpression<T> clone() {
		return new RelationalExpression<>(this);
	}

}
