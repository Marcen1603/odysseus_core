package de.uniol.inf.is.odysseus.mep;

import de.uniol.inf.is.odysseus.mep.functions.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.OrOperator;

public class ExpressionOptimizer {

	public static IExpression<?> simplifyExpression(IExpression<?> expression) {
		PreCalculateConstants simplificator = new PreCalculateConstants();
		return (IExpression<?>) expression.acceptVisitor(simplificator, null);
	}

	/**
	 * pre calculate constant expressions
	 */
	private static class PreCalculateConstants implements IExpressionVisitor {

		@Override
		public Variable visit(Variable variable, Object data) {
			return variable;
		}

		@Override
		public Constant<?> visit(Constant<?> constant, Object data) {
			return constant;
		}

		@Override
		public IExpression<?> visit(IFunction<?> function, Object data) {
			boolean isAllInputsConstant = true;
			for (int i = 0; i < function.getArity(); ++i) {
				IExpression<?> iExpression = function.getArguments()[i];
				function.getArguments()[i] = (IExpression<?>) iExpression
						.acceptVisitor(this, data);
				if (!(function.getArguments()[i] instanceof Constant)) {
					isAllInputsConstant = false;
				}
			}

			if (isAllInputsConstant && !function.isContextDependent()) {
				return new Constant<Object>(function.getValue());
			}

			if (function.getClass() == AndOperator.class) {
				return isConstantPredicate(function.getArguments(), false) ? new Constant<Boolean>(
						false) : function;
			} else {
				if (function.getClass() == OrOperator.class) {
					return isConstantPredicate(function.getArguments(), true) ? new Constant<Boolean>(
							true) : function;
				} else {
					return function;
				}
			}
		}

		private boolean isConstantPredicate(IExpression<?>[] iExpression,
				Boolean value) {
			return iExpression[0] instanceof Constant
					&& iExpression[0].getValue().equals(value)
					|| iExpression[1] instanceof Constant
					&& iExpression[1].getValue().equals(value);
		}

	}

}
