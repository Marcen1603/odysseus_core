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
 * Rule to transform a given expression into the conjunctive normal form (CNF)
 * Based on
 * http://csliu.com/2009/05/convert-arbitrary-bool-expression-into-conjunctive
 * -normal-form/
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id: ConjunctiveNormalFormRule.java | ConjunctiveNormalFormRule.java
 *          | ConjunctiveNormalFormRule.java $
 *
 */
public class ConjunctiveNormalFormRule extends AbstractExpressionOptimizerRule<IMepExpression<?>> {
    private final DeMorganRule deMorganRule = new DeMorganRule();

    /**
     * {@inheritDoc}
     */
    @Override
    public IMepExpression<?> execute(IMepExpression<?> expression) {
        return toConjunctiveNormalForm(expression);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExecutable(IMepExpression<?> expression) {
        return true;
    }

    private IMepExpression<?> toConjunctiveNormalForm(IMepExpression<?> expression) {
        if (!isBooleanOperator(expression)) {
            return expression;
        }
        if (expression instanceof NotOperator) {
            IMepExpression<?> leaf = expression.toFunction().getArgument(0);
            if (!isBooleanOperator(leaf)) {
                return expression;
            }
            if (leaf instanceof NotOperator) {
                return toConjunctiveNormalForm(leaf.toFunction().getArgument(0));
            }
            IMepExpression<?> deMorgan = this.deMorganRule.executeRule(expression);
            return toConjunctiveNormalForm(deMorgan);
        }

        IMepExpression<?> left = expression.toFunction().getArgument(0);
        if (left != null) {
            left = toConjunctiveNormalForm(left);
        }
        IMepExpression<?> right = expression.toFunction().getArgument(1);
        if (right != null) {
            right = toConjunctiveNormalForm(right);
        }
        if (expression instanceof AndOperator) {
            AndOperator and = new AndOperator();
            and.setArguments(new IMepExpression<?>[] { left, right });
            return and;
        }
        if (expression instanceof OrOperator) {
            // (A ∧ B) ∨ (C ∧ D) => (A ∨ C) ∧ (B ∨ C) ∧ (A ∨ D) ∧ (B ∨ D)
            if ((left != null) && (left instanceof AndOperator) && (right != null) && (right instanceof AndOperator)) {
                IMepFunction<?> tmpLeft = left.toFunction();
                IMepFunction<?> tmpRight = right.toFunction();

                OrOperator leftLeftNode = new OrOperator();
                leftLeftNode.setArguments(new IMepExpression<?>[] { tmpLeft.toFunction().getArgument(0), tmpRight.toFunction().getArgument(0) });
                OrOperator leftRightNode = new OrOperator();
                leftRightNode.setArguments(new IMepExpression<?>[] { tmpLeft.toFunction().getArgument(1), tmpRight.toFunction().getArgument(0) });
                left = new AndOperator();
                left.toFunction().setArguments(new IMepExpression<?>[] { leftLeftNode, leftRightNode });

                OrOperator rightLeftNode = new OrOperator();
                rightLeftNode.setArguments(new IMepExpression<?>[] { tmpLeft.toFunction().getArgument(0), tmpRight.toFunction().getArgument(1) });
                OrOperator rightRightNode = new OrOperator();
                rightRightNode.setArguments(new IMepExpression<?>[] { tmpLeft.toFunction().getArgument(1), tmpRight.toFunction().getArgument(1) });
                right = new AndOperator();
                right.toFunction().setArguments(new IMepExpression<?>[] { rightLeftNode, rightRightNode });

                AndOperator and = new AndOperator();
                and.setArguments(new IMepExpression<?>[] { toConjunctiveNormalForm(left), toConjunctiveNormalForm(right) });
                return and;

            }
            // ? ∨ (C ∧ D) => (? ∨ D) ∧ (? ∨ C)
            if ((right != null) && (right instanceof AndOperator)) {
                IMepFunction<?> tmpRight = right.toFunction();
                IMepExpression<?> tmpLeft = left;

                left = new OrOperator();
                left.toFunction().setArguments(new IMepExpression<?>[] { tmpLeft, tmpRight.toFunction().getArgument(1) });

                right = new OrOperator();
                right.toFunction().setArguments(new IMepExpression<?>[] { tmpLeft, tmpRight.toFunction().getArgument(0) });

                AndOperator and = new AndOperator();
                and.setArguments(new IMepExpression<?>[] { toConjunctiveNormalForm(left), toConjunctiveNormalForm(right) });
                return and;
            }
            // (A ∧ B) ∨ ? => (A ∨ ?) ∧ (B ∨ ?)
            if ((left != null) && (left instanceof AndOperator)) {
                IMepFunction<?> tmpLeft = left.toFunction();
                IMepExpression<?> tmpRight = right;

                left = new OrOperator();
                left.toFunction().setArguments(new IMepExpression<?>[] { tmpLeft.toFunction().getArgument(0), tmpRight });

                right = new OrOperator();
                right.toFunction().setArguments(new IMepExpression<?>[] { tmpLeft.toFunction().getArgument(1), tmpRight });

                AndOperator and = new AndOperator();
                and.setArguments(new IMepExpression<?>[] { toConjunctiveNormalForm(left), toConjunctiveNormalForm(right) });
                return and;

            }
            OrOperator or = new OrOperator();
            or.setArguments(new IMepExpression<?>[] { left, right });
            return or;
        }
        return expression;
    }

}
