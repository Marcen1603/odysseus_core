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

import de.uniol.inf.is.odysseus.core.mep.IMepConstant;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepExpressionVisitor;
import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.core.mep.IMepVariable;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.OrOperator;
import de.uniol.inf.is.odysseus.mep.intern.Constant;

/**
 * @author Jonas Jacobi
 */
public class ExpressionOptimizer {

	public static IMepExpression<?> simplifyExpression(IMepExpression<?> expression) {
		PreCalculateConstants simplificator = new PreCalculateConstants();
		expression = (IMepExpression<?>) expression.acceptVisitor(simplificator,
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
            Stack<IMepExpression<?>> expressionStack = new Stack<IMepExpression<?>>();
            expressionStack.push(expression.getMEPExpression());

            while (!expressionStack.isEmpty()) {
                IMepExpression<?> curExpression = expressionStack.pop();
                if (isAndOperator(curExpression)) {
                    expressionStack.push(curExpression.toFunction().getArgument(0));
                    expressionStack.push(curExpression.toFunction().getArgument(1));
                }
                else {
                    SDFExpression sdfExpression = new SDFExpression(curExpression.toString(), expression.getAttributeResolver(), MEP.getInstance());
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
            Stack<IMepExpression<?>> expressionStack = new Stack<IMepExpression<?>>();
            expressionStack.push(expression.getMEPExpression());

            while (!expressionStack.isEmpty()) {
                IMepExpression<?> curExpression = expressionStack.pop();
                if (isOrOperator(curExpression)) {
                    expressionStack.push(curExpression.toFunction().getArgument(0));
                    expressionStack.push(curExpression.toFunction().getArgument(1));
                }
                else {
                    SDFExpression sdfExpression = new SDFExpression(curExpression.toString(), expression.getAttributeResolver(), MEP.getInstance());
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
    public static boolean isAndOperator(final IMepExpression<?> expression) {
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
    public static boolean isOrOperator(final IMepExpression<?> expression) {
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
    public static boolean isNotOperator(final IMepExpression<?> expression) {
        return ((expression.isFunction()) && (expression.toFunction().getSymbol().equalsIgnoreCase("!")));
    }
    
	/**
	 * combines all constants in a nested call of the same (commutative and associative) operator to a single constant.
	 */
	private static class CombineConstants implements IMepExpressionVisitor {

		@Override
		public Object visit(IMepVariable variable, Object data) {
			return null;
		}

		@Override
		public Object visit(IMepConstant<?> constant, Object data) {
			return null;
		}

		@Override
		public Object visit(IMepFunction<?> function, Object data) {
			if (function instanceof IBinaryOperator) {
				IBinaryOperator<?> op = (IBinaryOperator<?>) function;
				if (op.isAssociative() && op.isCommutative()) {
					List<IMepExpression<?>> expressions = collectExpressions((IBinaryOperator<?>) function);
					Iterator<IMepExpression<?>> it = expressions.iterator();
					List<IMepExpression<?>> constants = new LinkedList<IMepExpression<?>>();
					while (it.hasNext()) {
						IMepExpression<?> curExpression = it.next();
						if (curExpression.isConstant()) {
							constants.add(curExpression);
							it.remove();
						}
					}
					if (!constants.isEmpty()) {
						IMepConstant<?> c = calculateConstant(function.getClass(),
								constants);
						function.setArgument(0, c);

						Iterator<IMepExpression<?>> exprIt = expressions
								.iterator();
						IMepFunction<?> curFunction = function;
						for (int i = 0; i < expressions.size() - 1; ++i) {
							try {
								IMepFunction<?> newFunc = createFunction(function
										.getClass());
								curFunction.setArgument(1, newFunc);
								newFunc.setArgument(0, exprIt.next());
								curFunction = newFunc;
							} catch (Exception e) {
								throw new RuntimeException(e);
							}
						}
						curFunction.setArgument(1, exprIt.next());

						for (IMepExpression<?> curExpression : expressions) {
							curExpression.acceptVisitor(this, null);
						}
					}
				}
			} else {
				for (IMepExpression<?> curArgument : function.getArguments()) {
					curArgument.acceptVisitor(this, null);
				}
			}
			return null;
		}

		private static IMepConstant<?> calculateConstant(
				@SuppressWarnings("rawtypes") Class<? extends IMepFunction> class1,
				List<IMepExpression<?>> constants) {
			if (constants.size() == 1) {
				return constants.get(0).toConstant();
			}
			try {
				final IMepFunction<?> function = createFunction(class1);
				Iterator<IMepExpression<?>> i = constants.iterator();
				IMepConstant<?> c = i.next().toConstant();
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

		private static IMepFunction<?> createFunction(
				@SuppressWarnings("rawtypes") Class<? extends IMepFunction> class1) {
			try {
				IMepFunction<?> function = class1.newInstance();
				function.setArguments(new IMepExpression[function.getArity()]);
				return function;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		private static List<IMepExpression<?>> collectExpressions(
				IBinaryOperator<?> function) {
			List<IMepExpression<?>> list = new LinkedList<IMepExpression<?>>();
			Stack<IMepExpression<?>> expressions = new Stack<IMepExpression<?>>();
			expressions.push(function);
			do {
				IMepExpression<?> curExpression = expressions.pop();
				if (curExpression.getClass() == function.getClass()) {
					for (IMepExpression<?> expr : curExpression.toFunction()
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
	private static class PreCalculateConstants implements IMepExpressionVisitor {

		@Override
		public IMepVariable visit(IMepVariable variable, Object data) {
			return variable;
		}

		@Override
		public IMepConstant<?> visit(IMepConstant<?> constant, Object data) {
			return constant;
		}

		@Override
		public IMepExpression<?> visit(IMepFunction<?> function, Object data) {
			boolean isAllInputsConstant = true;
			for (int i = 0; i < function.getArity(); ++i) {
				IMepExpression<?> iExpression = function.getArguments()[i];
				function.getArguments()[i] = (IMepExpression<?>) iExpression
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

		private static boolean isConstantPredicate(IMepExpression<?>[] iExpression, Boolean value) {
			return iExpression[0] instanceof Constant && iExpression[0].getValue() != null
					&& iExpression[0].getValue().equals(value)
					|| iExpression[1] instanceof Constant && iExpression[1].getValue() != null
							&& iExpression[1].getValue().equals(value);
		}

	}

}
