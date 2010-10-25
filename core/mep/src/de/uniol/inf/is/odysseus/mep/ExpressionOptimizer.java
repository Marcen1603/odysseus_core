package de.uniol.inf.is.odysseus.mep;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import de.uniol.inf.is.odysseus.mep.functions.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.OrOperator;

/**
 * @author Jonas Jacobi
 */
public class ExpressionOptimizer {

	public static IExpression<?> simplifyExpression(IExpression<?> expression) {
		PreCalculateConstants simplificator = new PreCalculateConstants();
		expression = (IExpression<?>) expression.acceptVisitor(simplificator,
				null);
		CombineConstants constants = new CombineConstants();
		expression.acceptVisitor(constants, null);

		return expression;
	}

	/**
	 * combines all constants in a nested call of the same (commutative and associative) operator to a single constant.
	 */
	private static class CombineConstants implements IExpressionVisitor {

		@Override
		public Object visit(Variable variable, Object data) {
			return null;
		}

		@Override
		public Object visit(Constant<?> constant, Object data) {
			return null;
		}

		@Override
		public Object visit(IFunction<?> function, Object data) {
			if (function instanceof IBinaryOperator) {
				IBinaryOperator<?> op = (IBinaryOperator<?>) function;
				if (op.isAssociative() && op.isCommutative()) {
					List<IExpression<?>> expressions = collectExpressions((IBinaryOperator<?>) function);
					Iterator<IExpression<?>> it = expressions.iterator();
					List<IExpression<?>> constants = new LinkedList<IExpression<?>>();
					while (it.hasNext()) {
						IExpression<?> curExpression = it.next();
						if (curExpression.isConstant()) {
							constants.add(curExpression);
							it.remove();
						}
					}
					if (!constants.isEmpty()) {
						Constant<?> c = calculateConstant(function.getClass(),
								constants);
						function.setArgument(0, c);

						Iterator<IExpression<?>> exprIt = expressions
								.iterator();
						IFunction<?> curFunction = function;
						for (int i = 0; i < expressions.size() - 1; ++i) {
							try {
								IFunction<?> newFunc = createFunction(function
										.getClass());
								curFunction.setArgument(1, newFunc);
								newFunc.setArgument(0, exprIt.next());
								curFunction = newFunc;
							} catch (Exception e) {
								throw new RuntimeException(e);
							}
						}
						curFunction.setArgument(1, exprIt.next());

						for (IExpression<?> curExpression : expressions) {
							curExpression.acceptVisitor(this, null);
						}
					}
				}
			} else {
				for (IExpression<?> curArgument : function.getArguments()) {
					curArgument.acceptVisitor(this, null);
				}
			}
			return null;
		}

		private Constant<?> calculateConstant(
				@SuppressWarnings("rawtypes") Class<? extends IFunction> class1,
				List<IExpression<?>> constants) {
			if (constants.size() == 1) {
				return constants.get(0).toConstant();
			}
			try {
				final IFunction<?> function = createFunction(class1);
				Iterator<IExpression<?>> i = constants.iterator();
				Constant<?> c = i.next().toConstant();
				function.setArgument(0, c);
				while (i.hasNext()) {
					function.setArgument(1, i.next());
					c = new Constant<Object>(function.getValue());
					function.setArgument(0, c);
				}
				return c;
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		private IFunction<?> createFunction(
				@SuppressWarnings("rawtypes") Class<? extends IFunction> class1) {
			try {
				IFunction<?> function = class1.newInstance();
				function.setArguments(new IExpression[function.getArity()]);
				return function;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		private List<IExpression<?>> collectExpressions(
				IBinaryOperator<?> function) {
			List<IExpression<?>> list = new LinkedList<IExpression<?>>();
			Stack<IExpression<?>> expressions = new Stack<IExpression<?>>();
			expressions.push(function);
			do {
				IExpression<?> curExpression = expressions.pop();
				if (curExpression.getClass() == function.getClass()) {
					for (IExpression<?> expr : curExpression.toFunction()
							.getArguments()) {
						expressions.push(expr);
					}
				} else {
					list.add(curExpression);
				}
			} while (!expressions.isEmpty());

			return list;
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
