/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.mep;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import de.uniol.inf.is.odysseus.core.mep.Constant;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.IExpressionVisitor;
import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.mep.Variable;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.OrOperator;

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
     * Conjunctive split the given expression into a set of expressions.
     * 
     * @param expression
     *            The expression
     * 
     * @return A set of expressions
     */
    public static Collection<SDFExpression> conjunctiveSplitExpression(final SDFExpression expression) {
        Set<SDFExpression> result = new TreeSet<SDFExpression>(new Comparator<SDFExpression>() {

            @Override
            public int compare(SDFExpression o1, SDFExpression o2) {
                return Integer.compare(o1.getAllAttributes().size(), o2.getAllAttributes().size());
            }
        });
        if (isAndOperator(expression.getMEPExpression())) {
            Stack<IExpression<?>> expressionStack = new Stack<IExpression<?>>();
            expressionStack.push(expression.getMEPExpression());

            while (!expressionStack.isEmpty()) {
                IExpression<?> curExpression = expressionStack.pop();
                if (isAndOperator(curExpression)) {
                    expressionStack.push(curExpression.toFunction().getArgument(0));
                    expressionStack.push(curExpression.toFunction().getArgument(1));
                }
                else {
                    SDFExpression sdfExpression = new SDFExpression(curExpression.toString(), MEP.getInstance());
                    result.add(sdfExpression);
                }
            }
            return result;

        }
        result.add(expression);
        return result;
    }

    /**
     * Disjunctive split the given expression into a set of expressions.
     * 
     * @param expression
     *            The expression
     * 
     * @return A set of expressions
     */
    public static Collection<SDFExpression> disjunctiveSplitExpression(final SDFExpression expression) {
        Set<SDFExpression> result = new TreeSet<SDFExpression>(new Comparator<SDFExpression>() {

            @Override
            public int compare(SDFExpression o1, SDFExpression o2) {
                return Integer.compare(o1.getAllAttributes().size(), o2.getAllAttributes().size());
            }
        });
        if (isOrOperator(expression.getMEPExpression())) {
            Stack<IExpression<?>> expressionStack = new Stack<IExpression<?>>();
            expressionStack.push(expression.getMEPExpression());

            while (!expressionStack.isEmpty()) {
                IExpression<?> curExpression = expressionStack.pop();
                if (isOrOperator(curExpression)) {
                    expressionStack.push(curExpression.toFunction().getArgument(0));
                    expressionStack.push(curExpression.toFunction().getArgument(1));
                }
                else {
                    SDFExpression sdfExpression = new SDFExpression(curExpression.toString(), MEP.getInstance());
                    result.add(sdfExpression);
                }
            }
            return result;

        }
        result.add(expression);
        return result;
    }
    
    /**
     * Checks whether the given expression is an AND operator.
     * 
     * A AND B AND C ...
     * 
     * @param expression
     *            The expression
     * @return <code>true</code> if the given expression is an AND operator
     */
    public static boolean isAndOperator(final IExpression<?> expression) {
        return ((expression.isFunction()) && (expression.toFunction().getSymbol().equalsIgnoreCase("&&")));
    }

    /**
     * Checks whether the given expression is an OR operator.
     * 
     * A OR B OR C ...
     * 
     * @param expression
     *            The expression
     * @return <code>true</code> if the given expression is an OR operator
     */
    public static boolean isOrOperator(final IExpression<?> expression) {
        return ((expression.isFunction()) && (expression.toFunction().getSymbol().equalsIgnoreCase("||")));
    }

    /**
     * Checks whether the given expression is an NOT operator.
     * 
     * NOT A ...
     * 
     * @param expression
     *            The expression
     * @return <code>true</code> if the given expression is an NOT operator
     */
    public static boolean isNotOperator(final IExpression<?> expression) {
        return ((expression.isFunction()) && (expression.toFunction().getSymbol().equalsIgnoreCase("!")));
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

		private static Constant<?> calculateConstant(
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
					c = new Constant<Object>(function.getValue(), function.getReturnType());
					function.setArgument(0, c);
				}
				return c;
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		private static IFunction<?> createFunction(
				@SuppressWarnings("rawtypes") Class<? extends IFunction> class1) {
			try {
				IFunction<?> function = class1.newInstance();
				function.setArguments(new IExpression[function.getArity()]);
				return function;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		private static List<IExpression<?>> collectExpressions(
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
				if (!(function.getArguments()[i].isConstant())) {
					isAllInputsConstant = false;
				}
			}

			if (isAllInputsConstant && function.optimizeConstantParameter()) {
				return new Constant<Object>(function.getValue(), function.getReturnType());
			}

			if (function.getClass() == AndOperator.class) {
				return isConstantPredicate(function.getArguments(), false) ? new Constant<Boolean>(
						false, SDFDatatype.BOOLEAN) : function;
			}
            if (function.getClass() == OrOperator.class) {
            	return isConstantPredicate(function.getArguments(), true) ? new Constant<Boolean>(
            			true, SDFDatatype.BOOLEAN) : function;
            }
            return function;
		}

		private static boolean isConstantPredicate(IExpression<?>[] iExpression, Boolean value) {
			return iExpression[0] instanceof Constant && iExpression[0].getValue() != null
					&& iExpression[0].getValue().equals(value)
					|| iExpression[1] instanceof Constant && iExpression[1].getValue() != null
							&& iExpression[1].getValue().equals(value);
		}

	}

}
