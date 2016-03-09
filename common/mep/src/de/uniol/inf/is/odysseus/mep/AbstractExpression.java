package de.uniol.inf.is.odysseus.mep;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import de.uniol.inf.is.odysseus.core.mep.IConstant;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.mep.IVariable;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.NotOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.OrOperator;

public abstract class AbstractExpression<T> implements IExpression<T> {

	private static final long serialVersionUID = 9124933241292239141L;

	@Override
	public IFunction<?> and(IExpression<?> expression) {
		AndOperator and = new AndOperator();
		and.setArguments(new IExpression<?>[] { this, expression });
		return and;
	}

	@Override
	public IFunction<?> or(IExpression<?> expression) {
		OrOperator or = new OrOperator();
		or.setArguments(new IExpression<?>[] { this, expression });
		return or;
	}

	@Override
	public IFunction<?> not() {
		NotOperator not = new NotOperator();
		not.setArguments(new IExpression<?>[] { this});
		return not;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<IExpression<T>> conjunctiveSplit() {
		List<IExpression<T>> result = new ArrayList<>();
		// Split with and
		final Stack<IExpression<T>> expressionStack = new Stack<>();
		expressionStack.push(this);
		while (!expressionStack.isEmpty()) {
			final IExpression<T> curExpression = expressionStack.pop();
			if (curExpression.isFunction() && ((IFunction<?>)curExpression).isAndPredicate()) {
				expressionStack.push((IExpression<T>) curExpression.toFunction().getArgument(0));
				expressionStack.push((IExpression<T>) curExpression.toFunction().getArgument(1));
			} else {
				result.add(curExpression);
			}
		}
		return result;
	}
	
	
	@Override
	public IVariable toVariable() {
		throw new RuntimeException("cannot convert to Variable");
	}

	@Override
	public IFunction<T> toFunction() {
		throw new RuntimeException("cannot convert to Function");
	}
	
	@Override
	public IConstant<T> toConstant() {
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
	public SDFDatatype determineType(IExpression<?>[] args) {
		throw new RuntimeException("cannot determine type");
	}
	
	@Override
	public boolean determineTypeFromInput() {
		return false;
	}

}
