/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mep.optimizer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.NotOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.OrOperator;
import de.uniol.inf.is.odysseus.mep.intern.Constant;

/**
 * Abstract expression optimizer rule.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id: AbstractExpressionOptimizerRule.java |
 *          AbstractExpressionOptimizerRule.java |
 *          AbstractExpressionOptimizerRule.java $
 * @param <E>
 *
 */
public abstract class AbstractExpressionOptimizerRule<E extends IMepExpression<?>> implements IExpressionOptimizerRule<E> {
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public IMepExpression<?> executeRule(IMepExpression<?> expression) {
        if (isExecutable(expression)) {
            return execute((E) expression);
        }
        return expression;
    }

    /**
     * Creates a conjunctive split of the expression.
     * 
     * @param expression
     *            The expression
     * @return A conjunctive split of the expression
     */
    //@SuppressWarnings("static-method")
    protected Set<IMepExpression<?>> getConjunctiveSplit(IMepExpression<?> expression) {
        Set<IMepExpression<?>> result = new HashSet<>();
        Stack<IMepExpression<?>> expressionStack = new Stack<>();
        expressionStack.push(expression);
        while (!expressionStack.isEmpty()) {
            IMepExpression<?> curExpression = expressionStack.pop();
            if (curExpression instanceof AndOperator) {
                expressionStack.push(curExpression.toFunction().getArgument(0));
                expressionStack.push(curExpression.toFunction().getArgument(1));
            }
            else {
                result.add(curExpression);
            }
        }
        return result;
    }

    /**
     * Creates a disjunctive split of the expression.
     * 
     * @param expression
     *            The expression
     * @return A disjunctive split of the expression
     */
    //@SuppressWarnings("static-method")
    protected Set<IMepExpression<?>> getDisjunctiveSplit(IMepExpression<?> expression) {
        Set<IMepExpression<?>> result = new HashSet<>();
        Stack<IMepExpression<?>> expressionStack = new Stack<>();
        expressionStack.push(expression);
        while (!expressionStack.isEmpty()) {
            IMepExpression<?> curExpression = expressionStack.pop();
            if (curExpression instanceof OrOperator) {
                expressionStack.push(curExpression.toFunction().getArgument(0));
                expressionStack.push(curExpression.toFunction().getArgument(1));
            }
            else {
                result.add(curExpression);
            }
        }
        return result;
    }

    /**
     * Combines the given list of expressions with logical Or.
     * 
     * @param expression
     *            The list of expressions
     * @return A disjunction of the given expressions
     */
    protected IMepExpression<?> disjunction(List<IMepExpression<?>> expression) {
        return disjunction(expression, 0);
    }

    private IMepExpression<?> disjunction(List<IMepExpression<?>> expression, int index) {
        if (expression.size() == 0) {
            return new Constant<>(Boolean.FALSE, SDFDatatype.BOOLEAN);
        }
        if (index + 1 == expression.size()) {
            return expression.get(index);
        }
        OrOperator or = new OrOperator();
        or.setArguments(new IMepExpression[] { expression.get(index), disjunction(expression, index + 1) });
        return or;
    }

    /**
     * Combines the given list of expressions with logical And.
     * 
     * @param expression
     *            The list of expressions
     * @return A conjunction of the given expressions
     */
    protected IMepExpression<?> conjunction(List<IMepExpression<?>> expression) {
        return conjunction(expression, 0);
    }

    private IMepExpression<?> conjunction(List<IMepExpression<?>> expression, int index) {
        if (expression.size() == 0) {
            return new Constant<>(Boolean.TRUE, SDFDatatype.BOOLEAN);
        }
        if (index + 1 == expression.size()) {
            return expression.get(index);
        }
        AndOperator and = new AndOperator();
        and.setArguments(new IMepExpression[] { expression.get(index), conjunction(expression, index + 1) });
        return and;
    }

    /**
     * Checks whether the given expression is a boolean operator.
     * 
     * @param expression
     *            The expression
     * @return <code>true</code> if the expression is a {@link NotOperator},
     *         {@link OrOperator}, or {@link AndOperator}.
     */
    protected boolean isBooleanOperator(IMepExpression<?> expression) {
        if (expression.isFunction()) {
            return ((expression instanceof NotOperator) || (expression instanceof OrOperator) || (expression instanceof AndOperator));
        }
        return false;
    }
}
