package de.uniol.inf.is.odysseus.mep;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import de.uniol.inf.is.odysseus.core.mep.IMepConstant;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.core.mep.IMepVariable;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.NotOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.OrOperator;

public abstract class AbstractExpression<T> implements IMepExpression<T> {

	private static final long serialVersionUID = 9124933241292239141L;

	@Override
	public IMepFunction<?> and(IMepExpression<?> expression) {
		AndOperator and = new AndOperator();
		and.setArguments(new IMepExpression<?>[] { this, expression });
		return and;
	}

	@Override
	public IMepFunction<?> or(IMepExpression<?> expression) {
		OrOperator or = new OrOperator();
		or.setArguments(new IMepExpression<?>[] { this, expression });
		return or;
	}

	@Override
	public IMepFunction<?> not() {
		NotOperator not = new NotOperator();
		not.setArguments(new IMepExpression<?>[] { this});
		return not;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<IMepExpression<T>> conjunctiveSplit() {
		List<IMepExpression<T>> result = new ArrayList<>();
		// Split with and
		final Stack<IMepExpression<T>> expressionStack = new Stack<>();
		expressionStack.push(this);
		while (!expressionStack.isEmpty()) {
			final IMepExpression<T> curExpression = expressionStack.pop();
			if (curExpression.isFunction() && ((IMepFunction<?>)curExpression).isAndPredicate()) {
				expressionStack.push((IMepExpression<T>) curExpression.toFunction().getArgument(0));
				expressionStack.push((IMepExpression<T>) curExpression.toFunction().getArgument(1));
			} else {
				result.add(curExpression);
			}
		}
		return result;
	}
	
	
	@Override
	public IMepVariable toVariable() {
		throw new RuntimeException("cannot convert to Variable");
	}

	@Override
	public IMepFunction<T> toFunction() {
		throw new RuntimeException("cannot convert to Function");
	}
	
	@Override
	public IMepConstant<T> toConstant() {
		throw new RuntimeException("cannot convert to Constant");
	}
	
	@Override
	public boolean isFunction() {
		return false;
	}

	@Override
	public boolean isConstant() {
		return false;
	}
	
	@Override
	public boolean isVariable() {
		return false;
	}
	
	@Override
	public SDFDatatype determineType(IMepExpression<?>[] args) {
		throw new RuntimeException("cannot determine type");
	}
	
	@Override
	public boolean determineTypeFromInput() {
		return false;
	}
	
	@Override
	public Collection<SDFConstraint> getConstraintsToAdd() {
		// In the default case, no constraints are added
		return new ArrayList<SDFConstraint>(0);
	}

}
