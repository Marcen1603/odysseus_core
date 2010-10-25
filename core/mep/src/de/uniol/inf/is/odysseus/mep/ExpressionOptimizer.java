package de.uniol.inf.is.odysseus.mep;

import de.uniol.inf.is.odysseus.mep.functions.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.OrOperator;

public class ExpressionOptimizer {

	public static IExpression<?> simplifyExpression(IExpression<?> expression) {
		PushDownConstants pushDown = new PushDownConstants();
		PreCalculateConstants simplificator = new PreCalculateConstants();
		expression = (IExpression<?>) expression.acceptVisitor(pushDown, null);
		return (IExpression<?>) expression.acceptVisitor(simplificator, null);
	}
	
	private static class PushDownConstants implements IExpressionVisitor {

		@Override
		public Object visit(Variable variable, Object data) {
			return variable;
		}

		@Override
		public Object visit(Constant<?> constant, Object data) {
			return constant;
		}

		@Override
		public Object visit(IFunction<?> function, Object data) {
			for(IExpression<?> expr : function.getArguments()){
				expr.acceptVisitor(this, data);
			}
			if (function instanceof IBinaryOperator){
				IBinaryOperator<?> operator = (IBinaryOperator<?>)function;
				if (!operator.isAssociative() || !operator.isCommutative()){
					return function;
				}
				IExpression<?> arg0 = operator.getArgument(0);
				IExpression<?> arg1 = operator.getArgument(1);
				if (arg0.getClass() == function.getClass() && arg1.isConstant()){
					IBinaryOperator<?> inputOperator = (IBinaryOperator<?>)arg0;
					if(!inputOperator.getArgument(0).isConstant()) {
						IExpression<?> var = inputOperator.getArgument(0);
						inputOperator.setArgument(0, function.getArgument(1));
						function.setArgument(1, var);
					} else {
						IExpression<?> var = inputOperator.getArgument(1);
						inputOperator.setArgument(1, function.getArgument(1));
						function.setArgument(1, var);
					}
					inputOperator.acceptVisitor(this, null);
				}
			}
			return function;
		}
		
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
