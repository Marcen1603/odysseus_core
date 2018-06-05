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

import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.NotOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.OrOperator;

/**
 * Rule to transform a given expression into the disjunctive normal form (DNF)
 * Based on
 * http://csliu.com/2009/05/convert-arbitrary-bool-expression-into-conjunctive
 * -normal-form/
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id: DisjunctiveNormalFormRule.java | DisjunctiveNormalFormRule.java
 *          | DisjunctiveNormalFormRule.java $
 *
 */
public class DisjunctiveNormalFormRule extends AbstractExpressionOptimizerRule<IMepExpression<?>> {
    private final DeMorganRule deMorganRule = new DeMorganRule();

    /**
     * {@inheritDoc}
     */
    @Override
    public IMepExpression<?> execute(IMepExpression<?> expression) {
        return toDisjunctiveNormalForm(expression);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExecutable(IMepExpression<?> expression) {
        return true;
    }

    private IMepExpression<?> toDisjunctiveNormalForm(IMepExpression<?> expression) {
        if (!isBooleanOperator(expression)) {
            return expression;
        }
        if (expression instanceof NotOperator) {
            IMepExpression<?> leaf = expression.toFunction().getArgument(0);
            if (!isBooleanOperator(leaf)) {
                return expression;
            }
            if (leaf instanceof NotOperator) {
                return toDisjunctiveNormalForm(leaf.toFunction().getArgument(0));
            }
            IMepExpression<?> deMorgan = this.deMorganRule.executeRule(expression);
            return toDisjunctiveNormalForm(deMorgan);
        }

        IMepExpression<?> left = expression.toFunction().getArgument(0);
        if (left != null) {
            left = toDisjunctiveNormalForm(left);
        }
        IMepExpression<?> right = expression.toFunction().getArgument(1);
        if (right != null) {
            right = toDisjunctiveNormalForm(right);
        }
        if (expression instanceof OrOperator) {
            OrOperator or = new OrOperator();
            or.setArguments(new IMepExpression<?>[] { left, right });
            return or;
        }
        if (expression instanceof AndOperator) {
            // (A v B) ∧ (C v D) => (A ∧ C) v (B ∧ C) v (A ∧ D) v (B ∧ D)
            if ((left != null) && (left instanceof OrOperator) && (right != null) && (right instanceof OrOperator)) {
                IMepFunction<?> tmpLeft = left.toFunction();
                IMepFunction<?> tmpRight = right.toFunction();

                AndOperator leftLeftNode = new AndOperator();
                leftLeftNode.setArguments(new IMepExpression<?>[] { tmpLeft.toFunction().getArgument(0), tmpRight.toFunction().getArgument(0) });
                AndOperator leftRightNode = new AndOperator();
                leftRightNode.setArguments(new IMepExpression<?>[] { tmpLeft.toFunction().getArgument(1), tmpRight.toFunction().getArgument(0) });
                left = new OrOperator();
                left.toFunction().setArguments(new IMepExpression<?>[] { leftLeftNode, leftRightNode });

                AndOperator rightLeftNode = new AndOperator();
                rightLeftNode.setArguments(new IMepExpression<?>[] { tmpLeft.toFunction().getArgument(0), tmpRight.toFunction().getArgument(1) });
                AndOperator rightRightNode = new AndOperator();
                rightRightNode.setArguments(new IMepExpression<?>[] { tmpLeft.toFunction().getArgument(1), tmpRight.toFunction().getArgument(1) });
                right = new OrOperator();
                right.toFunction().setArguments(new IMepExpression<?>[] { rightLeftNode, rightRightNode });

                OrOperator or = new OrOperator();
                or.setArguments(new IMepExpression<?>[] { toDisjunctiveNormalForm(left), toDisjunctiveNormalForm(right) });
                return or;

            }
            // ? ∧ (C v D) => (? ∧ D) v (? ∧ C)
            if ((right != null) && (right instanceof OrOperator)) {
                IMepFunction<?> tmpRight = right.toFunction();
                IMepExpression<?> tmpLeft = left;

                left = new AndOperator();
                left.toFunction().setArguments(new IMepExpression<?>[] { tmpLeft, tmpRight.toFunction().getArgument(1) });

                right = new AndOperator();
                right.toFunction().setArguments(new IMepExpression<?>[] { tmpLeft, tmpRight.toFunction().getArgument(0) });

                OrOperator or = new OrOperator();
                or.setArguments(new IMepExpression<?>[] { toDisjunctiveNormalForm(left), toDisjunctiveNormalForm(right) });
                return or;
            }
            // (A v B) ∧ ? => (A ∧ ?) v (B ∧ ?)
            if ((left != null) && (left instanceof OrOperator)) {
                IMepFunction<?> tmpLeft = left.toFunction();
                IMepExpression<?> tmpRight = right;

                left = new AndOperator();
                left.toFunction().setArguments(new IMepExpression<?>[] { tmpLeft.toFunction().getArgument(0), tmpRight });

                right = new AndOperator();
                right.toFunction().setArguments(new IMepExpression<?>[] { tmpLeft.toFunction().getArgument(1), tmpRight });

                OrOperator or = new OrOperator();
                or.setArguments(new IMepExpression<?>[] { toDisjunctiveNormalForm(left), toDisjunctiveNormalForm(right) });
                return or;

            }
            AndOperator and = new AndOperator();
            and.setArguments(new IMepExpression<?>[] { left, right });
            return and;
        }
        return expression;
    }
}
