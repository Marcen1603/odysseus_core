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
package de.uniol.inf.is.odysseus.core.predicate.optimizer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import de.uniol.inf.is.odysseus.core.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.core.predicate.FalsePredicate;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.predicate.NotPredicate;
import de.uniol.inf.is.odysseus.core.predicate.OrPredicate;
import de.uniol.inf.is.odysseus.core.predicate.TruePredicate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public abstract class AbstractPredicateOptimizerRule<E extends IPredicate<?>> implements IPredicateOptimizerRule<E> {
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public IPredicate<?> executeRule(IPredicate<?> predicate) {
        if (isExecutable(predicate)) {
            return execute((E) predicate);
        }
        return predicate;
    }

    /**
     * Creates a conjunctive split of the expression.
     * 
     * @param expression
     *            The expression
     * @return A conjunctive split of the expression
     */
    //@SuppressWarnings("static-method")
    protected Set<IPredicate<?>> getConjunctiveSplit(IPredicate<?> expression) {
        Set<IPredicate<?>> result = new HashSet<>();
        Stack<IPredicate<?>> expressionStack = new Stack<>();
        expressionStack.push(expression);
        while (!expressionStack.isEmpty()) {
            IPredicate<?> curExpression = expressionStack.pop();
            if (curExpression instanceof AndPredicate) {
                expressionStack.push(((AndPredicate<?>) curExpression).getLeft());
                expressionStack.push(((AndPredicate<?>) curExpression).getRight());
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
    protected Set<IPredicate<?>> getDisjunctiveSplit(IPredicate<?> expression) {
        Set<IPredicate<?>> result = new HashSet<>();
        Stack<IPredicate<?>> expressionStack = new Stack<>();
        expressionStack.push(expression);
        while (!expressionStack.isEmpty()) {
            IPredicate<?> curExpression = expressionStack.pop();
            if (curExpression instanceof OrPredicate) {
                expressionStack.push(((OrPredicate<?>) curExpression).getLeft());
                expressionStack.push(((OrPredicate<?>) curExpression).getRight());
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
    protected IPredicate<?> disjunction(List<IPredicate<?>> expression) {
        return disjunction(expression, 0);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private IPredicate<?> disjunction(List<IPredicate<?>> expression, int index) {
        if (expression.size() == 0) {
            return FalsePredicate.getInstance();
        }
        if (index + 1 == expression.size()) {
            return expression.get(index);
        }
        OrPredicate or = new OrPredicate();
        or.setLeft(expression.get(index));
        or.setRight(disjunction(expression, index + 1));
        return or;
    }

    /**
     * Combines the given list of expressions with logical And.
     * 
     * @param expression
     *            The list of expressions
     * @return A conjunction of the given expressions
     */
    protected IPredicate<?> conjunction(List<IPredicate<?>> expression) {
        return conjunction(expression, 0);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private IPredicate<?> conjunction(List<IPredicate<?>> expression, int index) {
        if (expression.size() == 0) {
            return TruePredicate.getInstance();
        }
        if (index + 1 == expression.size()) {
            return expression.get(index);
        }
        AndPredicate and = new AndPredicate();
        and.setLeft(expression.get(index));
        and.setRight(conjunction(expression, index + 1));
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
    protected boolean isBooleanOperator(IPredicate<?> expression) {
        return ((expression instanceof NotPredicate) || (expression instanceof OrPredicate) || (expression instanceof AndPredicate));
    }
}
